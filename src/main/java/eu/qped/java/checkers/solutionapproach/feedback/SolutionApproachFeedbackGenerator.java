package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.Type;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedback;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackMapper;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackParser;
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

    private DefaultFeedbackParser defaultFeedbackParser;
    private DefaultFeedbackMapper defaultFeedbackMapper;
    private MarkdownFeedbackFormatter markdownFeedbackFormatter;

    public List<Feedback> generateFeedbacks(List<SolutionApproachReportEntry> reportEntries, SolutionApproachGeneralSettings checkerSetting) {
        List<DefaultFeedback> allDefaultFeedbacks = getAllDefaultSyntaxFeedbacks(checkerSetting.getLanguage(), SolutionApproachChecker.class);
        var allDefaultJsonFeedbacksByTechnicalCause =
                allDefaultFeedbacks.stream()
                        .collect(Collectors.groupingBy(DefaultFeedback::getTechnicalCause));
        var filteredFeedbacks = filterFeedbacks(reportEntries, allDefaultJsonFeedbacksByTechnicalCause); // get related default json feedbacks
        if (defaultFeedbackMapper == null) defaultFeedbackMapper = new DefaultFeedbackMapper();
        var feedbacks = defaultFeedbackMapper.mapDefaultFeedbackToFeedback(filteredFeedbacks,SolutionApproachChecker.class, Type.CORRECTION); // map default json feedbacks to naked feedbacks
        feedbacks = adaptFeedbackByCheckerSetting(feedbacks, checkerSetting); // adapted naked feedbacks by check setting like check level
        return formatFeedbacks(feedbacks); // formatted feedbacks
    }

    protected List<DefaultFeedback> getAllDefaultSyntaxFeedbacks(@NotNull String language, @NotNull Class<?> aClass) {
        var dirPath = DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory(aClass);
        if (defaultFeedbackParser == null) {
            defaultFeedbackParser = new DefaultFeedbackParser();
        }
        return defaultFeedbackParser.parse(dirPath, language + FileExtensions.JSON);
    }

    protected List<Feedback> formatFeedbacks(@NotNull List<Feedback> feedbacks) {

        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(feedbacks);
    }


    protected List<DefaultFeedback> filterFeedbacks(List<SolutionApproachReportEntry> reportEntries, Map<String, List<DefaultFeedback>> allDefaultJsonFeedbacksByTechnicalCause) {
        List<DefaultFeedback> result = new ArrayList<>();
        for (SolutionApproachReportEntry solutionApproachReportEntry : reportEntries) {
            if (allDefaultJsonFeedbacksByTechnicalCause.containsKey(solutionApproachReportEntry.getErrorCode())) {
                result.add(DefaultFeedback.builder()
                        .technicalCause(solutionApproachReportEntry.getErrorCode())
                        .readableCause(allDefaultJsonFeedbacksByTechnicalCause.get(solutionApproachReportEntry.getErrorCode()).get(0).getReadableCause())
//                        .relatedLocation(RelatedLocation.builder()
//                                .fileName(solutionApproachReportEntry.getRelatedSemanticSettingItem().getFilePath())
//                                .methodName(solutionApproachReportEntry.getRelatedSemanticSettingItem().getMethodName())
//                                .build()
//                        )
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
