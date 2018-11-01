package mazeproject;

import java.util.LinkedList;

public class Cell {
	private boolean intactWalls;
	private LinkedList<Cell> edges;
	public Cell() {
		
	}
	public void addEdge(Cell c)
	{
		edges.add(c);
		c.edges.add(this);
	}
	class Node{
		
	}
}
