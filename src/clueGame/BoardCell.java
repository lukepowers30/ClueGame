package clueGame;

import java.awt.Color;
import java.awt.Font;
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
	
	
	/*
	 * drawing cells based on room and target
	 */
	public void draw(int cellWidth, int cellHeight, Graphics g, boolean isTarget) {
		if(secretPassage != ' ') {
			g.setColor(new Color(128, 0, 128)); //purple
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
			g.setColor(Color.cyan);
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, cellHeight / 2));
			g.drawString(Character.toString(secretPassage), (int)((this.col + 0.25) * cellWidth), (int)((this.row + 0.75) * cellHeight));
		}else if(isRoom) {
			if (Board.getInstance().getTargets().contains(Board.getInstance().getRoom(this).getCenterCell())) {
				g.setColor(Color.pink);			//if the cell is part of a room and that room is a target highlight the cell
			} else {
				g.setColor(Color.gray);
			}
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
		}else if (this.initial == 'X'){
			g.setColor(Color.black);			// if the cell is unused, make it black
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
		}else {
			if (isTarget) {						// if the cell is a target highlight it
				g.setColor(Color.pink);
			} else {
				g.setColor(Color.yellow);
			}
			g.fillRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);
			g.setColor(Color.black);
			g.drawRect(cellWidth * this.col, cellHeight * this.row, cellWidth, cellHeight);		// only draw a border for walkways
		}
		
	}
	
	
	/*
	 * drawing doorways so that drawing cells does not overwrite
	 */
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
