package org.jetbrains.test.Tracker;

import org.jetbrains.test.Tracker.XML.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

/**
 * Vladimir Shefer
 * 09.05.2017.
 */
public class TrackerPool implements XMLWritable, Iterable<Tracker>{
    private final TreeMap<String, Tracker> pool = new TreeMap<>();

    public TrackerPool() {}

    public boolean check(String name){
        return  pool.containsKey(name);
    }

    public Tracker get(String name){
        return get(name, false);
    }

    public Tracker get(String name, boolean createIfNull){
        if (check(name)) {
            return pool.get(name);
        } else if (createIfNull) {
            return create(name);
        } else return null;
    }

    public Tracker create(String name){
        return create(name, false);
    }

    public Tracker create(String name, boolean replaceIfExists) {
        if (check(name) && !replaceIfExists) return get(name);

        Tracker newTracker = new Tracker(name);
        synchronized (pool) {
            pool.put(name, newTracker);
        }
        return newTracker;
    }

    public void put(String name, Tracker tracker){
        pool.put(name, tracker);
    }

    @Override
    public Iterator<Tracker> iterator() {
        return pool.values().iterator();
    }

    @Override
    public void writeToXML(XMLStreamWriter writer) {
        try {
            writer.writeStartElement("trackers");
                for (Tracker tracker : this) {
                    tracker.writeToXML(writer);
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
