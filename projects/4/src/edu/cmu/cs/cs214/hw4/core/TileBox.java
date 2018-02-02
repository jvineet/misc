package edu.cmu.cs.cs214.hw4.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class TileBox {
	private RegularTile[] tileSet;
	private int numTiles;
	private Random rand;

	// setting up the tilebox
	public TileBox() throws IOException {
		tileSet = new RegularTile[98];
		numTiles = 98;
		rand = new Random();
		
		BufferedReader reader = new BufferedReader(	new FileReader(
						"assets/letters.txt"));

		String line;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			Scanner s = new Scanner(line);
			String letter = s.next();
			int score = s.nextInt();
			int count = s.nextInt();
			
			for(int i=0; i<count; i++){
				tileSet[index] = new RegularTile(letter, score);
				index++;
			}			
		}
			
	}

	// returns the number of tiles currently in tilebox
	public int getNumTiles() {
		return numTiles;
	}

	// inserting a tile in the tilebox if it is not full
	public void addTile(RegularTile tile) {
		if (numTiles < 100) {
			tileSet[numTiles] = tile;
			numTiles++;
		}
	}

	// draws a random tile from the tilebox
	public RegularTile drawRandom() {
		int ind = rand.nextInt(numTiles);
		RegularTile next = tileSet[ind];

		// for removing tile 'next' from the tile set
		tileSet[ind] = tileSet[numTiles - 1];
		numTiles--;

		return next;
	}

}
