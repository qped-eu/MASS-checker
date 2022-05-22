package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.utils.compiler.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;

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
public class SyntaxChecker {

    private String stringAnswer;

    private String targetProject;

    @Deprecated(forRemoval = true)
    private CheckLevel level;

    public SyntaxCheckReport check() {
        SyntaxCheckReport.SyntaxCheckReportBuilder resultBuilder = SyntaxCheckReport.builder();
        Compiler compiler = Compiler.builder().build();
        boolean compileResult;

        if (stringAnswer != null && !stringAnswer.equals("")) {
            compileResult = compiler.compile(stringAnswer);
            resultBuilder.compiledSourceType(CompiledSourceType.STRING);
            resultBuilder.codeAsString(compiler.getFullSourceCode());
        } else {
            compiler.setTargetProjectOrClassPath(targetProject);
            compileResult = compiler.compile(stringAnswer);
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

        return codeSplitByLine[line -1];
    }

    private List<SyntaxError> analyseDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        List<SyntaxError> syntaxErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            String errorTrigger = getErrorTrigger(diagnostic);
            syntaxErrors.add(
                    SyntaxError.builder()
                            .errorCode(diagnostic.getCode())
                            .fileName(diagnostic.getSource().getName())
                            .errorMessage(diagnostic.getMessage(Locale.GERMAN))
                            .startPos(diagnostic.getStartPosition())
                            .endPos(diagnostic.getEndPosition())
                            .line(diagnostic.getLineNumber())
                            .errorTrigger(errorTrigger)
                            .columnNumber(diagnostic.getColumnNumber())
                            .build()
            );

        }
        return syntaxErrors;
    }

    public static void main(String[] args) throws IOException {
        String code = "private void print() {\n" +
            "        System.out.println();\n" +
                    "    }";

        String[] codeLines = code.split("\n");


        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();

        int line = (int) syntaxChecker.check().getSyntaxErrors().get(0).getLine();
        System.out.println(codeLines[line - 1].trim());

        String lineCode = codeLines[line - 1].trim();

        int column = (int) syntaxChecker.check().getSyntaxErrors().get(0).getColumnNumber();


        System.out.println(
                "custom Feedback \n" +
                        syntaxChecker.check().getSyntaxErrors().get(0).getErrorMessage() + "\n" +
                        "at line: " + syntaxChecker.check().getSyntaxErrors().get(0).getColumnNumber() + "\n" +
                        "for example:  int var = value;"
        );
//        System.out.println(syntaxChecker.check().getSyntaxErrors().get(0).getErrorSourceCode());
    }

}
