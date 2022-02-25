package experiment;

import java.util.*;

public class TestBoard {

	public TestBoard() {
		super();

	}
	
	public void calcTargets( TestBoardCell startCell, int pathlength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		return new HashSet<TestBoardCell>();
	}
	
	public TestBoardCell getCell(int row, int col ) {
		return new TestBoardCell(row, col);
	}

}
