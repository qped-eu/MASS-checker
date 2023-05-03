package eu.qped.framework.feedback.hint;

import lombok.*;

/**
 * representation of a feedback content to give student a hint to fix his Solution. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hint implements Cloneable{

    @NonNull
    private HintType type;
    @NonNull
    private String content;

    @Override
    public Hint clone() {
        try {
            return (Hint) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

}
