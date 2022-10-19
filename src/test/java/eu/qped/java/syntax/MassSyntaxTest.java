package eu.qped.java.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.framework.feedback.Feedback;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QfMainSettings;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.feedback.model.SyntaxFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testMethodNoError() {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();

        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getSyntaxFeedbacks().size());
    }

    @Test
    void testMethodMissingSemicolon() {

        String code = "void rec (){\n"
                + "System.out.println(\"pretty\")\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();

        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(1, massE.getSyntaxFeedbacks().size());
        Feedback feedback = massE.getSyntaxFeedbacks().get(0);
        assertEquals("';' expected", feedback.getTechnicalCause());
    }

    @Test
    void testClassNoError() {

        String code = "class Simple {}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();
        MassExecutor massE = new MassExecutor(null, null, syntaxChecker,
                null, null, null, mainSettingsConfiguratorConf);


        massE.execute();

        assertEquals(0, massE.getSyntaxFeedbacks().size());
    }
}
