package experiment;

import java.util.*;

public class TestBoard {
	
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	
	final static int COLS = 4;
	final static int ROWS = 4;

	public TestBoard() {
		super();
		this.grid = new TestBoardCell[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		calcAdjLists();
		this.targets = new HashSet<TestBoardCell>();
		this.visited = new HashSet<TestBoardCell>();
	}
	
	public void calcAdjLists () {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				
				if ((i-1) >= 0)
				    grid[i][j].addAdj(this.getCell(i-1,j));
				if ((i+1) < ROWS)
				    grid[i][j].addAdj(this.getCell(i+1,j));
				if ((j-1) >= 0)
					grid[i][j].addAdj(this.getCell(i,j-1));
				if ((j+1) < COLS)
					grid[i][j].addAdj(this.getCell(i,j+1));
			}
		}
	}

	public void calcTargets( TestBoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}	
	private void findAllTargets(TestBoardCell thisCell, int stepsLeft) {
		for (TestBoardCell c: thisCell.getAdjList()) {
			if (c.isRoom()) {
				targets.add(c);
				continue;
			}
			if (visited.contains(c) || c.isOccupied()) {
				continue;
			}
			visited.add(c);
			if (stepsLeft == 1) {
				targets.add(c);
			} else {
				findAllTargets(c, stepsLeft-1);
			}
			visited.remove(c);
		}
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col ) {
		return grid[row][col];
	}

}
