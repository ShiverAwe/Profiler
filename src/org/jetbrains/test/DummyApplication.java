package org.jetbrains.test;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.TrackController;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
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

        TrackController.registerCall("abc",
                new ArgumentList()
                        .add("s", s)
        );

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
        TrackController.registerOut();
    }

    private void def(String s) {
        //your code here
        TrackController.registerCall("def",
                new ArgumentList().add("s", s) );
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
        TrackController.registerOut();
    }

    private void xyz(String s) {
        //your code here
        TrackController.registerCall("xyz",
                new ArgumentList().add("s", s)
        );
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
        TrackController.registerOut();
    }

    public void start() {
        TrackController.registerCall("start");

        abc(nextArg());

        TrackController.registerOut();
        TrackController.assertThreadExit();

        TrackController.printLastTrack(); // to print call-tree into console
        // Так как нельзя найти момент, когда все потоки будут
        // закончены приходится перезаписывать файл каждый раз. Последний поток запишет самую полную версию.
        // А вообще, надо бы это писть в конце программы, когда все деревья достроены.
        TrackController.saveToFile("dummys.xml");
    }
}
