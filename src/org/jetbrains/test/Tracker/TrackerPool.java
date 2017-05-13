package org.jetbrains.test.Tracker;

import org.jetbrains.test.Tracker.XML.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

/**
 * Created by Vladimir Shefer on 09.05.2017.
 */
public class TrackerPool implements XMLReadable, XMLWritable, Iterable<Tracker>{
    private final TreeMap<String, Tracker> pool = new TreeMap<>();

    public TrackerPool() { }

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

    public Set<String> getNames(){
        return pool.keySet();
    }

    @Override
    public Iterator<Tracker> iterator() {
        return pool.values().iterator();
    }

    @Override
    public void read(XMLStreamReader reader) {
        try {
            if (!XMLReader.nextElementIs(reader, "trackers")) return;
            reader.next();
            while (XMLReader.nextElementIs(reader, "tracker")){
                System.out.println("read tracker");
                Tracker tracker = new Tracker();
                tracker.read(reader);
                pool.put(tracker.getName(), tracker);
                reader.next();
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(XMLStreamWriter writer) {
        try {
            writer.writeStartElement("trackers");
                for (Tracker tracker : this) {
                    tracker.write(writer);
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
