package eu.qped.java.checkers.coverage.framework.coverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoverageFrameworkFactoryAbstractTest {

    @Test
    public void createTest() {
        for (String framework : CoverageFrameworkFactoryAbstract.framework) {
            assertNotNull(CoverageFrameworkFactoryAbstract.create(framework));
        }
    }

    @Test
    public void createTestIllegalStateException() {
        for (String framework : new String[] {"", "#"}) {
            assertThrowsExactly(IllegalStateException.class, () -> CoverageFrameworkFactoryAbstract.create(framework));
        }
    }

}