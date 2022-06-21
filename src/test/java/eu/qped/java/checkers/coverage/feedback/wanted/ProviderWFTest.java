package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import eu.qped.java.checkers.coverage.feedback.DefaultFB;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderWFTest {

    private void getWanted(String test, boolean allTest, boolean allCoverage, WantedFeedback e, WantedFeedback f) {
        class Test {
            boolean isTest;
            String className;
            String identifier;
            WantedFeedback want;
            int num;

            public Test(boolean isTest, String className, String identifier, WantedFeedback want, int num) {
                this.isTest = isTest;
                this.className = className;
                this.identifier = identifier;
                this.want = want;
                this.num = num;
            }

            @Override
            public String toString() {
                return "Test " + num + " has failed";
            }
        }

        DefaultFB df = new DefaultFB();
        df.setTestFB(String.format("%s%s%s%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_WANT, WantedFeedback.REPLACE_GOT));
        df.setIfFB(String.format("%s%s%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setElseFB(String.format("%s %s%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setElseIfFB(String.format("%s%s %s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setForFB(String.format("%s %s %s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setForEachFB(String.format("%s_%s%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setWhileFB(String.format("%s%s_%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));
        df.setCaseFB(String.format("%s_%s_%s", WantedFeedback.REPLACE_CLASS, WantedFeedback.REPLACE_METHOD, WantedFeedback.REPLACE_INDEX));

        WantedFeedback a = ProviderWF.TEST_WF;
        WantedFeedback b = ProviderWF.COVERAGE_WF;

        WantedFeedback d = new WantedFeedback(FeedbackType.CUSTOM, "CUSTOM");

        ProviderByClassWF part = new ProviderByClassWF();
        part.feedbackByIdentifier.put("a", a);
        part.feedbackByIdentifier.put("b", b);

        part.feedbackByIdentifier.put("d", d);
        Map<String, ProviderByClassWF> classByClassName = new HashMap<>();
        classByClassName.put("A", part);

        List<Test> wanted = Arrays.asList(
                new Test(true, "A", "a", ProviderWF.TEST_WF, 1),
                new Test(false, "A",  "b", ProviderWF.COVERAGE_WF, 2),
                new Test(false, "A",  "d", d, 4),
                new Test(false, "A",  "e", e, 5),
                new Test(true, "A", "f", f, 6));

        ProviderWF toTest = new ProviderWF(classByClassName, allTest, allCoverage, df);

        for (Test t : wanted) {
            assertEquals(t.want, toTest.provide(t.isTest, t.className, t.identifier), test + t.toString());
        }
    }

    @org.junit.jupiter.api.Test
    public void noDefault() {
        getWanted("noDefault: ", false, false, null, null);
    }

    @org.junit.jupiter.api.Test
    public void defaultTest() {
        getWanted("defaultTest: ", true, false, null, ProviderWF.TEST_WF);
    }

    @org.junit.jupiter.api.Test
    public void defaultCoverage() {
        getWanted("defaultCoverage: ", false, true, ProviderWF.COVERAGE_WF, null);
    }

    @org.junit.jupiter.api.Test
    public void defaultBoth() {
        getWanted("defaultBoth: ", true, true, ProviderWF.COVERAGE_WF, ProviderWF.TEST_WF);
    }

}