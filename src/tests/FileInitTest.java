package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTest {

	// Constants to test whether the file was loaded correctly
	public static final int NUM_ROWS = 30;
	public static final int NUM_COLUMNS = 30;

	//Board should not change between tests
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// including the first and last in the file
		assertEquals("Cellar", board.getRoom('C').getName() );
		assertEquals("Forge", board.getRoom('F').getName() );
		assertEquals("Jail", board.getRoom('J').getName() );
		assertEquals("Observatory", board.getRoom('O').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	@Test
	public void FourDoorDirections() {
		//Entrance to Forge
		BoardCell cell = board.getCell(10, 14);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		//Entrance to Jail on edge of board
		cell = board.getCell(6, 0);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		//Entrance to DiningHall
		cell = board.getCell(7, 17);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		//Entrance to Cellar
		cell = board.getCell(1, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(12, 14);
		assertFalse(cell.isDoorway());
		//Test that unused space are not doors
		cell = board.getCell(29, 29);
		assertFalse(cell.isDoorway());
	}

	// Test that the correct number of doors were loaded
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(27, numDoors);
	}
	
	/*
	 * Test various rooms to make sure room cells have correct initial, and
	 * that the rooms have the proper center cell and label cell.
	 */
		@Test
		public void testRooms() {
			// just test a standard room location
			BoardCell cell = board.getCell( 25, 25);
			Room room = board.getRoom( cell ) ;
			assertTrue( room != null );
			assertEquals( room.getName(), "Observatory" ) ;
			assertFalse( cell.isLabel() );
			assertFalse( cell.isRoomCenter() ) ;
			assertFalse( cell.isDoorway()) ;

			// this is a label cell for Jail to test
			cell = board.getCell(2, 2);
			room = board.getRoom( cell ) ;
			assertTrue( room != null );
			assertEquals( room.getName(), "Jail" ) ;
			assertTrue( cell.isLabel() );
			assertTrue( room.getLabelCell() == cell );
			
			// this is a room center cell for Forge to test
			cell = board.getCell(9, 8);
			room = board.getRoom( cell ) ;
			assertTrue( room != null );
			assertEquals( room.getName(), "Forge" ) ;
			assertTrue( cell.isRoomCenter() );
			assertTrue( room.getCenterCell() == cell );
			
			// this is a secret passage test
			cell = board.getCell(20, 19);
			room = board.getRoom( cell ) ;
			assertTrue( room != null );
			assertEquals( room.getName(), "Observatory" ) ;
			assertTrue( cell.getSecretPassage() == 'C' );
			
			// test a walkway
			cell = board.getCell(15,29);
			room = board.getRoom( cell ) ;
			// Note for our purposes, walkways are rooms
			assertTrue( room != null );
			assertEquals( room.getName(), "Walkway" ) ;
			assertFalse( cell.isRoomCenter() );
			assertFalse( cell.isLabel() );
			
			// test an unused space
			cell = board.getCell(29, 0);
			room = board.getRoom( cell ) ;
			assertTrue( room != null );
			assertEquals( room.getName(), "Unused" ) ;
			assertFalse( cell.isRoomCenter() );
			assertFalse( cell.isLabel() );
			
		}
		
		
}
