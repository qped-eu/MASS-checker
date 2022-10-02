package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.java.checkers.syntax.SyntaxError;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
@Deprecated(forRemoval = true)
public abstract class AbstractSyntaxFeedbackGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    private CheckLevel checkLevel;


    @Deprecated(forRemoval = true)
    public List<SyntaxFeedback> generateFeedbacks(List<SyntaxError> syntaxErrors) {
        if (syntaxErrors == null || syntaxErrors.isEmpty()) return Collections.emptyList();
        return syntaxErrors.stream()
                .map(this::generateFeedback)
                .map(syntaxFeedback -> SyntaxFeedbackFormatter.builder().build().formatFeedback(syntaxFeedback))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Feedback> generateFeedback(List<SyntaxError> syntaxErrors) {

        var nakedFeedbacks = new ArrayList<Feedback>();

        return Collections.emptyList();
    }

    public void getFromFile(String errorKey) {

    }

    protected Feedback gnF(SyntaxError error) {


        String tempTitle =
                String.format("Syntax error %02d :", counter.incrementAndGet());


        if ("compiler.err.expected".equals(error.getErrorCode())) {
            getFromFile(error.getErrorMessage());
        }


        var hints = new HashSet<Hint>();

        if (checkLevel == CheckLevel.BEGINNER) {
            hints.add(
                    Hint.builder()
                            .type(HintType.CODE_EXAMPLE)
                            .build()
            );
            hints.add(
                    Hint.builder()
                            .type(HintType.TEXT)
                            .build()
            );
        }
        ;

        return
                Feedback
                        .builder()
//                        .title(tempTitle)
                        .build();

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
