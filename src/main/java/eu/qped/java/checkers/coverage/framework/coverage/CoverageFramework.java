package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactory;

import java.util.List;
import java.util.Objects;

public abstract class CoverageFramework {
    protected final TestFrameworkFactory factory;

    CoverageFramework(TestFrameworkFactory factory) {
        this.factory = Objects.requireNonNull(factory, "ERROR::CoverageFramework.new() Parameter factory can't be null");
    }

    public abstract CoverageCollection analyze(
            CoverageCollection collection,
            List<CoverageFacade> testClasses,
            List<CoverageFacade> classes
            ) throws Exception;

}
