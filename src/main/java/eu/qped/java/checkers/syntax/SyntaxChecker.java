package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.template.TemplateBuilder;
import eu.qped.java.checkers.syntax.feedback.FeedbackGenerator;
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
    private FeedbackGenerator feedbackGenerator;


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

        if (feedbackGenerator == null) {
            feedbackGenerator = FeedbackGenerator.builder().build();
        }
        return feedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
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

        String codeToCompile = "double krt(double a, double k, double d){\n" +
                "    retrun a;\n" +
                "}\n" +
                "\n" +
                "double krtH(double a, double k, double d, double x_n){\n" +
                "    return  a;\n" +
                "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        syntaxChecker.setStringAnswer(codeToCompile);
        var setting = SyntaxSetting.builder().checkLevel(CheckLevel.BEGINNER).language("en").build();
        syntaxChecker.setSyntaxSetting(setting);
        var feedbacks = syntaxChecker.check();
        TemplateBuilder templateBuilder = TemplateBuilder.builder().build();
        var test = templateBuilder.buildFeedbacksInTemplate(feedbacks,SupportedLanguages.ENGLISH);
        feedbacks.forEach(System.out::println);

    }

}
