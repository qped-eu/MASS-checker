package eu.qped.java.checkers.style.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbacksStore;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.analyse.reportModel.StyleAnalysisReport;
import eu.qped.java.checkers.style.analyse.reportModel.Violation;
import eu.qped.java.checkers.style.config.StyleSettings;
import eu.qped.java.utils.FileExtensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory;

@AllArgsConstructor
@Builder
@Data
public class StyleFeedbackGenerator {

    private DefaultFeedbacksStore defaultFeedbacksStore;
    private FeedbackManager feedbackManager;

    /**
     * @return map of filenames as keys and a collection of feedbacks for key file.
     */
    public List<String> generateFeedbacks(StyleAnalysisReport report, StyleSettings styleSettings) {
        List<Feedback> nakedFeedbacks = generateNakedFeedback(report, styleSettings);
        var adaptedFeedbacks = adaptFeedbackByCheckerSetting(nakedFeedbacks, styleSettings);
        if (feedbackManager == null) feedbackManager = FeedbackManager.builder().build();
        feedbackManager.setFeedbacks(adaptedFeedbacks);
        return feedbackManager.buildFeedbackInTemplate(styleSettings.getLanguage());
    }

    public List<Feedback> generateNakedFeedback(StyleAnalysisReport report, StyleSettings settings) {
        List<Feedback> result = new ArrayList<>();
        if (defaultFeedbacksStore == null) {
            defaultFeedbacksStore = new DefaultFeedbacksStore(
                    provideDefaultFeedbackDirectory(StyleChecker.class)
                    , settings.getLanguage() + FileExtensions.JSON
            );
        }
        List<Violation> violations = report.getFileEntries().stream()
                .flatMap(fileEntry ->
                        fileEntry.getViolations().stream().peek(violation -> violation.setFileName(fileEntry.getFileName()))
                )
                .collect(Collectors.toList());
        for (Violation violation : violations) {
            var defaultFeedback = defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(violation.getRule());
            // TODO: default Feedback
            if (defaultFeedback != null) {
                var feedbackBuilder = Feedback.builder();
                // TODO: can change
                feedbackBuilder.type(Type.IMPROVEMENT);
                feedbackBuilder.checkerName(StyleChecker.class.getSimpleName());
                feedbackBuilder.updateFeedback(defaultFeedback);
                File file = new File(violation.getFileName());
                feedbackBuilder.relatedLocation(
                        RelatedLocation.builder()
                                .fileName(file.getName())
                                .startLine(
                                        file.getName().contains("TestClass")
                                                ? violation.getBeginLine() - 3
                                                : violation.getBeginLine()
                                )
                                .endLine(
                                        file.getName().contains("TestClass")
                                                ? violation.getEndLine() - 3
                                                : violation.getEndLine()
                                )
                                .build()
                );
                result.add(feedbackBuilder.build());

//                Feedback feedback = Feedback.builder().build();
//                // TODO: can change
//                feedback.setType(Type.IMPROVEMENT);
//                feedback.setCheckerName(StyleChecker.class.getSimpleName());
//                feedback.updateFeedback(defaultFeedback);
//                File file = new File(violation.getFileName());
//                feedback.setRelatedLocation(
//                        RelatedLocation.builder()
//                                .fileName(file.getName())
//                                .startLine(
//                                        file.getName().contains("TestClass")
//                                                ? violation.getBeginLine() - 3
//                                                : violation.getBeginLine()
//                                )
//                                .endLine(
//                                        file.getName().contains("TestClass")
//                                                ? violation.getEndLine() - 3
//                                                : violation.getEndLine()
//                                )
//                                .build()
//                );
//                result.add(feedback);

            }
        }
        return result;
    }

    private List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, StyleSettings styleSettings) {
        return feedbacks;
    }


}
