package eu.qped.java.utils.compiler;



import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.*;
import org.apache.logging.log4j.LogManager;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compiler {

    private String code;
    private StringBuilder javaFileContent;
    private ArrayList<SyntaxError> syntaxErrors;
    private boolean canCompile;
    private String fullSourceCode;


    public Compiler (String code){
        this.code = code;
    }

    //todo: Java Doc @later

    /**
     * die Methode konvertiert ein String-Source zu einem Java Objekt
     * Die Methode betrachtet ob es eine Klasse bzw. eine Methode geschrieben wurde.
     *
     * @return SimpleJavaFileObject
     */
    public SimpleJavaFileObject getJavaFileContentFromString() {
        javaFileContent = new StringBuilder();
        writeCodeAsClass();
        JavaObjectFromString javaObjectFromString = null;
        try {
            javaObjectFromString = new JavaObjectFromString("TestClass", javaFileContent.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace(); //todo
        }
        return javaObjectFromString;
    }

    private void writeCodeAsClass() {
        boolean isClassOrInterface = code.contains("class") || code.contains("interface");
        boolean isPublic = false;
        if (isClassOrInterface) {
            String classDeclaration = code.substring(0, code.indexOf("class"));
            isPublic = classDeclaration.contains("public");
        }
        if (isPublic) {
            code = code.substring(code.indexOf("public") + "public".length());
        }
        if (isClassOrInterface) {
            javaFileContent.append(code);
        } else {
            javaFileContent.append("/**" +
                    "* Test class" +
                    "*/" +
                    "import java.*;" +
                    "class TestClass {").append(code).append("}");
        }
    }

    private void writeJavaFileContent() {
        try (OutputStream output = Files.newOutputStream(Paths.get("TestClass.java"))) {
            output.write(javaFileContent.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
        }
    }


    public void compile()  {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.GERMANY, Charset.defaultCharset());
        JavaFileObject javaFileObjectFromString = getJavaFileContentFromString();
        Iterable<JavaFileObject> fileObjects = Collections.singletonList(javaFileObjectFromString);

        StringWriter output = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(output, standardJavaFileManager, diagnosticCollector, null, null, fileObjects);

        Boolean result = task.call();
        fullSourceCode = "";

        List<Diagnostic< ? extends JavaFileObject> > diagnostics = diagnosticCollector.getDiagnostics();
        for (Diagnostic< ? extends JavaFileObject>  diagnostic : diagnostics) {

            try {
                fullSourceCode = diagnostic.getSource().getCharContent(true).toString();
            }
            catch (IOException e){
                LogManager.getLogger((Class<?>) getClass()).throwing(e);
                fullSourceCode = diagnostic.getSource().toString();
            }

            //System.out.println(source);
            String errorSource;
            try {
                errorSource = fullSourceCode.substring((int) diagnostic.getStartPosition());
            } catch (StringIndexOutOfBoundsException e) {
                errorSource = fullSourceCode.substring((int) diagnostic.getStartPosition() + 1);
            }
            String[] splitSource = errorSource.split(";");

            Map<String, String> addProp = new HashMap<>();

            if (diagnostic.getCode().equals("compiler.err.expected")) {
                String forExpected = errorSource.split("[{]")[0];
                addProp.put("forSemExpected", forExpected);
            }

            String errorTrigger = splitSource[0];

            syntaxErrors.add(new SyntaxError(diagnostic.getCode(), diagnostic.getMessage(Locale.GERMAN), diagnostic.getLineNumber(), errorTrigger, addProp, diagnostic.getStartPosition(), diagnostic.getEndPosition()));
        }
        if (result) {
            setCanCompile(true);
            try {
                writeJavaFileContent();
                fullSourceCode = javaFileContent.toString();
            }
            catch (Exception e){
                LogManager.getLogger((Class<?>) getClass()).throwing(e);
            }


        } else {
            setCanCompile(false);
        }
    }


    public static void main(String[] args) {
    }
}
