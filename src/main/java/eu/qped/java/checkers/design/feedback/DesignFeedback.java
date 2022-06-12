package eu.qped.java.checkers.design.feedback;

import eu.qped.framework.Feedback;

public class DesignFeedback extends Feedback {

    public DesignFeedback(String body) {
        super(body);
    }

    public String toString() {
        return super.getBody();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
