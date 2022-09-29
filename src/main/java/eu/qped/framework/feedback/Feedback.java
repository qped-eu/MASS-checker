package eu.qped.framework.feedback;

import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.severity.Severity;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


    @NotNull
    private String cause;
    @NotNull
    private Severity severity;
    private List<Hint> hints;
    private ErrorLocation errorLocation;

    public List<Hint> getHints() {
        return Optional.ofNullable(hints).orElse(Collections.emptyList());
    }

    public ErrorLocation getErrorLocation() {
        return Optional.ofNullable(this.errorLocation).orElse(ErrorLocation.builder().build());
    }

}
