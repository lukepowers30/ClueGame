package clueGame;

import java.awt.Color;

import javax.swing.JOptionPane;

public class HumanPlayer extends Player {
	
	private boolean hasMoved;
	
	public boolean isHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public HumanPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}

	public void move(BoardCell c) {
		Board board = Board.getInstance();
		if(board.getCell(this.getRow(), this.getColumn()).isRoom()) {
			board.getRoom(board.getCell(this.getRow(), this.getColumn())).removePlayer(this);
		}else {
			board.getCell(this.getRow(), this.getColumn()).setOccupied(false);
		}
		if(c.isRoom()) {
			board.getRoom(c).addPlayer(this);
		}
		c.setOccupied(true);
		this.setRow(c.getRow());
		this.setColumn(c.getCol());
		board.getTargets().clear();
	}

	public void makeSuggestion() {
		JOptionPane suggstion = new JOptionPane();
		
	}
}
