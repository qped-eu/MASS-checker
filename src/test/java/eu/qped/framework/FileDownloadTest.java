package eu.qped.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.lingala.zip4j.exception.ZipException;

class FileDownloadTest {

	private static final String JAVA_FILE_PATH = "./src/test/resources/test-files/EmptyMain.java";
	private static final String ZIP_FILE_PATH = "./src/test/resources/test-files/hello.zip";
	private static final String ZIP_CONTENT_PATH = "./src/test/resources/test-files/hello/Hello.java";
	
	@Test
	void testDownloadFileLocal() throws FileNotFoundException, ZipException, IOException {
		FileInfo fileInfo = FileInfo.createForUrl("file:" + JAVA_FILE_PATH, "text/plain");
		
		QpedQfFilesUtility.downloadAndUnzipIfNecessary(fileInfo);
		
		assertNotNull(fileInfo.getDownloadedFile());
		assertNull(fileInfo.getUnzippedDirectory());
		
		String downloaded = FileUtils.readFileToString(fileInfo.getDownloadedFile(), Charset.defaultCharset());
		String original = FileUtils.readFileToString(new File(JAVA_FILE_PATH), Charset.defaultCharset());
		
		assertEquals(original, downloaded);
	}

	@ParameterizedTest
	@ValueSource(strings = {"application/zip", "application/x-zip-compressed", "application/zip-compressed"})
	void testDownloadArchiveLocal(String mimeType) throws FileNotFoundException, ZipException, IOException {
		FileInfo fileInfo = FileInfo.createForUrl("file:" + ZIP_FILE_PATH, mimeType);
		
		QpedQfFilesUtility.downloadAndUnzipIfNecessary(fileInfo);
		
		assertNotNull(fileInfo.getDownloadedFile());
		assertNotNull(fileInfo.getUnzippedDirectory());
		
		String downloaded = FileUtils.readFileToString(
				new File(fileInfo.getUnzippedDirectory(), "hello/Hello.java"), Charset.defaultCharset());
		String original = FileUtils.readFileToString(new File(ZIP_CONTENT_PATH), Charset.defaultCharset());
		
		assertEquals(original, downloaded);
	}

}
