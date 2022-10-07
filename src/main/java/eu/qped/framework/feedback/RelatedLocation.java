package eu.qped.framework.feedback;

import lombok.*;

/**
 * representation of Error location in the Student solution. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar Aji <pre>ajio@students.uni-marburg.de</pre>
 * @since 15.09.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatedLocation {

    @NonNull
    private String fileName;
    private String methodName;
    private int startLine = -1;
    private int endLine = -1;

}
