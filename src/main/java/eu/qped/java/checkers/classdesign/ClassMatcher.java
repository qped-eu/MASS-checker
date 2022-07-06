package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import eu.qped.java.checkers.classdesign.enums.AccessModifier;
import eu.qped.java.checkers.classdesign.enums.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.enums.KeywordType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;

import static eu.qped.java.checkers.classdesign.enums.ClassFeedbackType.*;

/**
 * Matches the provided class declarations with the expected class infos
 * If it can find a match, they will be stored in a map
 * Otherwise we can generate feedback based on what is most likely the issue
 * @author Paul Engelmann
 */
class ClassMatcher {

    /**
     * Matches up class declarations with given class infos. This is done by comparing the given name from the classInfo
     * and seeing if we can find a corresponding class declaration. If we can, those are matched together and removed from
     * both lists.
     * This assumes that every class has a unique name!
     * @param classDecls compilation units to go through
     * @param classInfos expected class infos
     * @return Map with matches class declarations and class infos
     */
    public Map<ClassInfo, ClassOrInterfaceDeclaration> matchClassNames(List<ClassOrInterfaceDeclaration> classDecls,
                                                                       List<ClassInfo> classInfos) {
        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl = new HashMap<>();

        Iterator<ClassOrInterfaceDeclaration> declIterator = classDecls.iterator();
        while(declIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = declIterator.next();
            Iterator<ClassInfo> infoIterator = classInfos.iterator();

            while (infoIterator.hasNext()) {
                ClassInfo classInfo = infoIterator.next();
                ClassKeywordConfig classKeywordConfig = classInfo.getClassKeywordConfig();
                String className = classKeywordConfig.getName();

                if (className.isBlank()) {
                    continue;
                }

                boolean nameMatch = isClassNameMatch(classDecl, className);

                if(nameMatch) {
                    matchedInfoDecl.put(classInfo, classDecl);
                    declIterator.remove();
                    infoIterator.remove();
                    break;
                }
            }
        }

        //Since we only have one of each we can say that they belong to each other and match them up anyway,
        // even if the name is wrong
        if(classDecls.size() == 1 && classInfos.size() == 1) {
            ClassOrInterfaceDeclaration classDecl = classDecls.get(0);
            ClassInfo classInfo = classInfos.remove(0);
            matchedInfoDecl.put(classInfo, classDecl);

            String elementName = classInfo.getClassKeywordConfig().getName();

            if(isClassNameMatch(classDecl, elementName)) {
                classDecls.remove(0);
            }
        }
        return matchedInfoDecl;
    }

    /**
     * All remaining class declarations have mismatched names, so that we generated them here
     * @param classDecls class declarations to generate feedback for
     * @return list of all generated feedback for the class declarations
     */
    public List<ClassFeedback> generateClassNameFeedback(List<ClassOrInterfaceDeclaration> classDecls) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        Iterator<ClassOrInterfaceDeclaration> classDeclIterator = classDecls.iterator();

        while(classDeclIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = classDeclIterator.next();
            String className = classDecl.getNameAsString();
            String classType = classDecl.isInterface() ? "interface" : "class";
            String classTypeName = classType + " " + className;
            collectedFeedback.add(ClassFeedbackGenerator.generateFeedback(classTypeName, "",
                    WRONG_CLASS_NAME));
            classDeclIterator.remove();
        }

        return collectedFeedback;
    }

    /**
     * Generate feedback based on access, non access and type mismatches. If any mismatch for an element
     * is found, only the found one will be displayed to not overwhelm the student with error messages.
     * @param classDecl class declaration to check
     * @param elemInfo expected element info extracted from class info
     */
    public List<ClassFeedback> checkClassMatch(ClassOrInterfaceDeclaration classDecl, ExpectedElement elemInfo) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        boolean accessMatch = CheckerUtils.isAccessMatch(classDecl.getAccessSpecifier().asString(), elemInfo.getPossibleAccessModifiers());
        boolean nonAccessMatch = CheckerUtils.isNonAccessMatch(classDecl.getModifiers(), elemInfo.getNonAccessModifiers());
        boolean typeMatch = isClassTypeMatch(classDecl, elemInfo.getType());

        ClassFeedbackType violation = null;
        if(!typeMatch) {
            violation = WRONG_CLASS_TYPE;
        } else if(!nonAccessMatch) {
            violation = WRONG_CLASS_NON_ACCESS_MODIFIER;
        } else if(!accessMatch){

            violation = WRONG_CLASS_ACCESS_MODIFIER;
        }

        if(violation != null) {
            String className = classDecl.getNameAsString();
            String classType = classDecl.isInterface() ? "interface" : "class";
            String classTypeName = classType +" "+className;

            collectedFeedback.add(ClassFeedbackGenerator.generateFeedback(
                    classTypeName,
                    "",
                    violation));
        }

        return collectedFeedback;
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isClassTypeMatch(ClassOrInterfaceDeclaration classDecl, String classType) {
        boolean foundTypeMatch = false;

        if(classType.equals(ClassType.INTERFACE.toString())) {
            foundTypeMatch = classDecl.isInterface();
        } else if (classType.equals(ClassType.CLASS.toString())) {
            foundTypeMatch = !classDecl.isInterface();
        }

        return foundTypeMatch;
    }

    private boolean isClassNameMatch(ClassOrInterfaceDeclaration classDecl, String className) {
        return classDecl.getNameAsString().equals(className);
    }
}
