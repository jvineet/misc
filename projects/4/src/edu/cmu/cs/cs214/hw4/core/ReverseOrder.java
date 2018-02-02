package edu.cmu.cs.cs214.hw4.core;

public class ReverseOrder extends SpecialTile{

	public ReverseOrder(Player p) {
		super(p, 10);		
	}
	
	@Override
	public void effect(Scrabble game, int[] coordinates){
		game.reversePlayers();			
	}
		
}


