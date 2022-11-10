package eu.qped.framework.feedback.defaultfeedback;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.Type;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DefaultFeedbackMapper {

    public List<Feedback> mapDefaultFeedbackToFeedback(List<DefaultFeedback> feedbacks, Class<?> checker, Type feedbackTyp) {
        return feedbacks.stream()
                .map(
                        f -> mapDefaultFeedbackToFeedback(f, checker, feedbackTyp)
                ).collect(Collectors.toList());
    }

    public Feedback mapDefaultFeedbackToFeedback(DefaultFeedback feedback, Class<?> checker, Type feedbackTyp) {
        return Feedback.builder()
                .technicalCause(feedback.getTechnicalCause())
                .type(feedbackTyp)
                .readableCause(feedback.getReadableCause())
                .checkerName(checker.getSimpleName())
                .hints(
                        feedback.getHints()
                )
                .build();
    }


}
