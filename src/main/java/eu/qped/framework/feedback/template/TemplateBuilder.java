package eu.qped.framework.feedback.template;

import eu.qped.framework.feedback.*;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TemplateBuilder {

    private final static AtomicInteger counter = new AtomicInteger(0);
    private TemplateTextProvider templateTextProvider;

    public List<String> buildFeedbacksInTemplate(List<Feedback> feedbacks, String language) {
        return feedbacks.stream().map(feedback -> buildFeedbackInTemplate(feedback, language)).collect(Collectors.toList());
    }

    private String buildFeedbackInTemplate(Feedback feedback, String language) {
        var templateTextByLanguage = getTemplateKeyWords(language);
        String feedbackHeader = getTemplateFormattedHeader(feedback, templateTextByLanguage);
        String feedbackRelatedLocation = getTemplateFormattedRelatedLocation(feedback.getRelatedLocation(), templateTextByLanguage);
        String feedbackCause = getTemplateFormattedCause(feedback.getReadableCause());
        String feedbackHints = getTemplateFormattedHints(feedback.getHints());
        String feedbackReference = getTemplateFormattedReference(feedback.getReference(), templateTextByLanguage);
        return "" +
//            feedbackHeader +
                feedbackRelatedLocation +
                feedbackCause +
                feedbackHints +
                feedbackReference +
                HORIZONTAL_RULE;
    }

    private String getTemplateFormattedCause(String cause) {
//        return cause +
//                NEW_LINE;
        return Arrays.stream(cause.split(NEW_LINE))
                .map(String::trim)
                .collect(Collectors.joining(DOT+NEW_LINE)) + DOT + NEW_LINE;
    }

    private String getTemplateFormattedHeader(Feedback feedback, Map<String, String> templateTextByLanguage) {
        return asHeading4(String.format("%s %02d %s:"
                , templateTextByLanguage.get(feedback.getCheckerName())
                , counter.incrementAndGet()
                , templateTextByLanguage.get(String.valueOf(feedback.getType()))
        )) +
//                NEW_LINE;
                DOT
                + NEW_Double_LINE;
    }

    private String getTemplateFormattedHints(List<Hint> hints) {
        StringBuilder feedbackHints = new StringBuilder();
        if (hints == null) hints = Collections.emptyList();
        for (var hint : hints) {
            feedbackHints.append(hint.getContent()).append(NEW_LINE);
        }
        return feedbackHints.toString();
    }

    private String getTemplateFormattedRelatedLocation(RelatedLocation location, Map<String, String> templateTextByLanguage) {
        String result = "";
        if (location == null) return result;
        if (!location.getFileName().equals("")) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_IN)
                    + SPACE + location.getFileName()
                    + DOT + SPACE;
        }
        if (location.getMethodName() != null && !location.getMethodName().equals("")) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_METHOD)
                    + SPACE + location.getMethodName()
                    + DOT + SPACE;
        }
        if (location.getStartLine() != 0 && location.getEndLine() != 0) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_BETWEEN_LINES)
                    + SPACE + location.getStartLine()
                    + SPACE + templateTextByLanguage.get(TemplateTextProvider.KEY_AND)
                    + SPACE + location.getEndLine()
                    + DOT + SPACE;
        } else if (location.getStartLine() != 0) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_LINE)
                    + SPACE + location.getStartLine()
                    + DOT + SPACE;
        }
//        result += NEW_Double_LINE;
        result += NEW_LINE;
//        result += DOT + NEW_Double_LINE;
        return result;
    }

    private String getTemplateFormattedReference(ConceptReference conceptReference, Map<String, String> templateTextByLanguage) {
        String result = "";
        if (conceptReference != null && !conceptReference.equals("")) {
            boolean hasSection = conceptReference.getSection() != null && !conceptReference.getSection().equals("");
            boolean hasPages = conceptReference.getPageNumbers() != null && conceptReference.getPageNumbers().size() != 0;
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_MORE_INFORMATION)
                    + SPACE
                    + asBold(asLink(conceptReference.getReferenceName(), conceptReference.getReferenceLink()))
                    + SPACE
                    + (hasSection ?
                    templateTextByLanguage.get(TemplateTextProvider.KEY_AT)
                            + SPACE
                            + asBold(conceptReference.getSection())
                            + SPACE
                    : "")
                    + (hasPages ?
                    templateTextByLanguage.get(TemplateTextProvider.KEY_PAGE)
                            + SPACE
                            + StringUtils.join(conceptReference.getPageNumbers(), ",")
                            + SPACE
                    : "")
                    + NEW_LINE;
        }
        return result;
    }

    private Map<String, String> getTemplateKeyWords(String language) {
        var dirPath = DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory(TemplateBuilder.class);
        if (templateTextProvider == null) {
            templateTextProvider = new TemplateTextProvider();
        }
        return templateTextProvider.provide(language);
    }


    public static void main(String[] args) {
        var hint = Hint.builder()
                .type(HintType.CODE_EXAMPLE)
                .content(asCodeBlock("int i = 0;", CODE_JAVA))
                .build();
        var errorLocation = RelatedLocation.builder()
                .methodName("getName")
                .fileName("TestClass.java")
                .startLine(2)
                .build();
        var reference = ConceptReference.builder()
                .referenceName("folie 1")
                .referenceLink("www.google.com")
                .section("Sektion 1")
                .pageNumbers(List.of(1, 2, 11))
                .build();

        Feedback feedback = Feedback.builder()
                .checkerName(SyntaxChecker.class.getSimpleName())
                .type(Type.CORRECTION)
                .readableCause("du Hast Simcolen  vergessen")
                .technicalCause("';' expect")
                .hints(List.of(hint))
                .relatedLocation(errorLocation)
                .reference(reference)
                .build();
        var templateBuilder = TemplateBuilder.builder().build();
        var result = templateBuilder.buildFeedbacksInTemplate(List.of(feedback), SupportedLanguages.ENGLISH);
        System.out.println(result.get(0));

    }


}
