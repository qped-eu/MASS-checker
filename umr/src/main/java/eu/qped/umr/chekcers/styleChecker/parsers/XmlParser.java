package eu.qped.umr.chekcers.styleChecker.parsers;

import eu.qped.umr.exceptions.NoSuchRulesetException;
import eu.qped.umr.helpers.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlParser implements Parser {

    private final String path;

    private XmlParser(String path){
        this.path = path;
    }

    public static XmlParser createXmlParser(String path){
        return new XmlParser(path);
    }

    @Override
    public NodeList parse () {

        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null;
        try {
            // use the Builder to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // addToMainRuleset using the builder to get the DOM mapping of the
            // XML file
            Document inputDom;

            if (path != null){
               inputDom  = db.parse(path);
            }
            else {
                throw new NoSuchRulesetException("");
            }

            Element doc = inputDom.getDocumentElement();
            nodeList = doc.getElementsByTagName("rule");

        } catch (ParserConfigurationException | SAXException | IOException | NoSuchRulesetException pce) {
            Logger.getInstance().log(pce.getMessage() + " " + pce.getCause());
        }
        return nodeList;
    }
}

