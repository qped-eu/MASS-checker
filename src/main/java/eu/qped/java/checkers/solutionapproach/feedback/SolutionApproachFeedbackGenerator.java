package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachReportEntry;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator {

    public List<Feedback> generateFeedbacks(List<SolutionApproachReportEntry> reportEntries, QfSemanticSettings settings) {

        return Collections.emptyList();
    }
}
