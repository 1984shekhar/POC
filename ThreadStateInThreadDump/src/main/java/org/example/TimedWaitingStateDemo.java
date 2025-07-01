package org.example;

/**
 * Hello world!
 *
 */
public class TimedWaitingStateDemo {

    public static void main(String[] args) {
        System.out.println("main thread started");
        try {
            Thread.sleep(10000); // Give the thread some time to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("main thread completed");
    }
}
