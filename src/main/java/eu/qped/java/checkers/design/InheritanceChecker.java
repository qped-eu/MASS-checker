package eu.qped.java.checkers.design;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;

import java.util.*;

public class InheritanceChecker {
    /**
     * Checks if the class declaration possesses the expected parents by matching them up exactly at first
     * and then finding the mistakes if a match cannot be found
     * @param classDecl ClassDeclaration to check
     * @param expectedParents the expected super classes that classDecl should have
     */
    public List<DesignFeedback> checkInheritanceMatch(ClassOrInterfaceDeclaration classDecl, List<String> expectedParents) {
        if(expectedParents.isEmpty()) {
            return new ArrayList<>();
        }
        List<DesignFeedback> inheritanceFeedback = new ArrayList<>();

        List<ClassOrInterfaceType> implementedInterfaces = classDecl.getImplementedTypes();
        List<ClassOrInterfaceType> extendedClasses = classDecl.getExtendedTypes();

        for (String parentInfo : expectedParents) {
            String[] expectedParentInfo = CheckerUtils.extractClassNameInfo(parentInfo);

            boolean matchFound = findExactInheritanceMatch(extendedClasses, implementedInterfaces, expectedParentInfo);

            if (!matchFound) {
                inheritanceFeedback = findInheritanceViolation(classDecl.getNameAsString(), extendedClasses, implementedInterfaces, expectedParentInfo);
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
                                              String[] expectedParentInfo) {
        String expectedParentType = expectedParentInfo[2];
        String expectedParentName = expectedParentInfo[3];

        switch(expectedParentType) {
            case CheckerUtils.INTERFACE_TYPE:
                String interfaceMatch = findInheritedNameMatch(implementedInterfaces, expectedParentName, true);
                return !interfaceMatch.isBlank();
            case CheckerUtils.CONCRETE_CLASS_TYPE:
                String classMatch = findInheritedNameMatch(extendedClasses, expectedParentName, true);
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
     * @param expectedParentInfo expected parent classes with all info
     */
    private List<DesignFeedback> findInheritanceViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses,
                                          List<ClassOrInterfaceType> implementedInterfaces,
                                          String[] expectedParentInfo) {
        List<DesignFeedback> inheritanceFeedback = new ArrayList<>();

        String expectedParentAccess = expectedParentInfo[0];
        String expectedParentNonAccess = expectedParentInfo[1];
        String expectedParentType = expectedParentInfo[2];
        String expectedParentName = expectedParentInfo[3];

        String implementedNameMatch = findInheritedNameMatch(implementedInterfaces, expectedParentName, false);
        String extendedNameMatch = findInheritedNameMatch(extendedClasses, expectedParentName, false);
        if(implementedNameMatch.isBlank() && extendedNameMatch.isBlank()) {
            switch (expectedParentType) {
                case CheckerUtils.INTERFACE_TYPE:
                    inheritanceFeedback.add(findInterfaceNameViolation(currentClassName, implementedInterfaces));
                    break;

                case CheckerUtils.CONCRETE_CLASS_TYPE:
                    inheritanceFeedback.add(findClassNameViolation(currentClassName, extendedClasses, expectedParentNonAccess));
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
    private DesignFeedback findClassNameViolation(String currentClassName, List<ClassOrInterfaceType> extendedClasses, String expectedNonAccess) {
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
