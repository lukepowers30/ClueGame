package clueGame;

import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoardCell;

public class Board {

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigfile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		this.roomMap = new HashMap<Character, Room>();
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
			}else if (line[0].equals("Room") || line[0].equals("Space")){		// handling specific lines by adding room to roomMap
				Room temp = new Room(line[1]);
				char sym = line[2].charAt(0);
				roomMap.put(sym, temp);
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
		if(cell.isRoom() || cell.getInitial() == 'W' || cell.getInitial() == 'X') {		// X and W are not defined as a room but are in the roomMap 
			return roomMap.get(cell.getInitial());
		}else {
			return null;
		}
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
	public void calcTargets( BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}	
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



}
