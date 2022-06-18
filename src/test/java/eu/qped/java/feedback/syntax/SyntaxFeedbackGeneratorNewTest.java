package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.SyntaxError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SyntaxFeedbackGeneratorNewTest {
    private SyntaxError syntaxError;

    @BeforeEach
    void setUp() {
        syntaxError = SyntaxError.builder()
                .errorCode("compiler.err.expected")
                .errorMessage("';' expected")
                .errorSourceCode("int i = 0")
                .line(2)
                .build();
    }

    @Test
    void generateHeader() {
        Assertions.assertNotEquals("", SyntaxFeedbackGenerator.builder().build().generateHeader());
    }

    @Test
    void generateFeedbackMessage() {
        Assertions.assertNotEquals("", SyntaxFeedbackGenerator.builder().build().generateFeedbackMessage(syntaxError));
    }


    @Test
    void generateErrorLine() {
        Assertions.assertNotEquals("", SyntaxFeedbackGenerator.builder().build().generateErrorLine(syntaxError.getLine()));
    }

    @Test
    void generateErrorSource() {
        Assertions.assertNotEquals("", SyntaxFeedbackGenerator.builder().build().generateErrorSource(syntaxError.getErrorTrigger()));
    }

    @Test
    void generateSolutionExample() {
        Assertions.assertNotEquals("", SyntaxFeedbackGenerator.builder().build().generateSolutionExample(syntaxError));
    }
}