package eu.qped.java.checkers.coverage.framework.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestResultTest {

    @Test
    void constructor() {
        TestResult toTest = new TestResult("A", "B", "C", "D");
        assertEquals("A", toTest.className());
        assertEquals("B", toTest.methodName());
        assertEquals("C", toTest.want());
        assertEquals("D", toTest.got());
    }

    @Test
    void constructorNullPointerException() {
        String[][] tests = new String[][] {
                new String[] {null, "B", "C", "D"},
                new String[] {"A", null, "C", "D"},
                new String[] {"A", "B", null, "D"},
                new String[] {"A", "B", "C", null}};
        for (int i = 0; i < tests.length; i ++) {
            int j = i;
            assertThrowsExactly(NullPointerException.class, () -> { new TestResult(tests[j][0], tests[j][1], tests[j][2], tests[j][3]); });
        }
    }

    @Test
    void testEquals() {
        TestResult a = new TestResult("A", "B", "C", "D");
        TestResult b = new TestResult("A", "B", "C", "D");
        TestResult c = new TestResult("A", "B", "C", "D");
        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(b, c);
        String[][] tests = new String[][] {
                new String[] {"X", "B", "C", "D"},
                new String[] {"A", "X", "C", "D"},
                new String[] {"A", "B", "X", "D"},
                new String[] {"A", "B", "C", "X"}};
        for (int j = 0; j < tests.length; j ++) {
            assertNotEquals(a, new TestResult(tests[j][0], tests[j][1], tests[j][2], tests[j][3]));
        }
    }
}