package eu.qped.java.checkers.mass;

import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedback;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;

import java.util.ArrayList;


public class Mass implements Checker {

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
        SyntaxErrorAnalyser syntaxErrorAnalyser = SyntaxErrorAnalyser.builder().build();
        if (file != null) {
            syntaxErrorAnalyser.setTargetProject(file.getUnzipped().getPath());
        } else {
            syntaxErrorAnalyser.setStringAnswer(qfObject.getAnswer());
        }
        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(mass.getStyle()).build();

        SemanticChecker semanticChecker = SemanticChecker.builder().feedbacks(new ArrayList<>()).qfSemSettings(mass.getSemantic()).build();

        MetricsChecker metricsChecker = MetricsChecker.builder().qfMetricsSettings(mass.getMetrics()).build();

        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(this.classSettings);
        ClassChecker classChecker = new ClassChecker(classConfigurator);

        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, semanticChecker, syntaxErrorAnalyser, metricsChecker, classChecker, mainSettings);
        massExecutor.execute();


        /*
         feedbacks
         */
        var styleFeedbacks = massExecutor.getStyleFeedbacks();
        var syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();
        var semanticFeedbacks = massExecutor.getSemanticFeedbacks();
        var metricsFeedbacks = massExecutor.getMetricsFeedbacks();

        int resultLength = 100
                + ((styleFeedbacks != null) ? styleFeedbacks.size() : 0)
                + ((semanticFeedbacks != null) ? semanticFeedbacks.size() : 0)
                + ((metricsFeedbacks != null) ? metricsFeedbacks.size() : 0)
                + ((syntaxFeedbacks != null) ? syntaxFeedbacks.size() : 0);
        String[] result = new String[resultLength];
        int resultIndex = 0;

        for (StyleFeedback styleFeedback : styleFeedbacks) {
            result[resultIndex] = "style Feedback";
            result[resultIndex + 1] =
                    styleFeedback.getFile()
                            + NEW_LINE
                            + styleFeedback.getDesc()
                            + NEW_LINE
                            + styleFeedback.getContent()
                            + NEW_LINE
                            + styleFeedback.getLine()
                            + NEW_LINE
                            + styleFeedback.getExample()
                            + NEW_LINE;
            resultIndex = resultIndex + 2;
        }

        for (SemanticFeedback semanticFeedback : semanticFeedbacks) {
            result[resultIndex] = "semantic Feedback";
            result[resultIndex + 1] = semanticFeedback.getBody() + NEW_LINE
                    + "--------------------------------------------------";
            resultIndex = resultIndex + 2;
        }


        result[resultIndex] = MarkdownFormatterUtility.asHeading2("Metrics Feedback");
        for (MetricsFeedback metricsFeedback : metricsFeedbacks) {
            result[resultIndex + 1] =
                    MarkdownFormatterUtility.asHeading3("In class " + MarkdownFormatterUtility.asMonospace(metricsFeedback.getClassName() + ".java", false, null))
                            + MarkdownFormatterUtility.asBold(metricsFeedback.getMetric() + " (" + metricsFeedback.getBody() + ")")
                            + " measured with value: " + MarkdownFormatterUtility.asMonospace(Double.toString(metricsFeedback.getValue()), false, null)
                            + NEW_LINE
                            + metricsFeedback.getSuggestion()
                            + NEW_LINE
                            + "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -";
            resultIndex = resultIndex + 2;
        }


//        for (ClassFeedback classFeedback : classFeedbacks) {
//            result[resultIndex] = "class Feedback";
//            result[resultIndex + 1] = classFeedback.getBody() + NEW_LINE
//                    + "--------------------------------------------------";
//            resultIndex = resultIndex + 2;
//        }

        for (SyntaxFeedback syntax : syntaxFeedbacks) {
            result[resultIndex + 1] = ""
                    + syntax.toString()
                    + NEW_LINE
                    + "--------------------------------------------------";
            resultIndex = resultIndex + 2;
        }

        qfObject.setFeedback(result);
    }


}