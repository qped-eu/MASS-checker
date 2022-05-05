package eu.qped.java.checkers.design;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

public class ClassChecker {

    private final DesignChecker designChecker;

    public ClassChecker(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos, DesignChecker designChecker) {
        this.designChecker = designChecker;
        matchCompUnitAndClassInfo(compilationUnits, classInfos);
    }



    /**
     * Check if the class given has the expected type and if it implements the expected super classes
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
                //Since we check for type and name match in the compUnitClassInfo match, we only have to check if
                //the parent classes match up here
                //TODO: clean up
                //Does the expected super class match the actual super class?
                checkSuperClassMatch(classInfo.getInheritsFrom(), classDecl);

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
                        //we have a name issue here
                        //TODO: Catch exception here
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
    private void generateFeedbackForCompUnitAndClassInfo(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
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
                //Iterate over all classes in one file
                //TODO: Inner classes
                for (ClassOrInterfaceDeclaration classDecl : classDeclarations) {
                    boolean typeMatch = isTypeMatch(classDecl, expectedClassType);

                    //Since type matches, we know the name is wrong
                    if(typeMatch) {
                        designChecker.addFeedback(DesignViolation.WRONG_CLASS_NAME, expectedClassName);
                    } else {
                        //Otherwise since the type doesn't match up, we give feedback based on wrong class type
                        designChecker.addFeedback(DesignViolation.WRONG_CLASS_TYPE, expectedClassName);
                    }
                }



            }
        }
    }

    /**
     * Tries to match up given compilation units with given class infos
     * if there isn't an exact match between elements of those two, we generate feedback based on the name and type
     * of the expected class
     * @param compilationUnits compilation units to go through
     * @param classInfos expected class infos
     */
    private void matchCompUnitAndClassInfo(List<CompilationUnit> compilationUnits, List<ClassInfo> classInfos) {
        matchExactCompUnitAndClassInfo(compilationUnits, classInfos);
        generateFeedbackForCompUnitAndClassInfo(compilationUnits, classInfos);
    }

    /**
     * Checks if the expected class type matches up with the actual class type
     * @param classType expected class type
     * @param classDecl class declaration to check the class type from
     */
    private boolean isTypeMatch(ClassOrInterfaceDeclaration classDecl, String classType) {
        boolean foundTypeMatch = false;
        switch (classType) {
            case "Interface":
                foundTypeMatch = classDecl.isInterface();
                break;
            case "AbstractClass":
                foundTypeMatch = classDecl.isAbstract();
                break;
            case "Class":
                foundTypeMatch = !classDecl.isInterface() && !classDecl.isAbstract();
                break;
        }
        return foundTypeMatch;
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

    //*
//     * Create a map linking each class with its parent
//
//
//    private void buildInheritanceMap() {
//        for (Map.Entry<CompilationUnit, ClassInfo> entry: designChecker.getCompUnitToClassInfoMap().entrySet()) {
//            CompilationUnit compUnit = entry.getKey();
//
//            List<ClassOrInterfaceDeclaration> classDecls = compUnit.findAll(ClassOrInterfaceDeclaration.class);
//
//            for (ClassOrInterfaceDeclaration classDecl : classDecls) {
//                String currentClassName = classDecl.getNameAsString();
//                fillParentList(currentClassName, classDecl.getImplementedTypes());
//                fillParentList(currentClassName, classDecl.getExtendedTypes());
//            }
//
//        }
//    }
//
//    private void fillParentList(String currentClassName, List<ClassOrInterfaceType> parentClassOrInterfaces) {
//        if(!inheritanceMap.containsKey(currentClassName)) {
//            inheritanceMap.put(currentClassName, new ArrayList<>());
//        }
//
//        for (ClassOrInterfaceType parent: parentClassOrInterfaces) {
//            inheritanceMap.get(currentClassName).add(parent);
//        }
//    }


}
