package eu.qped.java.checkers.design;

import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.design.ckjm.QPEDMetricsFilter;
import eu.qped.java.checkers.design.ckjm.SaveMapResults;
import eu.qped.java.utils.compiler.Compiler;
import gr.spinellis.ckjm.CkjmOutputHandler;
import gr.spinellis.ckjm.utils.CmdLineParser;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class represents a checker for class design.
 *
 * @author Jannik Seus
 */
@Data
@Builder
public class DesignChecker {


    private String answer;

    private String targetProject;

    /**
     * is able to check one or multiple .class files
     * for defined metrics ({@link Metric}).
     * The output is printed on the console (plain or xml) or saved in {@link DesignCheckReport#getMetricsMap()} ).
     */
    public DesignCheckReport check() {
        DesignCheckReport.DesignCheckReportBuilder resultBuilder = DesignCheckReport.builder();
        Map<String, Map<Metric, Double>> metricsMap = new HashMap<>();

        QPEDMetricsFilter qmf = new QPEDMetricsFilter();
        CmdLineParser cmdParser = new CmdLineParser();

        cmdParser.parse(new String[]{targetProject});

        CkjmOutputHandler handler;
        handler = new SaveMapResults(metricsMap);

        qmf.runMetricsInternal(cmdParser.getClassNames(), handler);

        resultBuilder.metricsMap(((SaveMapResults) handler).getOutputMetrics());
        //resultBuilder.metricsThresholds();
        //resultBuilder.codeAsString();
        //resultBuilder.path();
        return resultBuilder.build();
    }

    // for removal

    public static void main(String[] args) {

        String answer = "import java.util.List;\n" +
                "    import java.util.ArrayList;\n" +
                "    public class Mmm{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }";

        Compiler c = Compiler.builder().build();
        c.compileFromString(answer);

        String pathToClass = "/Users/jannik/ProgrammingProjects/IdeaProjects/uni/ba/fork/QPED-O3/Mmm.class";

        DesignChecker b = DesignChecker.builder().answer(new QfObject().getAnswer()).build();
        b.setTargetProject(pathToClass);
        b.setAnswer(answer);
        b.check().getMetricsMap().forEach((k, v) -> System.out.println(k + " : " + v));
    }

}
