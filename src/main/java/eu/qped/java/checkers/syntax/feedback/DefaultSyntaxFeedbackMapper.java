package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackMapper;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultSyntaxFeedbackMapper {

    private DefaultFeedbackMapper defaultFeedbackMapper;

    public List<Feedback> map(List<DefaultSyntaxFeedback> feedbacks) {
        if (defaultFeedbackMapper == null) defaultFeedbackMapper = new DefaultFeedbackMapper();
        return feedbacks.stream().map(this::map).collect(Collectors.toList());
    }

    private Feedback map(DefaultSyntaxFeedback defaultSyntaxFeedback) {
        var feedback = defaultFeedbackMapper.mapDefaultFeedbackToFeedback(defaultSyntaxFeedback.getDefaultFeedback(), SyntaxChecker.class,Type.CORRECTION);
        feedback.setRelatedLocation(defaultSyntaxFeedback.getRelatedLocation());
        return feedback;
    }

}
