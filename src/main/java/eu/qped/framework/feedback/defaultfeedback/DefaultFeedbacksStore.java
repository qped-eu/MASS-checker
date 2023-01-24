package eu.qped.framework.feedback.defaultfeedback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.framework.feedback.ConceptReference;
import eu.qped.framework.feedback.taskspecificfeedback.TaskSpecificFeedback;
import eu.qped.java.utils.FileExtensions;
import eu.qped.java.utils.SupportedLanguages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DefaultFeedbacksStore {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<DefaultFeedback> defaultFeedbacks;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private List<TaskSpecificFeedback> taskSpecificFeedbacks;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private boolean hasTaskSpecificFeedbacks = false;

    @NonNull
    private String storeDirectory;
    @NonNull
    private String storeFileName;


    public DefaultFeedbacksStore(@NonNull String storeDirectory, @NonNull String storeFileName) {
        this.storeDirectory = storeDirectory;
        this.storeFileName = storeFileName;
        defaultFeedbacks = parse(storeDirectory, storeFileName);
    }

    private List<DefaultFeedback> parse(@NonNull String dir, @NonNull String fileName) {
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


    public DefaultFeedback getRelatedDefaultFeedbackByTechnicalCause(String technicalCause) {
        DefaultFeedback.DefaultFeedbackBuilder resultBuilder = DefaultFeedback.builder();
        resultBuilder.readableCause(null);

        var relatedDefaultFeedback = defaultFeedbacks.stream()
                .filter(defaultFeedback -> defaultFeedback.getTechnicalCause().equals(technicalCause))
                .findFirst();

        if (relatedDefaultFeedback.isPresent()) {
            resultBuilder.readableCause(relatedDefaultFeedback.get().getReadableCause());
            resultBuilder.technicalCause(technicalCause);
            resultBuilder.hints(relatedDefaultFeedback.get().getHints());
        }
        if (isHasTaskSpecificFeedbacks()) {
            var relatedTaskSpecificFeedback = taskSpecificFeedbacks.stream()
                    .filter(taskSpecificFeedback -> taskSpecificFeedback.getTechnicalCause().equals(technicalCause))
                    .findFirst();

            if (relatedTaskSpecificFeedback.isPresent()) {
                resultBuilder.technicalCause(technicalCause);
                if (!StringUtils.isEmpty(relatedTaskSpecificFeedback.get().getReadableCause())) {
                    resultBuilder.readableCause(relatedTaskSpecificFeedback.get().getReadableCause());
                }
                if (Objects.nonNull(relatedTaskSpecificFeedback.get().getHints()) && !relatedTaskSpecificFeedback.get().getHints().isEmpty()) {
                    resultBuilder.hints(relatedTaskSpecificFeedback.get().getHints());
                }
            }
        }
        var result = resultBuilder.build();
        return (!StringUtils.isEmpty(result.getTechnicalCause())) ? result : null;
    }

    public ConceptReference getConceptReference(String technicalCause) {
        ConceptReference result = null;
        if (isHasTaskSpecificFeedbacks()) {
            var relatedTaskSpecificFeedback = taskSpecificFeedbacks.stream()
                    .filter(taskSpecificFeedback -> taskSpecificFeedback.getTechnicalCause().equals(technicalCause))
                    .findFirst();
            if (relatedTaskSpecificFeedback.isPresent()) {
                result = relatedTaskSpecificFeedback.get().getConceptReference();
            }
        }
        return result;
    }


    public void customizeStore(@NonNull List<TaskSpecificFeedback> taskSpecificFeedbacks) {
        setTaskSpecificFeedbacks(taskSpecificFeedbacks);
        setHasTaskSpecificFeedbacks(true);
    }

    public void rebuildStoreToDefault() {
        setTaskSpecificFeedbacks(null);
        setHasTaskSpecificFeedbacks(false);
    }


}
