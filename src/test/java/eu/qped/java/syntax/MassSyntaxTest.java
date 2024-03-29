package eu.qped.java.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QfMainSettings;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MassSyntaxTest {

    private MainSettings mainSettingsConfiguratorConf;

    @BeforeEach
    public void setup() {
        QfMainSettings qfMainSettings = new QfMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("false");
        qfMainSettings.setPreferredLanguage("en");


        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

    }

    @Test
    void testMethodNoError() throws IOException {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        File solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        System.out.println(solutionRoot);
        QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, code);

        var syntaxSetting = SyntaxSetting.builder()
                .language(Locale.ENGLISH.getLanguage())
                .checkLevel(CheckLevel.BEGINNER)
                .build();
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject(solutionRoot).syntaxSetting(syntaxSetting).build();

        MassExecutor.builder().syntaxChecker(syntaxChecker).mainSettings(mainSettingsConfiguratorConf);
        MassExecutor massE = MassExecutor.builder().syntaxChecker(syntaxChecker).mainSettings(mainSettingsConfiguratorConf).build();

        massE.execute();

        assertEquals(0, massE.getSyntaxFeedbacks().size());
    }

    @Test
    void testMethodMissingSemicolon() throws IOException {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\")\n"
                + "}";

        File solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, code);

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject(solutionRoot).build();

        MassExecutor massE =
                MassExecutor.builder().syntaxChecker(syntaxChecker).mainSettings(mainSettingsConfiguratorConf).build();

        massE.execute();

        assertEquals(1, massE.getSyntaxFeedbacks().size());
        String feedback = massE.getSyntaxFeedbacks().get(0);
        assertTrue(feedback.toLowerCase().contains(";"));
    }

    @Test
    void testClassNoError() throws IOException {

        String code = "class Simple {}";

        File solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
        QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, code);

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().targetProject(solutionRoot).build();

        MassExecutor massE =
                MassExecutor.builder().syntaxChecker(syntaxChecker).mainSettings(mainSettingsConfiguratorConf).build();


        massE.execute();

        assertEquals(0, massE.getSyntaxFeedbacks().size());
    }
}
