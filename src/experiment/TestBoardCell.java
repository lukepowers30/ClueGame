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


	public Set<TestBoardCell> getAdjList() {
		return new HashSet<TestBoardCell>();
	}


	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	
	

}
