package eu.qped.framework.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
