package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.java.checkers.classdesign.enums.ClassFeedbackType;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;

import java.util.*;

import static eu.qped.java.checkers.classdesign.enums.ClassFeedbackType.*;

public final class ClassFeedbackGenerator {

    private static final String elementPlaceholder = "%NAME%";
    private static final String classPlaceholder = "%CLASSNAME%";

    private static final Map<ClassFeedbackType, String> FEEDBACK_MAP = createFeedbackMap();
    public static final Map<List<Boolean>, ClassFeedbackType> VIOLATION_CHECKS = createViolationMap();

    private ClassFeedbackGenerator() {}

    private static Map<ClassFeedbackType, String> createFeedbackMap() {
        Map<ClassFeedbackType, String> feedbackMap = new HashMap<>();

        feedbackMap.put(WRONG_ELEMENT_TYPE,
                "Element "+ elementPlaceholder +" in "+classPlaceholder+"  does not possess the expected type.\n" +
                "Is the type of \""+elementPlaceholder+"\" set according to the task description?");
        feedbackMap.put(WRONG_ELEMENT_NAME,
                "Element "+elementPlaceholder+" in "+classPlaceholder+"  does not possess the expected name.\n" +
                "Is the name of "+elementPlaceholder+"  set according to the task description?");
        feedbackMap.put(WRONG_ACCESS_MODIFIER,
                "Different access modifier for "+elementPlaceholder+" in "+classPlaceholder+" expected.\n" +
                "Is the access modifier (e.g. public, private, protected, ...) of "+elementPlaceholder+" set according to the task description?");
        feedbackMap.put(WRONG_NON_ACCESS_MODIFIER,
                "Different non access modifiers for "+elementPlaceholder+" in "+classPlaceholder+"  expected.\n" +
                "Are the non access modifiers (e.g. static, final, abstract, ...) of \""+elementPlaceholder+"\" set according to the task description?");
        feedbackMap.put(WRONG_CLASS_ACCESS_MODIFIER,
                "Different access modifier for "+classPlaceholder+" expected.\n" +
                "Is the access modifier (e.g. public, ...) of "+classPlaceholder+" set according to the task description?");
        feedbackMap.put(WRONG_CLASS_NON_ACCESS_MODIFIER,
                "Different non access modifiers for "+classPlaceholder+"  expected.\n" +
                "Are the non access modifiers (e.g. abstract, final, ...) of "+classPlaceholder+" set according to the task description?");
        feedbackMap.put(MISSING_FIELDS,
                "Expected fields in "+classPlaceholder+" missing.\n" +
                "Do all fields, mentioned in the task description, exist in "+classPlaceholder+"?");
        feedbackMap.put(HIDDEN_FIELD,
                "The field "+elementPlaceholder+" in "+classPlaceholder+" is hiding a superclass' field.\n" +
                "Have you tried renaming "+elementPlaceholder+" so that you can access the superclass field as well?");
        feedbackMap.put(MISSING_METHODS,
                "Expected methods in "+classPlaceholder+" missing.\n" +
                "Do all methods, mentioned in the task description, exist in \""+classPlaceholder+"\"?");
        feedbackMap.put(OVERWRITTEN_METHOD,
                "The method "+elementPlaceholder+" in "+classPlaceholder+" is overwriting a method in a parent class.\n" +
                "Have you tried using the implemented method in the parent class of "+classPlaceholder+" instead?");
        feedbackMap.put(HIDDEN_METHOD,
                "The static method "+elementPlaceholder+" in "+classPlaceholder+" is hiding a method in a parent class.\n" +
                "Have you tried renaming "+elementPlaceholder+", so that the method in the parent class of "+classPlaceholder+" is not hidden?");
        feedbackMap.put(MISSING_INTERFACE_IMPLEMENTATION,
                "Expected interface implementation missing in "+classPlaceholder+".\n" +
                "Has the interface, mentioned in the task, been implemented in "+classPlaceholder+"?");
        feedbackMap.put(MISSING_CLASS_EXTENSION,
                "Expected class extension missing in "+classPlaceholder+".\n" +
                "Have the required classes, mentioned in the task, been extended in "+classPlaceholder+"?");
        feedbackMap.put(WRONG_CLASS_TYPE,
                "Different type for "+classPlaceholder+" expected.\n" +
                "Is the type of "+classPlaceholder+" set according to the task description?");
        feedbackMap.put(WRONG_CLASS_NAME,
                "Different name for "+classPlaceholder+" expected.\n" +
                "Is the name of "+classPlaceholder+" set according to the task description?");
        feedbackMap.put(WRONG_SUPER_CLASS_TYPE,
                "Different inherited type for "+elementPlaceholder+" in "+classPlaceholder+" expected.\n" +
                "Does the inherited class "+elementPlaceholder+" in "+classPlaceholder+" have the class type set according to the task description?");
        feedbackMap.put(WRONG_SUPER_CLASS_NAME,
                "Different inherited name for "+elementPlaceholder+" in "+classPlaceholder+" expected.\n" +
                "Does the inherited class "+elementPlaceholder+" in "+classPlaceholder+" have the name set according to the task description?");
        feedbackMap.put(DIFFERENT_INTERFACE_NAMES_EXPECTED,
                "Different inherited interface names in "+classPlaceholder+" expected.\n" +
                "Do the implemented interfaces in "+classPlaceholder+" have the name set according to the task description?");
        feedbackMap.put(DIFFERENT_CLASS_NAMES_EXPECTED,
                "Different inherited class names in "+classPlaceholder+" expected.\n" +
                "Do the extended classes in "+classPlaceholder+" have the name set according to the task description?");

        return Collections.unmodifiableMap(feedbackMap);
    }

    private static Map<List<Boolean>, ClassFeedbackType> createViolationMap() {
        //Format: (access, non access, type, name) for correctness
        Map<List<Boolean>, ClassFeedbackType> violationMap = new HashMap<>();
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

    private static String getFeedbackBody(ClassFeedbackType violationType) {
        return FEEDBACK_MAP.getOrDefault(violationType, "");
    }

    public static ClassFeedback generateFeedback(String className, String elementName, ClassFeedbackType violationType, String customFeedback) {
        String feedbackBody = violationType+": "+getFeedbackBody(violationType);
        feedbackBody = feedbackBody.replaceAll(classPlaceholder, MarkdownFormatterUtility.asBold(className));
        feedbackBody = feedbackBody.replaceAll(elementPlaceholder, MarkdownFormatterUtility.asBold(elementName));

        String feedbackMsg = feedbackBody + "\n\n" +customFeedback;

        return new ClassFeedback(feedbackMsg);
    }

}
