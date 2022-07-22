package eu.qped.java.checkers.classdesign;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
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

        HashMap<NodeWithModifiers<T>, List<ExpectedElement>> presentElemsPref = getPresentElementPreferenceList(presentElements, expectedElements);
        HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElemsPref = getExpectedElementPreferenceList(presentElements, expectedElements);

        Map<NodeWithModifiers<T>, ExpectedElement> matches;

        if(presentElements.size() > expectedElements.size()) {
            //ExpectedElement has to propose first
            matches = findExpectedElemMatching(presentElemsPref, expectedElemsPref);
        } else {
            //PresentElements have to propose first
            matches = findPresentElemMatching(presentElemsPref, expectedElemsPref);
        }

        for (Map.Entry<NodeWithModifiers<T>, ExpectedElement> match : matches.entrySet()) {
            NodeWithModifiers<T> presentElement = match.getKey();
            ExpectedElement expectedElement = match.getValue();
            List<Boolean> matchingResult = getMatchingResult(presentElement, expectedElement);
            ClassFeedbackType violationFound = ClassFeedbackGenerator.VIOLATION_CHECKS.get(matchingResult);
            String elementName = getVariableName(presentElement);
            if (CHECKER_TYPE.equals(ClassMemberType.METHOD)) {
                if (!elementName.contains("()")) {
                    elementName += "()";
                }
                if (violationFound.equals(MISSING_FIELDS)) {
                    violationFound = MISSING_METHODS;
                }
            }
            ClassFeedback fb = ClassFeedbackGenerator.generateFeedback(classTypeName, elementName, violationFound, String.join("\n", customFeedback));
            collectedFeedback.add(fb);
        }
        return collectedFeedback;
    }

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




    private Map<NodeWithModifiers<T>, ExpectedElement> findPresentElemMatching(HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref,
                                                                                   HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElemsPref) {
        //actualElems = women
        //expectedElems = men

        Map<NodeWithModifiers<T>, ExpectedElement> matches = new HashMap<>();

        for (NodeWithModifiers<T> actualElem: actualElemsPref.keySet()) {
            matches.put(actualElem, null);
        }
        Set<ExpectedElement> bachelors = new HashSet<>(expectedElemsPref.keySet());

        int bachelorCount = bachelors.size();

        while(bachelorCount>0){
            ExpectedElement currentBachelor = bachelors.iterator().next();
            List<NodeWithModifiers<T>> menPrefList = expectedElemsPref.get(currentBachelor);
            boolean noMatchFound = true;

            for (NodeWithModifiers<T> woman: menPrefList) {
                if(matches.get(woman) == null){
                    noMatchFound = false;
                    matches.put(woman, currentBachelor);
                    bachelors.remove(currentBachelor);
                    break;
                } else {
                    ExpectedElement alreadyAcceptedMan = matches.get(woman);
                    if(willChangeExpectedElement(currentBachelor, alreadyAcceptedMan, woman, actualElemsPref)){
                        noMatchFound = false;
                        matches.put(woman, currentBachelor);
                        bachelors.add(alreadyAcceptedMan);
                        bachelors.remove(currentBachelor);
                        break;
                    }
                }
            }

            if(noMatchFound) {
                bachelors.remove(currentBachelor);
            }
            bachelorCount = bachelors.size();
        }
        return matches;
    }

    private Map<NodeWithModifiers<T>, ExpectedElement> findExpectedElemMatching(HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref,
                                                                                   HashMap<ExpectedElement, List<NodeWithModifiers<T>>> expectedElemsPref) {

        HashMap<ExpectedElement, NodeWithModifiers<T>> matches = new HashMap<>();

        for (ExpectedElement expectedElement: expectedElemsPref.keySet()) {
            matches.put(expectedElement, null);
        }

        Set<NodeWithModifiers<T>> bachelors = new HashSet<>(actualElemsPref.keySet());
        int bachelorCount = bachelors.size();
        while(bachelorCount>0){
            NodeWithModifiers<T> currentBachelor = bachelors.iterator().next();
            List<ExpectedElement> womanPrefList = actualElemsPref.get(currentBachelor);
            boolean noMatchFound = true;

            for (ExpectedElement man: womanPrefList) {
                if(matches.get(man) == null){
                    noMatchFound = false;
                    matches.put(man, currentBachelor);
                    bachelors.remove(currentBachelor);
                    break;
                } else {
                    NodeWithModifiers<T> alreadyAcceptedWoman = matches.get(man);
                    if(willChangePresentElement(currentBachelor, alreadyAcceptedWoman, man, expectedElemsPref)){
                        noMatchFound = false;
                        matches.put(man, currentBachelor);
                        bachelors.add(alreadyAcceptedWoman);
                        bachelors.remove(currentBachelor);
                        break;
                    }
                }
            }
            if(noMatchFound) {
                bachelors.remove(currentBachelor);
            }
            bachelorCount = bachelors.size();
        }

        return matches.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    boolean willChangePresentElement(NodeWithModifiers<T> currentBachelor, NodeWithModifiers<T> alreadyAcceptedWoman, ExpectedElement currentMan,
                              HashMap<ExpectedElement, List<NodeWithModifiers<T>>> actualElemsPref) {

        NodeWithModifiers<T> pref_currentBachelor = null;
        NodeWithModifiers<T> pref_alreadyAcceptedWoman = null;

        List<NodeWithModifiers<T>> womanPrefList = actualElemsPref.get(currentMan);
        //get the preferences of both the men
        for (NodeWithModifiers<T> presentElement : womanPrefList) {

            if (presentElement.equals(currentBachelor)) {
                pref_currentBachelor = presentElement;
            }

            if (presentElement.equals(alreadyAcceptedWoman)) {
                pref_alreadyAcceptedWoman = presentElement;
            }

        }

        if(pref_alreadyAcceptedWoman == null) {
            return true;
        }

        if(pref_currentBachelor == null) {
            return false;
        }

        //women will accept the current bachelor only if he has higher preference
        //than the man she had accepted earlier
        //compute the matching of each with the woman, higher one gets assigned
        //if a tie exists, use the modifier ranking to determine winner
        List<Boolean> currentBachelorMatching = getMatchingResult(pref_currentBachelor, currentMan);
        List<Boolean> alreadyAcceptedMatching = getMatchingResult(pref_alreadyAcceptedWoman, currentMan);

        int result = CheckerUtils.compareMatchingLists(currentBachelorMatching, alreadyAcceptedMatching);
        return result < 0;
    }

    boolean willChangeExpectedElement(ExpectedElement currentBachelor, ExpectedElement alreadyAcceptedMan, NodeWithModifiers<T> currentWoman,
                                      HashMap<NodeWithModifiers<T>, List<ExpectedElement>> actualElemsPref){

        ExpectedElement pref_currentBachelor = null;
        ExpectedElement pref_alreadyAcceptedMan = null;

        List<ExpectedElement> womanPrefList = actualElemsPref.get(currentWoman);

        //Does the current and preferred person exist in the pref list?
        for (ExpectedElement expectedElement : womanPrefList) {

            if (expectedElement.equals(currentBachelor)) {
                pref_currentBachelor = expectedElement;
            }

            if (expectedElement.equals(alreadyAcceptedMan)) {
                pref_alreadyAcceptedMan = expectedElement;
            }
        }
        //If one of them is null, that means that the assigned element does not exist in the preference list of the other
        //Thus we can remove and say and it wants to change.
        if(pref_alreadyAcceptedMan == null) {
            return true;
        }

        //If the preferred current bachelor does not exist, that means that the pref list does not contain this one
        //and we can say that we do not want to change
        if(pref_currentBachelor == null) {
            return false;
        }

        //women will accept the current bachelor only if he has higher preference
        //than the man she had accepted earlier
        //compute the matching of each with the woman, higher one gets assigned
        //if a tie exists, use the modifier ranking to determine winner
        List<Boolean> currentBachelorMatching = getMatchingResult(currentWoman, pref_currentBachelor);
        List<Boolean> alreadyAcceptedMatching = getMatchingResult(currentWoman, pref_alreadyAcceptedMan);

        int result = CheckerUtils.compareMatchingLists(currentBachelorMatching, alreadyAcceptedMatching);
        return result < 0;
    }

//    /**
//     * Determine the most likely match for a keyword issue. This is accomplished by iterating through all possible
//     * expected keyword pairs and picking the most likely one. The likeliness is determined by the amount of correct
//     * matches between keywords. The higher the match count, the more likely that this is the correct expectedElement object
//     * for the present element.
//     * If there are ties between matches, and no better can be found, we determine the order by selecting the object,
//     * that possesses the first "false" match, as this indicates a more important error, thus needing a feedback message
//     * more than the other object.
//     * If no match can be found, we assume that the expected element simply does not exist and return that.
//     * @param presentElement Element to find a match for
//     //* @param expectedElements all possible expected elements
//     * @return the most likely match, in form of a boolean list, indicating the presence of keywords in the declaration
//     */
//    private Map<ExpectedElement, List<Boolean>> getMostLikelyMatchResult(NodeWithModifiers<T> presentElement, List<ExpectedElement> expectedElements) {
//        int maxCount = 0;
//        ExpectedElement maxExpectedElement = expectedElements.get(0);
//        List<Boolean> maxMatchingResult = Arrays.asList(false, false, false, false); //Assume that the element is missing
//        Map<ExpectedElement, List<Boolean>> matchingMap = new HashMap<>();
//
//        for (ExpectedElement expectedElement : expectedElements) {
//            List<Boolean> matchingResult = getMatchingResult(presentElement, expectedElement);
//
//            int countMatchings = 0;
//            for (Boolean match : matchingResult) {
//                if (match) {
//                    countMatchings++;
//                }
//                if (countMatchings > maxCount) {
//                    maxCount = countMatchings;
//                    maxMatchingResult = matchingResult;
//                    maxExpectedElement = expectedElement;
//                } else if (countMatchings == maxCount && countMatchings > 0) {
//                    for (int i = 0; i < matchingResult.size(); i++) {
//                        if (!matchingResult.get(i).equals(maxMatchingResult.get(i))) {
//                            //Find the one that is wrong first
//                            if (!matchingResult.get(i)) {
//                                maxMatchingResult = matchingResult;
//                                maxExpectedElement = expectedElement;
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        expectedElements.remove(maxExpectedElement);
//
//        matchingMap.put(maxExpectedElement, maxMatchingResult);
//        return matchingMap;
//    }

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
            elementName = ((MethodDeclaration) element).getNameAsString();
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
        if(expectedTypes.isEmpty()) {
            return false;
        }

        String presentType;
        if(CHECKER_TYPE.equals(ClassMemberType.FIELD)) {
            FieldDeclaration fieldElement = (FieldDeclaration) elem;
            presentType = fieldElement.getElementType().asString();
        } else {
            MethodDeclaration methodElement = (MethodDeclaration) elem;
            presentType = methodElement.getType().asString();
        }
        return expectedTypes.contains(presentType.toLowerCase());
    }

    /**
     * True if either the expectedElementName is * (optional) or if the expected and actual names match up
     * @param elem element to check
     * @param expectedElementName expected element name, can either be * or the name
     * @return true if exact match
     */
    private boolean isElementNameMatch(NodeWithModifiers<T> elem, String expectedElementName) {
        if(expectedElementName.equals(KeywordType.OPTIONAL.toString())) {
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
