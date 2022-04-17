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

    private String methodName;
    private String recursionAllowed;
    private String whileLoop;
    private String forLoop;
    private String forEachLoop;
    private String ifElseStmt;
    private String doWhileLoop;
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
        setRecursionAllowed("false");
        setWhileLoop("-1");
        setForLoop("-1");
        setForEachLoop("-1");
        setIfElseStmt("-1");
        setDoWhileLoop("-1");
        setReturnType("undefined");
    }

    private void applySettings(){

        if(qfSemSettings.getMethodName()!=null){
            setMethodName(qfSemSettings.getMethodName());
        }
        if(qfSemSettings.getRecursionAllowed()!=null){
            setRecursionAllowed(qfSemSettings.getRecursionAllowed());
        }
        if(qfSemSettings.getDoWhileLoop()!=null){
            setWhileLoop(qfSemSettings.getWhileLoop());
        }
        if(qfSemSettings.getForLoop()!=null){
            setForLoop(qfSemSettings.getForLoop());
        }
        if(qfSemSettings.getForEachLoop()!=null){
            setForEachLoop(qfSemSettings.getForEachLoop());
        }
        if(qfSemSettings.getIfElseStmt()!=null){
            setIfElseStmt(qfSemSettings.getIfElseStmt());
        }
        if(qfSemSettings.getDoWhileLoop()!=null){
            setDoWhileLoop(qfSemSettings.getDoWhileLoop());
        }
        if(qfSemSettings.getReturnType()!=null){
            setReturnType(qfSemSettings.getReturnType());
        }

    }

}
