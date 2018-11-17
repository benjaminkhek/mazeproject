package mazeproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
	private Random myRandGen;
	private String[][] grid;
	Cell[][] cells;
	private int cellCount;

	public double myrandom() {
		return myRandGen.nextDouble(); // random in 0-1
	}

	public MazeGenerator(int dimension_in) {

		myRandGen = new java.util.Random(0);

	}

	public String[][] maze(int mazeSize) {
		cellCount = mazeSize * mazeSize;
		int n = 2 * mazeSize + 1;
		int m = 2 * mazeSize + 1;
		grid = new String[n][m];
		cells = new Cell[mazeSize][mazeSize];

		for (int rowIndex = 0; rowIndex < n; rowIndex++) {
			for (int colIndex = 0; colIndex < m; colIndex++) {
				if (rowIndex == 0 && colIndex == 1) {
					grid[rowIndex][colIndex] = "S"; // Start
				} else if (rowIndex == 2 * mazeSize && colIndex == 2 * mazeSize - 1) {
					grid[rowIndex][colIndex] = "E"; // End
				}
				// evens rows
				else if (rowIndex % 2 == 0) {
					// even columns
					if (colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "+";
					} else {
						grid[rowIndex][colIndex] = "-";
					}
				}
				// odds rows
				else {
					// even columns
					if (colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "|";
					} else {
						grid[rowIndex][colIndex] = " ";
					}
				}
			}
		}
		initializeCells();
		// Create the actual maze using DFS
		Stack<Cell> cellStack = new Stack<Cell>();
		int visitedCells = 1;
		Cell current = cells[0][0];
		grid[1][1] = " ";
		while (visitedCells < cellCount) {
			ArrayList<Cell> intactCells = new ArrayList<Cell>();
			for (Cell c : current.neighbors) {
				if (c.intactWalls) {
					intactCells.add(c);// adds intactCells to the arrayList
				}
			}

			if (intactCells.size() > 1) {
				int j = (int) (myrandom() * intactCells.size());// chooses a random cell from intact neighbors
				Cell toKnock = intactCells.get(j);
				grid[2 * toKnock.y + 1][2 * toKnock.x + 1] = " ";
				editWall(grid, current, toKnock, " ");// knock down wall between current and toKnock
				current.intactWalls = false;
				toKnock.intactWalls = false;
				cellStack.push(current);
				current = toKnock; // current cell is now the one that just had its walls knocked down
				visitedCells++;
			} else {
				current = cellStack.peek(); // pop most recent cell and make it the current cell
				cellStack.pop();
			}
		}

		return grid;
	}

	/**
	 * Edits a wall between current and next.
	 * 
	 * @param current
	 *            The current Cell that has a wall between it and next
	 * @param next
	 *            A cell that is an intact neighbor of current.
	 */
	public void editWall(String[][] grid, Cell current, Cell next, String r) {
		if (next.x > current.x) {
			// edit left wall of next
			grid[2 * next.y + 1][2 * next.x] = r.toString();
		}
		if (next.x < current.x) {
			// edit right wall of next
			grid[2 * next.y + 1][2 * next.x + 2] = r.toString();
		}
		if (next.y < current.y) {
			// edit bottom wall of next
			grid[2 * next.y + 2][2 * next.x + 1] = r.toString();
		}
		if (next.y > current.y) {
			// edit top wall of next
			grid[2 * next.y][2 * next.x + 1] = r.toString();
		}
	}

	public boolean pathBetween(Cell current, Cell next, String[][] grid) {
		if (next.x > current.x && grid[2 * next.y + 1][2 * next.x] == " ") {
			// path to the left of cell one
			return true;
		}
		if (next.x < current.x && grid[2 * next.y + 1][2 * next.x + 2] == " ") {
			// path to the right of cell one
			return true;
		}
		if (next.y < current.y && grid[2 * next.y + 2][2 * next.x + 1] == " ") {
			// knock down bottom wall of next
			return true;
		}
		if (next.y > current.y && grid[2 * next.y][2 * next.x + 1] == " ") {
			// knock down top wall of next
			return true;
		}
		return false;
	}

	/**
	 * Solves a given maze using DFS
	 * 
	 * @param grid
	 * @return
	 */
	public String[][] DFSSolve(String[][] grid) {
		String[][] dfsGrid = grid.clone(); // copy the grid
		Stack<Cell> cellStack = new Stack<Cell>(); // A stack to keep track of visited cells
		Cell current = cells[0][0];// start from the very first cell of the maze
		dfsGrid[1][1] = "0";
		Integer r = 1; // use r to show the order cells are traversed in
		while (current != cells[cells.length - 1][cells.length - 1]) {
			ArrayList<Cell> openCells = new ArrayList<Cell>();
			for (Cell c : current.neighbors) {
				if (pathBetween(current, c, grid)) {
					openCells.add(c);// adds openCells to the arrayList
				}
			}

			if (openCells.size() > 1) {
				int j = (int) (myrandom() * openCells.size());// chooses a random cell from those that are open
				Cell next = openCells.get(j);
				dfsGrid[2 * next.y + 1][2 * next.x + 1] = r.toString();// marks the cell on the maze with number to show
																		// order
				r = increment(r);
				editWall(dfsGrid, current, next, "#");
				cellStack.push(current);
				next.parent = current; // keep track of parent for use in the path method

				current = next; // current cell is now the one that just had its walls knocked down
			} else {
				current = cellStack.peek(); // pop most recent cell and make it the current cell
				cellStack.pop();
			}
		}
		for (int i = 0; i < dfsGrid.length; i++) {
			for (int j = 0; j < dfsGrid.length; j++) {
				if (dfsGrid[i][j] == "#") {
					dfsGrid[i][j] = " ";
				}
			}
		}
		return dfsGrid;
	}

	/**
	 * Converts the given solvedMaze to be one with #'s to denote the shortest path
	 * 
	 * @param grid
	 *            A solved grid
	 * @return A grid that has the shortest path denoted with "#"s
	 */
	public String[][] path(String[][] grid) {
		Cell current = cells[cells.length - 1][cells.length - 1];// start from the ending cell and work our way back
		grid[1][1] = "#";
		while (current != cells[0][0]) {// this loop will run until we come back to the start
			grid[2 * current.y + 1][2 * current.x + 1] = "#";// marks current
			editWall(grid, current, current.parent, "#");// marks the path between current and its parent
			current = current.parent;
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				for (Integer k = 0; k < 10; k++) {
					if (k.toString().equals(grid[i][j])) {
						grid[i][j] = " ";
					}
				}
			}
		}
		return grid;

	}

	/**
	 * increments r and resets it back to 0 if it hits then for use in DFSSolve and
	 * BFSSolve
	 * 
	 * @param r
	 * @return
	 */
	public Integer increment(Integer r) {
		r++;
		if (r > 9) {
			r = 0;
		}
		return r;
	}

	/**
	 * Finds the neighbors of every cell in cells and then adds them to their
	 * adjacency list
	 */
	public void findNeighbors() {
		for (int i = 0; i <= cells.length - 1; i++) {

			for (int j = 0; j <= cells.length - 1; j++) {
				if (j != cells.length - 1) {
					cells[i][j].addNeighbor(cells[i][j + 1]);
				}
				if (i != cells.length - 1) {
					cells[i][j].addNeighbor(cells[i + 1][j]);
				}
				if (i != 0) {
					cells[i][j].addNeighbor(cells[i - 1][j]);
				}
				if (j != 0) {
					cells[i][j].addNeighbor(cells[i][j - 1]);
				}
			}
		}
	}

	/**
	 * Initializes all cells
	 */
	public void initializeCells() {
		for (int i = 0; i < cells.length; i++) {

			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = new Cell(i, j);

			}
		}
		findNeighbors();
	}

	class Cell {
		private int x;
		private int y;
		private Cell parent;
		private boolean intactWalls = true;
		public LinkedList<Cell> neighbors;

		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
			neighbors = new LinkedList<Cell>();
		}

		public void addNeighbor(Cell c) {
			neighbors.add(c);
			c.neighbors.add(this);
		}

	}

}
