package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.framework.test.TestCollection;

public interface CoverageCollection extends TestCollection {

    void add(Coverage coverage);

}
