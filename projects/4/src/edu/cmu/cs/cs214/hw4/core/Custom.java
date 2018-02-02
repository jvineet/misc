package edu.cmu.cs.cs214.hw4.core;

/*Custom tile has an effect in which the player gets no points from
 * his valid move and the owner gets 15 bonus points */
public class Custom extends SpecialTile{

	public Custom(Player p) {
		super(p, 10);		
	}
	
	@Override
	public void effect(Scrabble game, int[] coordinates){
		game.setScoreMultiplier(0);	
		this.getOwner().updateScore(10);
	}
}

