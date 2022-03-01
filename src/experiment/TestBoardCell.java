package experiment;

import java.util.*;

public class TestBoardCell {
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	private Set<TestBoardCell> adjList;
	
	
	public boolean isOccupied() {
		return isOccupied;
	}


	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}


	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isRoom( ) {
		return this.isRoom;
	}


	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}


	public TestBoardCell(int row, int col) {
		super();
		adjList = new HashSet<TestBoardCell>();
		this.row = row;
		this.col = col;
	}


	public int getRow() {
		return row;
	}


	public int getCol() {
		return col;
	}
	
	public void addAdj (TestBoardCell c) {
		adjList.add(c);
	}
	
	
	
	

}
