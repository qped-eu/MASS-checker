package eu.qped.java.checkers.mass;

import eu.qped.framework.Translator;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
import lombok.AccessLevel;
import lombok.Builder;
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
@Builder
public class MassExecutor {

    private final MainSettings mainSettings;

    private List<String> syntaxFeedbacks;
    private List<String> styleFeedbacks;
    private List<String> solutionApproachFeedbacks;
    private List<ClassFeedback> classFeedbacks;
    private List<MetricsFeedback> metricsFeedbacks;
    private List<String> coverageFeedbacks;


    private final StyleChecker styleChecker;
    private final SolutionApproachChecker solutionApproachChecker;
    private final SyntaxChecker syntaxChecker;
    private final ClassChecker classChecker;
    private final MetricsChecker metricsChecker;
    private final CoverageChecker coverageChecker;


    /**
     * execute the Mass System
     */
    public void execute() {
        init();

        boolean styleNeeded = mainSettings.isStyleNeeded();
        boolean semanticNeeded = mainSettings.isSemanticNeeded();
        boolean metricsNeeded = mainSettings.isMetricsNeeded();
        boolean classNeeded = mainSettings.isClassNeeded();
        boolean coverageNeeded = mainSettings.isCoverageNeeded();


        syntaxFeedbacks = syntaxChecker.check();
        var syntaxAnalyseReport = syntaxChecker.getAnalyseReport();
        boolean isCompilable = syntaxAnalyseReport.isCompilable();
        if (isCompilable) {
            if (styleNeeded) {
                styleChecker.setTargetPath(syntaxAnalyseReport.getPath().toString());
                styleFeedbacks = styleChecker.check();
            }
            if (semanticNeeded) {
                solutionApproachChecker.setTargetProjectPath(syntaxAnalyseReport.getPath().toString());
                solutionApproachFeedbacks = solutionApproachChecker.check();
            }
            if (metricsNeeded) {
//                syntaxChecker.setClassFilesDestination("");
                metricsChecker.check();
                metricsFeedbacks = metricsChecker.getMetricsFeedbacks();
            }
            if (classNeeded) {
                try {
//                    classChecker.setTargetPath(syntaxAnalyseReport.getPath());
                    classChecker.check(null);
                    classFeedbacks = classChecker.getClassFeedbacks();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (coverageNeeded) {
                // Found no other solution:
                // The problem is if the student answer needs a klass from a teacher to compile
                // the syntaxChecker always fails.
                coverageFeedbacks = coverageChecker.check();
            }
        }

        // translate Feedback body if needed
        if (!mainSettings.getPreferredLanguage().equals(SupportedLanguages.ENGLISH)) {
            translate(styleNeeded, semanticNeeded, metricsNeeded);
        }
    }

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        solutionApproachFeedbacks = new ArrayList<>();
        metricsFeedbacks = new ArrayList<>();
        classFeedbacks = new ArrayList<>();
        coverageFeedbacks = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded, boolean metricsNeeded) {
        String prefLanguage = mainSettings.getPreferredLanguage();
        Translator translator = new Translator();
        if (metricsNeeded) {
            for (MetricsFeedback feedback : metricsFeedbacks) {
                translator.translateMetricsBody(prefLanguage, feedback);
            }
        }
//        if (classNeeded) {
//            for (Feedback feedback : classFeedbacks) {
//                translator.translateBody(prefLanguage, feedback);
//            }
//        }
    }
}