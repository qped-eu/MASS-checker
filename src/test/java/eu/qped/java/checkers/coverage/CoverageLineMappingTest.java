package eu.qped.java.checkers.coverage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.qped.java.checkers.mass.QfCoverageSettings;

class CoverageLineMappingTest {
	
	@Test
	void testCase1() throws IOException {
		genericTest("case1");
	}

	@Test
	void testCase2() throws IOException {
		genericTest("case2");
	}

	@Test
	void testCase3() throws IOException {
		genericTest("case3");
	}

	@Test
	void testCase4() throws IOException {
		genericTest("case4");
	}

	public void genericTest(String caseName) throws IOException, JsonProcessingException, JsonMappingException {
		String settingsJson = FileUtils.readFileToString(new File("src/test/resources/coverage/" + caseName + "/CoverageSettings.json"), Charset.defaultCharset());
		ObjectMapper mapper = new ObjectMapper();
		QfCoverageSettings covSetting = mapper.readValue(settingsJson, QfCoverageSettings.class);

		String answer = FileUtils.readFileToString(new File("src/test/resources/coverage/" + caseName + "/answer.java"), Charset.defaultCharset());
		
        CoverageSetup coverageSetup = new CoverageSetup(null, covSetting.getPrivateImplementation(), answer, null);

        CoverageChecker checker = new CoverageChecker(covSetting, coverageSetup);
        String[] messages = checker.check();
        String actual = Stream.of(messages).collect(Collectors.joining("\n"));

		String expected = FileUtils.readFileToString(new File("src/test/resources/coverage/" + caseName + "/expected.txt"), Charset.defaultCharset());

		assertEquals(expected, actual);
	}

}
