package eu.qped.umr.model;

public class SemanticViolation {

    private String description;
    private String methodName;


    public SemanticViolation (String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
