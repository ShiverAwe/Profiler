package org.jetbrains.test.XML;

import org.jetbrains.test.Tracker.Tracker;
import org.jetbrains.test.Tracker.TrackerPool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Владимир on 09.05.2017.
 */
public class XMLSaver {
    public void save(TrackerPool trackerPool, String filename){
        Document document = buildDOM(trackerPool);
        XMLUtils.saveDocument(document, filename);
    }

    public Document buildDOM(TrackerPool pool){
        Document document = XMLUtils.getNewDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        // For every threadTracker...
        for(String name : pool.getNames()) {
            Tracker tracker = pool.get(name);
            tracker.putToXMLElement(root);
        }
        return document;
    }
}
