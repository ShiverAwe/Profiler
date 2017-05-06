package org.jetbrains.test;
import java.util.ArrayList;

/**
 * Created by Владимир on 05.05.2017.
 *
 * Thread call tree unit.
 * Contains
 *  invoker - method name, previous stack trace unit.
 *  invoked - method names, which was called from there.
 *
 */
public class CallTreeUnit {
    private CallTreeUnit invoker;
    private ArrayList<CallTreeUnit> invoked = new ArrayList<>();
    private String methodName;
    private int callCount;


    public CallTreeUnit(CallTreeUnit invoker, String methodName){
        this.invoker = invoker;
        this.methodName = methodName;
        callCount = 1;
    }

    public void inc(){
        this.callCount++;
    }

    public CallTreeUnit invoke(String methodName) {
        for (CallTreeUnit unit : invoked){
            if (unit.getMethodName().compareTo(methodName) == 0)
            {
                unit.inc();
                return unit;
            }
        }
        CallTreeUnit newCall = new CallTreeUnit(this, methodName);
        invoked.add(newCall);
        return newCall;
    }

    public void printTrace(){
        int invokeCount = invoked.size();
        if (invokeCount == 0) {
            System.out.println(" > " + methodName + "(" + callCount + ")");
        }
        else {
            int number = 0;
            for (CallTreeUnit unit : invoked){
                System.out.print(" > " + methodName + "(" + callCount + ")");
                unit.printTrace();
            }
        }
    }


    public CallTreeUnit getInvoker(){
        return this.invoker;
    }

    public String getMethodName(){
        return this.methodName;
    }
}
