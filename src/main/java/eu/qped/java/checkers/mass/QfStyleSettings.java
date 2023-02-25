package eu.qped.java.checkers.mass;

import eu.qped.framework.feedback.taskspecificfeedback.TaskSpecificFeedback;
import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QfStyleSettings extends QfObjectBase {


    private String basisLevel;
    private String complexityLevel;
    private String namesLevel;

    private String classLength;
    private String methodLength;
    private String cyclomaticComplexity;
    private String fieldsCount;

    private String variableNamePattern;
    private String methodNamePattern;
    private String methodParameterNamePattern;
    private String classNamePattern;

    private String language;
    private Boolean isCorrection;
    private String checkLevel;
    private List<TaskSpecificFeedback> taskSpecificFeedbacks;
}
