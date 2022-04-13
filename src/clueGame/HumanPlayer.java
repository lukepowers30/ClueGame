package clueGame;

import java.awt.Color;

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

}
