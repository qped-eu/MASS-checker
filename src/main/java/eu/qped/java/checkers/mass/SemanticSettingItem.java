package eu.qped.java.checkers.mass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticSettingItem {

    private String filePath;

    private String methodName;

    private boolean recursionAllowed;

    private int whileLoop;

    private int forLoop;

    private int forEachLoop;

    private int ifElseStmt;

    private int doWhileLoop;

    private String returnType;

}
