package eu.qped.framework.feedback;

import eu.qped.framework.feedback.defaultfeedback.StoredFeedback;
import eu.qped.framework.feedback.fromatter.KeyWordReplacer;
import eu.qped.framework.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.template.TemplateBuilder;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * representation of a feedback for the student. <br/>
 * This model must be generated in all checkers.
 *
 * @author Omar Aji
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    /**
     * Represents the Checker component that generated this feedback.
     */
    private String checkerName;
    /**
     * Contains the system designation of the relevant error for which this feedback is generated
     */
    @NonNull
    private String technicalCause;
    /**
     * Provides information whether the error for which this feedback is generated needs to be corrected or improvement.
     */
    private Type type;
    /**
     * This feedback component focuses on the error in the submitted solution. <br/>
     * Thus, this field contains information about the cause of the detected error, which must be understandable for the student. <br/>
     * This feedback component named <b>knowledge about mistakes [KM]</b> in @see <a href="https://www.waxmann.com/waxmann-buecher/?no_cache=1&tx_p2waxmann_pi2%5Bbuchnr%5D=1641&tx_p2waxmann_pi2%5Baction%5D=show&tx_p2waxmann_pi2%5Bcontroller%5D=Buch&cHash=70094cfe0fbda759e008598052dbe275">Narciss, Susanne. Informatives tutorielles Feedback</a>
     */
    @NonNull
    private String readableCause;
    /**
     * This feedback component also focuses on the errors in a submitted solution. <br/>
     * Hints are given here in the form of feedback.
     * The hints give the student the opportunity to make corrections and are not intended to explain the cause of the error. <br/>
     * This feedback component named <b>Knowledge on how to proceed [KH]</b> in @see <a href="https://www.waxmann.com/waxmann-buecher/?no_cache=1&tx_p2waxmann_pi2%5Bbuchnr%5D=1641&tx_p2waxmann_pi2%5Baction%5D=show&tx_p2waxmann_pi2%5Bcontroller%5D=Buch&cHash=70094cfe0fbda759e008598052dbe275">Narciss, Susanne. Informatives tutorielles Feedback</a>
     */
    @Nullable
    private List<Hint> hints;
    /**
     * This feedback component focuses on the concept that a task should test. In return, a reference to the concept is given to the student to read through. <br/>
     * This feedback component named <b>Knowledge about concepts [KC]</b> in @see <a href="https://www.waxmann.com/waxmann-buecher/?no_cache=1&tx_p2waxmann_pi2%5Bbuchnr%5D=1641&tx_p2waxmann_pi2%5Baction%5D=show&tx_p2waxmann_pi2%5Bcontroller%5D=Buch&cHash=70094cfe0fbda759e008598052dbe275">Narciss, Susanne. Informatives tutorielles Feedback</a>
     */
    @Nullable
    private ConceptReference reference;
    /**
     * A part of Feedback contains for which location this feedback related to
     */
    @Nullable
    private RelatedLocation relatedLocation;

    @Setter(AccessLevel.PACKAGE)
    private boolean isFormatted = false;

    public static class FeedbackBuilder {
        public void updateFeedback(@NonNull StoredFeedback storedFeedback) {
            this.technicalCause = storedFeedback.getTechnicalCause();
            this.readableCause = storedFeedback.getReadableCause();
            if (storedFeedback.getHints() != null) {
                this.hints = storedFeedback.getHints();
            }
        }

        public void replaceKeyWords(@NonNull Object replacement) {
            var keyWordReplacer = KeyWordReplacer.builder().build();
            this.readableCause = keyWordReplacer.replace(this.readableCause, replacement);
            if (this.hints != null) {
                this.hints = this.hints.stream().peek(hint ->
                        hint.setContent(keyWordReplacer.replace(
                                hint.getContent(),
                                replacement
                        ))
                ).collect(Collectors.toList());
            }
        }
    }

    public List<Hint> getHints() {
        return hints == null ? Collections.emptyList() : hints.stream().map(Hint::clone).collect(Collectors.toList());
    }

    protected Feedback format() {
        MarkdownFeedbackFormatter markdownFeedbackFormatter = MarkdownFeedbackFormatter.builder().build();
        return markdownFeedbackFormatter.format(this);
    }

    protected String buildFeedbackInTemplate(@NonNull String language) {
        TemplateBuilder templateBuilder = TemplateBuilder.builder().build();
        return templateBuilder.buildFeedbackInTemplate(this, language);
    }


}
