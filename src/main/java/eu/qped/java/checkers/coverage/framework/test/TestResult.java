package eu.qped.java.checkers.coverage.framework.test;

import java.util.Objects;


/**
 * Collects all information related to a failed test.
 * @author Herfurth
 */
public class TestResult {
    private final String className;
    private final String methodName;
    private final String expected;
    private final String actual;

    TestResult(String className, String methodName) {
        this.className = Objects.requireNonNull(className);
        this.methodName = Objects.requireNonNull(methodName);
        this.expected = null;
        this.actual = null;
    }

    TestResult(String className, String methodName, String expected, String actual) {
        this.className = Objects.requireNonNull(className);
        this.methodName = Objects.requireNonNull(methodName);
        this.expected = Objects.requireNonNull(expected);
        this.actual = Objects.requireNonNull(actual);
    }

    public boolean hasFailedWithoutAssertion() {
        return Objects.isNull(expected) && Objects.isNull(actual);
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }


    @Override
    public String toString() {
        return "TestResult{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", expected='" + expected + '\'' +
                ", actual='" + actual + '\'' +
                '}';
    }

}
