package eu.qped.java.checkers.coverage;


import eu.qped.framework.Feedback;
import eu.qped.framework.Translator;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageFacade;
import eu.qped.java.checkers.syntax.SyntaxCheckReport;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.feedback.syntax.AbstractSyntaxFeedbackGenerator;
import eu.qped.java.feedback.syntax.SyntaxFeedback;
import eu.qped.java.feedback.syntax.SyntaxFeedbackGenerator;
import eu.qped.java.utils.compiler.Com;
import eu.qped.java.utils.compiler.Compiler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class CoverageSetup {
    private static final String MAVEN = "MAVEN", JAVA = "JAVA";

    public class Data {
        public final List<CoverageFacade> testclasses;
        public final List<CoverageFacade> classes;
        public final List<String> syntaxFeedback;
        public final boolean isCompiled;
        private final ZipService zipService;

        public Data(List<CoverageFacade> testclasses, List<CoverageFacade> classes, List<String> syntaxFeedback, boolean isCompiled, ZipService zipService) {
            this.testclasses = testclasses;
            this.classes = classes;
            this.syntaxFeedback = syntaxFeedback;
            this.isCompiled = isCompiled;
            this.zipService = zipService;
        }

        public void cleanUp() {
            zipService.cleanUp();
        }
    }





    private final QfCovSetting setting;

    public CoverageSetup(QfCovSetting setting) {
        this.setting = setting;
    }


    /**
     *
     */
    public Data setUp() {
        ZipService zipService = new Zip();
        ZipService.Extracted extracted = extract(zipService);

        // Validates if at least on testclass and on class are present
        if (extracted.classes().isEmpty())
            throw new IllegalStateException(ErrorMSG.MISSING_CLASS);

        if (extracted.testClasses().isEmpty())
            throw new IllegalStateException(ErrorMSG.MISSING_TESTCLASS);

        SyntaxCheckReport report = compile(extracted.root());

        if (! report.isCompilable()) {
            AbstractSyntaxFeedbackGenerator syntaxFeedbackGenerator = SyntaxFeedbackGenerator.builder().build();
            Translator translator = new Translator();
            List<SyntaxFeedback> feedback = syntaxFeedbackGenerator.generateFeedbacks(report.getSyntaxErrors());
            for (SyntaxFeedback syntaxFeedback : feedback) {
                translator.translateBody(setting.getLanguage(), syntaxFeedback);
            }
            return new Data(null, null, feedback.stream().map(Feedback::getBody).collect(Collectors.toList()), false, zipService);
        }

        return new Data(
                preprocessing(extracted.javafileByClassname(), extracted.testClasses(), report.getPath()),
                preprocessing(extracted.javafileByClassname(), extracted.classes(), report.getPath()),
                new LinkedList<>(),
                true,
                zipService);
    }


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
            if (setting.getConvention().equals(JAVA)) {
                classname = ZipService.JAVA_CLASS_NAME;
                testClass = ZipService.JAVA_TEST_CLASS;

            } else if (setting.getConvention().equals(MAVEN)) {
                classname = ZipService.MAVEN_CLASS_NAME;
                testClass = ZipService.MAVEN_TEST_CLASS;

            } else {
                throw new IllegalStateException(ErrorMSG.UPS);
            }

            if (Objects.nonNull(setting.getFile()) && (Objects.nonNull(setting.getPrivateImplementation()) && !setting.getPrivateImplementation().isBlank())) {
                // Teacher and Student provide data
                return zipService.extractBoth(
                        setting.getFile().getSubmittedFile(),
                        zipService.download(setting.getPrivateImplementation()),
                        testClass,
                        classname);

            } else if (Objects.nonNull(setting.getFile())) {
                // only Student provide data muss contain a  test class and class
                return zipService.extract(setting.getFile().getSubmittedFile(),testClass, classname);

            } else if (Objects.nonNull(setting.getPrivateImplementation()) && !setting.getPrivateImplementation().isBlank()) {
                // Teacher and Student provide data. Students answer is a string.
                ZipService.Extracted extracted = zipService.extract(
                        zipService.download(setting.getPrivateImplementation()),
                        ZipService.JAVA_TEST_CLASS,
                        ZipService.JAVA_CLASS_NAME);

                if (Objects.nonNull(setting.getAnswer()) && !setting.getAnswer().isBlank()) {
                    Com.Created answerAsClass = new Com().createClassFromString(extracted.root(), setting.getAnswer());
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

    private SyntaxCheckReport compile(File root) {

        String path;
        if (System.getProperty("maven.compile.classpath") != null) {
            // requires that the corresponding system property is set in the Maven pom
            path = System.getProperty("maven.compile.classpath");
        } else {
            // if the checker is not run from Maven (e.g., during testing), inherit classpath from current JVM
            path = System.getProperty("java.class.path");
        }

        Compiler compiler = Compiler.builder().options(
                List.of(DIR_CLASS, root.getAbsolutePath(), DIR_SOURCE, root.getAbsolutePath(), CLASSPATH, path))
                .build();

        SyntaxChecker syntaxChecker = SyntaxChecker.builder()
                .targetProject(root.getAbsolutePath())
                .compiler(compiler)
                .build();
        return syntaxChecker.check();
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
            throw new InternalError(String.format(ErrorMSG.CANT_READ_FILE, file));
        }
    }

}
