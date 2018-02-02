package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScrabbleTest {
	int x;
	private Scrabble scrabble;
	private Dictionary dictionary;
	private Board board;
	private Player player1, player2;
	private Player[] players;
	private TileBox tileBox;

	@Before
	public void setUp() throws Exception {
		board = new Board();
		tileBox = new TileBox();

		player1 = new Player(tileBox, "Player 1");
		player2 = new Player(tileBox, "Player 2");

		players = new Player[2];
		players[0] = player1;
		players[1] = player2;

		BufferedReader reader = new BufferedReader(new FileReader(
				"assets/words.txt"));

		String line;
		int wordCount = 0;
		while ((line = reader.readLine()) != null)
			wordCount++;

		reader = new BufferedReader(new FileReader("assets/words.txt"));

		String[] list = new String[wordCount];
		for (int i = 0; i < wordCount; i++)
			list[i] = reader.readLine();

		dictionary = new Dictionary(list);

		scrabble = new Scrabble(players, tileBox, board, dictionary);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDictionary() {
		assertTrue(dictionary.isValid("aardvark"));
		assertTrue(dictionary.isValid("aardvarks"));
		assertTrue(dictionary.isValid("zygotes"));	
		assertTrue(dictionary.isValid("zygote"));
		assertTrue(dictionary.isValid("zucchini"));
	}
	
	@Test
	public void testIsValid() {
		// player 1 starts by placing first move 
		RegularTile[] t = { new RegularTile("a", 1), new RegularTile("g", 4),
				new RegularTile("o", 2) };
		int[][] regCoordinates = { { 7, 6 }, { 7, 7 }, { 7, 8 } };
		SpecialTile[] st = { new Boom(player1) };

		int[][] specCoordinates = { { 8, 8 } };
        Move move = new Move(regCoordinates, specCoordinates, t, st, null,
				false);
		assertTrue(scrabble.isValid(move));

		//move is non-linear
		int[][] regCoordinates2 = { { 7, 6 }, { 7, 7 }, { 8, 10 } };
		move = new Move(regCoordinates2, specCoordinates, t, st, null, false);
		assertFalse(scrabble.isValid(move));

		RegularTile[] t2 = { new RegularTile("a", 1), new RegularTile("r", 4),
				new RegularTile("o", 2) };
		move = new Move(regCoordinates, specCoordinates, t2, st, null, false);
		assertFalse(scrabble.isValid(move));

		// move coordinates are outside the bard
		int[][] regCoordinates3 = { { 17, 6 }, { 7, 7 }, { 8, 10 } };
		move = new Move(regCoordinates3, null, t, null, null, false);
		assertFalse(scrabble.isValid(move));

		// if game does not start from centre of the board
		int[][] regCoordinates4 = { { 3, 6 }, { 3, 7 }, { 3, 8 } };
		move = new Move(regCoordinates3, null, t, null, null, false);
		assertFalse(scrabble.isValid(move));

		//no word is placed, illegal move
		move = new Move(null, specCoordinates, null, st, null, false);
		assertFalse(scrabble.isValid(move));

		//simple first move starting from centre
		move = new Move(regCoordinates, null, t, null, null, false);
		assertTrue(scrabble.isValid(move));

		 x = scrabble.Play(move);

		//checking for moves with board not empty
		RegularTile[] t21 = { new RegularTile("s", 3), new RegularTile("g", 4) };
		int[][] regCoordinates21 = { { 6, 6 }, { 8, 6 } };
		move = new Move(regCoordinates21, null, t21, null, null, false);
		assertTrue(scrabble.isValid(move));

		// does not have at least one neighbor
		RegularTile[] t22 = { new RegularTile("o", 3), new RegularTile("n", 4) };
		int[][] regCoordinates22 = { { 1, 1 }, { 1, 2 } };
		move = new Move(regCoordinates22, null, t22, null, null, false);
		assertFalse(scrabble.isValid(move));

	}

	@Test
	public void testMakeMove() {
		RegularTile[] t = { new RegularTile("a", 1), new RegularTile("g", 4),
				new RegularTile("o", 2) };
		int[][] regCoordinates = { { 7, 6 }, { 7, 7 }, { 7, 8 } };
		SpecialTile[] st = { new NegativePoints(player1) };
		int[][] specCoordinates = { { 8, 8 } };
		Move move = new Move(regCoordinates, specCoordinates, t, st, null,
				false);

		//Player one makes first move
		x = scrabble.Play(move);
		assertTrue(player1.getScore() == 7);
		//assertTrue(tileBox.getNumTiles() == 86);

		RegularTile[] t2 = { new RegularTile("s", 3), new RegularTile("g", 4) };
		int[][] regCoordinates2 = { { 6, 6 }, { 8, 6 } };

		//Player 2 makes a valid move
		move = new Move(regCoordinates2, null, t2, null, null, false);
		x = scrabble.Play(move);
		assertTrue(player2.getScore() == 15);

		//Player 1 make a second move that forms 3 words but activates boom tile
		RegularTile[] t3 = { new RegularTile("o", 2), new RegularTile("n", 3),
				new RegularTile("e", 3) };
		int[][] regCoordinates3 = { { 8, 7 }, { 8, 8 }, { 8, 9 } };
		move = new Move(regCoordinates3, null, t3, null, null, false);
		assertTrue(scrabble.isValid(move));
		x = scrabble.Play(move);
		assertTrue(player1.getScore() == -22);

		// Player 2 purchase special tile
		assertTrue(player2.getScore() == 15);
		move = new Move(null, null, null, null, new Boom(
				scrabble.getCurrentPlayer()), false);
		x = scrabble.Play(move);
		assertTrue(player2.getScore() == 5);

		// wrong move (player1) control remains with player 1
		move = new Move(null, null, null, null, null, false);
		x = scrabble.Play(move);
		
		// right move (player 1)
		RegularTile[] t4 = { new RegularTile("b", 3), new RegularTile("r", 1),
				new RegularTile("a", 2), new RegularTile("t", 4) };
		int[][] regCoordinates4 = { { 7, 10 }, { 8, 10 }, { 9, 10 }, { 10, 10 } };
		move = new Move(regCoordinates4, null, t4, null, null, false);
		x = scrabble.Play(move);
		assertTrue(player1.getScore() == 11);
		assertTrue(player2.getScore() == 5);

		// player 2 passes
		move = new Move(null, null, null, null, null, true);
		assertTrue(move.isPass());
		x = scrabble.Play(move);
		assertTrue(player2.getScore() == 5);
		assertTrue(scrabble.getCurrentPlayer() == player1);

		board.displayBoard();

		// test final results
		scrabble.printResults();

	}

	@Test
	public void testCustom() {
		RegularTile[] t = { new RegularTile("a", 1), new RegularTile("g", 4),
				new RegularTile("o", 2) };
		int[][] regCoordinates = { { 7, 6 }, { 7, 7 }, { 7, 8 } };
		SpecialTile[] st = { new Custom(player2) };
		int[][] specCoordinates = { { 8, 8 } };
		
		//Player 1
		Move move = new Move(regCoordinates, null, t, null, null,
				false);

		x = scrabble.Play(move);

		//Player 2
		RegularTile[] t2 = { new RegularTile("s", 3), new RegularTile("g", 4) };
		int[][] regCoordinates2 = { { 6, 6 }, { 8, 6 } };

		move = new Move(regCoordinates2, specCoordinates, t2, st, null, false);
		x = scrabble.Play(move);

		//Player 1
		RegularTile[] t3 = { new RegularTile("o", 2), new RegularTile("n", 3),
				new RegularTile("e", 3) };
		int[][] regCoordinates3 = { { 8, 7 }, { 8, 8 }, { 8, 9 } };
		move = new Move(regCoordinates3, null, t3, null, null, false);
		
		assertTrue(player1.getScore() == 7);
		assertTrue(player2.getScore() == 15);
		x = scrabble.Play(move);                  		//make move
		assertTrue(player1.getScore() == 7);        //doesn't change
		assertTrue(player2.getScore() == 25);       //original (15) + 10
		
	}
		
	@Test
	public void testBoom() {
		RegularTile[] t = { new RegularTile("a", 1), new RegularTile("g", 4),
				new RegularTile("o", 2) };
		int[][] regCoordinates = { { 7, 6 }, { 7, 7 }, { 7, 8 } };
		SpecialTile[] st = { new Boom(player1) };
		int[][] specCoordinates = { { 8, 8 } };
		Move move = new Move(regCoordinates, specCoordinates, t, st, null,
				false);

		x = scrabble.Play(move);

		RegularTile[] t2 = { new RegularTile("s", 3), new RegularTile("g", 4) };
		int[][] regCoordinates2 = { { 6, 6 }, { 8, 6 } };

		move = new Move(regCoordinates2, null, t2, null, null, false);
		x = scrabble.Play(move);

		RegularTile[] t3 = { new RegularTile("o", 2), new RegularTile("n", 3),
				new RegularTile("e", 3) };
		int[][] regCoordinates3 = { { 8, 7 }, { 8, 8 }, { 8, 9 } };
		move = new Move(regCoordinates3, null, t3, null, null, false);
		x = scrabble.Play(move);
		assertTrue(player1.getScore() == 7);     //score didn't change

	}

	@Test
	public void testReverseOrder() {
		RegularTile[] t = { new RegularTile("a", 1), new RegularTile("g", 4),
				new RegularTile("o", 2) };
		int[][] regCoordinates = { { 7, 6 }, { 7, 7 }, { 7, 8 } };
		SpecialTile[] st = { new ReverseOrder(player1) };
		int[][] specCoordinates = { { 8, 8 } };
		Move move = new Move(regCoordinates, specCoordinates, t, st, null,
				false);

		x = scrabble.Play(move);

		RegularTile[] t2 = { new RegularTile("s", 3), new RegularTile("g", 4) };
		int[][] regCoordinates2 = { { 6, 6 }, { 8, 6 } };

		move = new Move(regCoordinates2, null, t2, null, null, false);
		x = scrabble.Play(move);

		RegularTile[] t3 = { new RegularTile("o", 2), new RegularTile("n", 3),
				new RegularTile("e", 3) };
		int[][] regCoordinates3 = { { 8, 7 }, { 8, 8 }, { 8, 9 } };
		move = new Move(regCoordinates3, null, t3, null, null, false);
		x = scrabble.Play(move);
		assertTrue(player1.getScore() == 36);
		assertTrue(players[0] == player2); // order reversed
	}

	@Test
	public void testGetSpecialTile() {
		scrabble.getCurrentPlayer().updateScore(20);
		assertTrue(player1.getSpecialTileCount() == 0);

		Move move = new Move(null, null, null, null, new Boom(
				scrabble.getCurrentPlayer()), false);

		x = scrabble.Play(move);
		assertTrue(player1.getSpecialTileCount() == 1);

	}
}
