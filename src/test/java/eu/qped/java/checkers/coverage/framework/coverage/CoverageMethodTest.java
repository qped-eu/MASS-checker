package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CoverageMethodTest {
    private class Parameters {
        private final CoverageCount branch;
        private final CoverageCount line;
        private final StateOfCoverage stateMethod;
        private final String className;
        private final String methodName;

        Parameters(
                CoverageCount branch,
                CoverageCount line,
                StateOfCoverage stateMethod,
                String className,
                String methodName) {
            this.branch = branch;
            this.line = line;
            this.stateMethod = stateMethod;
            this.className = className;
            this.methodName = methodName;
        }
    }

    @Test
    public void constructor() {
        CoverageMethod toTest = new CoverageMethod(
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.NOT,
                "Class",
                "Method");

        assertEquals(new CoverageCount(1,0), toTest.branch());
        assertEquals(new CoverageCount(2,1), toTest.line());
        assertEquals(StateOfCoverage.NOT, toTest.state());
        assertEquals("Class", toTest.className());
        assertEquals("Method", toTest.methodName());
    }

    @Test
    public void constructorException() {
        Parameters[] nullPointerException = new Parameters[] {
                new Parameters(null, new CoverageCount(0,0), StateOfCoverage.NOT, "C", "M"),
                new Parameters( new CoverageCount(0,0),null, StateOfCoverage.NOT, "C", "M"),
                new Parameters( new CoverageCount(0,0), new CoverageCount(0,0), null, "C", "M"),
                new Parameters( new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.NOT, null, "M"),
                new Parameters( new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.NOT, "C", null),
                new Parameters( new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.NOT, "", "M"),
                new Parameters( new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.NOT, "C", ""),
        };

        int i = 0;
        for (Parameters p : nullPointerException) {
            assertThrows(Exception.class, () -> {
                new CoverageMethod(p.branch, p.line, p.stateMethod, p.className, p.methodName);
            }, "at index " + i++);
        }
    }

    @Test
    public void equalsTest() {
        CoverageMethod a = new CoverageMethod(
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class",
                "Method");

        CoverageMethod b = new CoverageMethod(
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class",
                "Method");

        CoverageMethod c = new CoverageMethod(
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class",
                "Method");

        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);

        Parameters[] notEquals = new Parameters[] {
                new Parameters( new CoverageCount(4,0), new CoverageCount(2,1), StateOfCoverage.FULL, "Class", "Method"),
                new Parameters( new CoverageCount(1,0), new CoverageCount(4,1), StateOfCoverage.FULL, "Class", "Method"),
                new Parameters( new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.NOT, "Class", "Method"),
                new Parameters( new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.FULL, "C", "Method"),
                new Parameters( new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.FULL, "Class", "M")};

        int i = 0;
        for (Parameters p : notEquals) {
            assertNotEquals(a, new CoverageMethod(p.branch, p.line, p.stateMethod, p.className, p.methodName), "at index " + i++);
        }
    }

}