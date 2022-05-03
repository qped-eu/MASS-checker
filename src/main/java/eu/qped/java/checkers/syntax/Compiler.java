package eu.qped.java.checkers.syntax;



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

import org.apache.logging.log4j.LogManager;


public class Compiler {

    private StringBuilder javaFileContent;

    private String input;
    private final ArrayList<SyntaxError> syntaxErrors = new ArrayList<>();




    private boolean canCompile = false;

    private String source;




    public Compiler (String input){
        this.input = input;
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
        boolean isClassOrInterface = input.contains("class") || input.contains("interface");
        boolean isPublic = false;
        if (isClassOrInterface) {
            String classDeclaration = input.substring(0, input.indexOf("class"));
            isPublic = classDeclaration.contains("public");
        }
        if (isPublic) {
            input = input.substring(input.indexOf("public") + "public".length());
        }
        if (isClassOrInterface) {
            javaFileContent.append(input);
        } else {
            javaFileContent.append("/**" +
                    "* Test class" +
                    "*/" +
                    "import java.util.*;" +
                    "class TestClass {").append(input).append("}");
        }


        JavaObjectFromString javaObjectFromString = null;
        try {
            javaObjectFromString = new JavaObjectFromString("TestClass", javaFileContent.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace(); //todo
        }
        return javaObjectFromString;
    }

    //TODO: don't write into project folder. Rather write to temp folder
    private void writeJavaFileContent() {
        try (OutputStream output = Files.newOutputStream(Paths.get("TestClass.java"))) {
            output.write(javaFileContent.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogManager.getLogger((Class<?>) getClass()).throwing(e);
        }
    }


    //TODO: don't compile class file into project folder. Rather write to temp folder
    public void compile()  {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.GERMANY, Charset.defaultCharset());
        JavaFileObject javaFileObjectFromString = getJavaFileContentFromString();
        Iterable<JavaFileObject> fileObjects = Collections.singletonList(javaFileObjectFromString);

        StringWriter output = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(output, standardJavaFileManager, diagnosticCollector, null, null, fileObjects);

        Boolean result = task.call();
        source = "";

        List<Diagnostic< ? extends JavaFileObject> > diagnostics = diagnosticCollector.getDiagnostics();
        for (Diagnostic< ? extends JavaFileObject>  diagnostic : diagnostics) {

            try {
                source = diagnostic.getSource().getCharContent(true).toString();
            }
            catch (IOException e){
                LogManager.getLogger((Class<?>) getClass()).throwing(e);
                source = diagnostic.getSource().toString();
            }

            //System.out.println(source);
            String errorSource;
            try {
                errorSource = source.substring((int) diagnostic.getStartPosition());
            } catch (StringIndexOutOfBoundsException e) {
                errorSource = source.substring((int) diagnostic.getStartPosition() + 1);
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
                source = javaFileContent.toString();
            }
            catch (Exception e){
                LogManager.getLogger((Class<?>) getClass()).throwing(e);
            }


        } else {
            setCanCompile(false);
        }
    }
    public boolean canCompile() {
        return canCompile;
    }

    public void setCanCompile(boolean canCompile) {
        this.canCompile = canCompile;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }
}
