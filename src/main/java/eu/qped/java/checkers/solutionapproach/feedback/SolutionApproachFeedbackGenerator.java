package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbacksStore;
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

import static eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator {

    private DefaultFeedbacksStore defaultFeedbacksStore;
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
        if (defaultFeedbacksStore == null) {
            defaultFeedbacksStore = new DefaultFeedbacksStore(
                    provideDefaultFeedbackDirectory(SolutionApproachChecker.class)
                    , checkerSetting.getLanguage() + FileExtensions.JSON
            );
        }
        var keyWordReplacer = KeyWordReplacer.builder().build();
        for (SolutionApproachReportItem reportItem : reportItems) {
            defaultFeedbacksStore.customizeStore(reportItem.getRelatedSemanticSettingItem().getTaskSpecificFeedbacks());
            var defaultFeedback = defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(reportItem.getErrorCode());
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
                        defaultFeedbacksStore.getConceptReference(reportItem.getErrorCode())
                );
                result.add(feedbackBuilder.build());
            }
            defaultFeedbacksStore.rebuildStoreToDefault();
        }
        return result;
    }


}

