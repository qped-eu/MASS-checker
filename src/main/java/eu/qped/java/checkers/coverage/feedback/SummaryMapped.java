package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.feedback.wantMap.Provider;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;

import java.util.*;


public class SummaryMapped implements CoverageCollection, TestCollection {
    public class LineFB extends Feedback {
        public final String className;
        public final String index;

        public LineFB(String className, int index) {
            super("");
            this.className = className;
            this.index = Integer.toString(index);
        }
    }

    public class ClassFB {
        private final List<Feedback> feedbacks = new LinkedList<>();
        private final CoverageClass coverageClass;

        public ClassFB(CoverageClass coverageClass) {
            this.coverageClass = Objects.requireNonNull(coverageClass);
        }

        public List<Feedback> feedbacks() {
            return feedbacks;
        }

        public String className() {
            return coverageClass.className();
        }

        public StateOfCoverage state() {
            return coverageClass.stateOfClass();
        }

        public CoverageCount line() {
            return coverageClass.line();
        }

        public CoverageCount branch() {
            return coverageClass.branch();
        }

        public boolean analyse(Provider provider) {
            for (int index = 0; index < coverageClass.endIndex(); index++) {
                if (coverageClass.byIndex(index).equals(StateOfCoverage.NOT)) {
                    LineFB fb = new LineFB(className(), index);
                    if (provider.setFeedback(fb))
                        feedbacks.add(fb);
                }
            }
            return !feedbacks.isEmpty();
        }
    }

    private final List<TestFB> testFBs = new LinkedList<>();
    private final List<ClassFB> classFBs = new ArrayList<>();

    public List<TestFB> testFBs() {
        return testFBs;
    }

    public List<ClassFB> classFBs() {
        return classFBs;
    }

    @Override
    public void add(Coverage coverage) {
        if (coverage instanceof CoverageClass)
            classFBs.add(new ClassFB((CoverageClass) coverage));
    }

    @Override
    public void add(TestResult result) {
        testFBs.add(new TestFB(result));
    }

    public void analyse(Provider provider) {
        for (Iterator<TestFB> iterator = testFBs.iterator(); iterator.hasNext(); ) {
            if (! provider.setFeedback(iterator.next()))
                iterator.remove();
        }
        for (Iterator<ClassFB> iterator = classFBs.iterator(); iterator.hasNext(); ) {
            if (! iterator.next().analyse(provider))
                iterator.remove();
        }
    }

}
