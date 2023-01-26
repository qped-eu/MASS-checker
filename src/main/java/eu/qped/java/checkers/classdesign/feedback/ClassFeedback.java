package eu.qped.java.checkers.classdesign.feedback;

import eu.qped.framework.Feedback;

/**
 A class that represents the feedback for a class design issue.
 This class extends the {@link Feedback} class and provides additional functionality specific to class design issues.
 @author Jannik Seus
 @see Feedback
 */

public class ClassFeedback extends Feedback {

    /**
     Constructor for the ClassFeedback
     @param body is the body of the feedback represented as a string
     */
    public ClassFeedback(final String body) {
        super(body);
    }

    /**
     A method used to get the body of the feedback as a string.
     The feedback body is the text describing the class design issue.
     @return the feedback represented as a string
     */
    public String toString() {
        return super.getBody();
    }

    /**
     A method used to compare two ClassFeedback objects.
     The comparison is based on the equality of the feedback body.
     @param object the object to be compared with
     @return a boolean value, true is the two obects are equal, false if not
     */
    @Override
    public boolean equals(final Object object) {
        return super.equals(object);
    }

    /**
     A method used to calculate the hashcode of the ClassFeedback object.
     @return the calculated hashcode
     */
    @Override
    public int hashCode(){return super.hashCode();}
}
