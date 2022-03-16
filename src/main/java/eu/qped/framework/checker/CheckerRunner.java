package eu.qped.framework.checker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import eu.qped.framework.qf.QfObject;

public class CheckerRunner {

	private static final File QF_OBJECT_JSON_FILE = new File("qf.json");


	private static String toJsonString(QfObject qfObject) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new MinimalPrettyPrinter());
		StringWriter sw = new StringWriter();
		writer.writeValue(sw, qfObject);
		return sw.toString();
	}

	private final QfObject qfObject;
	
	private final Checker checker;
	
	public CheckerRunner() throws IOException {
		this((Checker) null);
	}

	public CheckerRunner(Checker checker) throws IOException {
		this(FileUtils.readFileToString(QF_OBJECT_JSON_FILE, Charset.defaultCharset()), checker);
	}

	public CheckerRunner(String qfObjectJsonString) throws JsonMappingException, JsonProcessingException {
		this(qfObjectJsonString, null);
	}
	
	public CheckerRunner(String qfObjectJsonString, Checker checker) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		qfObject = mapper.readValue(qfObjectJsonString, new TypeReference<QfObject>(){});
		
		String checkerClassName = qfObject.getCheckerClass();
		if (checkerClassName != null) {
			if (checker != null) {
				throw new IllegalArgumentException("Checker is ambiguous. A checker was provided to the CheckerRunner constructor and a checker class was specified in the Qf obejct.");
			}
			try {
				@SuppressWarnings("unchecked")
				Class<Checker> cls = (Class<Checker>) Class.forName(checkerClassName);
				this.checker = cls.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				throw new IllegalArgumentException("Illegal checker class specified", e);
			}
		} else if (checker != null) {
			this.checker = checker;
		} else {
			throw new IllegalArgumentException("No checker class specified");
		}
	}
	
	public void check() throws Exception {
		checker.check(qfObject);
	}
	
	
	public static void main(String[] args) throws IOException {
		CheckerRunner runner = new CheckerRunner();
		try {
			runner.getQfObject().setMessage("qfObjectInput", toJsonString(runner.getQfObject()));

			runner.check();

			runner.getQfObject().setMessage("qfObjectOutput", toJsonString(runner.getQfObject()));
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			runner.getQfObject().setMessage("exceptionDuringCheck", sw.toString());
		}

		ObjectMapper mapper = new ObjectMapper();

		ObjectWriter writer = mapper.writer(new MinimalPrettyPrinter());
		writer.writeValue(QF_OBJECT_JSON_FILE, runner.getQfObject());
	}
		


	public QfObject getQfObject() {
		return qfObject;
	}
}
