package jd;

import java.util.concurrent.ConcurrentHashMap;

public class Printer extends Thread {
	
	private int M;
	private ConcurrentHashMap<String, Integer> processed;
	private Barrier printer;
	private Buffer<Film> end;
	
	public Printer(Barrier printer,int M,Buffer<Film> end,
			ConcurrentHashMap<String, Integer> processed) {
		this.M=M;
		this.processed=processed;
		this.printer=printer;
		this.end=end;
	}
	
	public void run() {
		while(true) {
			if(printer.await(M))break;
			else {
				System.out.println(processed.toString());
			}
		}
		Film tmp;
		while((tmp=end.get())!=null) {
			System.out.println(tmp.toString());
		}
		
	}

}
