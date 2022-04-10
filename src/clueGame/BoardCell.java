package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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
	
	
	
	/*
	 * Constructor for BoardCell only takes the row and column. 
	 * All other attributes are filled after the constructor.
	 */
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
	
	/*
	 * Returns true unless doorDirection is NONE
	 */
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
		return adjList;
	}


	public boolean isOccupied() {
		return isOccupied;
	}


	public int getRow() {
		return row;
	}


	public int getCol() {
		return col;
	}
	
	public void draw(int cellWidth, int cellHeight, Graphics g) {
		if(isRoom) {
			g.setColor(Color.gray);
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
		}else if (this.initial == 'X'){
			g.setColor(Color.black);
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
		}else {
			g.setColor(Color.yellow);
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
			g.setColor(Color.black);
			g.drawRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
		}
		
	}
	
	public void drawDoorways(int cellWidth, int cellHeight, Graphics g) {
		int doorWidth = (int) (0.1 * cellWidth);
				
		g.setColor(Color.blue);
		switch(this.doorDirection) {
		case RIGHT:
			g.fillRect(cellWidth * (this.col + 1), cellHeight * this.row, doorWidth, cellHeight);
			break;
		case LEFT:
			g.fillRect(cellWidth * this.col - doorWidth, cellHeight * this.row, doorWidth, cellHeight);
			break;
		case UP:
			g.fillRect(cellWidth * this.col, cellHeight * this.row - doorWidth, cellWidth, doorWidth);
			break;
		case DOWN:
			g.fillRect(cellWidth * this.col, cellHeight * (this.row + 1), cellWidth, doorWidth);
			break;
		default:
			break;
		}
	}
	
}
