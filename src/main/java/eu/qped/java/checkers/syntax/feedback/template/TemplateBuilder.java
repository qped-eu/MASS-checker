package eu.qped.java.checkers.syntax.feedback.template;

import eu.qped.framework.feedback.*;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
import lombok.Builder;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.*;


@Builder
public class TemplateBuilder {

    private AtomicInteger counter = new AtomicInteger(0);
    private TemplateTextProvider templateTextProvider;

    // EN


    public List<String> buildFeedbacksInTemplate(List<Feedback> feedbacks, String language) {
        return feedbacks.stream().map(feedback -> buildFeedbackInTemplate(feedback, language)).collect(Collectors.toList());
    }

    private String buildFeedbackInTemplate(Feedback feedback, String language) {
        var templateTextByLanguage = getTemplateKeyWords(language);
        String feedbackHints = getFeedbackHints(feedback);
        String feedbackReference = getFormattedFeedbackReference(feedback.getReference(), templateTextByLanguage);


//        feedback.getCheckerName();
//        feedback.getReadableCause();
//        feedback.getHints();
//        feedback.getTechnicalCause();
//        feedback.getReference();
        feedback.getType();
        feedback.getErrorLocation();


        return asHeading4(String.format("%s %02d:", templateTextByLanguage.get(feedback.getCheckerName()), counter.incrementAndGet())) +
                NEW_Double_LINE +

                feedback.getReadableCause() +
                NEW_Double_LINE +
                feedbackHints +
                feedbackReference +

                HORIZONTAL_RULE;
    }

    private static String getFeedbackHints(Feedback feedback) {
        StringBuilder feedbackHints = new StringBuilder();
        if (feedback.getHints() == null) feedback.setHints(Collections.emptyList());
        for (var hint : feedback.getHints()) {
            feedbackHints.append(hint).append(NEW_Double_LINE);
        }
        return feedbackHints.toString();
    }

    private String getFormattedFeedbackReference(ConceptReference conceptReference, Map<String, String> templateTextByLanguage) {
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
                    + NEW_Double_LINE;
        }
        return result;
    }

    private Map<String, String> getTemplateKeyWords(String language) {
        var dirPath = FeedbackFileDirectoryProvider.provide(TemplateBuilder.class);
        if (templateTextProvider == null) {
            templateTextProvider = new TemplateTextProvider();
        }
        return templateTextProvider.provide(language);
    }


    public static void main(String[] args) {
        var hint = Hint.builder()
                .type(HintType.CODE_EXAMPLE)
                .content("int i = 0;")
                .build();
        var errorLocation = ErrorLocation.builder()
                .methodName("getName")
                .fileName("TestClass.java")
                .startLine(2)
                .build();
        var reference = ConceptReference.builder()
                .referenceName("folie 1")
                .referenceLink("www.google.com")
                .section("Sektion 1")
                .pageNumbers(List.of(1,2,11))
                .build();

        Feedback feedback = Feedback.builder()
                .checkerName(SyntaxChecker.class.getSimpleName())
                .type(Type.CORRECTION)
                .readableCause("du Hast Simcolen  vergessen")
                .technicalCause("';' expect")
                .hints(List.of(hint))
                .errorLocation(errorLocation)
                .reference(reference)
                .build();
        var templateBuilder = TemplateBuilder.builder().build();
        var result =templateBuilder.buildFeedbacksInTemplate(List.of(feedback),SupportedLanguages.GERMAN);
        System.out.println(result);

    }


}
