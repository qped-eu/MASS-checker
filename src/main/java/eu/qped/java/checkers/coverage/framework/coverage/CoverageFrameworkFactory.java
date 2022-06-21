package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactory;

public interface CoverageFrameworkFactory {

    CoverageFramework create(TestFrameworkFactory factory);

}
