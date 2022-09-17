package eu.qped.java.checkers.syntax.feedback.generator;

import eu.qped.framework.feedback.Feedback;
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

    // field realize mocking
    private FeedbackFromJsonMapper jsonMapper;
    private FeedbackMapper feedbackMapper;

    private final static String EN_FEEDBACKS_FILE_PATH = "src/main/resources/syntax/en.json";
    private final static String DE_FEEDBACKS_FILE_PATH = "src/main/resources/syntax/de.json";

    public List<Feedback> generate(List<SyntaxError> errors, SyntaxSetting syntaxSetting) {

        var language = syntaxSetting.getLanguage();
        var level = syntaxSetting.getCheckLevel();
        if (jsonMapper == null) jsonMapper = new FeedbackFromJsonMapper();

        List<SyntaxFeedback> rawSyntaxFeedbacks = getAllRawSyntaxFeedbacksFromJson(language);

        var keyToFeedbacks =
                rawSyntaxFeedbacks.stream()
                        .collect(Collectors.groupingBy(SyntaxFeedback::getErrorKey));

        var filteredFeedbacks = filterFeedbacks(errors, keyToFeedbacks);

        var nakedFeedbacks = feedbackMapper.mapFromJsonFeedback(filteredFeedbacks);

        return Collections.emptyList();
    }

    private List<SyntaxFeedback> filterFeedbacks(List<SyntaxError> errors, Map<String, List<SyntaxFeedback>> keyToFeedbacks) {
        List<SyntaxFeedback> filteredSyntaxFeedbacksFromJson = new ArrayList<>();
        for (SyntaxError error : errors) {
            var byErrorCode = keyToFeedbacks.get(error.getErrorCode());
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
                                    .build();
                    filteredSyntaxFeedbacksFromJson.add(syntaxFeedback);
                }
            } else {
                filteredSyntaxFeedbacksFromJson.addAll(byErrorCode);
            }
        }
        return filteredSyntaxFeedbacksFromJson;
    }

    private List<SyntaxFeedback> getAllRawSyntaxFeedbacksFromJson(String language) {
        List<SyntaxFeedback> syntaxFeedbacks = new ArrayList<>();
        if ("en".equalsIgnoreCase(language)) {
            syntaxFeedbacks = jsonMapper.map(EN_FEEDBACKS_FILE_PATH);
        } else if ("de".equalsIgnoreCase(language)) {
            syntaxFeedbacks = jsonMapper.map(DE_FEEDBACKS_FILE_PATH);
        }
        return syntaxFeedbacks;
    }

}
