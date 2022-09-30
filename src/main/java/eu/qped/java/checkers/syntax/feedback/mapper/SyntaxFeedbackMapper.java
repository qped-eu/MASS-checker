package eu.qped.java.checkers.syntax.feedback.mapper;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.Type;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
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
                                .technicalCause(sf.getErrorCause())
                                .errorLocation(sf.getErrorLocation())
                                .build()
                ).collect(Collectors.toList());
    }
}
