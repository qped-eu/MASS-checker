package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.utils.FileExtensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackGenerator {

    private SyntaxFeedbackMapper syntaxFeedbackMapper;

    private MarkdownFeedbackFormatter markdownFeedbackFormatter;
    private DefaultSyntaxFeedbackProvider defaultSyntaxFeedbackProvider;

    public List<Feedback> generateFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        //TODO abstract
        List<SyntaxFeedback> allDefaultSyntaxFeedbacks = getAllDefaultSyntaxFeedbacks(syntaxSetting.getLanguage());

        var FeedbacksByErrorKeys =
                allDefaultSyntaxFeedbacks.stream()
                        .collect(Collectors.groupingBy(SyntaxFeedback::getTechnicalCause));

        var filteredFeedbacks = filterFeedbacks(errors, FeedbacksByErrorKeys);

        if (syntaxFeedbackMapper == null) syntaxFeedbackMapper = new SyntaxFeedbackMapper();
        // naked feedbacks
        var feedbacks = syntaxFeedbackMapper.mapSyntaxFeedbackToFeedback(filteredFeedbacks);
        // adapted by check level naked feedbacks
        feedbacks = adaptFeedbackByCheckLevel(syntaxSetting, feedbacks);
        // formatted feedbacks
        return formatFeedbacks(feedbacks);
    }

    private List<Feedback> formatFeedbacks(List<Feedback> feedbacks) {
        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(feedbacks);
    }

    private List<Feedback> adaptFeedbackByCheckLevel(SyntaxSetting syntaxSetting, List<Feedback> feedbacks) {
        if (syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    private List<SyntaxFeedback> getAllDefaultSyntaxFeedbacks(String language) {
        var dirPath = DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory(SyntaxChecker.class);
        if (defaultSyntaxFeedbackProvider == null) {
            defaultSyntaxFeedbackProvider = new DefaultSyntaxFeedbackProvider();
        }
        return defaultSyntaxFeedbackProvider.provide(dirPath, language + FileExtensions.JSON);
    }

    private List<SyntaxFeedback> filterFeedbacks(List<SyntaxError> errors, Map<String, List<SyntaxFeedback>> keyToFeedbacks) {
        List<SyntaxFeedback> filteredSyntaxFeedbacks = new ArrayList<>();
        for (SyntaxError error : errors) {
            var byErrorCode =
                    keyToFeedbacks.get(error.getErrorCode());
            if (byErrorCode == null) {
                var byErrorMessage = keyToFeedbacks.get(error.getErrorMessage());
                if (byErrorMessage != null) {
                    filteredSyntaxFeedbacks.addAll(byErrorMessage);
                } else {
                    var syntaxFeedback =
                            SyntaxFeedback.builder()
                                    .hints(Collections.emptyList())
                                    .readableCause(error.getErrorMessage())
                                    .technicalCause(error.getErrorCode())
                                    .relatedLocation(RelatedLocation.builder().build())
                                    .build();
                    filteredSyntaxFeedbacks.add(syntaxFeedback);
                }
            } else {
                filteredSyntaxFeedbacks.addAll(byErrorCode);
            }
            filteredSyntaxFeedbacks.forEach(
                    sf -> sf.setRelatedLocation(
                            RelatedLocation.builder()
                                    .startLine((int) error.getLine())
                                    .methodName("")
                                    .fileName(error.getFileName().equals("TestClass.java") ? "" : error.getFileName())
                                    .build()
                    ));
        }
        return filteredSyntaxFeedbacks;
    }


}
