package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.classdesign.config.*;
import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.infos.*;

import java.util.*;

/**
 * Utility Class for the checkers, useful for tasks that do not require code parsing or feedback generation
 */
public final class CheckerUtils {

    public static final List<String> possibleAccessModifiers = createAccessList();

    private CheckerUtils() { }

    public static List<String> getPossibleAccessModifiers(KeywordConfig keywordConfig) {
        return getPossibleModifiers(keywordConfig.getAccessModifierMap());
    }

    public static List<String> getPossibleNonAccessModifiers(KeywordConfig keywordConfig) {
        return getPossibleModifiers(keywordConfig.getNonAccessModifierMap());
    }

    public static String getTypeFromConfig(KeywordConfig keywordConfig) {
        return keywordConfig.getType();
    }

    public static List<String> getPossibleModifiers(Map<String, String> keywordChoiceMap) {
        boolean containsYes = false;
        List<String> possibleMods = new ArrayList<>();
        for (Map.Entry<String, String> entry: keywordChoiceMap.entrySet()) {
            String modifier = entry.getKey();
            String choice = entry.getValue();

            if(choice.equals(KeywordChoice.YES.toString())) {
                possibleMods.add(modifier);
                containsYes = true;
            }
        }

        if(!containsYes) {
            for (Map.Entry<String, String> entry: keywordChoiceMap.entrySet()) {
                String modifier = entry.getKey();
                String choice = entry.getValue();

                if(!choice.equals(KeywordChoice.NO.toString())) {
                    possibleMods.add(modifier);
                }
            }
        }
        return possibleMods;
    }


    public static String getNameFromConfig(KeywordConfig keywordConfig) {
        return keywordConfig.getName();
    }

    public static boolean getAllowExactMatch(KeywordConfig keywordConfig) {
        return Boolean.parseBoolean(keywordConfig.getAllowExactModifierMatching());
    }

    /**
     * Extract all possible modifiers, type and name from the field configuration provided by json
     * @param keywordConfig field keyword modifiers from json
     * @return expected element with all possible modifiers
     */

    public static ExpectedElement extractExpectedInfo(KeywordConfig keywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(keywordConfig);
        List<String> nonAccessMods = getPossibleNonAccessModifiers(keywordConfig);
        String type = getTypeFromConfig(keywordConfig);
        String name = getNameFromConfig(keywordConfig);
        boolean allowExactMatch = getAllowExactMatch(keywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name, allowExactMatch);
    }



    /**
     * Checks if the expected access modifier matches up with the present element access modifier
     * @param presentAccessMod access modifier of the present element
     * @param expectedAccessModifiers expected access modifier from class info
     * @return true, if present and expected match up
     */
    public static boolean isAccessMatch(String presentAccessMod, List<String> expectedAccessModifiers) {
        return expectedAccessModifiers.contains(presentAccessMod.trim());
    }

    public static boolean isNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedNonAccessModifiers, boolean isExactMatch) {
        List<String> actualModifiers = getActualNonAccessModifiers(presentModifiers);
        if(!isExactMatch) {
            return expectedNonAccessModifiers.containsAll(actualModifiers);
        }

        if(expectedNonAccessModifiers.size() > 1) {
            expectedNonAccessModifiers.remove("");
        }
        return expectedNonAccessModifiers.containsAll(actualModifiers) && actualModifiers.containsAll(expectedNonAccessModifiers);
    }

    /**
     * Compares the expected non access modifiers with the modifiers from the present element
     * @param presentModifiers present modifiers to check
     * @param expectedNonAccessModifiers expected modifiers to compare to
     * @return true, if the expected non access modifiers match up with the actual non access modifiers
     */
    public static boolean isNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedNonAccessModifiers) {
        List<String> actualModifiers = getActualNonAccessModifiers(presentModifiers);
        return expectedNonAccessModifiers.containsAll(actualModifiers);
    }

    private static List<String> getActualNonAccessModifiers(List<Modifier> presentModifiers) {
        List<String> actualModifiers = new ArrayList<>();
        for (Modifier modifier: presentModifiers) {
            String modifierName = modifier.getKeyword().asString().trim();
            if(possibleAccessModifiers.contains(modifierName)) {
                continue;
            }
            actualModifiers.add(modifierName);
        }

        if(actualModifiers.isEmpty()) {
            actualModifiers.add("");
        }
        return actualModifiers;
    }

    /**
     * Creates a possible access modifier list, filled with all possible access modifiers
     * @return access modifier list
     */
    private static List<String> createAccessList() {
        List<String> possibleAccess = new ArrayList<>();
        possibleAccess.add("public");
        possibleAccess.add("private");
        possibleAccess.add("protected");
        return possibleAccess;
    }

}
