package eu.qped.java.checkers.coverage.framework.ast;

import eu.qped.java.checkers.coverage.enums.StatementType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AstMethodTest {

    private class Parameters {
        private StatementType type;
        private String content;

        public Parameters(StatementType type, String content) {
            this.type = type;
            this.content = content;
        }
    }

    private AstMethod create(Parameters p) {
        return new AstMethod(p.type, "Class", "Method", 1, 2, p.content);
    }

    @Test
    public void constructor() {
        AstMethod toTest = create(new Parameters(StatementType.METHOD, "MSG"));
        assertEquals(StatementType.METHOD, toTest.type());
        assertEquals("Class", toTest.className());
        assertEquals("Method", toTest.methodName());
        assertEquals(1, toTest.start());
        assertEquals(2, toTest.end());
        assertEquals("MSG", toTest.content());
    }

    @Test
    public void constructorException() {
        Parameters[] nullPointerException = new Parameters[] {
                new Parameters(StatementType.IF, "MSG"),
                new Parameters(StatementType.ELSE_IF, "MSG"),
                new Parameters(StatementType.ELSE, "MSG"),
                new Parameters(StatementType.CASE, "MSG"),
                new Parameters(StatementType.WHILE, "MSG"),
                new Parameters(StatementType.FOR, "MSG"),
                new Parameters(StatementType.FOREACH, "MSG"),
                new Parameters(StatementType.METHOD, null),
        };
        for (Parameters p : nullPointerException) {
            assertThrows(Exception.class, () -> {
                create(p);
            });
        }
    }

    @Test
    public void equalsTest() {
        Parameters d = new Parameters(StatementType.METHOD, "MSG");
        AstMethod a = create(d);
        AstMethod b = create(d);
        AstMethod c = create(d);
        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);
        assertNotEquals(a, create(new Parameters(StatementType.METHOD, "")));
    }


}