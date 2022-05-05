package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QFSemSettings extends QfObjectBase {
    //todo rec muss und rec allowed

    private String methodName;
    private String recursionAllowed; //todo name ändern
    private String whileLoop;
    private String forLoop;
    private String forEachLoop;
    private String ifElseStmt;
    private String doWhileLoop;
    private String returnType;

}
