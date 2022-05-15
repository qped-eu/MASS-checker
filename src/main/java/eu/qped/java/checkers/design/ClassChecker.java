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

            boolean matchFound = checkExactMatchInterfaceOrClass(extendedClasses, implementedInterfaces, expectedInheritedType, expectedInheritedName);

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
    private boolean checkExactMatchInterfaceOrClass(List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
                                                    String expectedInheritedType, String expectedInheritedName) {
        switch(expectedInheritedType) {
            case DesignChecker.INTERFACE_TYPE:
                return doesNameMatchExist(implementedInterfaces, expectedInheritedName);
            case DesignChecker.ABSTRACT_CLASS_TYPE:
            case DesignChecker.DEFAULT_CLASS_TYPE:
                return doesNameMatchExist(extendedClasses, expectedInheritedName);
        }
        return false;
    }

    /**
     * Compares the expected inherited name of a class to the list of actual inherited classes
     * This gives us the name of either the matching super class or blank, if there is no match
     * @param types either the implemented interfaces or the extended classes
     * @param expectedInheritedName expected name of the interface / class
     * @return super class name, if found
     */
    private String findInheritedNameMatch(List<ClassOrInterfaceType> types, String expectedInheritedName) {
        String matchedName = "";
        for (ClassOrInterfaceType type: types) {
            String actualInheritedName = type.getNameAsString();
            if(actualInheritedName.equals(expectedInheritedName)) {
                matchedName = actualInheritedName;
                break;
            }
        }
        return matchedName;
    }

    /**
     * Looks for a name match with the expected inherited name and all the given type names
     * @param types all possible classes being implemented
     * @param expectedInheritsFromName expected inheritsFrom name
     * @return true, if there exists a match between type and expected name
     */
    private boolean doesNameMatchExist(List<ClassOrInterfaceType> types, String expectedInheritsFromName) {
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

        List<ClassOrInterfaceType> superClassList = new ArrayList<>();
        String missingClassImplementationViolation = "";
        switch (expectedInheritedType) {
            case DesignChecker.INTERFACE_TYPE:
                superClassList = implementedInterfaces;
                missingClassImplementationViolation = DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION;
                break;

            case DesignChecker.ABSTRACT_CLASS_TYPE:
                superClassList = extendedClasses;
                missingClassImplementationViolation = DesignFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION;
                break;

            case DesignChecker.DEFAULT_CLASS_TYPE:
                superClassList = extendedClasses;
                missingClassImplementationViolation = DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION;
                break;
        }

        //For a violation to occur we have 3 cases:
        //1. We have a name error: We have the proper class implemented / extended but the name mismatches
        //2. We have a type error: We can find the name in one of the lists but the list does not match the expected type
        //3. Name and type error: The entire implementation is missing.

        //Try to find a match in every list
        String implementedNameMatch = findInheritedNameMatch(implementedInterfaces, expectedInheritedName);
        String extendedNameMatch = findInheritedNameMatch(extendedClasses, expectedInheritedName);
        if(implementedNameMatch.isBlank() && extendedNameMatch.isBlank()) {
            //We cannot find the name in either list, so either the name is wrong or the implementation is missing
            switch (expectedInheritedType) {
                case DesignChecker.INTERFACE_TYPE:
                    if(implementedInterfaces.isEmpty()) {
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION);
                    } else {
                        //Get actual class name here
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.WRONG_INHERITED_CLASS_NAME);
                    }
                    break;

                case DesignChecker.ABSTRACT_CLASS_TYPE:
                    if(implementedInterfaces.isEmpty()) {
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
                    } else {
                        //Get actual class name here
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.WRONG_INHERITED_CLASS_NAME);
                    }
                    break;

                case DesignChecker.DEFAULT_CLASS_TYPE:
                    if(implementedInterfaces.isEmpty()) {
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);
                    } else {
                        //TODO: Change feedback so that we can just assume that one of them is wrong, not specify which one?
                        //Get actual class name here (closest?)
                        designChecker.addFeedback(currentClassName, "", DesignFeedbackGenerator.WRONG_INHERITED_CLASS_NAME);
                    }
                    break;
            }
        } else {
            if(implementedNameMatch.isBlank()) {
                designChecker.addFeedback(currentClassName, extendedNameMatch, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
            } else {
                designChecker.addFeedback(currentClassName, implementedNameMatch, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
            }
        }


        //If we cannot find a match, we can assume that the name is wrong, so we give a name error
        //Otherwise if we can find a match, we either have a type error or a completely missing implementation
//        String actualInheritedClassName = findInheritedNameMatch(superClassList, expectedInheritedName);
//        if(!actualInheritedClassName.isBlank()) {
//            designChecker.addFeedback(currentClassName, actualInheritedClassName, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
//        } else {
//            //Now either the inherited class type is wrong or the class is missing entirely
//            designChecker.addFeedback(currentClassName, actualInheritedClassName, missingClassImplementationViolation);
//        }
    }
}
