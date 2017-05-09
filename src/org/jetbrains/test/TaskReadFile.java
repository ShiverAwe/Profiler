package org.jetbrains.test;

import org.jetbrains.test.Tracker.TrackerPool;
import org.jetbrains.test.XML.XMLLoader;

/**
 * Created by Владимир on 09.05.2017.
 */
public class TaskReadFile {
    public static void main(String[] args) {

        XMLLoader loader = new XMLLoader();

        TrackerPool trackers = loader.load("dummys.xml");

        for (String trackerName : trackers.getNames()){
            trackers.get(trackerName).printAllTracks();
        }

    }
}
