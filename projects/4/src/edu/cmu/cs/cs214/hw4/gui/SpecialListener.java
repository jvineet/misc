package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.cmu.cs.cs214.hw4.core.*;

public class SpecialListener implements ActionListener {
	final private int x;
	final private Scrabble scrabble;
	final private JButton button;
	private final BoardPanel boardPanel;
	final private Player player;

	public SpecialListener(int x, JButton button, Scrabble scrabble,
			BoardPanel boardPanel, Player p) {
		this.x = x;
		this.scrabble = scrabble;
		this.button = button;
		this.boardPanel = boardPanel;
		this.player = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Do something

	}

}