package eu.qped.java.checkers.design;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

public class ClassChecker {

    private final DesignChecker designChecker;

    private final String DEFAULT_CLASS_TYPE = "class";
    private final String ABSTRACT_CLASS_TYPE = "abstract class";
    private final String INTERFACE_TYPE = "interface";

    public ClassChecker(DesignChecker designChecker) {
        this.designChecker = designChecker;
    }

    /**
     * Tries to match up given compilation units with given class infos
     * if there isn't an exact match between elements of those two, we generate feedback based on the name and type
     * of the expected class
     * @param compilationUnits compilation units to go through
     * @param classInfos expected class infos
     */
    public void matchCompUnitAndClassInfo(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
        matchExactCompUnitAndClassInfo(compilationUnits, classInfos);
        generateFeedbackForMismatch(compilationUnits, classInfos);
    }

    /**
     * Go through all matched up classes and check if their super classes match up with the expected super classes
     */
    public void checkClassDeclaration() {
        //TODO: Generic Types?

        for (Map.Entry<CompilationUnit, ClassInfo> entry: designChecker.getCompUnitToClassInfoMap().entrySet()) {
            CompilationUnit compUnit = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            List<ClassOrInterfaceDeclaration> classDeclarations = compUnit.findAll(ClassOrInterfaceDeclaration.class);

            String expectedClassTypeName = classInfo.getClassTypeName();

            if(expectedClassTypeName.isBlank()) {
                continue;
            }

            for (ClassOrInterfaceDeclaration classDecl: classDeclarations) {
                //TODO: clean up
                //Does the expected super class match the actual super class?
                String currentClassName = classDecl.getNameAsString();
                checkSuperClassMatch(currentClassName, classInfo.getInheritsFrom(), classDecl);

            }
        }
    }

    /**
     * Tries to match up comp units with class infos exactly
     * @param compilationUnits compilation units to go through
     * @param classInfos expected class infos
     */
    private void matchExactCompUnitAndClassInfo(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
        Iterator<CompilationUnit> compIterator = compilationUnits.iterator();

        //Find exact match between compilation unit and class infos here
        while(compIterator.hasNext()) {
            CompilationUnit compUnit = compIterator.next();
            List<ClassOrInterfaceDeclaration> classDeclarations = compUnit.findAll(ClassOrInterfaceDeclaration.class);

            boolean foundMatch = false;

            //TODO: Inner classes might cause an issue here.
            for (ClassOrInterfaceDeclaration classDecl : classDeclarations) {
                //Find class info that belongs to this classDecl
                Iterator<ClassInfo> infoIterator = classInfos.iterator();

                while (infoIterator.hasNext()) {
                    ClassInfo classInfo = infoIterator.next();
                    String classTypeName = classInfo.getClassTypeName();

                    if (classTypeName.isBlank() || !classTypeName.contains(":")) {
                        //Since classTypeName is blank or doesn't conform to standards
                        //So we can't assign it to any class declaration
                        //TODO: We might be able to match them if only one exists
                        continue;
                    }

                    String[] classTypeNameSplit = classTypeName.split(":");
                    String classType = classTypeNameSplit[0];
                    String className = classTypeNameSplit[1];

                    boolean foundTypeMatch = isTypeMatch(classDecl, classType);

                    if (foundTypeMatch) {
                        if (classDecl.getNameAsString().equals(className)) {
                            //we can match the classInfo with the compUnit here since both fit
                            designChecker.addCompUnitToMap(compUnit, classInfo);
                            compIterator.remove();
                            infoIterator.remove();
                            foundMatch = true;
                            break;
                        }
                    }
                }
                if (foundMatch) {
                    break;
                }
            }
        }
    }


    /**
     * Generates feedback based on missing or wrong class type / name. we define missing or wrong as not being equal
     * to the expected class infos given by class infos
     * @param compilationUnits compilation units of all the classes given to us
     * @param classInfos expected class infos
     */
    private void generateFeedbackForMismatch(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
        for (CompilationUnit compilationUnit : compilationUnits) {

            List<ClassOrInterfaceDeclaration> classDeclarations = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
            //Go through all compilation units, that have not been matched up yet
            //There has to be an issue here somewhere, otherwise they wouldn't have been here in the first place
            //So we try to identify the most likely match and generate feedback based on that

            for (ClassInfo classInfo : classInfos) {
                String[] classTypeName = classInfo.getClassTypeName().split(":");
                String expectedClassType = classTypeName[0];
                String expectedClassName = classTypeName[1];


                //Check type first: If the type is right, we know the name has to be wrong
                //TODO: Inner classes
                for (ClassOrInterfaceDeclaration classDecl : classDeclarations) {
                    boolean typeMatch = isTypeMatch(classDecl, expectedClassType);

                    //Since type matches, we know the name is wrong
                    if(typeMatch) {
                        designChecker.addFeedback(classDecl.getNameAsString(), "", DesignViolation.WRONG_CLASS_NAME);
                    } else {
                        //Otherwise since the type doesn't match up, we give feedback based on wrong class type
                        designChecker.addFeedback(classDecl.getNameAsString(), "", DesignViolation.WRONG_CLASS_TYPE);
                    }
                }



            }
        }
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isTypeMatch(ClassOrInterfaceDeclaration classDecl, String classType) {
        boolean foundTypeMatch = false;
        switch (classType) {
            case INTERFACE_TYPE:
                foundTypeMatch = classDecl.isInterface();
                break;
            case ABSTRACT_CLASS_TYPE:
                foundTypeMatch = classDecl.isAbstract();
                break;
            case DEFAULT_CLASS_TYPE:
                foundTypeMatch = !classDecl.isInterface() && !classDecl.isAbstract();
                break;
        }
        return foundTypeMatch;
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
            case INTERFACE_TYPE:
                return inheritedNameMatches(implementedInterfaces, expectedInheritedName);
            case ABSTRACT_CLASS_TYPE:
            case DEFAULT_CLASS_TYPE:
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
            case INTERFACE_TYPE:
                //TODO: Should be actual Inherited Name and not actual
                if(findExpectedDiffClassType(extendedClasses, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.MISSING_INTERFACE_IMPLEMENTATION);
                }
                break;
            case ABSTRACT_CLASS_TYPE:
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
                }

                break;
            case DEFAULT_CLASS_TYPE:
                if(findExpectedDiffClassType(implementedInterfaces, expectedInheritedName)) {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.WRONG_INHERITED_CLASS_TYPE);
                } else {
                    designChecker.addFeedback(currentClassName, expectedInheritedName, DesignViolation.MISSING_CLASS_IMPLEMENTATION);
                }
                break;
        }
    }
}
