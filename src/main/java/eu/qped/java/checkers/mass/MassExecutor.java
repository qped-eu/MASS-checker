package eu.qped.java.checkers.mass;

import eu.qped.framework.Feedback;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.classdesign.ClassChecker;
import eu.qped.java.checkers.classdesign.feedback.ClassFeedback;
import eu.qped.java.checkers.coverage.CoverageChecker;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.solutionApproach.SolutionApproachAnalyser;
import eu.qped.java.checkers.solutionApproach.SemanticFeedback;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
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

    private final MainSettings mainSettings;


    private List<eu.qped.framework.feedback.Feedback> syntaxFeedbacks;
    private List<StyleFeedback> styleFeedbacks;
    private List<SemanticFeedback> semanticFeedbacks;
    private List<ClassFeedback> classFeedbacks;
    private List<MetricsFeedback> metricsFeedbacks;
    private String[] coverageFeedbacks;


    private final StyleChecker styleChecker;
    private final SolutionApproachAnalyser solutionApproachAnalyser;
    private final SyntaxChecker syntaxChecker;
    private final ClassChecker classChecker;
    private MetricsChecker metricsChecker;
    private final CoverageChecker coverageChecker;

    /**
     * To create an Object use the factory Class @MassExecutorFactory
     *
     * @param styleChecker    style checker component
     * @param solutionApproachAnalyser semantic checker component
     * @param syntaxChecker   syntax checker component
     * @param metricsChecker  metrics checker component
     * @param mainSettings    settings
     */

    public MassExecutor(final StyleChecker styleChecker, final SolutionApproachAnalyser solutionApproachAnalyser,
                        final SyntaxChecker syntaxChecker, final MetricsChecker metricsChecker,
                        final ClassChecker classChecker,
                        final CoverageChecker coverageChecker,
                        final MainSettings mainSettings
    ) {

        this.styleChecker = styleChecker;
        this.solutionApproachAnalyser = solutionApproachAnalyser;
        this.syntaxChecker = syntaxChecker;
        this.metricsChecker = metricsChecker;
        this.classChecker = classChecker;
        this.coverageChecker = coverageChecker;
        this.mainSettings = mainSettings;
        this.coverageFeedbacks = new String[]{};
    }

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
        if (syntaxAnalyseReport.isCompilable()) {
            if (styleNeeded) {
                styleChecker.setTargetPath(syntaxAnalyseReport.getPath());
                styleChecker.check();
                styleFeedbacks = styleChecker.getStyleFeedbacks();
            }
            if (semanticNeeded) {
                solutionApproachAnalyser.setTargetProjectPath(syntaxAnalyseReport.getPath());
                solutionApproachAnalyser.check();
                semanticFeedbacks = solutionApproachAnalyser.getFeedbacks();
            }
            if (metricsNeeded) {
                syntaxChecker.setClassFilesDestination("");
                metricsChecker.check();
                metricsFeedbacks = metricsChecker.getMetricsFeedbacks();
            }
            if (classNeeded) {
                try {
                    classChecker.setTargetPath(syntaxAnalyseReport.getPath());
                    classChecker.check(null);
                    classFeedbacks = classChecker.getClassFeedbacks();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (coverageNeeded)
                coverageFeedbacks = coverageChecker.check();

        } else if (coverageNeeded) {
            // Found no other solution:
            // The problem is if the student answer needs a klass from a teacher to compile
            // the syntaxChecker always fails.
            coverageFeedbacks = coverageChecker.check();
        }

        // translate Feedback body if needed
        if (!mainSettings.getPreferredLanguage().equals(SupportedLanguages.ENGLISH)) {
            translate(styleNeeded, semanticNeeded, metricsNeeded);
        }
    }

    private void init() {
        syntaxFeedbacks = new ArrayList<>();
        styleFeedbacks = new ArrayList<>();
        semanticFeedbacks = new ArrayList<>();
        metricsFeedbacks = new ArrayList<>();
        classFeedbacks = new ArrayList<>();
    }


    private void translate(boolean styleNeeded, boolean semanticNeeded, boolean metricsNeeded) {
        String prefLanguage = mainSettings.getPreferredLanguage();
        Translator translator = new Translator();

        //List is Empty when the syntax is correct
        for (eu.qped.framework.feedback.Feedback feedback : syntaxFeedbacks) {
            translator.translateFeedback(prefLanguage, feedback);
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
//        if (classNeeded) {
//            for (Feedback feedback : classFeedbacks) {
//                translator.translateBody(prefLanguage, feedback);
//            }
//        }
    }
}