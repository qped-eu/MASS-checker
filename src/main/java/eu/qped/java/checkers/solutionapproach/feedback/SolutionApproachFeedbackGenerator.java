package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.gerator.AbstractFeedbackGenerator;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.SolutionApproachGeneralSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachReportEntry;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator extends AbstractFeedbackGenerator<SolutionApproachReportEntry, SolutionApproachGeneralSettings> {

    @Override
    protected String getFeedbackLanguage(SolutionApproachGeneralSettings checkerSetting) {
        return checkerSetting.getLanguage();
    }

    @Override
    protected Class<?> getCheckerName() {
        return SolutionApproachChecker.class;
    }

    @Override
    protected List<DefaultJsonFeedback> filterFeedbacks(List<SolutionApproachReportEntry> reportEntries, Map<String, List<DefaultJsonFeedback>> allDefaultJsonFeedbacksByTechnicalCause) {
        List<DefaultJsonFeedback> result = new ArrayList<>();
        for (SolutionApproachReportEntry solutionApproachReportEntry : reportEntries) {
            if (allDefaultJsonFeedbacksByTechnicalCause.containsKey(solutionApproachReportEntry.getErrorCode())) {
                result.add(DefaultJsonFeedback.builder()
                        .technicalCause(solutionApproachReportEntry.getErrorCode())
                        .readableCause(allDefaultJsonFeedbacksByTechnicalCause.get(solutionApproachReportEntry.getErrorCode()).get(0).getReadableCause())
                        .relatedLocation(RelatedLocation.builder()
                                .fileName(solutionApproachReportEntry.getRelatedSemanticSettingItem().getFilePath())
                                .methodName(solutionApproachReportEntry.getRelatedSemanticSettingItem().getMethodName())
                                .build()
                        )
                        .hints(Collections.emptyList())
                        .build()
                );
            }
        }
        return result;
    }

    @Override
    protected List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, SolutionApproachGeneralSettings checkerSetting) {
        return feedbacks;
    }
}
