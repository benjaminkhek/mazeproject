package mazeproject;

import java.awt.Color;
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
		myRandGen = new java.util.Random(dimension_in);
		Stack<Cell> cellStack = new Stack<Cell>();
		ArrayList<Cell> intactCells = new ArrayList<Cell>();
		int totalCells = cellCount;
		Cell currentCell = cells[0][0];//how does this even work with our testing stuff
		int visitedCells = 1;
		while (visitedCells < totalCells) {
			for (Cell c : currentCell.neighbors) {
				if (c.intactWalls) {
					intactCells.add(c);
				}
				if (intactCells.size() > 0) {
					int j = (int) (myrandom() * intactCells.size());
					intactCells.remove(j);
					cellStack.push(currentCell);
					currentCell = c;
					visitedCells++;
				} else {

					currentCell = cellStack.pop();
				}
			}

		} /**
			 * cellCount = r * r; grid = new char[(r * 2) + 1][(r * 2) + 1]; cells = new
			 * Cell[r][r]; for (int i = 0; i < r; i++) { System.out.println(""); for (int j
			 * = 0; j < r; j++) { System.out.print(j); cells[i][j] = new Cell(i, j); } } for
			 * (int i = 0; i < r; i++) { for (int j = 0; j < r; j++) { if (j < r - 1) {
			 * cells[i][j].addEdge(cells[i][j + 1]);// figure out why this doesnt go thru }
			 * if (i < r - 1) { cells[i][j].addEdge(cells[i + 1][j]); }
			 * 
			 * }
			 * 
			 * } System.out.println(""); for (int l = 0; l < r; l++) { for (int i = 0; i <
			 * r; i++) { System.out.print(cells[l][i].x); System.out.print(cells[l][i].y);
			 * System.out.print(" "); } System.out.println(" "); } for (int l = 0; l < r;
			 * l++) { for (int i = 0; i < r; i++) { System.out.print("+");
			 * System.out.print("-"); } System.out.println("+"); for (int i = 0; i < r; i++)
			 * { System.out.print("|"); System.out.print(" "); } System.out.print("|");
			 * System.out.println(""); } for (int i = 0; i <= r; i++) {
			 * System.out.print("+"); if (i < r) { System.out.print("-"); } }
			 **/

	}
	
	public String[][] maze(int mazeSize) {
		int n = 2 * mazeSize + 1;
		int m = 2 * mazeSize + 1;
		String[][] grid = new String[n][m];
		
		for(int rowIndex = 0; rowIndex < n; rowIndex++) {
			for(int colIndex = 0; colIndex < m; colIndex++) {
				if(rowIndex == 0 && colIndex == 1) {
					grid[rowIndex][colIndex] = "S"; //Start
				}
				else if(rowIndex == 2 * mazeSize - 1 && colIndex == 2 * mazeSize) {
					grid[rowIndex][colIndex] = "E"; //End
				}
				//evens
				else if(rowIndex % 2 == 0) {
					if(colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "+";
					}
					else {
						grid[rowIndex][colIndex] = "|";
					}
				}
				//odds
				else {
					if(colIndex % 2 == 0) {
						grid[rowIndex][colIndex] = "-";
					}
					else {
						grid[rowIndex][colIndex] = "0";
					}
				}
			}
		}
		
		Stack<Cell> cellStack = new Stack<Cell>();
		int totalCells = cellCount;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);
		while (visitedCells < totalCells) {
			int j = (int) (myrandom() * current.neighbors.size() - 1);
			current = path(grid, current, j);
			visitedCells = visitedCells + 1;
			cellStack.push(current);
		}
		return grid;
	}
	
	/**

	public String[][] generate(String[][] grid) {
		Stack<Cell> cellStack = new Stack<Cell>();
		int totalCells = cellCount;
		int visitedCells = 1;
		Cell current = new Cell(0, 0);
		while (visitedCells < totalCells) {
			int j = (int) (myrandom() * current.neighbors.size() -1);
			current = path(grid, current, j);
			visitedCells = visitedCells + 1;
			cellStack.push(current);
		}
		return grid;
	}
	*/

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

		public Cell(int x, int y) {
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
		}

		public Cell getNext() {
			return node;
		}

	}

}
