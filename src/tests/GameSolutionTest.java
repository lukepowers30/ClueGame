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
import clueGame.ClueGame;
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
		// correct solution test
		assertTrue(board.checkAccusation(correct));
		// Defining wrong cards
		Card room = new Card("Patio", CardType.ROOM);
		Card person = new Card("George", CardType.CHARACTER);
		Card weapon = new Card("Candlestick", CardType.WEAPON);
		Solution wrong = new Solution(room, person, weapon);
		// Sets theAnswer to known values
		board.setTheAnswer(wrong);
		Card room2 = new Card("Kitchen", CardType.ROOM);
		Card person2 = new Card("Bob", CardType.CHARACTER);
		Card weapon2 = new Card("Knife", CardType.WEAPON);
		wrong = new Solution(room2, person2, weapon2);
		// Solution with wrong everything
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room, person2, weapon);
		// Solution with wrong person
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room, person, weapon2);
		// Solution with wrong weapon
		assertFalse(board.checkAccusation(wrong));
		wrong = new Solution(room2, person, weapon);
		// Solution with wrong room
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
		// Player has no matching cards
		assertNull(test.disproveSuggestion(sol1));
		sol1 = new Solution(card1, card5, card6);
		// Player has one matching card
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
		// Player has 2 matching cards. Checks that both cards are selected sometimes
		assertFalse(repeat);
	}
	
	@Test
	void handleSuggestionTest() {
		Player test1 = new HumanPlayer("George", Color.red, 0, 0);
		Player test2 = new ComputerPlayer("Bob", Color.red, 0, 0);
		Player test3 = new ComputerPlayer("Sally", Color.red, 0, 0);
		Card card1 = new Card("Patio", CardType.ROOM);
		Card card2 = new Card("Bob", CardType.CHARACTER);
		Card card3 = new Card("Knife", CardType.WEAPON);
		Card card4 = new Card("Kitchen", CardType.ROOM);
		Card card5 = new Card("George", CardType.CHARACTER);
		Card card6 = new Card("Candlestick", CardType.WEAPON);
		// Adding one card to each player's hand
		test1.updateHand(card1);
		test2.updateHand(card2);
		test3.updateHand(card3);
		ArrayList<Player>  players = new ArrayList<Player>();
		players.add(test1);
		players.add(test2);
		players.add(test3);
		board.setPlayers(players);
		Solution sol1 = new Solution(card4, card5, card6);
		// Suggestion no one can disprove
		Card cardTest1 = board.handleSuggestion(sol1, test1);
		assertNull(cardTest1);
		
		// Suggestion only accusiung player can disprove
		sol1 = new Solution(card1, card5, card6);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertNull(cardTest1);
		// Solution one non accusing player can disprove
		sol1 = new Solution(card1, card2, card6);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertTrue(cardTest1.isEquals(card2));
		
		// Solution all 3 players can disprove
		sol1 = new Solution(card1, card2, card3);
		cardTest1 = board.handleSuggestion(sol1, test1);
		assertTrue(cardTest1.isEquals(card2));
		
		// Solution all 3 players can disprove starting with player 3
		sol1 = new Solution(card1, card2, card3);
		cardTest1 = board.handleSuggestion(sol1, test2);
		assertTrue(cardTest1.isEquals(card3));
	}

}
