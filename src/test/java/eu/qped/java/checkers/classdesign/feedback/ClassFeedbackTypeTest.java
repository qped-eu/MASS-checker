package eu.qped.java.checkers.classdesign.feedback;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassFeedbackTypeTest {

    @Test
    public void testToString_WRONG_ELEMENT_NAME() {
        ClassFeedbackType type = ClassFeedbackType.WRONG_ELEMENT_NAME;
        assertEquals("ElementNameError", type.toString());
    }

    @Test
    public void testToString_WRONG_ACCESS_MODIFIER() {
        ClassFeedbackType type = ClassFeedbackType.WRONG_ACCESS_MODIFIER;
        assertEquals("AccessModifierError", type.toString());
    }

    @Test
    public void testToString_MISSING_FIELDS() {
        ClassFeedbackType type = ClassFeedbackType.MISSING_FIELDS;
        assertEquals("MissingFieldsError", type.toString());
    }

    @Test
    public void testToString_WRONG_CLASS_TYPE() {
        ClassFeedbackType type = ClassFeedbackType.WRONG_CLASS_TYPE;
        assertEquals("ClassTypeError", type.toString());
    }

    @Test
    public void testToString_MISSING_SUPER_CLASS() {
        ClassFeedbackType type = ClassFeedbackType.MISSING_SUPER_CLASS;
        assertEquals("MissingSuperClass", type.toString());
    }

    // test other ClassFeedbackType enumeration values similarly
}