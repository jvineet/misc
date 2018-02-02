package edu.cmu.cs.cs214.hw4.core;

public class RegularTile extends Tile{
	private String letter;
	private int score;
	
	public RegularTile(String l, int point) {
		super(true);
		score = point;
		letter = l;
	}
		
	public int getScore(){
		return score;
	}
		
	public String getLetter(){
		return letter;
	}

}
