package eu.qped.java.checkers.mass;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.Feedback;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticConfigurator;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxCheckReport;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.feedback.syntax.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.feedback.syntax.SyntaxFeedback;
import eu.qped.java.feedback.syntax.SyntaxFeedbackGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Executor class, execute all components of the System to analyze the code
 *
 * @author Basel Alaktaa & Mayar Hamdash
 * @version 1.0
 * @since 19.08.2021
 */

public class MassExecutor {

    private final MainSettings mainSettingsConfigurator;


    private List<StyleFeedback> styleFeedbacks;
    private List<SemanticFeedback> semanticFeedbacks;
    private List<SyntaxFeedback> syntaxFeedbacks;


    private List<SyntaxError> syntaxErrors;

    private final StyleChecker styleChecker;
    private final SemanticChecker semanticChecker;
    private final SyntaxChecker syntaxChecker;

    /**
     * To create an Object use the factory Class @MassExecutorFactory
     *
     * @param styleChecker             style checker component
     * @param semanticChecker          semantic checker component
     * @param syntaxChecker            syntax checker component
     * @param mainSettingsConfigurator settings
     */

    public MassExecutor(final StyleChecker styleChecker, final SemanticChecker semanticChecker,
                        final SyntaxChecker syntaxChecker, final MainSettings mainSettingsConfigurator) {

        this.styleChecker = styleChecker;
        this.semanticChecker = semanticChecker;
        this.syntaxChecker = syntaxChecker;
        this.mainSettingsConfigurator = mainSettingsConfigurator;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();

        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("true");
        qfMainSettings.setPreferredLanguage("en");



        MainSettings mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QFSemSettings qfSemSettings = new QFSemSettings();
        qfSemSettings.setMethodName("grayCodeStrings");
        qfSemSettings.setRecursionAllowed("true");
        qfSemSettings.setWhileLoop("-1");
        qfSemSettings.setForLoop("2");
        qfSemSettings.setForEachLoop("-1");
        qfSemSettings.setIfElseStmt("0");
        qfSemSettings.setDoWhileLoop("-1");
        qfSemSettings.setReturnType("int");

        SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(qfSemSettings);

        String code = "import java.util.ArrayList;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class GrayCode {\n" +
                "\n" +
                "    public static List<String> grayCodeStrings(int n) {\n" +
                "        List<String> list = new ArrayList<>();\n" +
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
                "            for (int i = prev.size() - 1; i >= 0; i--) {\n" +
                "                String bits = \"abc\" \n + \"ccc\"; \n" +
                "                list.set(i, \"0\" + bits);\n" +
                "                list.add(\"1\" + bits);\n" +
                "            }\n" +
                "            return list;\n" +
                "        }\n" +
                "    }\n" +
                "}";


        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel("ADV");
        qfStyleSettings.setCompLevel("ADV");
        qfStyleSettings.setMainLevel("ADV");
        qfStyleSettings.setMethodName("[AA]");
        qfStyleSettings.setBasisLevel("ADVANCED");
        qfStyleSettings.setClassLength("10");
        qfStyleSettings.setMethodLength("10");

        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(qfStyleSettings).build();

        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);


        SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject("src/main/resources/testProject").build();


        MassExecutor massE = new MassExecutor(styleChecker, semanticChecker, syntaxChecker, mainSettingsConfiguratorConf);

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

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        semanticFeedbacks = new ArrayList<>();
        syntaxErrors = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded) {
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
    }


    public List<StyleFeedback> getStyleFeedbacks() {
        return styleFeedbacks;
    }

    public List<SemanticFeedback> getSemanticFeedbacks() {
        return semanticFeedbacks;
    }

    public List<SyntaxFeedback> getSyntaxFeedbacks() {
        return syntaxFeedbacks;
    }


    public List<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }

    /**
     * execute the Mass System
     */
    public void execute() {
        init();

        boolean styleNeeded = mainSettingsConfigurator.isStyleNeeded();
        boolean semanticNeeded = mainSettingsConfigurator.isSemanticNeeded();


        SyntaxCheckReport syntaxCheckReport = syntaxChecker.check();

        if (syntaxCheckReport.isCompilable()) {
            if (styleNeeded) {
                styleChecker.setTargetPath(syntaxCheckReport.getPath());
                styleChecker.check();
                styleFeedbacks = styleChecker.getStyleFeedbacks();

            }
            if (semanticNeeded) {
                final String source = syntaxCheckReport.getCodeAsString();
                semanticChecker.setSource(source);
                semanticChecker.check();
                semanticFeedbacks = semanticChecker.getFeedbacks();
            }
        } else {
            syntaxChecker.setLevel(mainSettingsConfigurator.getSyntaxLevel());
            syntaxErrors = syntaxCheckReport.getSyntaxErrors();
            AbstractSyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
            syntaxFeedbacks = syntaxFeedbackGenerator.generateFeedbacks(syntaxErrors);
        }

        // translate Feedback body if needed
        if (!mainSettingsConfigurator.getPreferredLanguage().equals("en")) {
            translate(styleNeeded, semanticNeeded);
        }
    }
}