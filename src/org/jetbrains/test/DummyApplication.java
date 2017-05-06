package org.jetbrains.test;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
    ThreadCallTracer threadCallTracer;
    private final List<String> args;
    private Random random = new Random(System.nanoTime());

    public DummyApplication(List<String> args) {
        this.args = args;
    }

    private boolean nextBoolean() {
        return random.nextBoolean();
    }

    private boolean stop() {
        return random.nextDouble() < 0.05;
    }

    private String nextArg() {
        int idx = random.nextInt(args.size());
        return args.get(idx);
    }

    private void sleep() {
        try {
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException ignored) {

        }
    }

    private void abc(String s) {
        //your code here
        CallTracer.registerCall();
        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            def(nextArg());
        }
        else {
            xyz(nextArg());
        }
        CallTracer.registerOut();
    }

    private void def(String s) {
        //your code here
        CallTracer.registerCall();
        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            xyz(nextArg());
        }
        CallTracer.registerOut();
    }

    private void xyz(String s) {
        //your code here
        CallTracer.registerCall();
        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            def(nextArg());
        }
        CallTracer.registerOut();
    }

    public void start() {
        CallTracer.registerCall();
        abc(nextArg());
        abc(nextArg());
        CallTracer.registerOut();
        CallTracer.printTrace();
    }
}
