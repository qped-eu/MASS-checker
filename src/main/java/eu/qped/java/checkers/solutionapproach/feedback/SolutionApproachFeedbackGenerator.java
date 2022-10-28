package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.gerator.AbstractFeedbackGenerator;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachReportEntry;
import eu.qped.java.checkers.solutionapproach.configs.FileSettingEntry;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator  {


    // solution approach general settings
    // solution approach setting item
    public List<Feedback> generateFeedbacks(List<SolutionApproachReportEntry> reportEntries, QfSemanticSettings settings) {

        return Collections.emptyList();
    }


}
