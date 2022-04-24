package eu.qped.java.utils.compiler;


import eu.qped.java.utils.ExtractJavaFilesFromDirectory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Java compiler for source code as String.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compiler {

    private static final String DEFAULT_CLASS_PATH = "TestClass.java";

    private List<Diagnostic<? extends JavaFileObject>> collectedDiagnostics;
    private String fullSourceCode;
    private String targetProjectPath;

    public boolean compile(String answer){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, Locale.GERMANY, Charset.defaultCharset());
        List<File> files = new ArrayList<>();

        if (answer != null && !answer.equals("")) {
            createJavaClass(writeCodeAsClass(answer));
            files.add(new File(DEFAULT_CLASS_PATH));
        } else {
            ExtractJavaFilesFromDirectory extractJavaFilesFromDirectory = ExtractJavaFilesFromDirectory.builder().dirPath(targetProjectPath).build();
            files = extractJavaFilesFromDirectory.filesWithJavaExtension();
            if (files.size() == 0){
                return false;
            }
        }
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
        boolean result = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits).call();
        this.setCollectedDiagnostics(diagnosticsCollector.getDiagnostics());
        return result;
    }

    private void writeJavaFileContent(String code) {
        try (OutputStream output = Files.newOutputStream(Paths.get(DEFAULT_CLASS_PATH))) {
            output.write(code.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }

    private void createJavaClass(String javaFileContent) {
        try {
            // create class for Answer
            writeJavaFileContent(javaFileContent);
        } catch (Exception e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }


    private String writeCodeAsClass(String answer) {
        StringBuilder javaFileContent = new StringBuilder();
        boolean isClassOrInterface = answer.contains("class") || answer.contains("interface");
        boolean isPublic = false;
        if (isClassOrInterface) {
            String classDeclaration = answer.substring(0, answer.indexOf("class"));
            isPublic = classDeclaration.contains("public");
        }
        if (isPublic) {
            answer = answer.substring(answer.indexOf("public") + "public".length());
        }
        if (isClassOrInterface) {
            javaFileContent.append(answer);
        } else {
            javaFileContent.append("/**" +
                    "* Test class" +
                    "*/" +
                    "import java.util.*;" +
                    "class TestClass {").append(answer).append("}");
        }
        fullSourceCode = javaFileContent.toString();
        return javaFileContent.toString();
    }

}
