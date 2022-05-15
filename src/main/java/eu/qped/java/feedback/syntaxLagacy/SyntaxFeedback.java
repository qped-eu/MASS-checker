package eu.qped.java.feedback.syntaxLagacy;


import eu.qped.framework.Feedback;
import eu.qped.java.checkers.syntax.ErrorInfo;
import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.*;

@ToString
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

    public String toInfoString(){
        return "FeedbackContent: " + this.getFeedbackContent() + "\n" +
                "SolutionExample: " + this.getSolutionExample() + "\n" +
                "ErrorMessage: " + this.getErrorMessage() + "\n" +
                "ErrorInfo: "+ this.getErrorInfo();
    }

}
