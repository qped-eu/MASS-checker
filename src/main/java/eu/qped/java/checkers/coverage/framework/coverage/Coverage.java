package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;

public interface Coverage {

    String className();
    CoverageCount branch();
    CoverageCount line();
    StateOfCoverage state();

}
