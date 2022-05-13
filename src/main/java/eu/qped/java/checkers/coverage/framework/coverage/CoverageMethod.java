package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import java.util.*;


public class CoverageMethod implements Coverage {
    private final CoverageCount branch;
    private final CoverageCount line;
    private final StateOfCoverage state;
    private final String className;
    private final String methodName;

    public CoverageMethod(
            CoverageCount branch,
            CoverageCount line,
            StateOfCoverage state,
            String className,
            String methodName) {

        this.branch = Objects.requireNonNull(branch, "ERROR::Coverage.new() parameter branch can't be null.");
        this.line = Objects.requireNonNull(line, "ERROR::Coverage.new() parameter line can't be null.");
        this.state = Objects.requireNonNull(state, "ERROR::Coverage.new() parameter stateMethod can't be null.");
        if (! (Objects.nonNull(className) && ! className.isBlank()))
            throw new IllegalArgumentException("ERROR::Coverage.new() parameter className can't be null or blank.");
        this.className = className;
        if (! (Objects.nonNull(methodName) && ! methodName.isBlank()))
            throw new IllegalArgumentException("ERROR::Coverage.new() parameter methodName can't be null or blank");
        this.methodName = methodName;
    }

    public CoverageCount branch() {
        return branch;
    }

    public CoverageCount line() {
        return line;
    }

    public StateOfCoverage state() {
        return state;
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverageMethod coverage = (CoverageMethod) o;
        return branch.equals(coverage.branch) && line.equals(coverage.line) && state == coverage.state && className.equals(coverage.className) && methodName.equals(coverage.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branch, line, state, className, methodName);
    }

    @Override
    public String toString() {
        return "Coverage{" +
                ", branch=" + branch +
                ", line=" + line +
                ", stateMethod=" + state +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }

}
