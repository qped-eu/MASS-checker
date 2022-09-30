package eu.qped.framework.feedback.abs;

import eu.qped.framework.feedback.Feedback;

public class FeedbackGen {






    <T extends AbstractError> Feedback generate(T error){
       if (error instanceof StyleError){
       }
        return null;
    }

    public static void main(String[] args) {


        StyleError error = new StyleError();

    }



}
