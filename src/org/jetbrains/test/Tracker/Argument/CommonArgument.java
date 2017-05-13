package org.jetbrains.test.Tracker.Argument;

import org.jetbrains.test.Tracker.XML.XMLReader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by Vladimir Shefer on 10.05.2017.
 *
 * Common argument is used to save values of elementary types.
 * Uses toString() to serialize value.
 */
public class CommonArgument<T> extends Argument {

    public CommonArgument() {}

    public CommonArgument(String name, T value){
        this.name = name;
        this.serializedValue = value.toString();
        this.typeName = value.getClass().getName();
    }

    public CommonArgument(String name, String typeName, String serializedValue) {
        this.name = name;
        this.typeName = typeName;
        this.serializedValue = serializedValue;
    }

    @Override
    public void read(XMLStreamReader reader) {
        try {
            if (!XMLReader.nextElementIs(reader, "argument")) return;
            this.name = reader.getAttributeValue("", "name");
            this.serializedValue = reader.getAttributeValue("", "serializedValue");
            this.typeName = reader.getAttributeValue("", "typeName");
            reader.next();
        } catch ( XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(XMLStreamWriter writer) {
        try {
            writer.writeEmptyElement("argument");
                writer.writeAttribute("name", name);
                writer.writeAttribute("serializedValue", serializedValue);
                writer.writeAttribute("typeName", typeName);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
