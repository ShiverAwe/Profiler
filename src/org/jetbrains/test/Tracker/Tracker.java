package org.jetbrains.test.Tracker;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.XML.XMLReadable;
import org.jetbrains.test.Tracker.XML.XMLWritable;
import org.jetbrains.test.Tracker.XML.XMLReader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;

/**
 * Created by Владимир on 05.05.2017.
 *
 * Non-concurrent tree builder of method-calls.
 */

public class Tracker implements XMLReadable, XMLWritable {
    private String name;
    private TrackUnit root;
    private TrackUnit current;

    /**
     * When first called method after initialization ends, @root is added to this list
     * to be accessed later.
     */
    private ArrayList<TrackUnit> exRoots = new ArrayList<>();

    public Tracker(String trackerName){
        this.name = trackerName;
    }

    public Tracker() { }

    public String getName() {
        return name;
    }

    private void init(String methodName, ArgumentList arguments){
        root = new TrackUnit(null, methodName, arguments);
        current = root;
    }

    public void registerCall(String methodName, ArgumentList arguments){
        if (root == null) init(methodName, arguments);
        else
        current = current.getInvoked(methodName, arguments);
    }

    public void registerCall(String methodName){
        registerCall(methodName, new ArgumentList());
    }

    public void registerOut(){
        if (current.getInvoker() == null) {
            registerExit();
        } else {
            current = current.getInvoker();
        }
    }

    /**
     * Called when root node is registeredOut.
     * Saves root into ex-roots list and reinitializes the "root" and "current" nodes.
     */
    private void registerExit(){
        exRoots.add(root);
        root = null;
        current = null;
    }

    /**
     * Use this method to check if we are out of all the tracked methods.
     * in other words
     * Claims that all your registered calls are out.
     */
    public void assertExit() {
        if (root != null) throw new RuntimeException(
                "assertExit exception. Maybe you forgot registerOut()? Current method: "
                        + current.getMethodName()
        );
    }

    /**
     * @return count of thread usages.
     */
    public int getExitedTracksCount(){
        return exRoots.size();
    }

    /**
     * Prints to console current track, which is still tracking.
     */
    public void printCurrentTrack(){
        synchronized (System.out) {
            System.out.print("tracker " + name + " current tree ");
            root.printTrack();
            System.out.print("Track end.");
            System.out.println();
        }
    }

    /**
     * Prints to console last exited track.
     */
    public void printLastTrack(){
        synchronized (System.out) {
            if (exRoots.size() == 0) {
                System.out.println("This thread has never ended before");
                System.out.println(current.getMethodName());
                return;
            }
            System.out.print("tracker " + name + " last tree: ");
            exRoots.get(exRoots.size()-1).printTrack();
            System.out.print("Track end.");
            System.out.println();
        }
    }

    /**
     * Prints to console all exited track.
     */
    public void printAllTracks(){
        synchronized (System.out) {
            System.out.println("tracker " + name + " all tracks : ");
            for (TrackUnit exHead : exRoots) {
                exHead.printTrack();
            }

            if (root != null) {
                System.out.print("Current: ");
                root.printTrack();
            }
            System.out.print("Track end.");
            System.out.println();
        }
    }

    @Override
    public void read(XMLStreamReader reader){
        try {
            if (!XMLReader.nextElementIs(reader, "tracker")) return;
            this.name = reader.getLocalName();
            reader.next();
            while (XMLReader.nextElementIs(reader, "method")){
                    exRoots.add(TrackUnit.read(null, reader));
            }
            reader.next();
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(XMLStreamWriter writer) {
        try {
            writer.writeStartElement("tracker");
                writer.writeAttribute("name", name);
                for (TrackUnit unit : exRoots) {
                    unit.write(writer);
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
