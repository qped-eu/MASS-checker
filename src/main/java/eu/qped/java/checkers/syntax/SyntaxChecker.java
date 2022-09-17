package eu.qped.java.checkers.syntax;

import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.syntax.feedback.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.checkers.syntax.feedback.FeedbackMapper;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import lombok.*;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyntaxChecker {

    private AbstractSyntaxFeedbackGenerator syntaxFeedbackGenerator;
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

        if (syntaxFeedbackGenerator == null) {
            syntaxFeedbackGenerator = SyntaxFeedbackGenerator
                    .builder()
                    .build();
        }


        var syntaxFeedbacks = syntaxFeedbackGenerator
                .generateFeedbacks(analyseReport.getSyntaxErrors());
        // naked
        var feedbacks = feedbackMapper.map(syntaxFeedbacks);

        return Collections.emptyList();
    }

}
