package eu.qped.java.checkers.solutionApproach;


import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
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

    private SolutionApproachAnalyser solutionApproachAnalyser;
    private QfSemanticSettings qfSemanticSettings;
    private String targetProjectPath;

    public List<Feedback> check() {
        if (solutionApproachAnalyser == null) {
            solutionApproachAnalyser = SolutionApproachAnalyser
                    .builder()
                    .qfSemanticSettings(qfSemanticSettings)
                    .targetProjectPath(targetProjectPath)
                    .build();
        }
        solutionApproachAnalyser.check();


//        if (syntaxFeedbackGenerator == null) {
//            syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
//        }
//        return syntaxFeedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
        return Collections.emptyList();
    }


}
