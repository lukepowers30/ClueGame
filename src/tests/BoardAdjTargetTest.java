package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the kitchen that has 4 doors an a secret room
		Set<BoardCell> testList = board.getAdjList(3, 3);
		assertEquals(5, testList.size());
		assertTrue(testList.contains(board.getCell(6, 2)));
		assertTrue(testList.contains(board.getCell(6, 3)));
		assertTrue(testList.contains(board.getCell(6, 4)));
		assertTrue(testList.contains(board.getCell(5, 8)));
		assertTrue(testList.contains(board.getCell(22, 14)));
		
		// now test the Bedroom (note not marked since multiple test here)
		// 3 doors and 1 secret passage
		testList = board.getAdjList(12, 4);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(9, 9)));
		assertTrue(testList.contains(board.getCell(10, 9)));
		assertTrue(testList.contains(board.getCell(11, 9)));
		assertTrue(testList.contains(board.getCell(10, 26)));
		
		// one more room, the Garage
		testList = board.getAdjList(23, 25);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(20, 19)));
		assertTrue(testList.contains(board.getCell(20, 20)));
		assertTrue(testList.contains(board.getCell(2, 24)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(6, 3);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 2)));
		assertTrue(testList.contains(board.getCell(6, 4)));
		assertTrue(testList.contains(board.getCell(7, 3)));
		assertTrue(testList.contains(board.getCell(3, 3)));

		testList = board.getAdjList(17, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(17, 13)));
		assertTrue(testList.contains(board.getCell(17, 15)));
		assertTrue(testList.contains(board.getCell(16, 14)));
		assertTrue(testList.contains(board.getCell(22, 14)));
		
		testList = board.getAdjList(4, 26);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(4, 25)));
		assertTrue(testList.contains(board.getCell(5, 26)));
		assertTrue(testList.contains(board.getCell(2, 24)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(18, 0);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(18, 1)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(6, 6);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 5)));
		assertTrue(testList.contains(board.getCell(6, 7)));
		assertTrue(testList.contains(board.getCell(7, 6)));

		// Test adjacent to walkways
		testList = board.getAdjList(6, 19);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 18)));
		assertTrue(testList.contains(board.getCell(6, 20)));
		assertTrue(testList.contains(board.getCell(7, 19)));
		assertTrue(testList.contains(board.getCell(5, 19)));

		// Test next to closet
		testList = board.getAdjList(9, 15);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(8, 15)));
		assertTrue(testList.contains(board.getCell(9, 14)));
		assertTrue(testList.contains(board.getCell(9, 16)));
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInDiningRoom() {
		// test a roll of 1
		board.calcTargets(board.getCell(3, 13), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(5, 9)));
		assertTrue(targets.contains(board.getCell(6, 9)));	
		assertTrue(targets.contains(board.getCell(7, 13)));
		assertTrue(targets.contains(board.getCell(7, 14)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(3, 13), 3);
		targets= board.getTargets();
		assertEquals(18, targets.size());
		assertTrue(targets.contains(board.getCell(3, 9)));
		assertTrue(targets.contains(board.getCell(4, 8)));	
		assertTrue(targets.contains(board.getCell(5, 8)));
		assertTrue(targets.contains(board.getCell(6, 8)));	
		assertTrue(targets.contains(board.getCell(7, 9)));	
		assertTrue(targets.contains(board.getCell(6, 7)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(3, 13), 4);
		targets= board.getTargets();
		assertEquals(35, targets.size());
		assertTrue(targets.contains(board.getCell(4, 9)));
		assertTrue(targets.contains(board.getCell(8, 8)));	
		assertTrue(targets.contains(board.getCell(7, 17)));
		assertTrue(targets.contains(board.getCell(9, 15)));	
		assertTrue(targets.contains(board.getCell(3, 3)));	
	}
	
	@Test
	public void testTargetsInGarage() {
		// test a roll of 1
		board.calcTargets(board.getCell(23, 25), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(20, 20)));
		assertTrue(targets.contains(board.getCell(20, 19)));	
		assertTrue(targets.contains(board.getCell(2, 24)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(23, 25), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(2, 24)));
		assertTrue(targets.contains(board.getCell(20, 17)));	
		assertTrue(targets.contains(board.getCell(18, 19)));
		assertTrue(targets.contains(board.getCell(20, 22)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(23, 25), 4);
		targets= board.getTargets();
		assertEquals(23, targets.size());
		assertTrue(targets.contains(board.getCell(2, 24)));
		assertTrue(targets.contains(board.getCell(20, 17)));	
		assertTrue(targets.contains(board.getCell(19, 17)));
		assertTrue(targets.contains(board.getCell(17, 19)));	
		assertTrue(targets.contains(board.getCell(20, 23)));
		assertTrue(targets.contains(board.getCell(22, 14)));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(13, 27), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(10, 26)));
		assertTrue(targets.contains(board.getCell(13, 26)));	
		assertTrue(targets.contains(board.getCell(14, 27)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(13, 27), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(10, 26)));
		assertTrue(targets.contains(board.getCell(13, 24)));
		assertTrue(targets.contains(board.getCell(13, 30)));	
		assertTrue(targets.contains(board.getCell(14, 25)));
		assertTrue(targets.contains(board.getCell(14, 27)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(13, 27), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(10, 26)));
		assertTrue(targets.contains(board.getCell(13, 25)));	
		assertTrue(targets.contains(board.getCell(14, 30)));	
		assertTrue(targets.contains(board.getCell(13, 23)));	
		assertTrue(targets.contains(board.getCell(14, 24)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(20, 27), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(20, 26)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(20, 27), 3);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(20, 24)));

		
		// test a roll of 4
		board.calcTargets(board.getCell(20, 27), 4);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(20, 23)));
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(18, 1), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(18, 0)));
		assertTrue(targets.contains(board.getCell(18, 2)));	
		assertTrue(targets.contains(board.getCell(17, 1)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(18, 1), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(17, 1)));	
		assertTrue(targets.contains(board.getCell(18, 2)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(18, 1), 4);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(22, 4)));
		assertTrue(targets.contains(board.getCell(18, 3)));
		assertTrue(targets.contains(board.getCell(18, 5)));	
		assertTrue(targets.contains(board.getCell(17, 4)));
		assertTrue(targets.contains(board.getCell(17, 2)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(18, 3).setOccupied(true);
		board.calcTargets(board.getCell(18, 1), 4);
		board.getCell(18, 3).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(17, 4)));

	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(10, 26).setOccupied(true);
		board.calcTargets(board.getCell(9, 21), 1);
		board.getCell(10, 26).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(10, 26)));	
		assertTrue(targets.contains(board.getCell(8, 21)));	
		assertTrue(targets.contains(board.getCell(10, 21)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(17, 21).setOccupied(true);
		board.calcTargets(board.getCell(17, 26), 2);
		board.getCell(17, 21).setOccupied(false);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(15, 21)));
		assertTrue(targets.contains(board.getCell(16, 20)));	
		assertTrue(targets.contains(board.getCell(13, 25)));

	}
}
