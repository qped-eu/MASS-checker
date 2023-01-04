package eu.qped.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.mass.Mass;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

public class CheckerRunner {

	private static final String QF_OBJECT_JSON_FILE_NAME = "qf.json";

	private final QfObject qfObject;

	private final Checker checker;

	private File qfObjectJsonFile;
	
	public CheckerRunner(File qfObjectJsonFile) throws JsonMappingException, JsonProcessingException, IOException {
		this.qfObjectJsonFile = qfObjectJsonFile;
		String qfObjectJsonString = FileUtils.readFileToString(this.qfObjectJsonFile, Charset.defaultCharset());

		ObjectMapper mapper = new ObjectMapper();

		// sem, semObj
		// syntax, syntax
		// style, style
		// checkerClass: name
		Map<String, Object> qfObjectMap = mapper.readValue(qfObjectJsonString,
				new TypeReference<Map<String, Object>>() {
				});

		String checkerClassName = (String) qfObjectMap.get("checkerClass");
		// if no Checker class is specified, use Mass as default.
		if (checkerClassName == null) {
			checkerClassName = Mass.class.getName();
		}

		try {
			@SuppressWarnings("unchecked")
			Class<Checker> cls = (Class<Checker>) Class.forName(checkerClassName);
			this.checker = cls.getDeclaredConstructor().newInstance();

			qfObject = mapper.readValue(mapper.writeValueAsString(qfObjectMap), new TypeReference<QfObject>() {
			});

			for (Field field : cls.getDeclaredFields()) {
				QfProperty qfPropertyAnnotation = field.getDeclaredAnnotation(QfProperty.class);
				if (qfPropertyAnnotation != null) {
						Object propertyValue = qfObjectMap.get(field.getName());
						field.setAccessible(true);
						field.set(checker, mapper.readValue(mapper.writeValueAsString(propertyValue), field.getType()));
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Illegal checker class specified", e);
		}
	}

	public File getQfObjectJsonFile() {
		return qfObjectJsonFile;
	}

	public void check() throws Exception {
		checker.check(qfObject);
	}

	public static void main(String[] args) throws IOException {
		String qfJsonFileName;
		boolean overwriteJsonFile;
		if (args.length > 0) {
			qfJsonFileName = args[0];
			overwriteJsonFile = false;
		} else {
			qfJsonFileName = QF_OBJECT_JSON_FILE_NAME;
			overwriteJsonFile = true;
		}
		
		File qfJsonFile = new File(qfJsonFileName);
		
		CheckerRunner runner = new CheckerRunner(qfJsonFile);
		
		try {
			runner.check();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			runner.getQfObject().setMessage("exceptionDuringCheck", sw.toString());
		}

		ObjectMapper mapper = new ObjectMapper();
	
		ObjectWriter writer = mapper.writer(new MinimalPrettyPrinter());
		if (overwriteJsonFile) {
			writer.writeValue(runner.getQfObjectJsonFile(), runner.getQfObject());
		}
		else {
			writer.writeValue(System.out, runner.getQfObject());
		}
	}

	public QfObject getQfObject() {
		Arrays.stream(qfObject.getFeedback()).forEach(System.out::println);
		return qfObject;
	}
}
