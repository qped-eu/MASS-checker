package eu.qped.framework;

import eu.qped.framework.FileInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ZipService {

    @FunctionalInterface
    interface TestClass {
        boolean isTrue(File file);
    }

    @FunctionalInterface
    interface Classname {
        String parse(File file);
    }

    interface Extracted {

        List<String> testClasses();
        List<String> classes();
        List<File> files();

        Map<String, File> javafileByClassname();

        File root();
    }

    FileInfo download(FileInfo file) throws Exception;

    Extracted extract(FileInfo file, TestClass testClass, Classname classname) throws Exception;

    public Extracted extractBoth(FileInfo fileA, FileInfo fileB, TestClass testClass, Classname classname) throws Exception;


    void cleanUp();

}
