package org.jetbrains.test.Tracker.Argument;

import org.jetbrains.test.Tracker.XML.XMLWritable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir Shefer on 10.05.2017.
 *
 * Container for Arguments. Used to save arguments of called methods in call trackers.
 */
public class ArgumentList implements XMLWritable {
    public List<Argument> arguments = new ArrayList<>();

    public List<Argument> getArgumentList(){
        return arguments;
    }

    public ArgumentList() { }

    public ArgumentList(Argument... arguments){
        for (Argument argument : arguments) {
            this.arguments.add(argument);
        }
    }

    public ArgumentList add(Argument argument){
        arguments.add(argument);
        return this;
    }

    public ArgumentList add(String argumentName, Object argument){
        arguments.add(new Argument<>(argumentName, argument));
        return this;
    }

    public void print(){
        if (arguments.size() == 0) {
            System.out.print("[]");
            return;
        }
        System.out.print("[List ");
        for (Argument argument : arguments) {
            argument.print();
        }
        System.out.print("] ");
    }

    @Override
    public void writeToXML(XMLStreamWriter writer) {
        try {
            if (arguments.size() == 0) return;
            writer.writeStartElement("arguments");
            for (Argument argument: arguments) {
                argument.writeToXML(writer);
            }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
