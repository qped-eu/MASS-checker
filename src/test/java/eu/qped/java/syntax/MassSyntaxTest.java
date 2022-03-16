package eu.qped.java.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.syntax.SyntaxError;
import eu.qped.java.checkers.syntax.SyntaxErrorChecker;

class MassSyntaxTest {

	private MainSettings mainSettingsConfiguratorConf;

	@BeforeEach
	public void setup() {
		Map<String, String> mainSettings = new HashMap<>();
		mainSettings.put("semanticNeeded", "false");
		mainSettings.put("syntaxLevel", CheckLevel.ADVANCED.name());
		mainSettings.put("preferredLanguage", "en");
		mainSettings.put("styleNeeded", "false");

		mainSettingsConfiguratorConf = new MainSettings(mainSettings);

	}
	
	@Test
	void testMethodNoError() {

		String code = " void rec (){\n" + "        System.out.println(\"pretty\");\n" + "    }";

		SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(code);

		MassExecutor massE = new MassExecutor(null, null, syntaxErrorChecker,
				mainSettingsConfiguratorConf);

		massE.execute();

		assertEquals(0, massE.getSyntaxErrors().size());
	}

	@Test
	void testMethodMissingSemicolon() {

		String code = "void rec (){\n"
				+ "System.out.println(\"pretty\")\n"
				+ "}";

		SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(code);

		MassExecutor massE = new MassExecutor(null, null, syntaxErrorChecker,
				mainSettingsConfiguratorConf);

		massE.execute();

		assertEquals(1, massE.getSyntaxErrors().size());
		SyntaxError error = massE.getSyntaxErrors().get(0);
		assertEquals("compiler.err.expected", error.getErrorCode());
	}

	@Test
	void testClassNoError() {

		String code = "class Simple {}";

		SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(code);

		MassExecutor massE = new MassExecutor(null, null, syntaxErrorChecker,
				mainSettingsConfiguratorConf);

		massE.execute();

		assertEquals(0, massE.getSyntaxErrors().size());
	}
}
