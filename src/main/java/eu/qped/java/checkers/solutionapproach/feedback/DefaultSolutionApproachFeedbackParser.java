package eu.qped.java.checkers.solutionapproach.feedback;


import eu.qped.framework.feedback.RelatedLocation;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackDirectoryProvider;
import eu.qped.framework.feedback.defaultfeedback.DefaultFeedbackParser;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.utils.FileExtensions;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultSolutionApproachFeedbackParser {

    DefaultFeedbackParser defaultFeedbackParser;

    public List<DefaultSolutionApproachFeedback> parse(String language) {
        var dirPath = DefaultFeedbackDirectoryProvider.provideDefaultFeedbackDirectory(SolutionApproachChecker.class);
        if (defaultFeedbackParser == null) {
            defaultFeedbackParser = new DefaultFeedbackParser();
        }
        var allDefaultFeedbacks = defaultFeedbackParser.parse(dirPath, language + FileExtensions.JSON);
        return allDefaultFeedbacks.stream().map(defaultFeedback -> DefaultSolutionApproachFeedback.builder()
                .defaultFeedback(defaultFeedback)
                .relatedLocation(RelatedLocation.builder().build())
                .build()
        ).collect(Collectors.toList());
    }
}
