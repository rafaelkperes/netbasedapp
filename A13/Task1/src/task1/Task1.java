/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task1;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author rafaelkperes
 */
public class Task1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {

        // Task 1.2 (xml schema validation)
        SchemaFactory schemaFactory
                = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("people.xsd"));
        DocumentBuilderFactory parserFactory
                = DocumentBuilderFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        parserFactory.setValidating(false);
        parserFactory.setSchema(schema);
        parserFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder parser = parserFactory.newDocumentBuilder();

        // Task 1.1
        File inputFile = new File("scientists.xml");

//        DocumentBuilderFactory dbFactory
//                = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = parser.parse(inputFile);
        doc.getDocumentElement().normalize();

        // create new person
        Element person = doc.createElement("person");
        Element name = doc.createElement("name");
        Element firstName = doc.createElement("firstname");
        firstName.appendChild(doc.createTextNode("Denis"));
        Element lastName = doc.createElement("lastname");
        lastName.appendChild(doc.createTextNode("Fedorov"));
        name.appendChild(firstName);
        name.appendChild(lastName);
        person.appendChild(name);
        person.setAttribute("born", "1992");

        NodeList people = doc.getElementsByTagName("people");
        people.item(0).appendChild(person);

        // save document
        TransformerFactory transformerFactory
                = TransformerFactory.newInstance();
        Transformer transformer
                = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result
                = new StreamResult(new File("newscientists.xml"));
        transformer.transform(source, result);

        // print list of people
        NodeList nList = doc.getElementsByTagName("person");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("Person no " + temp);

                System.out.println("First Name: "
                        + eElement
                                .getElementsByTagName("firstname")
                                .item(0)
                                .getTextContent().trim());

                System.out.println("Last Name: "
                        + eElement
                                .getElementsByTagName("lastname")
                                .item(0)
                                .getTextContent().trim());

                System.out.println("Date of Birth: "
                        + eElement
                                .getAttribute("born"));

                System.out.println("Date of Death: "
                        + eElement
                                .getAttribute("deceased"));
            }
            System.out.println("");
        }

    }

}
