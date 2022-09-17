package eu.qped.framework.feedback.hint;

import eu.qped.framework.feedback.ErrorLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hint {

    private Set<HintType> hintTypes;
    private String hintContent;

    public String translate(String s) {
        return s;
    }


    public String generate(ErrorLocation location, String code, String externalHintContent) {


        // error ist da -> error message -> naked feedback content
        // -> translated feedback content -> tempalated Feedback content -> markdown formatted feedback content

        var feedbackContent = "";

        // custom
        feedbackContent = externalHintContent;

        // if we need to translate
        feedbackContent = translate(feedbackContent);


        // intern
        Map<String, String> errorCodeFeedback = new HashMap<>();

        errorCodeFeedback.put(
                "error404-en", "Feedback-EN"
        );
        errorCodeFeedback.put(
                "error404-de", "Feedback-DE"
        );

        feedbackContent = errorCodeFeedback.get(code);


        var feedback = String.format("in der Klasse %s und das Feedback ist: %s  ", location.getClassName(), feedbackContent);


        return "";
    }
}
