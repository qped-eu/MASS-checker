package eu.qped.java.style.analyse.pmd;

import eu.qped.java.checkers.style.analyse.pmd.XmlFileManager;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class XmlFileManagerTest {
    private Path testFile;

    @Before
    public void setUp() throws IOException {
        testFile = Files.createTempFile(null, null);

        URL url = new URL("https://raw.githubusercontent.com/pmd/pmd/master/src/main/resources/rulesets/unusedcode.xml");
        try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream outputStream = new FileOutputStream(testFile.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    @Ignore
    @Test
    public void addToMainRuleset() {
        XmlFileManager xmlFileManager = new XmlFileManager();
        xmlFileManager.addToMainRuleset(testFile.toString());
        Assert.assertTrue(xmlFileManager.toString().contains("<rule name=\"UnusedLocalVariable\">"));
    }

    @Ignore
    @Test
    public void testEditProperty() throws Exception {
        // Create a temporary file with some PMD rules
        File tempFile = File.createTempFile("test-ruleset", ".xml");
        tempFile.deleteOnExit();
        FileWriter writer = new FileWriter(tempFile);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<ruleset xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\">\n");
        writer.write("  <rule name=\"Rule1\" language=\"java\" message=\"Message1\">\n");
        writer.write("    <description>Description1</description>\n");
        writer.write("    <priority>3</priority>\n");
        writer.write("    <properties>\n");
        writer.write("      <property name=\"Property1\" value=\"Value1\"/>\n");
        writer.write("    </properties>\n");
        writer.write("  </rule>\n");
        writer.write("  <rule name=\"Rule2\" language=\"java\" message=\"Message2\">\n");
        writer.write("    <description>Description2</description>\n");
        writer.write("    <priority>5</priority>\n");
        writer.write("    <properties>\n");
        writer.write("      <property name=\"Property2\" value=\"Value2\"/>\n");
        writer.write("    </properties>\n");
        writer.write("  </rule>\n");
        writer.write("</ruleset>\n");
        writer.close();

        // Initialize the XmlFileManager and add the rules from the temp file to the main ruleset
        XmlFileManager xmlFileManager = new XmlFileManager();
        xmlFileManager.addToMainRuleset(tempFile.getAbsolutePath());

        // Edit a property in one of the rules
        xmlFileManager.editProperty("Rule1", "NewValue", "Property1");

        // Verify that the property was edited correctly
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xmlFileManager.getDocument());
        StringWriter writer2 = new StringWriter();
        StreamResult result = new StreamResult(writer2);
        transformer.transform(source, result);
        String xmlString = writer2.toString();
        assertThat(xmlString, containsString("<property name=\"Property1\" value=\"NewValue\"/>"));
    }

    private void assertThat(String xmlString, Matcher<String> containsString) {
        if (!containsString.matches(xmlString)) {
            throw new AssertionError("Expected string to contain '" + containsString + "', but it didn't:\n" + xmlString);
        }
    }


}
