package eu.qped.java.checkers.syntax;

import java.util.List;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.syntax.analyser.SyntaxAnalysisReport;
import eu.qped.java.checkers.syntax.analyser.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.SupportedLanguages;
import eu.qped.java.utils.compiler.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private SyntaxAnalysisReport analyseReport;

    public List<String> check() {
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

    public List<String> check(Compiler compiler) {
        buildSyntaxSettings();
        if (syntaxErrorAnalyser == null) {
            syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .classFilesDestination(classFilesDestination)
                    .targetProject(targetProject)
                    .stringAnswer(stringAnswer)
                    .compiler(compiler)
                    .build();
        }
        analyseReport = syntaxErrorAnalyser.check();

        if (syntaxFeedbackGenerator == null) {
            syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
        }
        return syntaxFeedbackGenerator.generateFeedbacks(analyseReport.getSyntaxErrors(), syntaxSetting);
    }

    private void buildSyntaxSettings() {
        if (syntaxSetting == null) {
            syntaxSetting = SyntaxSetting.builder().build();
        }
        if (syntaxSetting.getLanguage() == null) {
            syntaxSetting.setLanguage(SupportedLanguages.ENGLISH);
        }
        if (syntaxSetting.getCheckLevel() == null) {
            syntaxSetting.setCheckLevel(CheckLevel.BEGINNER);
        }
    }


    public static void main(String[] args) {

        String codeToCompile = "double krt(double A, double k, double d){\n" +
                "    int K,L;\n" +
                "    return  A\n" +
                "}\n" +
                "\n" +
                "double krtH(double a, double k, double d, double x_n){\n" +
                "    return a;\n" +
                "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        syntaxChecker.setStringAnswer(codeToCompile);
        var setting = SyntaxSetting.builder().checkLevel(CheckLevel.BEGINNER).language("en").build();
        syntaxChecker.setSyntaxSetting(setting);
        var feedbacks = syntaxChecker.check();

        feedbacks.forEach(e -> System.out.println(e));

    }

}
