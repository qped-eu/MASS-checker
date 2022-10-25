package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.Type;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SyntaxFeedbackMapper {

    public List<Feedback> mapSyntaxFeedbackToFeedback(List<SyntaxFeedback> feedbacks) {
        return feedbacks.stream()
                .map(
                        sf -> Feedback.builder()
                                .type(Type.CORRECTION)
                                .hints(
                                        sf.getHints()
                                )
                                .technicalCause(sf.getTechnicalCause())
                                .readableCause(sf.getReadableCause())
                                .relatedLocation(sf.getRelatedLocation())
                                .checkerName(SyntaxChecker.class.getSimpleName())
                                .build()
                ).collect(Collectors.toList());
    }
}
