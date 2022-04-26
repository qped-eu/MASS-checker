package eu.qped.java.checkers.design;

import eu.qped.framework.Feedback;

public class DesignFeedback extends Feedback {

    private String name;
    private String violationType;

    public DesignFeedback(String violationType, String name, String body) {
        super(body);
        this.name = name;
        this.violationType = violationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return violationType+":"+name+":"+super.getBody();
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


}
