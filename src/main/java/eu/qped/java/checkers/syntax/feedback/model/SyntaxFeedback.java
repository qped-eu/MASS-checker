package eu.qped.java.checkers.syntax.feedback.model;

import eu.qped.framework.feedback.hint.Hint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxFeedback {
    private String errorKey;
    private String errorCause;
    private List<Hint> hints;
}
