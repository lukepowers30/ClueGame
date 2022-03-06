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
	public void initialize(){
		loadSetupConfig();
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
		loadLayoutConfig();
	}

	public void loadSetupConfig() {
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
			}else{
				Room temp = new Room(line[1]);
				char sym = line[2].charAt(0);
				roomMap.put(sym, temp);
			}
		}
	}

	public void loadLayoutConfig() {
		File layout = new File(layoutConfigFile);
		Scanner reader = null;
		try {
			reader = new Scanner(layout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
					}
					last = c;
				}
			}
			row++;
		}
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
	
	public static void main(String[] args) {
		// Board is singleton, get the only instance
		Board board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	

}
