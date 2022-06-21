package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import java.util.*;


public class CoverageClass implements Coverage {
    private final ArrayList<StateOfCoverage> byIndex;
    private final CoverageCount branch;
    private final CoverageCount line;
    private final StateOfCoverage state;
    private final String className;

    public CoverageClass(
            ArrayList<StateOfCoverage> byIndex,
            CoverageCount branch,
            CoverageCount line,
            StateOfCoverage state,
            String className) {

        this.byIndex = Objects.requireNonNull(byIndex, "ERROR::Coverage.new() parameter byIndex can't be null.");
        this.branch = Objects.requireNonNull(branch, "ERROR::Coverage.new() parameter branch can't be null.");
        this.line = Objects.requireNonNull(line, "ERROR::Coverage.new() parameter line can't be null.");
        this.state = Objects.requireNonNull(state, "ERROR::Coverage.new() parameter state can't be null.");
        if (!(Objects.nonNull(className) && !className.isBlank()))
            throw new IllegalArgumentException("ERROR::Coverage.new() parameter className can't be null or blank.");
        this.className = className;
    }

    public StateOfCoverage byIndex(int index) {
        try {
            return byIndex.get(index);
        } catch (IndexOutOfBoundsException i) {
            return StateOfCoverage.EMPTY;
        }
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

    public StateOfCoverage stateOfClass() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverageClass that = (CoverageClass) o;
        return Objects.deepEquals(byIndex,that.byIndex) && branch.equals(that.branch) && line.equals(that.line) && state == that.state && className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byIndex, branch, line, state, className);
    }

    @Override
    public String toString() {
        return "CoverageClass{" +
                "byIndex=" + byIndex +
                ", branch=" + branch +
                ", line=" + line +
                ", state=" + state +
                ", className='" + className + '\'' +
                '}';
    }

}
