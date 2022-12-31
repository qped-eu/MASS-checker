package eu.qped.framework;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QpedQfFilesUtility {

    public static List<File> filesWithExtension(String dirPath, String extension) {
        List<File> allFiles = new ArrayList<>();
        List<File> filesWithJavaExtension = new ArrayList<>();
        File fileOrDirectory = new File(Objects.requireNonNull(dirPath));
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                allFiles.add(fileOrDirectory);
                allFiles.addAll(getFilesRecursively(fileOrDirectory));
            } else {
                allFiles.add(new File(fileOrDirectory.getPath()));
            }
        }
        for (File file : allFiles) {
            if (FilenameUtils.getExtension(String.valueOf(file)).equals(extension)) {
                filesWithJavaExtension.add(file);
            }
        }
        return filesWithJavaExtension;
    }

    private static List<File> getFilesRecursively(File path) {
        List<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                files.addAll(getFilesRecursively(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }

	private static List<File> tempFiles = new ArrayList<>();

	public static void cleanupTempFiles() {
		for (File file : tempFiles) {
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
		tempFiles.clear();
	}

	/**
	 * This method downloads a file from URL of the provided argument to a local temp file, and unzips it
	 * to a local temp directory of the mime type is zip.
	 * The allocated temporary file and possibly the directory with all its contained files and directories
	 * will be deleted at the end of the execution of this program. I.e., when the main-method in this class ends.
	 * The location of the downloaded file and (in case of a zip file) the location of the extracted files are
	 * set as properties of the passed FileInfo object.
	 * The downloaded file is unzipped if the passed fileInfo specifies as mime type either
	 * "application/zip", "application/zip-compressed" or "application/x-zip-compressed".
	 * 
	 * @param fileInfo
	 * @return The directory containing the downloaded file, if it is a plain file, or the unzipped contents in 
	 * case the downloaded file was a zip archive.
	 * @throws FileNotFoundException
	 * @throws MalformedURLException
	 * @throws ZipException
	 * @throws IOException
	 */
	public static File downloadAndUnzipIfNecessary(FileInfo fileInfo) throws FileNotFoundException, MalformedURLException, ZipException, IOException {
		File targetDirectory = createManagedTempDirectory();
		File submittedFile = new File(targetDirectory, fileInfo.getId() + fileInfo.getExtension());
	
		try (InputStream input = new URL(fileInfo.getUrl()).openStream()) {
			try (OutputStream output = new FileOutputStream(submittedFile)) {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead;
				while ((bytesRead = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}
		}
		fileInfo.setDownloadedFile(submittedFile);
		
		if (fileInfo.getMimetype().contains("application/x-zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip") ) {
			File unzipTarget = createManagedTempDirectory();
	
			ZipFile zipFile = new ZipFile(fileInfo.getDownloadedFile());
			zipFile.extractAll(unzipTarget.toString());
	
			fileInfo.setUnzippedDirectory(unzipTarget);
			return unzipTarget;
		}
		
		return targetDirectory;
	}

	public static File createManagedTempDirectory() throws IOException {
		File unzipTarget = Files.createTempDirectory("qf-checker").toFile();
		tempFiles.add(unzipTarget);
		return unzipTarget;
	}

	public static File createManagedTempFile(String filename, String extension) throws IOException {
		File submittedFile = File.createTempFile(filename, extension);
		tempFiles.add(submittedFile);
		return submittedFile;
	}
}