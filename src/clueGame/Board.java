package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import experiment.TestBoardCell;

public class Board {

	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigfile;
	private Map<String, Room> roomMap;
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
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){

	}

	public void loadSetupConfig() {

	}

	public void loadLayoutConfig() {

	}

	public void setConfigFiles(String csv, String txt) {

	}

	public Room getRoom(char key) {
		return new Room();
	}

	public Room getRoom(BoardCell cell) {
		return new Room();
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int row, int col) {
		return new BoardCell();
	}

}
