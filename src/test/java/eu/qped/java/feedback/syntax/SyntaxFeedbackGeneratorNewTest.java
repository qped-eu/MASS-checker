package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.SyntaxError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        Assertions.assertNotEquals("", SyntaxFeedbackGeneratorNew.builder().build().generateHeader());
    }

    @Test
    void generateFeedbackMessage() {
        Assertions.assertNotEquals("", SyntaxFeedbackGeneratorNew.builder().build().generateFeedbackMessage(syntaxError));
    }


    @Test
    void generateErrorLine() {
        Assertions.assertNotEquals("", SyntaxFeedbackGeneratorNew.builder().build().generateErrorLine(syntaxError));
    }

    @Test
    void generateErrorSource() {
        Assertions.assertNotEquals("", SyntaxFeedbackGeneratorNew.builder().build().generateErrorSource(syntaxError));
    }

    @Test
    void generateSolutionExample() {
        Assertions.assertNotEquals("", SyntaxFeedbackGeneratorNew.builder().build().generateSolutionExample(syntaxError));
    }
}