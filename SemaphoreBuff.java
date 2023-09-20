package jd;

import java.util.concurrent.Semaphore;

public class SemaphoreBuff<T> {
	private Semaphore producer;
	private Semaphore consumer;
	private Semaphore mutexP,mutexC;
	private int read=0,write=0;
	private int cap;
	private T[] films;
	
	@SuppressWarnings("unchecked")
	protected SemaphoreBuff(int cap){
		films=(T[])new Object[cap];
		producer=new Semaphore(cap);
		consumer=new Semaphore(0);
		mutexP=new Semaphore(1);
		mutexC=new Semaphore(1);
		this.cap=cap;
	}
	
	protected void put(T film, String name) {
		producer.acquireUninterruptibly();
		mutexP.acquireUninterruptibly();
		System.out.println("put :"+name);
		films[write]=film;
		write=(write+1)%cap;
		mutexP.release();
		consumer.release();
	}
	
	protected T get(String name) {
		consumer.acquireUninterruptibly();
		mutexC.acquireUninterruptibly();
		T ret=films[read];
		System.out.println("get :"+name);
		producer.release();
		read=(++read)%cap;
		mutexC.release();
		return ret;
	}

}
