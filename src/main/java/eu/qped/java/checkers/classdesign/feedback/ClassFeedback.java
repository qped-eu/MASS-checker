package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.framework.Feedback;

/** A class that represents the feedback */

public class ClassFeedback extends Feedback {

    /**
     * Constructor for the ClassFeedback
     * @param body is the body of the feedback represented as a string
     */
    public ClassFeedback(final String body) {
        super(body);
    }

    /**
     * A method used to get the body of the feedback as a string
     * @return the feedback represented as a string
     */
    public String toString() {
        return super.getBody();
    }

    /**
     * A method used to compare two objects
     * @param object the object to be compared with
     * @return a boolean value, true is the two obects are equal, false if not
     */
    @Override
    public boolean equals(final Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode(){return super.hashCode();}
}
