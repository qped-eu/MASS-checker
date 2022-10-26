package eu.qped.framework.feedback.gerator;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackFileDirectoryProvider;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedback;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackMapper;
import eu.qped.framework.feedback.defaultjsonfeedback.DefaultJsonFeedbackProvider;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.utils.FileExtensions;

import java.util.List;

public abstract class AbstractFeedbackGenerator<REPORT,SETTING> {

    private DefaultJsonFeedbackMapper defaultJsonFeedbackMapper;

    private MarkdownFeedbackFormatter markdownFeedbackFormatter;
    private DefaultJsonFeedbackProvider defaultJsonFeedbackProvider;

    public abstract List<Feedback> generateFeedbacks(List<REPORT> reportEntries, SETTING checkerSetting);

    private List<Feedback> formatFeedbacks(List<Feedback> feedbacks) {
        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(feedbacks);
    }

    private List<DefaultJsonFeedback> getAllDefaultSyntaxFeedbacks(String language,Class checkerName) {
        var dirPath = DefaultJsonFeedbackFileDirectoryProvider.provideFeedbackDataFile(checkerName);
        if (defaultJsonFeedbackProvider == null) {
            defaultJsonFeedbackProvider = new DefaultJsonFeedbackProvider();
        }
        return defaultJsonFeedbackProvider.provide(dirPath, language + FileExtensions.JSON);
    }



}
