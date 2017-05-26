package org.jetbrains.test.demo;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.TrackController;

import java.util.Random;

/**
 * Vladimir Shefer
 * 06.05.2017.
 */
public class DemoRandomCall {
    private Random random = new Random();
    private int callCounter = 1;
    private final int maxCallCounter = 1000;

    public void start() {
        randomCall();
    }

    private void randomCall(){
        if (stop()) return;
        for (int i = 0; i < random.nextInt(5); i++) {
            int nextFunction = random.nextInt(callCounter % 10 + 1);
            switch (nextFunction) {
                case 0: a(); break;
                case 1: ab(); break;
                case 2: abc(); break;
                case 3: abcd(); break;
                default: callCounter--; break;
            }
        }
    }

    private boolean stop(){
        return (callCounter++ > maxCallCounter);
    }

    private void a(){
        TrackController.registerCall("a",
                new ArgumentList()
                        .add("a1", stop())
                        .add("a2", 536)
        );
        randomCall();
        TrackController.registerOut();
    }

    private void ab(){
        TrackController.registerCall("ab",
                new ArgumentList()
                        .add("yo", "abgil")
        );
        randomCall();
        TrackController.registerOut();
    }
    private void abc(){
        TrackController.registerCall("abc");
        randomCall();
        TrackController.registerOut();
    }

    private void abcd(){
        TrackController.registerCall("abcd",
                new ArgumentList()
                        .add("s", "ttt")
                        .add("f", 123.55)
                        .add("i", 56)
        );
        randomCall();
        TrackController.registerOut();
    }

}
