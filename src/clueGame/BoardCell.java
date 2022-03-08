package clueGame;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	
	
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		this.secretPassage = ' ';
		adjList = new HashSet<BoardCell>();
		doorDirection = DoorDirection.NONE;
	}


	public void addAdj (BoardCell c) {
		adjList.add(c);
	}


	public DoorDirection getDoorDirection() {
		return doorDirection;
	}


	public boolean isLabel() {
		return roomLabel;
	}


	public boolean isRoomCenter() {
		return roomCenter;
	}


	public boolean isRoom() {
		return isRoom;
	}


	public char getSecretPassage() {
		return secretPassage;
	}
	
	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}
	
	public char getInitial() {
		return initial;
	}


	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}


	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}


	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}


	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}


	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}


	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}


	public void setInitial(char initial) {
		this.initial = initial;
	}


	public Set<BoardCell> getAdjList() {
		// TODO Auto-generated method stub
		return adjList;
	}


	public boolean isOccupied() {
		return isOccupied;
	}
	
	
	
}
