package eu.qped.umr.exceptions;

public class NoSuchPropertyException extends Exception{
    private final String propName;
    private final String ruleset;
    public NoSuchPropertyException(String msg , String propName , String ruleset){
        super(msg);
        this.propName = propName;
        this.ruleset = ruleset;
    }

    public String getPropName() {
        return propName;
    }

    public String getRuleset() {
        return ruleset;
    }
}
