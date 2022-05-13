package eu.qped.java.checkers.coverage;

import eu.qped.java.checkers.coverage.testhelp.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MemoryLoaderTest {

    @Test
    public void upload() {
        Preprocessed preprocessed = new Preprocessing().processingOnlyByteCode(
                FileResources.filesByClassName,
                FileResources.convertNames,
                Arrays.asList("test.TestFramework"),
                Arrays.asList("test.TestFrameworkIsCorrect"));

        CovInformation wantTest = preprocessed.getTestClasses().get(0);
        CovInformation want = preprocessed.getClasses().get(0);

        MemoryLoader memoryLoader = new MemoryLoader();
        memoryLoader.upload(wantTest.className(), wantTest.byteCode());
        memoryLoader.upload(want.className(), want.byteCode());

        try {
            assertEquals(wantTest.className(), memoryLoader.loadClass(wantTest.className()).getName());
            assertEquals(want.className(), memoryLoader.loadClass(want.className()).getName());
        } catch (ClassNotFoundException e) {
            assertTrue(false, e.getMessage());
        }
    }

}