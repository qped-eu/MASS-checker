package eu.qped.java.checkers.syntax.feedback.template;

import eu.qped.framework.feedback.Feedback;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Builder
public class templateBuilder {

    private AtomicInteger counter = new AtomicInteger(0);

    // EN
    private final static String EN_TITLE_SYNTAX = "Syntax error";
    private final static String EN_MORE_INFORMATION = "for more information:";
    private final static String EN_AT = "at";
    private final static String EN_PAGE = "pages";

    // DE
    private final static String DE_TITLE_SYNTAX = "Syntaktische Fehler";
    private final static String DE_MORE_INFORMATION = "Mehr erfahren:";
    private final static String DE_AT = "at";
    private final static String DE_PAGE = "pages";

    public List<String> buildFeedbacksInTemplate(List<Feedback> feedbacks, String language) {
        return feedbacks.stream().map(feedback -> buildFeedbackInTemplate(feedback, language)).collect(Collectors.toList());
    }

    private String buildFeedbackInTemplate(Feedback feedback, String language) {

        
        return new StringBuilder()
                .append(MarkdownFormatterUtility.asHeading2(String.format("%s %02d:", EN_TITLE_SYNTAX, counter.incrementAndGet())))
                .append(MarkdownFormatterUtility.NEW_Double_LINE)
                .append(feedback.getReadableCause())
                .append(MarkdownFormatterUtility.NEW_Double_LINE)
                .append(feedback.getHints().stream().map(hint -> hint.getContent() + MarkdownFormatterUtility.NEW_Double_LINE))
                .append(String.format("%s"))
                .toString();
    }


}
