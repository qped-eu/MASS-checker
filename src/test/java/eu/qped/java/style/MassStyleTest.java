package eu.qped.java.style;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QfMainSettings;
import eu.qped.java.checkers.mass.QfStyleSettings;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxErrorAnalyser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MassStyleTest {

    private MainSettings mainSettingsConfiguratorConf;

    private StyleChecker styleChecker;

    @BeforeEach
    public void setup() {

        QfMainSettings qfMainSettings = new QfMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("true");
        qfMainSettings.setPreferredLanguage("en");

        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QfStyleSettings qfStyleSettings = new QfStyleSettings();
        qfStyleSettings.setNamesLevel(CheckLevel.ADVANCED.name());
        qfStyleSettings.setMethodNamePattern("[a-z][a-zA-Z0-9_]*");


        styleChecker = StyleChecker.builder().qfStyleSettings(qfStyleSettings).build();

    }

    @Test
    void testMethodNoError() {

        String code = "void rec(){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxErrorAnalyser syntaxErrorAnalyser = SyntaxErrorAnalyser.builder().stringAnswer(code).build();
        MassExecutor massE = new MassExecutor(styleChecker, null, syntaxErrorAnalyser,
                null, null, mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getStyleFeedbacks().size());
    }

}
