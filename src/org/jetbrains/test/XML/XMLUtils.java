package org.jetbrains.test.XML;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;

/**
 * Created by Vlasimir Shefer on 09.05.2017.
 *
 */
public class XMLUtils {

    public static Document getNewDocument(){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Transformer getNewTransformer(){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            return transformer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveDocument(Document document, String filename) {
        try {
            getNewTransformer().transform(
                    new DOMSource(document),
                    new StreamResult(filename)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static XMLStreamReader getNewReader(String filename){
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileReader(filename));
            return reader;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create XML reader");
        }
    }

}
