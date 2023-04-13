package eu.qped.java.checkers.syntax.feedback;
import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackManager;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbacksStore;
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

import static eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackGenerator {

    private DefaultFeedbacksStore defaultFeedbacksStore;
    private FeedbackManager feedbackManager;

    public List<String> generateFeedbacks(final List<SyntaxError> errors, final SyntaxSetting syntaxSetting) {
        List<Feedback> nakedFeedbacks = generateNakedFeedbacks(errors, syntaxSetting);
        nakedFeedbacks = adaptFeedbackByCheckLevel(syntaxSetting, nakedFeedbacks);
        if (feedbackManager == null) {
            feedbackManager = FeedbackManager.builder().build();
        }
        feedbackManager.setFeedbacks(nakedFeedbacks);
        return feedbackManager.buildFeedbackInTemplate(syntaxSetting.getLanguage());
    }

    private List<Feedback> adaptFeedbackByCheckLevel(final SyntaxSetting syntaxSetting, final List<Feedback> feedbacks) {
        if (syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    private List<Feedback> generateNakedFeedbacks(final List<SyntaxError> errors, final SyntaxSetting syntaxSetting) {
        final List<Feedback> result = new ArrayList<>();
        if (defaultFeedbacksStore == null) {
            defaultFeedbacksStore = new DefaultFeedbacksStore(
                    provideDefaultFeedbackDirectory(SyntaxChecker.class)
                    , syntaxSetting.getLanguage() + FileExtensions.JSON
            );
        }

        for (final SyntaxError error : errors) {
            final var feedback = Feedback.builder().build();
            feedback.setType(Type.CORRECTION);
            feedback.setCheckerName(SyntaxChecker.class.getSimpleName());
            if (defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(error.getErrorMessage()) != null) {
                feedback.updateFeedback(defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(error.getErrorMessage()));
            } else if (defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(error.getErrorCode()) != null) {
                feedback.updateFeedback(defaultFeedbacksStore.getRelatedDefaultFeedbackByTechnicalCause(error.getErrorCode()));
            } else {
                feedback.setTechnicalCause(error.getErrorMessage());
                feedback.setReadableCause(error.getErrorMessage());
            }
            feedback.setRelatedLocation(RelatedLocation.builder()
                    .startLine((int) error.getLine())
                    .methodName("")
                    .fileName(
                            error.getFileName() != null && "TestClass.java".equals(error.getFileName()) ? "" : error.getFileName()
                    )
                    .build()
            );
            result.add(feedback);
        }
        return result;
    }


}
