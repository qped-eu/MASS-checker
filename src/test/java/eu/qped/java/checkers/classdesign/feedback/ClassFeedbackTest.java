package eu.qped.java.checkers.classdesign.feedback;

import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class ClassFeedbackTest {

    @Test
    public void testConstructor() {
        ClassFeedback feedback = new ClassFeedback("Test Feedback");
        assertEquals("Test Feedback", feedback.toString());
    }

    @Test
    public void testEquals() {
        ClassFeedback feedback1 = new ClassFeedback("Test Feedback");
        ClassFeedback feedback2 = new ClassFeedback("Test Feedback");
        assertTrue(feedback1.equals(feedback2));
    }

    @Test
    public void testNotEquals() {
        ClassFeedback feedback1 = new ClassFeedback("Test Feedback");
        ClassFeedback feedback2 = new ClassFeedback("Different Feedback");
        assertFalse(feedback1.equals(feedback2));
    }

    @Test
    public void testHashCode() {
        ClassFeedback feedback1 = new ClassFeedback("Test Feedback");
        ClassFeedback feedback2 = new ClassFeedback("Test Feedback");
        assertEquals(feedback1.hashCode(), feedback2.hashCode());
    }
}
