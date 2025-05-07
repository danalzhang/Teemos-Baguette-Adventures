package main;

public class Stats implements Comparable <Stats>{
	private String date;
	private int score;
	
	public Stats(String date, int currentScore) {
		this.date = date;
		this.score = currentScore;
	}

	// getters
	public String getDate() {
		return this.date;
	}
	
	public int getScore() {
		return this.score;
	}

	@Override
	public int compareTo(Stats o) {
		// TODO Auto-generated method stub
		return o.score-this.score;
	}
	
	
}
