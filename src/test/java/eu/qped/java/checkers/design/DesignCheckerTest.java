package eu.qped.java.checkers.design;

import eu.qped.framework.qf.QfObject;
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


    private String pathToClass;
    private String answer;
    private DesignChecker b;

    private static String DIRECTORY = ""; // root dir
    private static String SEP = "/"; // root dir

    @BeforeEach
    void setUp() {
        createTestClass();

        pathToClass = DIRECTORY + "DCTest.class";
        answer = "    import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "    public class DCTest{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }";

        b = DesignChecker.builder().answer(new QfObject().getAnswer()).build();
        b.setTargetProject(pathToClass);
        b.setAnswer(answer);
    }

    @Test
    void getAndSetAnswer() {
        assertEquals("    import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "    public class DCTest{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }", b.getAnswer());
        b.setAnswer("class answer {\nString s = \"this is a random answer\"}");
        assertEquals("class answer {\nString s = \"this is a random answer\"}", b.getAnswer());    }

    @Test
    void getAndSetTargetProject() {
        assertEquals(DIRECTORY + "DCTest.class", b.getTargetProject());
        b.setTargetProject("/this/is/a/random/path.jk");
        assertEquals("/this/is/a/random/path.jk", b.getTargetProject());
    }

    @Test
    void check() {
        assertEquals(1, b.check().getMetricsMap().size());
        assertEquals(Metric.values().length - 2, b.check().getMetricsMap().get(b.check().getMetricsMap().keySet().iterator().next()).size());

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
        c.compile(stringAnswer);
    }
}