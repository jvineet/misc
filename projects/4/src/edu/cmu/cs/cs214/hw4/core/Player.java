package edu.cmu.cs.cs214.hw4.core;

public class Player {
	private Tile[] inventory; // first seven indices reserved for regular tiles
	private int score;
	private int regularTilesCount;
	private int specialTilesCount;
	private String identifier; 

	public Player(TileBox tileBox, String id) {
		score = 0;
		inventory = new Tile[14]; // seven regular and at most seven special
		//for filling tiles initially
		regularTilesCount = 0;
		refillTiles(tileBox);
		//initializing number to tiles
		specialTilesCount = 0;
		regularTilesCount = 7;
		identifier = id;
	}

	// refills regular tiles back to seven in the inventory
	public void refillTiles(TileBox tileBox) {
		for (int i = regularTilesCount; i < 7; i++){
			if (tileBox.getNumTiles()>0){
				inventory[regularTilesCount] = tileBox.drawRandom();
				regularTilesCount++;
			}
		}
	}

	// adds special tile to the inventory
	public void addSpecialTile(SpecialTile tile) {
		if (specialTilesCount < 7) {
			inventory[specialTilesCount + 7] = tile;
			updateScore(-tile.getPrice()); // removes the price from player's score
			specialTilesCount++;
		}
	}

	//removes regular tile from the inventory
	public void removeRegTile(RegularTile t){
		for(int j=0; j<regularTilesCount; j++){
			if(t == (RegularTile)inventory[j]){
				//for removing tile 'next' from the inventory
				inventory[j] = inventory[regularTilesCount-1];
				regularTilesCount--;
			}
		}
	}
	
	//removes special tile from the inventory
		public SpecialTile removeSpecTile(int ind){
			SpecialTile next = (SpecialTile)inventory[ind];
			
			//for removing tile 'next' from the inventory
			inventory[ind] = inventory[specialTilesCount-1+7];
			specialTilesCount--;
			
			return next;		
		}
	
	// updates the player's score
	public void updateScore(int add) {
		score = score + add;
	}

	// returns the player's current score
	public int getScore() {
		return score;
	}
	
	//gives count of special tiles a player possesses
	public int getSpecialTileCount(){
		return specialTilesCount;
	}
	
	//returns the player's identifying string
	public String getIdentifier() {
		return identifier;
	}
	
	//returns the player's identifying string
		public Tile[] getInventory() {
			return inventory;
		}
}
