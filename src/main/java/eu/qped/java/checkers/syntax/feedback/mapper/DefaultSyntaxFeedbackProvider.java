package eu.qped.java.checkers.syntax.feedback.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DefaultSyntaxFeedbackProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SyntaxFeedback> provide(@NonNull String dir,  @NonNull String fileName) {
        var result = new ArrayList<SyntaxFeedback>();
        try {
            String filePath = dir + fileName;
            String defaultFilePath = dir + "en.json" ;
            File jsonFile;
            if(new File(filePath).getAbsoluteFile().exists()) {
                jsonFile = new File(filePath).getAbsoluteFile();
            } else {
                jsonFile = new File(defaultFilePath).getAbsoluteFile();
            }
            result = objectMapper.readValue(jsonFile, new TypeReference<>() {});
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

}
