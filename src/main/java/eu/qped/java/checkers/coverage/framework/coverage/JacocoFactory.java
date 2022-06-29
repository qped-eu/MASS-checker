package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactory;

class JacocoFactory implements CoverageFrameworkFactory {

    JacocoFactory() {}

    @Override
    public CoverageFramework create(TestFrameworkFactory factory) {
        return new Jacoco(factory);
    }

}
