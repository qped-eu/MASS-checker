package eu.qped.java.checkers.coverage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ZipService {

    static String UNZIPPED_NAME = "unzipped";

    static String sep(String pattern) {
        if (File.separator.equals("/")) {
            return pattern;
        } else {
            return pattern.replace("/", "\\\\");
        }
    }


    TestClass MAVEN_TEST_CLASS = (file) -> {
        return Pattern.matches(sep( ".*src/test/java/.*\\.java$"), file.getPath());
    };

    TestClass JAVA_TEST_CLASS = (file) -> {
        return Pattern.matches(".*[Tt]est\\.java$", file.getPath()) ||
        		Pattern.matches(".*[Tt]est[^/]*\\.java$", file.getPath());
    };

    Classname MAVEN_CLASS_NAME = (file) -> {
        Pattern pattern = Pattern.compile(sep(".*src/(test|main)/java/(.*)\\.java$"));
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(2).replace(File.separator,".");
        }
        return null;
    };

    Classname JAVA_CLASS_NAME = (file) -> {
        Pattern pattern = Pattern.compile(sep(".*/"+UNZIPPED_NAME+"\\d+/(.*)\\.java$"));
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(1).replace(File.separator,".");
        }
        return null;
    };




    @FunctionalInterface
    interface TestClass {
        boolean isTrue(File file);
    }

    @FunctionalInterface
    interface Classname {
        String parse(File file);
    }

    interface Extracted {

        void add(String name, File file, boolean isTest);
        List<String> testClasses();
        List<String> classes();
        List<File> files();

        Map<String, File> javafileByClassname();

        File root();
    }

    File download(String url) throws Exception;

    Extracted extract(File file, TestClass testClass, Classname classname) throws Exception;

    public Extracted extractBoth(File fileA, File fileB, TestClass testClass, Classname classname) throws Exception;


    void cleanUp();

}
