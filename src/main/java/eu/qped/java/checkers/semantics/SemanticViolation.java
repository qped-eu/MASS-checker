package eu.qped.java.checkers.semantics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticViolation {

    private String description;
    private String methodName;

}
