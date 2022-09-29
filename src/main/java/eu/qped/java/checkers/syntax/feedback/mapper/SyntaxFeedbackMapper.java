package eu.qped.java.checkers.syntax.feedback.mapper;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.severity.Severity;
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
                                .severity(Severity.CORRECTION)
                                .hints(
                                        sf.getHints()
                                )
                                .cause(sf.getErrorCause())
                                .errorLocation(sf.getErrorLocation())
                                .build()
                ).collect(Collectors.toList());
    }
}
