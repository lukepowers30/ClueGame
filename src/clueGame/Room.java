package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private ArrayList<Player> occupied;
	
	public Room(String name) {
		super();
		this.name = name;
		occupied = new ArrayList<Player>();
	}
	
	public void addPlayer(Player p) {
		occupied.add(p);
	}
	
	public void removePlayer(Player p) {
		occupied.remove(p);
	}

	public ArrayList<Player> getOccupied() {
		return occupied;
	}

	public String getName() {
		return name;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
	public void drawRoomName(int cellWidth, int cellHeight, Graphics g) {
		if(this.labelCell != null) {
			g.setColor(Color.blue);
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, cellHeight / 2));
			g.drawString(name, (labelCell.getCol() - 1) * cellWidth, (labelCell.getRow() + 1) * cellHeight);
		}
	}
	
	
}
