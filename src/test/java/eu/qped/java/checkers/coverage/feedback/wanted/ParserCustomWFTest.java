package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ParserCustomWFTest {

    @Test
    public void parseTest() {
        class Test {
            String testName;
            String string;
            boolean hasMatch;
            String wantClass;
            FeedbackType wantType;
            String wantIdentifier;
            String wantFeedback;

            public Test(String testName, String string, boolean hasMatch, String wantClass, FeedbackType wantType, String wantIdentifier, String wantFeedback) {
                this.testName = testName;
                this.string = string;
                this.hasMatch = hasMatch;
                this.wantClass = wantClass;
                this.wantType = wantType;
                this.wantIdentifier = wantIdentifier;
                this.wantFeedback = wantFeedback;
            }
        }
        List<Test> tests = Arrays.asList(
                new Test("null: ", null, false, null, null, null, null),
                new Test("empty: ", "", false, null, null, null, null),
                new Test("empty no type: " , ":::", false, null, null, null, null),
                new Test("empty TEST:" , ":TEST::", false, null, null, null, null),
                new Test("empty COVERAGE:" , ":COVERAGE::", false, null, null, null, null),
                new Test("empty CUSTOM:" , ":CUSTOM::", false, null, null, null, null),
                new Test("full CUSTOM:" , "CLASS:CUSTOM:IDENTIFIER:MSG", true, "CLASS", FeedbackType.CUSTOM, "IDENTIFIER", "MSG"),
                new Test("full TEST:" , "CLASS:TEST:IDENTIFIER:MSG", true, "CLASS", FeedbackType.TEST, "IDENTIFIER", "MSG"),
                new Test("full COVERAGE:" , "CLASS:COVERAGE:IDENTIFIER:MSG", true, "CLASS", FeedbackType.COVERAGE, "IDENTIFIER", "MSG"));
        ParserCustomWF toTest = new ParserCustomWF();
        for (Test test : tests) {
            assertEquals(test.hasMatch, toTest.parse(test.string), test.testName + "hasMatch not equals");
            assertEquals(test.wantClass, toTest.className(), test.testName + "parseClassName not equals");
            assertEquals(test.wantType, toTest.type(), test.testName + "parseType not equals");
            assertEquals(test.wantIdentifier, toTest.identifier(), test.testName + "parseIdentifier not equals");
            assertEquals(test.wantFeedback, toTest.customWF(), test.testName + "parseCustomFeedback not equals");
        }
    }

}