package org.example;

/**
 * Hello world!
 *
 */
public class RunnableStateDemo {

    static class RunnableTask implements Runnable {
        @Override
        public void run() {
            long counter = 0;
            while (true) {
                counter++;
                if (counter < 0) { // This condition will never be true, but it's there to prevent optimizations.
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread runnableThread = new Thread(new RunnableTask(),"DemoThread");
        runnableThread.start();

        try {
            Thread.sleep(100); // Give the thread some time to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("State of runnableThread: " + runnableThread.getState());
        System.out.println("main thread completed");
    }
}
