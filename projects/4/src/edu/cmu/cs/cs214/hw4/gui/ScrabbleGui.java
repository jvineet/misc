package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw4.core.*;


public class ScrabbleGui extends JPanel {
	private static final long serialVersionUID = 6647535125556634481L;

	private final JFrame parentFrame;
	private final List<String> names;

	public ScrabbleGui(JFrame frame) {
		this.parentFrame = frame;
		this.names = new ArrayList<String>();

		// Create the components to add to the panel.
		JLabel participantLabel = new JLabel("Name: ");

		// Must be final to be accessible to the anonymous class.
		final JTextField participantText = new JTextField(20);

		JButton participantButton = new JButton("Add participant");
		JPanel participantPanel = new JPanel();
		participantPanel.setLayout(new BorderLayout());
		participantPanel.add(participantLabel, BorderLayout.WEST);
		participantPanel.add(participantText, BorderLayout.CENTER);
		participantPanel.add(participantButton, BorderLayout.EAST);

		 // Defines an anonymous class to handle.
		ActionListener newParticipantListener = new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				String name = participantText.getText();
				if (!name.isEmpty() && !names.contains(name)) {
					names.add(name);
				}
				participantText.setText("");
				participantText.requestFocus();
			}
		};

        // notify the action listener when participantButton or
        // participantText-related events happen.
		participantButton.addActionListener(newParticipantListener);
		participantText.addActionListener(newParticipantListener);

		JButton createButton = new JButton("Start Scrabble");
		createButton.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				if (!names.isEmpty()) {
                    // Starts a new chat when the createButton is clicked.
					try {
						startGame();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

        // Adds the components we've created to the panel (and to the window).
		setLayout(new BorderLayout());
		add(participantPanel, BorderLayout.NORTH);
		add(createButton, BorderLayout.SOUTH);
		setVisible(true);
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Start Game");
				frame.add(new ScrabbleGui(frame));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});

	}

	/**
	 * Starts a new chat, opening one window for each participant.
	 * @throws IOException 
	 */
	private void startGame() throws IOException {
		
		final GameServer server = new GameServer();
		final Dictionary dictionary;
		final Board board;
		
		final TileBox tileBox;
		final int plr;
		
		final Scrabble scrabble;
		
		board = new Board();
		tileBox = new TileBox();

		if(names.size()>4)
			plr = 4;
		else
			plr = names.size();
		
		final Player[] players = new Player[plr];
		
		for(int i = 0; i<plr; i++)
			players[i] = new Player(tileBox, names.get(i));
	
		
				
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
				
        // Removes the initial dialog panel and starts the first participant's
        // chat in the existing window.
		parentFrame.remove(this);
		parentFrame.add(new BoardPanel(scrabble, players[0], server));
		parentFrame.setTitle(players[0].getIdentifier());
		parentFrame.setResizable(true);
		parentFrame.pack();

		// Creates a new window for each other participant.
		for (int i = 1; i < players.length; i++) {
			
			final Player p = players[i];
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JFrame frame = new JFrame(p.getIdentifier());
					BoardPanel pn = new BoardPanel(scrabble, p, server);
					pn.freezeInv();
					frame.add(pn);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setResizable(true);
					frame.setVisible(true);
				}
			});
		}
		
	}
}


