package eu.qped.java.checkers.semantics.configs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SemanticMethodConfig {

    private String methodName;
    private Boolean recursionAllowed;
    private Integer whileLoop;
    private Integer forLoop;
    private Integer forEachLoop;
    private Integer ifElseStmt;
    private Integer doWhileLoop;
    private String returnType;

}
