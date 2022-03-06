package clueGame;

public enum DoorDirection {
	UP ('^'), DOWN ('v'), LEFT ('<'), RIGHT ('>'), NONE;
	
	private char direction;
	
	DoorDirection(char c) {
		direction = c;
	}
	
	DoorDirection() {
	}
	
}
