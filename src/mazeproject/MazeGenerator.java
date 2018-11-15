package mazeproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
	private Random myRandGen;
	private String[][] grid;
	private int gridlength;
	Cell[][] cells;
	private int cellCount;

	public double myrandom() {
		return myRandGen.nextDouble(); // random in 0-1
	}

	public MazeGenerator(int dimension_in) {
		grid = new String[dimension_in][dimension_in];
		gridlength = ((dimension_in * 2));
		myRandGen = new java.util.Random(0);
		cells = new Cell[dimension_in][dimension_in];

	}

	public String[][] maze(int mazeSize) {
		cellCount = mazeSize * mazeSize;
		int n = 2 * mazeSize + 1;
		int m = 2 * mazeSize + 1;
		grid = new String[n][m];

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
				knockWall(current, toKnock);// knock down wall between current and toKnock
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
	 * Knocks down a wall between current and next.
	 * @param current The current Cell that has a wall between it and next
	 * @param next A cell that is an intact neighbor of current. 
	 */
	public void knockWall(Cell current, Cell next) {
		current.intactWalls = false;
		next.intactWalls = false;
		grid[2*next.y + 1][2*next.x + 1] = " ";
		if (next.x > current.x) {
			// knock down left wall of next
			grid[2 * next.y + 1][2 * next.x] = " ";
		}
		if (next.x < current.x) {
			// knock down right wall of next
			grid[2 * next.y + 1][2 * next.x + 2] = " ";
		}
		if (next.y < current.y) {
			// knock down bottom wall of next
			grid[2 * next.y + 2][2 * next.x + 1] = " ";
		}
		if (next.y > current.y) {
			// knock down top wall of next
			grid[2 * next.y][2 * next.x + 1] = " ";
		}
	}

	public void findNeighbors() {
		for (int i = 0; i <= cells.length - 1; i++) {

			for (int j = 0; j <= cells.length - 1; j++) {
				if (j != cells.length - 1) {
					cells[i][j].addNeighbor(cells[i][j + 1]);
				}
				if (i != cells.length - 1) {
					cells[i][j].addNeighbor(cells[i + 1][j]);
				}
				if(i != 0)
				{
					cells[i][j].addNeighbor(cells[i -1][j]);
				}
				if(j!= 0)
				{
					cells[i][j].addNeighbor(cells[i][j-1]);
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
		private boolean intactWalls = true;
		public LinkedList<Cell> neighbors;
		public String brokenWall;

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
