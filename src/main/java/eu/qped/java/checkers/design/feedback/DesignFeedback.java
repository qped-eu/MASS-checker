package eu.qped.java.checkers.design.feedback;

import eu.qped.framework.Feedback;

public class DesignFeedback extends Feedback {


    public DesignFeedback(String body) {
        super(body);
    }

    public String toString() {
        return super.getBody();
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
