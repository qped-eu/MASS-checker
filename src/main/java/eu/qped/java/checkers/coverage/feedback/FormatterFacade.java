package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;

import java.util.List;


/**
 * Defines the API that can be used in the {@link Formatter}.
 * @author Herfurth
 * @version 1.0
 */
public interface FormatterFacade {

    List<Feedback> feedbacks();

    List<TestFB> testFeedback();

    List<StmtFB> stmtFeedback();

    List<ByClass> byClass();

}
