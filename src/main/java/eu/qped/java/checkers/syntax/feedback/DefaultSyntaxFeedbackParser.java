package eu.qped.java.checkers.syntax.feedback;

import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackParser;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.FileExtensions;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultSyntaxFeedbackParser {
    DefaultFeedbackParser defaultFeedbackParser;

    public List<DefaultSyntaxFeedback> parse(String language) {
        var dirPath = DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory(SyntaxChecker.class);
        if (defaultFeedbackParser == null) {
            defaultFeedbackParser = new DefaultFeedbackParser();
        }
        var allDefaultFeedbacks = defaultFeedbackParser.parse(dirPath, language + FileExtensions.JSON);
        return allDefaultFeedbacks.stream().map(defaultFeedback -> DefaultSyntaxFeedback.builder()
                .defaultFeedback(defaultFeedback)
                .relatedLocation(RelatedLocation.builder().build())
                .build()
        ).collect(Collectors.toList());
    }
}
