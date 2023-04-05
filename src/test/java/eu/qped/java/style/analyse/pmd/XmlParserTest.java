package eu.qped.java.style.analyse.pmd;

import eu.qped.java.checkers.style.analyse.pmd.XmlParser;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertNull;

public class XmlParserTest {

    @Test
    public void testParseWithNullPath() {
        XmlParser xmlParser = new XmlParser(null);
        assertNull(xmlParser.parse());
    }
}