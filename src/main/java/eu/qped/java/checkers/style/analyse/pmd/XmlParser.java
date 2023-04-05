package eu.qped.java.checkers.style.analyse.pmd;

import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**

 The XmlParser class provides functionality to parse an XML file and extract information from it.
 */

public class XmlParser {

    private final String path;

    /**
     * Constructs a new XmlParser object with the specified path.
     *
     * @param path the path to the XML file to be parsed
     */
    public XmlParser(final String path) {
        this.path = path;
    }

    /**
     * Parses the XML file specified in the path field and returns a NodeList containing all 'rule' elements.
     *
     * @return a NodeList containing all 'rule' elements in the XML file
     */
    public NodeList parse() {

        // Make an  instance of the DocumentBuilderFactory
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null;
        try {
            // use the Builder to take an instance of the document builder
            final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            // addToMainRuleset using the builder to get the DOM mapping of the
            // XML file
            Document inputDom;

            if (path != null) {
                inputDom = documentBuilder.parse(this.getClass().getClassLoader().getResourceAsStream(path));
                final Element doc = inputDom.getDocumentElement();
                nodeList = doc.getElementsByTagName("rule");
            } else {
                LogManager.getLogger(getClass()).error("No such ruleset: " + path);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
        return nodeList;
    }
}

