package eu.qped.java.checkers.design;

import java.util.*;

/**
 * Utility Class for the checkers, used for tasks that do not require code parsing or feedback generation
 */
public final class CheckerUtils {

    public static final String OPTIONAL_KEYWORD = "*";
    public static final String EMPTY_MODIFIER = "";

    public static final String CONCRETE_CLASS_TYPE = "class";
    public static final String ABSTRACT_CLASS_TYPE = "abstract class";
    public static final String INTERFACE_TYPE = "interface";

    public static final String FIELD_CHECKER = "field";
    public static final String METHOD_CHECKER = "method";

    public static final List<String> possibleAccessModifiers = createAccessList();
    public static final List<String> possibleNonAccessModifiers = createNonAccessList();
    public static final List<String> possibleClassTypes = createClassTypeList();

    private CheckerUtils() { }

    /**
     * Get a list of all access modifiers from expected modifier string list
     * If we have found an access modifier, we remove it from the expected modifiers and continue to iterate through the rest
     * @param expectedModifiers expected modifiers from field or method
     * @return list of all access modifiers from expected modifiers
     */
    public static List<String> getAccessModifiersFromString(List<String> expectedModifiers) {
        List<String> accessModifiers = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            String expectedModifier = "";
            if(possibleAccessModifiers.contains(splitExpected.get(0))) {
                expectedModifier = splitExpected.remove(0);
            }
            accessModifiers.add(expectedModifier);
            replaceKeywordString(splitExpected, expectedModifiers, i);
        }

        return accessModifiers;
    }

    /**
     * Split each string in expectedModifiers and extract all non access modifiers from each string,
     * remove from original string if found such that we can continue to use it for the next methods
     * get all non access modifiers from the expected modifiers
     * @param expectedModifiers expected modifier list
     * @return all non access modifiers as a list
     */
    public static List<List<String>> getNonAccessModifiersFromString(List<String> expectedModifiers) {
        List<List<String>> nonAccessModifiers = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();
            List<String> foundNonAccessModifiers = extractNonAccessFromString(expectedKeywords, expectedModifiers, i);
            nonAccessModifiers.add(foundNonAccessModifiers);
        }

        return nonAccessModifiers;
    }

    /**
     * Get the field type / return type of a method and remove it from the expected modifiers list
     * @param expectedModifiers list to go through and check
     * @return a list of all expected return / field types
     */
    public static List<String> getElementType(List<String> expectedModifiers) {
        List<String> elementTypes = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();
            //Split the expectedKeywords into a string each
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            String elementType = splitExpected.remove(0);
            elementTypes.add(elementType);
            replaceKeywordString(splitExpected, expectedModifiers, i);
        }

        return elementTypes;
    }

    /**
     * Get the name of each expected element after performing all the other keyword extractions
     * @param expectedModifiers expected modifiers, here only containing the name now since the other methods
     *                          removed the previous keywords from all strings inside the list
     * @return list of all names of the elements
     */
    public static List<String> getExpectedElementName(List<String> expectedModifiers) {
        List<String> expectedNames = new ArrayList<>();

        for (String expectedName: expectedModifiers) {
            expectedName = expectedName.trim();
            expectedName = expectedName.replaceAll(";", "");
            expectedNames.add(expectedName);
        }

        return expectedNames;
    }


    /**
     * Extracts the non access modifiers of a string
     * @param expectedKeywordsStr the string to extract the modifiers from
     * @param expectedModifiers list where the string is from
     * @param idx index of the position of the string
     * @return list of all extracted modifiers
     */
    private static List<String> extractNonAccessFromString(String expectedKeywordsStr, List<String> expectedModifiers, int idx) {
        final int MINIMUM_REQUIRED_KEYWORDS = 2;

        List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywordsStr.split("\\s+")));
        List<String> foundModifiers = findModifiers(splitExpected, possibleNonAccessModifiers);
        if(foundModifiers.isEmpty()) {
            if(splitExpected.get(0).equals(OPTIONAL_KEYWORD) && splitExpected.size() > MINIMUM_REQUIRED_KEYWORDS) {
                foundModifiers.add(splitExpected.remove(0));
            } else {
                foundModifiers.add(EMPTY_MODIFIER);
            }

        }
        replaceKeywordString(splitExpected, expectedModifiers, idx);

        return foundModifiers;
    }

    /**
     * Find all modifiers, specified by the possible modifiers list
     * @param expectedSplit split list from the string of modifiers
     * @param possibleModifiers all possible modifiers
     * @return list of found modifiers
     */
    private static List<String> findModifiers(List<String> expectedSplit, List<String> possibleModifiers) {
        List<String> foundModifiers = new ArrayList<>();
        Iterator<String> expectedIterator = expectedSplit.iterator();
        while(expectedIterator.hasNext()) {
            String expectedKeyword = expectedIterator.next();
            if(possibleModifiers.contains(expectedKeyword)) {
                foundModifiers.add(expectedKeyword);
                expectedIterator.remove();
            } else {
                //list doesn't contain the word anymore, so we have found all and leave
                break;
            }
        }

        return foundModifiers;
    }

    /**
     * Replace the String in expectedSplit with the String in expectedModifiers at position idx
     * @param expectedSplit String Words to replace the String with
     * @param expectedModifiers expected modifiers
     * @param idx index to replace at
     */

    private static void replaceKeywordString(List<String> expectedSplit, List<String> expectedModifiers, int idx) {
        String remainingKeywords = String.join(" ", expectedSplit);
        expectedModifiers.set(idx, remainingKeywords);
    }

    /**
     * Extracts the class type and class name from a string. It assumes that the String ends with a Class Name.
     * @param classTypeName string to extract the type and name from
     * @return String array with:
     * - Access Modifier (pos 0)
     * - Type of Class (class / interface) (pos 1)
     * - Name (pos 2)
     */
    public static String[] extractClassNameInfo(String classTypeName) {
        List<String> splitList = Arrays.asList(classTypeName.trim().split("\\s+"));
        List<String> typeNameSplit = new ArrayList<>(splitList);
        String accessMod = "";
        if(possibleAccessModifiers.contains(typeNameSplit.get(0))) {
            accessMod = typeNameSplit.remove(0).toLowerCase();
        }
        List<String> nonAccessMods = findModifiers(typeNameSplit, possibleNonAccessModifiers);
        String nonAccessMod = String.join(" ", nonAccessMods).toLowerCase();
        String name = typeNameSplit.remove(typeNameSplit.size()-1);
        String type = String.join(" ", typeNameSplit).toLowerCase();

        return new String[] {accessMod, nonAccessMod, type, name};
    }

    /**
     * Count occurrences of the optional character in given keywords
     * @param keywords string to count the optional character "*" in
     * @return amount of optional characters found
     */
    public static int countOptionalOccurrences(String keywords) {
        int count = 0;
        for (int i = 0; i < keywords.length(); i++) {
            if(Character.toString(keywords.charAt(i)).equals(OPTIONAL_KEYWORD)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Creates a possible access modifier list, filled with all possible access modifiers
     * @return access modifier list
     */
    private static List<String> createAccessList() {
        List<String> possibleAccess = new ArrayList<>();
        possibleAccess.add(OPTIONAL_KEYWORD);
        possibleAccess.add("public");
        possibleAccess.add("private");
        possibleAccess.add("protected");
        return possibleAccess;
    }

    /**
     * Create possible non access modifier list for fields and methods to check against
     * @return non access modifier list
     */
    private static List<String> createNonAccessList() {
        List<String> possibleNonAccess = new ArrayList<>();
        possibleNonAccess.add("default");
        possibleNonAccess.add("abstract");
        possibleNonAccess.add("static");
        possibleNonAccess.add("final");
        possibleNonAccess.add("synchronized");
        possibleNonAccess.add("transient");
        possibleNonAccess.add("volatile");
        possibleNonAccess.add("native");
        possibleNonAccess.add("strictfp");
        possibleNonAccess.add("transitive");
        return possibleNonAccess;
    }

    private static List<String> createClassTypeList() {
        List<String> possibleClassTypes = new ArrayList<>();
        possibleClassTypes.add("class");
        possibleClassTypes.add("interface");
        return possibleClassTypes;
    }
}
