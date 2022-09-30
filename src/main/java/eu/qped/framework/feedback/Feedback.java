package eu.qped.framework.feedback;

import eu.qped.framework.feedback.hint.Hint;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * representation of a feedback for the student. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar Aji
 * @since 15.09.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    private Type type;
    private String checkerName;
    private String readableCause;
    private String technicalCause;
    @Nullable
    private List<Hint> hints;
    @Nullable
    private ConceptReference reference;
    @Nullable
    private ErrorLocation errorLocation;


}
