package eu.qped.framework.feedback.defaultjsonfeedback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.utils.FileExtensions;
import eu.qped.java.utils.SupportedLanguages;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DefaultJsonFeedbackProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // TODO abstract to get all data for all checkers.
    public List<DefaultJsonFeedback> provide(@NonNull String dir, @NonNull String fileName) {
        var result = new ArrayList<DefaultJsonFeedback>();
        try {
            String filePath = dir + fileName;
            String defaultFilePath = dir + SupportedLanguages.ENGLISH + FileExtensions.JSON;
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
