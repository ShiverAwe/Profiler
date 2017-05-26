package org.jetbrains.test.demo;

import org.jetbrains.test.Tracker.TrackerPool;
import org.jetbrains.test.Tracker.XML.XMLWriter;
import org.jetbrains.test.Tracker.XML.XMLTrackLoader;

/**
 * Created by Vladimir Shefer on 09.05.2017.
 */
public class DemoReadFile {
    public static void main(String[] args) {
        XMLTrackLoader loader = new XMLTrackLoader("dummys.xml");
        loader.start();
        XMLWriter writer = new XMLWriter();
        TrackerPool trackers = loader.getTrackers();
        writer.write(trackers, "emulout.xml");
    }
}
