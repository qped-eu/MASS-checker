package eu.qped.java.checkers.syntax.feedback.template;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.FeedbackFileDirectoryProvider;
import eu.qped.java.utils.FileExtensions;
import eu.qped.java.utils.SupportedLanguages;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Builder
public class TemplateBuilder {

    private AtomicInteger counter = new AtomicInteger(0);
    private TemplateTextProvider templateTextProvider;

    // EN


    public List<String> buildFeedbacksInTemplate(List<Feedback> feedbacks, String language) {
        return feedbacks.stream().map(feedback -> buildFeedbackInTemplate(feedback, language)).collect(Collectors.toList());
    }

    private String buildFeedbackInTemplate(Feedback feedback, String language) {


        return new StringBuilder()
//                .append(MarkdownFormatterUtility.asHeading2(String.format("%s %02d:", EN_TITLE_SYNTAX, counter.incrementAndGet())))
                .append(MarkdownFormatterUtility.NEW_Double_LINE)
                .append(feedback.getReadableCause())
                .append(MarkdownFormatterUtility.NEW_Double_LINE)
                .append(feedback.getHints().stream().map(hint -> hint.getContent() + MarkdownFormatterUtility.NEW_Double_LINE))
                .append(String.format("%s"))
                .toString();
    }

    private TemplateKeyWords getTemplateKeyWords(String language) {
        var dirPath = FeedbackFileDirectoryProvider.provideFeedbackDataFile(TemplateBuilder.class);
        if (templateTextProvider == null) {
            templateTextProvider = new TemplateTextProvider();
        }
        return templateTextProvider.provideFromJson(dirPath, language + FileExtensions.JSON);
    }

    public static void main(String[] args) {
        TemplateBuilder templateBuilder = TemplateBuilder.builder().build();
        TemplateKeyWords templateKeyWords = templateBuilder.getTemplateKeyWords(SupportedLanguages.GERMAN);
        System.out.println(templateKeyWords);
    }


}
