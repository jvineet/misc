package edu.cmu.cs.cs214.hw4.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Board {
	private static int xdim = 15;
	private static int ydim = 15;
	private Square[][] squares;

	public Board() throws IOException {
		squares = new Square[xdim][ydim];

		for (int i = 0; i < xdim; i++) {
			for (int j = 0; j < ydim; j++) {
				squares[i][j] = new Square(1);
			}
		}

		for (int i = 0; i < xdim; i++) {
			squares[i][i] = new Square(4);
			squares[i][14 - i] = new Square(4);
		}

		BufferedReader reader = new BufferedReader(new FileReader(
				"assets/board.txt"));

		String line;
		while ((line = reader.readLine()) != null) {
			Scanner s = new Scanner(line);
			int x = s.nextInt();
			int y = s.nextInt();
			int type = s.nextInt();

			squares[x][y] = new Square(type);
		}

		squares[7][7] = new Square(1);
	}

	// returns if the board is considered empty
	private boolean boardEmpty() {
		return !squares[7][7].hasRegTile();
	}

	// checks whether location at x,y is empty
	public boolean checkIfEmpty(int x, int y) {
		return !squares[x][y].hasTile();
	}

	// checks if a coordinate has a neighbor with a regular tile
	private boolean hasValidNeighbour(int x, int y) {
		boolean flag = false;

		if (x - 1 >= 0 && squares[x - 1][y].hasRegTile())
			flag = true;

		if (x + 1 < 15 && squares[x + 1][y].hasRegTile())
			flag = true;

		if (y - 1 >= 0 && squares[x][y - 1].hasRegTile())
			flag = true;

		if (y + 1 < 15 && squares[x][y + 1].hasRegTile())
			flag = true;

		return flag;
	}

	// checks if move coordinates for regular and special tiles are within
	// the dimensions of the board and square location doesn't have a regular
	// tile and the move has at least one neighbor or if this move places the
	// first word
	public boolean isValidLocation(Move move) {

		// checks move with regular tiles
		int[][] coordinates = move.getRegCoordinates();
		int len = move.getRegMoveLength();
		boolean validNeighbour = false;
		boolean midFlag = false;

		// check if the move is valid horizontally or vertically
		if (!move.isHorizantal() && !move.isVertical())
			return false;

		// if placing first word on the board
		if (boardEmpty()) {
			validNeighbour = true;
			for (int i = 0; i < len; i++) {
				int x = coordinates[i][0];
				int y = coordinates[i][1];

				// check if the coordinates are inside the board
				if (!(0 <= x && x < 15 && 0 <= y && y < 15))
					return false;

				// first move has to contain middle square
				if (x == 7 && y == 7)
					midFlag = true;
			}
		}

		// if placing other regular moves when board is not empty
		else {
			midFlag = true;
			for (int i = 0; i < len; i++) {
				int x = coordinates[i][0];
				int y = coordinates[i][1];

				// check if the coordinates are inside the board
				if (!(0 <= x && x < 15 && 0 <= y && y < 15))
					return false;

				// check if all the locations don't already have regular tiles
				if (squares[x][y].hasRegTile() == true)
					return false;

				// checks if at least one location has a neighboring regular
				// tile
				if (hasValidNeighbour(x, y)) {
					validNeighbour = true;
				}
			}
		}

		if (!(validNeighbour && midFlag))
			return false;

		// checks move with special tiles
		coordinates = move.getSpecCoordinates();

		if (coordinates != null) {
			len = move.getSpecMoveLength();

			for (int i = 0; i < len; i++) {
				int x = coordinates[i][0];
				int y = coordinates[i][1];

				if (!(0 <= x && x < 15 && 0 <= y && y < 15))
					return false;

				if (squares[x][y].hasRegTile() == true)
					return false;
			}
		}
		return true;
	}

	// updates the board once move has been validated
	public void update(Move move, Scrabble game) {
		// update regular tiles
		int[][] coordinates = move.getRegCoordinates();
		Tile[] moveTiles = move.getRegMoveTiles();
		int len = move.getRegMoveLength();

		for (int i = 0; i < len; i++) {
			int x = coordinates[i][0];
			int y = coordinates[i][1];
			squares[x][y].placeTile(moveTiles[i], game, coordinates[i]);
		}

		// update special tiles
		coordinates = move.getSpecCoordinates();
		if (coordinates != null) {
			moveTiles = move.getSpecMoveTiles();
			len = move.getSpecMoveLength();

			for (int i = 0; i < len; i++) {
				int x = coordinates[i][0];
				int y = coordinates[i][1];
				squares[x][y].placeTile(moveTiles[i], game, coordinates[i]);
			}

		}
	}

	// returns a word in horizontal direction, if present
	private String getXWord(int x, int yfront, int yback, String[][] wordGrid) {
		String word = "";
		for (int j = yback; j <= yfront; j++) {
			// get letter from the tile placed on current square
			String s = wordGrid[x][j];
			if (s == null)
				return "$$$"; // random string not in dictionary to make move
								// invalid
			word = word.concat(s);
		}
		return word;
	}

	// returns a word in vertical direction, if present
	private String getYWord(int xfront, int xback, int y, String[][] wordGrid) {
		String word = "";
		for (int i = xback; i <= xfront; i++) {
			// get letter from the tile placed on current square
			String s = wordGrid[i][y];
			if (s == null)
				return "$$$"; // random string not in dictionary to make move
								// invalid
			word = word.concat(s);
		}
		return word;
	}

	// returns score of a word in horizontal direction, if present
	private int getXWordScore(int x, int ystart, int yend) {
		int score = 0;
		for (int j = ystart; j <= yend; j++) {
			RegularTile tl = squares[x][j].getRegularTile();
			// to prevent null access if tile was removed by explosion
			if (tl != null)
				score = score + tl.getScore();
		}
		return score;
	}

	// returns score of a word in vertical direction, if present
	private int getYWordScore(int xstart, int xend, int y) {
		int score = 0;
		for (int i = xstart; i <= xend; i++) {
			RegularTile tl = squares[i][y].getRegularTile();
			// to prevent null access if tile was removed by explosion
			if (tl != null)
				score = score + tl.getScore();
		}
		return score;
	}

	// Calculates the score for a valid board move
	public int moveScore(Move move, Scrabble game) {
		// updates the board state after the move
		update(move, game);

		int[][] coordinates = move.getRegCoordinates();
		int score = 0;
		int mult = 0;
		int add = 0;
		int temp;

		// if move is made horizontal
		if (move.isHorizantal()) {
			int x = coordinates[0][0];
			int yend = 0;
			int ystart = ydim - 1;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				if (yend < coordinates[i][1])
					yend = coordinates[i][1];
				if (ystart > coordinates[i][1])
					ystart = coordinates[i][1];
			}

			while (ystart - 1 >= 0 && squares[x][ystart - 1].hasRegTile())
				ystart--;
			while (yend + 1 < 15 && squares[x][yend + 1].hasRegTile())
				yend++;
			
			if (ystart != yend) {      //checks if the move was only single coordinate
				temp = getXWordScore(x, ystart, yend);

				// checking for tile type and adding further points
				for (int i = 0; i < move.getRegMoveLength(); i++) {

					int y = coordinates[i][1];
					int type = squares[x][y].getType();
					// if 2L, 3L tile
					if (2 <= type && type < 4) {
						add = add + (type - 1)
								* squares[x][y].getRegularTile().getScore();
					}
					// if 2W, 3W tile
					else if (type == 4 || type == 5) {
						mult = mult + (type - 2);
					}
				}

				if (mult > 0)
					temp = temp * mult + add;
				else
					temp = temp + add;
				score = score + temp;
			}

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				int xstart = x;
				int xend = x;
				int y = coordinates[i][1];
				while (xstart - 1 >= 0 && squares[xstart - 1][y].hasRegTile())
					xstart--;
				while (xend + 1 < 15 && squares[xend + 1][y].hasRegTile())
					xend++;

				if (xstart != xend) {
					temp = getYWordScore(xstart, xend, y);
					int type = squares[x][y].getType();
					// if 2L, 3L tile
					if (2 <= type && type < 4)
						temp = temp + (type - 1)
								* squares[x][y].getRegularTile().getScore();
					// if 2W, 3W tile
					else if (type == 4 || type == 5)
						temp = temp * (type - 2);

					score = score + temp;
				}
			}
		}
		// if the move made is vertical
		else {
			int y = coordinates[0][1];
			int xstart = xdim - 1;
			int xend = 0;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				if (xstart > coordinates[i][0])
					xstart = coordinates[i][0];
				if (xend < coordinates[i][0])
					xend = coordinates[i][0];
			}

			while (xstart - 1 >= 0 && squares[xstart - 1][y].hasRegTile())
				xstart--;
			while (xend + 1 < 15 && squares[xend][y].hasRegTile())
				xend++;

			temp = getYWordScore(xstart, xend, y);

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				int x = coordinates[i][0];
				int type = squares[x][y].getType();
				// if 2L, 3L tile
				if (2 <= type && type < 4) {
					add = add + (type - 1)
							* squares[x][y].getRegularTile().getScore();
				}
				// if 2W, 3W tile
				else if (type == 4 || type == 5) {
					mult = mult + (type - 2);
				}
			}

			if (mult > 0)
				temp = temp * mult + add;
			else
				temp = temp + add;
			score = score + temp;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				int ystart = y;
				int yend = y;
				int x = coordinates[i][0];
				while (ystart - 1 >= 0 && squares[x][ystart - 1].hasRegTile())
					ystart--;
				while (yend + 1 < 15 && squares[x][yend + 1].hasRegTile())
					yend++;
				if (ystart != yend) {
					temp = getXWordScore(x, ystart, yend);
					int type = squares[x][y].getType();
					// if 2L, 3L tile
					if (2 <= type && type < 4)
						temp = temp + (type - 1)
								* squares[x][y].getRegularTile().getScore();
					// if 2W, 3W tile
					else if (type == 4 || type == 5)
						temp = temp * (type - 2);

					score = score + temp;
				}
			}
		}
		System.out.println("Score " + score);
		return score;
	}

	// Checks if all the words formed in a move are valid
	public boolean checkAllWords(Move move, Dictionary dictionary) {
		String[][] wordGrid = new String[xdim][ydim];
		int[][] coordinates = move.getRegCoordinates();
		RegularTile[] regTiles = move.getRegMoveTiles();

		if(move.getRegCoordinates().length == 1 && boardEmpty())
			return false;
		
		// create word grid based on existing board state
		for (int i = 0; i < xdim; i++) {
			for (int j = 0; j < xdim; j++) {
				RegularTile tl = squares[i][j].getRegularTile();
				if (tl != null)
					wordGrid[i][j] = tl.getLetter();
			}
		}

		// add letters from the current move to word grid
		for (int i = 0; i < move.getRegMoveLength(); i++) {
			int x = coordinates[i][0];
			int y = coordinates[i][1];
			wordGrid[x][y] = regTiles[i].getLetter();
		}

		// if the move made is horizontal
		if (move.isHorizantal()) {
			int x = coordinates[0][0];
			int ystart = ydim - 1;
			int yend = 0;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				if (ystart > coordinates[i][1])
					ystart = coordinates[i][1];
				if (yend < coordinates[i][1])
					yend = coordinates[i][1];
			}

			while (ystart - 1 >= 0 && squares[x][ystart - 1].hasRegTile())
				ystart--;
			while (yend + 1 < 15 && squares[x][yend + 1].hasRegTile())
				yend++;

			String xword = getXWord(x, yend, ystart, wordGrid);
			if (dictionary.isValid(xword) == false && xword.length() > 1)
				return false;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				int xstart = x;
				int xend = x;
				int y = coordinates[i][1];
				while (xstart - 1 >= 0 && squares[xstart - 1][y].hasRegTile())
					xstart--;
				while (xend + 1 < 15 && squares[xend + 1][y].hasRegTile())
					xend++;

				if (xend != xstart) {
					String yword = getYWord(xend, xstart, y, wordGrid);
					if (dictionary.isValid(yword) == false)
						return false;
				}
			}
		}
		// if the move made is vertical
		else {
			int y = coordinates[0][1];
			int xstart = xdim - 1;
			int xend = 0;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				if (xstart > coordinates[i][0])
					xstart = coordinates[i][0];
				if (xend < coordinates[i][0])
					;
				xend = coordinates[i][0];
			}

			while (xstart - 1 >= 0 && squares[xstart - 1][y].hasRegTile())
				xstart--;
			while (xend + 1 < 15 && squares[xend + 1][y].hasRegTile())
				xend++;

			String yword = getYWord(xend, xstart, y, wordGrid);
			if (dictionary.isValid(yword) == false && yword.length() > 1)
				return false;

			for (int i = 0; i < move.getRegMoveLength(); i++) {
				int ystart = y;
				int yend = y;
				int x = coordinates[i][0];
				while (ystart - 1 >= 0 && squares[x][ystart - 1].hasRegTile())
					ystart--;
				while (yend + 1 < 15 && squares[x][yend + 1].hasRegTile())
					yend++;

				if (yend != ystart) {
					String xword = getXWord(x, yend, ystart, wordGrid);
					if (dictionary.isValid(xword) == false)
						return false;
				}
			}
		}
		return true;
	}

	// returns all the squares that make up the board
	public Square[][] getSquares() {
		return squares;
	}

	// displays the tiles placed on the board
	public void displayBoard() {
		for (int i = 0; i < xdim; i++) {
			for (int j = 0; j < ydim; j++) {
				RegularTile tl = squares[i][j].getRegularTile();
				if (tl != null)
					System.out.print(tl.getLetter());
				else
					System.out.print("_");
				System.out.print(" ");
			}
			System.out.println("");
		}
	}
}
