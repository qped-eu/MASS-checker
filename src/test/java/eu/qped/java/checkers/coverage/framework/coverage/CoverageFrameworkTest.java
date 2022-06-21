package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.framework.test.*;
import eu.qped.java.checkers.coverage.testhelp.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CoverageFrameworkTest {
    private class Results implements CoverageCollection {
        LinkedList<CoverageMethod> coverages = new LinkedList<>();
        CoverageClass coverageClass;

        Results() {
        }

        @Override
        public void add(TestResult result) {}

        @Override
        public void add(Coverage coverage) {
            if (coverage instanceof CoverageClass) {
                coverageClass = (CoverageClass) coverage;
            } else {
                coverages.add((CoverageMethod) coverage);
            }
        }
    }

    @Test
    public void analyzeJacoco(){
        genericTest("JACOCO");
    }

    private void genericTest(String coverageframework) {
        genericTest(coverageframework, "JUNIT5");
    }

    private void genericTest(String coverageframework, String testframework) {
        assertNotNull(coverageframework);
        assertNotNull(testframework);
        TestFrameworkFactory testFrameworkFactory = TestFrameworkFactoryAbstract.create(testframework);
        CoverageFramework toTest = CoverageFrameworkFactoryAbstract.create(coverageframework).create(testFrameworkFactory);
        Results got = new Results();
        genericHelp(
                toTest,
                got,
                bagCoverage(),
                Arrays.asList("add", "remove", "length", "equals", "getElems", "cardinality").stream().collect(Collectors.toSet()),
                List.of("case_all_method"),
                List.of("adt.Bag"));
    }

    private void genericHelp(CoverageFramework toTest, Results got, List<StateOfCoverage> byIndex, Set<String> want, List<String> testClasses, List<String> classes) {
        Preprocessed preprocessed = new Preprocessing().processingOnlyByteCode(
                FileResources.filesByClassName,
                FileResources.convertNames,
                testClasses,
                classes);
        try {
            got = (Results) toTest.analyze(
                    got,
                    new LinkedList<>(preprocessed.getTestClasses()),
                    new LinkedList<>(preprocessed.getClasses()));

            assertEquals(want.size(), got.coverages.size());
            for (CoverageMethod g : got.coverages) {
                assertTrue(want.contains(g.methodName()));
            }
            assertNotNull(got.coverageClass);
            int i = 1;
            for (StateOfCoverage w : byIndex) {
                assertEquals(w, got.coverageClass.byIndex(i ++), "at index::" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<StateOfCoverage> bagCoverage() {
        return Arrays.asList(
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.FULL,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.PARTLY,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.PARTLY,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.FULL,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY,
                StateOfCoverage.EMPTY
                );
    }
}