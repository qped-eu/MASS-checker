package eu.qped.java.checkers.syntax.feedback;
import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.FeedbacksStore;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.analyser.SyntaxError;
import eu.qped.java.utils.FileExtensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.qped.framework.feedback.defaultfeedback.StoredFeedbackDirectoryProvider.provideStoredFeedbackDirectory;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackGenerator {

    private FeedbacksStore feedbacksStore;
    private FeedbackManager feedbackManager;

    public List<String> generateFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        List<Feedback> nakedFeedbacks = generateNakedFeedbacks(errors, syntaxSetting);
        nakedFeedbacks = adaptFeedbackByCheckLevel(syntaxSetting, nakedFeedbacks);
        if (feedbackManager == null) feedbackManager = FeedbackManager.builder().build();
        feedbackManager.setFeedbacks(nakedFeedbacks);
        return feedbackManager.buildFeedbackInTemplate(syntaxSetting.getLanguage());
    }

    private List<Feedback> adaptFeedbackByCheckLevel(SyntaxSetting syntaxSetting, List<Feedback> feedbacks) {
        if (syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    private List<Feedback> generateNakedFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        List<Feedback> result = new ArrayList<>();
        if (feedbacksStore == null) {
            feedbacksStore = new FeedbacksStore(
                    provideStoredFeedbackDirectory(SyntaxChecker.class)
                    , syntaxSetting.getLanguage() + FileExtensions.JSON
            );
        }
        for (SyntaxError error : errors) {
            var feedbackBuilder = Feedback.builder();
            feedbackBuilder.type(Type.CORRECTION);
            feedbackBuilder.checkerName(SyntaxChecker.class.getSimpleName());
            feedbackBuilder.relatedLocation(RelatedLocation.builder()
                    .startLine((int) error.getLine())
                    .methodName("")
                    .fileName(
                            error.getFileName() != null && error.getFileName().equals("TestClass.java") ? "" : error.getFileName()
                    )
                    .build()
            );
            if (feedbacksStore.getRelatedFeedbackByTechnicalCause(error.getErrorMessage()) != null) {
                feedbackBuilder.updateFeedback(feedbacksStore.getRelatedFeedbackByTechnicalCause(error.getErrorMessage()));
            } else if (feedbacksStore.getRelatedFeedbackByTechnicalCause(error.getErrorCode()) != null) {
                feedbackBuilder.updateFeedback(feedbacksStore.getRelatedFeedbackByTechnicalCause(error.getErrorCode()));
            } else {
                feedbackBuilder.technicalCause(error.getErrorMessage());
                feedbackBuilder.readableCause(error.getErrorMessage());
            }
            result.add(feedbackBuilder.build());
        }
        return result;
    }


}
