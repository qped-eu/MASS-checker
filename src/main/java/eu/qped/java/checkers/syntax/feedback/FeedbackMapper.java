package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.feedback.ErrorLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.framework.feedback.severity.Severity;
import eu.qped.framework.feedback.severity.SeverityType;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class FeedbackMapper {


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
                        .hintContent(syntaxFeedback.getFeedbackMessage())
                        .hintTypes(
                                Set.of(
                                        HintType.TEXT,
                                        HintType.CODE_EXAMPLE
                                )
                        ).build();

        var severity = Severity.
                builder()
                .severity(syntaxFeedback.getErrorSource())
                .severityType(SeverityType.ERROR)
                .build();

        return
                Feedback
                        .builder()
                        .errorLocation(errorLocation)
                        .hint(hint)
                        .severity(severity)
                        .title(syntaxFeedback.getHeader())
                        .build();
    }


}
