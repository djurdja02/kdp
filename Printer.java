package jd;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Printer extends Thread {
	
	private int M;
	private ConcurrentHashMap<String, Integer> processed;
	private Barrier printer;
	private List<Film> end;
	
	public Printer(Barrier printer,int M,List<Film> end,
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
		while((tmp=end.remove(0))!=null) {
			System.out.println(tmp.toString());
		}
		
	}

}
