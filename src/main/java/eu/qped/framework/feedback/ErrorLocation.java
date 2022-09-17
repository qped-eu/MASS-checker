package eu.qped.framework.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * representation of Error location in the Student solution. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar
 * @since 15.09.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorLocation {

    private String className;
    private String methodName;
    private long startPosition;
    private long endPosition;

}
