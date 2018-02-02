package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Scrabble;
import edu.cmu.cs.cs214.hw4.core.Tile;

public class ResetListener implements ActionListener{
	final private BoardPanel boardPanel;
	final private Scrabble scrabble;
	final private Player player;

	public ResetListener(BoardPanel boardPanel, Scrabble scrabble, Player p) {
		this.boardPanel = boardPanel;
		this.scrabble = scrabble;
		this.player = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Tile> mov = boardPanel.getMovTile();
		ArrayList<Integer[]> cord = boardPanel.getMovCord();
		
		boardPanel.updateDisplay();
		boardPanel.unfreezeInv();	
		boardPanel.setImage("");
		
		mov.clear();
		cord.clear();
	}

}
