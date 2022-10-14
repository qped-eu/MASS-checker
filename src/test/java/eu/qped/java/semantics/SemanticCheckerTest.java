package eu.qped.java.semantics;

import eu.qped.java.checkers.mass.QfSemanticSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import eu.qped.java.checkers.semantics.SemanticChecker;
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
                                        .filePath("tmp/code-example-for-sematnic-testing-fail")
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
                                        .filePath("tmp/code-example-for-sematnic-testing-pass")
                                        .build()
                        )
                )
                .build();
    }


}
