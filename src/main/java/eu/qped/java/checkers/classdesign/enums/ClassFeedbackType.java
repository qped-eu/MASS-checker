package eu.qped.java.checkers.classdesign.enums;

public enum ClassFeedbackType {
    //Keyword Errors
    WRONG_ELEMENT_NAME("ElementNameError"),
    WRONG_ELEMENT_TYPE("ElementTypeError"),
    WRONG_ACCESS_MODIFIER("AccessModifierError"),
    WRONG_NON_ACCESS_MODIFIER("NonAccessModifierError"),
    WRONG_CLASS_ACCESS_MODIFIER("ClassAccessModifierError"),
    WRONG_CLASS_NON_ACCESS_MODIFIER("ClassNonAccessModifierError"),

    //Field Errors
    MISSING_FIELDS("MissingFieldsError"),
    HIDDEN_FIELD("HiddenFieldError"),

    //Method Errors
    MISSING_METHODS("MissingMethodsError"),
    OVERWRITTEN_METHOD("OverwrittenMethodError"),
    HIDDEN_METHOD("HiddenMethodError"),

    //Class Errors
    MISSING_INTERFACE_IMPLEMENTATION("MissingInterfaceImplementationError"),
    MISSING_CLASS_EXTENSION("MissingClassExtensionError"),
    WRONG_CLASS_TYPE("ClassTypeError"),
    WRONG_CLASS_NAME("ClassNameError"),
    WRONG_SUPER_CLASS_TYPE("SuperClassTypeError"),
    WRONG_SUPER_CLASS_NAME("SuperClassNameError"),
    DIFFERENT_INTERFACE_NAMES_EXPECTED("DifferentInterfaceNamesExpected"),
    DIFFERENT_CLASS_NAMES_EXPECTED("DifferentClassNamesExpected");

    private final String error;
    ClassFeedbackType(final String error) {
        this.error = error;
    }

    public String toString() {
        return this.error;
    }

}
