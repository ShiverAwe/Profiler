package org.jetbrains.test.Tracker.XML;

import javax.xml.stream.XMLStreamWriter;

/**
 * Vladimir Shefer
 * 12.05.2017.
 */
public interface XMLWritable {
    void writeToXML(XMLStreamWriter writer);
}
