package eu.qped.framework.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * representation of Error location in the Student solution. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar Aji <pre>ajio@students.uni-marburg.de</pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatedLocation {

    private String fileName;
    private String methodName;
    private int startLine = -1;
    private int endLine = -1;

}
