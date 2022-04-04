package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {
	
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
	void checkAccustationTest() {
		Solution correct = board.getSolution();
		assertTrue(board.checkAccusation(correct));
		Card room = new Card("Patio", CardType.ROOM);
		Card person = new Card("George", CardType.CHARACTER);
		Card weapon = new Card("Candlestick", CardType.WEAPON);
		Solution wrong = new Solution(room, person, weapon);
		board.setTheAnswer(wrong);
		Card room2 = new Card("Kitchen", CardType.ROOM);
		Card person2 = new Card("Bob", CardType.CHARACTER);
		Card weapon2 = new Card("Knife", CardType.WEAPON);
		wrong = new Solution(room2, person2, weapon2);
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room, person2, weapon2);
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room, person, weapon2);
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room2, person, weapon);
		assertFalse(board.checkAccusation(wrong));
	}
	
	
	@Test
	void disproveSuggestionTest() {
		Player test = new ComputerPlayer("test", Color.red, 0, 0);
		Card card1 = new Card("Patio", CardType.ROOM);
		Card card2 = new Card("Bob", CardType.CHARACTER);
		Card card3 = new Card("Knife", CardType.WEAPON);
		Card card4 = new Card("Kitchen", CardType.ROOM);
		Card card5 = new Card("George", CardType.CHARACTER);
		Card card6 = new Card("Candlestick", CardType.WEAPON);
		test.updateHand(card1);
		test.updateHand(card2);
		test.updateHand(card3);
		Solution sol1 = new Solution(card4, card5, card6);
		assertNull(test.disproveSuggestion(sol1));
		sol1 = new Solution(card1, card5, card6);
		assertTrue(test.disproveSuggestion(sol1).isEquals(card1));
		sol1 = new Solution(card4, card2, card3);
		Card card7 = test.disproveSuggestion(sol1);
		boolean repeat = true;
		while(repeat) {
			Card card8 = test.disproveSuggestion(sol1);
			if(card7.isEquals(card8)) {
				card7 = card8;
			}else {
				repeat = false;
			}
		}
		assertFalse(repeat);
	}
	
	@Test
	void handleSuggestionTest() {
		Player test1 = new HumanPlayer("test1", Color.red, 0, 0);
		Player test2 = new ComputerPlayer("test2", Color.red, 0, 0);
		Player test3 = new ComputerPlayer("test3", Color.red, 0, 0);
		Card card1 = new Card("Patio", CardType.ROOM);
		Card card2 = new Card("Bob", CardType.CHARACTER);
		Card card3 = new Card("Knife", CardType.WEAPON);
		Card card4 = new Card("Kitchen", CardType.ROOM);
		Card card5 = new Card("George", CardType.CHARACTER);
		Card card6 = new Card("Candlestick", CardType.WEAPON);
		test1.updateHand(card1);
		test2.updateHand(card2);
		test3.updateHand(card3);
		ArrayList<Player>  players = new ArrayList<Player>();
		players.add(test1);
		players.add(test2);
		players.add(test3);
		board.setPlayers(players);
		Solution sol1 = new Solution(card4, card5, card6);
		Card cardTest1 = board.handleSuggestion(sol1, test1);
		assertNull(cardTest1);
		sol1 = new Solution(card1, card5, card6);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertNull(cardTest1);
		sol1 = new Solution(card1, card2, card6);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertTrue(cardTest1.isEquals(card2));
		sol1 = new Solution(card1, card2, card3);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertTrue(cardTest1.isEquals(card2));
		sol1 = new Solution(card1, card2, card3);
		cardTest1 = board.handleSuggestion(sol1, test2);
		assertTrue(cardTest1.isEquals(card3));
	}

}
