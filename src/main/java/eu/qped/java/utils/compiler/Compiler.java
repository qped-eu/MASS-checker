package eu.qped.java.utils.compiler;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import javax.tools.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
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

    private String answer;
    private boolean isCompilable;
    private String fullSourceCode;

    /**
     * die Methode konvertiert ein String-Source zu einem {@link JavaFileObject}
     *
     * @return SimpleJavaFileObject
     */
    public SimpleJavaFileObject getJavaFileObjectFromString() {
        String javaFileContent = writeCodeAsClass();
        JavaObjectFromString javaObjectFromString = null;
        try {
            javaObjectFromString = new JavaObjectFromString("TestClass", javaFileContent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return javaObjectFromString;
    }

    public List<Diagnostic<? extends JavaFileObject>> compile() {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.GERMANY, Charset.defaultCharset());
        JavaFileObject javaFileObjectFromString = getJavaFileObjectFromString();
        Iterable<JavaFileObject> fileObjects = Collections.singletonList(javaFileObjectFromString);

        StringWriter output = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(output, standardJavaFileManager, diagnosticCollector, null, null, fileObjects);

        Boolean result = task.call();

        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        if (result) {
            setCompilable(true);
            try {
                writeJavaFileContent(fullSourceCode);
            } catch (Exception e) {
                LogManager.getLogger(getClass()).throwing(e);
            }


        } else {
            setCompilable(false);
        }
        return diagnostics;
    }

    private String writeCodeAsClass() {
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

    private void writeJavaFileContent(String code) {
        try (OutputStream output = Files.newOutputStream(Paths.get("TestClass.java"))) {
            output.write(code.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }

}
