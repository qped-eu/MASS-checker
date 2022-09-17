package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
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

        if (feedbackGenerator == null) {
            feedbackGenerator = new FeedbackGenerator();
        }
        var nakedFeedbacks = feedbackGenerator.generate(analyseReport.getSyntaxErrors(), syntaxSetting);

        if (markdownFeedbackFormatter == null) markdownFeedbackFormatter = new MarkdownFeedbackFormatter();
        return markdownFeedbackFormatter.format(nakedFeedbacks);
    }

    public int add(int num1, int num2) {
        int r = num1 + num2;
        return r;
    }

    public static void main(String[] args) {

        String codeToCompile = "   public int add(int num1, int num2){\n" +
                "        int r = num1 + num2;\n" +
                "        return r\n" +
                "    }";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        syntaxChecker.setStringAnswer(codeToCompile);
        var setting = SyntaxSetting.builder().checkLevel(CheckLevel.BEGINNER).language("en").build();
        syntaxChecker.setSyntaxSetting(setting);
        var feedbacks = syntaxChecker.check();

        feedbacks.forEach(System.out::println);

    }

}
