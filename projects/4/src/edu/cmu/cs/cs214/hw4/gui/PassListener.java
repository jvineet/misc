package edu.cmu.cs.cs214.hw4.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.RegularTile;
import edu.cmu.cs.cs214.hw4.core.Scrabble;
import edu.cmu.cs.cs214.hw4.core.Square;
import edu.cmu.cs.cs214.hw4.core.Tile;

public class PassListener implements ActionListener {
	final private BoardPanel boardPanel;
	final private Scrabble scrabble;
	final private Player player;

	public PassListener(BoardPanel boardPanel, Scrabble scrabble, Player p) {
		this.boardPanel = boardPanel;
		this.scrabble = scrabble;
		this.player = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Tile> mov = boardPanel.getMovTile();
		ArrayList<Integer[]> cord = boardPanel.getMovCord();
		
		Move move = new Move(null, null, null, null, null, true);
		int xa = scrabble.Play(move);
		
		mov.clear();
		cord.clear();
		
		boardPanel.freezeInv();
		
		BoardPanel next = (BoardPanel)boardPanel.server.getNextPanel();
		next.unfreezeInv();
		
		boardPanel.server.publish();
	}
}