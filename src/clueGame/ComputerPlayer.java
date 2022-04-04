package clueGame;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}
	
	public Solution makeSuggestion(Room room) {
		return null;
	}
	
	public void move(int pathLength) {
		Board board = Board.getInstance();
		board.calcTargets(board.getCell(this.getRow(), this.getColumn()), pathLength);
		Set<BoardCell> targets = board.getTargets();
		for(BoardCell c: targets) {
			if(c.isRoom()) {
				this.setRow(c.getRow());
				this.setColumn(c.getCol());
				return;
			}
		}
		Object[] arr = targets.toArray();
		Random rand = new Random();
		BoardCell destination = (BoardCell) arr[rand.nextInt(arr.length)];
		this.setRow(destination.getRow());
		this.setColumn(destination.getCol());
	}

}
