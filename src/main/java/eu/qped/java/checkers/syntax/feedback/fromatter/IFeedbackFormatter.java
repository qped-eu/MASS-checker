package eu.qped.java.checkers.syntax.feedback.fromatter;

import eu.qped.framework.feedback.Feedback;

import java.util.List;

public interface IFeedbackFormatter {

    List<Feedback> format(List<Feedback> feedbacks);

}
