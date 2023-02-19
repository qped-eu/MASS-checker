package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.FeedbacksStore;
import eu.qped.framework.feedback.fromatter.KeyWordReplacer;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.configs.SolutionApproachReportItem;
import eu.qped.java.utils.FileExtensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.qped.framework.feedback.defaultfeedback.StoredFeedbackDirectoryProvider.provideStoredFeedbackDirectory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator {

    private FeedbacksStore feedbacksStore;
    private FeedbackManager feedbackManager;


    public List<String> generateFeedbacks(List<SolutionApproachReportItem> reportEntries, QfSemanticSettings checkerSetting) {
        List<Feedback> nakedFeedbacks = generateNakedFeedbacks(reportEntries, checkerSetting);
        var adaptedFeedbacks = adaptFeedbackByCheckerSetting(nakedFeedbacks, checkerSetting);
        if (feedbackManager == null) feedbackManager = FeedbackManager.builder().build();
        feedbackManager.setFeedbacks(adaptedFeedbacks);
        return feedbackManager.buildFeedbackInTemplate(checkerSetting.getLanguage());
    }

    private List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, QfSemanticSettings checkerSetting) {
        return feedbacks;
    }

    private List<Feedback> generateNakedFeedbacks(List<SolutionApproachReportItem> reportItems, QfSemanticSettings checkerSetting) {
        List<Feedback> result = new ArrayList<>();
        if (feedbacksStore == null) {
            feedbacksStore = new FeedbacksStore(
                    provideStoredFeedbackDirectory(SolutionApproachChecker.class)
                    , checkerSetting.getLanguage() + FileExtensions.JSON
            );
        }
        var keyWordReplacer = KeyWordReplacer.builder().build();
        for (SolutionApproachReportItem reportItem : reportItems) {
            feedbacksStore.customizeStore(reportItem.getRelatedSemanticSettingItem().getTaskSpecificFeedbacks());
            var defaultFeedback = feedbacksStore.getRelatedFeedbackByTechnicalCause(reportItem.getErrorCode());
            if (defaultFeedback != null) {
                var feedbackBuilder = Feedback.builder();
                feedbackBuilder.type(Type.CORRECTION);
                feedbackBuilder.checkerName(SolutionApproachChecker.class.getSimpleName());
                feedbackBuilder.technicalCause(reportItem.getErrorCode());
                feedbackBuilder.readableCause(
                        keyWordReplacer.replace(
                                defaultFeedback.getReadableCause(),
                                reportItem
                        )
                );
                feedbackBuilder.hints((defaultFeedback.getHints() != null) ?
                        defaultFeedback.getHints().stream().peek(hint ->
                                hint.setContent(keyWordReplacer.replace(
                                        hint.getContent(),
                                        reportItem
                                ))
                        ).collect(Collectors.toList())
                        : Collections.emptyList()
                );
                feedbackBuilder.relatedLocation(RelatedLocation.builder()
                        .fileName(
                                reportItem.getRelatedSemanticSettingItem().getFilePath().substring(
                                        reportItem.getRelatedSemanticSettingItem().getFilePath().lastIndexOf("/") + 1
                                )
                        )
                        .methodName(reportItem.getRelatedSemanticSettingItem().getMethodName())
                        .build()
                );
                feedbackBuilder.reference(
                        feedbacksStore.getConceptReference(reportItem.getErrorCode())
                );
                result.add(feedbackBuilder.build());
            }
            feedbacksStore.rebuildStore();
        }
        return result;
    }


}

