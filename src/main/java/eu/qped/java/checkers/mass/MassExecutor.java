package eu.qped.java.checkers.mass;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.Feedback;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxCheckReport;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.feedback.syntax.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.feedback.syntax.SyntaxFeedback;
import eu.qped.java.feedback.syntax.SyntaxFeedbackGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Executor class, execute all components of the System to analyze the code
 *
 * @author Basel Alaktaa & Mayar Hamdash
 * @version 1.0
 * @since 19.08.2021
 */
@Getter
@Setter
public class MassExecutor {

    private final MainSettings mainSettingsConfigurator;


    private List<StyleFeedback> styleFeedbacks;
    private List<SemanticFeedback> semanticFeedbacks;
    private List<SyntaxFeedback> syntaxFeedbacks;
    private List<ClassFeedback> classFeedbacks;
    private List<MetricsFeedback> metricsFeedbacks;

    private List<SyntaxError> syntaxErrors;

    private final StyleChecker styleChecker;
    private final SemanticChecker semanticChecker;
    private final SyntaxChecker syntaxChecker;
    private final ClassChecker classChecker;
    private MetricsChecker metricsChecker;

    /**
     * To create an Object use the factory Class @MassExecutorFactory
     *
     * @param styleChecker             style checker component
     * @param semanticChecker          semantic checker component
     * @param syntaxChecker            syntax checker component
     * @param metricsChecker           metrics checker component
     * @param mainSettingsConfigurator settings
     */

    public MassExecutor(final StyleChecker styleChecker, final SemanticChecker semanticChecker,
                        final SyntaxChecker syntaxChecker, final MetricsChecker metricsChecker,
                        final ClassChecker classChecker, final MainSettings mainSettingsConfigurator
                        ) {

        this.styleChecker = styleChecker;
        this.semanticChecker = semanticChecker;
        this.syntaxChecker = syntaxChecker;
        this.metricsChecker = metricsChecker;
        this.classChecker = classChecker;
        this.mainSettingsConfigurator = mainSettingsConfigurator;
    }

    /**
     * execute the Mass System
     */
    public void execute() {
        init();

        boolean styleNeeded = mainSettingsConfigurator.isStyleNeeded();
        boolean semanticNeeded = mainSettingsConfigurator.isSemanticNeeded();
        boolean metricsNeeded = mainSettingsConfigurator.isMetricsNeeded();
        boolean classNeeded = mainSettingsConfigurator.isClassNeeded();


        SyntaxCheckReport syntaxCheckReport = syntaxChecker.check();

        if (syntaxCheckReport.isCompilable()) {
            if (styleNeeded) {
                styleChecker.setTargetPath(syntaxCheckReport.getPath());
                styleChecker.check();
                styleFeedbacks = styleChecker.getStyleFeedbacks();
            }
            if (semanticNeeded) {
                semanticChecker.setTargetProjectPath(syntaxCheckReport.getPath());
                semanticChecker.check();
                semanticFeedbacks = semanticChecker.getFeedbacks();
            }
            if (metricsNeeded) {
                metricsChecker.check();
                metricsFeedbacks = metricsChecker.getMetricsFeedbacks();
            }
            if (classNeeded) {
                try {
                    classChecker.check(null);
                    classFeedbacks = classChecker.getClassFeedbacks();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            syntaxChecker.setLevel(mainSettingsConfigurator.getSyntaxLevel());
            syntaxErrors = syntaxCheckReport.getSyntaxErrors();
            AbstractSyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
            syntaxFeedbacks = syntaxFeedbackGenerator.generateFeedbacks(syntaxErrors);
        }

        // translate Feedback body if needed
        if (!mainSettingsConfigurator.getPreferredLanguage().equals("en")) {
            translate(styleNeeded, semanticNeeded, metricsNeeded);
        }
    }

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        semanticFeedbacks = new ArrayList<>();
        metricsFeedbacks = new ArrayList<>();
        syntaxErrors = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded, boolean metricsNeeded) {
        String prefLanguage = mainSettingsConfigurator.getPreferredLanguage();
        Translator translator = new Translator();

        //List is Empty when the syntax is correct
        for (SyntaxFeedback feedback : syntaxFeedbacks) {
            translator.translateBody(prefLanguage, feedback);
        }
        if (semanticNeeded) {
            for (Feedback feedback : semanticFeedbacks) {
                translator.translateBody(prefLanguage, feedback);
            }
        }
        if (styleNeeded) {
            for (StyleFeedback feedback : styleFeedbacks) {
                translator.translateStyleBody(prefLanguage, feedback);
            }
        }
        if (metricsNeeded) {
            for (MetricsFeedback feedback : metricsFeedbacks) {
                translator.translateMetricsBody(prefLanguage, feedback);
            }
        }
    }


    public static void main(String[] args) {
        long start = System.nanoTime();
        String code = "//\n" +
                "// Source code recreated from a .class file by IntelliJ IDEA\n" +
                "// (powered by FernFlower decompiler)\n" +
                "//\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "class GrayCode {\n" +
                "    GrayCode() {\n" +
                "    }\n" +
                "\n" +
                "    public static List<String> grayCodeStrings(int n) {\n" +
                "        List<String> list = new ArrayList();\n" +
                "        if (n == 0) {\n" +
                "            list.add(\"\");\n" +
                "            return list;\n" +
                "        } else if (n == 1) {\n" +
                "            list.add(\"0\");\n" +
                "            list.add(\"1\");\n" +
                "            return list;\n" +
                "        } else {\n" +
                "            List<String> prev = grayCodeStrings(n - 1);\n" +
                "            list.addAll(prev);\n" +
                "\n" +
                "            for(int i = prev.size() - 1; i >= 0; --i) {\n" +
                "                String bits = \"abcccc\";\n" +
                "                list.set(i, \"0\" + bits);\n" +
                "                list.add(\"1\" + bits);\n" +
                "            }\n" +
                "\n" +
                "            return list;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private int anotherMethod(boolean a, boolean b, double c) {\n" +
                "        if (a) {\n" +
                "            if (b) {\n" +
                "                return (int)c;\n" +
                "            } else if (c >= 43) {\n" +
                "                return 0;\n" +
                "            } else {\n" +
                "                if (a && !b && c < 231){\n" +
                "                    if (doSomething()) {\n" +
                "                        return Integer.MAX_VALUE;\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            if ((int)c == 5) {\n" +
                "                return (int)(4.0D * c);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return 0;\n" +
                "    }\n" +
                "\n" +
                "    private boolean doSomething() {\n" +
                "        return false;\n" +
                "    }\n" +
                "}\n";

        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("false");
        qfMainSettings.setMetricsNeeded("true");
        qfMainSettings.setPreferredLanguage("en");


        MainSettings mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QFSemSettings qfSemSettings = new QFSemSettings();
//        qfSemSettings.setFilePath("src/main/resources/exam-results/src");
//        qfSemSettings.setMethodName("grayCodeStrings");
//        qfSemSettings.setRecursionAllowed("true");
//        qfSemSettings.setWhileLoop("-1");
//        qfSemSettings.setForLoop("2");
//        qfSemSettings.setForEachLoop("-1");
//        qfSemSettings.setIfElseStmt("0");
//        qfSemSettings.setDoWhileLoop("-1");
//        qfSemSettings.setReturnType("int");

//        SemanticSettingReader semanticSettingReader = SemanticSettingReader.createSemanticConfigurator(qfSemSettings);


        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel("ADV");
        qfStyleSettings.setComplexityLevel("ADV");
        qfStyleSettings.setBasisLevel("ADVANCED");
        qfStyleSettings.setClassLength("10");
        qfStyleSettings.setMethodLength("10");


        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(qfStyleSettings).build();

        SemanticChecker semanticChecker = SemanticChecker.builder().qfSemSettings(qfSemSettings).build();

        QFMetricsSettings qfMetricsSettings = new QFMetricsSettings();
        qfMetricsSettings.setAmc("0.5", "1.0");
        qfMetricsSettings.setAmcNoMax("false");
        qfMetricsSettings.setCa("0.5", "1.0");
        qfMetricsSettings.setCaNoMax("false");
        qfMetricsSettings.setCam("0.5", "1.0");
        qfMetricsSettings.setCamNoMax("false");
        qfMetricsSettings.setCam("0.5", "1.0");
        qfMetricsSettings.setCamNoMax("false");
        qfMetricsSettings.setCbm("0.5", "1.0");
        qfMetricsSettings.setCbmNoMax("false");
        qfMetricsSettings.setCbo("0.5", "1.0");
        qfMetricsSettings.setCboNoMax("false");
        qfMetricsSettings.setCc("0.5", "3.0");
        qfMetricsSettings.setCcNoMax("false");
        qfMetricsSettings.setCe("0.5", "1.0");
        qfMetricsSettings.setCeNoMax("false");
        qfMetricsSettings.setDam("0.5", "1.0");
        qfMetricsSettings.setDamNoMax("false");
        qfMetricsSettings.setDit("0.5", "1.0");
        qfMetricsSettings.setDitNoMax("false");
        qfMetricsSettings.setIc("0.5", "1.0");
        qfMetricsSettings.setIcNoMax("false");
        qfMetricsSettings.setLcom("0.5", "1.0");
        qfMetricsSettings.setLcomNoMax("false");
        qfMetricsSettings.setLcom3("0.5", "1.0");
        qfMetricsSettings.setLcom3NoMax("false");
        qfMetricsSettings.setLoc("15.0", "60.0");
        qfMetricsSettings.setLocNoMax("false");
        qfMetricsSettings.setMoa("0.5", "1.0");
        qfMetricsSettings.setMoaNoMax("false");
        qfMetricsSettings.setMfa("0.5", "1.0");
        qfMetricsSettings.setMfaNoMax("false");
        qfMetricsSettings.setNoc("0.0", "5.0");
        qfMetricsSettings.setNocNoMax("false");
        qfMetricsSettings.setNpm("0.5", "1.0");
        qfMetricsSettings.setNpmNoMax("false");
        qfMetricsSettings.setRfc("0.5", "1.0");
        qfMetricsSettings.setRfcNoMax("false");
        qfMetricsSettings.setWmc("0.5", "1.0");
        qfMetricsSettings.setWmcNoMax("false");
        qfMetricsSettings.setAmcCustomSuggestionUpper("WHAT ARE YOU DOING?!?!");
        qfMetricsSettings.includeOnlyPublicClasses("false");

        MetricsChecker metricsChecker = MetricsChecker.builder().qfMetricsSettings(qfMetricsSettings).build();


        //SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject("src/main/resources/testProject").build();
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();


        MassExecutor massE = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, metricsChecker, null, mainSettingsConfiguratorConf);

        massE.execute();

        //todo false Alarm: Here was Semicolon expected!

        for (SyntaxFeedback syntaxFeedback : massE.getSyntaxFeedbacks()) {
            System.out.println(syntaxFeedback);
        }

        for (Feedback s : massE.semanticFeedbacks) {
            System.out.println(s.getBody());
        }

        /*
        for Style Errors
         */

        List<StyleFeedback> feedbacks = massE.styleFeedbacks;

        for (StyleFeedback f : feedbacks) {
            System.out.println(f.getDesc());
            System.out.println(f.getContent());
            System.out.println(f.getLine());
            System.out.println(f.getExample());
            System.out.println("-----------------------------------------------------------------");
        }

        List<MetricsFeedback> metricsFeedbacks = massE.metricsFeedbacks;
        for (MetricsFeedback df : metricsFeedbacks) {
            System.out.println(df);
            System.out.println("---------------------");
        }

        /*
        for Syntax Errors
         */
        List<SyntaxFeedback> arrayList = massE.syntaxFeedbacks;
        for (SyntaxFeedback s : arrayList) {
            System.out.println(s.getBody());
            System.out.println(s.getBody());
            System.out.println(s.getSolutionExample());
            System.out.println("--------0T0----------");
        }
        long end = System.nanoTime() - start;
        System.out.println("Feedback generated in: " + end * Math.pow(10.0, -9.0) + " sec");
    }
}