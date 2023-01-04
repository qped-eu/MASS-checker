package eu.qped.java.checkers.syntax;

import java.io.File;
import java.util.List;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.syntax.analyser.SyntaxAnalysisReport;
import eu.qped.java.checkers.syntax.analyser.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.SupportedLanguages;
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

    private File targetProject;

    private SyntaxAnalysisReport analyseReport;

    public List<String> check() {
        buildSyntaxSettings();
        if (syntaxErrorAnalyser == null) {
            syntaxErrorAnalyser = SyntaxErrorAnalyser
                    .builder()
                    .solutionRoot(targetProject)
                    .level(syntaxSetting.getCheckLevel())
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

}
