package jd;

import java.util.concurrent.ConcurrentHashMap;

public class Test {
	public final static int cCnt=5;

	public static void main(String[] args) {
		String line ="src/jd/data.txt";
		SemaphoreBuff<Film> buf= new SemaphoreBuff<>(10);
		ConcurrentHashMap<String, Integer> processed=new ConcurrentHashMap<>();
		Barrier combiner=new Barrier(cCnt);
		Barrier combiner2=new Barrier(1);
		SemaphoreBuff<Result> results=new SemaphoreBuff<>(1000);
		ConcurrentHashMap<String, Boolean> checkMax=new ConcurrentHashMap<String, Boolean>();
		SemaphoreBuff<Film> send= new SemaphoreBuff<Film>(1000);
		Producer p=new Producer(line, buf);
		p.start();
		for(int i=0;i<cCnt;i++) {
		Consumer cs=new Consumer(10, "consumer"+i, buf, results, combiner, combiner2, processed, checkMax, send);
		cs.start();
		}
		Barrier printer=new Barrier(1);
		Buffer<Film> end=new Buffer<>();
		Combiner c=new Combiner(results, combiner, combiner2, printer, checkMax, send, end);
		c.start();
		Printer pr=new Printer(printer, 1, end, processed);
		pr.start();
	}

}
