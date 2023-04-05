package eu.qped.java.checkers.style.analyse.pmd;

import eu.qped.java.utils.FileExtensions;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**

 The XmlFileManager class provides methods to create, read, and modify XML files
 for PMD static code analysis tool.
 */

public class XmlFileManager {

    /**
     * Template of the main PMD ruleset XML file, in the format specified by PMD.
     */
    private static final String MAIN_RULESET_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<ruleset xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\"\n"
                    + "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"ruleset\"\n"
                    + "	xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd\">\n"
                    + "	<description>\n"
                    + "	Effective MASS ruleset.\n"
                    + "	</description>\n"
                    + "</ruleset>";
    private Document document;
    private File mainRulesetFile;

    /**
     * Constructs an XmlFileManager object and initializes the document field
     * by parsing the MAIN_RULESET_TEMPLATE.
     */
    public XmlFileManager() {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(MAIN_RULESET_TEMPLATE)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LogManager.getLogger(getClass()).throwing(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the rules in the specified XML file to the main PMD ruleset XML file.
     *
     * @param path the path of the XML file to be added to the main ruleset
     */
    public void addToMainRuleset(final String path) {
        final XmlParser xmlParser = new XmlParser(path);
        final NodeList rules = xmlParser.parse();
        writeIntoMainRuleset(rules);
    }

    /**
     * Writes the specified node list into the main PMD ruleset document.
     *
     * @param nodeList the node list to be written into the main ruleset document
     */
    private void writeIntoMainRuleset(final NodeList nodeList) {
        final Element root = document.getDocumentElement();
        final int length = nodeList.getLength();

        for (int i = 0; i < length; i++) {
            final Node node = nodeList.item(i);
            final Node copy = document.importNode(node, true);
            root.appendChild(copy);
        }
    }

    /**

     Edits the value of a property for a given rule in the PMD configuration document.
     @param ruleName The name of the rule to edit the property for.
     @param newValue The new value for the property.
     @param propName The name of the property to edit.
     @throws PmdConfigException If either the ruleName or propName is null or if the rule or property is not found.
     */
    public void editProperty(final String ruleName, final String newValue, final String propName)
            throws PmdConfigException {

        if (ruleName == null || propName == null) {
            throw new PmdConfigException(ruleName, propName);
        }

        final NodeList allNodes = document.getElementsByTagName("rule");
        Node searchedNode = null;


        for (int i = 0; i < allNodes.getLength(); i++) {
            final Node temp = allNodes.item(i);
            if (temp.getAttributes().getNamedItem("name").getTextContent().equals(ruleName)) {
                searchedNode = temp;
            }
        }
        if (searchedNode == null) {
            throw new PmdConfigException("Rule not found.", ruleName, propName);
        }

        final NodeList allProperties = searchedNode.getChildNodes();
        boolean propFound = false;
        for (int i = 0; i < allProperties.getLength(); i++) {
            //item is the Rule here.
            final NodeList ruleProperties = allProperties.item(i).getChildNodes();
            for (int j = 0; j < ruleProperties.getLength(); j++) {
                final Node tempAtt = ruleProperties.item(j);
                if (tempAtt.getNodeType() == Node.ELEMENT_NODE) {
                    final String property = tempAtt.getAttributes().getNamedItem("name").getTextContent();
                    if (propName.equals(property.trim())) {
                        tempAtt.getAttributes().getNamedItem("value").setNodeValue(newValue);
                        propFound = true;
                        break;
                    }
                }
            }
        }
        if (!propFound) {
            throw new PmdConfigException("Property not found.", ruleName, propName);
        }

    }

    /**

     Gets the file name for the main PMD ruleset file. If it has not been set yet,
     it generates a temporary file using the current PMD configuration document.
     @return The file path of the main PMD ruleset file.
     */
    public String getFilename() {
        if (mainRulesetFile == null) {
            try {
                document.normalize();
                final TransformerFactory transformerFactory = TransformerFactory.newInstance();

                final Transformer transformer = transformerFactory.newTransformer();
                final DOMSource domSource = new DOMSource(document);

                mainRulesetFile = File.createTempFile("mainRuleset", FileExtensions.XML);

                final StreamResult streamResult = new StreamResult(mainRulesetFile.toURI().getPath());
                transformer.transform(domSource, streamResult);

                // make sure that the document is not edited anymore.
                document = null;
            } catch (TransformerFactoryConfigurationError | IOException | TransformerException e) {
                LogManager.getLogger(getClass()).throwing(e);
                throw new RuntimeException(e);
            }
        }
        return mainRulesetFile.getPath();
    }

    public Document getDocument() {
        return document;
    }

    public String getMainRulesetFileName() {
        return mainRulesetFile.getName();
    }

    public File getMainRulesetFile() {
        return mainRulesetFile;
    }
}
