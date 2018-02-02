package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.cmu.cs.cs214.hw4.core.*;

public class RackListener implements ActionListener {
	final private int x;
	final private Scrabble scrabble;
	final private JButton button;
	private final BoardPanel boardPanel;
	final private Player player;

	public RackListener(int x, JButton button, Scrabble scrabble,
			BoardPanel boardPanel, Player p) {
		this.x = x;
		this.scrabble = scrabble;
		this.button = button;
		this.boardPanel = boardPanel;
		this.player = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println(boardPanel.getImage());
		Tile rack = player.getInventory()[x];
		if (boardPanel.getImage() == "") {
			boardPanel.setImage(((RegularTile) rack).getLetter());
			boardPanel.getMovTile().add(rack);
			System.out.println(boardPanel.getMovTile().size());
			button.setEnabled(false);
		}

	}

}
