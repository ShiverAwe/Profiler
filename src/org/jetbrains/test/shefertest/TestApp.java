package org.jetbrains.test.shefertest;

import org.jetbrains.test.Tracker.Argument.ArgumentList;
import org.jetbrains.test.Tracker.TrackController;

import java.util.Random;

/**
 * Created by Владимир on 06.05.2017.
 */
public class TestApp {
    Random random = new Random();
    int callCounter = 1;
    final int maxCallCounter = 1000;

    TestApp(){
          randomCall();
    }

    public void randomCall(){
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

    public boolean stop(){
        return (callCounter++ > maxCallCounter);
    }

    void a(){
        TrackController.registerCall("a", new ArgumentList()
                .add("a1", stop())
                .add("a2", 536)
        );
        randomCall();
        TrackController.registerOut();
    }

    void ab(){
        TrackController.registerCall("ab");
        randomCall();
        TrackController.registerOut();
    }
    void abc(){
        TrackController.registerCall("abc");
        randomCall();
        TrackController.registerOut();
    }

    void abcd(){
        TrackController.registerCall("abcd",
                new ArgumentList().add("s", "ttt").add("f", 123.55).add("i", 56));
        randomCall();
        TrackController.registerOut();
    }
}
