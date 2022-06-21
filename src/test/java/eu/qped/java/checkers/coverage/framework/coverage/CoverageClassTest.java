package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoverageClassTest {
    private class Parameters {
        private final ArrayList<StateOfCoverage> byIndex;
        private final CoverageCount branch;
        private final CoverageCount line;
        private final StateOfCoverage stateClass;
        private final String className;
        Parameters(
                ArrayList<StateOfCoverage> byIndex,
                CoverageCount branch,
                CoverageCount line,
                StateOfCoverage stateClass,
                String className) {
            this.byIndex = byIndex;
            this.branch = branch;
            this.line = line;
            this.stateClass = stateClass;
            this.className = className;
        }
    }

    @Test
    public void constructor() {
        CoverageClass toTest = new CoverageClass(
                new ArrayList<>(List.of(
                        StateOfCoverage.FULL,
                        StateOfCoverage.NOT)),
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class");

        assertEquals(new CoverageCount(1,0), toTest.branch());
        assertEquals(new CoverageCount(2,1), toTest.line());
        assertEquals(StateOfCoverage.FULL, toTest.state());
        assertEquals("Class", toTest.className());


        int index = 0;
        for (StateOfCoverage want : new StateOfCoverage[] {
                StateOfCoverage.FULL,
                StateOfCoverage.NOT,
                StateOfCoverage.EMPTY}) {

            assertEquals(want, toTest.byIndex(index ++));
        }
    }

    @Test
    public void constructorException() {
        Parameters[] nullPointerException = new Parameters[] {
                new Parameters(null, new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.FULL, "C"),
                new Parameters(new ArrayList<>(), null, new CoverageCount(0,0), StateOfCoverage.FULL,  "C"),
                new Parameters(new ArrayList<>(), new CoverageCount(0,0),null, StateOfCoverage.FULL, "C"),
                new Parameters(new ArrayList<>(), new CoverageCount(0,0), new CoverageCount(0,0), null,  "C"),
                new Parameters(new ArrayList<>(), new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.FULL, null),
                new Parameters(new ArrayList<>(), new CoverageCount(0,0), new CoverageCount(0,0), StateOfCoverage.FULL, "")};

        int i = 0;
        for (Parameters p : nullPointerException) {
            assertThrows(Exception.class, () -> {
                new CoverageClass(p.byIndex, p.branch, p.line, p.stateClass, p.className);
            }, "at index " + i++);
        }
    }

    @Test
    public void equalsTest() {
        CoverageClass a = new CoverageClass(
                new ArrayList<>(List.of(
                        StateOfCoverage.FULL,
                        StateOfCoverage.NOT)),
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class");

        CoverageClass b = new CoverageClass(
                new ArrayList<>(List.of(
                        StateOfCoverage.FULL,
                        StateOfCoverage.NOT)),
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class");

        CoverageClass c = new CoverageClass(
                new ArrayList<>(List.of(
                        StateOfCoverage.FULL,
                        StateOfCoverage.NOT)),
                new CoverageCount(1,0),
                new CoverageCount(2,1),
                StateOfCoverage.FULL,
                "Class");

        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);

        Parameters[] notEquals = new Parameters[] {
                new Parameters( new ArrayList<>(List.of(StateOfCoverage.FULL, StateOfCoverage.NOT, StateOfCoverage.PARTLY)), new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.FULL, "Class"),
                new Parameters( new ArrayList<>(List.of(StateOfCoverage.FULL, StateOfCoverage.NOT)), new CoverageCount(4,0), new CoverageCount(2,1), StateOfCoverage.FULL, "Class"),
                new Parameters( new ArrayList<>(List.of(StateOfCoverage.FULL, StateOfCoverage.NOT)), new CoverageCount(1,0), new CoverageCount(4,1), StateOfCoverage.FULL, "Class"),
                new Parameters( new ArrayList<>(List.of(StateOfCoverage.FULL, StateOfCoverage.NOT)), new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.NOT, "Class"),
                new Parameters( new ArrayList<>(List.of(StateOfCoverage.FULL, StateOfCoverage.NOT)), new CoverageCount(1,0), new CoverageCount(2,1), StateOfCoverage.FULL,  "C")};

        int i = 0;
        for (Parameters p : notEquals) {
            assertNotEquals(a, new CoverageClass(p.byIndex, p.branch, p.line, p.stateClass, p.className), "at index " + i++);
        }
    }

}