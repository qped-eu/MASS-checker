package eu.qped.java.semantics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.framework.CheckLevel;
import eu.qped.java.checkers.mass.MainSettings;
import eu.qped.java.checkers.mass.MassExecutor;
import eu.qped.java.checkers.mass.QFSemSettings;
import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.semantics.SemanticConfigurator;
import eu.qped.java.checkers.semantics.SemanticFeedback;
import eu.qped.java.checkers.syntax.SyntaxChecker;

class MassSemanticsTest {

	private MainSettings mainSettingsConfiguratorConf;
	private SemanticChecker semanticChecker;

	@BeforeEach
	public void setup() {
		Map<String, String> mainSettings = new HashMap<>();
		mainSettings.put("semanticNeeded", "true");
		mainSettings.put("syntaxLevel", CheckLevel.ADVANCED.name());
		mainSettings.put("preferredLanguage", "en");
		mainSettings.put("styleNeeded", "false");

		mainSettingsConfiguratorConf = new MainSettings(mainSettings);
		
		Map<String, String> semanticConf = new HashMap<>();

		semanticConf.put("methodName", "recR");
		semanticConf.put("recursionAllowed", "false");
		semanticConf.put("whereLoop", "-1");
		semanticConf.put("forLoop", "1");
		semanticConf.put("forEachLoop", "-1");
		semanticConf.put("ifElseStmt", "-1");
		semanticConf.put("doWhileLoop", "-1");
		semanticConf.put("returnType", "int");

		QFSemSettings qfSemSettings = new QFSemSettings();
		qfSemSettings.setMethodName("rec");
		qfSemSettings.setRecursionAllowed("true");
		qfSemSettings.setWhileLoop("-1");
		qfSemSettings.setForLoop("2");
		qfSemSettings.setForEachLoop("-1");
		qfSemSettings.setIfElseStmt("-1");
		qfSemSettings.setDoWhileLoop("-1");
		qfSemSettings.setReturnType("void");

		SemanticConfigurator semanticConfigurator = SemanticConfigurator.createSemanticConfigurator(qfSemSettings);
		semanticChecker = SemanticChecker.createSemanticMassChecker(semanticConfigurator);

	}
	
	@Test
	void testMethodNoError() {

		String code = "void rec(){\n"
				+ "System.out.println(\"pretty\");\n"
				+ "}";

		SyntaxChecker syntaxChecker = SyntaxChecker.builder().answer(code).build();


		MassExecutor massE = new MassExecutor(null, semanticChecker, syntaxChecker,
				mainSettingsConfiguratorConf);

		massE.execute();

		assertEquals(0, massE.getStyleFeedbacks().size());
		SemanticFeedback feedback = massE.getSemanticFeedbacks().get(0);
		assertEquals("you have to solve the method recursive", feedback.getBody());
	}

}
