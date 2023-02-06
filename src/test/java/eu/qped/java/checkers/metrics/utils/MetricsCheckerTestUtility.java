package eu.qped.java.checkers.metrics.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.mass.QfMetricsSettings;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.analyser.SyntaxAnalysisReport;
import eu.qped.java.checkers.syntax.analyser.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.SupportedLanguages;

/**
 * Utility class for testing.
 * @author Jannik Seus
 */
public class MetricsCheckerTestUtility {

    private static final String TEST_CLASS_NAME = "DCTest";
	private static File solutionRoot;

	public static Field getFieldByName(String name, Field[] fields) {
        for (Field f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

	public static File getSolutionRoot() throws IOException {
		if (solutionRoot == null)
			generateTestClass();
		return solutionRoot;
	}
	
	public static String getTestClassName() {
		return TEST_CLASS_NAME;
	}
	
    public static void generateTestClass() throws IOException {
    	if (solutionRoot != null)
    		return;
        String answer = "    import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "    public class " + TEST_CLASS_NAME + "{\n" +
                "        List<String> xx(){\n" +
                "            List list = new ArrayList();\n" +
                "            list.add(\"8888\");\n" +
                "            return list;\n" +
                "        }\n" +
                "    }";
		SyntaxSetting syntaxSetting = SyntaxSetting.builder().build();
        syntaxSetting.setLanguage(SupportedLanguages.ENGLISH);
        syntaxSetting.setCheckLevel(CheckLevel.BEGINNER);

        solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, answer);
        
        SyntaxErrorAnalyser syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .solutionRoot(solutionRoot)
                    .build();
        SyntaxAnalysisReport analyseReport = syntaxErrorAnalyser.check();

        SyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
        syntaxFeedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
	}

    public static QfMetricsSettings generateSampleQFMetricsSettings() {
        QfMetricsSettings qfMetricsSettings = new QfMetricsSettings();
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

    public static QfMetricsSettings generateQMetricsSettings() {
        QfMetricsSettings qfMetricsSettings = new QfMetricsSettings();
        Random random = new Random();
        double[] doublesMin = random.doubles(18, 0, 0.5).toArray();
        double[] doublesMax = random.doubles(18, 0.5, 1).toArray();
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
