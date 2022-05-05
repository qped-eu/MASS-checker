package eu.qped.java.checkers.design;

import eu.qped.framework.Feedback;

public class DesignFeedback extends Feedback {

    private String className;
    private String elementName;
    private String violationType;


    public DesignFeedback(String className, String elementName, String violationType,  String body) {
        super(body);
        setClassName(className);
        setElementName(elementName);
        setViolationType(violationType);
    }

    public String toString() {
        return className+":"+elementName+":"+violationType+":"+super.getBody();
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        DesignFeedback fb = null;

        if(obj instanceof DesignFeedback) {
            fb = (DesignFeedback) obj;
        }

        if(fb != null) {
            isEqual = fb.toString().equals(this.toString());
        }

        return isEqual;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }
}
