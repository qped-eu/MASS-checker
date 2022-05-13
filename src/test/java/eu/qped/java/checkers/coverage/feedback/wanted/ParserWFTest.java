package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ParserWFTest {

    @Test
    public void formatWithWrongEncoding() {
        List<String> wantedFeedback = Arrays.asList(
                "wrong",
                "wrong?:TEST",
                "a:wrong",
                "a:wrong:l:a",
                ":TEST",
                ":COVERAGE",
                "A:TEST",
                "B:COVERAGE",
                "C:TEST:a:",
                "C:COVERAGE:c:",
                "C:CUSTOM:d:custom"
        );
        ProviderWF got = new ParserWF().parse("ger", wantedFeedback);

        assertEquals(got.wantsAllTest(), true);
        assertEquals(got.wantsAllCoverage(), true);

        class Test {
            int num;
            String wantedKey;
            FeedbackType wantedType;
            List<WantedFeedback> wantedFeedbacks;

            public Test(int num, String wantedKey, FeedbackType wantedType, List<WantedFeedback> wantedFeedbacks) {
                this.num = num;
                this.wantedKey = wantedKey;
                this.wantedType = wantedType;
                this.wantedFeedbacks = wantedFeedbacks;
            }
        }

        List<Test> tests = Arrays.asList(
                new Test(1, "A", FeedbackType.TEST, Arrays.asList(ProviderWF.TEST_WF, ProviderWF.TEST_WF, ProviderWF.TEST_WF, ProviderWF.TEST_WF)),
                new Test(2, "B", FeedbackType.COVERAGE, Arrays.asList(ProviderWF.COVERAGE_WF, ProviderWF.COVERAGE_WF, ProviderWF.COVERAGE_WF, ProviderWF.COVERAGE_WF)),
                new Test(3, "C", null, Arrays.asList(
                        new WantedFeedback(FeedbackType.TEST, ""),
                        null,
                        new WantedFeedback(FeedbackType.COVERAGE, ""),
                        new WantedFeedback(FeedbackType.CUSTOM, "custom")
                )));

        for (Test t : tests) {
            assertTrue(got.classByClassname().keySet().contains(t.wantedKey), "CoverageFeedbackTest: Test "  + t.num + " the key missing");
            ProviderByClassWF g = got.classByClassname().get(t.wantedKey);
            assertEquals(t.wantedType, g.defaultType,"CoverageFeedbackTest: Test "  + t.num );
            int i = 0;
            for (String k : Arrays.asList("a", "b", "c", "d")) {
                assertEquals(t.wantedFeedbacks.get(i ++), g.provide(k), "CoverageFeedbackTest: Test "  + t.num + " at k = " + k);
            }
        }

        assertEquals(got.provide(true, "C", "b"), ProviderWF.TEST_WF);
        assertEquals(got.provide(false, "C", "b"), ProviderWF.COVERAGE_WF);
    }

}