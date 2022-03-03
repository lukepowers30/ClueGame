package clueGame;

import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	private char intitial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	
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


	public char getSecretPassage() {
		return secretPassage;
	}
	
	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}
	
}
