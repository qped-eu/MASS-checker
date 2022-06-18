package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.Builder;

import java.util.concurrent.atomic.AtomicInteger;

@Builder
public class SyntaxFeedbackGenerator extends AbstractSyntaxFeedbackGenerator {

    private final AtomicInteger feedbackCounter = new AtomicInteger(0);

    @Override
    protected String generateHeader() {
        return "Error " + String.format("%02d", feedbackCounter.incrementAndGet()) + ":";
    }

    @Override
    protected String generateFeedbackMessage(SyntaxError error) {
        String result = "";
        SyntaxFeedbackMessages syntaxFeedbackMessages = SyntaxFeedbackMessages.builder().build();
        if (syntaxFeedbackMessages.getFeedbackMessagesByErrorCode().containsKey(error.getErrorCode())) {
            result += syntaxFeedbackMessages.getFeedbackMessagesByErrorCode().get(error.getErrorCode());
        }
        if (syntaxFeedbackMessages.getFeedbackMessagesByErrorMessage().containsKey(error.getErrorMessage())) {
            result += syntaxFeedbackMessages.getFeedbackMessagesByErrorMessage().get(error.getErrorMessage());
        }
        if (result.equals("")) {
            result = error.getErrorMessage();
        }
        return result;
    }

    @Override
    protected String generateErrorLine(long errorLine) {
        return "At line :" + String.format("%d", errorLine);
    }

    @Override
    protected String generateErrorSource(String errorTrigger) {
        return errorTrigger;
    }

    @Override
    protected String generateSolutionExample(SyntaxError error) {
        String result = "";
        SyntaxFeedbackSolutionExamples syntaxFeedbackSolutionExamples = SyntaxFeedbackSolutionExamples.builder().build();
        if (syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorCode().containsKey(error.getErrorCode())) {
            result += syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorCode().get(error.getErrorCode());
        }
        if (syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorMessage().containsKey(error.getErrorMessage())) {
            result += syntaxFeedbackSolutionExamples.getSolutionExamplesByErrorMessage().get(error.getErrorMessage());
        }
        return result;
    }

}
