package eu.qped.java.checkers.coverage.testhelp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Preprocessing {
    Map<String, File> filesByClassName;
    public Preprocessed processing(
            Map<String, File> filesByClassName,
            Map<String,String> newName,
            List<String> testClasses,
            List<String> classes) {
        this.filesByClassName = filesByClassName;
        LinkedList<String> failed = new LinkedList<>();
        LinkedList<PreprocessedClass> tc = processing(testClasses,newName,failed);
        LinkedList<PreprocessedClass> c = processing(classes,newName,failed);
        return new Preprocessed(tc, c, failed);
    }

    private LinkedList<PreprocessedClass> processing(
            List<String> classNames,
            Map<String,String> newName,
            List<String> failed) {
        LinkedList<PreprocessedClass> processed = new LinkedList<>();
        for (String name : classNames) {
            try {
                String content = readJavaFile(name);
                byte[] byteCode = readByteCode(name);
                if (! (! content.isBlank() && byteCode.length != 0)) {
                    failed.add(name);
                } else {
                    if (newName.containsKey(name)) {
                        name = newName.get(name);
                    }
                    processed.add(new PreprocessedClass(name, content, byteCode));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                failed.add(name);
            }
        }
        return processed;
    }

    public Preprocessed processingOnlyContent(
            Map<String, File> filesByClassName,
            Map<String,String> newName,
            List<String> testClasses,
            List<String> classes) {
        this.filesByClassName = filesByClassName;
        LinkedList<String> failed = new LinkedList<>();
        LinkedList<PreprocessedClass> tc = processingOnlyContent(testClasses,newName,failed);
        LinkedList<PreprocessedClass> c = processingOnlyContent(classes,newName,failed);
        return new Preprocessed(tc, c, failed);
    }

    private LinkedList<PreprocessedClass> processingOnlyContent (
            List<String> classNames,
            Map<String,String> newName,
            List<String> failed) {
        LinkedList<PreprocessedClass> processed = new LinkedList<>();
        for (String name : classNames) {
            try {
                String content = readJavaFile(name);

                if (content.isBlank()) {
                    failed.add(name);
                } else {
                    if (newName.containsKey(name)) {
                        name = newName.get(name);
                    }
                    processed.add(new PreprocessedClass(name, content, new byte[]{}));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                failed.add(name);
            }
        }
        return processed;
    }


    public Preprocessed processingOnlyByteCode(
            Map<String, File> filesByClassName,
            Map<String,String> newName,
            List<String> testClasses,
            List<String> classes) {
        this.filesByClassName = filesByClassName;
        LinkedList<String> failed = new LinkedList<>();
        LinkedList<PreprocessedClass> tc = processingOnlyByteCode(testClasses,newName,failed);
        LinkedList<PreprocessedClass> c = processingOnlyByteCode(classes,newName,failed);
        return new Preprocessed(tc, c, failed);
    }

    private LinkedList<PreprocessedClass> processingOnlyByteCode(
            List<String> classNames,
            Map<String,String> newName,
            List<String> failed) {
        LinkedList<PreprocessedClass> processed = new LinkedList<>();
        for (String name : classNames) {
            try {
                byte[] byteCode = readByteCode(name);
                if (byteCode.length == 0) {
                    failed.add(name);
                } else {
                    if (newName.containsKey(name)) {
                        name = newName.get(name);
                    }
                    processed.add(new PreprocessedClass(name, "content", byteCode));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                failed.add(name);
            }
        }
        return processed;
    }

    private byte[] readByteCode(String name) throws Exception {
        return  Files.readAllBytes(Paths.get(filesByClassName.get(name).getPath().replaceAll("\\.java", ".class")));
    }

    private String readJavaFile(String name) throws Exception {

        String content = Files.readAllLines(filesByClassName.get(name).toPath())
                .stream()
                .collect(Collectors.joining("\n"));
        return content;
    }

}
