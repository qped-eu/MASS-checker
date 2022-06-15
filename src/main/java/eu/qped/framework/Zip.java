package eu.qped.framework;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;


public class Zip implements ZipService {
    private static final int BUFFER_SIZE = 1024;
    private static final String SCHEME = "file:";
    private static final String MIMETYPE = "application/zip";


    public class ZipExtracted implements ZipService.Extracted {
        private final LinkedList<String> testClasses;
        private final LinkedList<String> classes;
        private final File root;
        private final Map<String, File> files;

        public ZipExtracted(LinkedList<String> testClasses, LinkedList<String> classes, File root, Map<String, File> files) {
            this.testClasses = testClasses;
            this.classes = classes;
            this.root = root;
            this.files = files;
        }

        @Override
        public List<String> testClasses() {
            return new LinkedList<>(testClasses);
        }

        @Override
        public List<String> classes() {
            return new LinkedList<>(classes);
        }

        @Override
        public List<File> files() {
            return new LinkedList<>(files.values());
        }

        @Override
        public Map<String, File> javafileByClassname() {
            return new HashMap<>(files);
        }

        @Override
        public File root() {
            return root;
        }

        @Override
        public String toString() {
            return "ZipExtracted{" +
                    "testClasses=" + testClasses +
                    ", classes=" + classes +
                    ", files=" + files +
                    '}';
        }
    }

    private final List<File> toDelete = new LinkedList<>();

    @Override
    public FileInfo download(FileInfo file) throws Exception {
        File copy = File.createTempFile(file.getId(), file.getExtension());
        toDelete.add(copy);

        try (InputStream input = new URL(file.getUrl()).openStream()) {
            try (OutputStream output = new FileOutputStream(copy)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                while (input.available() > BUFFER_SIZE) {
                    input.read(buffer);
                    output.write(buffer);
                }
                int remaining = input.available();
                input.read(buffer, 0, remaining);
                output.write(buffer, 0, remaining);
            }
        }

        FileInfo copyInfo =  new FileInfo();
        copyInfo.setSubmittedFile(copy);
        copyInfo.setId(copy.getName().substring(0, copy.getName().lastIndexOf(".")));
        copyInfo.setExtension(file.getExtension());
        copyInfo.setUrl(SCHEME + copy.getAbsolutePath());
        copyInfo.setPath(copy.getAbsolutePath());
        copyInfo.setMimetype(file.getMimetype());
        return copyInfo;
    }

    @Override
    public Extracted extract(FileInfo file, TestClass testClass, Classname classname) throws Exception {
        if (! file.getMimetype().equals(MIMETYPE))
            return null;

        File unzipTarget = Files.createTempDirectory("exam-results").toFile();
        toDelete.add(unzipTarget);
        ZipFile zipFileA = new ZipFile(FileUtils.getFile(file.getPath()));
        zipFileA.extractAll(unzipTarget.toString());
        return extracted(unzipTarget, testClass, classname);
    }


    @Override
    public Extracted extractBoth(FileInfo fileA, FileInfo fileB, TestClass testClass, Classname classname) throws Exception {
        if (! (fileA.getMimetype().equals(MIMETYPE) && fileB.getMimetype().equals(MIMETYPE)))
            return null;

        File unzipTarget = Files.createTempDirectory("exam-results").toFile();

        toDelete.add(unzipTarget);
        ZipFile zipFileA = new ZipFile(FileUtils.getFile(fileA.getPath()));
        zipFileA.extractAll(unzipTarget.toString());
        ZipFile zipFileB = new ZipFile(FileUtils.getFile(fileB.getPath()));
        zipFileB.extractAll(unzipTarget.toString());
        return extracted(unzipTarget, testClass, classname);
    }


    private Extracted extracted(File unzipTarget, TestClass testClass, Classname classname) {
        LinkedList<File> stack = new LinkedList<>();
        Map<String, File> files = new HashMap<>();
        LinkedList<String> testClasses = new LinkedList<>();
        LinkedList<String> classes = new LinkedList<>();
        stack.add(unzipTarget);

        while (!stack.isEmpty()) {
            File first = stack.removeFirst();
            if (first.isDirectory()) {
                stack.addAll(0, Arrays.asList(first.listFiles()));
            } else if (Pattern.matches(".*\\.java$", first.getName())) {
                String name = classname.parse(first);
                if (Objects.isNull(name))
                    continue;
                name = name.replace("/",".");
                files.put(name, first);
                if (testClass.isTrue(first)) {
                    testClasses.add(name);
                } else {
                    classes.add(name);
                }
            }
        }
        return new ZipExtracted(
                testClasses,
                classes,
                unzipTarget,
                files);
    }

    @Override
    public void cleanUp() {
        for (File file : toDelete) {
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
    }
}
