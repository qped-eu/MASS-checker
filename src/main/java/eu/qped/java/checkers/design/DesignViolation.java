package eu.qped.java.checkers.design;

public class DesignViolation {
    //Modifier Feedback
    public final static String WRONG_ACCESS_MODIFIER = "WRONG_ACCESS_MODIFIER";
    public final static String WRONG_NON_ACCESS_MODIFIER = "WRONG_NON_ACCESS_MODIFIER";

    //Field Feedback
    public final static String MISSING_FIELDS = "MISSING_FIELDS";
    public final static String FIELDS_NOT_RESTRICTIVE_ENOUGH = "FIELDS_NOT_RESTRICTIVE_ENOUGH";

    //Method Feedback
    public final static String MISSING_METHODS = "MISSING_METHODS";
    public final static String METHOD_NOT_IMPLEMENTED = "METHOD_NOT_IMPLEMENTED";
    public final static String METHODS_NOT_RESTRICTIVE_ENOUGH = "METHODS_NOT_RESTRICTIVE_ENOUGH";

    //Class Feedback
    public final static String MISSING_INTERFACE_IMPLEMENTATION = "MISSING_INTERFACE_IMPLEMENTATION";
    public final static String MISSING_ABSTRACT_CLASS_IMPLEMENTATION = "MISSING_ABSTRACT_CLASS_IMPLEMENTATION";
    public final static String MISSING_CLASS_IMPLEMENTATION = "MISSING_CLASS_IMPLEMENTATION";
    public final static String WRONG_CLASS_TYPE = "WRONG_CLASS_TYPE";
    public final static String WRONG_INHERITED_CLASS_TYPE = "WRONG_INHERITED_CLASS_TYPE";
    public final static String WRONG_CLASS_NAME = "WRONG_CLASS_NAME";
}
