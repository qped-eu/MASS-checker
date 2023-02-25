package eu.qped.framework.feedback.template;

import eu.qped.framework.feedback.ConceptReference;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultfeedback.StoredFeedbackDirectoryProvider;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static eu.qped.framework.QpedQfFilesUtility.DEFAULT_ANSWER_CLASS;
import static eu.qped.framework.feedback.template.TemplateTextProvider.KEY_CODE_EXAMPLE;
import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateBuilder {

    private final static AtomicInteger counter = new AtomicInteger(0);
    private TemplateTextProvider templateTextProvider;

    public List<String> buildFeedbacksInTemplate(@NonNull List<Feedback> feedbacks, @NonNull String language) {
        return feedbacks.stream().map(feedback -> buildFeedbackInTemplate(feedback, language)).collect(Collectors.toList());
    }

    public String buildFeedbackInTemplate(Feedback feedback, String language) {
        var templateTextByLanguage = getTemplateKeyWords(language);
        String feedbackHeader = getTemplateFormattedHeader(feedback, templateTextByLanguage);
        String feedbackRelatedLocation = getTemplateFormattedRelatedLocation(feedback.getRelatedLocation(), templateTextByLanguage);
        String feedbackCause = getTemplateFormattedCause(feedback.getReadableCause());
        String feedbackHints = getTemplateFormattedHints(feedback.getHints(), templateTextByLanguage);
        String feedbackReference = getTemplateFormattedReference(feedback.getReference(), templateTextByLanguage);
        return "" +
                feedbackHeader +
                feedbackRelatedLocation +
                feedbackCause +
                feedbackHints +
                feedbackReference +
                NEW_Double_LINE +
                HORIZONTAL_RULE;
    }

    private String getTemplateFormattedCause(String cause) {
        return Arrays.stream(cause.split(NEW_LINE))
                .map(String::trim)
                .collect(Collectors.joining( NEW_LINE)) + NEW_LINE;
    }

    private String getTemplateFormattedHeader(Feedback feedback, Map<String, String> templateTextByLanguage) {
        return asHeading4(String.format("%s %02d %s:"
                , templateTextByLanguage.get(feedback.getCheckerName())
                , counter.incrementAndGet()
                , templateTextByLanguage.get(String.valueOf(feedback.getType()))
        ))
                + NEW_Double_LINE;
    }

    private String getTemplateFormattedHints(List<Hint> hints, Map<String, String> templateTextByLanguage) {
        StringBuilder feedbackHints = new StringBuilder();
        if (hints == null) hints = Collections.emptyList();
        for (var hint : hints) {
            if (hint.getType().equals(HintType.CODE_EXAMPLE)) {
                feedbackHints
                        .append(NEW_Double_LINE)
                        .append(templateTextByLanguage.get(KEY_CODE_EXAMPLE))
                        .append(NEW_Double_LINE)
                        .append(hint.getContent())
                        .append(NEW_Double_LINE)
                ;
            } else {
                feedbackHints.append(hint.getContent()).append(NEW_Double_LINE);
            }
        }
        return feedbackHints.toString();
    }

    private String getTemplateFormattedRelatedLocation(RelatedLocation location, Map<String, String> templateTextByLanguage) {
        String result = "";
        if (location == null) return result;
        boolean isNotInDefaultAnswerClass = !location.getFileName().contains(DEFAULT_ANSWER_CLASS);
        if (StringUtils.isNotEmpty(location.getFileName()) && isNotInDefaultAnswerClass) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_IN)
                    + SPACE + location.getFileName()
                    + DOT + SPACE;
        }
        if (StringUtils.isNotEmpty(location.getMethodName())) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_METHOD)
                    + SPACE + location.getMethodName()
                    + DOT + SPACE;
        }
        if (location.getStartLine() != 0 && location.getEndLine() != 0 && location.getStartLine() != location.getEndLine()) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_BETWEEN_LINES)
                    + SPACE + (isNotInDefaultAnswerClass ? location.getStartLine() : location.getStartLine() - 1)
                    + SPACE + templateTextByLanguage.get(TemplateTextProvider.KEY_AND)
                    + SPACE + (isNotInDefaultAnswerClass ? location.getEndLine() : location.getEndLine() - 1)
                    + DOT + SPACE;
        } else if (location.getStartLine() != 0) {
            result += templateTextByLanguage.get(TemplateTextProvider.KEY_LINE)
                    + SPACE + (isNotInDefaultAnswerClass ? location.getStartLine() : location.getStartLine() - 1)
                    + DOT + SPACE;
        }
        if (StringUtils.isNotEmpty(result)) {
            result += NEW_Double_LINE;
        }
        return result;
    }

    private String getTemplateFormattedReference(ConceptReference conceptReference, Map<String, String> templateTextByLanguage) {
        String result = "";
        if (conceptReference != null) {
            boolean hasPages = conceptReference.getPageNumbers() != null && conceptReference.getPageNumbers().size() != 0;
            result += NEW_LINE
                    + templateTextByLanguage.get(TemplateTextProvider.KEY_MORE_INFORMATION)
                    + SPACE
                    + asBold(asLink(conceptReference.getReferenceName(), conceptReference.getReferenceLink()))
                    + SPACE
                    + (StringUtils.isNotEmpty(conceptReference.getSection()) ?
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
                    : "");
        }
        if (StringUtils.isNotEmpty(result)) {
            result += NEW_Double_LINE;
        }
        return result;
    }

    private Map<String, String> getTemplateKeyWords(String language) {
        var dirPath = StoredFeedbackDirectoryProvider.provideStoredFeedbackDirectory(TemplateBuilder.class);
        if (templateTextProvider == null) {
            templateTextProvider = new TemplateTextProvider();
        }
        return templateTextProvider.provide(language);
    }


//    public static void main(String[] args) {
//        var hint = Hint.builder()
//                .type(HintType.CODE_EXAMPLE)
//                .content(asCodeBlock("int i = 0;", CODE_JAVA))
//                .build();
//        var errorLocation = RelatedLocation.builder()
//                .methodName("getName")
//                .fileName("TestClass.java")
//                .startLine(2)
//                .build();
//        var reference = ConceptReference.builder()
//                .referenceName("folie 1")
//                .referenceLink("www.google.com")
//                .section("Sektion 1")
//                .pageNumbers(List.of(1, 2, 11))
//                .build();
//
//        Feedback feedback = Feedback.builder()
//                .checkerName(SyntaxChecker.class.getSimpleName())
//                .type(Type.CORRECTION)
//                .readableCause("du Hast Simcolen  vergessen")
//                .technicalCause("';' expect")
//                .hints(List.of(hint))
//                .relatedLocation(errorLocation)
//                .reference(reference)
//                .build();
//        var templateBuilder = TemplateBuilder.builder().build();
//        var result = templateBuilder.buildFeedbacksInTemplate(List.of(feedback), SupportedLanguages.ENGLISH);
//        System.out.println(result.get(0));
//
//    }


}
