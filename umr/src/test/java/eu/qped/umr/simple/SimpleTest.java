package eu.qped.umr.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import eu.qped.umr.CheckerRunner;
import eu.qped.umr.SimpleChecker;

class SimpleTest {

	@Test
	void test() throws Exception {
		String input = "{\n"
				+ "   \"attemptCount\":1,\n"
				+ "   \"feedback\":[\n"
				+ "      \n"
				+ "   ],\n"
				+ "   \"showSolution\":false,\n"
				+ "   \"user\":{\n"
				+ "      \"id\":\"5fd0de0f30795372827b0f2a\",\n"
				+ "      \"firstName\":\"Christoph\",\n"
				+ "      \"lastName\":\"Bockisch\"\n"
				+ "   },\n"
				+ "   \"assignment\":{\n"
				+ "      \"id\":\"604f7d7ea2a93773c04b7c1a\",\n"
				+ "      \"title\":\"How low can you go?\"\n"
				+ "   },\n"
				+ "   \"block\":{\n"
				+ "      \"id\":\"604f7d93a2a93756b64b7c2b\",\n"
				+ "      \"text\":\"Write something that contains \\\"simple\\\".\",\n"
				+ "      \"solution\":\"simple\",\n"
				+ "      \"programmingLanguage\":\"java\"\n"
				+ "   },\n"
				+ "   \"answers\":[\n"
				+ "      \"this is simple\"\n"
				+ "   ],\n"
				+ "   \"answer\":\"this is simple\",\n"
				+ "   \"checkerClass\":\"" + SimpleChecker.class.getName() + "\""
				+ "}";
		
		CheckerRunner runner = new CheckerRunner(input);
		runner.check();
		Assertions.assertTrue(runner.getQfObject().isConditionSatisfied("myCondition"));
		Assertions.assertEquals(runner.getQfObject().getMessage("myMessage"), "The answer is 14 characters long.");
	}

}
