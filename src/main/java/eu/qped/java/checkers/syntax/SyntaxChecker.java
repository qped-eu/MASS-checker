package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.SupportedLanguages;
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
    private SyntaxFeedbackGenerator syntaxFeedbackGenerator;


    private String classFilesDestination;
    private String stringAnswer;
    private String targetProject;

    private SyntaxCheckReport analyseReport;

    public List<Feedback> check() {
        buildSyntaxSettings();
        if (syntaxErrorAnalyser == null) {
            syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .classFilesDestination(classFilesDestination)
                    .targetProject(targetProject)
                    .stringAnswer(stringAnswer)
                    .build();
        }
        analyseReport = syntaxErrorAnalyser.check();

        if (syntaxFeedbackGenerator == null) {
            syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
        }
        return syntaxFeedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
    }

    private void buildSyntaxSettings() {
        if (syntaxSetting == null ) {
            syntaxSetting = SyntaxSetting.builder().build();
        }
        if(syntaxSetting.getLanguage() == null) {
            syntaxSetting.setLanguage(SupportedLanguages.ENGLISH);
        }
        if(syntaxSetting.getCheckLevel() == null) {
            syntaxSetting.setCheckLevel(CheckLevel.BEGINNER);
        }
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
