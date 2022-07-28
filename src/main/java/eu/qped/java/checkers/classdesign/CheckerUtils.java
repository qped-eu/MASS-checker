package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Modifier;
import eu.qped.java.checkers.classdesign.config.*;
import eu.qped.java.checkers.classdesign.enums.KeywordChoice;
import eu.qped.java.checkers.classdesign.exceptions.NoModifierException;
import eu.qped.java.checkers.classdesign.infos.*;

import java.util.*;

/**
 * Utility Class for the checkers, useful for tasks that do not require code parsing or feedback generation
 */
public final class CheckerUtils {

    private CheckerUtils() { }

    /**
     * Extract all possible modifiers, type and name from the field configuration provided by json
     * @param keywordConfig field keyword modifiers from json
     * @return expected element with all possible modifiers
     */

    public static ExpectedElement extractExpectedInfo(KeywordConfig keywordConfig) throws NoModifierException {
        List<String> accessMod = new ArrayList<>();
        fillWithPossibleModifiers(keywordConfig.getAccessModifierMap(), accessMod);

        List<String> nonAccessMods = new ArrayList<>();
        boolean containsYes = fillWithPossibleModifiers(keywordConfig.getNonAccessModifierMap(), nonAccessMods);

        List<String> type = getPossibleTypes(keywordConfig);
        String name = getNameFromConfig(keywordConfig);
        boolean allowExactMatch = keywordConfig.isAllowExactModifierMatching();
        return new ExpectedElement(accessMod, nonAccessMods, type, name, allowExactMatch, containsYes);
    }

    private static boolean fillWithPossibleModifiers(Map<String, String> keywordChoiceMap, List<String> possibleMods) throws NoModifierException {
        boolean containsYes = false;
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
        if(possibleMods.isEmpty()) {
            throw new NoModifierException();
        }

        return containsYes;
    }

    private static List<String> getPossibleTypes(KeywordConfig keywordConfig) {
        return keywordConfig.getPossibleTypes();
    }

    private static String getNameFromConfig(KeywordConfig keywordConfig) {
        String name = keywordConfig.getName().trim();
        name = name.replaceAll(";", "");
        int pos = name.indexOf("(");
        name = pos != -1 ? name.substring(0, pos) : name;
        return name;
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

    /**
     * Compares the expected non access modifiers with the modifiers from the present element
     * @param presentModifiers present modifiers to check
     * @param expectedNonAccessModifiers expected modifiers to compare to
     * @return true, if the expected non access modifiers match up with the actual non access modifiers
     */
    public static boolean isNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedNonAccessModifiers,
                                           boolean isExactMatch,
                                           boolean containsYes) {
        List<String> actualModifiers = getActualNonAccessModifiers(presentModifiers);
        if(!isExactMatch || !containsYes) {
            return expectedNonAccessModifiers.containsAll(actualModifiers);
        }

        if(expectedNonAccessModifiers.size() > 1) {
            expectedNonAccessModifiers.remove("");
        }
        return expectedNonAccessModifiers.containsAll(actualModifiers) && actualModifiers.containsAll(expectedNonAccessModifiers);
    }

    private static List<String> getActualNonAccessModifiers(List<Modifier> presentModifiers) {
        List<String> actualModifiers = new ArrayList<>();
        List<String> possibleAccessModifiers = createAccessList();
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

    /**
     * Compare two boolean lists with their amount of "true" in each. If both have the same amount, the one with the first
     * false comes first.
     * @param firstMatch First boolean list
     * @param secondMatch Second boolean list
     * @return ordering of the lists
     */

    public static int compareMatchingLists(List<Boolean> firstMatch, List<Boolean> secondMatch) {
        int countCurrent = 0;
        int countAccepted = 0;

        for (int i = 0; i < firstMatch.size(); i++) {
            if (firstMatch.get(i)) {
                countCurrent++;
            }
            if (secondMatch.get(i)) {
                countAccepted++;
            }
        }

        if (countCurrent == countAccepted && countCurrent > 0) {
            for (int i = 0; i < firstMatch.size(); i++) {
                if (!firstMatch.get(i).equals(secondMatch.get(i))) {
                    //Find the one that's false first, this is the one that has to be treated first!
                    if (!firstMatch.get(i)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }
        return countAccepted - countCurrent;
    }
}
