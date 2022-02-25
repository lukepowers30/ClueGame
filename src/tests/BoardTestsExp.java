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
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(0, 1));
		adjSet.add(new TestBoardCell(1, 0));
		
		assertIterableEquals(adjSet, board.getCell(0, 0).getAdjList());
	}
	
	// Tests bottom right corner
	@Test
	void testAdjacency33() {
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(2, 3));
		adjSet.add(new TestBoardCell(3, 2));
		
		assertIterableEquals(adjSet, board.getCell(3, 3).getAdjList());
	}
	
	// Tests a right edge
	@Test
	void testAdjacency13() {
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(1, 2));
		adjSet.add(new TestBoardCell(2, 3));
		adjSet.add(new TestBoardCell(0, 3));
		
		assertIterableEquals(adjSet, board.getCell(1, 3).getAdjList());
	}
	
	// Tests a bottom edge
	@Test
	void testAdjacency32() {
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(3, 1));
		adjSet.add(new TestBoardCell(3, 3));
		adjSet.add(new TestBoardCell(2, 2));
		
		assertIterableEquals(adjSet, board.getCell(3, 2).getAdjList());
	}
	
	// Tests a left edge
	@Test
	void testAdjacency20() {
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(2, 1));
		adjSet.add(new TestBoardCell(1, 0));
		adjSet.add(new TestBoardCell(3, 0));
		
		assertIterableEquals(adjSet, board.getCell(2, 0).getAdjList());
	}
	
	// Tests a middle tile
	@Test
	void testAdjacency21() {
		Set<TestBoardCell> adjSet = new HashSet<TestBoardCell>();
		adjSet.add(new TestBoardCell(1, 1));
		adjSet.add(new TestBoardCell(3, 1));
		adjSet.add(new TestBoardCell(2, 2));
		adjSet.add(new TestBoardCell(2, 0));
		
		assertIterableEquals(adjSet, board.getCell(2, 0).getAdjList());
	}

}
