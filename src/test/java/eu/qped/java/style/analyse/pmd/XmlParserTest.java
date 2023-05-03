package eu.qped.java.style.analyse.pmd;

import eu.qped.java.checkers.style.analyse.pmd.XmlParser;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

public class XmlParserTest {

    @Ignore
    @Test
    public void testParse() throws IOException {
        // Download the sample XML file from the internet
        final URL url = new URL("https://www.w3schools.com/xml/note.xml");
        final String path = url.getFile().substring(1); // Remove leading slash
        final XmlParser parser = new XmlParser(path);

        // Parse the XML file and get the NodeList
        final NodeList nodeList = parser.parse();

        // Assert that the NodeList contains exactly one 'to' element with the value 'Tove'
        MatcherAssert.assertThat(nodeList.getLength(), CoreMatchers.is(1));
        MatcherAssert.assertThat(nodeList.item(0).getFirstChild().getNodeName(), CoreMatchers.is("to"));
        MatcherAssert.assertThat(nodeList.item(0).getFirstChild().getTextContent(), CoreMatchers.is("Tove"));
    }
}
