package eu.qped.java.checkers.syntax;


import eu.qped.framework.Feedback;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxFeedback extends Feedback {

    private String feedbackContent;
    private String solutionExample;
    private String errorMessage;
    private SyntaxError syntaxError;
    private ErrorInfo errorInfo;

}
