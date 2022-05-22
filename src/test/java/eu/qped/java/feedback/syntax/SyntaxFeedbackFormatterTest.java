package eu.qped.java.feedback.syntax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxFeedbackFormatterTest {
    private SyntaxFeedbackNew syntaxFeedback;
    private SyntaxFeedbackNew emptySyntaxFeedback;


    @BeforeEach
    void setUp() {
        syntaxFeedback = SyntaxFeedbackNew.builder()
                .header("header")
                .feedbackMessage("feedbackMessage")
                .errorLine("2")
                .errorSource("int i = 0")
                .solutionExample("int i = 0;")
                .build();
        emptySyntaxFeedback = SyntaxFeedbackNew.builder().build();
    }

    @Test
    void formatFeedback() {
        SyntaxFeedbackFormatter.builder().build().formatFeedback(syntaxFeedback);
        Assertions.assertNotEquals("", syntaxFeedback.getHeader());
        Assertions.assertNotEquals("", syntaxFeedback.getFeedbackMessage());
        Assertions.assertNotEquals("", syntaxFeedback.getErrorLine());
        Assertions.assertNotEquals("", syntaxFeedback.getErrorSource());
        Assertions.assertNotEquals("", syntaxFeedback.getSolutionExample());
    }

    @Test
    void formatEmptyFeedback() {
        SyntaxFeedbackFormatter.builder().build().formatFeedback(emptySyntaxFeedback);
        Assertions.assertEquals("", emptySyntaxFeedback.getHeader());
        Assertions.assertEquals("", emptySyntaxFeedback.getFeedbackMessage());
        Assertions.assertEquals("", emptySyntaxFeedback.getErrorLine());
        Assertions.assertEquals("", emptySyntaxFeedback.getErrorSource());
        Assertions.assertEquals("", emptySyntaxFeedback.getSolutionExample());
    }
}