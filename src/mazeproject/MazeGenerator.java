package mazeproject;

import java.util.*;

public class MazeGenerator {
	private Random myRandGen;
	private String[][] grid;
	Cell[][] cells;
	private int cellCount;

	/**
	 * Used to choose a random number in order to remove walls
	 * @return
	 */
	public double myrandom() {
		return myRandGen.nextDouble(); // random in 0-1
	}

	/**
	 * Helps in the creation of creating a random maze using a different seed for 
	 * removing walls
	 * @param dimension_in
	 */
	public MazeGenerator(int dimension_in) {

		myRandGen = new java.util.Random(1);

	}

	/**
	 * Generates a maze of mazeSize x mazeSize
	 * @param mazeSize
	 * @return the maze created
	 */
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
	 * Solves a given maze using DFS
	 * 
	 * @param grid the grid that is to be solved
	 * @return the grid that is solved in DFS  with numbers showing order of steps taken
	 */
	public String[][] DFSSolve(String[][] grid) {
		initializeCells(); // initializes Cells for DFS
		String[][] dfsGrid = new String[grid.length][grid.length];
		dfsGrid = grid.clone(); // copy the grid
		Stack<Cell> cellStack = new Stack<Cell>(); // A stack to keep track of visited cells
		Cell[][] dfsCells = new Cell[cells.length][cells.length];
		dfsCells = cells.clone();
		Cell current = dfsCells[0][0];// start from the very first cell of the maze
		dfsGrid[1][1] = "0";
		Integer r = 1; // use r to show the order cells are traversed in
		while (current != dfsCells[dfsCells.length - 1][dfsCells.length - 1]) {
			ArrayList<Cell> openCells = new ArrayList<Cell>();
			for (Cell c : current.neighbors) {
				if (pathBetween(current, c, dfsGrid)) {
					openCells.add(c);// adds openCells to the arrayList
				}
			}

			if (openCells.size() > 1) {
				int j = (int) (myrandom() * openCells.size());// chooses a random cell from those that are open
				Cell next = openCells.get(j);
				dfsGrid[2 * next.y + 1][2 * next.x + 1] = r.toString();// marks the cell on the maze with number to show
																		// order // order
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
	 * Solves the maze using BFS
	 * 
	 * @param grid
	 * @return
	 */
	public String[][] BFSSolve(String[][] grid) {
		initializeBFSCells(); // initializes Cells for BFS
		String[][] bfsGrid = grid.clone();
		Queue<Cell> queue = new LinkedList<>(); // creates a queue to add in
		Cell current = cells[0][0]; // sets the beginning cell of the search
		bfsGrid[1][1] = "0"; // sets the beginning cell in the maze as the start (0th search)
		Integer r = 1; // Keeps track of which cell is discovered in the search
		queue.add(current); // adds the first cell into the queue to be discovered

		// loops through to add cells into the queue that can be discovered if there is
		// a path
		// between them
		while (!queue.isEmpty()) {
			// checks every neighbor in the "current" cell
			for (Cell c : current.neighbors) {
				// checks to see if there is a path the current cell and each of the neighbors
				// if there is a path between the two, add to the queue
				if (pathBFS(current, c, grid)) {
					queue.add(c);
					// checks if the parent is null before adding its making current c's parent
					// so as to not overwrite the parent and create a loop of constantly rewriting
					// the parent;
					if (c.parent == null) {
						c.parent = current;
					}
					// makes sure that the queue doesn't include so it doesn't discover backwards
					if (queue.contains(current.parent)) {
						queue.remove(current.parent);
					}
				}
			}

			// Checks to see if the queue isn't null
			if (queue.peek() != null) {
				queue.remove(); // removes the top of the queue
				Cell next = queue.peek(); // looks at the next in queue
				bfsGrid[2 * next.x + 1][2 * next.y + 1] = r.toString(); // sets the next cell with an integer showing
																		// its discovery time
				r = increment(r); // increments the integer for discovery time
				edit(bfsGrid, current, next, "#"); // adds a "#" in between the cell locations to show the path
				current = next; // sets current as next
			}
			// if current reaches the end of the cells, break out of the loop
			if (current == cells[cells.length - 1][cells.length - 1]) {
				break;
			}

		}

		// Removes the "#" in the BFS maze as to only leave the numbers of discovery in
		for (int i = 0; i < bfsGrid.length; i++) {
			for (int j = 0; j < bfsGrid.length; j++) {
				if (bfsGrid[i][j] == "#") {
					bfsGrid[i][j] = " ";
				}
			}
		}

		return bfsGrid;
	}

	/**
	 * Used in the BFS method in order to check whether or not there is a space
	 * between the current and next cell
	 * 
	 * @param current
	 * @param next
	 * @param grid
	 * @return
	 */
	public boolean pathBFS(Cell current, Cell next, String[][] grid) {
		if (next.x > current.x && grid[2 * next.x][2 * next.y + 1] == " ") {
			// path below of current is true
			return true;
		}
		if (next.x < current.x && grid[2 * next.x + 2][2 * next.y + 1] == " ") {
			// path above of current is true
			return true;
		}
		if (next.y < current.y && grid[2 * next.x + 1][2 * next.y + 2] == " ") {
			// path left of current is true
			return true;
		}
		if (next.y > current.y && grid[2 * next.x + 1][2 * next.y] == " ") {
			// path right of current is true
			return true;
		}
		return false;
	}

	/**
	 * Converts the given solvedMaze to be one with #'s to denote the shortest path
	 * 
	 * @param grid A solved grid
	 * @return A grid that has the shortest path denoted with "#"s
	 */
	public String[][] path(String[][] grid) {
		String[][] pathGrid = new String[grid.length][grid.length];
		pathGrid = grid.clone();
		Cell[][] dfsCells = new Cell[cells.length][cells.length];
		dfsCells = cells.clone();
		Cell current = dfsCells[dfsCells.length - 1][dfsCells.length - 1];// start from the ending cell and work our way
																			// back
		pathGrid[1][1] = "#";
		while (current != dfsCells[0][0]) {// this loop will run until we come back to the start
			pathGrid[2 * current.y + 1][2 * current.x + 1] = "#";// marks current
			editWall(pathGrid, current, current.parent, "#");// marks the path between current and its parent
			current = current.parent;
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				for (Integer k = 0; k < 10; k++) {
					if (k.toString().equals(pathGrid[i][j])) {
						pathGrid[i][j] = " ";
					}
				}
			}
		}
		return pathGrid;

	}

	/**
	 * Used for the BFS method in order to backtrack through the parents of the
	 * cells denoting them with a "#" in order to show the shortest path
	 * 
	 * @param grid
	 * @return
	 */
	public String[][] backTrack(String[][] grid) {
		String[][] pathGrid = grid.clone();
		Cell[][] bfsCells = new Cell[cells.length][cells.length];
		bfsCells = cells.clone();
		Cell current = bfsCells[bfsCells.length - 1][bfsCells.length - 1];// start from the ending cell and work our way
																			// back
		pathGrid[1][1] = "#";
		while (current != bfsCells[0][0]) {// this loop will run until we come back to the start
			pathGrid[2 * current.x + 1][2 * current.y + 1] = "#";// marks current
			edit(pathGrid, current.parent, current, "#");
			current = current.parent;
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				for (Integer k = 0; k < 10; k++) {
					if (k.toString().equals(pathGrid[i][j])) {
						pathGrid[i][j] = " ";
					}
				}
			}
		}
		return pathGrid;
	}

	/**
	 * increments r and resets it back to 0 if it hits then for use in DFSSolve and
	 * BFSSolve
	 * 
	 * @param r the integer that is between 0-9
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
	 * Edits the wall between the current and next cell in the BFS method with
	 * String r
	 * 
	 * @param grid
	 * @param current
	 * @param next
	 * @param r
	 */
	public void edit(String[][] grid, Cell current, Cell next, String r) {
		if (next.x > current.x && grid[2 * next.x][2 * next.y + 1] == " ") {
			// edit bottom wall of current
			grid[2 * next.x][2 * next.y + 1] = r.toString();
		}
		if (next.x < current.x && grid[2 * next.x + 2][2 * next.y + 1] == " ") {
			// edit above wall of current
			grid[2 * next.x + 2][2 * next.y + 1] = r.toString();
		}
		if (next.y < current.y && grid[2 * next.x + 1][2 * next.y + 2] == " ") {
			// edit left wall of current
			grid[2 * next.x + 1][2 * next.y + 2] = r.toString();
		}
		if (next.y > current.y && grid[2 * next.x + 1][2 * next.y] == " ") {
			// edit right wall of current
			grid[2 * next.x + 1][2 * next.y] = r.toString();
		}
	}

	/**
	 * Edits a wall between current and next.
	 * 
	 * @param current The current Cell that has a wall between it and next
	 * @param next    A cell that is an intact neighbor of current.
	 */
	public void editWall(String[][] thisgrid, Cell current, Cell next, String r) {
		if (next.x > current.x) {
			// edit left wall of next
			thisgrid[2 * next.y + 1][2 * next.x] = r.toString();
		}
		if (next.x < current.x) {
			// edit right wall of next
			thisgrid[2 * next.y + 1][2 * next.x + 2] = r.toString();
		}
		if (next.y < current.y) {
			// edit bottom wall of next
			thisgrid[2 * next.y + 2][2 * next.x + 1] = r.toString();
		}
		if (next.y > current.y) {
			// edit top wall of next
			thisgrid[2 * next.y][2 * next.x + 1] = r.toString();
		}
	}

	/**
	 * Checks to see if there is a path between the current and next cell in the creation of the 
	 * maze.
	 * @param current
	 * @param next
	 * @param grid the grid in which to check for a path
	 * @return whether or not the path exists
	 */
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
	 * Finds each neighbor and adds it to their adjacency list but only through a
	 * singly linked list
	 */
	public void findBFSNeighbors() {
		for (int i = 0; i <= cells.length - 1; i++) {

			for (int j = 0; j <= cells.length - 1; j++) {
				if (j != cells.length - 1) {
					cells[i][j].addNeighborBFS(cells[i][j + 1]);
				}
				if (i != cells.length - 1) {
					cells[i][j].addNeighborBFS(cells[i + 1][j]);
				}
				if (i != 0) {
					cells[i][j].addNeighborBFS(cells[i - 1][j]);
				}
				if (j != 0) {
					cells[i][j].addNeighborBFS(cells[i][j - 1]);
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

	/**
	 * Initializes the cells for the BFS method with a singly linked list of
	 * neighbors
	 */
	public void initializeBFSCells() {
		for (int i = 0; i < cells.length; i++) {

			for (int j = 0; j < cells.length; j++) {
				cells[i][j] = new Cell(i, j);

			}
		}
		findBFSNeighbors();
	}

	/**
	 * Cell class to hold a variety of properties: -x and y position -it's parent
	 * -checking for intact walls -adjacency list of neighbors
	 */
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

		public void addNeighborBFS(Cell c) {
			neighbors.add(c);
		}

	}

}
