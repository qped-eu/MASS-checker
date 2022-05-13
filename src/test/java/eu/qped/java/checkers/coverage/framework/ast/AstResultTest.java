package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AstResultTest {
    private class Parameters {
        private StatementType type;
        private final String className;
        private final String methodName;
        private final int start;
        private int end;

        public Parameters(StatementType type, String className, String methodName, int start, int end) {
            this.type = type;
            this.className = className;
            this.methodName = methodName;
            this.start = start;
            this.end = end;
        }
    }

    @Test
    public void constructor() {
        AstResult toTest = new AstResult(
                StatementType.METHOD,
                "Class",
                "Method",
                1,
                2
        );
        assertEquals(StatementType.METHOD, toTest.type());
        assertEquals("Class", toTest.className());
        assertEquals("Method", toTest.methodName());
        assertEquals(1, toTest.start());
        assertEquals(2, toTest.end());
    }

    @Test
    public void constructorException() {
        Parameters[] nullPointerException = new Parameters[] {
                new Parameters(null, "C", "M", 1, 2),
                new Parameters(StatementType.METHOD, null, "M", 1, 2),
                new Parameters(StatementType.METHOD, "", "M", 1, 2),
                new Parameters(StatementType.METHOD, "C", null, 1, 2),
                new Parameters(StatementType.METHOD, "C", "", 1, 2),
                new Parameters(StatementType.METHOD, "C", "M", 3, 2),
        };
        for (Parameters p : nullPointerException) {
            assertThrows(Exception.class, () -> {
                new AstResult(p.type, p.className, p.methodName, p.start, p.end);
            });
        }
    }

    @Test
    public void equalsTest() {
        AstResult a = new AstResult(
                StatementType.METHOD,
                "Class",
                "Method",
                1,
                2
        );
        AstResult b = new AstResult(
                StatementType.METHOD,
                "Class",
                "Method",
                1,
                2
        );
        AstResult c = new AstResult(
                StatementType.METHOD,
                "Class",
                "Method",
                1,
                2
        );
        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);
        Parameters[] notEquals = new Parameters[] {
                new Parameters(StatementType.METHOD, "C", "M", 1, 2),
                new Parameters(StatementType.CONSTRUCTOR, "c", "M", 1, 2),
                new Parameters(StatementType.METHOD, "C", "m", 1, 2),
                new Parameters(StatementType.METHOD, "C", "M", 0, 2),
                new Parameters(StatementType.METHOD, "C", "M", 1, 3),
        };
        for (Parameters p : notEquals) {
            assertNotEquals(a, new AstResult(p.type, p.className, p.methodName, p.start, p.end));
        }
    }
}