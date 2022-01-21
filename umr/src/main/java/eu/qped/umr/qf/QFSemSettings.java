package eu.qped.umr.qf;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class QFSemSettings {
    //todo rec muss und rec allowed

    private String methodName;
    private String recursionAllowed; //todo name ändern
    private String whileLoop;
    private String forLoop;
    private String forEachLoop;
    private String ifElseStmt;
    private String doWhileLoop;
    private String returnType;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }



	public boolean hasProperty(String property) {
		return additionalProperties.containsKey(property);
	}



	@JsonAnySetter
	public void setAdditionalProperty(String property, String value){
		additionalProperties.put(property, value);
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
