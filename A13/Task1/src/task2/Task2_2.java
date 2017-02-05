/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import sun.swing.SwingUtilities2;

/**
 *
 * @author rafaelkperes
 */
public class Task2_2 {

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader
                = factory.createXMLEventReader(
                        new FileReader("scientists.xml"));

        boolean shouldPrintContent = false;
        int i = 0;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("person")) {
                        System.out.println("Person no " + i + ":");
                        i++;
                        Iterator<Attribute> attrs = startElement.getAttributes();
                        while (attrs.hasNext()) {
                            Attribute attr = attrs.next();
                            
                            if (attr.getName().toString().equalsIgnoreCase("born")
                                    || attr.getName().toString().equalsIgnoreCase("deceased")) {
                                System.out.println(" " + attr);
                            }
                        }
                    } if (qName.equalsIgnoreCase("firstname") ||
                            qName.equalsIgnoreCase("lastname")) {
                        System.out.print(" " + qName + ": ");
                        shouldPrintContent = true;
                    }

                    break;

                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (!characters.isIgnorableWhiteSpace() && shouldPrintContent) {
                        System.out.println(" " + characters.toString().trim());
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    shouldPrintContent = false;
            }

        }
    }

}
