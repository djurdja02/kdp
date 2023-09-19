package jd;

import java.io.BufferedReader;
import java.io.FileReader;

public class Producer extends Thread{

	private SemaphoreBuff<Film> buf;
	private String films;
	
	protected Producer(String line , SemaphoreBuff<Film> buf) {
		this.films=line;
		this.buf=buf;
	}
	//process lines from the file until the EOF (null)
	public void run() {
		try {
			BufferedReader br=new BufferedReader(new FileReader(films));
			String film=br.readLine();
			while( !(film=br.readLine()).equals(null)) {
				buf.put(new Film(film));
			}
			br.close();
			buf.put(null);
			
		} catch (Exception e) {
			System.out.println("File can't be open.");
		} 
	}
	
}
