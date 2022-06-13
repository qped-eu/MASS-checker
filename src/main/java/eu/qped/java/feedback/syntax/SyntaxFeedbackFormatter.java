package eu.qped.java.feedback.syntax;

import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.Builder;

import java.util.Arrays;
import java.util.stream.Collectors;

@Builder
public class SyntaxFeedbackFormatter {

    public SyntaxFeedback formatFeedback(SyntaxFeedback syntaxFeedback) {
        if (syntaxFeedback != null) {
            syntaxFeedback.setHeader(formatHeader(syntaxFeedback.getHeader()));
            syntaxFeedback.setFeedbackMessage(formatFeedbackMessage(syntaxFeedback.getFeedbackMessage()));
            syntaxFeedback.setErrorLine(formatErrorLine(syntaxFeedback.getErrorLine()));
            syntaxFeedback.setErrorSource(formatErrorSource(syntaxFeedback.getErrorSource()));
            syntaxFeedback.setSolutionExample(buildFeedbackSolutionExample(syntaxFeedback.getSolutionExample()));
        }
        return syntaxFeedback;
    }


    private String formatHeader(String feedbackHeader) {
        return (feedbackHeader != null && !feedbackHeader.equals("") ? ""
                + MarkdownFormatterUtility.asHeading2(feedbackHeader)
                : ""
        );
    }


    private String formatFeedbackMessage(String feedbackMessage) {
        return (feedbackMessage != null && !feedbackMessage.equals("") ? ""
                + Arrays.stream(feedbackMessage.split(MarkdownFormatterUtility.NEW_Double_LINE))
                .map(line -> MarkdownFormatterUtility.asBold(line.trim()))
                .collect(Collectors.joining(""))
                : ""
        );
    }

    private String formatErrorLine(String errorLine) {
        return (errorLine != null && !errorLine.equals("") ? ""
                + MarkdownFormatterUtility.asBold(errorLine)
                : ""
        );
    }


    private String formatErrorSource(String errorSource) {
        return (errorSource != null && !errorSource.equals("") ? ""
                + MarkdownFormatterUtility.asBold("Where this error happens:")
                + MarkdownFormatterUtility.asJavaCodeBlock(errorSource)
                : ""
        );
    }


    private String buildFeedbackSolutionExample(String solutionExample) {
        return (solutionExample != null && !solutionExample.equals("") ? ""
                + MarkdownFormatterUtility.asBold("Example to fix this error:")
                + MarkdownFormatterUtility.asJavaCodeBlock(solutionExample)
                : ""
        );
    }
}
