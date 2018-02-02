package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.cmu.cs.cs214.hw4.core.*;

public class BoardPanel extends JPanel implements GameSubscriber {
	private Scrabble scrabble;
	private Player player;
	private final int GRID_HEIGHT = 15;
	private final int GRID_WIDTH = 15;
	public final JButton[][] squares;
	public final JButton[] rack;
	public JButton play;
	public JButton pass;
	public JButton reset;
	public final JLabel[] scores;
	public JLabel currPlayer;
	private String image;               //String temporarily holds tile chosen from rack
	private ArrayList<Tile> movTile;
	private ArrayList<Integer[]> movCord;
	public final GameServer server;

	private Board board;
	private Tile[] inventory;

	private JButton[][] boardButtons = new JButton[GRID_HEIGHT][GRID_WIDTH];
	private ArrayList<JButton> inventoryButtons = new ArrayList<JButton>();

	private JPanel fullPanel;
	private JLabel name;

	/**
	 * Constructs a GameBoardPanel with reference to game and player
	 * 
	 * @param game
	 *            ScrabbleGame containing the Player p
	 * @param p
	 *            Player who's game is represented by the panel
	 */
	public BoardPanel(Scrabble scrabble, Player p, GameServer server) {
		this.scrabble = scrabble;
		this.board = scrabble.getBoard();
		this.player = p;
		this.inventory = player.getInventory();
		rack = new JButton[14];
		squares = new JButton[GRID_WIDTH][GRID_HEIGHT];
		scores = new JLabel[scrabble.getNumPlayers()];
		image = "";
		movTile = new ArrayList<Tile>();
		movCord = new ArrayList<Integer[]>();
		this.server = server;

		initGui();

		server.subscribe(this);
	}

	private void initGui() {
		setLayout(new BorderLayout());

		// Create a board panel that is a grid of buttons representing board
		// spaces
		add(createBoardPanel(board), BorderLayout.CENTER);

		add(scorePanel(), BorderLayout.NORTH);
		add(purchasePanel(), BorderLayout.EAST);
		add(blankPanel(), BorderLayout.WEST);

		// Create a rack panel that is an array of buttons representing rack
		// spaces
		add(createInventoryPanel(inventory), BorderLayout.SOUTH);

	}

	private JPanel purchasePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JLabel purchase = new JLabel("Purchase Special Tiles");
		purchase.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		panel.add(purchase);
		panel.add(new JLabel("   Feature    Price"));
		panel.add(new JButton("Boom         10"));
		panel.add(new JButton("Reverse      10"));
		panel.add(new JButton("Neg Points  10"));
		panel.add(new JButton("Custom       10"));
		panel.add(new JButton("Extra Turn   10"));

		return panel;
	}

	private JPanel scorePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel score = new JLabel("Current Scores");
		panel.add(score);
		panel.add(new JLabel(" "));

		for (int i = 0; i < scrabble.getNumPlayers(); i++) {
			scores[i] = new JLabel(scrabble.getPlayers()[i].getIdentifier()
					+ " : " + scrabble.getPlayers()[i].getScore() + " Points ");
			panel.add(scores[i],BorderLayout.WEST);
		}
		panel.add(new JLabel(" "));

		JPanel curr = new JPanel();
		currPlayer = new JLabel ("Current Player's Turn : "+ scrabble.getCurrentPlayer().getIdentifier());
		curr.add(currPlayer);
		
		panel.add(curr, BorderLayout.CENTER);
		panel.add(new JLabel(" "));
		
		return panel;
	}
	
	private JPanel blankPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel lab = new JLabel("                                             ");
		panel.add(lab);
		return panel;
	}

	/**
	 * Changes the text in the name text field and updates with current score
	 */

	protected String getType(int i) {
		switch (i) {
		case 1:
			return "";
		case 2:
			return "2L";
		case 3:
			return "3L";
		case 4:
			return "2W";
		case 5:
			return "3W";
		default:
			return null;
		}
	}

	protected Color getColor(int i) {
		switch (i) {
		case 1:
			return Color.YELLOW;
		case 2:
			return Color.GREEN;
		case 3:
			return Color.orange;
		case 4:
			return Color.magenta;
		case 5:
			return Color.red;
		default:
			return null;
		}
	}

	/**
	 * Creates a grid of buttons corresponding to board spaces. Buttons are blue
	 * if they represent a space that contains a letter which is part of a valid
	 * word, otherwise they are white.
	 */
	public JPanel createBoardPanel(Board board) {

		Square[][] old_squares = board.getSquares();
		Color color = null;
		Color fg = null;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(GRID_HEIGHT, GRID_WIDTH));
		
		// Create all of the squares and display them.
		for (int x = 0; x < GRID_HEIGHT; x++) {
			for (int y = 0; y < GRID_WIDTH; y++) {
				squares[x][y] = new JButton();
								
				fg = null;
				String txt = getType(old_squares[x][y].getType());
				color = getColor(old_squares[x][y].getType());
				
				if (x == 7 && y == 7) {
					txt = "Start";
					color = Color.BLUE;
					fg = Color.white;
				}
				squares[x][y].setText(txt);
				squares[x][y].addActionListener(new SquareListener(x, y, this));
				squares[x][y].setBackground(color);
				squares[x][y].setForeground(fg);
				panel.setBackground(color);
				panel.add(squares[x][y]);
			}
		}

		return panel;
	}

	protected String getKind(Tile t) {
		if (t instanceof ReverseOrder)
			return "Rvs";
		if (t instanceof Boom)
			return "Bom";
		if (t instanceof NegativePoints)
			return "Ng";
		if (t instanceof Custom)
			return "Cst";

		return null;
	}

	public JPanel createInventoryPanel(Tile[] inventory) {
		JPanel rackPanel = new JPanel();              //rack panel
		String txt;
		for (int x = 0; x < 7; x++) {
			rack[x] = new JButton();
			if (inventory[x] == null)
				txt = "";
			else if (inventory[x] instanceof RegularTile)
				txt = ((RegularTile) inventory[x]).getLetter();
			else
				txt = getKind(inventory[x]);

			rack[x].setText(txt);
			rack[x].addActionListener(new RackListener(x, rack[x], scrabble,
					this, player));
			rackPanel.add(rack[x]);
		}

		Color color = Color.PINK;
		for (int x = 7; x < 14; x++) {
			rack[x] = new JButton();
			if (inventory[x] == null)
				txt = "";
			else if (inventory[x] instanceof RegularTile)
				txt = ((RegularTile) inventory[x]).getLetter();
			else
				txt = getKind(inventory[x]);

			rack[x].setText(txt);
			rack[x].addActionListener(new SpecialListener(x, rack[x], scrabble,
					this, player));
			rack[x].setBackground(color);
			rackPanel.add(rack[x]);
		}

		play = new JButton("Play");
		play.setBackground(Color.CYAN);
		play.addActionListener(new PlayListener(this, scrabble, player));
		rackPanel.add(play);

		pass = new JButton("Pass");
		pass.setBackground(Color.blue);
		pass.setForeground(Color.white);
		pass.addActionListener(new PassListener(this, scrabble, player));
		rackPanel.add(pass);
				
		reset = new JButton("Reset");
		reset.setBackground(Color.white);
		reset.addActionListener(new ResetListener(this, scrabble, player));
		rackPanel.add(reset);
		return rackPanel;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String img) {
		image = img;
	}

	public ArrayList<Tile> getMovTile() {
		return movTile;
	}

	public void setMovTile(ArrayList<Tile> movTile) {
		this.movTile = movTile;
	}

	public ArrayList<Integer[]> getMovCord() {
		return movCord;
	}

	public void setMovCord(ArrayList<Integer[]> movCord) {
		this.movCord = movCord;
	}

	@Override
	public void updateDisplay() {
		for (int x = 0; x < GRID_HEIGHT; x++) {
			for (int y = 0; y < GRID_WIDTH; y++) {
				Square[][] old_squares = board.getSquares();

				if (!board.getSquares()[x][y].hasTile()) {
					String txt = getType(old_squares[x][y].getType());
					Color color = getColor(old_squares[x][y].getType());
					if (x == 7 && y == 7) {
						txt = "Start";
						color = Color.BLUE;
					}
					squares[x][y].setText(txt);
					squares[x][y].setBackground(color);
				} else {
					squares[x][y].setText(board.getSquares()[x][y]
							.getRegularTile().getLetter());
					squares[x][y].setBackground(Color.LIGHT_GRAY);
					squares[x][y].setForeground(Color.black);
				}
			}
		}

		for (int i = 0; i < scrabble.getNumPlayers(); i++) {
			scores[i].setText(scrabble.getPlayers()[i].getIdentifier() + " : "
					+ scrabble.getPlayers()[i].getScore() + " Points ");
		}
		
		currPlayer.setText("Current Player's Turn : "+ scrabble.getCurrentPlayer().getIdentifier());
	}

	public void freezeInv(){
		for (int i = 0; i < 14; i++) 
			rack[i].setEnabled(false);
			
		play.setEnabled(false);
		pass.setEnabled(false);
		reset.setEnabled(false);
	}
	
	public void unfreezeInv(){
		for (int i = 0; i < 14; i++) 
			rack[i].setEnabled(true);
			
		play.setEnabled(true);
		pass.setEnabled(true);
		reset.setEnabled(true);
	}
	
}
