package org.jetbrains.test.XML;

import org.jetbrains.test.Tracker.Tracker;
import org.jetbrains.test.Tracker.TrackerPool;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by Владимир on 09.05.2017.
 */
public class XMLLoader {
    private String trackerName = null;
    private TrackerPool trackerPool = new TrackerPool();
    private XMLStreamReader reader = null;

    public TrackerPool load(String filename){
        reader = XMLUtils.getNewReader(filename);
        try {
            int event = reader.getEventType();
            while (true) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT :
                        handleElementStart();
                        break;
                    case XMLStreamConstants.END_ELEMENT :
                        handleElementEnd();
                        break;
                    default: break;
                }
                if (!reader.hasNext()) break;
                event = reader.next();
            }
            reader.close();
            return trackerPool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleElementStart(){
        switch (reader.getLocalName()){
            case "tracker" :
                handleTracerOpen();
                break;
            case "call" :
                handleCallOpen();
                break;
        }
    }

    private void handleElementEnd() {
        switch (reader.getLocalName()){
            case "tracker" :
                handleTracerEnd();
                break;
            case "call" :
                handleCallEnd();
                break;
        }
    }

    private void handleTracerOpen(){
        trackerName = reader.getAttributeValue("", "name");
        trackerPool.create(trackerName);
    }

    private void handleTracerEnd() {
        trackerName = null;
    }

    private void handleCallOpen() {
        String methodName = reader.getAttributeValue("", "method");
        trackerPool.get(trackerName).registerCall(methodName);
        int count = Integer.parseInt(reader.getAttributeValue("", "count"));
        // Emulating N entries in method.
        for (int i = 0; i < count-1; i++) {
            trackerPool.get(trackerName).registerOut();
            trackerPool.get(trackerName).registerCall(methodName);
        }
    }

    private void handleCallEnd() {
        getTracker().registerOut();
    }

    private Tracker getTracker(){
        if (trackerName == null) return null;
        return trackerPool.get(trackerName);
    }
}
