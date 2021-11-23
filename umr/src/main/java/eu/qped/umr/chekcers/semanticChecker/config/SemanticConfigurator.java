package eu.qped.umr.chekcers.semanticChecker.config;

import eu.qped.umr.qf.QFSemSettings;


public class SemanticConfigurator {

    private String methodName;
    private String recursionAllowed;
    private String whileLoop;
    private String forLoop;
    private String forEachLoop;
    private String ifElseStmt;
    private String doWhileLoop;
    private String returnType;

    private final QFSemSettings qfSemSettings;


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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRecursionAllowed() {
        return recursionAllowed;
    }

    public void setRecursionAllowed(String recursionAllowed) {
        this.recursionAllowed = recursionAllowed;
    }

    public String getWhileLoop() {
        return whileLoop;
    }

    public void setWhileLoop(String whileLoop) {
        this.whileLoop = whileLoop;
    }

    public String getForLoop() {
        return forLoop;
    }

    public void setForLoop(String forLoop) {
        this.forLoop = forLoop;
    }

    public String getForEachLoop() {
        return forEachLoop;
    }

    public void setForEachLoop(String forEachLoop) {
        this.forEachLoop = forEachLoop;
    }

    public String getIfElseStmt() {
        return ifElseStmt;
    }

    public void setIfElseStmt(String ifElseStmt) {
        this.ifElseStmt = ifElseStmt;
    }

    public String getDoWhileLoop() {
        return doWhileLoop;
    }

    public void setDoWhileLoop(String doWhileLoop) {
        this.doWhileLoop = doWhileLoop;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

}
