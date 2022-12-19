package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import eu.qped.java.checkers.classdesign.enums.*;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;
import eu.qped.java.checkers.classdesign.infos.ExpectedElement;

import java.util.*;
import java.util.stream.Collectors;

import static eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType.*;

/**
 * Keyword Checker for fields and methods, checking for access, non access modifiers, types and names
 * @param <T> FieldDeclaration or MethodDeclaration from JavaParser
 * @author Paul Engelmann
 */
class ClassMemberChecker<T extends Node> {

    private final ClassMemberType CHECKER_TYPE;
    private final List<String> customFeedback;

    public ClassMemberChecker(ClassMemberType CHECKER_TYPE, List<String> customFeedback) {
        this.CHECKER_TYPE = CHECKER_TYPE;
        this.customFeedback = customFeedback;
    }

    /**
     * Go through each specified element, split all keywords into lists and check if:
     * - enough elements exist in the code
     * - find all exact matches
     * - find mistakes in the remaining ones
     * The splitting is done by extracting the relevant piece from the string, shortening the string by the amount removed
     * This is done for all keywords, such that the string is empty by the end
     * The order to split the string is important, as they depend on the previous operation.
     //* @param expectedElements expected modifiers in a node
     */
    public List<ClassFeedback> checkModifiers(ClassOrInterfaceDeclaration classDecl, List<ExpectedElement> expectedElements, boolean matchExactAmount) {
        if(expectedElements.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClassFeedback> modifierFeedback = new ArrayList<>();
        String className = classDecl.getNameAsString();
        String classType = classDecl.isInterface() ?  "interface" : "class";
        String classTypeName = classType +" " +className;
        List<NodeWithModifiers<T>> presentElements = getAllFieldsOrMethods(classDecl);

        ClassFeedback sizeFb = checkIfLessThanExpectedPresent(classTypeName, presentElements, expectedElements, matchExactAmount);
        if(sizeFb != null) {
            modifierFeedback.add(sizeFb);
        }
        removeExactMatches(presentElements, expectedElements);
        modifierFeedback.addAll(findViolation(classTypeName, presentElements, expectedElements));
        return modifierFeedback;
    }

    /**
     * checks if an element with modifiers matches an exact combination of access and non access modifier
     * removes the element if it has an exact match
     * @param presentElements all elements that are present in the current class
     //* @param expectedElements all expected elements in the form of ExpectedElementInfo
     */
    private void removeExactMatches(List<NodeWithModifiers<T>> presentElements, List<ExpectedElement> expectedElements) {
        if(expectedElements.isEmpty()) {
            return;
        }

        Iterator<NodeWithModifiers<T>> elemIterator = presentElements.iterator();
        while(elemIterator.hasNext()) {
            NodeWithModifiers<T> presentElement = elemIterator.next();

            Iterator<ExpectedElement> expectedElemIterator = expectedElements.iterator();
            while(expectedElemIterator.hasNext()) {
                ExpectedElement expectedElement = expectedElemIterator.next();
                List<Boolean> matchingResult = getMatchingResult(presentElement, expectedElement);
                if(!matchingResult.contains(false)) {
                    elemIterator.remove();
                    expectedElemIterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * Finds the design violations in the given presentElements and adds them to the design feedback
     * Feedback for:
     * - Name Mismatch
     * - Type Mismatch
     * - Modifier Mismatch
     * - Element missing entirely
     * @param presentElements elements present in the class
     * @param expectedElements expected elements from class info
     */
    private List<ClassFeedback> findViolation(String classTypeName, List<NodeWithModifiers<T>> presentElements, List<ExpectedElement> expectedElements) {
        if(expectedElements.isEmpty()) {
            return new ArrayList<>();
        }

        List<ClassFeedback> collectedFeedback = new ArrayList<>();

        Map<NodeWithModifiers<T>, ExpectedElement> matches = performMatching(presentElements, expectedElements);

        // Objekte vor for-schleife deklarieren
        NodeWithModifiers<T> presentElement;
        ExpectedElement expectedElement;
        List<Boolean> matchingResult;
        ClassFeedbackType violationFound;
        StringBuilder elementName;

        for (Map.Entry<NodeWithModifiers<T>, ExpectedElement> match : matches.entrySet()) {
            presentElement = match.getKey();
            expectedElement = match.getValue();
            matchingResult = getMatchingResult(presentElement, expectedElement);
            violationFound = ClassFeedbackGenerator.VIOLATION_CHECKS.get(matchingResult);
            elementName = new StringBuilder(getVariableName(presentElement));
            if (CHECKER_TYPE.equals(ClassMemberType.METHOD)) {
                if ((elementName.indexOf("()")) < -1) {
                    elementName.append("()");
                }
                if (violationFound.equals(MISSING_FIELDS)) {
                    violationFound = MISSING_METHODS;
                }
            }
            ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(classTypeName, elementName.toString(), violationFound, String.join("\n", customFeedback));
            collectedFeedback.add(fb);
        }
        return collectedFeedback;
    }

    /**
     * Perform the Gale-Shapley matching algorithm to get stable matches for each keyword - element pair.
     * Since elements might not be same size, perform the matching on that list, that is smaller.
     * @param presentElements Present elements in code
     * @param expectedElements Expected elements from the configuration
     * @return stable matches from elements
     */
    private Map<NodeWithModifiers<T>, ExpectedElement> performMatching(List<NodeWithModifiers<T>> presentElements, List<ExpectedElement> expectedElements) {
        Map<NodeWithModifiers<T>, ExpectedElement> matches;

        HashMap<NodeWithModifiers<T>, List<ExpectedElement>> presentElementsPreference = getPresentElementPreferenceList(presentElements, expectedElements);
        HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElementsPreference = getExpectedElementPreferenceList(presentElements, expectedElements);

        if(presentElements.size() > expectedElements.size()) {
            matches = findExpectedElemMatching(presentElementsPreference, expectedElementsPreference);
        } else {
            matches = findPresentElemMatching(presentElementsPreference, expectedElementsPreference);
        }

        return matches;
    }

    /**
     * For each present element, generate a preference list of expected elements. THe order is determined by
     * amount of matches the present element has with the expected element.
     * @param presentElements present elements in the solution
     * @param expectedElements expected elements from config
     * @return preference map for each present element
     */
    private HashMap<NodeWithModifiers<T>, List<ExpectedElement>> getPresentElementPreferenceList(List<NodeWithModifiers<T>>  presentElements,
                                                                                             List<ExpectedElement> expectedElements) {
        HashMap<NodeWithModifiers<T>, List<ExpectedElement>> prefList = new HashMap<>();
        for (NodeWithModifiers<T> presentElem : presentElements) {
            Map<ExpectedElement, List<Boolean>> elemMatchingMap = new HashMap<>();

            for (ExpectedElement expectedElem : expectedElements) {
                List<Boolean> matchingResult = getMatchingResult(presentElem, expectedElem);
                elemMatchingMap.put(expectedElem, matchingResult);
            }

            List<Map.Entry<ExpectedElement, List<Boolean>>> sortedList = new ArrayList<>(elemMatchingMap.entrySet());
            sortedList.sort((e1, e2) ->  CheckerUtils.compareMatchingLists(e1.getValue(), e2.getValue()));
            List<ExpectedElement> preferenceArr = new ArrayList<>();
            for (Map.Entry<ExpectedElement, List<Boolean>> entry: sortedList) {
                preferenceArr.add(entry.getKey());
            }
            prefList.put(presentElem, preferenceArr);
        }

        return prefList;
    }

    /**
     * For each expected element, generate a preference list of present elements. THe order is determined by
     * amount of matches the expected element has with the present element.
     * @param presentElements present elements in the solution
     * @param expectedElements expected elements from config
     * @return preference map for each expected element
     */
    private HashMap<ExpectedElement, List<NodeWithModifiers<T>>> getExpectedElementPreferenceList(List<NodeWithModifiers<T>>  presentElements,
                                                                                             List<ExpectedElement> expectedElements) {
        HashMap<ExpectedElement, List<NodeWithModifiers<T>>> prefList = new HashMap<>();
        for (ExpectedElement expectedElement : expectedElements) {
            Map<NodeWithModifiers<T>, List<Boolean>> elemMatchingMap = new HashMap<>();

            for (NodeWithModifiers<T> presentElems : presentElements) {
                List<Boolean> matchingResult = getMatchingResult(presentElems, expectedElement);
                elemMatchingMap.put(presentElems, matchingResult);
            }

            List<Map.Entry<NodeWithModifiers<T>, List<Boolean>>> sortedList = new ArrayList<>(elemMatchingMap.entrySet());
            sortedList.sort((e1, e2) ->  CheckerUtils.compareMatchingLists(e1.getValue(), e2.getValue()));
            List<NodeWithModifiers<T>> preferenceArr = new ArrayList<>();
            for (Map.Entry<NodeWithModifiers<T>, List<Boolean>> entry: sortedList) {
                preferenceArr.add(entry.getKey());
            }
            prefList.put(expectedElement, preferenceArr);
        }

        return prefList;
    }


    /**
     * Find a matching based on the Gale-Shapley algorithm.
     * Present elements are proposing, such that they have the better matches in the end.
     * @param actualElemsPref present elements and their preferences of expected elements
     * @param expectedElemsPref expected elements and their preferences of present elements
     * @return a present element dominant matching
     */
    private Map<NodeWithModifiers<T>, ExpectedElement> findPresentElemMatching(HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref,
                                                                                   HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElemsPref) {

        Map<NodeWithModifiers<T>, ExpectedElement> matches = new HashMap<>();

        for (NodeWithModifiers<T> actualElem: actualElemsPref.keySet()) {
            matches.put(actualElem, null);
        }
        Set<ExpectedElement> possibleExpElemChoices = new HashSet<>(expectedElemsPref.keySet());

        int expElemCount = possibleExpElemChoices.size();

        while(expElemCount>0){
            ExpectedElement currentExpElem = possibleExpElemChoices.iterator().next();
            List<NodeWithModifiers<T>> expElemPrefList = expectedElemsPref.get(currentExpElem);
            boolean noMatchFound = true;

            for (NodeWithModifiers<T> presElem: expElemPrefList) {
                if(matches.get(presElem) == null){
                    noMatchFound = false;
                    matches.put(presElem, currentExpElem);
                    possibleExpElemChoices.remove(currentExpElem);
                    break;
                } else {
                    ExpectedElement acceptedExpElem = matches.get(presElem);
                    if(willChangeExpectedElement(currentExpElem, acceptedExpElem, presElem, actualElemsPref)){
                        noMatchFound = false;
                        matches.put(presElem, currentExpElem);
                        possibleExpElemChoices.add(acceptedExpElem);
                        possibleExpElemChoices.remove(currentExpElem);
                        break;
                    }
                }
            }

            if(noMatchFound) {
                possibleExpElemChoices.remove(currentExpElem);
            }
            expElemCount = possibleExpElemChoices.size();
        }
        return matches;
    }

    /**
     * Find a matching based on the Gale-Shapley algorithm.
     * Expected elements are proposing, such that they have the better matches in the end.
     * @param actualElemsPref present elements and their preferences of expected elements
     * @param expectedElemsPref expected elements and their preferences of present elements
     * @return a expected element dominant matching
     */
    private Map<NodeWithModifiers<T>, ExpectedElement> findExpectedElemMatching(HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref,
                                                                                   HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElemsPref) {

        HashMap<ExpectedElement, NodeWithModifiers<T>> matches = new HashMap<>();

        for (ExpectedElement expectedElement: expectedElemsPref.keySet()) {
            matches.put(expectedElement, null);
        }
        Set<NodeWithModifiers<T>> possiblePresElemChoices = new HashSet<>(actualElemsPref.keySet());
        int presElemCount = possiblePresElemChoices.size();
        while(presElemCount>0){
            NodeWithModifiers<T> currentPresElem = possiblePresElemChoices.iterator().next();
            List<ExpectedElement> presElemPrefList = actualElemsPref.get(currentPresElem);
            boolean noMatchFound = true;

            for (ExpectedElement presElem: presElemPrefList) {
                if(matches.get(presElem) == null){
                    noMatchFound = false;
                    matches.put(presElem, currentPresElem);
                    possiblePresElemChoices.remove(currentPresElem);
                    break;
                } else {
                    NodeWithModifiers<T> acceptedExpElem = matches.get(presElem);
                    if(willChangePresentElement(currentPresElem, acceptedExpElem, presElem, expectedElemsPref)){
                        noMatchFound = false;
                        matches.put(presElem, currentPresElem);
                        possiblePresElemChoices.add(acceptedExpElem);
                        possiblePresElemChoices.remove(currentPresElem);
                        break;
                    }
                }
            }
            if(noMatchFound) {
                possiblePresElemChoices.remove(currentPresElem);
            }
            presElemCount = possiblePresElemChoices.size();
        }

        return matches.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * Checks if the currentExpElem is a better match for currentPresElem than the already matched accepedExpElem
     * @param currentExpElem currently proposing expected element
     * @param acceptedExpElem already matched expected element
     * @param currentPresElem current present element
     * @param actualElemsPref preference list of present element
     * @return either stays the current match or currentPresElem forms a new match with currentExpElem over acceptedExpElem
     */
    private boolean willChangeExpectedElement(ExpectedElement currentExpElem, ExpectedElement acceptedExpElem, NodeWithModifiers<T> currentPresElem,
                                              HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref){

        ExpectedElement prefCurrentExpElem = null;
        ExpectedElement prefAcceptedExpElem = null;

        List<ExpectedElement> womanPrefList = actualElemsPref.get(currentPresElem);
        for (ExpectedElement expectedElement : womanPrefList) {

            if (expectedElement.equals(currentExpElem)) {
                prefCurrentExpElem = expectedElement;
            }

            if (expectedElement.equals(acceptedExpElem)) {
                prefAcceptedExpElem = expectedElement;
            }
        }
        if(prefAcceptedExpElem == null) {
            return true;
        }
        if(prefCurrentExpElem == null) {
            return false;
        }
        List<Boolean> currentBachelorMatching = getMatchingResult(currentPresElem, prefCurrentExpElem);
        List<Boolean> alreadyAcceptedMatching = getMatchingResult(currentPresElem, prefAcceptedExpElem);

        int result = CheckerUtils.compareMatchingLists(currentBachelorMatching, alreadyAcceptedMatching);
        return result < 0;
    }

    /**
     * Checks if the currentPresElem is a better match for currentExpElem than the already matched acceptedPresElem
     * @param currentExpElem currently proposing present element
     * @param acceptedPresElem already matched present element
     * @param currentPresElem current expected element
     * @param expElemPreferences preference list of expected element
     * @return either stays the current match or currentPresElem forms a new match with currentExpElem over acceptedPresElem
     */
    private boolean willChangePresentElement(NodeWithModifiers<T> currentPresElem, NodeWithModifiers<T> acceptedPresElem, ExpectedElement currentExpElem,
                              HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expElemPreferences) {

        NodeWithModifiers<T> prefCurrentPresElem = null;
        NodeWithModifiers<T> prefAcceptedPresElem = null;

        List<NodeWithModifiers<T>> currentExpElemPreferences = expElemPreferences.get(currentExpElem);
        for (NodeWithModifiers<T> presentElement : currentExpElemPreferences) {
            if (presentElement.equals(currentPresElem)) {
                prefCurrentPresElem = presentElement;
            }
            if (presentElement.equals(acceptedPresElem)) {
                prefAcceptedPresElem = presentElement;
            }
        }
        if(prefAcceptedPresElem == null) {
            return true;
        }
        if(prefCurrentPresElem == null) {
            return false;
        }
        List<Boolean> currentBachelorMatching = getMatchingResult(prefCurrentPresElem, currentExpElem);
        List<Boolean> alreadyAcceptedMatching = getMatchingResult(prefAcceptedPresElem, currentExpElem);

        int result = CheckerUtils.compareMatchingLists(currentBachelorMatching, alreadyAcceptedMatching);
        return result < 0;
    }



    /**
     * Checks if more or equal elements are there compared to the expected amount
     * @param expectedElements expected keywords, gives the size of the expected elements
     */
    private ClassFeedback checkIfLessThanExpectedPresent(String classTypeName, List<NodeWithModifiers<T>> presentElements,
                                                         List<ExpectedElement> expectedElements, boolean matchExactAmount) {
        ClassFeedbackType violation;
        if(expectedElements.size() > presentElements.size()) {
            violation = CHECKER_TYPE.equals(ClassMemberType.FIELD) ? MISSING_FIELDS : MISSING_METHODS;
            return ClassFeedbackGenerator.generateFeedback(classTypeName, "", violation, String.join("\n", customFeedback));
        }

        if(matchExactAmount) {
            if(expectedElements.size() < presentElements.size()) {
                violation = CHECKER_TYPE.equals(ClassMemberType.FIELD) ? TOO_MANY_FIELDS : TOO_MANY_METHODS;
                return ClassFeedbackGenerator.generateFeedback(classTypeName, "", violation, String.join("\n", customFeedback));
            }
        }
        return null;
    }

    /**
     * Get all names of the element. Fields can usually have more than one variale in a declaration
     * but as we unrolled them previously, every field can only have one variable.
     * @param element the element to get the name from
     * @return name of the element
     */
    private String getVariableName(NodeWithModifiers<T> element) {
        String elementName;

        if(CHECKER_TYPE.equals(ClassMemberType.FIELD)) {
            elementName = ((FieldDeclaration) element).getVariable(0).getNameAsString();
        } else {
            try{
                elementName = ((MethodDeclaration) element).getNameAsString();
            } catch (Exception e) {
                elementName = ((ConstructorDeclaration) element).getNameAsString();
            }

        }

        return elementName;
    }

    /**
     * Check the type of the element.
     * @param elem the element to check
     * @param expectedTypes expected field / return type
     * @return true if exact match
     */
    private boolean isElementTypeMatch(NodeWithModifiers<T> elem, List<String> expectedTypes) {
        String presentType;
        if(CHECKER_TYPE.equals(ClassMemberType.FIELD)) {
            //no type was provided for the field, treat as any type is allowed.
            if(expectedTypes.size() == 1 && expectedTypes.contains("")) {
                return true;
            }
            FieldDeclaration fieldElement = (FieldDeclaration) elem;
            presentType = fieldElement.getElementType().asString();
        } else {
            try {
                MethodDeclaration methodElement = (MethodDeclaration) elem;
                presentType = methodElement.getType().asString();
            } catch (Exception e) {
                //If it is not possible to cast to MethodDeclaration, then this element must be a constructor
                return !expectedTypes.isEmpty();
            }

        }
        return expectedTypes.contains(presentType.toLowerCase(Locale.ROOT));
    }

    /**
     * True if either the expectedElementName is * (optional) or if the expected and actual names match up
     * @param elem element to check
     * @param expectedElementName expected element name, can either be * or the name
     * @return true if exact match
     */
    private boolean isElementNameMatch(NodeWithModifiers<T> elem, String expectedElementName) {
        if(expectedElementName.equals(KeywordType.OPTIONAL.toString()) || expectedElementName.isBlank()) {
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

        if(CHECKER_TYPE.equals(ClassMemberType.FIELD)) {
            List<FieldDeclaration> foundFields = classDecl.findAll(FieldDeclaration.class);
            unrollVariableDeclarations(foundFields);
            foundFields.forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        } else {
            classDecl.findAll(ConstructorDeclaration.class).forEach(ct -> elementsWithModifiers.add((NodeWithModifiers<T>) ct));
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
        FieldDeclaration fd, field =null;
        while(elemIterator.hasNext()) {
            fd = elemIterator.next();
            if(fd.getVariables().size() > MAX_ALLOWED_VARIABLES) {
                elemIterator.remove();
                for (VariableDeclarator variable: fd.getVariables()) {
                    field = new FieldDeclaration(fd.getModifiers(), variable);
                    elemIterator.add(field);
                }
            }
        }
    }

    /**
     * Check for matches for each individual part of the element
     * @param presentElement present element to check keywords for
     * @param expectedElement expected element to check keywords against
     * @return list of all matches of the format: (access, non access, type, name)
     */
    private List<Boolean> getMatchingResult(NodeWithModifiers<T> presentElement, ExpectedElement expectedElement) {
        List<Boolean> matching = new ArrayList<>();
        boolean accessMatch = CheckerUtils.isAccessMatch(presentElement.getAccessSpecifier().asString(), expectedElement.getPossibleAccessModifiers());
        boolean nonAccessMatch = CheckerUtils.isNonAccessMatch(presentElement.getModifiers(),
                expectedElement.getPossibleNonAccessModifiers(),
                expectedElement.isExactMatch(),
                expectedElement.isContainsYes());
        boolean typeMatch = isElementTypeMatch(presentElement, expectedElement.getTypes());
        boolean nameMatch = isElementNameMatch(presentElement, expectedElement.getName());
        matching.add(accessMatch);
        matching.add(nonAccessMatch);
        matching.add(typeMatch);
        matching.add(nameMatch);
        return matching;
    }
}
