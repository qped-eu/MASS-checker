package eu.qped.java.checkers.coverage;


import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.template.TemplateBuilder;
import eu.qped.java.checkers.syntax.analyser.SyntaxAnalysisReport;
import eu.qped.java.checkers.syntax.analyser.SyntaxErrorAnalyser;
import eu.qped.java.checkers.syntax.SyntaxSetting;
import eu.qped.java.checkers.syntax.feedback.SyntaxFeedbackGenerator;
import eu.qped.java.utils.compiler.Com;
import eu.qped.java.utils.compiler.Compiler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class CoverageSetup {
    private static final String MAVEN = "MAVEN", JAVA = "JAVA";


    public class Info implements CovInformation {
        private final byte[] byteCode;
        private final String classname;
        private final String content;

        public Info(byte[] byteCode, String classname, String content) {
            this.byteCode = byteCode;
            this.classname = classname;
            this.content = content;
        }

        @Override
        public String simpleClassName() {
            return classname.substring(classname.lastIndexOf(".") + 1);
        }

        @Override
        public String content() {
            return content;
        }

        @Override
        public String className() {
            return classname;
        }

        @Override
        public byte[] byteCode() {
            return byteCode;
        }
    }

    public class Data {
        public final List<CovInformation> testclasses;
        public final List<CovInformation> classes;
        public final List<String> syntaxFeedback;
        public final boolean isCompiled;
        private final ZipService zipService;

        public Data(List<CovInformation> testclasses, List<CovInformation> classes, List<String> syntaxFeedback, boolean isCompiled, ZipService zipService) {
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

        SyntaxAnalysisReport report = compile(extracted.root());

        if (!report.isCompilable()) {
            List<String> feedbacks = SyntaxFeedbackGenerator.builder().build().generateFeedbacks(
                    report.getSyntaxErrors(),
                    SyntaxSetting.builder().language(setting.getLanguage()).build()
            );
            return new Data(null, null,feedbacks , false, zipService);
        }

        return new Data(
                preprocessing(extracted.javafileByClassname(), extracted.testClasses(), report.getPath()),
                preprocessing(extracted.javafileByClassname(), extracted.classes(), report.getPath()),
                new LinkedList<>(),
                true,
                zipService);
    }


    /**
     * Downloads the resource zip-folder "privateImplementation" and stores the files of the folder.
     * - If the answer of a student is a zip-folder the privateImplementation will be unzipped in the answer folder
     * and overwrites all classes that have the same name.
     * - If the answer of the student is a string the privateImplementation will be unzipped and the answer will
     * be saved as java class in the unzipped folder.
     * Note: im not using the {@link eu.qped.java.utils.compiler.Compiler} to create the java  file from a string.
     * - Provides not the real class name
     * - Always compiles without the possibility to add other files
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
                return zipService.extract(setting.getFile().getSubmittedFile(), testClass, classname);

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

    private SyntaxAnalysisReport compile(File root) {

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

        SyntaxErrorAnalyser syntaxChecker = SyntaxErrorAnalyser.builder()
                .targetProject(root.getAbsolutePath())
                .compiler(compiler)
                .build();
        return syntaxChecker.check();
    }

    private LinkedList<CovInformation> preprocessing(
            Map<String, File> javafileByClassname,
            List<String> classname,
            String absolutePath
    ) {
        LinkedList<CovInformation> infos = new LinkedList<>();
        for (String name : classname) {
            infos.add(new Info(
                    readByteCode(Path.of(absolutePath + "/" + name.replace(".", "/") + ".class").toString()),
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
