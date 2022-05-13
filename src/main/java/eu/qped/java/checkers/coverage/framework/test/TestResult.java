package eu.qped.java.checkers.coverage.framework.test;

import java.util.Objects;


/**
 * Collects all information related to a failed test.
 * @author Herfurth
 */
public class TestResult {
    private final String className;
    private final String methodName;
    private final String want;
    private final String got;

    TestResult(String className, String methodName, String want, String got) {
        this.className = Objects.requireNonNull(className);
        this.methodName = Objects.requireNonNull(methodName);
        this.want = Objects.requireNonNull(want);
        this.got = Objects.requireNonNull(got);
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public String want() {
        return want;
    }

    public String got() {
        return got;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return className.equals(that.className) && methodName.equals(that.methodName) && want.equals(that.want) && got.equals(that.got);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, want, got);
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", want='" + want + '\'' +
                ", got='" + got + '\'' +
                '}';
    }

}
