package org.csp.examples.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Semaphore_Example implements Runnable {

	private static final Semaphore semaphore  = new Semaphore(2, true);
	private static final AtomicInteger counter = new AtomicInteger();
	private static final long endMillis = System.currentTimeMillis() + 500;
	
	public static void main(String[] args){
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for(int i =0;i<3;i++){
			executor.execute(new Semaphore_Example());
		}
		executor.shutdown();
	}

	public void run() {
		// TODO Auto-generated method stub
		//while(System.currentTimeMillis() < endMillis){
			try{
				System.out.println(Thread.currentThread().getName()+ " Acquiring lock");
				semaphore.acquire();
				
			}catch(InterruptedException ie){
				System.out.println(Thread.currentThread().getName()+ " interrupted while acquiring lock");
			}
			int counterValue = counter.incrementAndGet();
			System.out.println(Thread.currentThread().getName()+ " semaphore acquired: "+counterValue);
			if(counterValue > 3){
				throw new IllegalStateException("More than three threads acquired the lock");
			}
			System.out.println("Releasing semaphore semaphore: "+counter);
			counter.decrementAndGet();
			semaphore.release();
			
	//	}
	}
	
	
}
