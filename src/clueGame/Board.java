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
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() {
		try {
			loadSetupConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}

		try {
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}

		calcAdjacencies();
	}

	public void loadSetupConfig() throws BadConfigFormatException  {
		File setup = new File(setupConfigfile);
		Scanner reader = null;
		try {
			reader = new Scanner(setup);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(reader.hasNextLine()) {
			String space = reader.nextLine();
			String[] line = space.split(", ");
			if(line[0].substring(0,2).equals("//")) {
				continue;
			}else if (line[0].equals("Room") || line[0].equals("Space")){
				Room temp = new Room(line[1]);
				char sym = line[2].charAt(0);
				roomMap.put(sym, temp);
			} else {
				throw new BadConfigFormatException();
			}
		}
	}

	public void setupGrid() {
		File layout = new File(layoutConfigFile);
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int rows = 0, cols = 1;
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
		grid = new BoardCell[numRows][numColumns];
		for(int i =0; i < numRows; i++) {
			for(int j =0; j < numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		setupGrid();
		File layout = new File(layoutConfigFile);
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int row = 0, col;
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			col = 0;
			char last = ',';
			for(char c: line.toCharArray()) {
				if(c == ',') {
					col++;
					last = c;
					continue;
				}else if(roomMap.containsKey(c) && last == ',') {
					grid[row][col].setInitial(c);
					if (c != 'W' && c != 'X') {
						grid[row][col].setRoom(true);
					} else {
						grid[row][col].setRoom(false);
					}

					last = c;
				}else {
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
			if (col+1 != numColumns) {
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
		if(cell.isRoom() || cell.getInitial() == 'W' || cell.getInitial() == 'X') {
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
		for (BoardCell c: thisCell.getAdjList()) {
			if(visited.contains(c)) {
				continue;
			}
			if (c.isRoom() ) {
				targets.add(c);
				continue;
			}
			if (c.isOccupied()) {
				continue;
			}
			visited.add(c);
			if (stepsLeft == 1) {
				targets.add(c);
			} else {
				findAllTargets(c, stepsLeft-1);
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
				if (grid[i][j].getSecretPassage() != ' ') {
					roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(roomMap.get(grid[i][j].getSecretPassage()).getCenterCell());
				} else if (grid[i][j].getInitial() == 'W'){
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
		if (adj.getInitial() == 'W') {
			current.addAdj(adj);
		} else if (adj.isRoom() && current.isDoorway()) {
			if (current.getDoorDirection() == dd) {
				BoardCell roomCenter = roomMap.get(adj.getInitial()).getCenterCell();
				current.addAdj(roomCenter);
				roomCenter.addAdj(current);
			}
		}
	}



}
