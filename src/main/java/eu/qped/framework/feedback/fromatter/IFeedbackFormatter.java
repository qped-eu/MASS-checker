package eu.qped.framework.feedback.fromatter;

import eu.qped.framework.feedback.Feedback;

import java.util.List;

public interface IFeedbackFormatter {

    List<Feedback> format(List<Feedback> feedbacks);

    Feedback format(Feedback feedback);

}
