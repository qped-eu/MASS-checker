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
        final List<String> accessMod = new ArrayList<>();
        fillWithPossibleModifiers(keywordConfig.getAccessModifierMap(), accessMod);

        final List<String> nonAccessMods = new ArrayList<>();
        boolean containsYes = fillWithPossibleModifiers(keywordConfig.getNonAccessModifierMap(), nonAccessMods);

        final List<String> type = getPossibleTypes(keywordConfig);
        final String name = getNameFromConfig(keywordConfig);
        final boolean allowExactMatch = keywordConfig.isAllowExactModifierMatching();
        return new ExpectedElement(accessMod, nonAccessMods, type, name, allowExactMatch, containsYes);
    }

    /**
     * Fills a list with possible modifiers, based on the choices YES, DON'T CARE OR NO
     * @param keywordChoiceMap map for all choices and their modifiers
     * @param possibleMods fills this list with all possible modifiers
     * @return if the modifier choices contain at least one yes
     * @throws NoModifierException if ever modifier has been chosen with NO, an exception is being thrown, since that would mean
     * that every modifier is not allowed.
     */
    private static boolean fillWithPossibleModifiers(Map<String, KeywordChoice> keywordChoiceMap, List<String> possibleMods) throws NoModifierException {
        boolean containsYes = false;
        String modifier = ""; // modifiers und choice sollten zuerst deklariert und initialisiert werden. Sie müsse nur einmal erstellt werden und brauchen nicht immer neu erstellt zu werden nach jedem Aufruf.
        KeywordChoice choice;
        for (Map.Entry<String, KeywordChoice> entry: keywordChoiceMap.entrySet()) {
            // String modifier = entry.getKey();
            // String choice = entry.getKey();
            modifier = entry.getKey(); // Hier werden einfach die werte der varaiblen nach jedem Aufruf nur geholt und nicht neu erstellt. Das ist effizienter
            choice = entry.getValue();

            if(choice.equals(KeywordChoice.YES)) {
                possibleMods.add(modifier);
                containsYes = true;
            }
        }

        if(!containsYes) {
            for (Map.Entry<String, KeywordChoice> entry: keywordChoiceMap.entrySet()) {
                modifier = entry.getKey(); // string gelöscht
                choice = entry.getValue(); // string gelöscht

                if(!choice.equals(KeywordChoice.NO)) {
                    possibleMods.add(modifier);
                }
            }
        }
        if(possibleMods.isEmpty()) {
            throw new NoModifierException();
        }

        return containsYes;
    }

    /**
     * @param keywordConfig field keyword modifiers from json
     * @return List of all possible types of the keyword
     */
    private static List<String> getPossibleTypes(KeywordConfig keywordConfig) {
        return keywordConfig.getPossibleTypes();
    }

    /**
     * take the String variable from keywordConfig and replace the Semicolon with an empty String to get a pure name
     * @param keywordConfig field keyword modifiers from json
     * @return name of the keyword modifiers from json
     */
    private static String getNameFromConfig(KeywordConfig keywordConfig) {
        String name = keywordConfig.getName().trim();
        name = name.replaceAll(";", "");
        final int pos = name.indexOf('('); //die varaible pos sollte final sein, da sie unverändert bleibt. //Laufzeit verbessern anstatt indexOf("(") haben wir 40
        //name = pos != -1 ? name.substring(0, pos) : name;
        name = pos == -1 ? name : name.substring(0,pos);
        /*
        Die meisten "if (x != y)"-Fälle ohne "else" sind oft Rückgabefälle,
        so dass die konsequente Anwendung dieser Regel den Code leichter lesbar macht.
         */
        return name;
    }

    public static void main(String[] args) {
        KeywordConfig keywordConfig = new KeywordConfig() {
            @Override
            public List<String> getPossibleTypes() {
                return null;
            }
        };
        keywordConfig.setName("Russia-Snezhnaya;");
        System.out.println(getNameFromConfig(keywordConfig));
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
            return expectedNonAccessModifiers.containsAll(actualModifiers); // expectedNAM soll als Hashset zurückgegeben werden, um die Leistun zu verbessern?!!
        }
        if(expectedNonAccessModifiers.size() > 1) {
            expectedNonAccessModifiers.remove("");
        }
        return expectedNonAccessModifiers.containsAll(actualModifiers) && actualModifiers.containsAll(expectedNonAccessModifiers);
    }

    /**
     * Check if the modifiers have non access modifiers
     * @param presentModifiers present modifiers to check
     * @return List filled with all non-actual access modifiers
     */
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
        possibleAccess.addAll(Arrays.asList("public","private","protected"));
        /*
        possibleAccess.add("public");
        possibleAccess.add("private");
        possibleAccess.add("protected");
         */
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
            if (secondMatch.get(i)) { //if Bedingungen um schreiben, nur if und ein else. Ansonsten if, else-if und else
                countAccepted++;
            }
        }

        if ((countCurrent == countAccepted) && (countCurrent > 0)) { //Hier gibt es zwei Bedingungen, die nicht getrennt sind und das führt zu Irreterum, deswegen haben iwr die beiden mit Klammern getrennt.
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
