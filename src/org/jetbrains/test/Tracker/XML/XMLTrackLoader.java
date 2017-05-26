package org.jetbrains.test.Tracker.XML;

import org.jetbrains.test.Tracker.Argument.Argument;
import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.Tracker;
import org.jetbrains.test.Tracker.TrackerPool;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;

/**
 * Created by Vladimir Shefer on 22.05.2017.
 */
public class XMLTrackLoader {
    XMLStreamReader reader;
    private TrackerPool trackers = new TrackerPool();
    private Tracker currentTracker;

    public XMLTrackLoader(String filename) {
        reader = getNewReader(filename);
    }

    public void start() {
        try {
            reader.next();
            while (!nextElementIs("trackers")) next();
            next();
            while (nextElementIs("tracker")) {
                handleTracker();
            }
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public TrackerPool getTrackers() {
        return trackers;
    }

    private void handleTracker() throws XMLStreamException {
        assertStartElement("tracker");
        String trackerName = getAttributeValue("name");
        currentTracker = new Tracker(trackerName);
        trackers.put(trackerName, currentTracker);
        System.out.println("In tracker " + trackerName);
        next();

        handleMethodList();

        currentTracker.assertExit();
        currentTracker.printAllTracks();

        assertEndElement("tracker");
        next();
    }

    private void handleMethodList() throws XMLStreamException {
        while (nextElementIs("method")) {
            handleMethod();
        }
    }

    private void handleMethod() throws XMLStreamException {
        assertStartElement("method");
        String name = getAttributeValue("name");
        next();

        ArgumentList arguments;
        if (nextElementIs("arguments")) {
            arguments = handleArguments();
        } else {
            arguments = new ArgumentList();
        }

        currentTracker.registerCall(name, arguments);

        if (nextElementIs("calls")) {
            handleCalls();
        }

        assertEndElement("method");
        next();
        currentTracker.registerOut();
    }

    private ArgumentList handleArguments() throws XMLStreamException {
        assertStartElement("arguments");
        next();
        ArgumentList arguments = new ArgumentList();

        while (nextElementIs("argument")) {
            arguments.add(handleArgument());
        }

        assertEndElement("arguments");
        next();

        return arguments;
    }

    private Argument handleArgument() throws XMLStreamException {
        assertStartElement("argument");
        String name = getAttributeValue("name");
        String typeName = getAttributeValue("typeName");
        String serializedValue = getAttributeValue("serializedValue");
        Argument argument = new Argument(name, typeName, serializedValue);
        assertEmptyElement("argument");
        next();
        return argument;
    }

    private void handleCalls() throws XMLStreamException {
        assertStartElement("calls");
        next();
        handleMethodList();
        assertEndElement("calls");
        next();
    }

    private String getElementName() throws XMLStreamException {
        skipText();
        return reader.getLocalName();
    }

    private String getAttributeValue(String name) {
        return reader.getAttributeValue("", name);
    }

    private boolean next() throws XMLStreamException {
        skipText();
        if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) return false;
        reader.next();
        skipText();
        return true;
    }

    private boolean nextElementIs(String name) throws XMLStreamException {
        while (!checkStartElement() && !checkEndElement()) {
            if (!next()) return false;
        }
        //System.out.println(name + " - " + reader.getLocalName());
        return reader.getLocalName().compareTo(name) == 0;
    }

    private boolean checkStartElement() {
        return reader.getEventType() == XMLStreamConstants.START_ELEMENT;
    }

    private boolean checkEndElement() {
        return reader.getEventType() == XMLStreamConstants.END_ELEMENT;
    }

    private boolean checkEndDocument(){
        return reader.getEventType() == XMLStreamConstants.END_DOCUMENT;
    }

    private void assertStartElement(String name) throws XMLStreamException {
        if (!checkStartElement()) {
            throw new RuntimeException("Wrong action ");
        } else if (getElementName().compareTo(name) != 0) {
            throw new RuntimeException("Wrong name " + reader.getLocalName() + ", " + name + " expected.");
        }
    }

    private void assertEndElement(String name) throws XMLStreamException {
        if (!checkEndElement()) {
            throw new RuntimeException("Wrong action " + reader.getEventType());
        } else if (getElementName().compareTo(name) != 0) {
            throw new RuntimeException("Wrong name " + reader.getLocalName() + ", " + name + " expected.");
        }
    }

    private void assertEmptyElement(String name) throws XMLStreamException {
        assertStartElement(name);
        reader.next();
        assertEndElement(name);
    }

    private void skipText() throws XMLStreamException {
        while (!checkStartElement() && !checkEndElement() && !checkEndDocument()) {
            reader.next();
        }
    }

    private XMLStreamReader getNewReader(String filename) {
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileReader(filename));
            return reader;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create XML reader", e);
        }
    }

}
