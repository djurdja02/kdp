package jd;

/**
 * buffer made with concurrent linkedQueue, producers 
 * put items on the end, consumers take from beginning
 * */
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SemaphoreBuff<T> {
	//concurrent structure requires no mutex for access
	private ConcurrentLinkedQueue<T> films=new ConcurrentLinkedQueue<T>();
	private Semaphore producer;
	private Semaphore consumer;
	
	protected SemaphoreBuff(int cap){
		producer=new Semaphore(cap);
		consumer=new Semaphore(0);
	}
	
	protected void put(T film) {
		producer.acquireUninterruptibly();
		System.out.println("put");
		films.add(film);
		consumer.release();
	}
	
	protected T get() {
		consumer.acquireUninterruptibly();
		T ret=films.remove();
		producer.release();
		System.out.println("get");
		return ret;
	}

}
