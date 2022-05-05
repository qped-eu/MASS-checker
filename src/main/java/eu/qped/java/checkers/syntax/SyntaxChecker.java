package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.utils.compiler.Compiler;
import lombok.*;
import lombok.Data;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;

/**
 * checker for the syntax problems in java code.
 *
 * @author Mayar Hamdhash hamdash@students.uni-marburg.de
 * @version 2.0
 * @since 17.04.2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxChecker {

    @NonNull
    private String answer;
    private String sourceCode;
    private CheckLevel level;
    private List<SyntaxError> syntaxErrors;
    private List<SyntaxFeedback> feedbacks;
    @NonNull
    private boolean errorOccurred;

    public void check() {
        Compiler compiler = Compiler.builder().build();
        this.errorOccurred = compiler.compile(answer);
        List<Diagnostic<? extends JavaFileObject>> diagnostics = compiler.getCollectedDiagnostics();
        this.setSourceCode(compiler.getFullSourceCode());
        if (!diagnostics.isEmpty()) {
            analyseDiagnostics(diagnostics);
            SyntaxFeedbackGenerator feedbackGenerator = new SyntaxFeedbackGenerator(this.getSourceCode(), this.getLevel());
            feedbacks = feedbackGenerator.generateFeedbacks(syntaxErrors);
        } else {
            syntaxErrors = Collections.emptyList();
            feedbacks = Collections.emptyList();
        }
    }

    private void analyseDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        syntaxErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            String errorSource;
            try {
                errorSource = sourceCode.substring((int) diagnostic.getStartPosition());
            } catch (StringIndexOutOfBoundsException e) {
                errorSource = sourceCode.substring((int) diagnostic.getStartPosition() + 1);
            }
            String[] splitSource = errorSource.split(";");

            Map<String, String> addProp = new HashMap<>();

            if (diagnostic.getCode().equals("compiler.err.expected")) {
                String forExpected = errorSource.split("[{]")[0];
                addProp.put("forSemExpected", forExpected);
            }
            String errorTrigger = splitSource[0];
            syntaxErrors.add(
                    SyntaxError.builder()
                            .errorCode(diagnostic.getCode())
                            .errorMsg(diagnostic.getMessage(Locale.GERMAN))
                            .startPos(diagnostic.getStartPosition())
                            .endPos(diagnostic.getEndPosition())
                            .line(diagnostic.getLineNumber())
                            .additionalProperties(addProp)
                            .errorTrigger(errorTrigger)
                            .build()
            );
        }
    }
}
