package eu.qped.java.checkers.design;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

public class ClassChecker {

    private final DesignChecker designChecker;

    public ClassChecker(DesignChecker designChecker) {
        this.designChecker = designChecker;
    }

    /**
     * Go through all matched up classes and check if their super classes match up with the expected super classes
     */
    public void checkSuperClassDeclaration(ClassOrInterfaceDeclaration classDecl, ClassInfo classInfo) {
        //TODO: Generic Types?
        String expectedClassTypeName = classInfo.getClassTypeName();

        if(expectedClassTypeName.isBlank()) {
            return;
        }

        //Does the expected super class match the actual super class?
        String currentClassName = classDecl.getNameAsString();
        checkSuperClassMatch(currentClassName, classInfo.getInheritsFrom(), classDecl);
    }


    /**
     * Checks if the expected inherited class is also the actual inherited class with matching type
     * @param currentClassName name of the currently looked at class
     * @param expectedInheritsFrom expected inherited class
     * @param classDecl class declaration to match with expectedInheritsFrom
     */
    private void checkSuperClassMatch(String currentClassName, List<String> expectedInheritsFrom, ClassOrInterfaceDeclaration classDecl) {
        if(expectedInheritsFrom.isEmpty()) {
            return;
        }

        List<ClassOrInterfaceType> implementedInterfaces = classDecl.getImplementedTypes();
        List<ClassOrInterfaceType> extendedClasses = classDecl.getExtendedTypes();

        //Check if the class is inheriting from the expected class / interface
        for (String s : expectedInheritsFrom) {
            String[] expectedInheritsFromTypeName = s.split(":");
            String expectedInheritedType = expectedInheritsFromTypeName[0];
            String expectedInheritedName = expectedInheritsFromTypeName[1];

            boolean matchFound = checkMatchInterfaceOrClass(extendedClasses, implementedInterfaces, expectedInheritedType, expectedInheritedName);

            if (!matchFound) {
                findViolation(currentClassName, extendedClasses, implementedInterfaces, expectedInheritedType, expectedInheritedName);
            }
        }
    }

    /**
     * Differentiate between having to look for implementing types or extending types
     * @param extendedClasses actual extended classes from the class
     * @param implementedInterfaces actual implemented interfaces from the class
     * @param expectedInheritedType expected class type from the inherited class
     * @param expectedInheritedName expected class name from the inherited class
     * @return true if we can find a match with the expected and actual
     */
    private boolean checkMatchInterfaceOrClass(List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
                                               String expectedInheritedType, String expectedInheritedName) {
        switch(expectedInheritedType) {
            case DesignChecker.INTERFACE_TYPE:
                return inheritedNameMatches(implementedInterfaces, expectedInheritedName);
            case DesignChecker.ABSTRACT_CLASS_TYPE:
            case DesignChecker.DEFAULT_CLASS_TYPE:
                return inheritedNameMatches(extendedClasses, expectedInheritedName);
        }
        return false;
    }

    /**
     * This method is only called if there are expected type / names, that do not have an exact match
     * This finds a name match with a type mismatch
     * @param types either the implemented interfaces or the extended classes
     * @param expectedInheritedName expected name of the interface / class
     * @return true if the expected name can be found in the given types list
     */
    private boolean findExpectedDiffClassType(List<ClassOrInterfaceType> types, String expectedInheritedName) {
        for (ClassOrInterfaceType type: types) {
            return type.getNameAsString().equals(expectedInheritedName);
        }
        return false;
    }

    /**
     * Looks for a name match with the expected inherited name and all the given type names
     * @param types all possible classes being implemented
     * @param expectedInheritsFromName expected inheritsFrom name
     * @return true, if there exists a match between type and expected name
     */
    private boolean inheritedNameMatches(List<ClassOrInterfaceType> types, String expectedInheritsFromName) {
        Iterator<ClassOrInterfaceType> typesIterator = types.iterator();
        while(typesIterator.hasNext()) {
            ClassOrInterfaceType actualClassName = typesIterator.next();
            if(expectedInheritsFromName.equals(actualClassName.getNameAsString())) {
                //Found a match
                typesIterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Couldn't find a match for this expectedInheritedType, so we generate feedback based on what is missing
     * @param currentClassName name of the currently looked at class
     * @param expectedInheritedType expected Class Type of the inherited class
     * @param expectedInheritedName expected class name of the inherited class
     */
    private void findViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
                               String expectedInheritedType, String expectedInheritedName) {

        //Since we know that there has to be a type mismatch here (otherwise they would have been removed above)
        //we only check the other implemented / extended classes to find a fault
        //If we can't find that, we can conclude that its simply missing from the declaration
        switch (expectedInheritedType) {
            case DesignChecker.INTERFACE_TYPE:
                //TODO: Should be actual Inherited Name and not actual
                if(findExpectedDiffClassType(extendedClasses, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
                }
                break;
            case DesignChecker.ABSTRACT_CLASS_TYPE:
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
                }

                break;
            case DesignChecker.DEFAULT_CLASS_TYPE:
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);
                }
                break;
        }
    }
}
