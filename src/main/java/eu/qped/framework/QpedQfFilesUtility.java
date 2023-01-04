package eu.qped.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import eu.qped.framework.QpedQfFilesUtility.AnswerDescription.AnswerDescriptionBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class QpedQfFilesUtility {
	
    private static final String DEFAULT_ANSWER_METHOD = "$answerMethod";
	private static final String DEFAULT_ANSWER_CLASS = "$AnswerClass";
	
	private static final boolean QPED_QF_FILES_DEBUG = Boolean.getBoolean("QPED_QF_FILES_DEBUG");

	private static List<File> tempFiles = new ArrayList<>();

	public static void cleanupTempFiles() {
		if (QPED_QF_FILES_DEBUG) {
			return;
		}
		
		for (File file : tempFiles) {
			if (file.isDirectory()) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					FileUtils.delete(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		tempFiles.clear();
	}
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(QpedQfFilesUtility::cleanupTempFiles));
	}

	/**
	 * Returns all files in the specified directory or any subdirectory thereof with the given extension.
	 * If <code>null</code> is provided for the extension, all files are included.
	 * The method only includes actual files, the directories themselves are non included in the result.
	 *   
	 * @param dirPath The directory whose contents are searched.
	 * @param extension The required file extension without a leading dot (e.g. "java" instead of ".java")
	 * or <code>null</code> if any file is to be included.
	 * @return All files matching the criteria. The result only contains <code>File</code> objects for which <code>isDirectory()</code> returns <code>false</code>;
	 */
    public static List<File> filesWithExtension(File dirPath, String extension) {
        List<File> allFiles = new ArrayList<>();
        List<File> filesWithExtension = new ArrayList<>();
        File fileOrDirectory = Objects.requireNonNull(dirPath);
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                allFiles.add(fileOrDirectory);
                allFiles.addAll(getFilesRecursively(fileOrDirectory));
            } else {
                allFiles.add(new File(fileOrDirectory.getPath()));
            }
        }
        for (File file : allFiles) {
        	if (file.isDirectory())
        		continue;
        	
            if (FilenameUtils.getExtension(String.valueOf(file)).equals(extension)) {
                filesWithExtension.add(file);
            }
        }
        return filesWithExtension;
    }

    private static List<File> getFilesRecursively(File path) {
        List<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                files.addAll(getFilesRecursively(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }

	/**
	 * This method downloads a file from URL of the provided argument to a local temp file, and unzips it
	 * to a local temp directory of the mime type is zip.
	 * The allocated temporary file and possibly the directory with all its contained files and directories
	 * will be deleted at the end of the execution of this program. I.e., when the main-method in this class ends.
	 * The location of the downloaded file and (in case of a zip file) the location of the extracted files are
	 * set as properties of the passed FileInfo object.
	 * The downloaded file is unzipped if the passed fileInfo specifies as mime type either
	 * "application/zip", "application/zip-compressed" or "application/x-zip-compressed".
	 * 
	 * @param fileInfo
	 * @return The directory containing the downloaded file, if it is a plain file, or the unzipped contents in 
	 * case the downloaded file was a zip archive.
	 * @throws FileNotFoundException
	 * @throws MalformedURLException
	 * @throws ZipException
	 * @throws IOException
	 */
	public static File downloadAndUnzipIfNecessary(FileInfo fileInfo) throws FileNotFoundException, MalformedURLException, ZipException, IOException {
		File targetDirectory = createManagedTempDirectory();
		File submittedFile = new File(targetDirectory, fileInfo.getId() + fileInfo.getExtension());
	
		try (InputStream input = new URL(fileInfo.getUrl()).openStream()) {
			try (OutputStream output = new FileOutputStream(submittedFile)) {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead;
				while ((bytesRead = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}
		}
		fileInfo.setDownloadedFile(submittedFile);
		
		if (fileInfo.getMimetype().contains("application/x-zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip") ) {
			File unzipTarget = createManagedTempDirectory();
	
			ZipFile zipFile = new ZipFile(fileInfo.getDownloadedFile());
			zipFile.extractAll(unzipTarget.toString());
	
			fileInfo.setUnzippedDirectory(unzipTarget);
			return unzipTarget;
		}
		
		return targetDirectory;
	}
	
	private static final AtomicInteger nextId = new AtomicInteger(0);

	public static File createManagedTempDirectory() throws IOException {
		File tempDir;
		if (QPED_QF_FILES_DEBUG) {
			String currentTimeStamp = DateTimeFormatter.
					ofPattern("yyyy-MM-dd--HH-mm-ss").
					format(LocalDateTime.now())
					+ nextId.getAndIncrement();
			tempDir = new File("target" + File.separator + "temp" + File.separator + currentTimeStamp);
		} else {
			tempDir = Files.createTempDirectory("qf-checker").toFile();
		}
		tempDir.mkdirs();
		tempFiles.add(tempDir);
		return tempDir;
	}

//	public static File createManagedTempFile(String filename, String extension) throws IOException {
//		File tempDir = createManagedTempDirectory();
//		File tempFile = File.createTempFile(filename, extension);
//		tempFiles.add(tempFile);
//		return tempFile;
//	}
	
	public static CreatedAnswerFileSummary createFileFromAnswerString(File solutionRoot, String answer) throws IOException {
		AnswerDescription answerDescription = getAnswerDescription(answer);
		String answerFileContent;
		int answerLineOffset;
		String filename;
		switch (answerDescription.getKind()) {
		case ASTParser.K_COMPILATION_UNIT: {
			answerFileContent = answer;
			answerLineOffset = 0;
			filename = answerDescription.getPackageName().replaceAll("/", File.separator);
			if (!filename.isEmpty()) {
				filename += File.separator;
			}
			filename += answerDescription.getPrimaryTypeSimpleName() + ".java";
			break;
		}
		case ASTParser.K_CLASS_BODY_DECLARATIONS: {
			answerFileContent = "public class " + DEFAULT_ANSWER_CLASS + " {\n"
					+ answer + "\n"
					+ "}\n";
			answerLineOffset = 1;
			filename = DEFAULT_ANSWER_CLASS + ".java";
			break;
		}
		case ASTParser.K_STATEMENTS: {
			answerFileContent = "public class "+ DEFAULT_ANSWER_CLASS + " {\n"
					+ "    public static void " + DEFAULT_ANSWER_METHOD + "() {\n"
					+ answer + "\n"
					+ "    }\n"
					+ "}\n";
			answerLineOffset = 2;
			filename = DEFAULT_ANSWER_CLASS + ".java";
			break;
		}
		case ASTParser.K_EXPRESSION: {
			answerFileContent = "public class " + DEFAULT_ANSWER_CLASS + " {\n"
					+ "    public static Object " + DEFAULT_ANSWER_METHOD + "() {\n"
					+ "        return\n"
					+ answer + "\n"
					+ "        ;\n"
					+ "    }\n"
					+ "}\n";
			answerLineOffset = 3;
			filename = DEFAULT_ANSWER_CLASS + ".java";
			break;
		}
		case AnswerDescription.K_INCONCLUSIVE: {
			// The answer was no legal Java compilation unit, method sequence, statement sequence or expression.
			// Therefore, we could not determine what kind of code the answer is supposed to contain.
			// But most Java elements are allowed inside a static method, so let's wrap it in a static method.
			answerFileContent = "public class " + DEFAULT_ANSWER_CLASS + " {\n"
					+ "    public static void " + DEFAULT_ANSWER_METHOD + "() {\n"
					+ answer + "\n"
					+ "    }"
					+ "}";
			answerLineOffset = 2;
			filename = DEFAULT_ANSWER_CLASS + ".java";
			break;
		}

		default:
			throw new IllegalArgumentException("Unexpected value: " + answerDescription.getKind());
		}
		
		File outputFile = new File(solutionRoot, filename);
		File outputDirectory = outputFile.getParentFile();
		if (outputDirectory != null) {
			outputDirectory.mkdirs();
		}
		FileUtils.write(outputFile, answerFileContent, Charset.defaultCharset());
		return new CreatedAnswerFileSummary(filename, answerLineOffset);
	}
	
	@Getter
	@AllArgsConstructor
	public static class CreatedAnswerFileSummary {
		private String fileName;
		private int lineOffset;
	}

	public static AnswerDescription getAnswerDescription(String answer) {
		AnswerDescriptionBuilder descriptionBuilder = AnswerDescription.builder();
		
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		
		// first try compilation unit
		parser.setIgnoreMethodBodies(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setStatementsRecovery(false);
		parser.setSource(answer.toCharArray());
		ASTNode ast = parser.createAST(null);
		
		if (ast != null && ast instanceof CompilationUnit) {
			CompilationUnit cu = (CompilationUnit) ast;
			@SuppressWarnings("unchecked")
			List<ImportDeclaration> imports = cu.imports();
			PackageDeclaration packageDeclaration = cu.getPackage();
			@SuppressWarnings("unchecked")
			List<AbstractTypeDeclaration> types = cu.types();
			if (!imports.isEmpty() || packageDeclaration != null || !types.isEmpty()) {
				// it is definitely meant to be a compilation unit
				descriptionBuilder.kind(ASTParser.K_COMPILATION_UNIT);
				descriptionBuilder.packageName(packageDeclaration != null ? packageDeclaration.getName().getFullyQualifiedName() : "");
				if (!types.isEmpty()) {
					AbstractTypeDeclaration primaryType = types.get(0);
					for (AbstractTypeDeclaration type : types) {
						if (Modifier.isPublic(type.getModifiers())) {
							primaryType = type;
						}
					}
					descriptionBuilder.primaryTypeSimpleName(primaryType.getName().getIdentifier());
				}
				descriptionBuilder.ast(ast);
				return descriptionBuilder.build();
			}
		}
		
		// if we have not been successful, try member declarations next
		// first try compilation unit
		parser.setIgnoreMethodBodies(true);
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		parser.setStatementsRecovery(true);
		parser.setSource(answer.toCharArray());
		ast = parser.createAST(null);
		
		// class body declarations, such as methods, as wrapped in a new class with name MISSING
		// in other cases, the type of the AST node would be the fallback "compilation unit"
		if (ast != null && ast instanceof TypeDeclaration && ((TypeDeclaration) ast).getName().getIdentifier().equals("MISSING")) {
			TypeDeclaration td = (TypeDeclaration) ast;

			List<BodyDeclaration> bodyDeclarations = td.bodyDeclarations();
			boolean onlyPotentialLocalVariables = true;
			for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
				if (!(bodyDeclaration instanceof FieldDeclaration)) {
					onlyPotentialLocalVariables = false;
					break;
				} else if (bodyDeclaration instanceof FieldDeclaration) {
					FieldDeclaration fd = (FieldDeclaration) bodyDeclaration;
					if ((Modifier.isFinal(fd.getModifiers()) && fd.modifiers().size() > 1) ||
							!Modifier.isFinal(fd.getModifiers()) && fd.modifiers().size() > 0) {
						onlyPotentialLocalVariables = false;
						break;
					}	
				}
			}
			// If the body declarations all are field declrations, it is most likely that
			// the answer actually consists of statements (with a syntax error) and we
			// accidentally recognized a local variable declaration as field declaration.
			// Only fields with a modifier other than final are definitely fields.
			if (!onlyPotentialLocalVariables) {
				// the answer must consist of class members such as methods
				// it cannot be a class, as it would have been interpreted as compilation unit above
				descriptionBuilder.kind(ASTParser.K_CLASS_BODY_DECLARATIONS);
				// no package and type name are provided in the answer, so we use default values
				descriptionBuilder.packageName("").primaryTypeSimpleName(null);
				descriptionBuilder.ast(ast);
				return descriptionBuilder.build();
			}
		}
		
		// package name and primary type name can only exist for
		// answers which are compilation units.
		descriptionBuilder.packageName("").primaryTypeSimpleName(null);

		
		// if we have not been successful, try expression next
		// first try compilation unit
		parser.setKind(ASTParser.K_EXPRESSION);
		parser.setStatementsRecovery(true);
		parser.setSource(answer.toCharArray());
		ast = parser.createAST(null);
		System.out.println(ast);
		
		// statements are wrapped in a block
		if (ast != null && ast instanceof Expression) {
			// the answer must consist of an expression
			descriptionBuilder.kind(ASTParser.K_EXPRESSION);
			descriptionBuilder.ast(ast);
			return descriptionBuilder.build();
		}


		// if we have not been successful, try statements next
		// first try compilation unit
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setStatementsRecovery(true);
		parser.setSource(answer.toCharArray());
		ast = parser.createAST(null);
		System.out.println(ast);
		
		// statements are wrapped in a block
		if (ast != null && ast instanceof Block && !((Block) ast).statements().isEmpty()) {
			// the answer must consist of statements
			descriptionBuilder.kind(ASTParser.K_STATEMENTS);
			descriptionBuilder.ast(ast);
			return descriptionBuilder.build();
		}

		descriptionBuilder.kind(AnswerDescription.K_INCONCLUSIVE).ast(null);
		return descriptionBuilder.build();
	}
	
	@Builder
	@Getter
	public static class AnswerDescription {
		public static final int K_INCONCLUSIVE = 0;
		
		private String primaryTypeSimpleName;
		private String packageName;
		
		/**
		 * Determine whether the answer is a complete compilation unit, a sequence of body
		 * declarations (method, field, etc.), a sequence of statements or an expression.
		 * 
		 * One of {@link ASTParser#K_COMPILATION_UNIT}, {@link ASTParser#K_CLASS_BODY_DECLARATIONS},
		 * {@link ASTParser#K_STATEMENTS}, {@link ASTParser#K_EXPRESSION}, or {@link #K_INCONCLUSIVE} for inconclusive.
		 */
		private int kind;
		
		private ASTNode ast;
		
	}
	
	
}