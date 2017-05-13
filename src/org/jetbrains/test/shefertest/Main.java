package org.jetbrains.test.shefertest;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.TrackController;

/**
 * Created by Владимир on 06.05.2017.
 */
public class Main {
    public static void main(String[] args) {

        TrackController.registerCall("main");

        TestApp test = new TestApp();

        TrackController.registerOut();

        TrackController.assertThreadExit();
        TrackController.printLastTrack();
        TrackController.saveToFile("shefer.xml");
    }

}
