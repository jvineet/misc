package edu.cmu.cs.cs214.hw4.core;

public class NegativePoints extends SpecialTile{

	public NegativePoints(Player p) {
		super(p, 10);		
	}
	
	@Override
	public void effect(Scrabble game, int[] coordinates){
		game.setScoreMultiplier(-1);		
	}
}
