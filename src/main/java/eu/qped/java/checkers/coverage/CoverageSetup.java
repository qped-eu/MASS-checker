package eu.qped.java.checkers.coverage;


import eu.qped.framework.FileInfo;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.mass.Convention;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.compiler.Com;
import eu.qped.java.utils.compiler.Compiler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class CoverageSetup {
	
    public final List<CoverageFacade> testclasses; //bbrauchen wir
    public final List<CoverageFacade> classes;//bbrauchen wir
    public final List<String> syntaxFeedback;
    public final boolean isCompiled;
    private final ZipService zipService;
    private Compiler compiler;

    public void cleanUp() {
    	zipService.cleanUp();
    }

	private FileInfo answerFile;
	public CoverageSetup(FileInfo answerFile, String privateImplementation, String answerText, String preferredLanguage) {
		super();
		this.answerFile = answerFile;
		this.privateImplementation = privateImplementation;
		this.answerText = answerText;
		this.preferredLanguage = preferredLanguage;
		this.convention = Convention.JAVA;
		
        zipService = new Zip();
        ZipService.Extracted extracted = extract(zipService);

        // Validates if at least on testclass and on class are present
        if (extracted.classes().isEmpty())
            throw new IllegalStateException(ErrorMSG.MISSING_CLASS);

        if (extracted.testClasses().isEmpty())
            throw new IllegalStateException(ErrorMSG.MISSING_TESTCLASS);

        syntaxFeedback = compile(extracted.root());

        if (!syntaxFeedback.isEmpty()) {
            isCompiled = false;
            testclasses = Collections.emptyList();
            classes = Collections.emptyList();
        } else {
            String path = root.getAbsolutePath();//compiler.getCompiledStringResourcePath();
            testclasses = preprocessing(extracted.javafileByClassname(), extracted.testClasses(), path);
            classes = preprocessing(extracted.javafileByClassname(), extracted.classes(), path);
            isCompiled = true;
        }
	}

	private String privateImplementation;
	private String answerText;
	private String preferredLanguage;
	private Convention convention;
	private File root;




    /**
     *  Downloads the resource zip-folder "privateImplementation" and stores the files of the folder.
     *  - If the answer of a student is a zip-folder the privateImplementation will be unzipped in the answer folder
     *    and overwrites all classes that have the same name.
     *  - If the answer of the student is a string the privateImplementation will be unzipped and the answer will
     *    be saved as java class in the unzipped folder.
     *  Note: im not using the {@link eu.qped.java.utils.compiler.Compiler} to create the java  file from a string.
     *    - Provides not the real class name
     *    - Always compiles without the possibility to add other files
     */
    private ZipService.Extracted extract(ZipService zipService) {

        try {
            ZipService.Classname classname;
            ZipService.TestClass testClass;
            
            switch (convention) {
            case JAVA:
                classname = ZipService.JAVA_CLASS_NAME;
                testClass = ZipService.JAVA_TEST_CLASS;
                break;
            case MAVEN:
                classname = ZipService.MAVEN_CLASS_NAME;
                testClass = ZipService.MAVEN_TEST_CLASS;
                break;
            default:
                throw new IllegalStateException(ErrorMSG.UPS);
            }

            if (Objects.nonNull(answerFile) && (Objects.nonNull(privateImplementation) && !privateImplementation.isBlank())) {
                // Teacher and Student provide data
                return zipService.extractBoth(
                        answerFile.getSubmittedFile(),
                        zipService.download(privateImplementation),
                        testClass,
                        classname);

            } else if (Objects.nonNull(answerFile)) {
                // only Student provide data muss contain a  test class and class
                return zipService.extract(answerFile.getSubmittedFile(),testClass, classname);

            } else if (Objects.nonNull(privateImplementation) && !privateImplementation.isBlank()) {
                // Teacher and Student provide data. Students answer is a string.
                ZipService.Extracted extracted = zipService.extract(
                        zipService.download(privateImplementation),
                        ZipService.JAVA_TEST_CLASS,
                        ZipService.JAVA_CLASS_NAME);

                if (Objects.nonNull(answerText) && !answerText.isBlank()) {
                    Com.Created answerAsClass = new Com().createClassFromString(extracted.root(), answerText);
                    if (answerAsClass.isTrue) {
                        extracted.add(answerAsClass.className, answerAsClass.file, ZipService.JAVA_TEST_CLASS.isTrue(answerAsClass.file));
                    }
                }

                return extracted;
            }
            throw new Exception(ErrorMSG.MISSING_FILES);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static final String DIR_CLASS = "-d";
    private static final String DIR_SOURCE = "-s";
    private static final String CLASSPATH = "-classpath";

    private List<String> compile(File root) {
    	this.root = root;
        String path;
        if (System.getProperty("maven.compile.classpath") != null) {
            // requires that the corresponding system property is set in the Maven pom
            path = System.getProperty("maven.compile.classpath");
        } else {
            // if the checker is not run from Maven (e.g., during testing), inherit classpath from current JVM
            path = System.getProperty("java.class.path");
        }

        compiler = Compiler.builder().options(
                List.of(DIR_CLASS, root.getAbsolutePath(), DIR_SOURCE, root.getAbsolutePath(), CLASSPATH, path))
                .build();

        
        
        SyntaxChecker syntaxChecker = SyntaxChecker.builder()
                .targetProject(root.getAbsolutePath())
                .build();
        return syntaxChecker.check(compiler);
    }

    private LinkedList<CoverageFacade> preprocessing(
            Map<String, File> javafileByClassname,
            List<String> classname,
            String absolutePath
    )  {
        LinkedList<CoverageFacade> infos = new LinkedList<>();
        for (String name : classname) {
            infos.add(new CoverageFacade(
                    readByteCode(Path.of(absolutePath + "/" + name.replace(".","/") + ".class").toString()),
                    name,
                    readJavacontent(javafileByClassname.get(name))));
        }
        return infos;
    }

    private String readJavacontent(File file) {
        try {
            return Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new InternalError(String.format(ErrorMSG.CANT_READ_FILE, file.toString()));
        }
    }

    private byte[] readByteCode(String file) {
        try {
            return Files.readAllBytes(Paths.get(file));
        } catch (Exception e) {
        	//FIXME change InternalError to some usefult exception
            throw new InternalError(String.format(ErrorMSG.CANT_READ_FILE, file));
        }
    }
    

}
