package eu.qped.java.checkers.syntax.analyser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.QpedQfFilesUtility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private File solutionRoot;
    private CheckLevel level = CheckLevel.BEGINNER;
    
    /**
     * @return {@link SyntaxAnalysisReport} after checking an answer in form of code or class or project. <br/>
     * A {@link SyntaxAnalysisReport} contains beside the errors if occurs relevant information like path of the answer.
     */
    public SyntaxAnalysisReport check() {
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        boolean compileResult = compile(diagnosticsCollector);
        
        SyntaxAnalysisReport.SyntaxAnalysisReportBuilder resultBuilder = SyntaxAnalysisReport.builder();
        
        resultBuilder.compiledSourceType(CompiledSourceType.PROJECT);
        resultBuilder.isCompilable(compileResult);

        List<SyntaxError> collectedErrors = new ArrayList<>();
        collectedErrors = analyseDiagnostics(diagnosticsCollector.getDiagnostics());
        
        resultBuilder.syntaxErrors(collectedErrors);
        resultBuilder.path(solutionRoot);
        return resultBuilder.build();
    }


    private boolean compile(DiagnosticCollector<JavaFileObject> diagnosticsCollector) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, Locale.GERMANY, Charset.defaultCharset());
        
        List<File> files = QpedQfFilesUtility.filesWithExtension(solutionRoot, "java");
        if (files.size() == 0) {
            return false;
        }

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);

        // create compiler options
        List<String> options = new ArrayList<>();
        
        options.add("-Xlint");   // Enable all recommended warnings. In this release, enabling all available warnings is recommended.
        options.add("-g");       // Generate all debugging information, including local variables. By default, only line number and source file information is generated.

        // set destination and source directories
        options.add("-d");
        options.add(solutionRoot.getAbsolutePath());
        options.add("-s");
        options.add(solutionRoot.getAbsolutePath());
        
        // set class path (inherited from the class path of the JVM running MASS)
        if (System.getProperty("maven.compile.classpath") != null) {
            // requires that the corresponding system property is set in the Maven pom
        	options.addAll(Arrays.asList("-classpath",System.getProperty("maven.compile.classpath")));
        } else {
        	// if the checker is not run from Maven (e.g., during testing), inherit classpath from current JVM
        	// this is only needed for local testing directly from IDE
            options.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));
        }

        // perform compilation
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnosticsCollector,
                options,
                null,
                compilationUnits
        );
        boolean result = task.call();

        return result;
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
        List<SyntaxError> syntaxErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
        	// the source did not originate from a source, so it is not associated with the student solution
        	if  (diagnostic.getSource() == null)
        		continue;
        	// For example, if the equals method is overridden but not hashCode that would be reported
        	// as warning, but it is usually not clear to first-year students.
        	if (level == CheckLevel.BEGINNER && diagnostic.getKind() == Kind.WARNING)
        		continue;
            String errorTrigger = getErrorTrigger(diagnostic);
            syntaxErrors.add(
                    SyntaxError.builder()
                            .errorCode(diagnostic.getCode())
                            .fileName(diagnostic.getSource().getName())
                            .errorMessage(diagnostic.getMessage(Locale.GERMAN)) // TODO why is GERMAN hard coded?
                            .startPos(diagnostic.getStartPosition())
                            .endPos(diagnostic.getEndPosition())
                            .line(diagnostic.getLineNumber()) // TODO correct with line offset for generated answer file
                            .errorTrigger(errorTrigger)
                            .columnNumber(diagnostic.getColumnNumber())
                            .build()
            );

        }
        return syntaxErrors;
    }

}
