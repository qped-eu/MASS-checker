package eu.qped.java.checkers.syntax;

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
public class SyntaxFeedbackData {

    public Map<String, List<SyntaxFeedback>> getDataDocument() {
        Map<String, List<SyntaxFeedback>> dataDocument = new HashMap<>();
        dataDocument
                .put(
                        "compiler.err.expected",
                        List.of(
                                SyntaxFeedback.builder()
                                        .feedbackContent("You used the braces incorrectly when declaring a method.")
                                        .solutionExample("")
                                        .errorMessage("';' expected")
                                        .errorInfo(
                                                ErrorInfo.builder()
                                                        .errorKey("braces_expected")
                                                        .build()
                                        )
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("Every java statement must end with a Semicolon.")
                                        .solutionExample("for example: int oddNumber = 7;")
                                        .errorMessage("';' expected")
                                        .errorInfo(
                                                ErrorInfo.builder()
                                                        .errorKey("semi_expected")
                                                        .build()
                                        )
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("")
                                        .solutionExample("")
                                        .errorMessage("")
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("You wrote a block of code somewhere where Java does not expect it, for example: System.out.println (\"Hello\"); inside the class outside a method")
                                        .solutionExample("")
                                        .errorMessage("<identifier> expected")
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("You have defined a data field without a name, you can define the data field")
                                        .solutionExample("like this: int variable = 5; define")
                                        .errorMessage("<identifier> expected")
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("if you try to write a method outside of the class, you can clear this error if you write this method inside the class")
                                        .solutionExample("")
                                        .errorMessage("class, interface, or enum expected")
                                        .build(),
                                SyntaxFeedback.builder()
                                        .feedbackContent("additional curly bracket \"}\" Here the error can be corrected by simply removing the additional curly bracket \"}\" or by observing the indentation")
                                        .solutionExample("")
                                        .errorMessage("class, interface, or enum expected")
                                        .build()
                        )
                );
        dataDocument.put(
                "compiler.err.var.might.not.have.been.initialized",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("The variable not only had to be declared but also initialized")
                                .solutionExample("(Declaration) int number = (Initialising) 10")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.already.defined",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("You already have a variable or a method in the common scope with the same name")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        dataDocument.put(
                "compiler.err.cant.resolve.location",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("You have called an undefined symbol (variable), at the above-mentioned position")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.abstract.cant.be.instantiated",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("No object could be created from abstract classes")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        dataDocument.put(
                "compiler.err.repeated.modifier",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("Dont repeat modifiers")
                                .solutionExample("public public")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.illegal.combination.of.modifiers",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent(
                                        "Modifiers are additional properties for Java declarations such as methods, variables and classes \n" +
                                                "you always have to specify it at the very beginning of the declaration \n" +
                                                "you can also combine them but unfortunately not like what you have done \n" +
                                                "You can combine almost all modifiers with static, but you are not allowed to combine it public with itself or with private or protected"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.illegal.start.of.expr",
                List.of(
                        SyntaxFeedback.builder()
                                .feedbackContent("Access Modifiers must not be used within the method with local variables, as their method area defines their accessibility")
                                .solutionExample("")
                                .errorMessage("")
                                .build(),
                        SyntaxFeedback.builder()
                                .feedbackContent("A method cannot have another method within its scope")
                                .solutionExample("")
                                .errorMessage("")
                                .build(),
                        SyntaxFeedback.builder()
                                .feedbackContent("String Character Without Double Quotes E.g")
                                .solutionExample("String = \"a\";")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.illegal.start.of.type",
                Collections.singletonList(SyntaxFeedback.builder()
                        .feedbackContent(" you have used a Type in a wrong place")
                        .solutionExample("")
                        .errorMessage("")
                        .build())
        );
        dataDocument.put(
                "compiler.err.not.stmt",
                List.of(
                        SyntaxFeedback.builder()
                                .feedbackContent("You are trying to initialize a statement, but unfortunately the declaration was wrong.")
                                .solutionExample("For example, a variable can be defined in Java like this: <data type> varName = value;")
                                .errorMessage("")
                                .build(),
                        SyntaxFeedback.builder()
                                .feedbackContent(
                                        "You have violated the Java Statement rules because there are the following statements in Java:\n" +
                                                "1) Expression Statments: to change the values of a data field or to load methods or to create an object \n" +
                                                "2) Declaration Statment: to declare variables E.g.: int <varName>; \n"
                                )
                                .solutionExample("int <varName> = value1;" + " " + "<varName> = value2;")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.unclosed.str.lit",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent(
                                        "If you want to define a character string with the Java language \n"
                                                + "it would be correct if you wrote within two quotation marks"
                                )
                                .solutionExample("like: String <var name> =\"value\"")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.else.without.if",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent(
                                        "if you want to check conditions in Java, you can use the if statement, you can add else to it\n" +
                                                "but an else statement without an if is problematic"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.missing.ret.stmt",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("Every method whose return type is not void needs a \"return\" at the end.")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        dataDocument.put(
                "compiler.err.unreachable.stmt",
                Collections.singletonList(
                        SyntaxFeedback.builder()
                                .feedbackContent("Return always closes a method, so you cannot pass statements after a return")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );

        dataDocument.put(
                "compiler.err.missing.meth.body.or.decl.abstract",
                List.of(
                        SyntaxFeedback.builder()
                                .feedbackContent(
                                        "The declaration of a method consists of 2 steps:\n" +
                                                "Method header: <return type> methodName () \n" +
                                                "Method body: {code block and a return if necessary}"
                                )
                                .solutionExample("")
                                .errorMessage("")
                                .build(),
                        SyntaxFeedback.builder()
                                .feedbackContent("In the abstract class you can declare a method head without a method body but with the keyword \"abstract\"")
                                .solutionExample("")
                                .errorMessage("")
                                .build()
                )
        );
        return dataDocument;
    }

}
