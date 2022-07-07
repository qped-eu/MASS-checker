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
        Map<String, String> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("public", keywordConfig.getPublicModifier());
        keywordChoiceMap.put("protected", keywordConfig.getProtectedModifier());
        keywordChoiceMap.put("private", keywordConfig.getPrivateModifier());
        keywordChoiceMap.put("", keywordConfig.getPackagePrivateModifier());

        return getPossibleModifiers(keywordChoiceMap);
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
     * @param fieldKeywordConfig field keyword modifiers from json
     * @return expected element with all possible modifiers
     */

    public static ExpectedElement extractExpectedFieldInfo(FieldKeywordConfig fieldKeywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(fieldKeywordConfig);
        List<String> nonAccessMods = getNonAccessModifiersFromFieldConfig(fieldKeywordConfig);
        String type = getTypeFromFieldConfig(fieldKeywordConfig);
        String name = getNameFromConfig(fieldKeywordConfig);
        boolean allowExactMatch = getAllowExactMatch(fieldKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name, allowExactMatch);
    }

    public static List<String> getNonAccessModifiersFromFieldConfig(FieldKeywordConfig keywordConfig) {
        List<String> nonAccessMods;
        Map<String, String> keywordChoiceMap = getCommonNonAccessModifiers(keywordConfig);
//        keywordChoiceMap.put("static", keywordConfig.getStaticModifier());
//        keywordChoiceMap.put("final", keywordConfig.getFinalModifier());
//        keywordChoiceMap.put("", keywordConfig.getEmptyNonAccessModifier());
        keywordChoiceMap.put("transient", keywordConfig.getTransientModifier());
        keywordChoiceMap.put("volatile", keywordConfig.getVolatileModifier());

        nonAccessMods = getPossibleModifiers(keywordChoiceMap);
        return nonAccessMods;
    }

    public static String getTypeFromFieldConfig(FieldKeywordConfig fieldKeywordConfig) {
        return fieldKeywordConfig.getFieldType();
    }

    /**
     * Extract all possible modifiers, type and name from the method configuration provided by json
     * @param methodKeywordConfig method keyword configuration from json
     * @return expected element with all possible modifiers
     */

    public static ExpectedElement extractExpectedMethodInfo(MethodKeywordConfig methodKeywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(methodKeywordConfig);
        List<String> nonAccessMods = getNonAccessModifiersFromMethodConfig(methodKeywordConfig);
        String type = getTypeFromMethodConfig(methodKeywordConfig);
        String name = getNameFromConfig(methodKeywordConfig);
        boolean allowExactMatch = getAllowExactMatch(methodKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name, allowExactMatch);
    }


    public static List<String> getNonAccessModifiersFromMethodConfig(MethodKeywordConfig keywordConfig) {
        Map<String, String> keywordChoiceMap = getCommonNonAccessModifiers(keywordConfig);
//        keywordChoiceMap.put("static", keywordConfig.getStaticModifier());
//        keywordChoiceMap.put("final", keywordConfig.getFinalModifier());
//        keywordChoiceMap.put("", keywordConfig.getEmptyNonAccessModifier());
        keywordChoiceMap.put("abstract", keywordConfig.getAbstractModifier());
        keywordChoiceMap.put("synchronized", keywordConfig.getSynchronizedModifier());
        keywordChoiceMap.put("native", keywordConfig.getNativeModifier());
        keywordChoiceMap.put("default", keywordConfig.getDefaultModifier());

        return getPossibleModifiers(keywordChoiceMap);
    }

    public static String getTypeFromMethodConfig(MethodKeywordConfig keywordConfig) {
        return keywordConfig.getMethodType();
    }



    /**
     * Class Info Stuff
     * @param classKeywordConfig class keyword configuration from json
     * @return expected element with all possible modifiers
     */
    public static ExpectedElement extractExpectedClassInfo(ClassKeywordConfig classKeywordConfig) {
        List<String> possibleAccess = getPossibleAccessModifiers(classKeywordConfig);
        Map<String, String> keywordChoiceMap = getCommonNonAccessModifiers(classKeywordConfig);
//        keywordChoiceMap.put("static", classKeywordConfig.getStaticModifier());
//        keywordChoiceMap.put("final", classKeywordConfig.getFinalModifier());
//        keywordChoiceMap.put("", classKeywordConfig.getEmptyNonAccessModifier());
        keywordChoiceMap.put("abstract", classKeywordConfig.getAbstractModifier());
        List<String> nonAccessMods = getPossibleModifiers(keywordChoiceMap);


        String type = getTypeFromClassConfig(classKeywordConfig);
        String name = getNameFromConfig(classKeywordConfig);

        boolean allowExactMatch = getAllowExactMatch(classKeywordConfig);
        return new ExpectedElement(possibleAccess, nonAccessMods, type, name, allowExactMatch);
    }

    public static String getTypeFromClassConfig(ClassKeywordConfig classKeywordConfig) {
        return classKeywordConfig.getInterfaceType().equals(KeywordChoice.YES.toString()) ? ClassType.INTERFACE.toString() : ClassType.CLASS.toString();
    }

    /**
     * Inherits From Info Stuff
     * @param keywordConfig keyword configuration about inherited classes from json
     * @return expected element with all possible modifiers
     */
    public static ExpectedElement extractExpectedInheritsFromInfo(InheritsFromConfig keywordConfig) {
        String type = getTypeFromInheritsConfig(keywordConfig);
        String name = getNameFromConfig(keywordConfig);
        return new ExpectedElement(new ArrayList<>(), new ArrayList<>(), type, name, false);
    }

    public static String getTypeFromInheritsConfig(InheritsFromConfig keywordConfig) {
        return keywordConfig.getInterfaceType().equals(KeywordChoice.YES.toString()) ? ClassType.INTERFACE.toString() : ClassType.CLASS.toString();
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

    public static boolean isExactNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedNonAccessModifiers) {
        List<String> actualModifiers = getActualNonAccessModifiers(presentModifiers);
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

    private static Map<String, String> getCommonNonAccessModifiers(KeywordConfig keywordConfig) {
        Map<String, String> keywordChoiceMap = new HashMap<>();
        keywordChoiceMap.put("static", keywordConfig.getStaticModifier());
        keywordChoiceMap.put("final", keywordConfig.getFinalModifier());
        keywordChoiceMap.put("", keywordConfig.getEmptyNonAccessModifier());
        return keywordChoiceMap;
    }

    public static String getAllowedAccess(List<String> modifiers, String disallowedMod) {
        String mod = disallowedMod;
        while(mod.equals(disallowedMod)) {
            int rnd = new Random().nextInt(modifiers.size());
            mod = modifiers.get(rnd);
        }
        return mod;
    }

    public static List<String> getAllowedNonAccess(List<String> modifiers, List<String> disallowedNonAccess) {
        List<String> allowedMods = new ArrayList<>();
        for (String modifier : modifiers) {
            if(!disallowedNonAccess.contains(modifier)) {
                allowedMods.add(modifier);
            }
        }
        return allowedMods;
    }
}
