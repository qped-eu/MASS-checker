package eu.qped.java.checkers.design;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassChecker {

    private final CompilationUnit compilationUnit;
    private final DesignChecker designChecker;

    public ClassChecker(CompilationUnit compilationUnit, DesignChecker designChecker) {
        this.compilationUnit = compilationUnit;
        this.designChecker = designChecker;
    }

    /**
     * Check if the class given has the expected type and if it implements the expected super classes
     * @param expectedClassTypeName expected classType and className in the format ClassType:ClassName
     * @param expectedInheritsFrom  expected super classes in the format ClassType:ClassName, if it exists
     */
    public void checkClassDeclaration(String expectedClassTypeName, ArrayList<String> expectedInheritsFrom) {
        //TODO: Generic Types?

        if(expectedClassTypeName.isBlank()) {
            return;
        }

        List<ClassOrInterfaceDeclaration> classDeclarations = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);

        String[] splitExpectedClassTypeName = expectedClassTypeName.split(":");
        String expectedClassType = splitExpectedClassTypeName[0];
        String expectedClassName = splitExpectedClassTypeName[1];

        for (ClassOrInterfaceDeclaration classDecl: classDeclarations) {

            //Does the expected class type fit the actual class type?
            checkClassTypeMatch(expectedClassType, classDecl);
            //Does the expected class name fit the actual class name?
            checkClassNameMatch(expectedClassName, classDecl);
            //Does the expected super class match the actual super class?
            checkSuperClassMatch(expectedInheritsFrom, classDecl);

        }
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param expectedClassType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private void checkClassTypeMatch(String expectedClassType, ClassOrInterfaceDeclaration classDecl) {
        switch (expectedClassType) {
            case "Interface":
                if(!classDecl.isInterface()) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, classDecl.getNameAsString());
                }
                break;
            case "Class":
                if(classDecl.isInterface() || classDecl.isAbstract()) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, classDecl.getNameAsString());
                }
                break;
            case "AbstractClass":
                if(!classDecl.isAbstract()) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, classDecl.getNameAsString());
                }
                break;
            case "InnerClass":
                if(!classDecl.isInnerClass()) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, classDecl.getNameAsString());
                }
        }
    }


    /**
     * Checks if the expected class name matches up with the actual class name
     * @param expectedClassName expected class name
     * @param classDecl class declaration to check name from
     */
    private void checkClassNameMatch(String expectedClassName, ClassOrInterfaceDeclaration classDecl) {
        //Check if the class has the expected name
        if(!expectedClassName.equals(classDecl.getNameAsString())) {
            //TODO
        }
    }


    /**
     * Checks if the expected inherited class is also the actual inherited class with matching type
     * @param expectedInheritsFrom expected inherited class
     * @param classDecl class declaration to match with expectedInheritsFrom
     */
    private void checkSuperClassMatch(List<String> expectedInheritsFrom, ClassOrInterfaceDeclaration classDecl) {
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
                findViolation(extendedClasses, implementedInterfaces, expectedInheritedType, expectedInheritedName);
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
            case "Interface":
                return inheritedNameMatches(implementedInterfaces, expectedInheritedName);
            case "AbstractClass":
            case "Class":
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
     * @param expectedInheritedType expected Class Type of the inherited class
     * @param expectedInheritedName expected class name of the inherited class
     */
    private void findViolation(List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
            String expectedInheritedType, String expectedInheritedName) {

        //Since we know that there has to be a type mismatch here (otherwise they would have been removed above)
        //we only check the other implemented / extended classes to find a fault
        //If we can't find that, we can conclude that its simply missing from the declaration
        switch (expectedInheritedType) {
            case "Interface":
                if(findExpectedDiffClassType(extendedClasses, expectedInheritedName)) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, expectedInheritedName);
                } else {
                    designChecker.addFeedback(DesignViolation.MISSING_INTERFACE_IMPLEMENTATION, expectedInheritedName);
                }
                break;
            case "AbstractClass":
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, expectedInheritedName);
                } else {
                    designChecker.addFeedback(DesignViolation.MISSING_ABSTRACT_CLASS_IMPLEMENTATION, expectedInheritedName);
                }

                break;
            case "Class":
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, expectedInheritedName);
                } else {
                    designChecker.addFeedback(DesignViolation.MISSING_CLASS_IMPLEMENTATION, expectedInheritedName);
                }
                break;
        }
    }


}
