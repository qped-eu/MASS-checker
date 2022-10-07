package eu.qped.java.checkers.syntax.feedback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.qped.framework.feedback.RelatedLocation;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyntaxFeedback {
    private String technicalCause;
    private String readableCause;
    private List<Hint> hints;

    @JsonIgnore
    private RelatedLocation relatedLocation;
}
