package eu.qped.java.checkers.design;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;
import eu.qped.java.checkers.design.infos.ClassInfo;

import java.util.*;

/**
 * Matches the provided compilation units with the expected class infos
 * If it can find a match, they will be stored in a map
 * Otherwise we can generate feedback based on what is most likely the issue
 */
public class ClassMatcher {

    /**
     * Matches up class declarations with given class infos. This is done by comparing the given name from the classInfo
     * and seeing if we can find a corresponding class declaration. If we can, those are matched together and removed from
     * both lists.
     * This assumes that every class has a unique name!
     * @param classDecls compilation units to go through
     * @param classInfos expected class infos
     * @return Map with matches class declarations and class infos
     */
    public Map<ClassOrInterfaceDeclaration, ClassInfo> matchClassNames(List<ClassOrInterfaceDeclaration> classDecls,
                                                                       List<ClassInfo> classInfos) {
        Map<ClassOrInterfaceDeclaration, ClassInfo> matchedDeclInfo = new HashMap<>();

        Iterator<ClassOrInterfaceDeclaration> declIterator = classDecls.iterator();
        while(declIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = declIterator.next();
            Iterator<ClassInfo> infoIterator = classInfos.iterator();

            while (infoIterator.hasNext()) {
                ClassInfo classInfo = infoIterator.next();
                String classTypeName = classInfo.getClassTypeName();

                if (classTypeName.isBlank()) {
                    continue;
                }

                String[] typeNameSplit = CheckerUtils.extractClassNameInfo(classTypeName);
                String name = typeNameSplit[3];

                boolean nameMatch = isNameMatch(classDecl, name);

                if(nameMatch) {
                    matchedDeclInfo.put(classDecl, classInfo);
                    declIterator.remove();
                    infoIterator.remove();
                    break;
                }
            }
        }
        return matchedDeclInfo;
    }

    public List<DesignFeedback> generateClassNameFeedback(List<ClassOrInterfaceDeclaration> classDecls) {
        List<DesignFeedback> collectedFeedback = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDecl: classDecls) {
            collectedFeedback.add(DesignFeedbackGenerator.generateFeedback(classDecl.getNameAsString(), "",
                    DesignFeedbackGenerator.WRONG_CLASS_NAME));
        }

        return collectedFeedback;
    }

    /**
     * Generate feedback based on access, non access and type mismatches. If any mismatch for an element
     * is found, only the found one will be displayed to not overwhelm the student with error messages.
     * @param classDecl class declaration to check
     * @param classTypeName class type and class name to check the declaration against
     */
    public List<DesignFeedback> checkClassMatch(ClassOrInterfaceDeclaration classDecl, String classTypeName) {
        List<DesignFeedback> collectedFeedback = new ArrayList<>();
        String[] classInfoSplit = CheckerUtils.extractClassNameInfo(classTypeName);
        String accessMod = classInfoSplit[0];
        String nonAccessMods = classInfoSplit[1];
        String expectedClassType = classInfoSplit[2];

        boolean accessMatch = isAccessMatch(classDecl, accessMod);
        boolean nonAccessMatch = isNonAccessMatch(classDecl, nonAccessMods);
        boolean typeMatch = isTypeMatch(classDecl, nonAccessMods, expectedClassType);

        String violation = "";
        if(!typeMatch) {
            violation = DesignFeedbackGenerator.WRONG_CLASS_TYPE;
        } else if(!nonAccessMatch) {
            violation = DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER;
        } else if(!accessMatch){
            violation = DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER;
        }

        if(!violation.isBlank()) {
            collectedFeedback.add(DesignFeedbackGenerator.generateFeedback(
                    classDecl.getNameAsString(),
                    classDecl.getNameAsString(),
                    violation));
        }

        return collectedFeedback;
    }

    private boolean isAccessMatch(ClassOrInterfaceDeclaration classDecl, String accessMod) {
        if(accessMod.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }
        return classDecl.getAccessSpecifier().asString().equals(accessMod);
    }

    private boolean isNonAccessMatch(ClassOrInterfaceDeclaration classDecl, String nonAccessMod) {
        List<String> classModifiers = new ArrayList<>();

        if(nonAccessMod.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }

        for (Modifier modifier: classDecl.getModifiers()) {
            String modifierName = modifier.getKeyword().asString();
            if(modifierName.equals(classDecl.getAccessSpecifier().asString())) {
                continue;
            }
            if(!nonAccessMod.contains(modifierName)) {
                return false;
            }
            classModifiers.add(modifierName);
        }

        String[] modSplit = nonAccessMod.split("\\s+");
        for (String expectedMod: modSplit) {
            if(!expectedMod.isBlank() && !classModifiers.contains(expectedMod)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isTypeMatch(ClassOrInterfaceDeclaration classDecl, String nonAccessMods, String classType) {
        boolean foundTypeMatch = false;
        Map<String, Boolean> nonAccessMatchMap = new LinkedHashMap<>();
        nonAccessMatchMap.put("abstract", classDecl.isAbstract());
        nonAccessMatchMap.put("final", classDecl.isFinal());
        nonAccessMatchMap.put("static", classDecl.isStatic());

        switch (classType) {
            case CheckerUtils.INTERFACE_TYPE:
                foundTypeMatch = classDecl.isInterface();
                break;
            case CheckerUtils.CONCRETE_CLASS_TYPE:
                foundTypeMatch = !classDecl.isInterface();
                for (Map.Entry<String, Boolean> entry: nonAccessMatchMap.entrySet()) {
                    if(nonAccessMods.contains(entry.getKey())) {
                        foundTypeMatch = foundTypeMatch && entry.getValue();
                        break;
                    }
                }
                break;
        }
        return foundTypeMatch;
    }

    private boolean isNameMatch(ClassOrInterfaceDeclaration classDecl, String className) {
        return classDecl.getNameAsString().equals(className);
    }
}
