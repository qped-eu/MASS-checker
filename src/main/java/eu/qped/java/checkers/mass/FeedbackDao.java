package eu.qped.java.checkers.mass;

import java.util.List;

public interface FeedbackDao<T> {

    List<T> fetchPotentialFeedbacks(String errorCode);

}
