package eu.qped.java.semantics;

import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SemanticCheckerTest {


    private SemanticChecker semanticChecker;

    @BeforeEach
    public void setup() {
        semanticChecker = SemanticChecker.builder().build();
    }

    @Test
    void testMethodNoErrorFail() {
        semanticChecker.setQfSemanticSettings(qfSemanticSettingsFail());
        semanticChecker.check();
        var fs = semanticChecker.getFeedbacks();
        assertThat(fs).isNotEmpty();

        assertThat(fs).anyMatch(
                f ->
                        f.getBody().contains("you have used a Loop without a recursive Call, you have to solve it just recursive in tmp/code-example-for-sematnic-testing-fail/CalcSum.java")

        );
    }

    @Test
    void testMethodNoErrorPass() {
        semanticChecker.setQfSemanticSettings(qfSemanticSettingsPass());
        semanticChecker.check();
        var fs = semanticChecker.getFeedbacks();
        assertThat(fs).isEmpty();
    }

    @Test
    void testMethodFail() {
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().build();
        syntaxChecker.setStringAnswer("String krt(double a, double k, double d){\n" +
                "    return \"\";\n" +
                "}\n" +
                "\n" +
                "double krtH(double a, double k, double d, double x_n){\n" +
                "    return a;\n" +
                "}");
        var syntaxFeedbacks = syntaxChecker.check();
        var syntaxAnalyseReport = syntaxChecker.getAnalyseReport();
        var settings= QfSemanticSettings
                .builder()
                .semantics(
                        List.of(
                                SemanticSettingItem
                                        .builder()
                                        .recursive(false)
                                        .whileLoop(0)
                                        .forLoop(0)
                                        .forEachLoop(0)
                                        .doWhileLoop(0)
                                        .ifElseStmt(-1)
                                        .returnType("double")
                                        .methodName("krt")
                                        .filePath("")
                                        .build()
                        )
                )
                .build();
        SemanticChecker semanticChecker = SemanticChecker.builder().qfSemanticSettings(settings).build();
        semanticChecker.setTargetProjectPath(syntaxAnalyseReport.getPath());
        semanticChecker.check();
        var semanticFeedbacks = semanticChecker.getFeedbacks();
        System.out.println(semanticFeedbacks);

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
