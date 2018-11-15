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
		gridlength = ((dimension_in * 2) + 1);
		cellCount = dimension_in * dimension_in;
		myRandGen = new java.util.Random(0);
		cells = new Cell[dimension_in][dimension_in];

	}
	/**
	public MazeGenerator(int size) {	
		grid = new String[2 * size + 1][2 * size + 1];
	}
	*/
	
	//generates starting maze
	
	public String convert(String[][] grid) {
		String maze = "";
		int size = grid.length;
		
		for(int rowIndex = 0; rowIndex < size; rowIndex++) {
			for(int colIndex = 0; colIndex < size; colIndex++) {
				if(grid[rowIndex][colIndex] == "+") {
					maze = maze + "+";
				}
				else if(grid[rowIndex][colIndex] == "-") {
					maze = maze + "-";
				}
				else if(grid[rowIndex][colIndex] == "|") {
					maze = maze + "|";
				}
				else if(grid[rowIndex][colIndex] == "S" || grid[rowIndex][colIndex] == "E") {
					maze = maze + " ";
				}
				else {
					maze = maze + " " + grid[rowIndex][colIndex] + " ";
				}
				/**
				if(rowIndex == (size - 1) && colIndex != (size - 1)) {
					maze = maze + System.lineSeparator();
				}
				*/
			}
		}
		
		return maze;
		
		
	}
	public String[][] maze(int mazeSize) {
		cellCount = mazeSize*2;
		int n = 2 * mazeSize + 1;
		int m = 2 * mazeSize + 1;
		String[][] grid = new String[n][m];

		for (int rowIndex = 0; rowIndex < n; rowIndex++) {
			for (int colIndex = 0; colIndex < m; colIndex++) {
				if (rowIndex == 0 && colIndex == 1) {
					grid[rowIndex][colIndex] = "S"; // Start
				} else if (rowIndex == 2 * mazeSize && colIndex == 2 * mazeSize - 1) {
					grid[rowIndex][colIndex] = "E"; // End
				}
				// evens rows
				else if (rowIndex % 2 == 0) {
					//even columns
					if (colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "+";
					} else {
						grid[rowIndex][colIndex] = "-";
					}
				}
				// odds rows
				else {
					//even columns
					if (colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "|";
					} 
					else {
						grid[rowIndex][colIndex] = "0";
					}
				}
			}
		}
/**
		Stack<Cell> cellStack = new Stack<Cell>();
		int totalCells = cellCount;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);
		while (visitedCells < totalCells) {
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
			} 
			else {
				current = cellStack.pop(); // pop most recent cell and make it the current cell
			}
		}
		**/
		return grid;
	}

	public void knockWall(Cell current, Cell next) {
		if (next.x > current.x) {
			// knock down left wall of next
			grid[2 * next.x - 1][2 * next.y] = " ";
		}
		if (next.x < current.x) {
			// knock down right wall of next
			grid[2 * next.x + 1][2 * next.y] = " ";
		}
		if (next.y < current.y) {
			// knock down bottom wall of next
			grid[2 * next.x][2 * next.y + 1] = " ";
		}
		if (next.y > current.y) {
			// knock down top wall of next
			grid[2 * next.x][2 * next.y - 1] = " ";
		}
	}

	public void findNeighbors() {
		for (int i = 0; i <= grid.length - 1; i++) {

			for (int j = 0; j <= grid.length - 1; j++) {
				if (j != grid.length - 1) {
					cells[i][j].addEdge(cells[i][j + 1]);
				}
				if (i != grid.length - 1) {
					cells[i][j].addEdge(cells[i + 1][j]);
				}
			}
		}
	}

	/**
	 * Initializes all cells
	 */
	public void initializeCells() {
		for (int i = 0; i <= grid.length - 1; i++) {

			for (int j = 0; j <= grid.length - 1; j++) {
				cells[i][j] = new Cell(i, j);

			}
		}
		findNeighbors();
	}

	/**
	 * 
	 * public String[][] generate(String[][] grid) { Stack<Cell> cellStack = new
	 * Stack<Cell>(); int totalCells = cellCount; int visitedCells = 1; Cell current
	 * = new Cell(0, 0); while (visitedCells < totalCells) { int j = (int)
	 * (myrandom() * current.neighbors.size() -1); current = path(grid, current, j);
	 * visitedCells = visitedCells + 1; cellStack.push(current); } return grid; }
	 */
	public int test() {
		int j = (int) (myrandom() * 2);
		System.out.println(j);
		j = (int) (myrandom() * 4);
		System.out.println(j);
		return j;
	}

	public boolean intact(Cell c) {
		if (grid[c.x][c.y].equals("#")) {
			return false;
		}
		return true;
	}

	public Cell path(String[][] grid, Cell current, int random) {
		grid[1][1] = "#";
		if (random == 1) {// go down and delete top wall from cell below
			current.setNextCell(new Cell(current.x, current.y + 1));
			current = current.getNext();
			grid[2 * (current.y)][2 * (current.x + 1)] = "#";
		} else if (random == 2)// go right and delete left wall from cell to the right
		{
			current.setNextCell(new Cell(current.x + 1, current.y));
			current = current.getNext();
			grid[2 * (current.y + 1)][2 * current.x] = "#";
			grid[2 * (current.y + 1)][2 * (current.x + 1)] = "#";
		} else if (random == 3) {// go up and delete bottom wall from cell above
			current.setNextCell(new Cell(current.x, (current.y - 1)));
			current = current.getNext();
			grid[2 * (current.y + 2)][2 * (current.x + 1)] = "#";
			grid[2 * (current.y + 1)][2 * (current.x + 1)] = "#";
		} else if (random == 4) { // go left and delete right wall from left cell
			current.setNextCell(new Cell(current.x - 1, current.y));
			current = current.getNext();
			grid[2 * (current.y + 1)][2 * (current.x + 2)] = "#";
			grid[2 * (current.y + 1)][2 * (current.x + 1)] = "#";
		}
		return current;

	}

	public void createPath(int r) {
		gridlength = ((r * 2) + 1);
		cellCount = r * r;
		grid = new String[(r * 2) + 1][(r * 2) + 1];
		cells = new Cell[r][r];
		for (int i = 0; i < r; i++) {
			System.out.println("");
			for (int j = 0; j < r; j++) {
				System.out.print(j);
				cells[i][j] = new Cell(i, j);
			}
		}
		/**
		 * for (int i = 0; i < r; i++) { for (int j = 0; j < r; j++) { if (j < r - 1) {
		 * cells[i][j].addEdge(cells[i][j + 1]);// figure out why this doesnt go thru }
		 * if (i < r - 1) { cells[i][j].addEdge(cells[i + 1][j]); }
		 * 
		 * }
		 * 
		 * }
		 **/
		System.out.println("");
		for (int l = 0; l < r; l++) {
			System.out.print("|");
			for (int i = 0; i < r; i++) {
				if (i < r - 1) {
					System.out.print(cells[l][i].x);
					System.out.print(cells[l][i].y);
					if (cells[l][i].neighbors.contains(cells[l][i + 1]))
						System.out.print("|");
				}
			}
			System.out.println(" ");
		}
		for (int l = 0; l < r; l++) {
			for (int i = 0; i < r; i++) {
				System.out.print("+");
				System.out.print("-");
			}
			System.out.println("+");
			for (int i = 0; i < r; i++) {
				System.out.print("|");
				System.out.print(" ");
			}
			System.out.print("|");
			System.out.println("");
		}
		for (int i = 0; i <= r; i++) {
			System.out.print("+");
			if (i < r) {
				System.out.print("-");
			}
		}
	}

	class Cell {
		private Cell node;
		private int x;
		private int y;
		private boolean intactWalls = true;
		public LinkedList<Cell> neighbors;
		public String character = " ";
		public String brokenWall;

		public Cell(int y, int x) {
			this.x = x;
			this.y = y;
			neighbors = new LinkedList<Cell>();
		}

		public void addEdge(Cell c) {
			neighbors.add(c);
			c.neighbors.add(this);
		}

		public void removeEdge(Cell c) {
			neighbors.remove(c);
			c.neighbors.remove(this);
		}

		public void setNextCell(Cell node) {
			this.node = node;
			neighbors.add(node);
		}

		public Cell getNext() {
			return node;
		}

	}

}
