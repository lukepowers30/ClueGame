package clueGame;

import java.util.HashSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import experiment.TestBoardCell;

public class Board extends JPanel{

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigfile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private Set<Card> deck;
	private Solution theAnswer;
	private ArrayList<Player> players;
	private int currentPlayerIndex;
	
	

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	public void setCurrentPlayerIndex(int currentPlayer) {
		this.currentPlayerIndex = currentPlayer;
	}

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super();
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		this.roomMap = new HashMap<Character, Room>();
		
		this.deck = new HashSet<Card>();
		this.players = new ArrayList<Player>();
		
		this.addMouseListener(new BoardListener());
	}
	// this method returns the only instance of Board (since we are using singleton pattern)
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board
	 */
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		calcAdjacencies();
		makeSolution();
		dealRemainingCards();
		currentPlayerIndex = 0;
		Player currentPlayer = players.get(this.currentPlayerIndex);
		int roll = rollDice();
		calcTargets(grid[currentPlayer.getRow()][currentPlayer.getColumn()], roll);
		((HumanPlayer) currentPlayer).setHasMoved(false);
	}
	

	private void dealRemainingCards() {
		Object[] cardArray = deck.toArray();
		Set<Card> tempDeck = new HashSet<Card>(deck);
		Random rand = new Random();
		int plus = rand.nextInt(cardArray.length);
		int iter = plus;
		do {
			for (Player p: players) {
				if (!tempDeck.isEmpty()) {
					p.updateHand((Card) cardArray[iter]);
					tempDeck.remove((Card) cardArray[iter]);
					iter = (iter + 1) % cardArray.length;
				}
			}
		}while(iter != plus);
		deck.add(theAnswer.getPerson());
		deck.add(theAnswer.getRoom());
		deck.add(theAnswer.getWeapon());
	}
	
	
	private void makeSolution() {
		Random rand = new Random();
		ArrayList<Card> playerDeck = new ArrayList<Card>();
		ArrayList<Card> roomDeck = new ArrayList<Card>();
		ArrayList<Card> weaponDeck = new ArrayList<Card>();
		for (Card c: deck) {
			if (c.getCardType() == CardType.ROOM) {
				roomDeck.add(c);
			} else if (c.getCardType() == CardType.WEAPON) {
				weaponDeck.add(c);
			} else if (c.getCardType() == CardType.CHARACTER) {
				playerDeck.add(c);
			}
		}
		Card solPlayer = playerDeck.get(rand.nextInt(6));
		Card solRoom = roomDeck.get(rand.nextInt(9));
		Card solWeapon = weaponDeck.get(rand.nextInt(6));
		
		deck.remove(solWeapon);
		deck.remove(solPlayer);
		deck.remove(solRoom);
		
		theAnswer = new Solution(solRoom, solPlayer, solWeapon);
	}
	
	
	public void loadSetupConfig() throws BadConfigFormatException  {
		File setup = new File(setupConfigfile);		// getting file and reader for setup
		Scanner reader = null;
		try {
			reader = new Scanner(setup);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(reader.hasNextLine()) {
			String space = reader.nextLine();		// getting the new line
			String[] line = space.split(", ");		// parsing through the line
			if(line[0].substring(0,2).equals("//")) {	// ignore comments
				continue;
			}else if (line[0].equals("Room")){		// handling specific lines by adding room to roomMap
				Room temp = new Room(line[1]);
				char sym = line[2].charAt(0);
				roomMap.put(sym, temp);
				Card room = new Card(line[1], CardType.ROOM);
				deck.add(room);
			} else if (line[0].equals("Space")) {
				Room temp = new Room(line[1]);
				char sym = line[2].charAt(0);
				roomMap.put(sym, temp);
			} else if (line[0].equals("Weapon")){
				Card weapon = new Card(line[1], CardType.WEAPON);
				deck.add(weapon);
			} else if (line[0].equals("Player")) {
				Color color = new Color(0, 0, 0);
				if (line[3].equals("green")) {
					color = Color.green;
				}else if (line[3].equals("blue")) {
					color = Color.cyan;
				}else if (line[3].equals("magenta")) {
					color = Color.magenta;
				}else if (line[3].equals("orange")) {
					color = Color.orange;
				}else if (line[3].equals("red")) {
					color = Color.red;
				}else if (line[3].equals("white")) {
					color = Color.white;
				}
				if (line[1].equals("Human")) {
					
					HumanPlayer player = new HumanPlayer(line[2], color, Integer.parseInt(line[4]), Integer.parseInt(line[5]));
					players.add(player);
					Card card = new Card(line[2], CardType.CHARACTER);
					deck.add(card);
				} else if (line[1].equals("Computer")) {
					ComputerPlayer player = new ComputerPlayer(line[2], color, Integer.parseInt(line[4]), Integer.parseInt(line[5]));
					players.add(player);
					Card card = new Card(line[2], CardType.CHARACTER);
					deck.add(card);
				}
				
			} else {	
				throw new BadConfigFormatException();
			}
		}
	}

	/*
	 * Loops through the layoutConfigFile to determine the number of rows and columns required for the grid
	 * Initializes the grid array and all BoardCells in that grid
	 */
	public void setupGrid() {
		File layout = new File(layoutConfigFile);		//getting file and reader for layout
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int rows = 0, cols = 1;		// getting rows and columns from the file (cols at one because while loop does not count the last column)
		String line = null;
		while(reader.hasNextLine()) {
			line = reader.nextLine();
			rows++;
		}
		numRows = rows;
		for(char c: line.toCharArray()) {
			if(c == ',') {
				cols++;
			}
		}
		numColumns = cols;
		grid = new BoardCell[numRows][numColumns];		// generating board array with new board cells
		for(int i =0; i < numRows; i++) {
			for(int j =0; j < numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
	}

	/*
	 * Reads the layoutConfigFile and inputs that data into the grid array.
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		setupGrid();
		File layout = new File(layoutConfigFile);		//getting file and reader for layout
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int row = 0, col;
		while(reader.hasNextLine()) {				// getting newline
			String line = reader.nextLine();
			col = 0;
			char last = ',';						// setting last to separator character the first character in the newline is a new cell
			for(char c: line.toCharArray()) {
				if(c == ',') {						// going to next cell
					col++;
					last = c;
					continue;
				}else if(roomMap.containsKey(c) && last == ',') {		// if first character in the cell
					grid[row][col].setInitial(c);
					if (c != 'W' && c != 'X') {
						grid[row][col].setRoom(true);
					} else {
						grid[row][col].setRoom(false);
					}

					last = c;
				}else {												// if second character in the cell
					if(roomMap.containsKey(c)) {
						grid[row][col].setSecretPassage(c);
					}else {
						switch (c) {
						case '*':
							grid[row][col].setRoomCenter(true);
							roomMap.get(last).setCenterCell(grid[row][col]);
							break;
						case '#':
							grid[row][col].setRoomLabel(true);
							roomMap.get(last).setLabelCell(grid[row][col]);
							break;
						case '^':
							grid[row][col].setDoorDirection(DoorDirection.UP);
							break;
						case 'v':
							grid[row][col].setDoorDirection(DoorDirection.DOWN);
							break;
						case '<':
							grid[row][col].setDoorDirection(DoorDirection.LEFT);
							break;
						case '>':
							grid[row][col].setDoorDirection(DoorDirection.RIGHT);
							break;
						default:
							throw new BadConfigFormatException("Board Layout refers to a room that does not exist.");
						}
					}
				}
				last = c;
			}
			row++;
			if (col+1 != numColumns) {				//checking for different number of columns
				throw new BadConfigFormatException("board layout file has inconsistent column numbers.");
			}
		}

	}

	public Set<BoardCell> getAdjList(int i, int j) {

		return grid[i][j].getAdjList();
	}

	public void setConfigFiles(String csv, String txt) {
		this.layoutConfigFile = "data/" + csv;
		this.setupConfigfile = "data/" + txt;
	}

	public Room getRoom(char key) {
		return roomMap.get(key);
	}

	public Room getRoom(BoardCell cell) { 
		return roomMap.get(cell.getInitial());
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	/*
	 * Calculates the possible move locations for a given starting cell and roll amount
	 */
	public void calcTargets( BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}	
	
	/*
	 * Recursive function that adds neighboring cells to the Set of targets for the given BoardCell
	 */
	private void findAllTargets(BoardCell thisCell, int stepsLeft) {
		for (BoardCell c: thisCell.getAdjList()) {			// iterate through all adjacent cells
			if(visited.contains(c)) {
				continue;						// if already visited ignore
			}
			if (c.isRoom() ) {					// if its a room add it as a target
				targets.add(c);
				continue;
			}
			if (c.isOccupied()) {				// if the cell is occupied and not a room ignore
				continue;
			}
			visited.add(c);						// add it to visited
			if (stepsLeft == 1) {				// if steps left is one add the adjacent cell
				targets.add(c);
			} else {
				findAllTargets(c, stepsLeft-1);		// else recall function
			}
			visited.remove(c);
		}
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}

	/*
	 * Creates the adjacency list for every BoardCell in grid.
	 * 
	 * This function is executed during the initialization of the board, and it makes calcTargets much easier.
	 */
	public void calcAdjacencies () {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				if (grid[i][j].getSecretPassage() != ' ') {		// if there is a secret passage add it to the adjacencies
					roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(roomMap.get(grid[i][j].getSecretPassage()).getCenterCell());
				} else if (grid[i][j].getInitial() == 'W'){		// add the four cardinal directions appropriately
					if ((i-1) >= 0)
						isAdjacent(grid[i][j], grid[i-1][j], DoorDirection.UP);
					if ((i+1) < numRows)
						isAdjacent(grid[i][j], grid[i+1][j], DoorDirection.DOWN);
					if ((j-1) >= 0)
						isAdjacent(grid[i][j], grid[i][j-1], DoorDirection.LEFT);
					if ((j+1) < numColumns)
						isAdjacent(grid[i][j], grid[i][j+1], DoorDirection.RIGHT);
				}
			}
		}
	}

	/*
	 * Helper function that adds a specific cell to a neighboring cell's adjacency list if it fits the criteria
	 */
	private void isAdjacent(BoardCell current, BoardCell adj, DoorDirection dd) {
		if (adj.getInitial() == 'W') {			// if adjacent cell is a walkway just add it
			current.addAdj(adj);
		} else if (adj.isRoom() && current.isDoorway()) {		// if adjacent cell is a room and the door points the right way add it
			if (current.getDoorDirection() == dd) {
				BoardCell roomCenter = roomMap.get(adj.getInitial()).getCenterCell();
				current.addAdj(roomCenter);
				roomCenter.addAdj(current);
			}
		}
	}
	
	
	public boolean checkAccusation(Solution accusation) {
		return accusation.isEquals(theAnswer);
	}
	
	public Card handleSuggestion(Solution suggestion, Player caller) {
		int index = players.indexOf(caller);
		int stop = index;
		index++;
		Card disprove;
		do {
			disprove = players.get(index).disproveSuggestion(suggestion);
			if(disprove != null) {
				caller.updateSeen(disprove, players.get(index));
				return disprove;
			}
			index = (index + 1) % players.size();
		}while(index != stop);
		return null;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int cellWidth = super.getWidth() / this.numColumns;
		int cellHeight = super.getHeight() / this.numRows;
		for (BoardCell[] row: grid) {
			for(BoardCell cell: row) {
				cell.draw(cellWidth, cellHeight, g, false);
			}
		}
		for (BoardCell cell: targets) {
			cell.draw(cellWidth, cellHeight, g, true);
		}
		for (BoardCell[] row: grid) {
			for(BoardCell cell: row) {
				cell.drawDoorways(cellWidth, cellHeight, g);
			}
		}
		for(Map.Entry<Character, Room> entry: this.roomMap.entrySet()) {
			entry.getValue().drawRoomName(cellWidth, cellHeight, g);
		}
		for(Player p: players) {
			if(getCell(p.getRow(), p.getColumn()).isRoom()) {
				continue;
			}else {
				p.drawPlayer(cellWidth, cellHeight, g);
			}
		}
		for(Map.Entry<Character, Room> entry: this.roomMap.entrySet()) {
			int counter = 0;
			for(Player p: entry.getValue().getOccupied()) {
				p.drawPlayerInRoom(cellWidth, cellHeight, g, counter);
				counter++;
			}
		}
	}
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Set<Card> getDeck() {
		return deck;
	}
	
	public Solution getSolution () {
		return theAnswer;
	}
	
	public void setTheAnswer(Solution theAnswer) {
		this.theAnswer = theAnswer;
	}
	
	public void setPlayers(ArrayList<Player>  players) {
		this.players = players;
	}
	
	public void goToNextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % 6;
		Player currentPlayer = players.get(this.currentPlayerIndex);
		int roll = rollDice();
		if (currentPlayer instanceof HumanPlayer) {
			calcTargets(grid[currentPlayer.getRow()][currentPlayer.getColumn()], roll);
			if(targets.isEmpty()) {
				JOptionPane noMoves = new JOptionPane();
				noMoves.showMessageDialog(this, "You have no possible moves.");
			}else {
				((HumanPlayer) currentPlayer).setHasMoved(false);
				this.repaint();
			}
		}else {
			calcTargets(grid[currentPlayer.getRow()][currentPlayer.getColumn()], roll);
			((ComputerPlayer) currentPlayer).makeAccusation();
			((ComputerPlayer) currentPlayer).move(roll);
			repaint();
			if(grid[currentPlayer.getRow()][currentPlayer.getColumn()].isRoomCenter()) {
				((ComputerPlayer) currentPlayer).makeSuggestion(this.getRoom(grid[currentPlayer.getRow()][currentPlayer.getColumn()]));
			}
		}
		
	}
	
	public static int rollDice() {
		Random rand = new Random();
		int roll = rand.nextInt(6)+1;
		GameControlPanel.setDiceRoll(roll);
		return roll;
	}


	private class BoardListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(currentPlayerIndex != 0) {
				return;
			}else {
				int cellWidth = Board.super.getWidth() / Board.getInstance().numColumns;
				int cellHeight = Board.super.getHeight() / Board.getInstance().numRows;
				int column = e.getX() / cellWidth;
				int row = e.getY() / cellHeight;
				BoardCell clickedCell = getCell(row, column);
				if(targets.contains(clickedCell) || clickedCell.isRoom() && targets.contains(getRoom(clickedCell).getCenterCell())) {
					if(clickedCell.isRoom()) {
						((HumanPlayer) players.get(0)).move(getRoom(clickedCell).getCenterCell());
					}else {
						((HumanPlayer) players.get(0)).move(getCell(row, column));
					}
					repaint();
					((HumanPlayer) players.get(0)).setHasMoved(true);
					((HumanPlayer) players.get(0)).makeSuggestion();
				}else {
					JOptionPane error = new JOptionPane();
					error.showMessageDialog(Board.getInstance(), "That is not a Target.");
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
