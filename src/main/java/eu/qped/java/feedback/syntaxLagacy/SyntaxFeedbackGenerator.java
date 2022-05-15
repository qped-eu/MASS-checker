package eu.qped.java.feedback.syntaxLagacy;

import java.util.ArrayList;
import java.util.List;

import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.feedback.FeedbackGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import eu.qped.framework.CheckLevel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntaxFeedbackGenerator implements FeedbackGenerator<SyntaxFeedback,SyntaxError> {


    private final static String ERROR_TRIGGER_CONS = " Error code: ";
    private final static String LINE_NUMBER_CONS = " Line: ";
    private final static String ERROR_MSG_CONS = " Error type: ";
    private final static String FEEDBACK_CONS = " Feedback: ";
    private final static String NEW_LINE = "\n\n";
    private final static ArrayList<String> TYPES = new ArrayList<>();

    static {
        TYPES.add("for");
        TYPES.add("switch");
        TYPES.add("while");
        TYPES.add("if");
        TYPES.add("else");
        TYPES.add("System");
        TYPES.add("break");
        TYPES.add("continue");
        TYPES.add("case");
    }

    private List<SyntaxError> syntaxErrors;
    private CheckLevel level;
    private String sourceCode;
    private String example;


    private final StringBuilder result = new StringBuilder();

    @Override
    public List<SyntaxFeedback> generateFeedbacks(List<SyntaxError> syntaxErrors){
        List<SyntaxFeedback> result = new ArrayList<>();
        syntaxErrors.forEach(syntaxError -> result.add(this.getFeedback(syntaxError)));
        return result;
    }

    private StringBuilder appendCliche(String errorMsg, String errorTrigger, long errorLine, StringBuilder result) {

        return result.append(NEW_LINE)
                .append(ERROR_TRIGGER_CONS).append(errorTrigger)
                .append(NEW_LINE)
                .append(LINE_NUMBER_CONS)
                .append(errorLine).append(NEW_LINE)
                .append(ERROR_MSG_CONS).append(errorMsg)
                .append(NEW_LINE)
                .append(FEEDBACK_CONS);
    }





    public SyntaxFeedback getFeedback(SyntaxError syntaxError)  {
        example = "";

        StringBuilder header = new StringBuilder();

        header = appendCliche(syntaxError.getErrorMsg(), syntaxError.getErrorTrigger(), syntaxError.getLine(), header);

        //result = appendCliche(syntaxError.getErrorMsg(), syntaxError.getErrorTrigger(), syntaxError.getLine(), result);


//        List<String> potentialFeedbacks = SyntaxFeedbackDao.data.get(syntaxError.getErrorCode());
//        String helper = potentialFeedbacks.get(0);
//        String formatter = potentialFeedbacks.get(0).toLowerCase(Locale.ROOT);




        SyntaxErrorPredictHelper syntaxErrorPredictHelper = new SyntaxErrorPredictHelper(syntaxError.getErrorCode(), syntaxError.getErrorMsg(), syntaxError.getErrorTrigger());
        switch (syntaxError.getErrorCode()) {
            case "compiler.err.expected":
            case "compiler.err.expected3":
            case "compiler.err.expected1":
            case "compiler.err.expected2": {
                expectedSubSwitch(result, syntaxErrorPredictHelper, syntaxError);
                //System.out.println(syntaxError.getErrorTrigger());
                break;
            }
            case "compiler.err.var.might.not.have.been.initialized": {
                result.append("The variable not only had to be declared but also initialized").append(NEW_LINE);
                setExample("(Declaration) int number = (Initialising) 10");
//                        .append(NEW_LINE)
//                        .append("The variable must be initialized and not just declared,for Example: ").append(NEW_LINE)
//                        .append("(Declaration) int number = (Initialising) 10;");
                break;
            }
            //isMatch maybe
            case "compiler.err.already.defined": {
                result
                        .append("You already have a variable or a method in the common scope with the same name")
                        .append(NEW_LINE)
                        .append("Scope means where the variable or method can be called.").append(NEW_LINE)
                        .append("In each scope, all variables or methods must have unique names.");
                break;
            }
            case "compiler.err.cant.resolve.location": {
                //for var
                result.append("You have called an undefined symbol (variable), at the above-mentioned position")
                        .append(NEW_LINE)
                        .append("it could be that you made a typo with the name or forgot to define the symbol.");
                break;
            }
            case "compiler.err.abstract.cant.be.instantiated": {
                result.append("No object could be created from abstract classes")
                .append(NEW_LINE)
                .append("it is possible to create an object of a subclass of an abstract class");
//                .append(NEW_LINE)
//                .append("ClassAbstract className = new SubClassFromClassAbstract();");
                break;
            }
            case "compiler.err.repeated.modifier": {
                try {
                    result.append("Dont repeat modifiers");
                }
                catch (Exception e){
                    LogManager.getLogger((Class<?>) getClass()).throwing(e);
                }
            }
            case "compiler.err.illegal.combination.of.modifiers": {
                result.append("Modifiers are additional properties for Java declarations such as methods, variables and classes")
                        .append(NEW_LINE)
                        .append("you always have to specify it at the very beginning of the declaration")
                        .append(NEW_LINE)
                        .append("you can also combine them but unfortunately not like what you have done")
                        .append(NEW_LINE);
                        setExample("You can combine almost all modifiers with static, but you are not allowed to combine it public with itself or with private or protected");
                break;
            }
            case "compiler.err.illegal.start.of.expr": {
                String kind = syntaxErrorPredictHelper.getErrorKind();
                String feedbackForVar = "Access Modifiers must not be used within the method with local variables, as their method area defines their accessibility";
                String feedbackForMethod = "A method cannot have another method within its scope";
                String feedbackForStrLit = "String Character Without Double Quotes E.g";
                setExample("String = \"a\";");
                //todo Feedback umschreiben (operationen)
                String feedbackForBraces = "every block (method) or class definition must begin and end with curly braces";

                if (kind.equals("variable")) {
                    result.append(feedbackForVar);
                } else if (kind.equals("method")) {
                    result.append(feedbackForMethod);
                } else {
                    if (level.equals(CheckLevel.BEGINNER)) {
                        result.append(feedbackForBraces)
                                .append(NEW_LINE)
                                .append(feedbackForStrLit)
                                .append(NEW_LINE)
                                .append(feedbackForMethod)
                                .append(NEW_LINE)
                                .append(feedbackForVar);
                    } else if (level.equals(CheckLevel.ADVANCED) || level.equals(CheckLevel.INTERMEDIATE)) {
                        result.append(feedbackForStrLit)
                                .append(NEW_LINE)
                                .append(feedbackForBraces)
                                .append(NEW_LINE)
                                .append(feedbackForMethod)
                                .append(NEW_LINE)
                                .append(feedbackForVar);
                    }
                }
                break;
            }
            case "compiler.err.illegal.start.of.type": {
                for (String s : TYPES) {
                    if (syntaxError.getErrorTrigger().contains(s)) {
                        result.append(" you have used the Type: ").append(s).append(" in a wrong place");
                    }
                }
                break;
            }
            case "compiler.err.not.stmt": {
                if (syntaxError.getErrorTrigger().contains("=")) {
                    result.append("You are trying to initialize a statement, but unfortunately the declaration was wrong.");
                    result.append(NEW_LINE);
                    setExample("For example, a variable can be defined in Java like this: <data type> varName = value;");
                }
                result.append("You have violated the Java Statement rules because there are the following statements in Java:")
                        .append(NEW_LINE)
                        .append("1) Expression Statments: to change the values of a data field or to load methods or to create an object")
                        .append("2) Declaration Statment: to declare variables E.g.: int <varName>;");
                        setExample("int <varName> = value1;" + " " + "<varName> = value2;");
                break;
            }
            case "compiler.err.unclosed.str.lit": {
                // str x = "x;
                result.append("If you want to define a character string with the Java language")
                .append(NEW_LINE)
                .append("it would be correct if you wrote within two quotation marks")
                .append(NEW_LINE);
                setExample("like: String <var name> =\"value\" ");
                break;
            }
            case "compiler.err.premature.eof":
            case "compiler.err.var.not.initialized.in.default.constructor":
            case "compiler.err.non-static.cant.be.ref":
            case "compiler.err.invalid.meth.decl.ret.type.req":
            case "compiler.err.override.static":
            case "compiler.err.cant.resolve.location.args":
            case "compiler.err.cant.apply.symbol":
            case "compiler.err.generic.array.creation": {
                try{
                    result.append( syntaxError.getErrorMsg());
                }
                catch (Exception e){
                    LogManager.getLogger((Class<?>) getClass()).throwing(e);
                }
                break;
            }
            case "compiler.err.prob.found.req": {
                if (syntaxError.getErrorMsg().equals("incompatible types: int cannot be converted to boolean")) {
                    try{
                        result.append(syntaxError.getErrorMsg());
                    }
                    catch (Exception e){
                        LogManager.getLogger((Class<?>) getClass()).throwing(e);
                    }

                } else if (syntaxError.getErrorMsg().equals("incompatible types: possible lossy conversion from double to int")) {
                    try{
                        result.append( syntaxError.getErrorMsg());
                    }
                    catch (Exception e){
                        LogManager.getLogger((Class<?>) getClass()).throwing(e);
                    }
                } else{
                    try{
                        result.append(syntaxError.getErrorMsg());
                    }
                    catch (Exception e){
                        LogManager.getLogger((Class<?>) getClass()).throwing(e);
                    }
                }
                break;
            }
            case "compiler.err.else.without.if": {
                result.append("if you want to check conditions in Java, you can use the if statement, you can add else to it").append(NEW_LINE)
                        .append("but an else statement without an if is problematic");
                break;
            }
            case "compiler.err.missing.ret.stmt": {
                //non-void method without return.
                result.append("Every method whose return type is not void needs a \"return\" at the end.");
                break;
            }
            case "compiler.err.unreachable.stmt": {
                //stmt after return
                result.append("Return always closes a method, so you cannot pass statements after a return");
                break;
            }
            case "compiler.err.missing.meth.body.or.decl.abstract": {
                //put a ; just before the first {
                result.append("The declaration of a method consists of 2 steps:")
                        .append(NEW_LINE)
                        .append("Method header: <return type> methodName ()")
                        .append(NEW_LINE)
                        .append("Method body: {code block and a return if necessary}");
                if(!level.equals(CheckLevel.BEGINNER)){
                    result .append(NEW_LINE)
                            .append("In the abstract class you can declare a method head without a method body but with the keyword \"abstract\"");
                }


                break;
            }
            default:
                result.append(syntaxError.getErrorMsg()).append(LINE_NUMBER_CONS).append(syntaxError.getLine());
        }
        result.append(NEW_LINE);
        return new SyntaxFeedback( result.toString() , syntaxError.getErrorCode() , "",syntaxError, null);
    }


    private void expectedSubSwitch(StringBuilder result, SyntaxErrorPredictHelper syntaxErrorPredictHelper, SyntaxError syntaxError) {

        example = "";
        String forSemExp = syntaxError.getAdditionalProperties().get("forSemExpected");

        switch (syntaxError.getErrorMsg()) {
            case "';' expected":
                if (forSemExp != null) {
                    if (syntaxErrorPredictHelper.hasBraces(forSemExp)) {
                        result.append("You used the braces incorrectly when declaring a method.");
                    } else {
                        result
                                .append("Every java statement must end with a Semicolon. ")
                                .append(NEW_LINE);
                                setExample(" for example :int oddNumber = 7;");
                    }
                }
                break;
            case "'(' expected":
            case "= expected":
            case "'.class' expected":
            case "'[' expected":
                try{
                    result.append(syntaxError.getErrorMsg());
                }
                catch (Exception e){
                    LogManager.getLogger((Class<?>) getClass()).throwing(e);
                }
                break;
            case "<identifier> expected":
                if (syntaxErrorPredictHelper.getErrorKind().equals("method")) {
                    result.append("You wrote a block of code somewhere where Java does not expect it, for example: System.out.println (\"Hello\"); inside the class outside a method");
                } else if (syntaxErrorPredictHelper.getErrorKind().equals("variable")) {
                    result.append("You have defined a data field without a name, you can define the data field");
                    setExample("like this: int variable = 5; define");
                }
                break;
            case "class, interface, or enum expected":
                boolean isNumberOfBrEq = syntaxErrorPredictHelper.hasEqualNumberOfBraces(getSourceCode());
                if (isNumberOfBrEq) {
                    result.append("if you try to write a method outside of the class, you can clear this error if you write this method inside the class");
                } else {
                    result.append("additional curly bracket \"}\" Here the error can be corrected by simply removing the additional curly bracket \"}\" or by observing the indentation");
                }
                break;
            default:
                result.append(syntaxError.getErrorMsg());
        }
    }


}

