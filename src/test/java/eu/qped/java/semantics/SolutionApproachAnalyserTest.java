package eu.qped.java.semantics;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.configs.SemanticSettingItem;
import eu.qped.java.utils.SupportedLanguages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionApproachAnalyserTest {


    private SolutionApproachChecker solutionApproachChecker;

    @BeforeEach
    public void setup() {
        var qfSetting = qfSemanticSettingsFail();
        solutionApproachChecker = SolutionApproachChecker.builder()
                .qfSemanticSettings(qfSetting)
                .build()
        ;
    }

    @Test
    void testMethodNoErrorFail() {
        solutionApproachChecker.setQfSemanticSettings(qfSemanticSettingsFail());
        var fs = solutionApproachChecker.check();
        assertThat(fs).isNotEmpty();

        assertThat(fs).anyMatch(
                f ->
                        f.contains("for loop")

        );
    }

    @Test
    void testMethodNoErrorPass() {
        solutionApproachChecker.setQfSemanticSettings(qfSemanticSettingsPass());
        var fs = solutionApproachChecker.check();
        assertThat(fs).isEmpty();
    }


    static QfSemanticSettings qfSemanticSettingsFail() {

        return QfSemanticSettings
                .builder()
                .semantics(
                        List.of(
                                SemanticSettingItem
                                        .builder()
                                        .recursive(true)
                                        .whileLoop(0)
                                        .forLoop(0)
                                        .forEachLoop(0)
                                        .doWhileLoop(0)
                                        .ifElseStmt(-1)
                                        .returnType("int")
                                        .methodName("calcSum")
                                        .filePath("src/test/resources/code-example-for-sematnic-testing-fail/CalcSum.java")
                                        .build()
                        )
                )
                .checkLevel(CheckLevel.BEGINNER)
                .language(Locale.ENGLISH.getLanguage())

                .build();
    }

    static QfSemanticSettings qfSemanticSettingsPass() {

        return QfSemanticSettings
                .builder()
                .semantics(
                        List.of(
                                SemanticSettingItem
                                        .builder()
                                        .recursive(true)
                                        .whileLoop(0)
                                        .forLoop(0)
                                        .forEachLoop(0)
                                        .doWhileLoop(0)
                                        .ifElseStmt(-1)
                                        .returnType("int")
                                        .methodName("calcSum")
                                        .filePath("src/test/resources/code-example-for-sematnic-testing-pass/CalcSum.java")
                                        .build()
                        )
                )
                .checkLevel(CheckLevel.BEGINNER)
                .language(SupportedLanguages.ENGLISH)
                .build();
    }


}
