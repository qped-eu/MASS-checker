package eu.qped.java.checkers.design;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;

import java.util.*;

/**
 * Modifier Checker for fields and methods, checking for access and non access modifiers
 * @param <T> FieldDeclaration or MethodDeclaration from JavaParser
 */
public class ModifierChecker<T extends Node> {

    private final String CHECKER_TYPE;

    public ModifierChecker(String CHECKER_TYPE) {
        this.CHECKER_TYPE = CHECKER_TYPE;
    }

    /**
     * Go through each specified element, split all keywords into lists and check if:
     * - enough elements exist in the code
     * - find all exact matches
     * - find mistakes in the remaining ones
     * The splitting is done by extracting the relevant piece from the string, shortening the string by the amount removed
     * This is done for all keywords, such that the string is empty by the end
     * The order to split the string is important, as they depend on the previous operation.
     * @param expectedKeywords expected modifiers in a node
     */
    public List<DesignFeedback> checkModifiers(ClassOrInterfaceDeclaration classDecl, List<String> expectedKeywords) {
        //TODO: Create separate class that puts everything into one (ExpectedElementInfo)
        //TODO: Go through each keyword one by one?
        //TODO: Fix classDecl mess
        if(expectedKeywords.isEmpty()) {
            return new ArrayList<>();
        }

        List<DesignFeedback> modifierFeedback = new ArrayList<>();

        List<String> expectedKeywordsCopy = new ArrayList<>(expectedKeywords);

        List<String> expectedAccessModifiers = CheckerUtils.getAccessModifiersFromString(expectedKeywordsCopy);
        List<List<String>> expectedNonAccessModifiers = CheckerUtils.getNonAccessModifiersFromString(expectedKeywordsCopy);
        List<String> expectedElementTypes = CheckerUtils.getElementType(expectedKeywordsCopy);
        List<String> expectedElementNames = CheckerUtils.getExpectedElementName(expectedKeywordsCopy);

        List<NodeWithModifiers<T>> presentElements = getAllFieldsOrMethods(classDecl);

        //Error Checking
        DesignFeedback sizeFb = checkIfLessThanExpectedPresent(classDecl, presentElements, expectedKeywords);
        if(sizeFb != null) {
            modifierFeedback.add(sizeFb);
        }
        removeExactMatches(presentElements, expectedAccessModifiers, expectedNonAccessModifiers, expectedElementTypes, expectedElementNames);
        modifierFeedback.addAll(findViolation(classDecl, presentElements,
                expectedAccessModifiers, expectedNonAccessModifiers,
                expectedElementTypes, expectedElementNames));

        return modifierFeedback;
    }

    /**
     * checks if an element with modifiers matches an exact combination of access and non access modifier
     * removes the element if it has an exact match
     * @param elementsWithModifiers all elementsWithModifiers
     * @param expectedAccessModifiers all expected access modifiers
     * @param expectedNonAccessModifiers all expected non access modifiers
     */
    private void removeExactMatches(List<NodeWithModifiers<T>> elementsWithModifiers,
                                    List<String> expectedAccessModifiers,
                                    List<List<String>> expectedNonAccessModifiers,
                                    List<String> expectedFieldOrReturnTypes,
                                    List<String> expectedElementNames) {

        if(expectedAccessModifiers.isEmpty()) {
            return;
        }

        //First Pass: Find exact match
        Iterator<NodeWithModifiers<T>> elemIterator = elementsWithModifiers.iterator();
        while(elemIterator.hasNext()) {
            NodeWithModifiers<T> elem = elemIterator.next();

            //for each field, first check if we can find a access Modifier match
            //if we can do that, we check if the other required modifiers match
            //if both match, we can safely delete the field

            Iterator<String> accessIterator = expectedAccessModifiers.iterator();
            Iterator<List<String>> nonAccessListIterator = expectedNonAccessModifiers.iterator();
            Iterator<String> typeIterator = expectedFieldOrReturnTypes.iterator();
            Iterator<String> nameIterator = expectedElementNames.iterator();

            while(accessIterator.hasNext()) {
                String currentExpectedAccessModifier = accessIterator.next();
                List<String> currentExpectedNonAccessModifierList = nonAccessListIterator.next();
                String currentExpectedType = typeIterator.next();
                String currentExpectedName = nameIterator.next();

                boolean exactMatch = accessMatch(elem, currentExpectedAccessModifier) &&
                        nonAccessMatch(elem, currentExpectedNonAccessModifierList) &&
                        typeMatch(elem, currentExpectedType) &&
                        nameMatch(elem, currentExpectedName);

                if(exactMatch) {
                    elemIterator.remove();
                    accessIterator.remove();
                    nonAccessListIterator.remove();
                    typeIterator.remove();
                    nameIterator.remove();
                    break;
                }

            }
        }
    }

    /**
     * Finds the design violations in the given elements and adds them to the design feedback
     * Feedback for:
     * - Name Mismatch
     * - Type Mismatch
     * - Modifier Mismatch
     * - Element missing entirely
     * @param elements elements to check violations for
     * @param expectedAccessModifiers expected access modifiers to compare the elements to
     * @param expectedNonAccessModifierList expected non access modifiers to compare the elements to
     */
    private List<DesignFeedback> findViolation(ClassOrInterfaceDeclaration classDecl,
                                    List<NodeWithModifiers<T>> elements,
                                    List<String> expectedAccessModifiers,
                                    List<List<String>> expectedNonAccessModifierList,
                                    List<String> expectedElementTypes,
                                    List<String> expectedElementNames) {

        if(expectedAccessModifiers.isEmpty()) {
            return new ArrayList<>();
        }

        List<DesignFeedback> collectedFeedback = new ArrayList<>();

        Iterator<NodeWithModifiers<T>> elemIterator = elements.iterator();
        Iterator<String> accessIterator = expectedAccessModifiers.iterator();
        Iterator<List<String>> nonAccessIterator = expectedNonAccessModifierList.iterator();
        Iterator<String> typeIterator = expectedElementTypes.iterator();
        Iterator<String> nameIterator = expectedElementNames.iterator();

        while(elemIterator.hasNext()) {

            NodeWithModifiers<T> element = elemIterator.next();
            String elementName = getVariableName(element);

            if(!accessIterator.hasNext()) {
                return collectedFeedback;
            }

            String expectedAccessModifier = accessIterator.next();
            List<String> expectedNonAccessModifiers = nonAccessIterator.next();
            String expectedType = typeIterator.next();
            String expectedName = nameIterator.next();

            boolean accessMatch = accessMatch(element, expectedAccessModifier);
            boolean nonAccessMatch = nonAccessMatch(element, expectedNonAccessModifiers);
            boolean typeMatch = typeMatch(element, expectedType);
            boolean nameMatch = nameMatch(element, expectedName);

            String violationFound = DesignFeedbackGenerator.VIOLATION_CHECKS.get(
                    Arrays.asList(accessMatch, nonAccessMatch, typeMatch, nameMatch));

            if(CHECKER_TYPE.equals(CheckerUtils.METHOD_CHECKER)) {
                elementName += "()";
                if(violationFound.equals(DesignFeedbackGenerator.MISSING_FIELDS)) {
                    violationFound = DesignFeedbackGenerator.MISSING_METHODS;
                }
            }
            DesignFeedback fb = DesignFeedbackGenerator.generateFeedback(classDecl.getNameAsString(), elementName, violationFound);
            collectedFeedback.add(fb);
        }
        return collectedFeedback;
    }

    /**
     * Checks if more or equal elements are there compared to the expected amount
     * @param expectedKeywords expected keywords, gives the size of the expected elements
     */
    private DesignFeedback checkIfLessThanExpectedPresent(ClassOrInterfaceDeclaration classDecl,
                                                          List<NodeWithModifiers<T>> presentElements,
                                                          List<String> expectedKeywords) {

        if(expectedKeywords.size() > presentElements.size()) {
            String violation;
            if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
                violation = DesignFeedbackGenerator.MISSING_FIELDS;
            } else {
                violation = DesignFeedbackGenerator.MISSING_METHODS;
            }
            return DesignFeedbackGenerator.generateFeedback(classDecl.getNameAsString(), "", violation);
        }
        return null;
    }

    /**
     * Get all names for the element, for fields this can be more than one variable
     * @param element the element to get the name from
     * @return name of the element
     */
    private String getVariableName(NodeWithModifiers<T> element) {
        String elementName;

        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            elementName = ((FieldDeclaration) element).getVariable(0).getNameAsString();
        } else {
            elementName = ((MethodDeclaration) element).getNameAsString();
        }

        return elementName;
    }

    /**
     * Checks if the expected access modifier matches up with the actual element modifier
     * @param elem element to check
     * @param expectedAccessModifier expected access modifier
     * @return true, if actual and expected match up
     */
    private boolean accessMatch(NodeWithModifiers<T> elem, String expectedAccessModifier) {
        if(expectedAccessModifier.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }
        return elem.getAccessSpecifier().asString().equals(expectedAccessModifier);
    }

    /**
     * Compares the expected non access modifiers with the modifiers from the element
     * @param elem element to check
     * @param expectedNonAccessModifiers expected non access modifiers
     * @return true, if the expected non access modifiers match up with the actual non access modifiers
     */
    private boolean nonAccessMatch(NodeWithModifiers<T> elem, List<String> expectedNonAccessModifiers) {
        List<String> actualModifiers = new ArrayList<>();

        if(expectedNonAccessModifiers.get(0).equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }

        for (Modifier fdModifier: elem.getModifiers()) {
            String fdModifierStr = fdModifier.getKeyword().asString();

            if(fdModifierStr.equals(elem.getAccessSpecifier().asString())) {
                continue;
            }
            if(!expectedNonAccessModifiers.contains(fdModifierStr)) {
                return false;
            }
            actualModifiers.add(fdModifierStr);
        }

        for (String expectedModifier : expectedNonAccessModifiers) {
            if (!expectedModifier.isBlank()) {
                if(!actualModifiers.contains(expectedModifier)) {
                    return false;
                }
            }
        }

        //both list contain each other, so we have an exact match
        return true;
    }

    /**
     * Returns true if the expected field / return type matches up with the actual field / return type of the element
     * @param elem the element to check
     * @param expectedFieldOrReturnType expected field / return type
     * @return true if exact match
     */
    private boolean typeMatch(NodeWithModifiers<T> elem, String expectedFieldOrReturnType) {
        if(expectedFieldOrReturnType.isBlank()) {
            return false;
        }

        if(expectedFieldOrReturnType.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }

        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            FieldDeclaration fieldElement = (FieldDeclaration) elem;
            return fieldElement.getElementType().asString().equals(expectedFieldOrReturnType);
        } else {
            MethodDeclaration methodElement = (MethodDeclaration) elem;
            return methodElement.getType().asString().equals(expectedFieldOrReturnType);
        }
    }

    /**
     * True if either the expectedElementName is * (optional) or if the expected and actual names match up
     * @param elem element to check
     * @param expectedElementName expected element name, can either be * or the name
     * @return true if exact match
     */
    private boolean nameMatch(NodeWithModifiers<T> elem, String expectedElementName) {
        if(expectedElementName.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }
        String elementName = getVariableName(elem);
        return expectedElementName.equals(elementName);
    }


    /**
     * Gets all fields or methods, specified by CHECKER_TYPE
     * @return a list of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllFieldsOrMethods(ClassOrInterfaceDeclaration classDecl) {
        List<NodeWithModifiers<T>> elementsWithModifiers = new ArrayList<>();

        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            List<FieldDeclaration> foundFields = classDecl.findAll(FieldDeclaration.class);
            unrollVariableDeclarations(foundFields);
            foundFields.forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        } else {
            classDecl.findAll(MethodDeclaration.class).forEach(md -> elementsWithModifiers.add((NodeWithModifiers<T>) md));
        }

        return elementsWithModifiers;
    }

    /**
     * Fields can possess multiple variable declarations in one statement. The checker does not recognize these
     * as separate fields, such that we need to unroll them into separate fields first before we can check them
     * for correctness.
     * @param elements fields to unroll
     */
    private void unrollVariableDeclarations(List<FieldDeclaration> elements) {
        final int MAX_ALLOWED_VARIABLES = 1;
        ListIterator<FieldDeclaration> elemIterator = elements.listIterator();

        while(elemIterator.hasNext()) {
            FieldDeclaration fd = elemIterator.next();
            if(fd.getVariables().size() > MAX_ALLOWED_VARIABLES) {
                elemIterator.remove();
                for (VariableDeclarator variable: fd.getVariables()) {
                    FieldDeclaration field = new FieldDeclaration(fd.getModifiers(), variable);
                    elemIterator.add(field);
                }
            }
        }

    }
}
