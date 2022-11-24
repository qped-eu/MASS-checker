package eu.qped.java.checkers.classdesign.utils;

import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassFeedbackTest {

    @Test
    public void generateCustomFeedback() {
        ClassFeedback fb =  ClassFeedbackGenerator.generateFeedback("class TestClass", "testField",
                ClassFeedbackType.WRONG_ACCESS_MODIFIER, "Look at hint 1.2 for further info.");

        String expected = "AccessModifierError: Different access modifier for **testField** in **class TestClass** expected.\n\n" +
                "Look at hint 1.2 for further info.";
        Assertions.assertEquals(expected, fb.toString());
    }
}
