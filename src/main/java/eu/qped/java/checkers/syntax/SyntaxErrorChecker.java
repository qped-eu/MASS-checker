package eu.qped.java.checkers.syntax;

import java.util.*;

import eu.qped.framework.CheckLevel;
import eu.qped.java.utils.compiler.Compiler;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Syntax checker
 * @since 19.08.2021
 * @author Mayar Hamdhash hamdash@students.uni-marburg.de
 * @version 1.1
 */

public class SyntaxErrorChecker {

    private Compiler compiler;

    private final String answer;

    private String sourceCode;

    private CheckLevel level;



    /*
    inputs
     */
    private ArrayList<SyntaxError> syntaxErrors;

    /*
    outputs
     */
    private final ArrayList<SyntaxFeedback> feedbacksArray = new ArrayList<>();



    private SyntaxErrorChecker(final String answer){
        this.answer = answer;
    }

    public static SyntaxErrorChecker createSyntaxErrorChecker(final String answer) {
        return new SyntaxErrorChecker(answer);
    }

    public void check(){
        compiler = Compiler.builder().answer(answer).build();
        List<Diagnostic<? extends JavaFileObject>> diagnostics = compiler.compile();
        analyseDiagnostics(diagnostics);
        this.setSourceCode(compiler.getFullSourceCode());
    }

    public void analyze(){
        SyntaxFeedbackGenerator feedbackGenerator = new SyntaxFeedbackGenerator( getSourceCode() ,getLevel());
        for (SyntaxError syntaxError : syntaxErrors) {
            feedbacksArray.add(feedbackGenerator.getFeedback(syntaxError));
        }
    }

    public boolean canCompile (){
        return compiler.isCompilable();
    }

    private void analyseDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            String errorSource;
            try {
                errorSource = compiler.getFullSourceCode().substring((int) diagnostic.getStartPosition());
            } catch (StringIndexOutOfBoundsException e) {
                errorSource = compiler.getFullSourceCode().substring((int) diagnostic.getStartPosition() + 1);
            }
            String[] splitSource = errorSource.split(";");

            Map<String, String> addProp = new HashMap<>();

            if (diagnostic.getCode().equals("compiler.err.expected")) {
                String forExpected = errorSource.split("[{]")[0];
                addProp.put("forSemExpected", forExpected);
            }

            String errorTrigger = splitSource[0];
            syntaxErrors = new ArrayList<>();
            syntaxErrors
                    .add(new SyntaxError(diagnostic.getCode(),
                            diagnostic.getMessage(Locale.GERMAN),
                            diagnostic.getLineNumber(),
                            errorTrigger,
                            addProp,
                            diagnostic.getStartPosition(),
                            diagnostic.getEndPosition()));
        }
    }

    public ArrayList<SyntaxFeedback> getFeedbacks() {
        return feedbacksArray;
    }

    public CheckLevel getLevel() {
        return level;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setLevel(CheckLevel level) {
        this.level = level;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }




    public ArrayList<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }

    public static void main(String[] args) {
        SyntaxErrorChecker checker = new SyntaxErrorChecker("public void print() {\n" +
                "    System.out.println(\"hallo\")\n" +
                "}");
        checker.check();
        for (SyntaxError syntaxError: checker.getSyntaxErrors()){
            System.out.println(syntaxError.getErrorMsg());
        }
    }
}
