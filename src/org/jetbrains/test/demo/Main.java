package org.jetbrains.test.demo;

import org.jetbrains.test.Tracker.TrackController;

/**
 * Vladimir Shefer
 * 06.05.2017.
 */
public class Main {
    public static void main(String[] args) {
        TrackController.registerCall("main");

        DemoRandomCall caller = new DemoRandomCall();
        caller.start();

        TrackController.registerOut();
        TrackController.assertTrackExit();
        TrackController.printLastTrack();
        TrackController.saveToFile("shefer.xml");
    }

}
