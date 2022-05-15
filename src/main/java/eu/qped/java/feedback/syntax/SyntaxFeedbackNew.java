package eu.qped.java.feedback.syntax;

import eu.qped.framework.Feedback;
import eu.qped.java.checkers.syntax.ErrorInfo;
import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxFeedbackNew extends Feedback {
    private String feedbackContent;
    private String solutionExample;
    private String errorMessage;
    private SyntaxError syntaxError;
    private ErrorInfo errorInfo;
}
