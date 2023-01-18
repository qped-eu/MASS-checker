package eu.qped.framework.feedback.defaultfeedback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.java.utils.FileExtensions;
import eu.qped.java.utils.SupportedLanguages;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class DefaultFeedbacksStore {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<DefaultFeedback> defaultFeedbacks;

    @NonNull
    private String storeDirectory;
    @NonNull
    private String storeFileName;


    public DefaultFeedbacksStore(@NonNull String storeDirectory, @NonNull String storeFileName) {
        this.storeDirectory = storeDirectory;
        this.storeFileName = storeFileName;
        defaultFeedbacks = parse(storeDirectory, storeFileName);
    }

    // TODO: private method
    public List<DefaultFeedback> parse(@NonNull String dir, @NonNull String fileName) {
        List<DefaultFeedback> result = new ArrayList<DefaultFeedback>();
        try {
            String filePath = dir + fileName;
            String defaultFilePath = dir + SupportedLanguages.ENGLISH + FileExtensions.JSON;
            File jsonFile;
            if (new File(filePath).getAbsoluteFile().exists()) {
                jsonFile = new File(filePath).getAbsoluteFile();
            } else {
                jsonFile = new File(defaultFilePath).getAbsoluteFile();
            }
            result = objectMapper.readValue(jsonFile, new TypeReference<>() {
            });
        } catch (JsonMappingException exception) {
            if (!fileName.equals(SupportedLanguages.ENGLISH + FileExtensions.JSON)) {
                result = parse(dir, SupportedLanguages.ENGLISH + FileExtensions.JSON);
            }
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private Map<String, List<DefaultFeedback>> getDefaultFeedbacksByTechnicalCause() {
        return defaultFeedbacks.stream()
                .collect(Collectors.groupingBy(
                        DefaultFeedback::getTechnicalCause
                ))
                ;
    }

    public DefaultFeedback getRelatedDefaultFeedbackByTechnicalCause(String technicalCause) {
        var feedbacksByTechnicalCause = getDefaultFeedbacksByTechnicalCause();
        if (!feedbacksByTechnicalCause.containsKey(technicalCause)
                || feedbacksByTechnicalCause.get(technicalCause).size() == 0
        ) {
            return null;
        }
        return feedbacksByTechnicalCause.get(technicalCause).get(0);
    }


}
