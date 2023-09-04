package eu.qped.java.checkers.mutation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QpedQfFilesUtility;
import eu.qped.framework.SystemTests;
import eu.qped.java.checkers.mass.QfMutationSettings;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutationLineMappingTest {
	
	@Test
	void testCase1() throws IOException {
		genericTest("case1", false);
	}

	@Test
	void testCase2() throws IOException {
		genericTest("case2", false);
	}

	@Test
	void testCase3() throws IOException {
		genericTest("case3", false);
	}

	@Test
	void testCase4() throws IOException {
		genericTest("case4", false);
	}

	@Test
	void testCase5() throws IOException {
		genericTest("case5", false);
	}

	@Test
	void testCase6() throws IOException {
		genericTest("case6", true);
	}

	public void genericTest(String caseName, boolean startsWithInsteadOfEquals) throws IOException, JsonProcessingException, JsonMappingException {
		File baseDirectory = new File("src/test/resources/mutation/" + caseName);
		String settingsJson = FileUtils.readFileToString(new File(baseDirectory, "MutationSettings.json"), Charset.defaultCharset());
		ObjectMapper mapper = new ObjectMapper();
		QfMutationSettings covSetting = mapper.readValue(settingsJson, QfMutationSettings.class);

		String answer = FileUtils.readFileToString(new File(baseDirectory,"answer.java"), Charset.defaultCharset());
		File privateImplFile = new File(baseDirectory,"PrivateImpl.zip");
		
		File solutionRoot = QpedQfFilesUtility.createManagedTempDirectory();
		QpedQfFilesUtility.createFileFromAnswerString(solutionRoot, answer);
    	File instructorRoot = QpedQfFilesUtility.downloadAndUnzipIfNecessary(FileInfo.createForUrl(privateImplFile.toURI().toURL().toExternalForm()));
    	FileUtils.copyDirectory(instructorRoot, solutionRoot);
    	
        SyntaxChecker syntaxChecker = SyntaxChecker.builder().
        		targetProject(solutionRoot).
        		build();
        List<String> syntaxMessages = syntaxChecker.check();
		assertTrue(syntaxMessages.isEmpty());
    	
        MutationChecker checker = new MutationChecker(covSetting, solutionRoot);
        List<String> messages = checker.check();
        String actual = messages.stream().collect(Collectors.joining("\n"));

		String expected = FileUtils.readFileToString(new File(baseDirectory, "expected.txt"), Charset.defaultCharset())
				.replaceAll("\\r\\n", "\n");  // Normalize Windows line endings to Unix/Linux;

		// filter out stack traces, as the exact trace (especially line numbers) may be depending
		// on the Java implementation
		expected = SystemTests.pruneStackTraces(expected);
		actual = SystemTests.pruneStackTraces(actual);

		
		if (startsWithInsteadOfEquals)
			assertTrue(actual.startsWith(expected));
		else
			assertEquals(expected, actual);
	}


}
