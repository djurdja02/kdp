package jd;
/*
 * additional class for encapsulating results of processing movies
 * */
public class Result {
	
	private String name;
	private int cnt;
	
	protected Result(String name, int cnt) {
		this.cnt=cnt;
		this.name=name;
	}

	protected String getName() {
		return name;
	}

	protected int getCnt() {
		return cnt;
	}
	
	

}
