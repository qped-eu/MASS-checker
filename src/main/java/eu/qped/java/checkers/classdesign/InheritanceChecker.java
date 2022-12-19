package eu.qped.java.checkers.classdesign;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import eu.qped.java.checkers.classdesign.exceptions.NoModifierException;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.enums.ClassType;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.infos.ClassInfo;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;
import java.util.stream.Collectors;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;

/**
 * Checker that concerns itself with inherited classes
 * @author Paul Engelmann
 */
class InheritanceChecker {

    private final Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl;
    private final List<String> customFeedback;

    public InheritanceChecker(Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl, List<String> customFeedback) {
        this.matchedInfoDecl = matchedInfoDecl;
        this.customFeedback = customFeedback;
    }

    /**
     * Checks if the class declaration possesses the expected parents by matching them up exactly at first
     * and then finding the mistakes if a match cannot be found
     * @param classDecl ClassDeclaration to check
     * @param expectedParents the expected super classes that classDecl should have
     */
    public List<ClassFeedback> checkInheritanceMatch(ClassOrInterfaceDeclaration classDecl, List<ExpectedElement> expectedParents) {
        if(expectedParents.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClassFeedback> inheritanceFeedback = new ArrayList<>();

        List<ClassOrInterfaceType> implementedInterfaces = classDecl.getImplementedTypes();
        List<ClassOrInterfaceType> extendedClasses = classDecl.getExtendedTypes();
        boolean matchFound;
        String classType="";
        String className="";
        String classTypeName = "";
        for (ExpectedElement elemInfo : expectedParents) {
            matchFound = findExactInheritanceMatch(extendedClasses, implementedInterfaces, elemInfo);

            classType = classDecl.isInterface() ? "interface" : "class";
            className = classDecl.getNameAsString();
            classTypeName = classType +" "+className;

            if (!matchFound) {
                inheritanceFeedback.addAll(findInheritanceViolation(classTypeName, extendedClasses, implementedInterfaces, elemInfo));
            }
//            else {
//                inheritanceFeedback.addAll(checkInheritedMethods(classTypeName, classDecl, elemInfo));
//                inheritanceFeedback.addAll(checkInheritedFields(classTypeName, classDecl, elemInfo));
//            }
        }

        return inheritanceFeedback;
    }

    /**
     * Differentiate between having to look for implementing types or extending types
     * @param extendedClasses actual extended classes from the class
     * @param implementedInterfaces actual implemented interfaces from the class
     * @return true if we can find a match with the expected and actual
     */
    private boolean findExactInheritanceMatch(List<ClassOrInterfaceType> extendedClasses,
                                              List<ClassOrInterfaceType> implementedInterfaces,
                                              ExpectedElement elemInfo) {

        List<String> possibleTypes = elemInfo.getTypes();
        String interfaceMatch="";
        String classMatch="";
        for (String type : possibleTypes) {
            if(type.equals(ClassType.INTERFACE.toString())) {
                interfaceMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), true);
                if(!interfaceMatch.isBlank()) {
                    return true;
                }
            } else if(type.equals(ClassType.CLASS.toString())) {
                classMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), true);
                if(!classMatch.isBlank()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if the fields inside the current class declaration are hiding fields from the super classes.
     * If that is the case, generate feedback to suggest renaming the field variable.
     *
     * - This feature is not being used by the checker. Works only for expected classes from the config, does not
     * work for java classes!
     * @param currentClassDecl current class declaration to check the fields for
     * @param expectedParent parent info
     * @return list of feedback if overwritten fields have been found
     */
    private List<ClassFeedback> checkInheritedFields(String currentClassTypeName, ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) throws Exception {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();
        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);
        if(parentDecl == null) {
            return new ArrayList<>();
        }

        List<FieldDeclaration> currentFields = new ArrayList<>(currentClassDecl.findAll(FieldDeclaration.class));
        List<FieldDeclaration> parentFields = new ArrayList<>(parentDecl.findAll(FieldDeclaration.class));

        Iterator<FieldDeclaration> curIterator = currentFields.listIterator();
        Iterator<FieldDeclaration> parIterator = parentFields.listIterator();

        while(parIterator.hasNext()) {
            FieldDeclaration parentField = parIterator.next();
            List<String> parentFieldNames = getAllFieldNames(parentField);

            if (parentField.isPrivate()) {
                continue;
            }

            while(curIterator.hasNext()) {
                FieldDeclaration currentField = curIterator.next();
                List<String> currentFieldNames = getAllFieldNames(currentField);
                List<String> sameNames = new ArrayList<>(parentFieldNames);
                sameNames.retainAll(currentFieldNames);

                if(!sameNames.isEmpty()) {
//                    for (String sameName: sameNames) {
//                        ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(
//                                currentClassTypeName,
//                                sameName,
//                                HIDDEN_FIELD, String.join("\n", customFeedback));
//                        collectedFeedback.add(fb);
//                    }
                    parIterator.remove();
                    curIterator.remove();
                    break;
                }
            }
        }
        return collectedFeedback;
    }

    /**
     * Check if the methods inside of the current class declaration are overwriting or hiding methods from the super class.
     * Methods are being overwritten if the name, return type and parameters match exactly. If the method is also static
     * together with the super classes' method, then the method is hiding it instead.
     * If overwritten or hidden, generate feedback to suggest a possible name change to avoid confusion.
     *
     * - This feature is currently not being used in the checker, but works for all provided classes. Does not work
     * for java specific classes.
     * @param currentClassDecl current class declaration to check the methods of
     * @param expectedParent parent info
     * @return list of feedback, if methods are found to be overwritten or hidden
     */
    private List<ClassFeedback> checkInheritedMethods(String currentClassTypeName, ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) throws Exception {
        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);
        if(parentDecl == null) {
            return new ArrayList<>();
        }

        List<MethodDeclaration> currentMethods = new ArrayList<>(currentClassDecl.findAll(MethodDeclaration.class));
        List<MethodDeclaration> parentMethods = new ArrayList<>(parentDecl.findAll(MethodDeclaration.class));

        Iterator<MethodDeclaration> curIterator = currentMethods.listIterator();
        Iterator<MethodDeclaration> parIterator = parentMethods.listIterator();

        while(parIterator.hasNext()) {
            MethodDeclaration parentMethod = parIterator.next();
            String parentMethodName = parentMethod.getNameAsString();
            String parentMethodType = parentMethod.getType().asString();
            List<Parameter> parentParameters = parentMethod.getParameters();

            if(parentMethod.getBody().isEmpty()) {
                continue;
            }

            while(curIterator.hasNext()) {
                MethodDeclaration currentMethod = curIterator.next();
                List<Parameter> currentParameters = currentMethod.getParameters();

                boolean sameName = parentMethodName.equals(currentMethod.getNameAsString());
                boolean sameReturnType = parentMethodType.equals(currentMethod.getType().asString());
                boolean sameParameters = parentParameters.containsAll(currentParameters) &&
                        currentParameters.containsAll(parentParameters);

                if(sameName && sameReturnType && sameParameters) {
//                    ClassFeedbackType violation;
//                    if(currentMethod.isStatic() && parentMethod.isStatic()) {
//                        violation = HIDDEN_METHOD;
//                    } else {
//                        violation = OVERWRITTEN_METHOD;
//                    }
//                    ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(
//                            currentClassTypeName,
//                            parentMethodName+"()",
//                            violation, String.join("\n", customFeedback));
//                    collectedFeedback.add(fb);
                    parIterator.remove();
                    curIterator.remove();
                    break;
                }
            }
        }

        return collectedFeedback;
    }

    /**
     * Compares the expected inherited name of a class to the list of actual inherited classes
     * This gives us the name of either the matching super class or blank, if there is no match
     * @param types either the implemented interfaces or the extended classes
     * @param expectedInheritedName expected name of the interface / class
     * @return super class name, if found
     */
    private String findInheritedNameMatch(List<ClassOrInterfaceType> types, String expectedInheritedName, boolean removeMatch) {
        String matchedName = "";
        Iterator<ClassOrInterfaceType> typesIterator = types.iterator();
        ClassOrInterfaceType actualClassName = null;
        String actualInheritedName ="";
        while(typesIterator.hasNext()) {
            actualClassName = typesIterator.next();
            actualInheritedName = actualClassName.getNameAsString();
            if(actualInheritedName.equals(expectedInheritedName)) {
                matchedName = actualInheritedName;
                if(removeMatch) {
                    typesIterator.remove();
                }
                break;
            }
        }
        return matchedName;
    }


    /**
     * For a given class, determine if expected super classes:
     * - have a wrong type
     * - have a wrong name
     * - are missing entirely
     * This is being achieved by checking if the classes appear in either the extendedClasses or the implementedInterfaces
     * If its in neither, either the name is wrong or the class is missing entirely.
     * If the class exists in one of the lists, it must be in the wrong list and feedback is necessary.
     * @param classTypeName name of the current class
     * @param extendedClasses extendedClasses of current class
     * @param implementedInterfaces implemented interfaces of the current class
     * @param elemInfo expected parent classes with all element info
     */
    private List<ClassFeedback> findInheritanceViolation(String classTypeName, List<ClassOrInterfaceType> extendedClasses,
                                                         List<ClassOrInterfaceType> implementedInterfaces,
                                                         ExpectedElement elemInfo) {
        List<ClassFeedback> inheritanceFeedback = new ArrayList<>();

        String implementedNameMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), false);
        String extendedNameMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), false);
        if(implementedNameMatch.isBlank() && extendedNameMatch.isBlank()) {
            List<String> possibleTypes = elemInfo.getTypes();
            if(possibleTypes.size() > 1) {
                //means that both interface and class were possible and none were found
                ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(classTypeName, "",
                        MISSING_SUPER_CLASS,
                        String.join("\n", customFeedback));
                inheritanceFeedback.add(fb);
            } else {
                for (String type : possibleTypes) {
                    if(type.equals(ClassType.INTERFACE.toString())) {
                        inheritanceFeedback.add(findInterfaceNameViolation(classTypeName, implementedInterfaces));
                    } else if(type.equals(ClassType.CLASS.toString())) {
                        inheritanceFeedback.add(findClassNameViolation(classTypeName, extendedClasses));
                    }
                }
            }
        } else {
            inheritanceFeedback.add(findTypeViolation(classTypeName, implementedNameMatch, extendedNameMatch));
        }
        return inheritanceFeedback;
    }

    /**
     * Find the corresponding interface violation with it having a different name or missing entirely
     * @param currentClassName name of current class
     * @param implementedInterfaces implemented interfaces in the current class
     */
    private ClassFeedback findInterfaceNameViolation(String currentClassName, List<ClassOrInterfaceType> implementedInterfaces) {
        ClassFeedbackType violation;
        if(implementedInterfaces.isEmpty()) {
            violation = MISSING_INTERFACE_IMPLEMENTATION;
        } else {
            violation = DIFFERENT_INTERFACE_NAMES_EXPECTED;
        }
        return ClassFeedbackGenerator.generateFeedback(currentClassName, "", violation, String.join("\n", customFeedback));
    }

    /**
     * Find the corresponding class violation with it having a different name or missing entirely
     * @param currentClassName name of the current class
     * @param extendedClasses extended classes from the current class
     */
    private ClassFeedback findClassNameViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses) {
        ClassFeedbackType violation;

        if(extendedClasses.isEmpty()) {
            violation = MISSING_CLASS_EXTENSION;
        } else {
            violation = DIFFERENT_CLASS_NAMES_EXPECTED;
        }
        return ClassFeedbackGenerator.generateFeedback(currentClassName, "", violation, String.join("\n", customFeedback));
    }

    /**
     * Since the name of the violating element has been found in another list, we just have to determine which list it
     * was found in and generate feedback based on that
     * @param currentClassName current class name
     * @param implementedNameMatch string that either gives us the found match in implementingInterfaces or empty string
     * @param extendedNameMatch string that either gives us the found match in extendedClasses or empty string
     */
    private ClassFeedback findTypeViolation(String currentClassName, String implementedNameMatch, String extendedNameMatch) {
        String violatingElement = implementedNameMatch.isBlank() ? extendedNameMatch : implementedNameMatch;
        return ClassFeedbackGenerator.generateFeedback(currentClassName, violatingElement, WRONG_SUPER_CLASS_TYPE, String.join("\n", customFeedback));
    }

    /**
     * Get the class declaration of the parent given the name and info about the parent
     * @param expectedParent parent to find the class declaration out of
     * @return parent declaration if found, otherwise null
     */
    private ClassOrInterfaceDeclaration getParentClassDecl(ExpectedElement expectedParent) throws NoModifierException {
        ClassOrInterfaceDeclaration parentClassDecl = null;
        for (Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry: matchedInfoDecl.entrySet()) {
            ClassInfo parentInfo = entry.getKey();
            ExpectedElement parentElement = CheckerUtils.extractExpectedInfo(parentInfo.getClassKeywordConfig());
            if(parentElement.getName().equals(expectedParent.getName())) {
                parentClassDecl = matchedInfoDecl.get(parentInfo);
                break;
            }
        }
        return parentClassDecl;
    }

    /**
     * Get all variable names of a field declaration
     * @param field declaration to check
     * @return all variable names from that field
     */
    private List<String> getAllFieldNames(FieldDeclaration field) {
        return field.getVariables().stream()
                .map(NodeWithSimpleName::getNameAsString)
                .collect(Collectors.toList());
    }
}
