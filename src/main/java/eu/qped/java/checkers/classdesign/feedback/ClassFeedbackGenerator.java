package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.java.utils.markdown.MarkdownFormatterUtility;

import java.util.*;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;

/**
 A class that generates feedback for class design issues.
 The class provides methods that generate feedback messages based on predefined templates, replacing placeholders with specific values.
 @author Jannik Seus
 @see ClassFeedback
 @see ClassFeedbackType
 */
public final class ClassFeedbackGenerator {

    /** Represents the placeholder string for the element in reference*/
    private static final String ELEMENTPLCHOLDER = "%NAME%";
    /** Represents the placeholder string for the class in reference*/

    private static final String CLASS_PLACEHOLDER = "%CLASSNAME%";

    /** Represents the map that assigns every feedback type to a string*/

    private static final Map<ClassFeedbackType, String> FEEDBACK_MAP = createFeedbackMap();
    /** Represents the map that assigns a list of violation checks to a feedback type*/

    public static final Map<List<Boolean>, ClassFeedbackType> VIOLATION_CHECKS = createViolationMap();

    /**
     Private constructor to prevent instantiation
     */
    private ClassFeedbackGenerator() {}

    /**
     * A private method that creates a map containing the different types of feedback messages as values, and the corresponding {@link ClassFeedbackType} as keys.
     * The feedback messages contain placeholders, which are replaced with actual values when the feedback is generated.
     * @return an unmodifiable map containing the feedback messages as values, and the corresponding {@link ClassFeedbackType} as keys.
     */
    private static Map<ClassFeedbackType, String> createFeedbackMap() {
        final Map<ClassFeedbackType, String> feedbackMap = new HashMap<>();

        feedbackMap.put(WRONG_ELEMENT_TYPE,
                "Element "+ ELEMENTPLCHOLDER +" in "+ CLASS_PLACEHOLDER +"  does not possess the expected type.");
        feedbackMap.put(WRONG_ELEMENT_NAME,
                "Element "+ ELEMENTPLCHOLDER +" in "+ CLASS_PLACEHOLDER +"  does not possess the expected name.");
        feedbackMap.put(WRONG_ACCESS_MODIFIER,
                "Different access modifier for "+ ELEMENTPLCHOLDER +" in "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(WRONG_NON_ACCESS_MODIFIER,
                "Different non access modifiers for "+ ELEMENTPLCHOLDER +" in "+ CLASS_PLACEHOLDER +"  expected.");
        feedbackMap.put(WRONG_CLASS_ACCESS_MODIFIER,
                "Different access modifier for "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(WRONG_CLASS_NON_ACCESS_MODIFIER,
                "Different non access modifiers for "+ CLASS_PLACEHOLDER +"  expected.");
        feedbackMap.put(MISSING_FIELDS,
                "Expected fields in "+ CLASS_PLACEHOLDER +" missing.");
        feedbackMap.put(TOO_MANY_FIELDS,
                "The "+ CLASS_PLACEHOLDER +" has more fields than expected.");
//        feedbackMap.put(HIDDEN_FIELD,
//                "The field "+elementPlaceholder+" in "+classPlaceholder+" is hiding a superclass' field.");
        feedbackMap.put(MISSING_METHODS,
                "Expected methods in "+ CLASS_PLACEHOLDER +" missing." );
        feedbackMap.put(TOO_MANY_METHODS,
                "The "+ CLASS_PLACEHOLDER +" has more methods than expected.");
//        feedbackMap.put(OVERWRITTEN_METHOD,
//                "The method "+elementPlaceholder+" in "+classPlaceholder+" is overwriting a method in a parent class.");
//        feedbackMap.put(HIDDEN_METHOD,
//                "The static method "+elementPlaceholder+" in "+classPlaceholder+" is hiding a method in a parent class." );
        feedbackMap.put(MISSING_INTERFACE_IMPLEMENTATION,
                "Expected interface implementation missing in "+ CLASS_PLACEHOLDER +".");
        feedbackMap.put(MISSING_CLASS_EXTENSION,
                "Expected class extension missing in "+ CLASS_PLACEHOLDER +".");
        feedbackMap.put(MISSING_SUPER_CLASS,
                "Expected super class missing in "+ CLASS_PLACEHOLDER +".");
        feedbackMap.put(MISSING_CLASSES,
                "Expected classes missing.");
        feedbackMap.put(WRONG_CLASS_TYPE,
                "Different type for "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(WRONG_CLASS_NAME,
                "Different name for "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(WRONG_SUPER_CLASS_TYPE,
                "Different inherited type for "+ ELEMENTPLCHOLDER +" in "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(DIFFERENT_INTERFACE_NAMES_EXPECTED,
                "Different inherited interface names in "+ CLASS_PLACEHOLDER +" expected.");
        feedbackMap.put(DIFFERENT_CLASS_NAMES_EXPECTED,
                "Different inherited class names in "+ CLASS_PLACEHOLDER +" expected.");
        return Collections.unmodifiableMap(feedbackMap);
    }

    /**

     A method that creates a map of violation checks and their corresponding feedback types.
     The format of the key in the map is a list of boolean values that represent the following checks:
     -access modifier
     -non access modifier
     -type
     -name
     The format of the value in the map is an enumeration of the class {@link ClassFeedbackType}
     @return an unmodifiable map that assigns a list of violation checks to a feedback type
     */
    private static Map<List<Boolean>, ClassFeedbackType> createViolationMap() {
        //Format: (access, non access, type, name) for correctness
        final Map<List<Boolean>, ClassFeedbackType> violationMap = new HashMap<>();
        violationMap.put(Arrays.asList(false, true, true, true), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(false, true, true, false), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(false, true, false, true), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(false, false, true, true), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(false, false, true, false), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(false, false, false, true), WRONG_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(true, false, true, true), WRONG_NON_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(true, false, true, false), WRONG_NON_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(true, false, false, true), WRONG_NON_ACCESS_MODIFIER);
        violationMap.put(Arrays.asList(true, true, false, true), WRONG_ELEMENT_TYPE);
        violationMap.put(Arrays.asList(true, true, false, false), WRONG_ELEMENT_TYPE);
        violationMap.put(Arrays.asList(true, true, true, false), WRONG_ELEMENT_NAME);

        //Since we do not have enough info to determine if the fields are there anymore, we expect them to be missing
        violationMap.put(Arrays.asList(false, false, false, false), MISSING_FIELDS);
        violationMap.put(Arrays.asList(true, false, false, false), MISSING_FIELDS);
        violationMap.put(Arrays.asList(false, true, false, false), MISSING_FIELDS);

        return Collections.unmodifiableMap(violationMap);
    }

    /**
     A private helper method that retrieves the corresponding feedback message for a given violation type.
     @param violationType the type of violation that has occurred, as specified by the enumeration {@link ClassFeedbackType}
     @return the corresponding feedback message as a string, or an empty string if the given violation type is not found in the feedback map.
     */
    private static String getFeedbackBody(final ClassFeedbackType violationType) {
        return FEEDBACK_MAP.getOrDefault(violationType, "");
    }

    /**
     Generates a {@link ClassFeedback} object based on the provided class name, element name, violation type, and custom feedback.
     The generated feedback will include a message describing the violation type, which is obtained from the {@link #FEEDBACK_MAP} and will replace placeholders with class name and element name.
     Additionally, the custom feedback message provided will be appended to the generated feedback.
     @param className the name of the class for which the feedback is generated
     @param elementName the name of the element for which the feedback is generated
     @param violationType the type of violation for which the feedback is generated
     @param customFeedback the custom feedback message to be appended to the generated feedback
     @return a {@link ClassFeedback} object representing the generated feedback
     */
    public static ClassFeedback generateFeedback(final String className, final String elementName, final ClassFeedbackType violationType, final String customFeedback) {
        String feedbackBody = violationType+": "+getFeedbackBody(violationType);
        feedbackBody = feedbackBody.replaceAll(CLASS_PLACEHOLDER, MarkdownFormatterUtility.asBold(className));
        feedbackBody = feedbackBody.replaceAll(ELEMENTPLCHOLDER, MarkdownFormatterUtility.asBold(elementName));

        final String feedbackMsg = feedbackBody + "\n\n" +customFeedback;

        return new ClassFeedback(feedbackMsg);
    }

}
