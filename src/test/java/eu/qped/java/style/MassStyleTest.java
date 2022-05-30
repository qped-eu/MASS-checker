package eu.qped.java.style;

import static org.junit.jupiter.api.Assertions.assertEquals;


import eu.qped.java.checkers.mass.QFMainSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QFStyleSettings;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.style.StyleConfigurator;
import eu.qped.java.checkers.syntax.SyntaxChecker;

class MassStyleTest {

    private MainSettings mainSettingsConfiguratorConf;
    private StyleConfigurator styleConfigurator;
    private StyleChecker styleChecker;

    @BeforeEach
    public void setup() {

        QFMainSettings qfMainSettings = new QFMainSettings();
        qfMainSettings.setSyntaxLevel(CheckLevel.ADVANCED.name());
        qfMainSettings.setSemanticNeeded("false");
        qfMainSettings.setStyleNeeded("true");
        qfMainSettings.setPreferredLanguage("en");

        mainSettingsConfiguratorConf = new MainSettings(qfMainSettings);

        QFStyleSettings qfStyleSettings = new QFStyleSettings();
        qfStyleSettings.setNamesLevel(CheckLevel.ADVANCED.name());
        qfStyleSettings.setMethodName("[a-z][a-zA-Z0-9_]*");

        styleConfigurator = StyleConfigurator.createStyleConfigurator(qfStyleSettings);
        styleChecker = new StyleChecker(styleConfigurator);

    }

    @Test
    void testMethodNoError() {

        String code = "void rec(){\n"
                + "System.out.println(\"pretty\");\n"
                + "}";

        SyntaxChecker syntaxChecker = SyntaxChecker.builder().stringAnswer(code).build();

        MassExecutor massE = new MassExecutor(styleChecker, null, syntaxChecker,
                mainSettingsConfiguratorConf);

        massE.execute();

        assertEquals(0, massE.getStyleFeedbacks().size());
    }

}
