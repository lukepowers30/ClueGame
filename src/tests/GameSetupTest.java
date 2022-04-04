package tests;

import clueGame.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSetupTest {
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
	void playersInitializedTest() {
		int humanCount = 0;
		int computerCount = 0;
		for (Player p: board.getPlayers()) {
			if (p instanceof clueGame.HumanPlayer) {
				humanCount++;
				assertEquals(p.getName(), "George");
			} else if (p instanceof clueGame.ComputerPlayer) {
				computerCount++;
			}
		}
		
		assertEquals(board.getPlayers().size(), 6);
		assertEquals(humanCount, 1);
		assertEquals(computerCount, 5);
	}
	
	@Test
	void deckTest () {
		int roomCt = 0;
		int  weaponCt = 0;
		int characterCt = 0;
		for (Card c: board.getDeck()) {
			if (c.getCardType() == CardType.ROOM) {
				roomCt++;
			} else if (c.getCardType() == CardType.CHARACTER) {
				characterCt++;
			} else if (c.getCardType() == CardType.WEAPON) {
				weaponCt++;
			}
		}
		assertEquals(board.getDeck().size(), 21);
		assertEquals(roomCt, 9);
		assertEquals(characterCt, 6);
		assertEquals(weaponCt, 6);
	}
	
	@Test
	void solutionTest () {
		Solution sol = board.getSolution();
		assertTrue(sol.getPerson().getCardType() == CardType.CHARACTER);
		assertTrue(sol.getWeapon().getCardType() == CardType.WEAPON);
		assertTrue(sol.getRoom().getCardType() == CardType.ROOM);
	}
	
	@Test
	void allCardsDealtTest () {
		for (Player p: board.getPlayers()) {
			assertEquals(p.getHand().size(), 3);
		}
	}

}
