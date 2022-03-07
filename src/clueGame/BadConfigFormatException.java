package clueGame;

public class BadConfigFormatException extends Exception {
	
	public BadConfigFormatException() {
		super("Could not cunstruct board");
	}

	public BadConfigFormatException(String message) {
		super(message);
		
	}

}
