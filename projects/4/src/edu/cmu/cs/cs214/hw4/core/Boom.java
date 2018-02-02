package edu.cmu.cs.cs214.hw4.core;

public class Boom  extends SpecialTile{

	public Boom(Player p) {
		super(p, 10);		
	}
			
	@Override
	public void effect(Scrabble game, int[] coordinates){
		int x = coordinates[0];
		int y = coordinates[1];
		Board board = game.getBoard();
		Square[][] squares = board.getSquares(); 
		
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++)
				squares[x+i][y+j].removeTile();
		}
		
		game.setScoreMultiplier(0); 			//player doesn't earn points in this round
	}		
}