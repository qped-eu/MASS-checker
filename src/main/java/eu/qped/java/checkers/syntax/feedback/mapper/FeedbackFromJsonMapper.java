package eu.qped.java.checkers.syntax.feedback.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class FeedbackFromJsonMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SyntaxFeedback> map(@NonNull String jsonFilePath) {
        // src/main/resources/syntax/en.json
        var result = new ArrayList<SyntaxFeedback>();
        try {
            result =
                    objectMapper.readValue(jsonFilePath, new TypeReference<>() {
                    });
        } catch (JsonProcessingException | IllegalArgumentException | NullPointerException e) {
            // todo sauberes Exception handling
            System.out.println(e.getMessage());
        }
        return result;
    }

}
