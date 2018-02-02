package edu.cmu.cs.cs214.hw4.core;

public class Tile {
	protected boolean visibility;
	
	public Tile(boolean vis){
		visibility = vis;
		
	}
	public boolean isVisible(){
		return visibility;
	}
	
}
