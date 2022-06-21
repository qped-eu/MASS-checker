package eu.qped.java.checkers.coverage.feedback.wanted;

import eu.qped.java.checkers.coverage.enums.FeedbackType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserDefaultWFTest {

    @Test
    public void parserTest() {
        class Test {
            String testName;
            String string;
            boolean hasMatch;
            String wantClass;
            FeedbackType wantType;
            public Test(String testName, String string, boolean hasMatch, String wantClass, FeedbackType wantType) {
                this.testName = testName;
                this.string = string;
                this.hasMatch = hasMatch;
                this.wantClass = wantClass;
                this.wantType = wantType;
            }
        }
        List<Test> tests = Arrays.asList(
                new Test("null: ", null, false, null, null),
                new Test("empty: ", "", false, null, null),
                new Test("empty no type: " , ":", false, null, null),
                new Test("empty type  TEST: " , ":TEST", true, "", FeedbackType.TEST),
                new Test("empty type COVERAGE: " , ":COVERAGE", true, "", FeedbackType.COVERAGE),
                new Test("full type COVERAGE :" , "CLASS:COVERAGE", true, "CLASS", FeedbackType.COVERAGE),
                new Test("full type CUSTOM :" , "CLASS:CUSTOM", false, null, null));
        ParserDefaultWF toTest = new ParserDefaultWF();
        for (Test test : tests) {
            assertEquals(test.hasMatch, toTest.parse(test.string), test.testName + "hasMatch not equals");
            assertEquals(test.wantClass, toTest.className(), test.testName + "parseClassName not equals");
            assertEquals(test.wantType, toTest.type(), test.testName + "parseType not equals");
        }
    }

}