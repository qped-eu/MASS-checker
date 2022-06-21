package eu.qped.java.checkers.coverage.testhelp;

import eu.qped.java.checkers.coverage.CovInformation;
import java.util.*;

public class Preprocessed {
    private final LinkedList<PreprocessedClass> testClasses;
    private final LinkedList<PreprocessedClass> classes;
    private final LinkedList<String> failedClasses;

    protected Preprocessed(
            LinkedList<PreprocessedClass> testClasses,
            LinkedList<PreprocessedClass> classes,
            LinkedList<String> failedClasses) {
        this.testClasses = testClasses;
        this.classes = classes;
        this.failedClasses = failedClasses;
    }

    public List<CovInformation> getTestClasses() {
        return Collections.unmodifiableList(testClasses);
    }

    public List<CovInformation> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public List<String> failedClasses() {
        return Collections.unmodifiableList(failedClasses);
    }

    public boolean hasFailed() {
        return ! failedClasses.isEmpty();
    }

}
