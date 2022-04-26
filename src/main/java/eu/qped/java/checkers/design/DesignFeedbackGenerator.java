package eu.qped.java.checkers.design;

import java.util.HashMap;

public class DesignFeedbackGenerator {

    private final HashMap<String, String> feedbackMap;

    private DesignFeedbackGenerator() {
        feedbackMap = new HashMap<>();
        setUpData();
    }

    public static DesignFeedbackGenerator createDesignFeedbackGenerator() {
        return new DesignFeedbackGenerator();
    }

    private void setUpData() {
        feedbackMap.put(DesignViolation.WRONG_ACCESS_MODIFIER, "Access Modifier has not been set properly.");
        feedbackMap.put(DesignViolation.WRONG_NON_ACCESS_MODIFIER, "Non Access Modifier has not been set properly.");

        feedbackMap.put(DesignViolation.MISSING_FIELDS, "Missing Fields.");
        feedbackMap.put(DesignViolation.FIELDS_NOT_RESTRICTIVE_ENOUGH, "Field Access Modifiers are not restrictive enough.");

        feedbackMap.put(DesignViolation.MISSING_METHODS, "Missing Methods.");
        feedbackMap.put(DesignViolation.METHOD_NOT_IMPLEMENTED, "Method Implementation missing.");
        feedbackMap.put(DesignViolation.METHODS_NOT_RESTRICTIVE_ENOUGH, "Method Access Modifiers are not restrictive enough.");

        feedbackMap.put(DesignViolation.MISSING_INTERFACE_IMPLEMENTATION, "Interface has not been implemented.");
        feedbackMap.put(DesignViolation.MISSING_ABSTRACT_CLASS_IMPLEMENTATION, "Abstract Class has not been extended");
        feedbackMap.put(DesignViolation.MISSING_CLASS_IMPLEMENTATION, "Class has not been extended");
        feedbackMap.put(DesignViolation.WRONG_CLASS_TYPE, "Wrong Class Type has been used.");
        feedbackMap.put(DesignViolation.EXPECTED_DIFF_CLASS_TYPE, "Expected different class or interface type.");
    }

    public String getFeedbackBody(String violationType) {
        return feedbackMap.get(violationType);
    }

    public DesignFeedback generateFeedback(String violationType, String identifier) {
        return new DesignFeedback(violationType, identifier, getFeedbackBody(violationType));
    }

}
