package eu.qped.java.checkers.coverage.framework.test;

import java.util.List;

/**
 * TestFramework executes all testClasses and
 * adds the {@link TestResult}s to the {@link TestCollection}.
 * @author Herfurth
 * */
public interface TestFramework {

    List<String> testing(List<String> testClasses, ClassLoader loader);

}
