package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackFileDirectoryProvider;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackMapper;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackProvider;
import eu.qped.framework.feedback.gerator.AbstractFeedbackGenerator;
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
@AllArgsConstructor
@Builder
public class SyntaxFeedbackGenerator extends AbstractFeedbackGenerator<SyntaxError,SyntaxSetting> {


    @Override
    protected List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks,SyntaxSetting syntaxSetting) {
        if (syntaxSetting.getCheckLevel().equals(CheckLevel.BEGINNER)) {
            return feedbacks;
        } else {
            return feedbacks.stream().peek(feedback -> feedback.setHints(Collections.emptyList())).collect(Collectors.toList());
        }
    }

    @Override
    protected String getFeedbackLanguage(SyntaxSetting syntaxSetting) {
        return syntaxSetting.getLanguage();
    }

    @Override
    protected Class<?> getCheckerName() {
        return SyntaxChecker.class;
    }

    @Override
    protected List<DefaultJsonFeedback> filterFeedbacks(List<SyntaxError> errors, Map<String, List<DefaultJsonFeedback>> allDefaultJsonFeedbacksByTechnicalCause) {
        List<DefaultJsonFeedback> filteredDefaultJsonFeedbacks = new ArrayList<>();
        for (SyntaxError error : errors) {
            var byErrorCode =
                    allDefaultJsonFeedbacksByTechnicalCause.get(error.getErrorCode());
            if (byErrorCode == null) {
                var byErrorMessage = allDefaultJsonFeedbacksByTechnicalCause.get(error.getErrorMessage());
                if (byErrorMessage != null) {
                    filteredDefaultJsonFeedbacks.addAll(byErrorMessage);
                } else {
                    var syntaxFeedback =
                            DefaultJsonFeedback.builder()
                                    .hints(Collections.emptyList())
                                    .readableCause(error.getErrorMessage())
                                    .technicalCause(error.getErrorCode())
                                    .relatedLocation(RelatedLocation.builder().build())
                                    .build();
                    filteredDefaultJsonFeedbacks.add(syntaxFeedback);
                }
            } else {
                filteredDefaultJsonFeedbacks.addAll(byErrorCode);
            }
            filteredDefaultJsonFeedbacks.forEach(
                    sf -> sf.setRelatedLocation(
                            RelatedLocation.builder()
                                    .startLine((int) error.getLine())
                                    .methodName("")
                                    .fileName(error.getFileName().equals("TestClass.java") ? "" : error.getFileName())
                                    .build()
                    ));
        }
        return filteredDefaultJsonFeedbacks;
    }

//    public List<Feedback> generateFeedbacks(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {
//        //TODO abstract
//        List<DefaultJsonFeedback> allDefaultJsonFeedbacks = getAllDefaultSyntaxFeedbacks(syntaxSetting.getLanguage(),SyntaxChecker.class);
//
//        var allDefaultJsonFeedbacksByTechnicalCause =
//                allDefaultJsonFeedbacks.stream()
//                        .collect(Collectors.groupingBy(DefaultJsonFeedback::getTechnicalCause));
//
//        var filteredFeedbacks = filterFeedbacks(errors, allDefaultJsonFeedbacksByTechnicalCause);
//
//        if (defaultJsonFeedbackMapper == null) defaultJsonFeedbackMapper = new DefaultJsonFeedbackMapper();
//        // naked feedbacks
//        var feedbacks = defaultJsonFeedbackMapper.mapSyntaxFeedbackToFeedback(filteredFeedbacks);
//        // adapted by check level naked feedbacks
//        feedbacks = adaptFeedbackByCheckLevel(syntaxSetting, feedbacks);
//        // formatted feedbacks
//        return formatFeedbacks(feedbacks);
//    }

//    private List<Feedback> formatFeedbacks(List<Feedback> feedbacks) {
//        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
//        return markdownFeedbackFormatter.format(feedbacks);
//    }

//    private List<DefaultJsonFeedback> getAllDefaultSyntaxFeedbacks(String language) {
//        var dirPath = DefaultJsonFeedbackFileDirectoryProvider.provideFeedbackDataFile(SyntaxChecker.class);
//        if (defaultJsonFeedbackProvider == null) {
//            defaultJsonFeedbackProvider = new DefaultJsonFeedbackProvider();
//        }
//        return defaultJsonFeedbackProvider.provide(dirPath, language + FileExtensions.JSON);
//    }



}
