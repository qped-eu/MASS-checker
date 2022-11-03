package eu.qped.java.semantics;

import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.solutionapproach.configs.SemanticSettingItem;
import eu.qped.java.checkers.solutionapproach.analyser.SolutionApproachAnalyser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionApproachAnalyserTest {


    private SolutionApproachAnalyser solutionApproachAnalyser;

    @BeforeEach
    public void setup() {
        solutionApproachAnalyser = SolutionApproachAnalyser.builder().build();
    }

    @Test
    void testMethodNoErrorFail() {
        solutionApproachAnalyser.setQfSemanticSettings(qfSemanticSettingsFail());
        solutionApproachAnalyser.check();
        var fs = solutionApproachAnalyser.getFeedbacks();
        assertThat(fs).isNotEmpty();

        assertThat(fs).anyMatch(
                f ->
                        f.getBody().contains("you have used a Loop without a recursive Call, you have to solve it just recursive in tmp/code-example-for-sematnic-testing-fail/CalcSum.java")

        );
    }

    @Test
    void testMethodNoErrorPass() {
        solutionApproachAnalyser.setQfSemanticSettings(qfSemanticSettingsPass());
        solutionApproachAnalyser.check();
        var fs = solutionApproachAnalyser.getFeedbacks();
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
                                        .filePath("tmp/code-example-for-sematnic-testing-fail/CalcSum.java")
                                        .build()
                        )
                )
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
                                        .filePath("tmp/code-example-for-sematnic-testing-pass/CalcSum.java")
                                        .build()
                        )
                )
                .build();
    }


}
