package org.jetbrains.test.Tracker.Argument;

import org.jetbrains.test.Tracker.XML.XMLWritable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * Created by Vladimir Shefer on 12.05.2017.
 */
public class Argument<T> implements XMLWritable {
    ArgumentBean data = new ArgumentBean();

    public Argument(){}

    public Argument(String name, T value){
        this(name, value, (T v) -> v.toString() );
    }

    public Argument(String name, T value, Serializer<? super T> serializer){
        this.data.setName(name);
        this.data.setTypeName(value.getClass().getName());
        this.data.setSerializedValue(serializer.getSerializedValue(value));
    }

    public Argument(String name, String typeName, String serializedValue){
        this.data.setName(name);
        this.data.setTypeName(typeName);
        this.data.setSerializedValue(serializedValue);
    }

    public String getName() {
        return data.getName();
    }

    public String getSerializedValue() {
        return data.getSerializedValue();
    }

    public String getTypeName() {
        return data.getSerializedValue();
    }

    public void print(){
        System.out.print(
                "[(" + getTypeName() + ")" + getName() + " == " + getSerializedValue() + " ] "
        );
    }

    @Override
    public void writeToXML(XMLStreamWriter writer) {
        try {
            writer.writeEmptyElement("argument");
            writer.writeAttribute("name", getName());
            writer.writeAttribute("serializedValue", getSerializedValue());
            writer.writeAttribute("typeName", getTypeName());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
