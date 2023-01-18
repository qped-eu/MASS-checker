package eu.qped.framework.feedback.fromatter;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.NEW_LINE;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MarkdownFeedbackFormatter implements IFeedbackFormatter {


    @Override
    public List<Feedback> format(List<Feedback> feedbacks) {
        return feedbacks.stream().map(this::format).collect(Collectors.toList());
    }

    @Override
    public Feedback format(Feedback feedback) {
        feedback.setTechnicalCause(feedback.getTechnicalCause());
        feedback.setReadableCause(formatReadableCause(feedback.getReadableCause()));
        feedback.setType(feedback.getType());
        feedback.setRelatedLocation(feedback.getRelatedLocation());
        if (feedback.getHints() == null) feedback.setHints(Collections.emptyList());
        List<Hint> formattedHints = formatHints(feedback.getHints());
        feedback.setHints(formattedHints);
        feedback.setReference(feedback.getReference());
        feedback.setCheckerName(feedback.getCheckerName());
        return feedback;
    }

    private String formatReadableCause(String readableCause) {
        return Arrays.stream(readableCause.split(NEW_LINE)).map(String::trim).collect(Collectors.joining(NEW_LINE));
    }

    private List<Hint> formatHints(List<Hint> hints) {
        List<Hint> formattedHints = new ArrayList<>();
        hints.forEach(
                h -> {
                    if (h.getType() == HintType.TEXT) {
                        formattedHints.add(Hint.builder()
                                .content(String.valueOf(new Text(h.getContent())))
                                .type(HintType.TEXT)
                                .build());
                    } else if (h.getType() == HintType.CODE_EXAMPLE) {
                        formattedHints.add(Hint.builder()
                                .content(String.valueOf(new CodeBlock(h.getContent(), "java")))
                                .type(HintType.CODE_EXAMPLE)
                                .build());
                    }
                }
        );
        return formattedHints;
    }
}

