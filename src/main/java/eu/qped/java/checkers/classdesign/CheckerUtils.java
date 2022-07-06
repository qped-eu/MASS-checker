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
        List<String> possibleAccessMods = new ArrayList<>();
        boolean containsYes = false;

        //If we have yes here, we include these
        if(keywordConfig.getPublicModifier().equals(KeywordChoice.YES.toString())) {
            possibleAccessMods.add("public");
            containsYes = true;
        }
        if(keywordConfig.getProtectedModifier().equals(KeywordChoice.YES.toString())) {
            possibleAccessMods.add("protected");
            containsYes = true;
        }
        if(keywordConfig.getPrivateModifier().equals(KeywordChoice.YES.toString())) {
            possibleAccessMods.add("private");
            containsYes = true;
        }
        if(keywordConfig.getPackagePrivateModifier().equals(KeywordChoice.YES.toString())) {
            possibleAccessMods.add("");
            containsYes = true;
        }

        //If we don't have any yes and only no's, we take everything that is not no
        if(!containsYes) {
            if(!keywordConfig.getPublicModifier().equals(KeywordChoice.NO.toString())) {
                possibleAccessMods.add("public");
            }
            if(!keywordConfig.getProtectedModifier().equals(KeywordChoice.NO.toString())) {
                possibleAccessMods.add("protected");
            }
            if(!keywordConfig.getPrivateModifier().equals(KeywordChoice.NO.toString())) {
                possibleAccessMods.add("private");
            }
            if(!keywordConfig.getPackagePrivateModifier().equals(KeywordChoice.NO.toString())) {
                possibleAccessMods.add("");
            }
        }
        return possibleAccessMods;
    }

    public static List<String> getCommonPossibleNonAccessModifiers(KeywordConfig keywordConfig) {
        List<String> nonAccessMods = new ArrayList<>();
        boolean containsYes = false;

        if(keywordConfig.getAbstractModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("abstract");
            containsYes = true;
        }
        if(keywordConfig.getStaticModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("static");
            containsYes = true;
        }
        if(keywordConfig.getFinalModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("final");
            containsYes = true;
        }

        if(!containsYes) {
            if(!keywordConfig.getAbstractModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("abstract");
            }
            if(!keywordConfig.getStaticModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("static");
            }
            if(!keywordConfig.getFinalModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("final");
            }
        }

        return nonAccessMods;
    }

    public static String getNameFromConfig(KeywordConfig keywordConfig) {
        return keywordConfig.getName();
    }

    /**
     * Extract all possible modifiers, type and name from the field configuration provided by json
     * @param fieldKeywordConfig field keyword modifiers from json
     * @return expected element with all possible modifiers
     */

    public static ExpectedElement extractExpectedFieldInfo(FieldKeywordConfig fieldKeywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(fieldKeywordConfig);
        List<String> nonAccessMods = getCommonPossibleNonAccessModifiers(fieldKeywordConfig);
        nonAccessMods.addAll(getNonAccessModifiersFromFieldConfig(fieldKeywordConfig));
        String type = getTypeFromFieldConfig(fieldKeywordConfig);
        String name = getNameFromConfig(fieldKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name);
    }

    public static List<String> getNonAccessModifiersFromFieldConfig(FieldKeywordConfig keywordConfig) {
        List<String> nonAccessMods = getCommonPossibleNonAccessModifiers(keywordConfig);
        boolean containsYes = false;

        if(keywordConfig.getTransientModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("transient");
            containsYes = true;
        }
        if(keywordConfig.getVolatileModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("volatile");
            containsYes = true;
        }

        if(!containsYes) {
            if(!keywordConfig.getTransientModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("transient");
            }
            if(!keywordConfig.getVolatileModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("volatile");
            }
        }

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
        List<String> nonAccessMods = getCommonPossibleNonAccessModifiers(methodKeywordConfig);
        nonAccessMods.addAll(getNonAccessModifiersFromMethodConfig(methodKeywordConfig));
        String type = getTypeFromMethodConfig(methodKeywordConfig);
        String name = getNameFromConfig(methodKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name);
    }


    public static List<String> getNonAccessModifiersFromMethodConfig(MethodKeywordConfig keywordConfig) {
        List<String> nonAccessMods = new ArrayList<>();
        boolean containsYes = false;
        if(keywordConfig.getSynchronizedModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("synchronized");
            containsYes = true;
        }
        if(keywordConfig.getNativeModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("native");
            containsYes = true;
        }
        if(keywordConfig.getDefaultModifier().equals(KeywordChoice.YES.toString())) {
            nonAccessMods.add("default");
            containsYes = true;
        }

        if(!containsYes) {
            if(!keywordConfig.getSynchronizedModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("synchronized");
            }
            if(!keywordConfig.getNativeModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("native");
            }
            if(!keywordConfig.getDefaultModifier().equals(KeywordChoice.NO.toString())) {
                nonAccessMods.add("default");
            }
        }


        return nonAccessMods;
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
        List<String> nonAccessMods = getCommonPossibleNonAccessModifiers(classKeywordConfig);
        String type = getTypeFromClassConfig(classKeywordConfig);
        String name = getNameFromConfig(classKeywordConfig);

        return new ExpectedElement(possibleAccess, nonAccessMods, type, name);
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
        return new ExpectedElement(new ArrayList<>(), new ArrayList<>(), type, name);
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

    /**
     * Compares the expected non access modifiers with the modifiers from the present element
     * @param presentModifiers present modifiers to check
     * @param expectedNonAccessModifiers expected modifiers to compare to
     * @return true, if the expected non access modifiers match up with the actual non access modifiers
     */
    public static boolean isNonAccessMatch(List<Modifier> presentModifiers, List<String> expectedNonAccessModifiers) {
        for (Modifier modifier: presentModifiers) {
            String modifierName = modifier.getKeyword().asString().trim();
            if(possibleAccessModifiers.contains(modifierName)) {
                continue;
            }
            if(!expectedNonAccessModifiers.contains(modifierName)) {
                return false;
            }
        }
        return true;
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
