package eu.qped.java.checkers.syntax.analyser;

import eu.qped.java.utils.compiler.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * checker for the syntax problems in java code.
 *
 * @author Mayar Hamdhash hamdash@students.uni-marburg.de
 * @version 2.0
 * @since 17.04.2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxErrorAnalyser {

    private String stringAnswer;

    private String targetProject;

    private String classFilesDestination;

    private Compiler compiler;


    /**
     * @return {@link SyntaxAnalysisReport} after checking an answer in form of code or class or project. <br/>
     * A {@link SyntaxAnalysisReport} contains beside the errors if occurs relevant information like path of the answer.
     */
    public SyntaxAnalysisReport check() {
        SyntaxAnalysisReport.SyntaxAnalysisReportBuilder resultBuilder = SyntaxAnalysisReport.builder();

        if (compiler == null) {
            compiler = Compiler.builder().build();
        }
        compiler.addClassFilesDestination("");
        boolean compileResult;

        compiler.setCompiledStringResourcePath("src/main/resources/exam-results/src");
        if (classFilesDestination != null && !"".equals(classFilesDestination)) {
            compiler.addClassFilesDestination(classFilesDestination);
        }

        if (stringAnswer != null && !stringAnswer.equals("")) {
            compileResult = compiler.compileFromString(stringAnswer);
            resultBuilder.compiledSourceType(CompiledSourceType.STRING);
            resultBuilder.codeAsString(compiler.getFullSourceCode());
        } else {
            compileResult = compiler.compileFromProject(targetProject);
            resultBuilder.compiledSourceType(CompiledSourceType.PROJECT);
        }
        resultBuilder.isCompilable(compileResult);

        List<Diagnostic<? extends JavaFileObject>> diagnostics = compiler.getCollectedDiagnostics();
        List<SyntaxError> collectedErrors = new ArrayList<>();
        if (diagnostics != null) {
            collectedErrors = analyseDiagnostics(diagnostics);
        }
        resultBuilder.syntaxErrors(collectedErrors);
        resultBuilder.path(compiler.getTargetProjectOrClassPath());
        return resultBuilder.build();
    }


    private String getErrorTrigger(Diagnostic<? extends JavaFileObject> diagnostic) {

        var errorCode = "";

        try {
        	if  (diagnostic.getSource() == null)
        		return "";
        	else
        		errorCode = diagnostic.getSource().getCharContent(false).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return errorCode;
        }
        String[] codeSplitByLine = errorCode.split("\n");

        if (codeSplitByLine.length < 1) {
            return "";
        }

        int line = (int) diagnostic.getLineNumber();

        return codeSplitByLine[line - 1];
    }

    private List<SyntaxError> analyseDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {

        //FIXME
        /*
           (stringAnswer != null && !stringAnswer.equals("") && Arrays.stream(new String[]{"class" , "interface"}).anyMatch(stringAnswer::contains))?
                                    diagnostic.getLineNumber() - 3
         */
        List<SyntaxError> syntaxErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
        	if  (diagnostic.getSource() == null)
        		continue;
            String errorTrigger = getErrorTrigger(diagnostic);
            syntaxErrors.add(
                    SyntaxError.builder()
                            .errorCode(diagnostic.getCode())
                            .fileName(diagnostic.getSource().getName())
                            .errorMessage(diagnostic.getMessage(Locale.GERMAN))
                            .startPos(diagnostic.getStartPosition())
                            .endPos(diagnostic.getEndPosition())
                            .line(
                                    (stringAnswer != null && !(stringAnswer.contains("class") || stringAnswer.contains("interface"))) ?
                                            diagnostic.getLineNumber() - 3 : diagnostic.getLineNumber()
                            )
                            .errorTrigger(errorTrigger)
                            .columnNumber(diagnostic.getColumnNumber())
                            .build()
            );

        }
        return syntaxErrors;
    }


    public static void main(String[] args) {
        SyntaxErrorAnalyser checker = SyntaxErrorAnalyser.builder().targetProject("tmp/exam-results62b874f9fb9d582f0b08d371").build();
        var report = checker.check();

        System.out.println("path: " + report.getPath());

        System.out.println(report.getSyntaxErrors().size());

    }


}
