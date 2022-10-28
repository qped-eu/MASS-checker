package eu.qped.framework.feedback.gerator;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackFileDirectoryProvider;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackMapper;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackProvider;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.checkerabstract.AbstractReportEntry;
import eu.qped.java.checkers.checkerabstract.AbstractSetting;
import eu.qped.java.utils.FileExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractFeedbackGenerator<REPORT extends AbstractReportEntry, SETTING extends AbstractSetting> {
    // check if interface  or abstract class ??
    // MUSS generic with abstract (report and setting)
    // FIXME rename provider (e.g. parser)
    private DefaultJsonFeedbackProvider defaultJsonFeedbackProvider;
    private DefaultJsonFeedbackMapper defaultJsonFeedbackMapper;
    private MarkdownFeedbackFormatter markdownFeedbackFormatter;


    /**
     * to generate Feedbacks we use for all checkers a template pattern
     *
     * @param reportEntries
     * @param checkerSetting
     * @return
     */
    public List<Feedback> generateFeedbacks(List<REPORT> reportEntries, SETTING checkerSetting) {
        String language = getFeedbackLanguage(checkerSetting);
        Class<?> checker = getCheckerName();
        List<DefaultJsonFeedback> allDefaultJsonFeedbacks = getAllDefaultSyntaxFeedbacks(language, checker);
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

    protected abstract String getFeedbackLanguage(SETTING checkerSetting);

    protected abstract Class<?> getCheckerName();

    protected abstract List<DefaultJsonFeedback> filterFeedbacks(List<REPORT> reportEntries, Map<String, List<DefaultJsonFeedback>> allDefaultJsonFeedbacksByTechnicalCause);

    protected abstract List<Feedback> adaptFeedbackByCheckerSetting(List<Feedback> feedbacks, SETTING checkerSetting);


}
