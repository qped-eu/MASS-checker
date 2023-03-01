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
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    /**
     * Generates naked feedback based on the provided {@link StyleAnalysisReport} and {@link StyleSettings}.
     *
     * @param report Contains the collection of violations that was provided by the {@link StyleChecker}
     * @param settings Contains settings like language to be used which comes from the {@link StyleChecker}
     * @return An {@link List} containing the created {@link Feedback}
     */
    public List<Feedback> generateNakedFeedback(StyleAnalysisReport report, StyleSettings settings) {
        List<Feedback> result = new ArrayList<>();
        if (defaultFeedbacksStore == null) {
            defaultFeedbacksStore = new DefaultFeedbacksStore(
                    provideDefaultFeedbackDirectory(StyleChecker.class)
                    , settings.getLanguage() + FileExtensions.JSON
            );
        }
        /*
        List<Violation> violations = report.getFileEntries().stream()
                .flatMap(fileEntry ->
                        fileEntry.getViolations().stream().peek(violation -> violation.setFileName(fileEntry.getFileName()))
                )
                .collect(Collectors.toList());
        for (Violation violation : violations) {
            var defaultFeedback = defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(violation.getRule());
            if (defaultFeedback != null) {
                Feedback feedback = Feedback.builder().build();
                // TODO: can change
                feedback.setType(Type.IMPROVEMENT);
                feedback.setCheckerName(StyleChecker.class.getSimpleName());
                feedback.updateFeedback(defaultFeedback);
                File file = new File(violation.getFileName());
                feedback.setRelatedLocation(
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
                result.add(feedback);

            }
        }
        return result;
    }

         */
        // store violations in map to optimize iteration
        Map<String, List<Violation>> violationsMap = report.getFileEntries().stream()
                .map(fileEntry -> fileEntry.getViolations().stream().map(violation -> {
                    violation.setFileName(fileEntry.getFileName());
                    return violation;
                }).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Violation::getRule));
        for (List<Violation> violations : violationsMap.values()) {
            var defaultFeedback = defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(violations.get(0).getRule());
            if (defaultFeedback != null) {
                for (Violation violation : violations) {
                    Feedback feedback = Feedback.builder().build();
                    // Default feedback type to IMPROVEMENT, but can be changed by the user later
                    feedback.setType(Type.IMPROVEMENT);
                    feedback.setCheckerName(StyleChecker.class.getSimpleName());
                    feedback.updateFeedback(defaultFeedback);
                    File file = new File(violation.getFileName());
                    feedback.setRelatedLocation(
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
                    result.add(feedback);
                }
            }
        }
        return result;
    }

    private List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, StyleSettings styleSettings) {
        return feedbacks;
    }


}
