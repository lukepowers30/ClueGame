package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import experiment.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTestsExp {
	TestBoard board;

	@BeforeEach
	void setUp() throws Exception {
		board = new TestBoard();
	}

	// Tests Top left corner
	@Test
	void testAdjacency00() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> adjList = cell.getAdjList();
		
		assertTrue(adjList.contains(board.getCell(0, 1)));
		assertTrue(adjList.contains(board.getCell(1, 0)));
		assertEquals(2, adjList.size());
	}
	
	// Tests Bottom right corner
	@Test
	void testAdjacency33() {
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> adjList = cell.getAdjList();
		
		assertTrue(adjList.contains(board.getCell(2, 3)));
		assertTrue(adjList.contains(board.getCell(3, 2)));
		assertEquals(2, adjList.size());
	}
	
	// Tests a right edge cell
	@Test
	void testAdjacency13() {
		TestBoardCell cell = board.getCell(1, 3);
		Set<TestBoardCell> adjList = cell.getAdjList();
		
		assertTrue(adjList.contains(board.getCell(2, 3)));
		assertTrue(adjList.contains(board.getCell(0, 3)));
		assertTrue(adjList.contains(board.getCell(1, 2)));
		assertEquals(3, adjList.size());
	}
	
	// Tests a left edge cell
	@Test
	void testAdjacency20() {
		TestBoardCell cell = board.getCell(2, 0);
		Set<TestBoardCell> adjList = cell.getAdjList();
		
		assertTrue(adjList.contains(board.getCell(2, 1)));
		assertTrue(adjList.contains(board.getCell(1, 0)));
		assertTrue(adjList.contains(board.getCell(3, 0)));
		assertEquals(3, adjList.size());
	}
	
	// Tests a central cell
	@Test
	void testAdjacency21() {
		TestBoardCell cell = board.getCell(2, 1);
		Set<TestBoardCell> adjList = cell.getAdjList();
		
		assertTrue(adjList.contains(board.getCell(2, 2)));
		assertTrue(adjList.contains(board.getCell(2, 0)));
		assertTrue(adjList.contains(board.getCell(1, 1)));
		assertTrue(adjList.contains(board.getCell(3, 1)));
		assertEquals(4, adjList.size());
	}
	
	@Test
	void testTargetsNormal() {
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell,  3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		
	}
	
	@Test
	void testTargetsCenter() {
		TestBoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell,  4);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
	}
	
	@Test
	void testTargetsRoom() {
		TestBoardCell cell = board.getCell(0, 0);
		board.getCell(0, 2).setRoom(true);
		board.calcTargets(cell,  2);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		
	}
	
	@Test
	void testTargetsPerson() {
		TestBoardCell cell = board.getCell(0, 0);
		board.getCell(2, 1).setOccupied(true);
		board.calcTargets(cell,  5);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
	@Test
	void testTargetsMixed() {
		TestBoardCell cell = board.getCell(2, 2);
		
		board.getCell(3, 1).setOccupied(true);
		board.getCell(0, 1).setRoom(true);
		board.calcTargets(cell,  6);
		Set<TestBoardCell> targets = board.getTargets();
		for (TestBoardCell c: targets) {
			System.out.println(c.getRow() + " " + c.getCol());
		}
		
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		
	}

}
