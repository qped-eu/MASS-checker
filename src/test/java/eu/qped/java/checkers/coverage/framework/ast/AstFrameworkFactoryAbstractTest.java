package eu.qped.java.checkers.coverage.framework.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AstFrameworkFactoryAbstractTest {

    @Test
    public void createTest() {
        for (String framework : AstFrameworkFactoryAbstract.frameworks) {
            assertNotNull(AstFrameworkFactoryAbstract.create(framework));
        }
    }

    @Test
    public void createTestIllegalStateException() {
        for (String framework : new String[] {"", "#"}) {
            assertThrowsExactly(IllegalStateException.class, () -> AstFrameworkFactoryAbstract.create(framework));
        }
    }

}