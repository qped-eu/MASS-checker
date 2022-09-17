package eu.qped.java.checkers.syntax.feedback.mapper;

import eu.qped.framework.feedback.ErrorLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.framework.feedback.severity.Severity;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedback;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@NoArgsConstructor
public class FeedbackMapper {

    private final AtomicInteger counter = new AtomicInteger(0);

    public List<Feedback> mapFromJsonFeedback(List<eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback> feedbacks) {

        return feedbacks.stream()
                .map(
                        sf -> Feedback.builder()
                                .title(String.format("Compiler error %02d", counter.incrementAndGet()))
                                .severity(Severity.ERROR)
                                .hints(
                                        sf.getHints()
                                )
                                .errorCause(sf.getErrorCause())
                                .build()
                ).collect(Collectors.toList());
    }

    public List<Feedback> map(List<SyntaxFeedback> syntaxFeedbacks) {
        return
                syntaxFeedbacks.stream()
                        .map(this::map)
                        .collect(Collectors.toList());
    }

    protected Feedback map(SyntaxFeedback syntaxFeedback) {
        var errorLocation =
                ErrorLocation.builder()
                        .startPosition(Long.parseLong(syntaxFeedback.getErrorLine()))
                        .build();

        var hint =
                Hint.builder()
                        .content(syntaxFeedback.getFeedbackMessage())
                        .type(
                                HintType.TEXT
                        ).build();


        return
                Feedback
                        .builder()
                        .errorLocation(errorLocation)
                        .title(syntaxFeedback.getHeader())
                        .build();
    }


}
