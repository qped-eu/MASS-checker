package eu.qped.framework.feedback.hint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * representation of a feedback content to give student a hint to fix his Solution. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar
 * @since 15.09.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hint {

    private HintType type;
    private String content;

}
