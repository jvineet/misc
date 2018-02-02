package edu.cmu.cs.cs214.hw4.core;

public class SpecialTile extends Tile{
	private int price;
	private Player owner;
	
	public SpecialTile(Player p, int amt) {
		super(false);
		owner = p;		
		price = amt;
	}

	public int getPrice(){
		return price;
	}
	
	public Player getOwner(){
		return owner;
	}
	
	public void effect(Scrabble game, int[] coordinates){
		//to do when defining each special tile
	}
}


