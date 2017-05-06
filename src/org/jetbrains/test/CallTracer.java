package org.jetbrains.test;

import java.util.TreeMap;

/**
 * Created by Владимир on 05.05.2017.
 *
 * Call tracer provider.
 * Creates ThreadCallTracer for each thread.
 *
 */
public class CallTracer {
    static int tracersCreated = 0;
    static private TreeMap<String, ThreadCallTracer> threadCallTracerPool = new TreeMap<>();

    static public ThreadCallTracer newTracer () {
        ThreadCallTracer newTracer = new ThreadCallTracer(getThreadID());
        synchronized (threadCallTracerPool) {
            threadCallTracerPool.put(getThreadID(), newTracer);
        }
        return newTracer;
    }


    /**
     * When called from the function, adds registration
     */
    static public void registerCall(){
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        ThreadCallTracer tracer = threadCallTracerPool.get(getThreadID());
        if (tracer != null) {
             tracer.registerCall(methodName);
        } else {
            newTracer().registerCall(methodName);
        }

    }

    public static void registerThread(){

    }

    private static String getThreadID(){
        return Thread.currentThread().getId()+"*";
    }

    static public void registerOut(){
        ThreadCallTracer tracer = threadCallTracerPool.get(getThreadID());
        if (tracer != null) {
            tracer.registerOut();
        } else {
            throw new RuntimeException("Nowhere to exit in thread " + getThreadID());
        }
    }

    static public void printTrace(){
        ThreadCallTracer tracer = threadCallTracerPool.get(getThreadID());
        if (tracer != null) {
            tracer.printLastEndedTrace();
        } else {
            System.out.println(getThreadID() + " was not traced!");
        }
    }
}
