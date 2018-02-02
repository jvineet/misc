package edu.cmu.cs.cs214.hw4.core;

/* Square attribute 'type' represents scoring system : 1 represents normal
 * 2 represents double letter score, 3 represents triple letter score
 * 4 represents double word score, 5 represents triple word score*/

public class Square {
	private int type;
	private Tile tileHeld;
	private boolean hasTile;
	
	public Square(int typ){
		tileHeld = null;
		hasTile = false;
		type = typ;
	}
	
	public int getType(){
		return type;
	}
	//returns true if square has a tile
	public boolean hasTile(){
		return hasTile;
	}
	
	//returns true if square has a regular tile
	public boolean hasRegTile(){
		if (hasTile && tileHeld instanceof RegularTile)   				//since regular tile will 
				return true;                                //always be at the top
		return false;
	}
	
	//places tile on the square
	public void placeTile(Tile tile, Scrabble game, int[] coordinates){
		//special tile is activated each time any tile is placed on top if it
		if(hasTile && tileHeld instanceof SpecialTile){
			SpecialTile a = (SpecialTile)tileHeld;
			a.effect(game, coordinates);             		 //performs effect
		}	
		
		tileHeld = tile;									 //replaces tile
		hasTile = true;		
	}
	
	//removes existing tile from the square
	public void removeTile(){
		tileHeld = null;
		hasTile = false;
	}
	
	//returns a regular tile if the square currently hold it, otherwise returns null
	public RegularTile getRegularTile(){
		if(hasRegTile())
			return (RegularTile)tileHeld;
		return null;			
	}
		
}
