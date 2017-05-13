package org.jetbrains.test.Tracker.XML;

import org.jetbrains.test.Tracker.TrackerPool;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;

/**
 * Created by Vladimir Shefer on 09.05.2017.
 */
public class XMLReader {
    private TrackerPool trackerPool = new TrackerPool();
    private XMLStreamReader reader = null;


    public TrackerPool read(String filename){
        reader = getNewReader(filename);
        try {
            reader.next();
            if (nextElementIs(reader,"trackers")){
                trackerPool.read(reader);
            }
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return trackerPool;
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

    public static boolean nextElementIs(XMLStreamReader reader, String name){
        while (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
            try {
                reader.next();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(name + " - " + reader.getLocalName());
        return reader.getLocalName().compareTo(name) == 0;
    }

}