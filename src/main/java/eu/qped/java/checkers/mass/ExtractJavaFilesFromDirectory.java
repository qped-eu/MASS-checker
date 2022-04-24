package eu.qped.java.checkers.mass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtractJavaFilesFromDirectory {
    String filePath;

    public List<File> getFilesRecursively(File fileName) {
        List<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(fileName.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                files.addAll(getFilesRecursively(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }

    public List<File> filesWithJavaExtension() {
        List<File> allFiles = new ArrayList<>();
        List<File> filesWithJavaExtension = new ArrayList<>();
        File f = new File(filePath);

        if (f.exists()) {
            if (f.isDirectory()) {
                allFiles.add(f);
                allFiles.addAll(getFilesRecursively(f));
            }
        } else {
            allFiles.add(new File(filePath));
        }
        for (File file : allFiles) {
            if (FilenameUtils.getExtension(String.valueOf(file)).equals("java")) {
                filesWithJavaExtension.add(file);
            }
        }
        return filesWithJavaExtension;
    }
}