package eu.qped.java.checkers.classdesign.feedback;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClassFeedbackGeneratorTest {
    @Test
    public void testGenerateFeedbackWithWrongElementType() {
        ClassFeedback feedback = ClassFeedbackGenerator.generateFeedback("TestClass", "testElement", ClassFeedbackType.WRONG_ELEMENT_TYPE, "Custom feedback");
        assertNotNull(feedback);
        assertEquals("ElementTypeError: Element **testElement** in **TestClass**  does not possess the expected type.\n" +
                "\n" +
                "Custom feedback", feedback.toString());
    }

    @Test
    public void testGenerateFeedbackWithWrongElementName() {
        ClassFeedback feedback = ClassFeedbackGenerator.generateFeedback("TestClass", "testElement", ClassFeedbackType.WRONG_ELEMENT_NAME, "Custom feedback");
        assertNotNull(feedback);
        assertEquals("ElementNameError: Element **testElement** in **TestClass**  does not possess the expected name.\n" +
                "\n" +
                "Custom feedback", feedback.toString());
    }

    @Test
    public void testGenerateFeedbackWithWrongAccessModifier() {
        ClassFeedback feedback = ClassFeedbackGenerator.generateFeedback("TestClass", "testElement", ClassFeedbackType.WRONG_ACCESS_MODIFIER, "Custom feedback");
        assertNotNull(feedback);
        assertEquals("AccessModifierError: Different access modifier for **testElement** in **TestClass** expected.\n\nCustom feedback", feedback.toString());
    }

    @Test
    public void testGenerateFeedbackWithWrongNonAccessModifier() {
        ClassFeedback feedback = ClassFeedbackGenerator.generateFeedback("TestClass", "testElement", ClassFeedbackType.WRONG_NON_ACCESS_MODIFIER, "Custom feedback");
        assertNotNull(feedback);
        assertEquals("NonAccessModifierError: Different non access modifiers for **testElement** in **TestClass**  expected.\n" +
                "\n" +
                "Custom feedback", feedback.toString());
    }

    // Add similar tests for the other feedback types

}