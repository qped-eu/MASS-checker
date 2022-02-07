package eu.qped.umr.simple;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import eu.qped.umr.CheckerRunner;
import eu.qped.umr.TrueChecker;

class QfDeserializeTest {
	
	public static final String CONDITION = "myCondition";
	
	@Test
	void testStandardQfObject() throws Exception {
		
		CheckerRunner runner = new CheckerRunner(
				FileUtils.readFileToString(new File("Qf-Object-2022-Jan.json"), Charset.defaultCharset()),
				new TrueChecker(CONDITION));
		runner.check();
		Assertions.assertTrue(runner.getQfObject().isConditionSatisfied(CONDITION));
	}


	@Test
	void testCustomQfObject() throws Exception {
		CheckerRunner runner = new CheckerRunner(
				FileUtils.readFileToString(new File("Qf-Object-2022-Jan-CustomFields.json"), Charset.defaultCharset()),
				new TrueChecker(CONDITION));
		runner.check();
		Assertions.assertTrue(runner.getQfObject().isConditionSatisfied("myCondition"));
	}

}
