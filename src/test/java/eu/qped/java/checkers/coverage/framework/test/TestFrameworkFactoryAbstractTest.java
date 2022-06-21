package eu.qped.java.checkers.coverage.framework.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFrameworkFactoryAbstractTest {

    @Test
    public void createTest() {
        for (String framework : TestFrameworkFactoryAbstract.frameworks) {
            assertNotNull(TestFrameworkFactoryAbstract.create(framework));
        }
    }

    @Test
    public void createTestIllegalStateException() {
        for (String framework : new String[] {"", "#"}) {
            assertThrowsExactly(IllegalStateException.class, () -> TestFrameworkFactoryAbstract.create(framework));
        }
    }

}