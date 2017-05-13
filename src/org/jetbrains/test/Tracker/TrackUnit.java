package org.jetbrains.test.Tracker;
import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.XML.XMLReadable;
import org.jetbrains.test.Tracker.XML.XMLWritable;
import org.jetbrains.test.Tracker.XML.XMLReader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;

/**
 * Created by Vladimir Shefer on 05.05.2017.
 */
public class TrackUnit implements XMLReadable, XMLWritable {
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
        return this.methodName;
    }

    public TrackUnit getInvoker(){
        return this.invoker;
    }

    /**
     * Increases the callCount of invoked method
     * OR Creates new TrackUnit, if method never called before.
     * @param methodName
     * @return TrackUnit of invoked method.
     */
    public TrackUnit getInvoked(String methodName, ArgumentList arguments) {
        TrackUnit newCall = new TrackUnit(this, methodName, arguments);
        invoked.add(newCall);
        return newCall;
    }

    public TrackUnit getInvoked(String methodName){
        return getInvoked(methodName, new ArgumentList(/*empty*/));
    }

    /**
     * Used to print root-node.
     * Запускает рекурсивный вывод дерева в консоль.
     */
    public void printTrack(){
        System.out.println();
        printTrack("");
    }
    
    /**
     *  Recursively prints call-tree.
     *  Every children prints right from parent.
     *  Рекурсивно выводит в консоль дерево вызовов.
     *  Каждый ребенок выводится правее родителя.
     * @param placeholder remains from parent node to denote hierarchy.
     *        отступ добавляется каждым родителем, чтобы графически обозначить иерархию.
     *
     */
    public void printTrack(String placeholder){
        int branchCount = invoked.size();
        // for list we do not draw brackets
        if (branchCount == 0) {
            System.out.print( placeholder + "|>--" +getInfo() + "--");
            arguments.print();
            System.out.println();
        } else // for nodes with children we prepare "brackets"
        {
            System.out.print( placeholder +"|\\--" +getInfo() + "--"); // opening block
            arguments.print();
            System.out.println();
            for (int i = 0; i < branchCount; i++) {
                TrackUnit unit = invoked.get(i);
                unit.printTrack(placeholder + "| ");
            }
            //System.out.println( placeholder + "|/-----"); // closing block // disabled to save vertical space
        }
    }

    /**
     * @return Information about method. Used for printing to console.
     */
    public String getInfo(){
        return methodName;
    }


    @Override
    public void read(XMLStreamReader reader) {
        if (reader.getEventType() != XMLStreamConstants.START_ELEMENT
            || reader.getLocalName() != "method") throw new RuntimeException("Wrong Reader position. Must be '<method>'.");
        this.invoker = null;
        this.methodName = reader.getAttributeValue("", "name");
        this.arguments.read(reader);
        read(this, reader);
    }

    public static TrackUnit read(TrackUnit parent, XMLStreamReader reader){
        try {
            if (!XMLReader.nextElementIs(reader, "method")) return null;
            String name = reader.getLocalName();
            reader.next();
            ArgumentList argumentList = new ArgumentList();
            if (XMLReader.nextElementIs(reader, "arguments")) {
                argumentList.read(reader);
            }

            TrackUnit unit = new TrackUnit(parent, name, argumentList);

            if (XMLReader.nextElementIs(reader, "calls")) {
                reader.next();
                while (XMLReader.nextElementIs(reader, "method")) {
                    TrackUnit child = read(unit, reader);
                    if (child != null) {
                        unit.invoked.add(child);
                    }
                }
            }
            return unit;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(XMLStreamWriter writer) {
        try {
            writer.writeStartElement("method");
                writer.writeAttribute("name", methodName);
                arguments.write(writer);
                if (invoked.size() > 0) {
                    writer.writeStartElement("calls");
                    for (TrackUnit unit : invoked) {
                        unit.write(writer);
                    }
                    writer.writeEndElement();
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
