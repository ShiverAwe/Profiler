package org.jetbrains.test;

import org.jetbrains.test.Tracker.Tracker;
import org.jetbrains.test.Tracker.TrackerPool;
import org.jetbrains.test.Tracker.XML.XMLReader;

/**
 * Created by Владимир on 09.05.2017.
 */
public class TaskReadFile {
    public static void main(String[] args) {

        XMLReader reader = new XMLReader();


        TrackerPool trackers = null;

        trackers = reader.read("dummys.xml");

        for (Tracker tracker : trackers){
            tracker.printAllTracks();
        }

    }
}
