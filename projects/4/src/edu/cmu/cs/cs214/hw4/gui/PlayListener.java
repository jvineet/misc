package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.*;

public class PlayListener implements ActionListener {
	final private BoardPanel boardPanel;
	final private Scrabble scrabble;
	final private Player player;

	public PlayListener(BoardPanel boardPanel, Scrabble scrabble, Player p) {
		this.boardPanel = boardPanel;
		this.scrabble = scrabble;
		this.player = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Tile> mov = boardPanel.getMovTile();
		ArrayList<Integer[]> cord = boardPanel.getMovCord();

		RegularTile[] movTiles = new RegularTile[mov.size()];
		for (int i = 0; i < mov.size(); i++) {
			movTiles[i] = (RegularTile) mov.get(i);

		}

		int[][] movcords = new int[cord.size()][2];
		for (int i = 0; i < cord.size(); i++) {
			movcords[i][0] = cord.get(i)[0];
			movcords[i][1] = cord.get(i)[1];
		}

		Move move = new Move(movcords, null, (RegularTile[]) movTiles, null,
				null, false);
		int xa = scrabble.Play(move);

		System.out.println(scrabble.getPlayers()[0].getScore());
		System.out.println("x is " + xa);

		if (xa == 0) {
			for (int i = 0; i < cord.size(); i++) {
				int x = movcords[i][0];
				int y = movcords[i][1];
				updateBoardPanel(boardPanel, scrabble.getBoard(), x, y);				
			}
			
			for (int i = 0; i < 14; i++) {
				if (!boardPanel.rack[i].isEnabled()) {
					boardPanel.rack[i].setEnabled(true);
				}
			}

		} else {
			updateInventory(boardPanel, player.getInventory());
			
			boardPanel.freezeInv();
			
			BoardPanel next = (BoardPanel)boardPanel.server.getNextPanel();
			next.unfreezeInv();
		}

		mov.clear();
		cord.clear();
		movcords = null;
		movTiles = null;

		for (int i = 0; i < scrabble.getNumPlayers(); i++) {
			boardPanel.scores[i].setText(scrabble.getPlayers()[i]
					.getIdentifier()
					+ " : "
					+ scrabble.getPlayers()[i].getScore() + " Points ");
		}
		
		boardPanel.server.publish();
		
		
	}

	private void updateInventory(BoardPanel boardPanel, Tile[] inventory) {
		String txt;
		JButton[] rack = boardPanel.rack;
		for (int x = 0; x < 14; x++) {
			if (inventory[x] == null)
				txt = "";
			else if (inventory[x] instanceof RegularTile)
				txt = ((RegularTile) inventory[x]).getLetter();
			else
				txt = boardPanel.getKind(inventory[x]);

			rack[x].setText(txt);
		}
	}

	private void updateBoardPanel(BoardPanel boardPanel, Board board, int x,
			int y) {

		JButton[][] squares = boardPanel.squares;
		Square[][] old_squares = board.getSquares();

		if (!board.getSquares()[x][y].hasTile()) {
			String txt = boardPanel.getType(old_squares[x][y].getType());
			Color color = boardPanel.getColor(old_squares[x][y].getType());
			if (x == 7 && y == 7) {
				txt = "Start";
				color = Color.BLUE;
			}
			squares[x][y].setText(txt);
			squares[x][y].setBackground(color);
		}
		else{
			squares[x][y].setText(board.getSquares()[x][y].getRegularTile().getLetter());
			squares[x][y].setBackground(Color.LIGHT_GRAY);
		}
			
	}
}
