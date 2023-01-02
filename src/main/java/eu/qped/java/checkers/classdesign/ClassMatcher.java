package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import eu.qped.java.checkers.classdesign.exceptions.ClassNameException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.config.ClassKeywordConfig;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;

/**
 * Matches the provided class declarations with the expected class infos
 * If it can find a match, they will be stored in a map
 * Otherwise we can generate feedback based on what is most likely the issue
 * @author Paul Engelmann
 */
class ClassMatcher {

    /**
     * Make sure that the amount of provided classes matches up with the amount of expected classes
     * @param classInfos expected class information
     * @param classDecls provided classes
     */
    public static ClassFeedback checkClassAmount(List<ClassInfo> classInfos, List<ClassOrInterfaceDeclaration> classDecls) {
        ClassFeedback fb = null;
        if(classInfos.size() > classDecls.size()) {
            fb = ClassFeedbackGenerator.generateFeedback("", "", MISSING_CLASSES, ""); //ClassFeedbackType.MISSING_CLASSES ist unnötig, man kann einfach MISSING_CLASSES schreiben, da die Klasse in import enthalten ist
        }
        return fb;
    }
    /**
     * Matches up class declarations with given class infos. This is done by comparing the given name from the classInfo
     * and seeing if we can find a corresponding class declaration. If we can, those are matched together and removed from
     * both lists.
     * This assumes that every class has a unique name!
     * @param classDecls compilation units to go through
     * @param classInfos expected class infos
     * @return Map with matches class declarations and class infos
     */
    public static Map<ClassInfo, ClassOrInterfaceDeclaration> matchClassNames(List<ClassOrInterfaceDeclaration> classDecls,
                                                                              List<ClassInfo> classInfos) throws ClassNameException {


        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl = matchWithName(classDecls, classInfos);
        //Since we only have one of each we can say that they belong to each other and match them up anyway,
        //even if the name is wrong
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
     * Try matching up the classes with provided fully qualified names
     * @param classDecls class declarations from the solutions given
     * @param classInfos provided expected class infos
     * @return matched declarations and class infos with provided fully qualified names
     */
    private static Map<ClassInfo, ClassOrInterfaceDeclaration> matchWithName(List<ClassOrInterfaceDeclaration> classDecls,
                                                                             List<ClassInfo> classInfos) throws ClassNameException {

        Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl = new HashMap<>();
        Iterator<ClassOrInterfaceDeclaration> declIterator = classDecls.iterator();
        boolean matchFound;
        String className="";
        boolean nameMatch;
        String fullyQualifiedName="";
        while(declIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = declIterator.next();
            Iterator<ClassInfo> infoIterator = classInfos.iterator();

            while (infoIterator.hasNext()) {
                matchFound = false;
                ClassInfo classInfo = infoIterator.next();

                ClassKeywordConfig classKeywordConfig = classInfo.getClassKeywordConfig();
                className = classKeywordConfig.getName();
                nameMatch = isClassNameMatch(classDecl, className);

                fullyQualifiedName = classInfo.getFullyQualifiedName();
                Optional<String> actualQualifiedName = classDecl.getFullyQualifiedName();
                if(actualQualifiedName.isPresent() && fullyQualifiedName.equals(actualQualifiedName.get())) {
                    matchFound = true;
                }
                if(nameMatch) {
                    matchFound = true;
                }

                if(matchFound) {
                    matchedInfoDecl.put(classInfo, classDecl);
                    declIterator.remove();
                    infoIterator.remove();
                    break;
                }
            }
        }
        return matchedInfoDecl;
    }
    /**
     * All remaining class declarations have mismatched names, so that we generated them here
     * @param classDecls class declarations to generate feedback for
     * @return list of all generated feedback for the class declarations
     */
    public static List<ClassFeedback> generateClassNameFeedback(List<ClassOrInterfaceDeclaration> classDecls) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        Iterator<ClassOrInterfaceDeclaration> classDeclIterator = classDecls.iterator();
        String className="";
        String classType="";
        String classTypeName ="";
        while(classDeclIterator.hasNext()) {
            ClassOrInterfaceDeclaration classDecl = classDeclIterator.next();
            className = classDecl.getNameAsString();
            classType = classDecl.isInterface() ? "interface" : "class";
            classTypeName = classType + " " + className;
            collectedFeedback.add(ClassFeedbackGenerator.generateFeedback(classTypeName, "",
                    WRONG_CLASS_NAME, ""));
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
    public List<ClassFeedback> checkClassMatch(ClassOrInterfaceDeclaration classDecl, ExpectedElement elemInfo, List<String> customFeedback) {
        boolean accessMatch = CheckerUtils.isAccessMatch(classDecl.getAccessSpecifier().asString(), elemInfo.getPossibleAccessModifiers());
        boolean nonAccessMatch = CheckerUtils.isNonAccessMatch(classDecl.getModifiers(),
                elemInfo.getPossibleNonAccessModifiers(),
                elemInfo.isExactMatch(),
                elemInfo.isContainsYes());
        boolean typeMatch = isClassTypeMatch(classDecl, elemInfo.getTypes());
        return findViolation(accessMatch, nonAccessMatch, typeMatch, classDecl, customFeedback);
    }

    /** check if the class have a Violation
     * @param accessMatch a boolean type describe the modifier like public
     * @param nonAccessMatch a boolean type describe the modifier like private and protected
     * @param typeMatch a boolean type describe the class type
     * @param classDecl an object which have two types either Class or Interface
     * @param customFeedback a list of feedback that describe a class or interface
     * @return a list of feedbacks with violation
     */
    private List<ClassFeedback> findViolation(boolean accessMatch, boolean nonAccessMatch, boolean typeMatch, ClassOrInterfaceDeclaration classDecl,
                                             List<String> customFeedback) {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

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
                    violation,
                    String.join("\n", customFeedback)));
        }
        return collectedFeedback;
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classTypes expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isClassTypeMatch(ClassOrInterfaceDeclaration classDecl, List<String> classTypes) {
        for (String classType: classTypes) {
            if((classType.equals(ClassType.INTERFACE.toString())) && (classDecl.isInterface())) {
                return true;
            }
            else if ((classType.equals(ClassType.CLASS.toString())) &&(!classDecl.isInterface())) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the provided class name equals the actual interface or class name
     * @param classDecl an object which have two types either Class or Interface
     * @param className a String which have the name of the class
     * @return true if the provided class name equals the actual interface or class name
     * @throws ClassNameException
     */
    private static boolean isClassNameMatch(ClassOrInterfaceDeclaration classDecl, String className) throws ClassNameException {
        if(className.isBlank()) {
            throw new ClassNameException();
        }
        return classDecl.getNameAsString().equals(className);
    }
}
