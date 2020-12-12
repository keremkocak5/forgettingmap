package com.kerem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ForgettingMapThreadTest {

    /**
     * For this test, a ForgettingMap is created. Were the ForgettingMap not synchronized, then a
     * ConcurrentModificationException would be thrown, or a race condition occurs, which
     * causes the assertions to fail.
     */
    @Test
    public void find_shouldReturnValue_whenMultipleThreadsAddIntoForgettingMap() throws InterruptedException {
        int threadName1 = 10000;
        int threadName2 = 20000;
        ForgettingMap<Integer, Integer> forgettingMap = new ForgettingMap<>(18000);
        AddToAndFindFromForgettingMap thread1 = new AddToAndFindFromForgettingMap(forgettingMap);
        AddToAndFindFromForgettingMap thread2 = new AddToAndFindFromForgettingMap(forgettingMap);
        thread1.setName(String.valueOf(threadName1));
        thread2.setName(String.valueOf(threadName2));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Assertions.assertEquals(forgettingMap.getSize(), 18000);
        for (int i = 0; i < 9000; i++) {
            Assertions.assertEquals(forgettingMap.find(threadName1 + i), threadName1 + i);
            Assertions.assertEquals(forgettingMap.find(threadName2 + i), threadName2 + i);
        }
    }

    private class AddToAndFindFromForgettingMap extends Thread {

        private ForgettingMap forgettingMap;

        public AddToAndFindFromForgettingMap(ForgettingMap forgettingMap) {
            this.forgettingMap = forgettingMap;
        }

        public void run() {
            for (int i = 0; i < 9000; i++) {
                forgettingMap.add(Integer.valueOf(Thread.currentThread().getName()) + i, Integer.valueOf(Thread.currentThread().getName()) + i);
            }
        }
    }
}