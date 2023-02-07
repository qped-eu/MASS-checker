package eu.qped.java.checkers.solutionapproach;


import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.analyser.SolutionApproachAnalyser;
import eu.qped.java.checkers.solutionapproach.feedback.SolutionApproachFeedbackGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * checker for Solution Approach.
 *
 * @author Omar Aji
 * @version 1.0
 * @since 25.10.2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachChecker {

    //    @NonNull
    private QfSemanticSettings qfSemanticSettings;
    private SolutionApproachAnalyser solutionApproachAnalyser;
    private SolutionApproachFeedbackGenerator solutionApproachFeedbackGenerator;

    private String targetProjectPath;

    public List<String> check() {
        if (solutionApproachAnalyser == null) {
            solutionApproachAnalyser = SolutionApproachAnalyser
                    .builder()
                    .qfSemanticSettings(qfSemanticSettings)
                    .targetProjectPath(targetProjectPath)
                    .build();
        }
        var solutionApproachReportEntries = solutionApproachAnalyser.check();

        if (solutionApproachFeedbackGenerator == null) {
            solutionApproachFeedbackGenerator = SolutionApproachFeedbackGenerator.builder().build();
        }
        return solutionApproachFeedbackGenerator.generateFeedbacks(solutionApproachReportEntries, qfSemanticSettings);
    }


}
