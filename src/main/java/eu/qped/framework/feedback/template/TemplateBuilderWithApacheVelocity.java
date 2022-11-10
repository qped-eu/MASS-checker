package eu.qped.framework.feedback.template;

import eu.qped.framework.feedback.*;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.CODE_JAVA;
import static eu.qped.java.utils.markdown.MarkdownFormatterUtility.asCodeBlock;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TemplateBuilderWithApacheVelocity {

    private final static AtomicInteger counter = new AtomicInteger(0);
    private TemplateTextProvider templateTextProvider;

    public List<String> buildFeedbacksInTemplate(List<Feedback> feedbacks, String language) {
        List<String> result = new ArrayList<>();
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init();

            String templateFilePath = Paths.get("src/main/java/eu/qped/framework/feedback/template/" + language + "_template.vm").toString();


            Template template = velocityEngine.getTemplate(templateFilePath);

            VelocityContext context = new VelocityContext();
            context.put("feedbacks", feedbacks);
            context.put("templateText",getTemplateKeyWords(language));
            context.put("MarkdownUtility", MarkdownFormatterUtility.class);

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            result.add(writer.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
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

        feedback.getReadableCause();
        var templateBuilder = TemplateBuilderWithApacheVelocity.builder().build();
        var result = templateBuilder.buildFeedbacksInTemplate(List.of(feedback), SupportedLanguages.ENGLISH);
        System.out.println(result.get(0));

    }



}
