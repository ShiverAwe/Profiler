package org.jetbrains.test;

import java.util.ArrayList;

/**
 * Created by Владимир on 05.05.2017.
 *
 * Call tree builder for single thread.
 * Creates from CallTracer
 */
public class ThreadCallTracer {
    String name;
    CallTreeUnit head;
    CallTreeUnit current;

    ArrayList<CallTreeUnit> exHeads = new ArrayList<>();

    public ThreadCallTracer(String tracerName){
        this.name = tracerName;
        head = new CallTreeUnit(null, "Tracer init " + exHeads.size());
        current = head;
    }

    public void registerCall(String methodName){
        current = current.invoke(methodName);
    }

    public void registerOut(){
        if (current.getInvoker() == null) throw new RuntimeException("Out of initer?");
        if (current.getInvoker() == head) {
            registerEnd();
            return;
        }
        current = current.getInvoker();
    }

    private void registerEnd(){
        exHeads.add(head);
        head = new CallTreeUnit(null, "Tracer init " + getInitCount());
        current = head;
    }

    public int getInitCount(){
        return exHeads.size();
    }

    public void printCurrentTrace(){
        synchronized (System.out) {
            System.out.println("Thread " + name + " current trace : ");
            head.printTrace();
            System.out.println("Trace end.");
            System.out.println();
        }
    }


    public void printLastEndedTrace(){
        synchronized (System.out) {
            if (exHeads.size() == 0) {
                System.out.println("This thread has never ended before");
                return;
            }

            System.out.println("Thread " + name + " last trace : ");
            exHeads.get(exHeads.size()-1).printTrace();
            System.out.println("Trace end.");
            System.out.println();
        }
    }

    public void printAllTraces(){
        synchronized (System.out) {
            System.out.println("Thread " + name + " all traces : ");
            exHeads.get(exHeads.size()-1).printTrace();
            for (CallTreeUnit exHead : exHeads) {
                exHead.printTrace();
            }

            System.out.print("Current: ");
            head.printTrace();
            System.out.println("Traces end.");
            System.out.println();
        }
    }
}
