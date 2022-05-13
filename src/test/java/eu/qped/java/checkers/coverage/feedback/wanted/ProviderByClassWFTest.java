package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderByClassWFTest {

    public void getWanted(ProviderByClassWF toTest, WantedFeedback e, String test) {
        class Test {
            int num;
            WantedFeedback want;
            String identifier;

            public Test(int num, WantedFeedback want, String identifier) {
                this.num = num;
                this.want = want;
                this.identifier = identifier;
            }

            @Override
            public String toString() {
                return "Test " + num + " has failed";
            }
        }
        WantedFeedback a = ProviderWF.TEST_WF;
        WantedFeedback b = ProviderWF.COVERAGE_WF;

        WantedFeedback d = new WantedFeedback(FeedbackType.CUSTOM, "CUSTOM");

        toTest.feedbackByIdentifier.put("A", a);
        toTest.feedbackByIdentifier.put("B", b);

        toTest.feedbackByIdentifier.put("D", d);

        List<Test> wanted = Arrays.asList(
                new Test(1, a, "A"),
                new Test(2, b, "B"),
                new Test(4, d, "D"),
                new Test(5, e, "E"));

        for (Test t : wanted) {
            assertEquals(t.want, toTest.provide(t.identifier), test + t.toString());
        }
    }

    @Test
    public void noDefault() {
        getWanted(new ProviderByClassWF(), null, "noDefault: ");
    }

    @Test
    public void defaultGeneratorTest() {
        ProviderByClassWF toTest = new ProviderByClassWF();
        toTest.defaultType = FeedbackType.TEST;
        getWanted(toTest, ProviderWF.TEST_WF, "defaultGeneratorTest: ");
    }

    @Test
    public void defaultGeneratorCoverage() {
        ProviderByClassWF toTest = new ProviderByClassWF();
        toTest.defaultType = FeedbackType.COVERAGE;
        getWanted(toTest, ProviderWF.COVERAGE_WF, "defaultGeneratorCoverage: ");
    }

    @Test
    public void defaultIsWrongType() {
        ProviderByClassWF toTest = new ProviderByClassWF();
        toTest.defaultType = FeedbackType.CUSTOM;
        getWanted(toTest, null, "defaultIsWrongType: ");
    }
}