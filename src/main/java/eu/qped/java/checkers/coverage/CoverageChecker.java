package eu.qped.java.checkers.coverage;

import eu.qped.framework.*;
import eu.qped.framework.qf.*;
import eu.qped.java.checkers.coverage.feedback.Formatter;
import eu.qped.java.checkers.coverage.feedback.Summary;
import eu.qped.java.checkers.coverage.feedback.wanted.ParserWF;
import eu.qped.java.checkers.coverage.framework.ast.*;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;
import eu.qped.java.utils.compiler.Com;
import eu.qped.java.utils.compiler.Compiler;
import org.apache.commons.lang.ArrayUtils;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Creates a custom code coverage for given list of classes
 * @author Herfurth
 */
public class CoverageChecker implements Checker {



    // frameworks
    // defines what frameworks are used
    private static final String COVERAGE_FRAMEWORK = "JACOCO", AST_FRAMEWORK = "JAVA_PARSER", TEST_FRAMEWORK = "JUNIT5";

    // conventions
    // defines what classnames and testclass are
    private static final String MAVEN = "MAVEN", JAVA = "JAVA";

    private final static ZipService.Classname mavenClassName = (file) -> {
        Pattern pattern = Pattern.compile(".*src/+(test|main)+/java/(.*)\\.java$");
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    };
    private final static ZipService.TestClass mavenTestClass = (file) -> {
        return Pattern.matches("src/test/java.*\\.java$", file.getPath());
    };
    private final static ZipService.Classname javaClassName = (file) -> {
        Pattern pattern = Pattern.compile(".*/exam-results\\d+/(.*)\\.java$");
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    };
    private final static ZipService.TestClass javaTestClass = (file) -> {
        return Pattern.matches(".*Test\\.java$", file.getPath());
    };

    private class Info implements CovInformation {
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

    QfCovSetting covSetting;
    FileInfo file = null;
    FileInfo additional = null;
    String answer = null;

    public CoverageChecker() {

    }

    public CoverageChecker(QfCovSetting covSetting) {
        this.covSetting = covSetting;
        this.file = covSetting.getFile();
        this.additional = covSetting.getAdditional();
        this.answer = covSetting.getAnswer();
    }


    public List<String> check()  {
        return  Arrays.stream(setUp()).collect(Collectors.toList());
    }

    @Override
    public void check(QfObject qfObject) throws Exception {
        qfObject.setFeedback(setUp());
    }

    private String[] setUp() {
        try {
            Zip zip = new Zip();
            ZipService.Extracted extracted = extract(zip);

            Map<String, File> fileByClassname = extracted.javafileByClassname();
            List<String> testClasses = extracted.testClasses();
            List<String> classes = extracted.classes();

            Com compiler = new Com();

            if (Objects.nonNull(answer) && !answer.isBlank()) {
                Com.Created f = compiler.createClassFromString(extracted.root(), answer);
                if (f.isTrue) {
                    if (Pattern.matches(".*Test$", f.className)) {
                        testClasses.add(f.className);
                    } else {
                        classes.add(f.className);
                    }
                    fileByClassname.put(f.className, f.file);
                }
            }


            if (! compiler.compileSource(extracted.root())) {
                System.out.println(compiler.protocol());
                List<String> failed = compiler.protocol().stream().map(s-> s.toString()).collect(Collectors.toList());
                System.out.println(compiler.protocol());
                failed.add(0, "Ups there are some compile issues: ");
                return (String[]) failed.toArray();
            }

            if (classes.isEmpty())
                throw new IllegalStateException("Ups something went wrong! Needs at least one class for testing." );
            if (testClasses.isEmpty())
                throw new IllegalStateException("Ups something went wrong! Needs at least one test class." );


            Summary summary = checker(
                    preprocessing(fileByClassname, testClasses),
                    preprocessing(fileByClassname, classes));
            zip.cleanUp();
            return Formatter.format(covSetting.getFormat(), summary);
        } catch (Exception e) {
            return new String[]{"Ups something  went wrong! " + e};
        }
    }


    private ZipService.Extracted extract(ZipService zipService) {
        try {
            ZipService.Classname classname;
            ZipService.TestClass testClass;
            if (covSetting.getConvention().equals(JAVA)) {
                classname = javaClassName;
                testClass = javaTestClass;
            } else {
                classname = mavenClassName;
                testClass = mavenTestClass;
            }

            if (Objects.nonNull(file) && Objects.nonNull(additional)) {
                return zipService.extractBoth(
                        zipService.download(file),
                        zipService.download(additional),
                        testClass,
                        classname);
            } else if (Objects.nonNull(file)) {
                return zipService.extract(zipService.download(file),testClass, classname);
            } else if (Objects.nonNull(additional)) {
                return zipService.extract(zipService.download(additional),testClass, classname);
            }
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Ups something went wrong!");
        }
    }

    public Summary checker(List<CovInformation> testClasses, List<CovInformation> classes) {
        Summary summary = new Summary();
        try {
            AstFramework ast = AstFrameworkFactoryAbstract.create(AST_FRAMEWORK).create();
            TestFrameworkFactory test = TestFrameworkFactoryAbstract.create(TEST_FRAMEWORK);
            CoverageFramework coverage = CoverageFrameworkFactoryAbstract.create(COVERAGE_FRAMEWORK).create(test);

            summary = (Summary) ast.analyze(
                    summary,
                    new LinkedList<>(classes),
                    covSetting.getExcludeByTypeSet(),
                    covSetting.getExcludeByNameSet());

            summary = (Summary) coverage.analyze(
                    summary,
                    new LinkedList<>(testClasses),
                    new LinkedList<>(classes));

            summary.analyze(new ParserWF().parse(covSetting.getLanguage(), covSetting.getFeedback()));
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalError("Ups there is a internal error!");
        }
    }

    private LinkedList<CovInformation> preprocessing(
            Map<String, File> javafileByClassname,
            List<String> classname)  {
        LinkedList<CovInformation> infos = new LinkedList<>();
        for (String name : classname) {
            infos.add(new Info(
                    readByteCode(javafileByClassname.get(name)),
                    name,
                    readJavacontent(javafileByClassname.get(name))));
        }
        return infos;
    }

    private String readJavacontent(File file) {
        try {
            return Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));

        } catch (Exception e) {
            throw new InternalError("ERROR::CoverageChecker ERROR-CODE:003");
        }
    }

    private byte[] readByteCode(File file) {
        try {
            return Files.readAllBytes(Paths.get(file.getPath().replaceAll("\\.java", ".class")));
        } catch (Exception e) {
            throw new InternalError("ERROR::CoverageChecker ERROR-CODE:004");
        }
    }
}