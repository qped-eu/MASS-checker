package eu.qped.java.checkers.metrics.utils;

import eu.qped.java.checkers.mass.QFMetricsSettings;
import eu.qped.java.utils.compiler.Compiler;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Utility class for testing.
 * @author Jannik Seus
 */
public class MetricsCheckerTestUtility {

    public static Field getFieldByName(String name, Field[] fields) {
        for (Field f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    public static void generateTestClass() {
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

    public static QFMetricsSettings generateSampleQFMetricsSettings() {
        QFMetricsSettings qfMetricsSettings = new QFMetricsSettings();
        qfMetricsSettings.setAmcSuggestionMax("AMC CUSTOM SUGGESTION UPPER BOUND");
        qfMetricsSettings.setCcSuggestionMax("CC CUSTOM SUGGESTION UPPER BOUND");
        qfMetricsSettings.setLcom3SuggestionMin("LCOM3 CUSTOM SUGGESTION LOWER BOUND");
        qfMetricsSettings.setWmcSuggestionMin("WMC CUSTOM SUGGESTION LOWER BOUND");
        qfMetricsSettings.includeCallsToJdk("false");
        qfMetricsSettings.includeOnlyPublicClasses("true");
        qfMetricsSettings.setAmc("0.5", "1.0");
        qfMetricsSettings.setCa("0.5", "1.0");
        qfMetricsSettings.setCam("0.5", "1.0");
        qfMetricsSettings.setCbm("0.5", "1.0");
        qfMetricsSettings.setCbo("0.5", "1.0");
        qfMetricsSettings.setCc("1.5", "1.0");
        qfMetricsSettings.setCe("0.5", "1.0");
        qfMetricsSettings.setDam("0.5", "1.0");
        qfMetricsSettings.setDit("0.5", "1.0");
        qfMetricsSettings.setIc("0.5", "1.0");
        qfMetricsSettings.setLcom("0.5", "1.0");
        qfMetricsSettings.setLcom3("0.5", "1.0");
        qfMetricsSettings.setLoc("0.5", "1.0");
        qfMetricsSettings.setMoa("0.5", "1.0");
        qfMetricsSettings.setMfa("0.5", "1.0");
        qfMetricsSettings.setNoc("0.5", "1.0");
        qfMetricsSettings.setNpm("0.5", "1.0");
        qfMetricsSettings.setRfc("0.5", "1.0");
        qfMetricsSettings.setWmc("0.5", "1.0");
        return qfMetricsSettings;
    }

    public static QFMetricsSettings generateQMetricsSettings() {
        QFMetricsSettings qfMetricsSettings = new QFMetricsSettings();
        Random random = new Random();
        double[] doublesMin = random.doubles(18, 0,0.5).toArray();
        double[] doublesMax = random.doubles(18, 0.5,1).toArray();
        for (int i = 0; i < doublesMin.length; i++) {


            qfMetricsSettings.setAmcSuggestionMax("AMC CUSTOM SUGGESTION UPPER BOUND");
            qfMetricsSettings.setCcSuggestionMax("CC CUSTOM SUGGESTION UPPER BOUND");
            qfMetricsSettings.setLcom3SuggestionMin("LCOM3 CUSTOM SUGGESTION LOWER BOUND");
            qfMetricsSettings.setWmcSuggestionMin("WMC CUSTOM SUGGESTION LOWER BOUND");
            qfMetricsSettings.includeCallsToJdk("false");
            qfMetricsSettings.includeOnlyPublicClasses("true");
            qfMetricsSettings.setAmc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCam(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCbm(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCbo(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setCe(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setDam(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setDit(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setIc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setLcom(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setLcom3(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setLoc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setMoa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setMfa(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setNoc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setNpm(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setRfc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
            qfMetricsSettings.setWmc(String.valueOf(doublesMin[i]), String.valueOf(doublesMax[i]));
        }
        return qfMetricsSettings;
    }
}
