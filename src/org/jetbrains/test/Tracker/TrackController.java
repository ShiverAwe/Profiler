package org.jetbrains.test.Tracker;

import org.jetbrains.test.XML.XMLSaver;

/**
 * Created by Vladimir Shefer on 05.05.2017.
 *
 * TrackController is a static class, which
 * redirects registration of calls to concrete Tracker
 *
 * Use TrackController.registerCall() at first line of method you want to track and
 * never forget about TrackController.registerOut() before method end.
 *
 * You can TrackController.printLastTrack() to console.
 *
 * XMLBuilder class lets you save track into xml file.
 * Use TrackController.XMLBuilder.save(String filename) to save all tracked trees into file
 */
public class TrackController {

    private static TrackerPool trackers = new TrackerPool();

    public static void registerCall(){
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        trackers.get(getThreadID(), true).
                registerCall(methodName);
    }

    public static void registerOut(){
        trackers.get(trackerName()).registerOut();
    }

    public static void assertThreadExit(){
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

    /**
     * @return name of tracker for current thread
     */
    private static String trackerName(){
        return getThreadID();
    }

    /**
     * @return current thread identifier
     */
    private static String getThreadID(){
        return "thread" + Thread.currentThread().getId();
    }

    /**
     * Saves all tracks, excluding current track, to file
     */
    public static void saveToFile(String filename){
        XMLSaver saver = new XMLSaver();
        saver.save(trackers, filename);
    }

}
