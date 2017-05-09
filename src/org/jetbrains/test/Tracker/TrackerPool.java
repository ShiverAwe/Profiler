package org.jetbrains.test.Tracker;

import org.jetbrains.test.XML.XMLSaver;

import java.util.*;

/**
 * Created by Vladimir Shefer on 09.05.2017.
 */
public class TrackerPool {
    private final TreeMap<String, Tracker> pool = new TreeMap<>();

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

}
