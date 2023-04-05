package eu.qped.java.style.analyse.pmd;

import static org.junit.Assert.*;

import eu.qped.java.checkers.style.analyse.pmd.PmdConfigException;
import eu.qped.java.checkers.style.analyse.pmd.XmlFileManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

public class XmlFileManagerTest {
    private XmlFileManager xmlFileManager;
    private String testFilePath;

    @Before
    public void setUp() {
        xmlFileManager = new XmlFileManager();
        testFilePath = Paths.get("src", "test", "resources", "test-rules.xml").toString();
    }

    @Ignore
    @Test
    public void testAddToMainRuleset() {
        // Ensure file is not already added
        assertFalse(xmlFileManager.getDocument().getElementsByTagName("rule").getLength() > 0);

        // Add test file
        xmlFileManager.addToMainRuleset(testFilePath);

        // Ensure file is added
        assertTrue(xmlFileManager.getDocument().getElementsByTagName("rule").getLength() > 0);
    }

    @Ignore
    @Test
    public void testEditProperty() throws PmdConfigException {
        final String ruleName = "TestRule";
        final String propName = "minimumPriority";
        final String newValue = "3";

        // Add test file
        xmlFileManager.addToMainRuleset(testFilePath);

        // Ensure original property value
        final String originalValue = xmlFileManager.getDocument()
                .getElementsByTagName("rule")
                .item(0)
                .getChildNodes()
                .item(3)
                .getAttributes()
                .getNamedItem("value")
                .getTextContent();

        assertEquals("5", originalValue);

        // Edit property value
        xmlFileManager.editProperty(ruleName, newValue, propName);

        // Ensure new property value
        final String editedValue = xmlFileManager.getDocument()
                .getElementsByTagName("rule")
                .item(0)
                .getChildNodes()
                .item(3)
                .getAttributes()
                .getNamedItem("value")
                .getTextContent();

        assertEquals(newValue, editedValue);
    }


    @Test(expected = PmdConfigException.class)
    public void testEditPropertyNullArguments() throws PmdConfigException {
        xmlFileManager.editProperty(null, null, null);
    }

    @Ignore
    @Test(expected = PmdConfigException.class)
    public void testEditPropertyRuleNotFound() throws PmdConfigException {
        xmlFileManager.addToMainRuleset(testFilePath);
        xmlFileManager.editProperty("NonExistentRule", "3", "minimumPriority");
    }

    @Ignore
    @Test(expected = PmdConfigException.class)
    public void testEditPropertyPropertyNotFound() throws PmdConfigException {
        xmlFileManager.addToMainRuleset(testFilePath);
        xmlFileManager.editProperty("TestRule", "3", "NonExistentProperty");
    }
}
