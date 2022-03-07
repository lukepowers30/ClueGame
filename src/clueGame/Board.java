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
		
	}

	public void loadSetupConfig() throws BadConfigFormatException  {
		File setup = new File(setupConfigfile);
		Scanner reader = null;
		try {
			reader = new Scanner(setup);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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

	public void loadLayoutConfig() throws BadConfigFormatException {
		File layout = new File(layoutConfigFile);
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
		
		File layout2 = new File(layoutConfigFile);
		Scanner reader2 = null;
		try {
			reader2 = new Scanner(layout2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int row = 0, col;
		while(reader2.hasNextLine()) {
			String line2 = reader2.nextLine();
			col = 0;
			char last = ',';
			for(char c: line2.toCharArray()) {
				if(c == ',') {
					col++;
					last = c;
					continue;
				}else if(roomMap.containsKey(c) && last == ',') {
					grid[row][col].setInitial(c);
					grid[row][col].setRoom(true);
					last = c;
				}else {
					if(roomMap.containsKey(c)) {
						grid[row][col].setSecretPassage(c);
					}else if(c == '*') {
						grid[row][col].setRoomCenter(true);
						roomMap.get(last).setCenterCell(grid[row][col]);
					}else if(c == '#') {
						grid[row][col].setRoomLabel(true);
						roomMap.get(last).setLabelCell(grid[row][col]);
					}else if(c == '^'){
						grid[row][col].setDoorDirection(DoorDirection.UP);
					}else if(c == 'v'){
						grid[row][col].setDoorDirection(DoorDirection.DOWN);
					}else if(c == '<'){
						grid[row][col].setDoorDirection(DoorDirection.LEFT);
					}else if(c == '>'){
						grid[row][col].setDoorDirection(DoorDirection.RIGHT);
					} else {
						throw new BadConfigFormatException("Board Layout refers to a room that does not exist.");
					}
					last = c;
				}
			}
			row++;
			if (col+1 != numColumns) {
				throw new BadConfigFormatException("board layout file has inconsistent column numbers.");
			}
		}
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		
		return new HashSet<BoardCell>();
	}

	public void setConfigFiles(String csv, String txt) {
		this.layoutConfigFile = "data/" + csv;
		this.setupConfigfile = "data/" + txt;
	}

	public Room getRoom(char key) {
		return roomMap.get(key);
	}

	public Room getRoom(BoardCell cell) {
		if(cell.isRoom()) {
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
	public void calcTargets(BoardCell cell, int i) {
		// TODO Auto-generated method stub
		
	}
	public Set<BoardCell> getTargets() {
		// TODO Auto-generated method stub
		return new HashSet<BoardCell>();
	}
	

	

}
