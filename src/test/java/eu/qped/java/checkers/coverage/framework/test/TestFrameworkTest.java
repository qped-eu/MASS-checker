package eu.qped.java.checkers.coverage.framework.test;

import eu.qped.java.checkers.coverage.*;
import eu.qped.java.checkers.coverage.testhelp.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class TestFrameworkTest {
    private class Results implements TestCollection {
        LinkedList<TestResult> results = new LinkedList<>();
        Results() {
        }
        @Override
        public void add(TestResult result) {
            results.add(result);
        }
    }

    @Test
    public void testingJUnit5() {
        genericTestTesting("JUNIT5");
    }

    private void genericTestTesting(String framework) {
        assertNotNull(framework);
        Results got = new Results();
        TestFramework toTest = TestFrameworkFactoryAbstract.create(framework).create();
        genericHelp(
                toTest,
                new HashMap<>(),
                got,
                "test.TestFrameworkIsCorrect");
        genericHelp(
                toTest,
                new HashMap<>() {{
                put("wrongTestIsTrue", new TestResult("TestFrameworkIsWrong","wrongTestIsTrue", "true", "false"));
                put("wrongTestIsFalse", new TestResult("TestFrameworkIsWrong","wrongTestIsFalse", "false", "true"));
                put("wrongTestIsFalseCustom", new TestResult("TestFrameworkIsWrong","wrongTestIsFalseCustom", "false", "true"));
                put("wrongTestIsZero", new TestResult("TestFrameworkIsWrong","wrongTestIsZero", "1", "0"));
                put("wrongTestIsZeroCustom", new TestResult("TestFrameworkIsWrong","wrongTestIsZeroCustom", "1", "0"));}},
                got,
                "test.TestFrameworkIsWrong");
    }

    private void genericHelp(TestFramework toTest, Map<String, TestResult> want, Results got, String test) {
        Preprocessed preprocessed = new Preprocessing().processingOnlyByteCode(
                FileResources.filesByClassName,
                FileResources.convertNames,
                List.of(test),
                List.of("test.TestFramework"));
        MemoryLoader memoryLoader = new MemoryLoader();
        preprocessed.getTestClasses().forEach(c -> memoryLoader.upload(c.className(), c.byteCode()));
        preprocessed.getClasses().forEach(c -> memoryLoader.upload(c.className(), c.byteCode()));
        try {
            got = (Results) toTest.testing(
                    preprocessed.getTestClasses().stream().map(CovInformation::className).collect(Collectors.toList()),
                    memoryLoader,
                    got );
            assertEquals(want.keySet().size(), got.results.size());
            for (TestResult g : got.results) {
                assertEquals(want.get(g.methodName()), g);
            }
        } catch (Exception e) {
            assertFalse(true, e.getMessage());
        }
    }

}