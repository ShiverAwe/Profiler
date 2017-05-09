package org.jetbrains.test.shefertest;

import org.jetbrains.test.Tracker.TrackController;
import org.jetbrains.test.XML.XMLLoader;

/**
 * Created by Владимир on 06.05.2017.
 */
public class Main {
    public static void main(String[] args) {
        TrackController.registerCall();

        TestApp test = new TestApp();

        TrackController.registerOut();

        TrackController.assertThreadExit();
        TrackController.printLastTrack();

        //XMLLoader parser = new XMLLoader();
        //parser.load("out.xml");
    }

}
