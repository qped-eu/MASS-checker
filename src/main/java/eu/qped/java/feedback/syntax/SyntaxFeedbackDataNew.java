package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackDataNew {

    public static Map<String, List<SyntaxFeedbackNew>> getSyntaxFeedbackByErrorCode() {
        Map<String, List<SyntaxFeedbackNew>> feedbackBySyntaxErrorCode = new HashMap<>();
        feedbackBySyntaxErrorCode.put(
                "compiler.err.expected",
                List.of(
//                        eu.qped.java.feedback.syntax.SyntaxFeedback.builder()
//                                .feedbackContent("You used the braces incorrectly when declaring a method.")
//                                .solutionExample("")
//                                .errorMessage("';' expected")
//                                .errorInfo(
//                                        // TODO create code
//                                        ErrorInfo.builder()
//                                                .errorKey("braces_expected")
//                                                .build()
//                                )
//                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("Every java statement must end with a Semicolon.")
                                .solutionExample("int oddNumber = 7;")
                                .errorMessage("';' expected")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("semi_expected")
                                                .build()
                                )
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("'(' expected")
                                .solutionExample("")
                                .errorMessage("'(' expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("= expected")
                                .solutionExample("")
                                .errorMessage("= expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("'.class' expected")
                                .solutionExample("")
                                .errorMessage("'.class' expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("'[' expected")
                                .solutionExample("")
                                .errorMessage("'[' expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("You wrote a block of code somewhere where Java does not expect it, for example: System.out.println (\"Hello\"); inside the class outside a method")
                                .solutionExample("")
                                .errorMessage("<identifier> expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("You have defined a data field without a name, you can define the data field")
                                .solutionExample("like this: int variable = 5; define")
                                .errorMessage("<identifier> expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("if you try to write a method outside of the class, you can clear this error if you write this method inside the class")
                                .solutionExample("")
                                .errorMessage("class, interface, or enum expected")
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("additional curly bracket \"}\" Here the error can be corrected by simply removing the additional curly bracket \"}\" or by observing the indentation")
                                .solutionExample("")
                                .errorMessage("class, interface, or enum expected")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.var.might.not.have.been.initialized",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("The variable not only had to be declared but also initialized")
                                .solutionExample("(Declaration) int number = (Initialising) 10")
                                .errorMessage("?")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.already.defined",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("You already have a variable or a method in the common scope with the same name. \n Scope means where the variable or method can be called. \n In each scope, all variables or methods must have unique names.")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        feedbackBySyntaxErrorCode.put(
                "compiler.err.cant.resolve.location",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("You have called an undefined symbol (variable), at the above-mentioned position \n it could be that you made a typo with the name or forgot to define the symbol.")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.abstract.cant.be.instantiated",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("No object could be created from abstract classes \n it is possible to create an object of a subclass of an abstract class")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        feedbackBySyntaxErrorCode.put(
                "compiler.err.repeated.modifier",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("Dont repeat modifiers")
                                .solutionExample("public public")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.illegal.combination.of.modifiers",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent( ""
                                        + "Modifiers are additional properties for Java declarations such as methods, variables and classes \n"
                                        + "you always have to specify it at the very beginning of the declaration \n"
                                        + "you can also combine them but unfortunately not like what you have done \n"
                                        + "You can combine almost all modifiers with static, but you are not allowed to combine it public with itself or with private or protected"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.illegal.start.of.expr",
                List.of(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("Access Modifiers must not be used within the method with local variables, as their method area defines their accessibility")
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.expr+µ+variable ")
                                                .build()
                                )
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("A method cannot have another method within its scope")
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.expr+µ+method")
                                                .build()
                                )
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                    + "every block (method) or class definition must begin and end with curly braces. \n"
                                    + "String Character Without Double Quotes E.g. \n"
                                    + "A method cannot have another method within its scope \n"
                                    + "Access Modifiers must not be used within the method with local variables, as their method area defines their accessibility."
                                )
                                .solutionExample("String = \"a\";")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.expr+µ+All")
                                                .build()
                                )
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                // TODO
                "compiler.err.illegal.start.of.type",
                List.of(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " for"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+for")
                                                .build()
                                )
                                .build()
                        ,
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " switch"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+switch")
                                                .build()
                                )
                                .build()
                        ,
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " while"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+while")
                                                .build()
                                )
                                .build()
                        , SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " if"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+if")
                                                .build()
                                )
                                .build()
                        , SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " else"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+else")
                                                .build()
                                )
                                .build()
                        , SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " System"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+System")
                                                .build()
                                )
                                .build()
                        ,
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " break"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+break")
                                                .build()
                                )
                                .build()
                        ,
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " continue"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+continue")
                                                .build()
                                )
                                .build()
                        ,
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "you have used the Type:"
                                        + " case"
                                        + " in a wrong place"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.illegal.start.of.type+µ+case")
                                                .build()
                                )
                                .build()
                )

        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.not.stmt",
                List.of(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("You are trying to initialize a statement, but unfortunately the declaration was wrong.")
                                .solutionExample("For example, a variable can be defined in Java like this: <data type> varName = value;")
                                .errorMessage("compiler.err.not.stmt")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.not.stmt+µ+=")
                                                .build()
                                )
                                .build(),
                        SyntaxFeedbackNew.builder()
                                .feedbackContent( ""
                                        + "You have violated the Java Statement rules because there are the following statements in Java:\n"
                                        + "1) Expression Statments: to change the values of a data field or to load methods or to create an object \n"
                                        + "2) Declaration Statment: to declare variables E.g.: int <varName>; \n"
                                )
                                .solutionExample("int <varName> = value1;" + " " + "<varName> = value2;")
                                .errorMessage("")
                                .errorInfo(
                                        ErrorInfo.builder()
                                                .errorKey("compiler.err.not.stmt+µ+Not=")
                                                .build()
                                )
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.unclosed.str.lit",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent(""
                                        + "If you want to define a character string with the Java language \n"
                                        + "it would be correct if you wrote within two quotation marks"
                                )
                                .solutionExample("like: String <var name> =\"value\"")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.else.without.if",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent( ""
                                        + "if you want to check conditions in Java, you can use the if statement, you can add else to it\n"
                                        + "but an else statement without an if is problematic"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.missing.ret.stmt",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("Every method whose return type is not void needs a \"return\" at the end.")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        feedbackBySyntaxErrorCode.put(
                "compiler.err.unreachable.stmt",
                Collections.singletonList(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent("Return always closes a method, so you cannot pass statements after a return")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        feedbackBySyntaxErrorCode.put(
                "compiler.err.missing.meth.body.or.decl.abstract",
                List.of(
                        SyntaxFeedbackNew.builder()
                                .feedbackContent( ""
                                        + "The declaration of a method consists of 2 steps:\n"
                                        + "Method header: <return type> methodName () \n"
                                        + "Method body: {code block and a return if necessary}"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        return feedbackBySyntaxErrorCode;
    }

}
