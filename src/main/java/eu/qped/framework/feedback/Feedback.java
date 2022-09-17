package eu.qped.framework.feedback;

import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.severity.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * representation of a feedback for the student. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar
 * @since 15.09.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    private String title;
    private Severity severity;
    private Hint hint;
    private ErrorLocation errorLocation;

}
