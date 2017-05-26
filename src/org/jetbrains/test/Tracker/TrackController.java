package org.jetbrains.test.Tracker;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.XML.XMLWriter;

/**
 * Created by Vladimir Shefer on 05.05.2017.
 *
 * TrackController is a static class, which
 * redirects registration of calls to concrete Tracker
 *
 * Use TrackController.registerCall(String methoName) at first line of method you want to track and
 * never forget about TrackController.registerOut() before method end.
 *
 * Use registerCall(String methodName, ArgumentList arguments) if you want to save arguments with call info.
 *
 * You can TrackController.printLastTrack() to console.
 *
 * XMLWriter class lets you save track into xml file.
 * Use TrackController.saveToFile(String filename) to save all tracked trees into file
 */
public class TrackController {

    private static TrackerPool trackers = new TrackerPool();

    public static void registerCall(String methodName){
        registerCall(methodName, new ArgumentList());
    }

    public static void registerCall(String methodName, ArgumentList arguments){
        trackers.get(trackerName(), true).
                registerCall(methodName, arguments);
    }

    public static void registerOut(){
        trackers.get(trackerName()).registerOut();
    }

    /**
     * Asserts that all your registered calls was registered out.
      */
    public static void assertTrackExit(){
        trackers.get(trackerName()).assertExit();
    }

    public static void printCurrentTrack(){
        Tracker tracker = trackers.get(getThreadID(), true);
        tracker.printCurrentTrack();
    }

    public static void printLastTrack(){
        if (! trackers.check(trackerName())) {
            System.out.println("Thread was not traced yet");
            return;
        }
        Tracker tracker = trackers.get(trackerName());
        tracker.printLastTrack();
    }

    private static String trackerName(){
        return getThreadID();
    }

    private static String getThreadID(){
        return "thread" + Thread.currentThread().getId();
    }

    public static void saveToFile(String filename){
        XMLWriter writer = new XMLWriter();
        writer.write(trackers, filename);
    }
}
