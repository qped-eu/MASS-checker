package eu.qped.java.checkers.design;

import java.util.HashMap;

public class DesignFeedbackGenerator {

    //Element Feedback
    public final static String WRONG_ELEMENT_NAME = "WrongElementName";
    public final static String WRONG_ELEMENT_TYPE = "WrongElementType";

    //Modifier Feedback
    public final static String WRONG_ACCESS_MODIFIER = "WrongAccessModifier";
    public final static String WRONG_NON_ACCESS_MODIFIER = "WrongNonAccessModifier";

    //Field Feedback
    public final static String MISSING_FIELDS = "MissingFields";
    public final static String FIELDS_NOT_RESTRICTIVE_ENOUGH = "FieldsNotRestrictiveEnough";

    //Method Feedback
    public final static String MISSING_METHODS = "MissingMethods";
    //public final static String METHOD_NOT_IMPLEMENTED = "MethodNotImplemented";
    public final static String METHODS_NOT_RESTRICTIVE_ENOUGH = "MethodsNotRestrictiveEnough";

    //Class Feedback
    public final static String MISSING_INTERFACE_IMPLEMENTATION = "MissingInterfaceImplementation";
    public final static String MISSING_ABSTRACT_CLASS_IMPLEMENTATION = "MissingAbstractClassImplementation";
    public final static String MISSING_CLASS_IMPLEMENTATION = "MissingClassImplementation";
    public final static String WRONG_CLASS_TYPE = "WrongClassType";
    public final static String WRONG_INHERITED_CLASS_TYPE = "WrongInheritedClassType";
    public final static String WRONG_CLASS_NAME = "WrongClassName";

    private final HashMap<String, String> feedbackMap;

    private DesignFeedbackGenerator() {
        feedbackMap = new HashMap<>();
        setUpData();
    }

    public static DesignFeedbackGenerator createDesignFeedbackGenerator() {
        return new DesignFeedbackGenerator();
    }

    private void setUpData() {
        feedbackMap.put(WRONG_ELEMENT_TYPE, "Element does not possess the expected type.");
        feedbackMap.put(WRONG_ELEMENT_NAME, "Element does not possess the expected name.");

        feedbackMap.put(WRONG_ACCESS_MODIFIER, "Access Modifier has not been set properly.");
        feedbackMap.put(WRONG_NON_ACCESS_MODIFIER, "Non Access Modifier has not been set properly.");

        feedbackMap.put(MISSING_FIELDS, "Missing Fields.");
        feedbackMap.put(FIELDS_NOT_RESTRICTIVE_ENOUGH, "Field Access Modifiers are not restrictive enough.");

        feedbackMap.put(MISSING_METHODS, "Missing Methods.");
        //feedbackMap.put(METHOD_NOT_IMPLEMENTED, "Method Implementation missing.");
        feedbackMap.put(METHODS_NOT_RESTRICTIVE_ENOUGH, "Method Access Modifiers are not restrictive enough.");

        feedbackMap.put(MISSING_INTERFACE_IMPLEMENTATION, "Interface has not been implemented.");
        feedbackMap.put(MISSING_ABSTRACT_CLASS_IMPLEMENTATION, "Abstract Class has not been extended");
        feedbackMap.put(MISSING_CLASS_IMPLEMENTATION, "Class has not been extended");
        feedbackMap.put(WRONG_CLASS_TYPE, "Wrong Class Type has been used.");
        feedbackMap.put(WRONG_INHERITED_CLASS_TYPE, "Wrong Class Type has been inherited.");
        feedbackMap.put(WRONG_CLASS_NAME, "Wrong Class Name has been used.");
    }

    private String getFeedbackBody(String violationType) {
        return feedbackMap.get(violationType);
    }

    public DesignFeedback generateFeedback(String className, String elementName, String violationType) {
        return new DesignFeedback(className, elementName, violationType, getFeedbackBody(violationType));
    }

}
