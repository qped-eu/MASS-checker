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

        return possibleAccessMods;
    }

    public static List<String> getCommonNonAccessModifiers(KeywordConfig keywordConfig) {
        List<String> nonAccessMods = new ArrayList<>();

        if(!keywordConfig.getAbstractModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("abstract");
        }
        if(!keywordConfig.getStaticModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("static");
        }
        if(!keywordConfig.getFinalModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("final");
        }
        return nonAccessMods;
    }

    public static String getNameFromConfig(KeywordConfig keywordConfig) {
        return keywordConfig.getName();
    }

    /**
     * Field Info Stuff
     * @param fieldKeywordConfig
     * @return
     */

    public static ExpectedElement extractExpectedFieldInfo(FieldKeywordConfig fieldKeywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(fieldKeywordConfig);
        List<String> nonAccessMods = getNonAccessModifiersFromFieldConfig(fieldKeywordConfig);
        String type = getTypeFromFieldConfig(fieldKeywordConfig);
        String name = getNameFromConfig(fieldKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name);
    }

    public static List<String> getNonAccessModifiersFromFieldConfig(FieldKeywordConfig keywordConfig) {
        List<String> nonAccessMods = getCommonNonAccessModifiers(keywordConfig);

        if(!keywordConfig.getTransientModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("transient");
        }
        if(!keywordConfig.getVolatileModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("volatile");
        }
        return nonAccessMods;
    }

    public static String getTypeFromFieldConfig(FieldKeywordConfig fieldKeywordConfig) {
        return fieldKeywordConfig.getFieldType();
    }

    /**
     * Method Info Stuff
     * @param methodKeywordConfig
     * @return
     */

    public static ExpectedElement extractExpectedMethodInfo(MethodKeywordConfig methodKeywordConfig) {
        List<String> accessMod = getPossibleAccessModifiers(methodKeywordConfig);
        List<String> nonAccessMods = getNonAccessModifiersFromMethodConfig(methodKeywordConfig);
        String type = getTypeFromMethodConfig(methodKeywordConfig);
        String name = getNameFromConfig(methodKeywordConfig);
        return new ExpectedElement(accessMod, nonAccessMods, type, name);
    }


    public static List<String> getNonAccessModifiersFromMethodConfig(MethodKeywordConfig keywordConfig) {
        List<String> nonAccessMods = new ArrayList<>();
        if(!keywordConfig.getSynchronizedModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("synchronized");
        }
        if(!keywordConfig.getNativeModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("native");
        }
        if(!keywordConfig.getDefaultModifier().equals(KeywordChoice.NO.toString())) {
            nonAccessMods.add("default");
        }

        return nonAccessMods;
    }

    public static String getTypeFromMethodConfig(MethodKeywordConfig keywordConfig) {
        return keywordConfig.getMethodType();
    }



    /**
     * Class Info Stuff
     * @param classKeywordConfig
     * @return
     */
    public static ExpectedElement extractExpectedClassInfo(ClassKeywordConfig classKeywordConfig) {
        List<String> possibleAccess = getPossibleAccessModifiers(classKeywordConfig);
        List<String> nonAccessMods = getCommonNonAccessModifiers(classKeywordConfig);
        String type = getTypeFromClassConfig(classKeywordConfig);
        String name = getNameFromConfig(classKeywordConfig);

        return new ExpectedElement(possibleAccess, nonAccessMods, type, name);
    }

    public static String getTypeFromClassConfig(ClassKeywordConfig classKeywordConfig) {
        return classKeywordConfig.getInterfaceType().equals(KeywordChoice.YES.toString()) ? ClassType.INTERFACE.toString() : ClassType.CLASS.toString();
    }

    /**
     * Inherits From Info Stuff
     * @param keywordConfig
     * @return
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
