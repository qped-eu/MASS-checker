package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackFileDirectoryProvider;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackMapper;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackProvider;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.SolutionApproachGeneralSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachReportEntry;
import eu.qped.java.utils.FileExtensions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionApproachFeedbackGenerator {

    private DefaultJsonFeedbackProvider defaultJsonFeedbackProvider;
    private DefaultJsonFeedbackMapper defaultJsonFeedbackMapper;
    private MarkdownFeedbackFormatter markdownFeedbackFormatter;

    public List<Feedback> generateFeedbacks(List<SolutionApproachReportEntry> reportEntries, SolutionApproachGeneralSettings checkerSetting) {
        List<DefaultJsonFeedback> allDefaultJsonFeedbacks = getAllDefaultSyntaxFeedbacks(checkerSetting.getLanguage(), SolutionApproachChecker.class);
        var allDefaultJsonFeedbacksByTechnicalCause =
                allDefaultJsonFeedbacks.stream()
                        .collect(Collectors.groupingBy(DefaultJsonFeedback::getTechnicalCause));
        var filteredFeedbacks = filterFeedbacks(reportEntries, allDefaultJsonFeedbacksByTechnicalCause); // get related default json feedbacks
        if (defaultJsonFeedbackMapper == null) defaultJsonFeedbackMapper = new DefaultJsonFeedbackMapper();
        var feedbacks = defaultJsonFeedbackMapper.mapSyntaxFeedbackToFeedback(filteredFeedbacks); // map default json feedbacks to naked feedbacks
        feedbacks = adaptFeedbackByCheckerSetting(feedbacks, checkerSetting); // adapted naked feedbacks by check setting like check level
        return formatFeedbacks(feedbacks); // formatted feedbacks
    }

    protected List<DefaultJsonFeedback> getAllDefaultSyntaxFeedbacks(@NotNull String language, @NotNull Class<?> aClass) {
        var dirPath = DefaultJsonFeedbackFileDirectoryProvider.provideFeedbackDataFile(aClass);
        if (defaultJsonFeedbackProvider == null) {
            defaultJsonFeedbackProvider = new DefaultJsonFeedbackProvider();
        }
        return defaultJsonFeedbackProvider.provide(dirPath, language + FileExtensions.JSON);
    }

    protected List<Feedback> formatFeedbacks(@NotNull List<Feedback> feedbacks) {

        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(feedbacks);
    }


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


    protected List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, SolutionApproachGeneralSettings checkerSetting) {
        return feedbacks;
    }
}
