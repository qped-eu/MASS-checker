package eu.qped.java.checkers.syntax.feedback;


import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultSyntaxFeedback {

    private DefaultFeedback defaultFeedback;

    private RelatedLocation relatedLocation;
}
