package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.java.utils.markdown.MarkdownFormatterUtility;

import java.util.*;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;

/** A class that generates feedback */
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
     * Constructor for the class FeedbackGenerator
     */
    private ClassFeedbackGenerator() {}

    /**
     * A method that assigns every feedback type to a message explaining the problem
     * @return a map using the class ClassFeedbackType and String
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
     * A method that assigns every violation type to a message explaining the problem
     * @return a map using a list of Boolean values representing the violation
     * and a String object that represents the message
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
     * A method that generates a feedback message for a given violation
     * @param violationType the object with the violation type
     * @return the corresponding value in the map, the message
     */
    private static String getFeedbackBody(final ClassFeedbackType violationType) {
        return FEEDBACK_MAP.getOrDefault(violationType, "");
    }

    /**
     * A method creating a complete feedback text
     * @param className the name of the class where the violation took place
     * @param elementName the name of the element
     * @param violationType the type of the violation that occurred
     * @param customFeedback a string that is customizable
     * @return the complete text as a ClassFeedback object
     */
    public static ClassFeedback generateFeedback(final String className, final String elementName, final ClassFeedbackType violationType, final String customFeedback) {
        String feedbackBody = violationType+": "+getFeedbackBody(violationType);
        feedbackBody = feedbackBody.replaceAll(CLASS_PLACEHOLDER, MarkdownFormatterUtility.asBold(className));
        feedbackBody = feedbackBody.replaceAll(ELEMENTPLCHOLDER, MarkdownFormatterUtility.asBold(elementName));

        final String feedbackMsg = feedbackBody + "\n\n" +customFeedback;

        return new ClassFeedback(feedbackMsg);
    }

}
