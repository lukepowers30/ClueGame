package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;

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
		ComputerPlayer test = (ComputerPlayer) board.getPlayers().get(1);
		
		//Test for room matches
		Solution sol1 = test.makeSuggestion(board.getRoom('K'));
		assertEquals("Kitchen", sol1.getRoom().getCardName());
		
		
		
		// Adding cards to seen
		test.updateSeen(new Card("George", CardType.CHARACTER), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Bob", CardType.CHARACTER), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Joe", CardType.CHARACTER), new ComputerPlayer("George", Color.BLACK, 0, 0));
		int counterJane = 0;
		int counterMichael = 0;
		int counterSally = 0;
		
		test.updateSeen(new Card("Revolver", CardType.WEAPON), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Dagger", CardType.WEAPON), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Lead Pipe", CardType.WEAPON), new ComputerPlayer("George", Color.BLACK, 0, 0));
		int counterRope = 0;
		int counterCandleStick = 0;
		int counterWrench = 0;
		
		test.setHand(new HashSet<Card>());
		
		// Tests for multiple unseen weapons and people
		for(int i = 0; i < 2000; i++) {
			Solution sol2 = test.makeSuggestion(board.getRoom('K'));
			if (sol2.getPerson().getCardName().equals("Jane")) {
				counterJane++;
			} else if (sol2.getPerson().getCardName().equals("Michael")) {
				counterMichael++;
			} else if (sol2.getPerson().getCardName().equals("Sally")) {
				counterSally++;
			}
			if (sol2.getWeapon().getCardName().equals("Rope")) {
				counterRope++;
			} else if (sol2.getWeapon().getCardName().equals("Candlestick")) {
				counterCandleStick++;
			} else if (sol2.getWeapon().getCardName().equals("Wrench")) {
				counterWrench++;
			} 
		}
		assertTrue(counterJane > 1);
		assertTrue(counterMichael > 1);
		assertTrue(counterSally > 1);
		assertTrue(counterRope > 1);
		assertTrue(counterCandleStick > 1);
		assertTrue(counterWrench > 1);
		
		// Only one weapon and person not seen
		test.updateSeen(new Card("Jane", CardType.CHARACTER), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Michael", CardType.CHARACTER), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Rope", CardType.WEAPON), new ComputerPlayer("George", Color.BLACK, 0, 0));
		test.updateSeen(new Card("Candlestick", CardType.WEAPON), new ComputerPlayer("George", Color.BLACK, 0, 0));
		
		Solution sol3 = test.makeSuggestion(board.getRoom('K'));
		assertEquals("Sally", sol3.getPerson().getCardName());
		assertEquals("Wrench", sol3.getWeapon().getCardName());
	}
	
	
	@Test
	void moveTest() {
		// Tests for an unseen room
		ComputerPlayer test = new ComputerPlayer("test", Color.black, 4, 19);
		test.move(1);
		Room room = board.getRoom(board.getCell(test.getRow(), test.getColumn()));
		assertEquals(room.getName(), "Mud Room");
		
		// Test for no room
				ComputerPlayer test2 = new ComputerPlayer("test", Color.black, 18, 1);
				int counter180 = 0;
				int counter171 = 0;
				int counter182 = 0;
				for(int i = 0; i < 100; i++) {
					test2 = new ComputerPlayer("test", Color.black, 18, 1);
					test2.move(1);
					if(test2.getRow() == 18 && test2.getColumn() == 0) {
						board.getCell(18, 0).setOccupied(false);
						counter180++;
					}else if(test2.getRow() == 18 && test2.getColumn() == 2) {
						counter182++;
						board.getCell(18, 2).setOccupied(false);
					}else if(test2.getRow() == 17 && test2.getColumn() == 1) {
						counter171++;
						board.getCell(17, 1).setOccupied(false);
					}
				}
				assertTrue(counter180 > 0);
				assertTrue(counter182 > 0);
				assertTrue(counter171 > 0);
		
		ComputerPlayer test3 = new ComputerPlayer("test", Color.black, 4, 19);
		test3.updateSeen(new Card("Mud Room", CardType.ROOM), new ComputerPlayer("George", Color.BLACK, 0, 0));
		
		int counter418 = 0;
		int counter519 = 0;
		int counter420 = 0;
		int counter224 = 0;
		for(int i = 0; i < 100; i++) {
			test3 = new ComputerPlayer("test", Color.black, 4, 19);
			test3.updateSeen(new Card("Mud Room", CardType.ROOM), new ComputerPlayer("George", Color.BLACK, 0, 0));
			test3.move(1);
			if(test3.getRow() == 4 && test3.getColumn() == 18) {
				counter418++;
				board.getCell(4, 18).setOccupied(false);
			}else if(test3.getRow() == 5 && test3.getColumn() == 19) {
				counter519++;
				board.getCell(5, 19).setOccupied(false);
			}else if(test3.getRow() == 4 && test3.getColumn() == 20) {
				counter420++;
				board.getCell(4, 20).setOccupied(false);
			}else if(test3.getRow() == 2 && test3.getColumn() == 24) {
				counter224++;
				board.getCell(2, 24).setOccupied(false);
			}
		}
		assertTrue(counter224 > 1);
		assertTrue(counter418 > 1);
		assertTrue(counter519 > 1);
		assertTrue(counter420 > 1);
		
		
	}

}
