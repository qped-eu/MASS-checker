package eu.qped.java.checkers.coverage;

import java.util.Objects;

public class CoverageCount {
    private int total;
    private int missing;

    public CoverageCount(int total, int missing) {
        if (total < 0 || missing < 0 || total < missing)
            throw new IllegalStateException("ERROR:CoverageCount.new() Parameters total and missing can't be smaller then zero");

        this.total = total;
        this.missing = total;
    }

    public void add(CoverageCount other) {
        total += other.total;
        missing += other.missing;
    }

    public int total() {
        return total;
    }

    public int missing() {
        return missing;
    }

    public String percent() {
        if (total == 0)
            return "0";
        return String.format("%.2d", missing/total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverageCount that = (CoverageCount) o;
        return total == that.total && missing == that.missing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, missing);
    }

    @Override
    public String toString() {
        return "CoverageCount{" +
                "total=" + total +
                ", missing=" + missing +
                '}';
    }
}
