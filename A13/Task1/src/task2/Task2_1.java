/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author rafaelkperes
 */
public class Task2_1 {

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty("javax.xml.stream.isNamespaceAware", false);
        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new FileInputStream("scientists_dtd.xml"));

        int event = reader.getEventType();
        while (reader.hasNext()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    System.out.println("Start of element: " + reader.getLocalName());
                    
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        System.out.println("Attribute: " + reader.getAttributeLocalName(i) 
                                + "=" + reader.getAttributeValue(i) 
                                + " (namespace: " + reader.getAttributeNamespace(i) + ")");
                    }
                    
                    break;

                case XMLStreamConstants.CHARACTERS:
                    System.out.println("Characters: " + reader.getText().trim());
                    break;

                case XMLStreamConstants.COMMENT:
                    System.out.println("Comment: " + reader.getText().trim());
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    System.out.println("End of element: " + reader.getLocalName());
                    break;
            }

            event = reader.next();
        }
    }

}
