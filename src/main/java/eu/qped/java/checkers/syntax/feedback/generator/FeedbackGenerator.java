package eu.qped.java.checkers.syntax.feedback.generator;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.ErrorLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackFileDirectoryProvider;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.syntax.feedback.mapper.DefaultSyntaxFeedbackProvider;
import eu.qped.java.checkers.syntax.feedback.mapper.SyntaxFeedbackMapper;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackGenerator {

    private SyntaxFeedbackMapper syntaxFeedbackMapper;

    private MarkdownFeedbackFormatter markdownFeedbackFormatter;

    public List<String> generateFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
        List<SyntaxFeedback> allDefaultSyntaxFeedbacks = getAllDefaultSyntaxFeedbacks(syntaxSetting.getLanguage());

        var FeedbacksByErrorKeys =
                allDefaultSyntaxFeedbacks.stream()
                        .collect(Collectors.groupingBy(SyntaxFeedback::getErrorKey));

        var filteredFeedbacks = filterFeedbacks(errors, FeedbacksByErrorKeys);

        if (syntaxFeedbackMapper == null) syntaxFeedbackMapper = new SyntaxFeedbackMapper();
        // naked feedbacks
        var feedbacks = syntaxFeedbackMapper.mapSyntaxFeedbackToFeedback(filteredFeedbacks);
        // adapted by check level naked feedbacks
        feedbacks = adaptFeedbackByCheckLevel(syntaxSetting, feedbacks);
        // formatted feedbacks
        feedbacks = formatFeedbacks(feedbacks);
        // build feedback in templates


        return Collections.emptyList();
    }

    private List<String> buildFeedbackInTemplate(List<Feedback> feedbacks) {
        AtomicInteger counter = new AtomicInteger(0);
        return feedbacks.stream().map(feedback -> {
           return new StringBuilder()
                   .append(String.format("%s %02d:","...",counter.get())).toString();
        }).collect(Collectors.toList());
    }

    private List<Feedback> formatFeedbacks(List<Feedback> feedbacks) {
        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(feedbacks);
    }

    private List<Feedback> adaptFeedbackByCheckLevel(SyntaxSetting syntaxSetting, List<Feedback> feedbacks) {
        if(syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return  feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    private List<SyntaxFeedback> getAllDefaultSyntaxFeedbacks(String language) {
        var dirPath = FeedbackFileDirectoryProvider.provide(SyntaxChecker.class);
        DefaultSyntaxFeedbackProvider defaultSyntaxFeedbackProvider = new DefaultSyntaxFeedbackProvider();
        return defaultSyntaxFeedbackProvider.provide(dirPath, language + ".json");
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
                                    .errorCause(error.getErrorMessage())
                                    .errorKey(error.getErrorCode())
                                    .errorLocation(ErrorLocation.builder().build())
                                    .build();
                    filteredSyntaxFeedbacks.add(syntaxFeedback);
                }
            } else {
                filteredSyntaxFeedbacks.addAll(byErrorCode);
            }
            filteredSyntaxFeedbacks.forEach(
                    sf -> sf.setErrorLocation(
                            ErrorLocation.builder()
                                    .startLine(error.getLine())
                                    .methodName("")
                                    .fileName(error.getFileName().equals("TestClass.java") ? "" : error.getFileName())
                                    .build()
                    ));
        }
        return filteredSyntaxFeedbacks;
    }


}
