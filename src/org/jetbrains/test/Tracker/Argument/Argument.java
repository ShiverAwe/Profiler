package org.jetbrains.test.Tracker.Argument;

import org.jetbrains.test.Tracker.XML.XMLReadable;
import org.jetbrains.test.Tracker.XML.XMLWritable;

/**
 * Created by Vladimir Shefer on 12.05.2017.
 *
 * Base class for arguments.
 */
public abstract class Argument implements XMLReadable, XMLWritable {
    String name = "UNDEFINED";
    String typeName = "UNDEFINED";
    String serializedValue = "UNDEFINED";

    public String getName() {
        return name;
    }

    public String getSerializedValue() {
        return serializedValue;
    }

    public String getTypeName() {
        return typeName;
    }

    public void print(){
        System.out.print(
                "[(" + getTypeName() + ")" + getName() + " == " + getSerializedValue() + " ] "
        );
    }

    public Argument(String name, String typeName, String serializedValue){
        this.name = name;
        this.typeName = typeName;
        this.serializedValue = serializedValue;
    }

    public Argument(){
    }

}
