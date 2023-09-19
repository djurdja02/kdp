package jd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Combiner extends Thread {
	private SemaphoreBuff<Result> results;
	private Barrier waitFor;
	private Barrier combiner2;
	private Barrier printer;
	private ConcurrentHashMap<String, Boolean> checkMax;
	private SemaphoreBuff<Film> send;
	private Buffer<Film> end;
	
	public Combiner(SemaphoreBuff<Result> results,
			Barrier waitFor,Barrier combiner2,Barrier printer,
			ConcurrentHashMap<String, Boolean> checkMax,
			SemaphoreBuff<Film> send, Buffer<Film> end) {
		this.results=results;
		this.waitFor=waitFor;
		this.combiner2=combiner2;
		this.checkMax=checkMax;
		this.send=send;
		this.end=end;
		this.printer=printer;
	}
	public void run() {
		//wait for every consumer to process data
		waitFor.await(0);
		results.put(null);
		int max=0;
		List<String> names=new ArrayList<>();
		Result tmp;
		//finding max data 
		while((tmp=results.get())!=null) {
			if(tmp.getCnt()>max) {
				max=tmp.getCnt();
				names.clear();
				names.add(tmp.getName());
			}
			else if(tmp.getCnt()==max) {
				names.add(tmp.getName());
			}
		}
		for(String name:names) {
			checkMax.put(name, true);
		}
		//alerting other consumers that data is ready
		combiner2.arrived();
		//getting films from consumers with most dirCnt
		for(int i=0;i<names.size();) {
			Film tmp2;
			while((tmp2=send.get())!=null) {
				end.put(tmp2);
			}
			i++;
		}
		end.put(null);
		//letting printer know that combiner is done with work
		printer.arrived();
	}

}
