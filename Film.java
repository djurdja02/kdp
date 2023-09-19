package jd;

import java.util.Arrays;

public class Film {
	//one film per row from the file
	private String[] directors;
	private String tconst;
	private String[] writers;
	
	
	protected Film(String[] d,String[] w,String t) {
		this.directors=d;
		this.tconst=t;
		this.writers=w;
	}
	
	protected Film(String str) {
		String[] d=
				str.split("\t")[1].equals("\\N") ? null : str.split("\t")[1].split(",");
		String[] w=
				str.split("\t")[2].equals("\\N") ? null : str.split("\t")[2].split(",");
		String t=str.split("\t")[0];
		this.directors=d;
		this.tconst=t;
		this.writers=w;
	}

	protected String[] getDirectors() {
		return directors;
	}

	protected String getTconst() {
		return tconst;
	}

	protected String[] getWriters() {
		return writers;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder(this.tconst+"\t"+(this.directors.equals(null)
				? new String("\\") : Arrays.toString(directors))+
				"\t"+(this.writers.equals(null)
						? new String("\\") : Arrays.toString(writers))
				);
		return sb.toString();
	}

	
	

}
