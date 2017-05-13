package org.jetbrains.test.Tracker.XML;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import org.jetbrains.test.Tracker.TrackerPool;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Владимир on 12.05.2017.
 */
public class XMLWriter {
    private XMLStreamWriter writer = null;


    public void write(TrackerPool trackerPool, String filename){
        writer = getNewWriter(filename);
        try {
            writer.writeStartDocument();
                trackerPool.write(writer);
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }


    public static XMLStreamWriter getNewWriter(String filename){
        try {
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(
                    new FileWriter(filename)));
            return writer;
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create XML writer");
        }
    }
}
