package org.jetbrains.test.Tracker;
import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.XML.XMLWritable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;

/**
 * Created by Vladimir Shefer on 05.05.2017.
 */
public class TrackUnit implements  XMLWritable {
    private String methodName;
    private ArgumentList arguments = new ArgumentList();
    private TrackUnit invoker;
    private ArrayList<TrackUnit> invoked = new ArrayList<>();

    public TrackUnit() { }

    public TrackUnit(TrackUnit invoker, String methodName, ArgumentList arguments){
        this.invoker = invoker;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public ArgumentList getArguments () {
        return arguments;
    }

    public String getMethodName(){
        return methodName;
    }

    public TrackUnit getInvoker(){
        return this.invoker;
    }

    public TrackUnit getInvoked(String methodName, ArgumentList arguments) {
        TrackUnit newCall = new TrackUnit(this, methodName, arguments);
        invoked.add(newCall);
        return newCall;
    }

    public TrackUnit getInvoked(String methodName){
        return getInvoked(methodName, new ArgumentList(/*empty*/));
    }

    public void printTrack(){
        System.out.println();
        printTrack("");
    }

    public void printTrack(String placeholder){
        int branchCount = invoked.size();
        // for list we do not draw brackets
        if (branchCount == 0) {
            System.out.print( placeholder + "|>--" + getMethodName() + "--");
            arguments.print();
            System.out.println();
        } else // for nodes with children we prepare "brackets"
        {
            System.out.print( placeholder +"|\\--" + getMethodName() + "--"); // opening block
            arguments.print();
            System.out.println();
            for (int i = 0; i < branchCount; i++) {
                TrackUnit unit = invoked.get(i);
                unit.printTrack(placeholder + "| ");
            }
        }
    }


    @Override
    public void writeToXML(XMLStreamWriter writer) {
        try {
            writer.writeStartElement("method");
                writer.writeAttribute("name", methodName);
                arguments.writeToXML(writer);
                if (invoked.size() > 0) {
                    writer.writeStartElement("calls");
                    for (TrackUnit unit : invoked) {
                        unit.writeToXML(writer);
                    }
                    writer.writeEndElement();
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
