package edu.cmu.cs.cs214.hw4.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.cmu.cs.cs214.hw4.core.*;

public class SquareListener implements ActionListener {

    private final int x;
    private final int y;
    private String image;
    private final BoardPanel boardPanel;

    /**
     * Creates a new square listener to get click events at a specific game grid
     * coordinate.
     */
    public SquareListener(int x, int y, BoardPanel boardPanel) {
        this.x = x;
        this.y = y;
        this.boardPanel = boardPanel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        //button.setBackground(color);
    	System.out.println(boardPanel.getImage());
    	if(boardPanel.getImage() != ""){
    		boardPanel.squares[x][y].setText(boardPanel.getImage());
    		boardPanel.squares[x][y].setBackground(Color.LIGHT_GRAY);
    		boardPanel.squares[x][y].setForeground(Color.black);
    		Integer[] i = new Integer[2];
    		i[0] = x;
    		i[1] = y;
    		boardPanel.getMovCord().add(i);
    	}
    	System.out.println(boardPanel.getMovCord().size());
        boardPanel.setImage("");        
    }

}
