package edu.cmu.cs.cs214.hw4.core;

public class Move {
	private int[][] regCoordinates;
	private int[][] specCoordinates;
	private RegularTile[] moveRegTiles;
	private SpecialTile[] moveSpecTiles;
	private SpecialTile specialTile;
	private boolean pass;                     //flag to check if player passed his turn
	
	public Move(int[][] rcrd, int[][] scrd, RegularTile[] rtls, 
				SpecialTile[] stls, SpecialTile spt, boolean ps){
		regCoordinates = rcrd;
		specCoordinates = scrd;
		moveRegTiles = rtls;
		moveSpecTiles = stls;
		specialTile = spt;
		pass = ps;
	}
	
	public boolean isPass(){
		return pass;
	}
	
	public int[][] getRegCoordinates(){
		return regCoordinates;
	}
	
	public int[][] getSpecCoordinates(){
		return specCoordinates;
	}
	
	public RegularTile[] getRegMoveTiles(){
		return moveRegTiles;
	}
	
	public SpecialTile[] getSpecMoveTiles(){
		return moveSpecTiles;
	}
	
	public int getRegMoveLength(){
		return moveRegTiles.length;
	}
	
	public int getSpecMoveLength(){
		return moveSpecTiles.length;
	}
	
	public SpecialTile getSpecialTile(){
		return specialTile;
	}
	
	//checks if the regular tiles are present in a move
	public boolean checkRegTiles(){
		if(moveRegTiles != null && moveRegTiles.length>0)
			return true;
		return false;
	}
	
	//checks if the move made is horizontally valid
	public boolean isHorizantal(){
		if (getRegMoveLength() == 1)
			return true;
		for (int i=0; i<getRegMoveLength()-1; i++){
			if(regCoordinates[i][0] != regCoordinates[i+1][0])
				return false;
		}
		return true;
	}
	
	//checks if the move made is vertically valid
	public boolean isVertical(){
		if (getRegMoveLength() == 1)
			return true;
		for (int i=0; i<getRegMoveLength()-1; i++){
			if(regCoordinates[i][1] != regCoordinates[i+1][1])
				return false;
		}
		return true;
	}
	
}
