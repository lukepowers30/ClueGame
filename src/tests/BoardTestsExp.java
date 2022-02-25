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
		assertEquals(3, adjList.size());
	}
	
	@Test
	void testTargetEmptyBoard () {
		
		
	}

}
