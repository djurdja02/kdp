package jd;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Buffer<T> {
	private ConcurrentLinkedQueue<T> films=new ConcurrentLinkedQueue<T>();
	
	protected Buffer(){}
	
	protected void put(T film) {
		films.add(film);
	}
	
	protected T get() {
		return films.remove();
	}

}
