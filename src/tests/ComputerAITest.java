package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Room;

class ComputerAITest {
	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	@Test
	void makeSuggestionTest() {
		ComputerPlayer test = (ComputerPlayer) board.getPlayers().get(2);
		int counter = 0;
		boolean skip = true;
		for(Card c: board.getDeck()) {
			if(skip) {
				return;
			}else {
				test.updateSeen(c);
			}
			
		}
		for(int i = 0; i < 200; i++) {
			test.makeSuggestion(board.getRoom('K'));
		}
	}
	
	
	@Test
	void moveTest() {
		ComputerPlayer test = new ComputerPlayer("test", Color.black, 4, 19);
		test.move(1);
		Room room = board.getRoom(board.getCell(test.getRow(), test.getColumn()));
		assertEquals(room.getName(), "Mud Room");
		ComputerPlayer test2 = new ComputerPlayer("test", Color.black, 18, 1);
		int counter180 = 0;
		int counter171 = 0;
		int counter182 = 0;
		for(int i = 0; i < 100; i++) {
			test2 = new ComputerPlayer("test", Color.black, 18, 1);
			test2.move(1);
			if(test2.getRow() == 18 && test2.getColumn() == 0) {
				counter180++;
			}else if(test2.getRow() == 18 && test2.getColumn() == 2) {
				counter182++;
			}else if(test2.getRow() == 17 && test2.getColumn() == 1) {
				counter171++;
			}
		}
		assertTrue(counter180 > 0);
		assertTrue(counter182 > 0);
		assertTrue(counter171 > 0);
	}

}
