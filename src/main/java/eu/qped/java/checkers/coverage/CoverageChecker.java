package eu.qped.java.checkers.coverage;

import eu.qped.framework.*;
import eu.qped.framework.qf.*;
import eu.qped.java.checkers.coverage.feedback.Formatter;
import eu.qped.java.checkers.coverage.feedback.Summary;
import eu.qped.java.checkers.coverage.feedback.wanted.ParserWF;
import eu.qped.java.checkers.coverage.framework.ast.*;
import eu.qped.java.checkers.coverage.framework.coverage.*;
import eu.qped.java.checkers.coverage.framework.test.*;
import eu.qped.java.utils.compiler.Compiler;
import org.junit.jupiter.api.Test;

import javax.tools.Diagnostic;
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
    private static final String COMPILE_FROM_FILE = null;
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
    private final ZipService.Classname mavenClassName = (file) -> {
        Pattern pattern = Pattern.compile(".*src/+(test|main)+/java/(.*)\\.java$");
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    };
    private final ZipService.TestClass mavenTestClass = (file) -> {
        return Pattern.matches("src/test/java.*\\.java$", file.getPath());
    };
    private final ZipService.TestClass javaTestClass = (file) -> {
        return Pattern.matches(".*Test\\.java$", file.getPath());
    };
    
    @QfProperty
    QfCovSetting covSetting;
    @QfProperty
    QfUser user;
    @QfProperty
    FileInfo file;
    @QfProperty
    FileInfo additional;
    
    @Override
    public void check(QfObject qfObject) throws Exception {

        try {
            Zip zip = new Zip();
            ZipService.Extracted extracted = extract(zip);
            Compiler compiler = Compiler.builder().build();
            compiler.setTargetProjectPath(extracted.root().getAbsolutePath());
            if (! compiler.compile(COMPILE_FROM_FILE)) {
                for (Diagnostic s : compiler.getCollectedDiagnostics()) {
                    System.out.println(s);
                }
                return;
            }

            Summary summary = checker(
                    preprocessing(extracted.javafileByClassname(), extracted.testClasses()),
                    preprocessing(extracted.javafileByClassname(), extracted.classes()));

            qfObject.setFeedback(Formatter.format(covSetting.getFormat(), summary));
            zip.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private ZipService.Extracted extract(ZipService zipService) throws Exception {
        ZipService.Classname classname;
        ZipService.TestClass testClass;
        if (covSetting.getConvention().equals("JAVA")) {
            classname = (f) -> {
                Pattern pattern = Pattern.compile("tmp/exam-results\\d+/("+additional.getId()+"|"+file.getId()+")/(.*)\\.java$");
                Matcher matcher = pattern.matcher(f.getPath());
                if (matcher.find()) {
                    return matcher.group(2);
                }
                return null;
            };
            testClass = javaTestClass;
        } else {
            classname = mavenClassName;
            testClass = mavenTestClass;
        }

        return zipService.extractBoth(
                zipService.download(file),
                zipService.download(additional),
                testClass,
                classname);
    }
    
    public Summary checker(List<CovInformation> testClasses, List<CovInformation> classes) {
        Summary summary = new Summary();
        try {
            AstFramework ast = AstFrameworkFactoryAbstract.create("JAVA_PARSER").create();
            TestFrameworkFactory test = TestFrameworkFactoryAbstract.create("JUNIT5");
            CoverageFramework coverage = CoverageFrameworkFactoryAbstract.create("JACOCO").create(test);

            summary = (Summary) ast.analyze(
                    summary,
                    new LinkedList<>(classes),
                    covSetting.getExcludeByTypeSet(),
                    covSetting.getExcludeByNameSet());

            summary = (Summary) coverage.analyze(
                    summary,
                    new LinkedList<>(testClasses),
                    new LinkedList<>(classes));

            summary.analyze(new ParserWF().parse(user.getLanguage(), covSetting.getFeedback()));
        } catch (Exception e) {
            e.printStackTrace();
            return summary;
        }
        return summary;
    }

    private LinkedList<CovInformation> preprocessing( 
            Map<String, File> javafileByClassname,
            List<String> classname) throws Exception {
        LinkedList<CovInformation> infos = new LinkedList<>();
        for (String name : classname) {
            infos.add(new Info(
                    readByteCode(javafileByClassname.get(name)),
                    name,
                    readJavacontent(javafileByClassname.get(name))));
        }
        return infos;
    }

    private String readJavacontent(File file) throws Exception{
        return Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
    }

    private byte[] readByteCode(File file) throws Exception {
        return  Files.readAllBytes(Paths.get(file.getPath().replaceAll("\\.java", ".class")));
    }

}
