package eu.qped.java.feedback;

import eu.qped.framework.Feedback;

import java.util.List;

public interface FeedbackGenerator<F,E> {
    List<F> generateFeedbacks(List<E> errors);
}
