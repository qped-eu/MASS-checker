package eu.qped.java.checkers;

import eu.qped.framework.QpedQfFilesUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Facade {

    private final byte[] byteCode;
    private final String className;

    public Facade(byte[] byteCode, String className) {
        this.byteCode = byteCode;
        this.className = className;
    }

    public String className() {
        return className;
    }

    public byte[] byteCode() {
        return byteCode;
    }

    public static void separateTestAndApplicationClasses(File solutionRoot, List<Facade> testClasses, List<Facade> classes)
            throws IOException {
        List<File> allClassFiles = QpedQfFilesUtility.filesWithExtension(solutionRoot, "class");
        String solutionDirectoryPath = solutionRoot.getCanonicalPath() + File.separator;

        // The file separator will be used as regular expression by replaceAll.
        // Therefore, we must escape the separator on Windows systems.
        String fileSeparator = File.separator;
        if (fileSeparator.equals("\\")) {
            fileSeparator = "\\\\";
        }
        for (File file : allClassFiles) {
            String filename = file.getCanonicalPath();

            String classname = filename.
                    substring(solutionDirectoryPath.length(), filename.length() - ".class".length()).
                    replaceAll(fileSeparator, ".");

            String[] classnameSegments = classname.split("\\.");
            String simpleClassname = classnameSegments[classnameSegments.length - 1];

            Facade facade = new Facade(
                    Files.readAllBytes(file.toPath()),
                    classname);

            if (simpleClassname.startsWith("Test")
                    || simpleClassname.startsWith("test")
                    || simpleClassname.endsWith("Test")
                    || simpleClassname.endsWith("test")) {
                // the class is a test
                testClasses.add(facade);
            } else {
                // the class is an application class (i.e., no test)
                classes.add(facade);
            }
        }
    }

}
