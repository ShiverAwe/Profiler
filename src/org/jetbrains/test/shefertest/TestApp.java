package org.jetbrains.test.shefertest;

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
        TrackController.registerCall();
        randomCall();
        TrackController.registerOut();
    }

    void ab(){
        TrackController.registerCall();
        randomCall();
        TrackController.registerOut();
    }
    void abc(){
        TrackController.registerCall();
        randomCall();
        TrackController.registerOut();
    }

    void abcd(){
        TrackController.registerCall();
        randomCall();
        TrackController.registerOut();
    }
}
