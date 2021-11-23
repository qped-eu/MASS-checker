package eu.qped.umr.checkers.styleChecker.managers;

import eu.qped.umr.checkers.styleChecker.parsers.XmlParser;
import eu.qped.umr.helpers.Logger;
import eu.qped.umr.exceptions.NoSuchPropertyException;
import eu.qped.umr.exceptions.NoSuchRuleException;
import eu.qped.umr.exceptions.NoSuchRulesetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

public class XmlFileManager {


    private XmlFileManager(){
    }

    public static XmlFileManager createXmlFileManager() {
        return new XmlFileManager();
    }

    public void addToMainRuleset(String path) {
        XmlParser xmlParser = XmlParser.createXmlParser(path);
        NodeList rules = xmlParser.parse();
        writeIntoMainRuleset(rules);
    }

    private void writeIntoMainRuleset(NodeList nodeList) {
        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse("xmls/mainRuleset.xml");

            Element root = document.getDocumentElement();
            int length = nodeList.getLength();

            for (int i = 0; i < length; i++) {
                Node node = nodeList.item(i);
                Node copy = document.importNode(node, true);
                root.appendChild(copy);
            }

            transform(document);

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause());
        }
    }

    private void transform(Document document) throws TransformerException {
        document.normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        StreamResult streamResult = new StreamResult("xmls/mainRuleset.xml");
        transformer.transform(domSource, streamResult);
    }

    public void editProperty(String xmlPath, String ruleName, String newValue, String propName)
            throws NoSuchRuleException, NoSuchPropertyException, NoSuchRulesetException {

        if (xmlPath == null) {
            throw new NoSuchRulesetException("This Ruleset does not exist within our Files.");
        }
        if (ruleName == null) {
            throw new NoSuchRuleException("The system cannot find the Rule ", xmlPath, null);
        }
        if (propName == null) {
            throw new NoSuchPropertyException("The System cannot find this Property in the specified Rule. ", null, xmlPath);
        }

        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(xmlPath);

            NodeList allNodes = document.getElementsByTagName("rule");
            Node searchedNode = null;


            for (int i = 0; i < allNodes.getLength(); i++) {
                Node temp = allNodes.item(i);
                if (temp.getAttributes().getNamedItem("name").getTextContent().equals(ruleName)) {
                    searchedNode = temp;
                }
            }
            if (searchedNode == null) {
                throw new NoSuchRuleException("The system cannot find the Rule ", xmlPath, ruleName);
            }

            NodeList allProperties = searchedNode.getChildNodes();
            boolean propFound = false;
            for (int i = 0; i < allProperties.getLength(); i++) {
                //item is the Rule here.
                NodeList ruleProperties = allProperties.item(i).getChildNodes();
                for (int j = 0; j < ruleProperties.getLength(); j++) {
                    Node tempAtt = ruleProperties.item(j);
                    if (tempAtt.getNodeType() == Node.ELEMENT_NODE) {
                        String property = tempAtt.getAttributes().getNamedItem("name").getTextContent();
                        if (propName.equals(property.trim())) {
                            tempAtt.getAttributes().getNamedItem("value").setNodeValue(newValue);
                            propFound = true;
                            break;
                        }
                    }
                }
            }
            if (!propFound) {
                throw new NoSuchPropertyException("The System cannot find the Property ", propName, xmlPath);
            }

            transform(xmlPath, document);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " here");
        }
    }

    public void clearXmlFile() {
        Document document;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse("xmls/mainRuleset.xml");
            Node root = document.getDocumentElement();
            while (root.hasChildNodes()) {
                root.removeChild(root.getFirstChild());
            }
            transform("xmls/mainRuleset.xml", document);

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            Logger.getInstance().log(e.getMessage() + " " + e.getCause() + " here 2");
        }
    }

    private void transform(String xmlPath, Document document) throws TransformerException {
        document.normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        StreamResult streamResult = new StreamResult(xmlPath);
        transformer.transform(domSource, streamResult);
    }
}
