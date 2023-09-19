package jd;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Barrier {
	
	private Semaphore bar1, bar2,finish,mutex;
	private int numOfConsumers;
	private int curr,waiting;
	private boolean pass=false;
	
	protected Barrier(int cnt) {
		this.numOfConsumers=cnt;
		this.curr=0;
		this.waiting=0;
		this.bar1=new Semaphore(1);
		this.bar2=new Semaphore(0);
		this.finish=new Semaphore(0);
		this.mutex=new Semaphore(1);
	}
	protected void arrived() {
		bar1.acquireUninterruptibly();
		//if everyone has arrived i can pass
		if(++curr==numOfConsumers) {
			bar2.release();
			mutex.acquireUninterruptibly();
			finish.release(waiting);
			pass=true;
			mutex.release();
		} else bar1.release();
		bar2.acquireUninterruptibly();
		if(--curr==0) bar1.release();
		else bar2.release();
	}

	protected boolean await(int time) {
		mutex.acquireUninterruptibly();
		//if everyone has arrived dont wait
		if(pass) {
			mutex.release();
			return true;
		}
		//wait for everyone to arrive
		waiting++;
		mutex.release();
		boolean ret=true;
		if(time!=0) {
			try {
				ret=finish.tryAcquire(time, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				return false;
			}
			
		}
		else {
			finish.acquireUninterruptibly();
		}
		mutex.acquireUninterruptibly();
		waiting--;
		mutex.release();
		
		return ret;
	}
}
