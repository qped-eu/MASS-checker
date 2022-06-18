package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.SyntaxError;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSyntaxFeedbackGenerator {
    public List<SyntaxFeedback> generateFeedbacks(List<SyntaxError> syntaxErrors) {
        return syntaxErrors.stream()
                .map(this::generateFeedback)
                .map(syntaxFeedback -> SyntaxFeedbackFormatter.builder().build().formatFeedback(syntaxFeedback))
                .collect(Collectors.toUnmodifiableList());
    }

    protected SyntaxFeedback generateFeedback(SyntaxError syntaxError) {
        return SyntaxFeedback.builder()
                .header(generateHeader())
                .feedbackMessage(generateFeedbackMessage(syntaxError))
                .errorLine(generateErrorLine(syntaxError.getLine()))
                .errorSource(generateErrorSource(syntaxError.getErrorTrigger()))
                .solutionExample(generateSolutionExample(syntaxError))
                .build();
    }

    protected abstract String generateHeader();

    protected abstract String generateFeedbackMessage(SyntaxError syntaxError);

    protected abstract String generateErrorLine(long errorLine);

    protected abstract String generateErrorSource(String errorTrigger);

    protected abstract String generateSolutionExample(SyntaxError syntaxError);


}
