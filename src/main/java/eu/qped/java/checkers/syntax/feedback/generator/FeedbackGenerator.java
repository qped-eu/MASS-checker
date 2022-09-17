package eu.qped.java.checkers.syntax.feedback.generator;

import eu.qped.framework.feedback.ErrorLocation;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackFilePathProvider;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.feedback.mapper.FeedbackFromJsonMapper;
import eu.qped.java.checkers.syntax.feedback.mapper.FeedbackMapper;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FeedbackGenerator {

    // field enable mocking
    private FeedbackFromJsonMapper jsonMapper;
    private FeedbackMapper feedbackMapper;


    public List<Feedback> generate(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {

        var language = syntaxSetting.getLanguage();
        var level = syntaxSetting.getCheckLevel();
        if (jsonMapper == null) jsonMapper = new FeedbackFromJsonMapper();

        List<SyntaxFeedback> rawSyntaxFeedbacks = getAllRawSyntaxFeedbacksFromJson(language);

        var keyToFeedbacks =
                rawSyntaxFeedbacks.stream()
                        .collect(Collectors.groupingBy(SyntaxFeedback::getErrorKey));

        var filteredFeedbacks = filterFeedbacks(errors, keyToFeedbacks);

        if (feedbackMapper == null) feedbackMapper = new FeedbackMapper();
        return feedbackMapper.mapFromJsonFeedback(filteredFeedbacks);
    }

    private List<SyntaxFeedback> filterFeedbacks(List<SyntaxError> errors, Map<String, List<SyntaxFeedback>> keyToFeedbacks) {
        List<SyntaxFeedback> filteredSyntaxFeedbacksFromJson = new ArrayList<>();
        for (SyntaxError error : errors) {
            var byErrorCode =
                    keyToFeedbacks.get(error.getErrorCode());
            if (byErrorCode == null) {
                var byErrorMessage = keyToFeedbacks.get(error.getErrorMessage());
                if (byErrorMessage != null) {
                    filteredSyntaxFeedbacksFromJson.addAll(byErrorMessage);
                } else {
                    var syntaxFeedback =
                            SyntaxFeedback.builder()
                                    .hints(Collections.emptyList())
                                    .errorCause(error.getErrorMessage())
                                    .errorKey(error.getErrorCode())
                                    .errorLocation(ErrorLocation.builder().build())
                                    .build();
                    filteredSyntaxFeedbacksFromJson.add(syntaxFeedback);
                }
            } else {
                filteredSyntaxFeedbacksFromJson.addAll(byErrorCode);
            }
            filteredSyntaxFeedbacksFromJson.forEach(
                    sf -> sf.setErrorLocation(
                            ErrorLocation.builder()
                                    .startPosition(error.getStartPos())
                                    .endPosition(error.getEndPos())
                                    .className(error.getFileName())
                                    .build()
                    ));
        }
        return filteredSyntaxFeedbacksFromJson;
    }

    private List<SyntaxFeedback> getAllRawSyntaxFeedbacksFromJson(String language) {
        var dirPath = FeedbackFilePathProvider.provide(SyntaxChecker.class);
        return jsonMapper.map(dirPath + language + ".json");
    }

}
