package eu.qped.java.checkers.classdesign;

import java.util.ArrayList;
import java.util.List;

import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackGenerator;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedbackType;

public final class TestUtils {

    private TestUtils() {}

    public static ClassFeedback getFeedback(String className, String elementName, ClassFeedbackType violation) {
        return ClassFeedbackGenerator.generateFeedback(className, elementName, violation, "");
    }

    public static String[][] getAllSubsets(List<String> modifierList) {
        List<String[]> combinationList = new ArrayList<>();
        // Start i at 1, so that we do not include the empty set in the results
        for (long i = 1; i < Math.pow(2, modifierList.size()); i++ ) {
            List<String> portList = new ArrayList<>();
            for ( int j = 0; j < modifierList.size(); j++ ) {
                if ( (i & (long) Math.pow(2, j)) > 0 ) {
                    // Include j in set
                    portList.add(modifierList.get(j));
                }
            }
            combinationList.add(portList.toArray(new String[0]));
        }
        return combinationList.toArray(new String[0][0]);
    }

    public static List<String> getDifferenceNonAccess(List<String> modifiers, List<String> disallowedNonAccess) {
        List<String> allowedMods = new ArrayList<>();
        for (String modifier : modifiers) {
            if(!disallowedNonAccess.contains(modifier)) {
                allowedMods.add(modifier);
            }
        }
        return allowedMods;
    }
}
