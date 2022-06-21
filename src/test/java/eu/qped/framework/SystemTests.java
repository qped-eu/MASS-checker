package eu.qped.framework;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ro.skyah.comparator.JSONCompare;

public class SystemTests {
	
	// By setting this to true, the Checker runner is executed in the same process
	// as the system test runner. This can be used for debugging processes.
	public static final boolean IN_PROCESS = false;

	private static final String SYSTEM_TEST_CONF_YAML = "system-test-conf.yaml";
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
	private static final int TIMEOUT_AMOUNT = 30;
	private static final String QF_INPUT_FILE_NAME = "qf-input.json";
	private static final String QF_EXPECTED_FILE_NAME = "qf-expected.json";
	private static final String DESCRIPTION_FILE_NAME = "description.yaml";
	private static final String SYSTEM_TESTS_FOLDER_NAME = "system-tests";
	private static final File QF_OBJECT_FILE = new File("qf-copy.json");
	private static SystemTestConf systemTestConf;
	private static ObjectMapper yamlMapper;
	
	@BeforeAll
	public static void setup() throws StreamReadException, DatabindException, IOException {
		yamlMapper = new ObjectMapper(new YAMLFactory());
		systemTestConf = yamlMapper.readValue(SystemTests.class.getClassLoader().getResourceAsStream(SYSTEM_TEST_CONF_YAML), SystemTestConf.class);
	}

	@DisplayName("QPED Checkers System Tests")
	@ParameterizedTest(name = "{0}")
	@MethodSource("provideStringsSystemTest")
	public void systemTests(String name, SystemTestDescription description, String input, String expected, Exception exception) throws IOException, InterruptedException, AssertionError {
		if (exception != null) {
			throw new AssertionError(exception);
		} else {
			FileUtils.writeStringToFile(QF_OBJECT_FILE, input, Charset.defaultCharset());

			if (IN_PROCESS) {
				CheckerRunner.main(new String[0]);
			}
			else {
				ProcessBuilder pb = new ProcessBuilder(systemTestConf.getCloudCheckRuner()).directory(new File(".")).inheritIO();
				pb.environment().put("PATH", pb.environment().get("PATH") + File.pathSeparator + systemTestConf.getMavenLocation());
				Process process = pb.start();
				if (!process.waitFor(TIMEOUT_AMOUNT, TIMEOUT_UNIT)) {
					throw new AssertionError(new TimeoutException("Timeout expired: " + TIMEOUT_AMOUNT + " " + TIMEOUT_UNIT));
				}
			}
			
			String actual = FileUtils.readFileToString(QF_OBJECT_FILE, Charset.defaultCharset());
			JSONCompare.assertEquals(expected, actual);
		}

	}

	private static Stream<Arguments> provideStringsSystemTest() {
		File systemTestsFolder = new File(ClassLoader.getSystemResource(SYSTEM_TESTS_FOLDER_NAME).getPath());
		List<Arguments> arguments = new ArrayList<>();
		scanForSystemTests(systemTestsFolder.getPath(), systemTestsFolder, arguments);

		return arguments.stream().sorted((o1, o2) -> ((String) o1.get()[0]).compareTo(((String) o2.get()[0])));
	}

	private static void scanForSystemTests(String systemTestsFolderPath, File currentFolder,
			List<Arguments> arguments) {
		if (!currentFolder.isDirectory()) {
			return;
		}

		String name = currentFolder.getPath().substring(systemTestsFolderPath.length());
		String qfExpected = null;
		String qfInput = null;
		Exception exception = null;

		File descriptionFile = new File(currentFolder, DESCRIPTION_FILE_NAME);

		SystemTestDescription description = null;
		if (descriptionFile.exists()) {
			
			try {
				description  = yamlMapper.readValue(descriptionFile, SystemTestDescription.class);
				name = description.getTitle();
			} catch (IOException e) {
				exception = e;
			}
		}

		File qfExpectedFile = new File(currentFolder, QF_EXPECTED_FILE_NAME);
		File qfInputFile = new File(currentFolder, QF_INPUT_FILE_NAME);

		if (qfExpectedFile.exists() && qfInputFile.exists() && !description.isDisabled()) {
			try {
				qfExpected = FileUtils.readFileToString(qfExpectedFile, Charset.defaultCharset());
				qfInput = FileUtils.readFileToString(qfInputFile, Charset.defaultCharset());
			} catch (IOException e) {
				exception = e;
			}
			arguments.add(Arguments.of(name, description, qfInput, qfExpected, exception));
		}

		for (File child : currentFolder.listFiles()) {
			if (child.isDirectory()) {
				scanForSystemTests(systemTestsFolderPath, child, arguments);
			}
		}
	}

}
