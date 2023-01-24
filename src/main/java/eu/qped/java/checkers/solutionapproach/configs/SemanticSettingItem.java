package eu.qped.java.checkers.solutionapproach.configs;

import eu.qped.framework.feedback.taskspecificfeedback.TaskSpecificFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticSettingItem {

    private String filePath;

    private String methodName;

    private Boolean recursive;

    private Integer whileLoop;

    private Integer forLoop;

    private Integer forEachLoop;

    private Integer ifElseStmt;

    private Integer doWhileLoop;

    private String returnType;

    private List<TaskSpecificFeedback> taskSpecificFeedbacks;

}
