package org.example;

public class BlockedStateDemo {

    private static final Object lock = new Object();

    static class BlockingTask implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    Thread.sleep(10000); // Hold the lock for a while
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class BlockedTask implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                // This code will not be executed until BlockingTask releases the lock
            }
        }
    }

    public static void main(String[] args) {
        Thread blockedThread1 = new Thread(new BlockingTask());
        Thread blockedThread2 = new Thread(new BlockedTask());

        blockedThread1.start();

        try {
            Thread.sleep(100); // Ensure blockedThread1 acquires the lock first
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        blockedThread2.start();

        try {
            Thread.sleep(100); // Give blockedThread2 a chance to try to acquire the lock
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("State of blockedThread2: " + blockedThread2.getState());
    }
}
