package org.csp.examples.concurrency;

import java.util.concurrent.CountDownLatch;

public class CountDownLatch_Example{ 
	
	public static void main(String[] args){
		CountDownLatch latch = new CountDownLatch(2);
		Thread t1 = new Thread(new ThreadTest("ThreadA",1000,latch));
		Thread t2 = new Thread(new ThreadTest("ThreadB",1000,latch));
		t1.start();
		t2.start();
		try{
			latch.await();
			System.out.println("Threads are up, Application is starting now");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class ThreadTest implements Runnable{

	private final String name;
    private final int timeToStart;
    private final CountDownLatch latch;

	public void run() {
		// TODO Auto-generated method stub
		
		try{
			Thread.sleep(timeToStart);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(name + " processed.");
		latch.countDown();
	}
	
	ThreadTest(String name, int timeToStart, CountDownLatch latch){
		this.name = name;
		this.timeToStart = timeToStart;
		this.latch = latch;
	}

}
