package eu.qped.java.checkers.design;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;
import eu.qped.java.checkers.design.infos.ClassInfo;
import eu.qped.java.checkers.design.infos.ExpectedElement;

import java.util.*;
import java.util.stream.Collectors;

class InheritanceChecker {

    private Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl;

    public InheritanceChecker(Map<ClassInfo, ClassOrInterfaceDeclaration> matchedInfoDecl) {
        this.matchedInfoDecl = matchedInfoDecl;
    }

    public ClassOrInterfaceDeclaration getParentClassDecl(ExpectedElement expectedParent) {
        ClassOrInterfaceDeclaration parentClassDecl = null;
        for (Map.Entry<ClassInfo, ClassOrInterfaceDeclaration> entry: matchedInfoDecl.entrySet()) {
            ClassInfo maybeParent = entry.getKey();
            ExpectedElement parentElement = CheckerUtils.extractExpectedInfo(maybeParent.getClassTypeName());
            if(parentElement.getName().equals(expectedParent.getName())) {
                parentClassDecl = matchedInfoDecl.get(maybeParent);
                break;
            }
        }
        return parentClassDecl;
    }

    public List<DesignFeedback> checkInheritedFields(ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) {
        List<DesignFeedback> collectedFeedback = new ArrayList<>();

        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);
        if(parentDecl != null) {
            List<FieldDeclaration> currentFields = new ArrayList<>(currentClassDecl.findAll(FieldDeclaration.class));
            List<FieldDeclaration> parentFields = new ArrayList<>(parentDecl.findAll(FieldDeclaration.class));

            Iterator<FieldDeclaration> curIterator = currentFields.listIterator();
            Iterator<FieldDeclaration> parIterator = parentFields.listIterator();

            while(parIterator.hasNext()) {
                FieldDeclaration parentField = parIterator.next();
                //Do we count private fields?
                while(curIterator.hasNext()) {
                    FieldDeclaration currentField = curIterator.next();
                    List<String> parentFieldNames = parentField.getVariables().stream()
                            .map(NodeWithSimpleName::getNameAsString)
                            .collect(Collectors.toList());
                    List<String> currentFieldNames = currentField.getVariables().stream()
                            .map(NodeWithSimpleName::getNameAsString)
                            .collect(Collectors.toList());
                    List<String> sameNames = new ArrayList<>();


                    for (String parentFieldName: parentFieldNames) {
                        Iterator<String> curFieldIterator = currentFieldNames.iterator();
                        while(curFieldIterator.hasNext()) {
                            String currentFieldName = curFieldIterator.next();
                            if(parentFieldName.equals(currentFieldName)) {
                                sameNames.add(currentFieldName);
                                curFieldIterator.remove();
                            }
                        }
                    }

                    if(!sameNames.isEmpty()) {
                        for (String sameName: sameNames) {
                            DesignFeedback fb = DesignFeedbackGenerator.generateFeedback(
                                    currentClassDecl.getNameAsString(),
                                    sameName,
                                    DesignFeedbackGenerator.HIDDEN_FIELD);
                            collectedFeedback.add(fb);
                        }
                        parIterator.remove();
                        curIterator.remove();
                        break;
                    }
                }
            }
        }
        return collectedFeedback;
    }

    /**
     * Check if inherited methods are being reimplemented here and generate feedback if they are
     * @return List of feedback
     */
    public List<DesignFeedback> checkInheritedMethods(ClassOrInterfaceDeclaration currentClassDecl, ExpectedElement expectedParent) {
        List<DesignFeedback> collectedFeedback = new ArrayList<>();

        ClassOrInterfaceDeclaration parentDecl = getParentClassDecl(expectedParent);
        if(parentDecl != null) {

            /*
            - Get the expected methods of the parent!!
            - The class here should not be an interface or abstract class. -> concrete class only
            - Check if they are implemented in here, they are not supposed to be here when:
                    - default methods in parent interface
                    - methods with implementations in parent class
             */
            List<MethodDeclaration> currentMethods = new ArrayList<>(currentClassDecl.findAll(MethodDeclaration.class));
            List<MethodDeclaration> parentMethods = new ArrayList<>(parentDecl.findAll(MethodDeclaration.class));

            Iterator<MethodDeclaration> curIterator = currentMethods.listIterator();
            Iterator<MethodDeclaration> parIterator = parentMethods.listIterator();

            while(parIterator.hasNext()) {
                MethodDeclaration parentMethod = parIterator.next();
                if(parentMethod.getBody().isPresent()) {
                    while(curIterator.hasNext()) {
                        MethodDeclaration currentMethod = curIterator.next();
                        String parentMethodName = parentMethod.getNameAsString();
                        //So we don't remove overloading ones but only overwritten ones
                        boolean sameName = parentMethodName.equals(currentMethod.getNameAsString());
                        boolean sameParameterSize = parentMethod.getParameters().size() == currentMethod.getParameters().size();
                        if(sameName && sameParameterSize) {
                            //we have an issue as we can just use the parent method instead of this one here
                            DesignFeedback fb = DesignFeedbackGenerator.generateFeedback(
                                    currentClassDecl.getNameAsString(),
                                    parentMethodName+"()",
                                    DesignFeedbackGenerator.OVERWRITTEN_METHOD);
                            collectedFeedback.add(fb);
                            parIterator.remove();
                            curIterator.remove();
                            break;
                        }
                    }
                }
            }
        }

        return collectedFeedback;
    }

    /**
     * Checks if the class declaration possesses the expected parents by matching them up exactly at first
     * and then finding the mistakes if a match cannot be found
     * @param classDecl ClassDeclaration to check
     * @param expectedParents the expected super classes that classDecl should have
     */
    public List<DesignFeedback> checkInheritanceMatch(ClassOrInterfaceDeclaration classDecl, List<ExpectedElement> expectedParents) {
        if(expectedParents.isEmpty()) {
            return new ArrayList<>();
        }
        List<DesignFeedback> inheritanceFeedback = new ArrayList<>();

        List<ClassOrInterfaceType> implementedInterfaces = classDecl.getImplementedTypes();
        List<ClassOrInterfaceType> extendedClasses = classDecl.getExtendedTypes();

        for (ExpectedElement elemInfo : expectedParents) {
            boolean matchFound = findExactInheritanceMatch(extendedClasses, implementedInterfaces, elemInfo);

            if (!matchFound) {
                inheritanceFeedback.addAll(findInheritanceViolation(classDecl.getNameAsString(), extendedClasses, implementedInterfaces, elemInfo));
            } else {
                inheritanceFeedback.addAll(checkInheritedMethods(classDecl, elemInfo));
                inheritanceFeedback.addAll(checkInheritedFields(classDecl, elemInfo));
            }
        }

        return inheritanceFeedback;
    }

    /**
     * Differentiate between having to look for implementing types or extending types
     * @param extendedClasses actual extended classes from the class
     * @param implementedInterfaces actual implemented interfaces from the class
     * @return true if we can find a match with the expected and actual
     */
    private boolean findExactInheritanceMatch(List<ClassOrInterfaceType> extendedClasses, List<ClassOrInterfaceType> implementedInterfaces,
                                              ExpectedElement elemInfo) {

        switch(elemInfo.getType()) {
            case CheckerUtils.INTERFACE_TYPE:
                String interfaceMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), true);
                return !interfaceMatch.isBlank();
            case CheckerUtils.CLASS_TYPE:
                String classMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), true);
                return !classMatch.isBlank();
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
    private String findInheritedNameMatch(List<ClassOrInterfaceType> types, String expectedInheritedName, boolean removeMatch) {
        String matchedName = "";
        Iterator<ClassOrInterfaceType> typesIterator = types.iterator();
        while(typesIterator.hasNext()) {
            ClassOrInterfaceType actualClassName = typesIterator.next();
            String actualInheritedName = actualClassName.getNameAsString();
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
     * @param currentClassName name of the current class
     * @param extendedClasses extendedClasses of current class
     * @param implementedInterfaces implemented interfaces of the current class
     * @param elemInfo expected parent classes with all element info
     */
    private List<DesignFeedback> findInheritanceViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses,
                                          List<ClassOrInterfaceType> implementedInterfaces,
                                          ExpectedElement elemInfo) {
        List<DesignFeedback> inheritanceFeedback = new ArrayList<>();

        String implementedNameMatch = findInheritedNameMatch(implementedInterfaces, elemInfo.getName(), false);
        String extendedNameMatch = findInheritedNameMatch(extendedClasses, elemInfo.getName(), false);
        if(implementedNameMatch.isBlank() && extendedNameMatch.isBlank()) {
            switch (elemInfo.getType()) {
                case CheckerUtils.INTERFACE_TYPE:
                    inheritanceFeedback.add(findInterfaceNameViolation(currentClassName, implementedInterfaces));
                    break;

                case CheckerUtils.CLASS_TYPE:
                    inheritanceFeedback.add(findClassNameViolation(currentClassName, extendedClasses, elemInfo.getNonAccessModifiers()));
                    break;
            }
        } else {
            inheritanceFeedback.add(findTypeViolation(currentClassName, implementedNameMatch, extendedNameMatch));
        }
        return inheritanceFeedback;
    }

    /**
     * Find the corresponding interface violation with it having a different name or missing entirely
     * @param currentClassName name of current class
     * @param implementedInterfaces implemented interfaces in the current class
     */
    private DesignFeedback findInterfaceNameViolation(String currentClassName, List<ClassOrInterfaceType> implementedInterfaces) {
        String violation;
        if(implementedInterfaces.isEmpty()) {
            violation = DesignFeedbackGenerator.MISSING_INTERFACE_IMPLEMENTATION;
        } else {
            violation = DesignFeedbackGenerator.DIFFERENT_INTERFACE_NAMES_EXPECTED;
        }
        return DesignFeedbackGenerator.generateFeedback(currentClassName, "", violation);
    }

    /**
     * Find the corresponding class violation with it having a different name or missing entirely
     * @param currentClassName name of the current class
     * @param extendedClasses extended classes from the current class
     * @param expectedNonAccess non access modifiers to determine the missing class extension
     */
    private DesignFeedback findClassNameViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses, List<String> expectedNonAccess) {
        String violation = "";
        Map<String, String> modifierMap = new LinkedHashMap<>();
        modifierMap.put("abstract", DesignFeedbackGenerator.MISSING_ABSTRACT_CLASS_IMPLEMENTATION);
        modifierMap.put("final", DesignFeedbackGenerator.MISSING_FINAL_CLASS_IMPLEMENTATION);
        modifierMap.put("static", DesignFeedbackGenerator.MISSING_STATIC_CLASS_IMPLEMENTATION);
        //modifierMap.put(CheckerUtils.EMPTY_MODIFIER, DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION);

        if(extendedClasses.isEmpty()) {
            for (Map.Entry<String, String> entry: modifierMap.entrySet()) {
                if(expectedNonAccess.contains(entry.getKey())) {
                    violation = entry.getValue();
                    break;
                }
            }
            if(violation.isBlank()) {
                violation = DesignFeedbackGenerator.MISSING_CLASS_IMPLEMENTATION;
            }
        } else {
            violation = DesignFeedbackGenerator.DIFFERENT_CLASS_NAMES_EXPECTED;
        }
        return DesignFeedbackGenerator.generateFeedback(currentClassName, "", violation);
    }

    /**
     * Since the name of the violating element has been found in another list, we just have to determine which list it
     * was found in and generate feedback based on that
     * @param currentClassName current class name
     * @param implementedNameMatch string that either gives us the found match in implementingInterfaces or empty string
     * @param extendedNameMatch string that either gives us the found match in extendedClasses or empty string
     */
    private DesignFeedback findTypeViolation(String currentClassName, String implementedNameMatch, String extendedNameMatch) {
        String violatingElement = implementedNameMatch.isBlank() ? extendedNameMatch : implementedNameMatch;
        return DesignFeedbackGenerator.generateFeedback(currentClassName, violatingElement, DesignFeedbackGenerator.WRONG_INHERITED_CLASS_TYPE);
    }
}
