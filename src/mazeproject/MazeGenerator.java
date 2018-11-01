package mazeproject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
	private Random myRandGen;
	private char[][] grid;
	private int gridlength;
	Cell[][] cells;
	private int cellCount;

	public double myrandom() {
		return myRandGen.nextDouble(); // random in 0-1
	}

	public MazeGenerator(int dimension_in) {
		myRandGen = new java.util.Random(dimension_in);
		/**
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

	public void createPath(int r) {
		gridlength = ((r * 2) + 1);
		cellCount = r * r;
		grid = new char[(r * 2) + 1][(r * 2) + 1];
		cells = new Cell[r][r];
		for (int i = 0; i < r; i++) {
			System.out.println("");
			for (int j = 0; j < r; j++) {
				System.out.print(j);
				cells[i][j] = new Cell(i, j);
			}
		}
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < r; j++) {
				if (j < r - 1) {
					cells[i][j].addEdge(cells[i][j + 1]);// figure out why this doesnt go thru
				}
				if (i < r - 1) {
					cells[i][j].addEdge(cells[i + 1][j]);
				}

			}

		}
		Stack<Cell> cellStack = new Stack<Cell>();
		ArrayList<Cell> intactCells = new ArrayList<Cell>();
		int totalCells = cellCount;
		Cell currentCell = cells[0][0];
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

		}
		System.out.println("");
		for (int l = 0; l < r; l++) {
			System.out.print("|");
			for (int i = 0; i < r; i++) {
				if(i< r-1) {
				System.out.print(cells[l][i].x);
				System.out.print(cells[l][i].y);
				if(cells[l][i].neighbors.contains(cells[l][i+1]))
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
		private int x;
		private int y;
		private String character = " ";
		private int color = WHITE;
		private boolean intactWalls = true;
		public LinkedList<Cell> neighbors = new LinkedList<Cell>();
		public static final int WHITE = 0;
		public static final int GREY = 1;
		public static final int BLACK = 2;

		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void addEdge(Cell c) {
			neighbors.add(c);
			c.neighbors.add(this);
		}
		public void removeEdge(Cell c) {
			neighbors.remove(c);
			c.neighbors.remove(this);
		}

	}

	class Graph {

	}

}
