package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.SyntaxError;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractFeedbackGenerator {
    public List<SyntaxFeedbackNew> generateFeedbacks(List<SyntaxError> syntaxErrors) {
        return syntaxErrors.stream()
                .map(this::generateFeedback)
                .map(syntaxFeedback -> SyntaxFeedbackFormatter.builder().build().formatFeedback(syntaxFeedback))
                .collect(Collectors.toUnmodifiableList());
    }

    protected SyntaxFeedbackNew generateFeedback(SyntaxError syntaxError) {
        return SyntaxFeedbackNew.builder()
                .header(generateHeader())
                .feedbackMessage(generateFeedbackMessage(syntaxError))
                .errorLine(generateErrorLine(syntaxError))
                .errorSource(generateErrorSource(syntaxError))
                .solutionExample(generateSolutionExample(syntaxError))
                .build();
    }

    protected abstract String generateHeader();

    protected abstract String generateFeedbackMessage(SyntaxError syntaxError);

    protected abstract String generateErrorLine(SyntaxError syntaxError);

    protected abstract String generateErrorSource(SyntaxError syntaxError);

    protected abstract String generateSolutionExample(SyntaxError syntaxError);


}
