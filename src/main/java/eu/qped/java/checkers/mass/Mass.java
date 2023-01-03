package eu.qped.java.checkers.mass;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.framework.QfProperty;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.template.TemplateBuilder;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.ClassConfigurator;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.coverage.CoverageSetup;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.configs.SolutionApproachGeneralSettings;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
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
        
        File solutionRoot;
        if (file != null) {
        	solutionRoot = QpedQfFilesUtility.downloadAndUnzipIfNecessary(file);
            var allJavaFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "java");
            if (allJavaFiles.isEmpty()) {
                throw new IllegalArgumentException("Uploaded solution does not contain files with extension java (either as single file or containted in zip archive.");
            }
        } else {
        	solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        	QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, qfObject.getAnswer());
        }

        
        // Syntax Checker
        SyntaxChecker syntaxChecker;
        syntaxChecker = SyntaxChecker.builder().targetProject(solutionRoot).build();
        
        
        if (syntaxChecker == null) return;

        // Style Checker
        StyleChecker styleChecker = StyleChecker.builder().qfStyleSettings(mass.getStyle()).build();

        // Solution Approach Checker
        var solutionApproachGeneralSettings = SolutionApproachGeneralSettings.builder()
                .language(qfObject.getUser().getLanguage())
                .checkLevel(CheckLevel.BEGINNER)
                .build()
        ;
        SolutionApproachChecker solutionApproachChecker = SolutionApproachChecker.builder()
                .qfSemanticSettings(mass.getSemantic())
                .solutionApproachGeneralSettings(solutionApproachGeneralSettings)
                .build()
        ;

        // Metrics Checker
        MetricsChecker metricsChecker = MetricsChecker.builder().qfMetricsSettings(mass.getMetrics()).solutionRoot(solutionRoot).build();

        // Class Checker
        ClassConfigurator classConfigurator = ClassConfigurator.createClassConfigurator(mass.getClasses());
        ClassChecker classChecker = new ClassChecker(classConfigurator);

        // Coverage Checker
        CoverageChecker coverageChecker = null;
        if (mainSettings.isCoverageNeeded()) {
            QfCoverageSettings covSetting = mass.getCoverage();

            CoverageSetup coverageSetup = new CoverageSetup(file, covSetting.getPrivateImplementation(), qfObject.getAnswer(), mainSettings.getPreferredLanguage());

            coverageChecker = new CoverageChecker(covSetting, coverageSetup);
        }

        //Mass
        MassExecutor massExecutor = new MassExecutor(styleChecker, solutionApproachChecker, syntaxChecker, metricsChecker, classChecker, coverageChecker, mainSettings);
        massExecutor.execute();

        /*
         feedbacks
         */
        var syntaxFeedbacks = massExecutor.getSyntaxFeedbacks();
        var styleFeedbacks = massExecutor.getStyleFeedbacks();
        var solutionApproachFeedbacks = massExecutor.getSolutionApproachFeedbacks();
        var metricsFeedbacks = massExecutor.getMetricsFeedbacks();
        var coverageFeedacks = massExecutor.getCoverageFeedbacks();

        var resultArray = mergeFeedbacks(
                syntaxFeedbacks,
                styleFeedbacks,
                solutionApproachFeedbacks,
                metricsFeedbacks,
                coverageFeedacks,
                qfObject
        );

        qfObject.setFeedback(resultArray);
    }

    private String[] mergeFeedbacks(
            @NonNull List<String> syntaxFeedbacks,
            @NonNull List<StyleFeedback> styleFeedbacks,
            @NonNull List<String> semanticFeedbacks,
            @NonNull List<MetricsFeedback> metricsFeedbacks,
            @NonNull List<String> coverageFeedbacks,
            @NonNull QfObject qfObject
    ) {

        var resultSize =
                !syntaxFeedbacks.isEmpty() ? syntaxFeedbacks.size() + 1 :
                        styleFeedbacks.size() + semanticFeedbacks.size() + metricsFeedbacks.size() + 3;

        String[] resultArray = new String[resultSize];
        List<String> resultArrayAsList = new ArrayList<>();
        resultArrayAsList.add("# Your Feedback\n");
        if (!syntaxFeedbacks.isEmpty()) {
            resultArrayAsList.addAll(syntaxFeedbacks);
        } else {
            if (!styleFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Style feedbacks\n");
            }
            styleFeedbacks.forEach(
                    styleFeedback -> {
                        String tempFeedbackAsString =
                                "in: " + styleFeedback.getFile() + "." + " Line: " + styleFeedback.getLine()
                                        + NEW_LINE
//                                        + styleFeedback.getDesc()
//                                        + NEW_LINE
                                        + styleFeedback.getContent()
                                        + NEW_LINE
//                                        + styleFeedback.getLine()
//                                        + NEW_LINE
                                        + styleFeedback.getExample()
                                        + NEW_LINE
                                        + SEPARATOR;
                        resultArrayAsList.add(tempFeedbackAsString);
                    }
            );
            if (!semanticFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Semantic feedbacks");
            }
            resultArrayAsList.addAll(semanticFeedbacks);
            if (!metricsFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Metric feedbacks");
            }
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
            if (!coverageFeedbacks.isEmpty()) {
                resultArrayAsList.add("## Coverage feedbacks");
            }
            resultArrayAsList.addAll(coverageFeedbacks);

        }
        if (resultArrayAsList.size() <= 1) {
            resultArrayAsList.add("Our checks could not find any improvements for your code. This does not mean that it is semantically correct but it adheres to the standards of the lecture in regards to syntax and style.");
        }
        resultArray = resultArrayAsList.toArray(resultArray);
        return resultArray;
    }

}