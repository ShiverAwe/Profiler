package org.jetbrains.test.Tracker.Argument;

/**
 * Vladimir Shefer
 * 22.05.2017.
 */
public class ArgumentBean {
    private String name = "UNDEFINED";
    private String typeName = "UNDEFINED";
    private String serializedValue = "UNDEFINED";

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSerializedValue() {
        return serializedValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setSerializedValue(String serializedValue) {
        this.serializedValue = serializedValue;
    }
}
