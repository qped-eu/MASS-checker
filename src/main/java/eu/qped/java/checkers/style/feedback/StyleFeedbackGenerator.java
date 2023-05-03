package eu.qped.java.checkers.style.feedback;


import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.FeedbacksStore;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.qped.framework.feedback.defaultfeedback.StoredFeedbackDirectoryProvider.provideStoredFeedbackDirectory;

@AllArgsConstructor
@Builder
@Data
public class StyleFeedbackGenerator {

    private FeedbacksStore feedbacksStore;
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
        if (feedbacksStore == null) {
            feedbacksStore = new FeedbacksStore(
                    provideStoredFeedbackDirectory(StyleChecker.class)
                    , settings.getLanguage() + FileExtensions.JSON
            );
        }
        if (settings.getTaskSpecificFeedbacks() != null) {
            feedbacksStore.customizeStore(settings.getTaskSpecificFeedbacks());
        }
        List<Violation> violations = report.getFileEntries().stream()
                .flatMap(fileEntry ->
                        fileEntry.getViolations().stream().peek(violation -> violation.setFileName(fileEntry.getFileName()))
                )
                .collect(Collectors.toList());
        for (Violation violation : violations) {
            var feedbackBuilder = Feedback.builder();
            feedbackBuilder.type((!settings.getIsCorrection()) ? Type.IMPROVEMENT : Type.CORRECTION);
            feedbackBuilder.checkerName(StyleChecker.class.getSimpleName());
            File file = new File(violation.getFileName());
            feedbackBuilder.relatedLocation(
                    RelatedLocation.builder()
                            .fileName(file.getName())
                            .startLine(violation.getBeginLine())
                            .endLine(violation.getEndLine())
                            .build()
            );
            var defaultFeedback = feedbacksStore.getRelatedFeedbackByTechnicalCause(violation.getRule());
            if (defaultFeedback != null) {
                feedbackBuilder.updateFeedback(defaultFeedback);
            } else {
                feedbackBuilder.readableCause(((violation.getDescription() != null) ? violation.getDescription() : violation.getRule()));
                feedbackBuilder.technicalCause(violation.getRule());
            }
            result.add(feedbackBuilder.build());
        }
        return result;
    }

    private List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, StyleSettings styleSettings) {
        if (styleSettings.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }


}
