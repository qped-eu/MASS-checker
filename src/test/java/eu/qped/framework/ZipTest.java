package eu.qped.framework;

import eu.qped.java.checkers.coverage.Zip;
import eu.qped.java.checkers.coverage.ZipService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

import static org.junit.jupiter.api.Assertions.*;

class ZipTest {

    @Test
    public void downloadTest() {
        FileInfo fileA = new FileInfo();
        fileA.setPath("src/test/resources/system-tests/framework/file-upload/hello_renamed.zip");
        fileA.setUrl("file:./src/test/resources/system-tests/framework/file-upload/hello_renamed.zip");
        fileA.setId("hello_renamed");
        fileA.setExtension(".zip");
        fileA.setMimetype("application/zip");

        Zip zip = new Zip();
        try {
            File downloaded = zip.download(fileA.getUrl());
            FileInfo download = new FileInfo();
            download.setPath(downloaded.getPath());
            download.setUrl("file:"+downloaded.getPath());
            download.setId(downloaded.getName());
            download.setExtension(FilenameUtils.getExtension(downloaded.getName()));
            download.setMimetype(Files.probeContentType(downloaded.toPath()));
            assertEquals("zip", download.getExtension(), "extension not equals");
            assertTrue(Pattern.matches("^.*hello_renamed\\d+\\.zip", download.getPath()), "path not matching, was "+download.getPath());
            assertTrue(Pattern.matches("^file:.*hello_renamed\\d+\\.zip", download.getUrl()), "url not matching, was "+download.getUrl());
            assertTrue(new File(download.getPath()).exists(), "downloaded file does not exist");
            zip.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void extractBoth() {
        FileInfo fileA = new FileInfo();
        fileA.setMimetype("application/zip");
        fileA.setUrl("file:./src/test/resources/system-tests/framework/testcoverage-pg-bag/PG-Bag-ANSWER.zip");
        fileA.setExtension(".zip");
        fileA.setId("PG-Bag-ANSWER");
        fileA.setPath("src/test/resources/system-tests/framework/testcoverage-pg-bag/PG-Bag-ANSWER.zip");

        FileInfo fileB = new FileInfo();
        fileB.setMimetype("application/zip");
        fileB.setUrl("file:./src/test/resources/system-tests/framework/testcoverage-pg-bag/PG-Bag-ASSIGNEMENT.zip");
        fileB.setExtension(".zip");
        fileB.setId("PG-Bag-ASSIGNEMENT");
        fileB.setPath("src/test/resources/system-tests/framework/testcoverage-pg-bag/PG-Bag-ASSIGNEMENT.zip");

        try {
            Zip toTest = new Zip();
            File downloadA = toTest.download(fileA.getUrl());
            File downloadB = toTest.download(fileB.getUrl());
            ZipService.Extracted extracted = toTest.extractBoth(
                    downloadA,
                    downloadB,
                    (file) -> Pattern.matches(".*[tT]est\\.java$", file.getName()),
                    (file) -> {
                        Pattern pattern = Pattern.compile("^.*(PG-Bag-ASSIGNEMENT|PG-Bag-ANSWER)(.*)\\.java$");
                        Matcher matcher = pattern.matcher(file.getAbsolutePath());
                        if (matcher.find()) {
                            return FilenameUtils.getName(matcher.group(2));
                        }
                        return null;});
            assertTrue(Objects.deepEquals(List.of("adt.BagTest"), extracted.testClasses()), "Test Classes not matching, was length of "+extracted.testClasses().size() +" name was "+ extracted.testClasses().get(0));
            assertTrue(Objects.deepEquals(List.of("adt.Bag"), extracted.classes()), "Classes not matching, was length of"+extracted.classes().size());
            toTest.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}