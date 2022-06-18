package eu.qped.java.checkers.semantics;

import eu.qped.java.checkers.mass.QFSemSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticConfigurator {

    private String filePath;
    private String methodName;
    private Boolean recursionAllowed;
    private Integer whileLoop;
    private Integer forLoop;
    private Integer forEachLoop;
    private Integer ifElseStmt;
    private Integer doWhileLoop;
    private String returnType;

    private QFSemSettings qfSemSettings;


    private SemanticConfigurator(QFSemSettings qfSemSettings) {
        this.qfSemSettings = qfSemSettings;
        setDefaults();
        applySettings();
    }

    public static SemanticConfigurator createSemanticConfigurator(QFSemSettings qfSemSettings) {
        return new SemanticConfigurator(qfSemSettings);
    }

    private void setDefaults(){
        setMethodName("undefined");
        setRecursionAllowed(false);
        setReturnType("undefined");
        setWhileLoop(-1);
        setForLoop(-1);
        setForEachLoop(-1);
        setIfElseStmt(-1);
        setDoWhileLoop(-1);
    }

    private void applySettings() {

        if (qfSemSettings.getMethodName() != null) {
            setFilePath(qfSemSettings.getFilePath());
        }
        if (qfSemSettings.getMethodName() != null) {
            setMethodName(qfSemSettings.getMethodName());
        }
        if (qfSemSettings.getRecursionAllowed() != null) {
            setRecursionAllowed(Boolean.parseBoolean(qfSemSettings.getRecursionAllowed()));
        }
        if (qfSemSettings.getDoWhileLoop() != null) {
            setWhileLoop(Integer.parseInt(qfSemSettings.getWhileLoop()));
        }
        if (qfSemSettings.getForLoop() != null) {
            setForLoop(Integer.parseInt(qfSemSettings.getForLoop()));
        }
        if(qfSemSettings.getForEachLoop()!=null){
            setForEachLoop(Integer.parseInt(qfSemSettings.getForEachLoop()));
        }
        if(qfSemSettings.getIfElseStmt()!=null){
            setIfElseStmt(Integer.parseInt(qfSemSettings.getIfElseStmt()));
        }
        if(qfSemSettings.getDoWhileLoop()!=null){
            setDoWhileLoop(Integer.parseInt(qfSemSettings.getDoWhileLoop()));
        }
        if(qfSemSettings.getReturnType()!=null){
            setReturnType(qfSemSettings.getReturnType());
        }

    }

}
