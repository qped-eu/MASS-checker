package eu.qped.java.checkers.design;

import eu.qped.java.utils.compiler.Compiler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DesignChecker}
 *
 * @author Jannik Seus
 */
class DesignCheckerTest {

    private DesignChecker b;

    private static String DIRECTORY = ""; // root dir can
    private static String SEP = "/";

    @BeforeEach
    void setUp() {
        createTestClass();

        b = DesignChecker.builder().build();
    }

    @Test
    void check() {
        assertEquals(1, b.check().getMetricsMap().size());
        //assertEquals(SaveMapResults.Metric.values().length - 2, b.check().getMetricsMap().get(b.check().getMetricsMap().keySet().iterator().next()).size());

    }


    private void createTestClass() {
        Compiler c = Compiler.builder().build();
        String stringAnswer = "    import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "    public class DCTest{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }";
        c.compileFromString(stringAnswer);
    }
}