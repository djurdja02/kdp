package jd;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer extends Thread{
	
	private int K;
	private String name;
	private SemaphoreBuff<Film> films;
	private ConcurrentHashMap<String, Integer> processed;
	private Barrier combiner;
	private Barrier combiner2;
	private SemaphoreBuff<Result> results;
	private ConcurrentHashMap<String, Boolean> checkMax;
	private SemaphoreBuff<Film> send;
	
	protected Consumer(int K,String name, SemaphoreBuff<Film> films,
			SemaphoreBuff<Result> results, Barrier combiner, Barrier combiner2, 
			ConcurrentHashMap<String, Integer> processed,
			ConcurrentHashMap<String, Boolean> checkMax,SemaphoreBuff<Film> send) {
		this.K=K;
		this.films=films;
		this.processed=processed;
		this.name=name;
		this.combiner=combiner;
		this.results=results;
		this.combiner2=combiner2;
		this.checkMax=checkMax;
		this.send=send;
		
	}

	public void run() {
		Film film;
		int num=0;
		int dirCnt=0;
		List<Film> max=new ArrayList<>();
		while((film=films.get())!=null) {
			
			if(film.getDirectors().length>dirCnt) {
				dirCnt=film.getDirectors().length;
				max.clear();
				max.add(film);
			}
			else if(film.getDirectors().length==dirCnt) {
				max.add(film);
			}
			if(++num%K==0) {
				processed.put(name, num);
			}
		}
		processed.put(name, num);
		films.put(null);
		results.put(new Result(name,dirCnt));
		//inform the combiner that we arrived
		combiner.arrived();
		//wait to check if we have global max dirCnt
		combiner2.await(0);
		if(checkMax.containsKey(name)) {
			if(checkMax.get(name)) {
				ListIterator<Film> iterator = max.listIterator();
				while(iterator.hasNext())send.put(iterator.next());
				send.put(null);
			}
		}
		
		
	}
}
