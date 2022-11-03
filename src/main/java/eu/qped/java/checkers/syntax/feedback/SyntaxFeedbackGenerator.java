package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedback;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.analyser.SyntaxError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackGenerator {


    private DefaultSyntaxFeedbackParser defaultSyntaxFeedbackParser;
    private DefaultSyntaxFeedbackMapper defaultSyntaxFeedbackMapper;
    private MarkdownFeedbackFormatter markdownFeedbackFormatter;

    public List<Feedback> generateFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        List<DefaultSyntaxFeedback> filteredFeedbacks = getDefaultSyntaxFeedbacks(errors, syntaxSetting);
        // naked feedbacks
        List<Feedback> feedbacks = mapToFeedbacks(filteredFeedbacks);
        // adapted by check level naked feedbacks
        feedbacks = adaptFeedbackByCheckLevel(syntaxSetting, feedbacks);
        // formatted feedbacks
        return formatFeedbacks(feedbacks);
    }

    private List<DefaultSyntaxFeedback> getDefaultSyntaxFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        if (defaultSyntaxFeedbackParser == null)
            defaultSyntaxFeedbackParser = DefaultSyntaxFeedbackParser.builder().build();
        List<DefaultSyntaxFeedback> allDefaultSyntaxFeedbacks = defaultSyntaxFeedbackParser.parse(syntaxSetting.getLanguage());
        var allDefaultSyntaxFeedbacksByTechnicalCause =
                allDefaultSyntaxFeedbacks.stream()
                        .collect(Collectors.groupingBy(
                                defaultSyntaxFeedback -> defaultSyntaxFeedback.getDefaultFeedback().getTechnicalCause()
                        ));
        return filterDefaultSyntaxFeedbacks(errors, allDefaultSyntaxFeedbacksByTechnicalCause);
    }

    private List<Feedback> mapToFeedbacks(List<DefaultSyntaxFeedback> filteredFeedbacks) {
        if (defaultSyntaxFeedbackMapper == null)
            defaultSyntaxFeedbackMapper = DefaultSyntaxFeedbackMapper.builder().build();
        return defaultSyntaxFeedbackMapper.map(filteredFeedbacks);
    }

    private List<Feedback> adaptFeedbackByCheckLevel(SyntaxSetting syntaxSetting, List<Feedback> feedbacks) {
        if (syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    private List<Feedback> formatFeedbacks(List<Feedback> feedbacks) {
        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = MarkdownFeedbackFormatter.builder().build();
        return markdownFeedbackFormatter.format(feedbacks);
    }

    private List<DefaultSyntaxFeedback> filterDefaultSyntaxFeedbacks(List<SyntaxError> errors, Map<String, List<DefaultSyntaxFeedback>> allDefaultSyntaxFeedbacksByTechnicalCause) {
        List<DefaultSyntaxFeedback> result = new ArrayList<>();
        for (SyntaxError error : errors) {
            var byErrorCode =
                    allDefaultSyntaxFeedbacksByTechnicalCause.get(error.getErrorCode());
            if (byErrorCode == null) {
                var byErrorMessage = allDefaultSyntaxFeedbacksByTechnicalCause.get(error.getErrorMessage());
                if (byErrorMessage != null) {
                    result.addAll(byErrorMessage);
                } else {
                    var syntaxFeedback =
                            DefaultSyntaxFeedback.builder()
                                    .defaultFeedback(DefaultFeedback.builder()
                                            .hints(Collections.emptyList())
                                            .technicalCause(error.getErrorCode())
                                            .readableCause(error.getErrorMessage())
                                            .build())
                                    .relatedLocation(RelatedLocation.builder().build())
                                    .build();
                    result.add(syntaxFeedback);
                }
            } else {
                result.addAll(byErrorCode);
            }
            result.forEach(
                    sf -> sf.setRelatedLocation(
                            RelatedLocation.builder()
                                    .startLine((int) error.getLine())
                                    .methodName("")
                                    .fileName(
                                            error.getFileName() != null && error.getFileName().equals("TestClass.java") ? "" : error.getFileName()
                                    )
                                    .build()
                    ));
        }
        return result;
    }


}
