package eu.qped.java.checkers.syntax;

import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.syntax.feedback.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.checkers.syntax.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.syntax.feedback.generator.FeedbackGenerator;
import eu.qped.java.checkers.syntax.feedback.mapper.FeedbackMapper;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyntaxChecker {

    private AbstractSyntaxFeedbackGenerator abstractSyntaxFeedbackGenerator;
    private FeedbackGenerator feedbackGenerator;
    private MarkdownFeedbackFormatter markdownFeedbackFormatter;
    private SyntaxErrorAnalyser syntaxErrorAnalyser;
    private SyntaxSetting syntaxSetting;
    @NonNull
    private String targetProject;

    private String stringAnswer;
    private String classFilesDestination;

    private FeedbackMapper feedbackMapper;


    public List<Feedback> check() {

        if (syntaxErrorAnalyser == null) {
            syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .syntaxSetting(syntaxSetting)
                    .classFilesDestination(classFilesDestination)
                    .targetProject(targetProject)
                    .stringAnswer(stringAnswer)
                    .build();
        }
        var analyseReport = syntaxErrorAnalyser.check();

        var nakedFeedbacks = feedbackGenerator.generate(analyseReport.getSyntaxErrors(), syntaxSetting);

        return markdownFeedbackFormatter.format(nakedFeedbacks);
    }

}
