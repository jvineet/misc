package edu.cmu.cs.cs214.hw4.core;

public class Scrabble {
	private Player[] players;
	private TileBox tileBox;
	private int numPlayers;
	private Board board;
	private int playerInd;
	private int scoreMultiplier;
	private Dictionary dictionary;
	
	public Scrabble(Player[] pl, TileBox tb, Board brd, Dictionary dn){
		players = pl;
		tileBox = tb;
		numPlayers = pl.length;
		board = brd;
		playerInd = 0;
		scoreMultiplier = 1;
		dictionary = dn;
	}
	
	//GUI calls this method
	public int Play(Move move){
		if(move.isPass()){
			pass();
			return 1;
		}
		else if(isValid(move)){
			moveAndUpdate(move);
			return 1;
		}
		else if(move.getSpecialTile() != null){
			purchaseOnly(move);	
			return 1;
		}
		
		return 0;
	}
	
	//checks if input move is valid
	public boolean isValid(Move move){
		//checks if player is not using just special tiles
		if (!move.checkRegTiles())
			return false;
		//checks if coordinates are valid and have no regular tile
		if(!board.isValidLocation(move))
			return false;
		//to check if all the new words formed are valid
		if(!board.checkAllWords(move, dictionary))
			return false;
		
		return true;					
	}
		
	//if only special tile purchase is made and word not played
	public void purchaseOnly(Move move){
		Player currentPlayer = getCurrentPlayer();
		getSpecialTile(currentPlayer, move.getSpecialTile());
		changePlayer();
	}
	
	//executes the move and updates the game
	public void moveAndUpdate(Move move){
		Player currentPlayer = getCurrentPlayer();
		removeTiles(currentPlayer, move);		
		int score = board.moveScore(move, this);
		currentPlayer.updateScore(scoreMultiplier*score);
		setScoreMultiplier(1);                             //resets the negIndicator to 1
		currentPlayer.refillTiles(tileBox);
		getSpecialTile(currentPlayer, move.getSpecialTile());
		changePlayer();		
	}
		
	private void removeTiles(Player currentPlayer, Move move) {
		RegularTile[] reg = move.getRegMoveTiles();
		for (int i=0; i<reg.length; i++)
			currentPlayer.removeRegTile(reg[i]);
	}

	public Player getCurrentPlayer(){
		return players[playerInd];
	}
	
	//adds special tile to current player's inventory , if enough points
	public void getSpecialTile(Player player, SpecialTile tile){
		if (tile != null && player.getScore() > tile.getPrice()){
			player.addSpecialTile(tile);			
		}
	}
	
	public void changePlayer(){
		playerInd = (playerInd+1)%numPlayers;
	}
	
	//reverses order of play
	public void reversePlayers(){
		Player temp;
		for(int i=0; i<numPlayers/2; i++){
			temp = players[i];
			players[i] = players[numPlayers-1-i];
			players[numPlayers-1-i] = temp;			
		}
	}
		
	//checks if the game is over
	public void gameOver(){
		if(tileBox.getNumTiles() == 0)
			printResults();		
	}
	
	//if the player passes his turn
	public void pass(){
		changePlayer();
	}

	public Board getBoard(){
		return board;
	}
	
	public int getNumPlayers(){
		return numPlayers;
	}
	
	public Player[] getPlayers(){
		return players;
	}
	
	//used to keep track that "Negative Points" special tile was activated
	public void setScoreMultiplier(int i){
		scoreMultiplier = i;
	}
	
	//Displays results when game is over
	public void printResults(){
		int maxPlayer = 0;
		for(int i=0; i<numPlayers;i++){
			if(players[i].getScore()>players[maxPlayer].getScore())
				maxPlayer = i;
		}
		System.out.print("Winner : ");
		System.out.println(players[maxPlayer].getIdentifier());		
	}
}
