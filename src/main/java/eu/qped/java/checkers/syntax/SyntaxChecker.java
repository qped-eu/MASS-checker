package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.syntax.feedback.fromatter.MarkdownFeedbackFormatter;
import eu.qped.java.checkers.syntax.feedback.generator.FeedbackGenerator;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyntaxChecker {

    private SyntaxSetting syntaxSetting;
    private SyntaxErrorAnalyser syntaxErrorAnalyser;
    private FeedbackGenerator feedbackGenerator;



    private String classFilesDestination;
    private String stringAnswer;
    private String targetProject;

    public List<String> check() {

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
        return feedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
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
