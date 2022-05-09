package eu.qped.java.checkers.design;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPrivateModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ModifierChecker<T extends Node> {

    private final ClassOrInterfaceDeclaration currentClass;
    private final DesignChecker designChecker;
    private final List<String> possibleAccessModifiers;
    private final List<String> possibleNonAccessModifiers;
    private final String fieldOrMethod;


    public ModifierChecker(ClassOrInterfaceDeclaration currentClass, DesignChecker designChecker, String fieldOrMethod) {
        this.currentClass = currentClass;
        this.designChecker = designChecker;
        this.fieldOrMethod = fieldOrMethod;
        possibleAccessModifiers = createPossibleAccessModifiersList();
        possibleNonAccessModifiers = createPossibleNonAccessModifiersList();
    }

    /**
     * Checks if every field or method is as restrictive as possible
     */
    public void checkModifierMaxRestrictive() {
        List<NodeWithModifiers<T>> allElements = getAllFieldsOrMethods();
        List<NodeWithModifiers<T>> privateElements = getAllPrivateElements();

        if(!allElements.containsAll(privateElements) || !privateElements.containsAll(allElements)) {
            if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
                designChecker.addFeedback(currentClass.getNameAsString(), "", DesignFeedbackGenerator.FIELDS_NOT_RESTRICTIVE_ENOUGH);
            } else {
                designChecker.addFeedback(currentClass.getNameAsString(), "", DesignFeedbackGenerator.METHODS_NOT_RESTRICTIVE_ENOUGH);
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

        List<String> expectedKeywordsCopy = new ArrayList<>(expectedKeywords);

        //Gets the access modifiers and removes it from the expectedKeywords String inside the list
        List<String> expectedAccessModifiers = getAccessModifiersFromString(expectedKeywordsCopy);

        //Gets the non access modifiers and removes it from the string inside of the list
        List<List<String>> expectedNonAccessModifiers = getNonAccessModifiersFromString(expectedKeywordsCopy);

        //Gets the return / field type and removes it from each element in the list
        List<String> expectedFieldOrReturnTypes = getFieldOrReturnType(expectedKeywordsCopy);

        //Gets the name of each field / method
        List<String> expectedElementNames = getExpectedElementName(expectedKeywordsCopy);

        //Get all fields / methods inside the source code
        List<NodeWithModifiers<T>> elementsWithModifiers = getAllFieldsOrMethods();

        //Check if amount of required fields are there
        checkIfLessThanExpectedPresent(expectedKeywords);

        //Remove all correct fields
        removeExactMatches(elementsWithModifiers, expectedAccessModifiers, expectedNonAccessModifiers, expectedFieldOrReturnTypes, expectedElementNames);

        //Remaining elements cannot be matched up properly, so find the appropriate violations
        findViolation(elementsWithModifiers, expectedAccessModifiers, expectedNonAccessModifiers, expectedFieldOrReturnTypes, expectedElementNames);
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
                        fieldOrReturnTypeMatch(elem, currentExpectedType) &&
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
     * Finds the design violations in the given elements and adds them to the design feedback list in DesignChecker
     * For each element we generate one feedback
     * @param elements elements to check violations for
     * @param expectedAccessModifiers expected access modifiers to compare the elements to
     * @param expectedNonAccessModifierList expected non access modifiers to compare the elements to
     */
    private void findViolation(List<NodeWithModifiers<T>> elements,
                                    List<String> expectedAccessModifiers,
                                    List<List<String>> expectedNonAccessModifierList,
                                    List<String> expectedFieldOrReturnTypes,
                                    List<String> expectedElementNames) {
        //Idea:
        //Look for all name mismatches, remove found ones
        //Look for all type mismatches, remove found ones
        //Look for all non access mismatches, remove found ones
        //Remaining ones have access mismatches

        if(expectedAccessModifiers.isEmpty()) {
            return;
        }

        Iterator<NodeWithModifiers<T>> elemIterator = elements.iterator();
        Iterator<String> accessIterator = expectedAccessModifiers.iterator();
        Iterator<List<String>> nonAccessIterator = expectedNonAccessModifierList.iterator();
        Iterator<String> typeIterator = expectedFieldOrReturnTypes.iterator();

        while(elemIterator.hasNext()) {

            NodeWithModifiers<T> element = elemIterator.next();
            String elementName = getActualElementName(element);
            if(fieldOrMethod.equals(DesignChecker.METHOD_CHECKER)) {
                elementName += "()";
            }

            //If we have more actual elements than expected elements, we do nothing
            if(!accessIterator.hasNext()) {
                return;
            }

            String expectedAccessModifier = accessIterator.next();
            List<String> expectedNonAccessModifiers = nonAccessIterator.next();
            String expectedType = typeIterator.next();

            boolean nameViolationFound = accessMatch(element, expectedAccessModifier) &&
                    nonAccessMatch(element, expectedNonAccessModifiers) &&
                    fieldOrReturnTypeMatch(element, expectedType);

            boolean typeViolationFound = accessMatch(element, expectedAccessModifier) &&
                    nonAccessMatch(element, expectedNonAccessModifiers);

            boolean nonAccessViolationFound = accessMatch(element, expectedAccessModifier);

            if(nameViolationFound) {
                designChecker.addFeedback(currentClass.getNameAsString(), elementName,
                        DesignFeedbackGenerator.WRONG_ELEMENT_NAME);
            } else {
                if(typeViolationFound) {
                    designChecker.addFeedback(currentClass.getNameAsString(), elementName,
                            DesignFeedbackGenerator.WRONG_ELEMENT_TYPE);
                } else {
                    if(nonAccessViolationFound) {
                        designChecker.addFeedback(currentClass.getNameAsString(), elementName,
                                DesignFeedbackGenerator.WRONG_NON_ACCESS_MODIFIER);
                    } else {
                        //has to have an access violation
                        designChecker.addFeedback(currentClass.getNameAsString(), elementName,
                                DesignFeedbackGenerator.WRONG_ACCESS_MODIFIER);
                    }
                }
            }
        }
    }

    /**
     * Checks if more or equal elements are there compared to the expected amount
     * @param expectedKeywords expected keywords, gives the size of the expected elements
     */
    private void checkIfLessThanExpectedPresent(List<String> expectedKeywords) {
        //TODO: Specify exactly which ones are missing or just keep it general?
        List<NodeWithModifiers<T>> presentElements = getAllFieldsOrMethods();
        if(expectedKeywords.size() > presentElements.size()) {
            if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
                designChecker.addFeedback(currentClass.getNameAsString(), "", DesignFeedbackGenerator.MISSING_FIELDS);
            } else {
                designChecker.addFeedback(currentClass.getNameAsString(), "", DesignFeedbackGenerator.MISSING_METHODS);
            }
        }
    }

    /**
     * Get a list of all access modifiers from expected modifier string list
     * If we have found an access modifier, we remove it from the expected modifiers and continue to iterate through the rest
     * @param expectedModifiers expected modifiers from field or method
     * @return list of all access modifiers from expected modifiers
     */
    private List<String> getAccessModifiersFromString(List<String> expectedModifiers) {
        List<String> accessModifiers = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();
            //Split the expectedKeywords into a string each
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            String accessModifier = "";
            //at most the first String is the access modifier
            String expectedAccessModifier = splitExpected.get(0);
            if(possibleAccessModifiers.contains(expectedAccessModifier)) {
                accessModifier = expectedAccessModifier;
            }
            accessModifiers.add(accessModifier);

            if(!accessModifier.isBlank()) {
                splitExpected.remove(0);
                String remainingModifiers = String.join(" ", splitExpected);
                expectedModifiers.set(i, remainingModifiers);
            }

        }

        return accessModifiers;
    }

    /**
     * Split each string in expectedModifiers and extract all non access modifiers from each string,
     * remove from original string if found such that we can continue to use it for the next methods
     * get all non access modifiers from the expected modifiers
     * @param expectedModifiers expected modifier list
     * @return all non access modifiers as a list
     */
    private List<List<String>> getNonAccessModifiersFromString(List<String> expectedModifiers) {
        List<List<String>> nonAccessModifiers = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();

            //what if expectedFieldKeyword empty?
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            List<String> foundNonAccessModifiers = new ArrayList<>();

            Iterator<String> expectedIterator = splitExpected.iterator();
            while(expectedIterator.hasNext()) {
                String expectedKeyword = expectedIterator.next();
                if(possibleNonAccessModifiers.contains(expectedKeyword)) {
                    foundNonAccessModifiers.add(expectedKeyword);
                    expectedIterator.remove();
                } else {
                    //Since we found a word, that does not exist in the possibleNonAccess List, we must've found all
                    //keywords and we can leave
                    break;
                }
            }
            nonAccessModifiers.add(foundNonAccessModifiers);
            if(!foundNonAccessModifiers.isEmpty()) {
                String remainingModifiers = String.join(" ", splitExpected);
                expectedModifiers.set(i, remainingModifiers);
            }
        }

        return nonAccessModifiers;
    }

    /**
     * Get the field / return type of an element and remove it from the expected modifiers list
     * @param expectedModifiers list to go through and check
     * @return a list of all expected return / field types
     */
    private List<String> getFieldOrReturnType(List<String> expectedModifiers) {
        List<String> fieldOrReturnTypes = new ArrayList<>();

        for (int i = 0; i < expectedModifiers.size(); i++) {
            String expectedKeywords = expectedModifiers.get(i).trim();
            //Split the expectedKeywords into a string each
            List<String> splitExpected = new ArrayList<>(Arrays.asList(expectedKeywords.split("\\s+")));
            String fieldOrReturnType = splitExpected.get(0);
            fieldOrReturnTypes.add(fieldOrReturnType);

            splitExpected.remove(0);
            String remainingKeywords = String.join(" ", splitExpected);
            expectedModifiers.set(i, remainingKeywords);
        }

        return fieldOrReturnTypes;
    }

    /**
     * Get the name of each expected element after performing all the other keyword extractions
     * @param expectedModifiers expected modifiers, here only containing the name now since the other methods
     *                          removed the previous keywords from all strings inside the list
     * @return list of all names of the elements
     */
    private List<String> getExpectedElementName(List<String> expectedModifiers) {
        List<String> expectedNames = new ArrayList<>();

        for (String expectedName: expectedModifiers) {
            expectedName = expectedName.trim();
            expectedNames.add(expectedName);
        }

        return expectedNames;
    }

    /**
     * Returns the name of an element, depending on whether it is a field or a method
     * @param element the element to get the name from
     * @return name of the element
     */
    private String getActualElementName(NodeWithModifiers<T> element) {
        String elementName;

        if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
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

            //If we see an access modifier, we just skip it
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
     * Returns true if the expected field / return type matches up with the actual field / return type of the element
     * @param elem the element to check
     * @param expectedFieldOrReturnType expected field / return type
     * @return true if exact match
     */
    private boolean fieldOrReturnTypeMatch(NodeWithModifiers<T> elem, String expectedFieldOrReturnType) {
        if(expectedFieldOrReturnType.isBlank()) {
            return false;
        }

        if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
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
        String optionalName = "*";
        if(expectedElementName.equals(optionalName)) {
            return true;
        }
        String actualName = getActualElementName(elem);
        return actualName.equals(expectedElementName);
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
     * Helper method to differentiate between access and non access modifiers
     * @return access modifier list
     */
    private List<String> createPossibleNonAccessModifiersList() {
        List<String> possibleAccess = new ArrayList<>();
        possibleAccess.add("default");
        possibleAccess.add("abstract");
        possibleAccess.add("static");
        possibleAccess.add("final");
        possibleAccess.add("synchronized");
        possibleAccess.add("transient");
        possibleAccess.add("volatile");
        possibleAccess.add("native");
        possibleAccess.add("strictfp");
        possibleAccess.add("transitive");

        return possibleAccess;
    }

    /**
     * Gets all fields or methods, specified by fieldOrMethod
     * @return a list of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllFieldsOrMethods() {
        List<NodeWithModifiers<T>> elementsWithModifiers = new ArrayList<>();

        if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
            currentClass.findAll(FieldDeclaration.class).forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        } else {
            currentClass.findAll(MethodDeclaration.class).forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        }

        return elementsWithModifiers;
    }

    /**
     * gets a list of all private fields or methods, specified by fieldOrMethod
     * @return lits of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllPrivateElements() {
        List<NodeWithModifiers<T>> elements = new ArrayList<>();

        if(fieldOrMethod.equals(DesignChecker.FIELD_CHECKER)) {
            currentClass.findAll(FieldDeclaration.class).stream()
                    .filter(NodeWithPrivateModifier::isPrivate)
                    .forEach(fd -> elements.add((NodeWithModifiers<T>) fd));
        } else {
            currentClass.findAll(MethodDeclaration.class).stream()
                    .filter(NodeWithPrivateModifier::isPrivate)
                    .forEach(fd -> elements.add((NodeWithModifiers<T>) fd));
        }

        return elements;
    }
}
