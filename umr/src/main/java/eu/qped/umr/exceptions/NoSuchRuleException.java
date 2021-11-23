package eu.qped.umr.exceptions;

public class NoSuchRuleException extends Exception{
    private final String xmlPath;
    private final String ruleName;
    public NoSuchRuleException (String msg , String xmlPath , String ruleName){
        super(msg);
        this.xmlPath = xmlPath;
        this.ruleName = ruleName;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public String getRuleName() {
        return ruleName;
    }
}
