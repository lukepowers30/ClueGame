package experiment;

import java.util.HashSet;
import java.util.Set;

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


	public Set<TestBoardCell> getAdjList(Set<TestBoardCell> adjList) {
		return null;
	}


	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	
	

}
