package org.jetbrains.test.Tracker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by Владимир on 05.05.2017.
 *
 * Non-concurrent tree builder of method-calls.
 */

public class Tracker {
    private String name;
    private TrackUnit root;
    private TrackUnit current;
    // List of root-nodes of tracker, which was already exited.
    /**
     * When first called method after initialization ends, @root is added to this list
     * to be accessed later.
     */
    private ArrayList<TrackUnit> exRoots = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Tracker(String trackerName){
        this.name = trackerName;
    }

    private void init(String methodName){
        root = new TrackUnit(null, methodName);
        current = root;
    }

    public void registerCall(String methodName){
        if (root == null) init(methodName);
        else
        current = current.getInvoked(methodName);
    }

    public void registerOut(){
        if (current.getInvoker() == null) {
            registerExit();
        } else {
            current = current.getInvoker();
        }
    }

    /**
     * Called when got back into the root node.
     * Saves root into ex-roots list and reinitializes root and current nodes.
     */
    private void registerExit(){
        exRoots.add(root);
        root = null;
        current = null;
    }

    /**
     * Use this method to check if we got out of all tracked methods.
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

            System.out.print("Current: ");
            root.printTrack();
            System.out.print("Track end.");
            System.out.println();
        }
    }


    /**
     * Builds DOM-tree into the element param.
     * @param element - DOM Document Element
     */
    public void putToXMLElement(Element element){
        Document document = element.getOwnerDocument();
        Element threadElement = document.createElement("tracker");
        threadElement.setAttribute("name", name);
        element.appendChild(threadElement);

        //TrackUnit callRoot = this.root;
        //callRoot.putToXMLElement(threadElement);
        for (TrackUnit exRoot : exRoots){
            exRoot.putToXMLElement(threadElement);
        }
    }
}
