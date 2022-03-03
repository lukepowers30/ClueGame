package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	public String getName() {
		return name;
	}

	public BoardCell getLabelCell() {
		return new BoardCell();
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	
}
