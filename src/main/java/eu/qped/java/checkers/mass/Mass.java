package eu.qped.java.checkers.mass;

import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.coverage.CoverageBlockChecker;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.coverage.CoverageMapChecker;
import eu.qped.java.checkers.coverage.QfCovSetting;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.solutionapproach.analyser.SolutionApproachAnalyser;
import eu.qped.java.checkers.solutionapproach.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.framework.feedback.template.TemplateBuilder;
import eu.qped.java.utils.MassFilesUtility;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


public class Mass implements Checker {

    public static final String SEPARATOR = "--------------------------------------------------";
    @QfProperty
    private FileInfo file;

    @QfProperty
    private QfMass mass;

    @QfProperty
    private QfClassSettings classSettings;

    private final static String NEW_LINE = "\n" + "\n";

    @Override
    public void check(QfObject qfObject) throws Exception {

        MainSettings mainSettings = new MainSettings(mass, qfObject.getUser().getLanguage());

        // Syntax Checker
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        if (file != null) {
            MassFilesUtility filesUtility = MassFilesUtility.builder()
                    .dirPath(file.getUnzipped().getPath()).build();
            var allJavaFiles = filesUtility.filesWithExtension("java");
            if (allJavaFiles.isEmpty()) {
                qfObject.setFeedback(new String[]{"No java files are detected in your solution"});
                return;
            }
            syntaxChecker.setTargetProject(file.getUnzipped().getPath());
        } else {
            syntaxChecker.setStringAnswer(qfObject.getAnswer());
        }
        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(mass.getStyle()).build();

        SolutionApproachAnalyser solutionApproachAnalyser = SolutionApproachAnalyser.builder().qfSemanticSettings(mass.getSemantic()).build();

        MetricsChecker metricsChecker = MetricsChecker.builder().qfMetricsSettings(mass.getMetrics()).build();

        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(mass.getClasses());
        ClassChecker classChecker = new ClassChecker(classConfigurator);

        //CoverageChecker
        CoverageChecker coverageChecker = null;
        if (mainSettings.isCoverageNeeded()) {
            QfCovSetting covSetting = mass.getCoverage();
            covSetting.setAnswer(qfObject.getAnswer());
            covSetting.setLanguage(mainSettings.getPreferredLanguage());
            covSetting.setFile(file);

            if (covSetting.isUseBlock()) {
                coverageChecker = new CoverageBlockChecker(covSetting);
            } else {
                coverageChecker = new CoverageMapChecker(covSetting);
            }
        }

        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, solutionApproachAnalyser, syntaxChecker, metricsChecker, classChecker, coverageChecker, mainSettings);
        massExecutor.execute();

        /*
         feedbacks
         */
        var syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();
        var styleFeedbacks = massExecutor.getStyleFeedbacks();
        var semanticFeedbacks = massExecutor.getSemanticFeedbacks();
        var metricsFeedbacks = massExecutor.getMetricsFeedbacks();

        var resultArray = mergeFeedbacks(
                syntaxFeedbacks,
                styleFeedbacks,
                semanticFeedbacks,
                metricsFeedbacks,
                qfObject
        );

        qfObject.setFeedback(resultArray);

//        var result = mergeFeedbacks(styleFeedbacks, semanticFeedbacks, semanticFeedbacks, metricsFeedbacks);

//        int resultLength = 100
//                + ((styleFeedbacks != null) ? styleFeedbacks.size() : 0)
//                + ((semanticFeedbacks != null) ? semanticFeedbacks.size() : 0)
//                + ((metricsFeedbacks != null) ? metricsFeedbacks.size() : 0)
//                + ((syntaxFeedbacks != null) ? syntaxFeedbacks.size() : 0);
//        String[] result = new String[resultLength];
//        int resultIndex = 0;
//
//        for (StyleFeedback styleFeedback : styleFeedbacks) {
//            result[resultIndex] = "style Feedback";
//            result[resultIndex + 1] =
//                    styleFeedback.getFile()
//                            + NEW_LINE
//                            + styleFeedback.getDesc()
//                            + NEW_LINE
//                            + styleFeedback.getContent()
//                            + NEW_LINE
//                            + styleFeedback.getLine()
//                            + NEW_LINE
//                            + styleFeedback.getExample()
//                            + NEW_LINE;
//            resultIndex = resultIndex + 2;
//        }
//
//        for (SemanticFeedback semanticFeedback : semanticFeedbacks) {
//            result[resultIndex] = "semantic Feedback";
//            result[resultIndex + 1] = semanticFeedback.getBody() + NEW_LINE
//                    + SEPARATOR;
//            resultIndex = resultIndex + 2;
//        }
//
//
//        result[resultIndex] = MarkdownFormatterUtility.asHeading2("Metrics Feedback");
//        for (MetricsFeedback metricsFeedback : metricsFeedbacks) {
//            result[resultIndex + 1] =
//                    MarkdownFormatterUtility.asHeading3("In class " + MarkdownFormatterUtility.asMonospace(metricsFeedback.getClassName() + ".java", false, null))
//                            + MarkdownFormatterUtility.asBold(metricsFeedback.getMetric() + " (" + metricsFeedback.getBody() + ")")
//                            + " measured with value: " + MarkdownFormatterUtility.asMonospace(Double.toString(metricsFeedback.getValue()), false, null)
//                            + NEW_LINE
//                            + metricsFeedback.getSuggestion()
//                            + NEW_LINE
//                            + SEPARATOR;
//            resultIndex = resultIndex + 2;
//        }
//
//        for (DefaultFeedback syntax : syntaxFeedbacks) {
//            result[resultIndex + 1] = ""
//                    + syntax.toString()
//                    + NEW_LINE
//                    + SEPARATOR;
//            resultIndex = resultIndex + 2;
//        }


    }

    private String[] mergeFeedbacks(
            @NonNull List<Feedback> syntaxFeedbacks,
            @NonNull List<StyleFeedback> styleFeedbacks,
            @NonNull List<SemanticFeedback> semanticFeedbacks,
            @NonNull List<MetricsFeedback> metricsFeedbacks,
            @NonNull QfObject qfObject
    ) {

        var resultSize =
                !syntaxFeedbacks.isEmpty() ? syntaxFeedbacks.size() + 1 :
                        styleFeedbacks.size() + semanticFeedbacks.size() + metricsFeedbacks.size() + 3;

        String[] resultArray = new String[resultSize];
        List<String> resultArrayAsList = new ArrayList<>();
        TemplateBuilder templateBuilder = TemplateBuilder.builder().build();
        if (!syntaxFeedbacks.isEmpty()) {
            resultArrayAsList.addAll(templateBuilder.buildFeedbacksInTemplate(syntaxFeedbacks, qfObject.getUser().getLanguage()));
        } else {
            resultArrayAsList.add("Style feedbacks");
            styleFeedbacks.forEach(
                    styleFeedback -> {
                        String tempFeedbackAsString =
                                styleFeedback.getFile()
                                        + NEW_LINE
                                        + styleFeedback.getDesc()
                                        + NEW_LINE
                                        + styleFeedback.getContent()
                                        + NEW_LINE
                                        + styleFeedback.getLine()
                                        + NEW_LINE
                                        + styleFeedback.getExample()
                                        + SEPARATOR;
                        resultArrayAsList.add(tempFeedbackAsString);
                    }
            );
            resultArrayAsList.add("Semantic feedbacks");
            semanticFeedbacks.forEach(
                    semanticFeedback -> {
                        String tempFeedbackAsString =
                                semanticFeedback.getBody() +
                                        NEW_LINE +
                                        SEPARATOR;
                        resultArrayAsList.add(tempFeedbackAsString);
                    }
            );
            resultArrayAsList.add("Metric feedbacks");
            metricsFeedbacks.forEach(
                    metricsFeedback -> {
                        String tempFeedbackAsString =
                                MarkdownFormatterUtility.asHeading4("In class " + MarkdownFormatterUtility.asCodeLine(metricsFeedback.getClassName() + ".java"))
                                        + MarkdownFormatterUtility.asBold(metricsFeedback.getMetric() + " (" + metricsFeedback.getBody() + ")")
                                        + " measured with value: " + MarkdownFormatterUtility.asCodeLine(Double.toString(metricsFeedback.getValue()))
                                        + NEW_LINE
                                        + metricsFeedback.getSuggestion()
                                        + NEW_LINE
                                        + SEPARATOR;
                        resultArrayAsList.add(tempFeedbackAsString);
                    }
            );
        }
        resultArray = resultArrayAsList.toArray(resultArray);
        return resultArray;
    }

    public int sum() {
        int a = 3;
        return a;
    }


}