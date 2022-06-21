package eu.qped.framework;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            FileInfo download = zip.download(fileA);
            assertTrue(Pattern.matches("^hello_renamed\\d+", download.getId()), "id not matching");
            assertEquals(".zip", download.getExtension(), "extension not equals");
            assertTrue(Pattern.matches("^/tmp/hello_renamed\\d+\\.zip", download.getPath()), "path not matching");
            assertTrue(Pattern.matches("^file:/.*/hello_renamed\\d+\\.zip", download.getUrl()), "url not matching");
            zip.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void extractBoth() {
        FileInfo fileA = new FileInfo();
        fileA.setMimetype("application/zip");
        fileA.setUrl("file:./src/test/resources/system-tests/framework/testcoverage-pg-bag-copy/PG-Bag-ANSWER.zip");
        fileA.setExtension(".zip");
        fileA.setId("PG-Bag-ANSWER");
        fileA.setPath("src/test/resources/system-tests/framework/testcoverage-pg-bag-copy/PG-Bag-ANSWER.zip");

        FileInfo fileB = new FileInfo();
        fileB.setMimetype("application/zip");
        fileB.setUrl("file:./src/test/resources/system-tests/framework/testcoverage-pg-bag-copy/PG-Bag-ASSIGNEMENT.zip");
        fileB.setExtension(".zip");
        fileB.setId("PG-Bag-ASSIGNEMENT");
        fileB.setPath("src/test/resources/system-tests/framework/testcoverage-pg-bag-copy/PG-Bag-ASSIGNEMENT.zip");

        try {
            Zip toTest = new Zip();
            fileA = toTest.download(fileA);
            fileB = toTest.download(fileB);
            ZipService.Extracted extracted = toTest.extractBoth(
                    fileA,
                    fileB,
                    (file) -> Pattern.matches(".*Test\\.java$", file.getName()),
                    (file) -> {
                        Pattern pattern = Pattern.compile("tmp/exam-results\\d+/(PG-Bag-ASSIGNEMENT|PG-Bag-ANSWER)/(.*)\\.java$");
                        Matcher matcher = pattern.matcher(file.getAbsolutePath());
                        if (matcher.find()) {
                            return matcher.group(2);
                        }
                        return null;});
            assertTrue(Objects.deepEquals(List.of("adt.BagTest"), extracted.testClasses()));
            assertTrue(Objects.deepEquals(List.of("adt.Bag"), extracted.classes()));
            toTest.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}