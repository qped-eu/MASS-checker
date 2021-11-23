package eu.qped.umr.chekcers.syntaxChecker;

import eu.qped.umr.chekcers.MassChecker;
import eu.qped.umr.helpers.Compiler;
import eu.qped.umr.model.CheckLevel;
import eu.qped.umr.model.SyntaxError;
import eu.qped.umr.model.SyntaxFeedback;


import java.util.ArrayList;

/**
 * Syntax checker
 * @since 19.08.2021
 * @author Mayar Hamdhash hamdash@students.uni-marburg.de
 * @version 1.1
 */

public class SyntaxErrorChecker implements MassChecker {

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

    @Override
    public void check(){
        compiler = new Compiler(answer);
        compiler.compile();
        this.setSyntaxErrors(compiler.getSyntaxErrors());
        this.setSourceCode(compiler.getSource());
    }

    public void analyze(){
        SyntaxFeedbackGenerator feedbackGenerator = new SyntaxFeedbackGenerator( getSourceCode() ,getLevel());
        for (SyntaxError syntaxError : syntaxErrors) {
            feedbacksArray.add(feedbackGenerator.getFeedback(syntaxError));
        }
    }

    public boolean canCompile (){
        return compiler.canCompile();
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

    public void setSyntaxErrors(ArrayList<SyntaxError> syntaxErrors) {
        this.syntaxErrors = syntaxErrors;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public ArrayList<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }
}
