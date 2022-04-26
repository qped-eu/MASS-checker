package eu.qped.java.checkers.design;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPrivateModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ModifierChecker<T extends Node> {

    private final CompilationUnit compilationUnit;
    private final DesignChecker designChecker;
    private final List<String> possibleAccessModifiers;
    private final String fieldOrMethod;


    public ModifierChecker(CompilationUnit compilationUnit, DesignChecker designChecker, String fieldOrMethod) {
        this.compilationUnit = compilationUnit;
        this.designChecker = designChecker;
        this.fieldOrMethod = fieldOrMethod;
        possibleAccessModifiers = createPossibleAccessModifiersList();
    }

    /**
     * Checks if every field or method is as restrictive as possible
     */
    public void checkModifierMaxRestrictive() {
        List<NodeWithModifiers<T>> allElements = getAllFieldsOrMethods();
        List<NodeWithModifiers<T>> privateElements = getAllPrivateElements();

        if(!allElements.containsAll(privateElements) || !privateElements.containsAll(allElements)) {
            if(fieldOrMethod.equals("field")) {
                designChecker.addFeedback(DesignViolation.FIELDS_NOT_RESTRICTIVE_ENOUGH,"");
            } else {
                designChecker.addFeedback(DesignViolation.METHODS_NOT_RESTRICTIVE_ENOUGH, "");
            }
        }
    }

    /**
     * Go through each element with modifiers in the class and check their modifiers, if all the modifiers in an element match
     * up with all the expected modifiers, we have no errors
     * otherwise we have errors and generate feedback
     * the order of the keyword list does not matter, we only check if there exists one node with one combination
     * @param expectedKeywords expected modifiers in a node
     */
    public void checkModifiers(ArrayList<String> expectedKeywords) {
        //TODO: What about multiple variable names in one field?
        //BUG: what about using no modifier, even though we expect one there?
        if(expectedKeywords.isEmpty()) {
            return;
        }

        List<String> expectedAccessModifiers = getAccessModifiersFromString(expectedKeywords);
        List<List<String>> expectedNonAccessModifiers = getNonAccessModifiersFromString(expectedKeywords);

        List<NodeWithModifiers<T>> elementsWithModifiers = getAllFieldsOrMethods();

        //Check if all required fields are there
        checkIfLessThanExpectedPresent(expectedKeywords);

        //Remove all correct fields
        removeExactMatches(elementsWithModifiers, expectedAccessModifiers, expectedNonAccessModifiers);

        //Remaining elements cannot be matched up properly, so find the appropriate violations
        findViolation(elementsWithModifiers, expectedAccessModifiers, expectedNonAccessModifiers);
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
                                    List<List<String>> expectedNonAccessModifiers) {

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

            while(accessIterator.hasNext()) {
                String expectedAccessModifier = accessIterator.next();
                List<String> expectedNonAccessModifierList = nonAccessListIterator.next();

                boolean exactModifierMatch = accessMatch(elem, expectedAccessModifier) && nonAccessMatch(elem, expectedNonAccessModifierList);

                if(exactModifierMatch) {
                    elemIterator.remove();
                    accessIterator.remove();
                    nonAccessListIterator.remove();
                    break;
                }

            }
        }
    }

    /**
     * Finds the design violations in the given elements and adds them to the design feedback list in DesignChecker
     * For each element we generate one feedback
     * @param elements elements to check violations for
     * @param expectedAccessModifiers expected access modifiers to compare the elements to
     * @param expectedNonAccessModifiers expected non access modifiers to compare the elements to
     */
    private void findViolation(List<NodeWithModifiers<T>> elements,
                                    List<String> expectedAccessModifiers,
                                    List<List<String>> expectedNonAccessModifiers) {
        //Check if we can find a matching access modifier
        //If we can, then the non access modifier has to be wrong
        //If we can't, then the access modifier has to be wrong

        if(expectedAccessModifiers.isEmpty()) {
            return;
        }

        Iterator<NodeWithModifiers<T>> elemIterator = elements.iterator();

        while(elemIterator.hasNext()) {
            NodeWithModifiers<T> element = elemIterator.next();

            String elementName = getElementName(element);

            boolean oneMatchFound = false;

            Iterator<String> accessIterator = expectedAccessModifiers.iterator();

            int idx = 0;

            while(accessIterator.hasNext()) {
                String accessModifier = accessIterator.next();
                if(element.getAccessSpecifier().asString().equals(accessModifier)) {
                    //if we are here, there must have been an error the expectedNonAccessModifiers
                    designChecker.addFeedback(DesignViolation.WRONG_NON_ACCESS_MODIFIER, elementName);

                    expectedNonAccessModifiers.remove(idx);
                    accessIterator.remove();
                    oneMatchFound = true;
                    break;
                }

                idx++;
            }

            if(!oneMatchFound) {
                designChecker.addFeedback(DesignViolation.WRONG_ACCESS_MODIFIER, elementName);
            }

            elemIterator.remove();
        }
    }

    /**
     * Checks if more or equal elements are there compared to the expected amount
     * @param expectedKeywords expected keywords, gives the size of the expected elements
     */
    private void checkIfLessThanExpectedPresent(List<String> expectedKeywords) {
        List<NodeWithModifiers<T>> presentElements = getAllFieldsOrMethods();
        if(expectedKeywords.size() > presentElements.size()) {
            if(fieldOrMethod.equals("field")) {
                designChecker.addFeedback(DesignViolation.MISSING_FIELDS, "");
            } else {
                designChecker.addFeedback(DesignViolation.MISSING_METHODS, "");
            }
        }
    }

    /**
     * Get a list of all access modifiers from expected modifier string list
     * @param expectedModifiers expected modifiers from field or method
     * @return list of all access modifiers from expected modifiers
     */
    private List<String> getAccessModifiersFromString(List<String> expectedModifiers) {
        List<String> accessModifiers = new ArrayList<>();

        for (String expectedKeywords : expectedModifiers) {
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            String accessModifier = "";
            String expectedAccessModifier = splitExpected.get(0);
            if(possibleAccessModifiers.contains(expectedAccessModifier)) {
                accessModifier = expectedAccessModifier;
            }
            accessModifiers.add(accessModifier);
        }

        return accessModifiers;
    }

    /**
     * Split each string in expectedModifiers and extract all non access modifiers from each string
     * get all non access modifiers from the expected modifiers
     * @param expectedModifiers expected modifier list
     * @return all non access modifiers as a list
     */
    private List<List<String>> getNonAccessModifiersFromString(List<String> expectedModifiers) {
        List<List<String>> nonAccessModifiers = new ArrayList<>();


        for (String expectedFieldKeyword: expectedModifiers) {
            //not sure if needed
            expectedFieldKeyword = expectedFieldKeyword.trim();
            //what if expectedFieldKeyword empty?
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedFieldKeyword.split("\\s+")));
            String expectedAccessModifier = splitExpected.get(0);
            if(possibleAccessModifiers.contains(expectedAccessModifier)) {
                splitExpected.remove(0);
            }
            nonAccessModifiers.add(splitExpected);
        }

        return nonAccessModifiers;
    }

    /**
     * Returns the name of an element, depending on whether it is a field or a method
     * @param element the element to get the name from
     * @return name of the element
     */
    private String getElementName(NodeWithModifiers<T> element) {
        String elementName;

        if(fieldOrMethod.equals("field")) {
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

        for (Modifier fdModifier: elem.getModifiers()) {
            String fdModifierStr = fdModifier.getKeyword().asString();

            if(fdModifierStr.equals(elem.getAccessSpecifier().asString())) {
                continue;
            }

            actualModifiers.add(fdModifierStr);
        }

        //only true, if both sets are equal to each other
        return actualModifiers.containsAll(expectedNonAccessModifiers)
                && expectedNonAccessModifiers.containsAll(actualModifiers);
    }

    /**
     * Helper method to differentiate between access and non access modifiers
     * @return access modifier list
     */
    private List<String> createPossibleAccessModifiersList() {
        List<String> possibleAccess = new ArrayList<>();
        possibleAccess.add("public");
        possibleAccess.add("private");
        possibleAccess.add("protected");

        return possibleAccess;
    }

    /**
     * Gets all fields or methods, specified by fieldOrMethod
     * @return a list of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllFieldsOrMethods() {
        List<NodeWithModifiers<T>> elementsWithModifiers = new ArrayList<>();

        if(fieldOrMethod.equals("field")) {
            compilationUnit.findAll(FieldDeclaration.class).forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        } else {
            compilationUnit.findAll(MethodDeclaration.class).forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        }

        return elementsWithModifiers;
    }

    /**
     * gets a list of all private fields or methods, specified by fieldOrMethod
     * @return lits of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllPrivateElements() {
        List<NodeWithModifiers<T>> elems = new ArrayList<>();

        if(fieldOrMethod.equals("field")) {
            compilationUnit.findAll(FieldDeclaration.class).stream()
                    .filter(NodeWithPrivateModifier::isPrivate)
                    .forEach(fd -> elems.add((NodeWithModifiers<T>) fd));
        } else {
            compilationUnit.findAll(MethodDeclaration.class).stream()
                    .filter(NodeWithPrivateModifier::isPrivate)
                    .forEach(fd -> elems.add((NodeWithModifiers<T>) fd));
        }

        return elems;
    }
}
