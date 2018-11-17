package mazeproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MazeGeneratorTest {
	@Test
	void test() {
		MazeGenerator mg = new MazeGenerator(0);
		System.out.println("Maze 1:");
		String[][] maze = mg.maze(4);
		for(int i = 0; i < maze.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze.length; j++) {
				System.out.print(maze[i][j]);
				if(j == maze.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] DFSsolvedMaze = mg.DFSSolve(maze);
		System.out.println("DFS: ");
		for(int i = 0; i < maze.length; i++){//Print out a maze solved using DFS
			for(int j = 0; j < maze.length; j++) {
				System.out.print(maze[i][j]);
				if(j == maze.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] mazepath = mg.path(DFSsolvedMaze);
		for(int i = 0; i < DFSsolvedMaze.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < DFSsolvedMaze.length; j++) {
				System.out.print(DFSsolvedMaze[i][j]);
				if(j == DFSsolvedMaze.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][]maze2 = mg.maze(5);
		System.out.println("Maze 2:");
		for(int i = 0; i < maze2.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze2.length; j++) {
				System.out.print(maze2[i][j]);
				if(j == maze2.length - 1) {
					System.out.println("");
				}
			}
		}
		
		String[][] DFSsolvedMaze2 = mg.DFSSolve(maze2);
		System.out.println("DFSSolve:");
		for(int i = 0; i < maze2.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze2.length; j++) {
				System.out.print(maze2[i][j]);
				if(j == maze2.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] mazepath2 = mg.path(DFSsolvedMaze2);
		for(int i = 0; i < DFSsolvedMaze2.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < DFSsolvedMaze2.length; j++) {
				System.out.print(DFSsolvedMaze2[i][j]);
				if(j == DFSsolvedMaze2.length - 1) {
					System.out.println("");
				}
			}
		}
		
	
		
	}
	@Test
	void test2() {
		MazeGenerator mg = new MazeGenerator(0);
		System.out.println("Check if mazes of size 4 are the same:");
		String[][] maze1 = mg.maze(4);
		for(int i = 0; i < maze1.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze1.length; j++) {
				System.out.print(maze1[i][j]);
				if(j == maze1.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] maze2 = mg.maze(4);
		for(int i = 0; i < maze2.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze2.length; j++) {
				System.out.print(maze2[i][j]);
				if(j == maze2.length - 1) {
					System.out.println("");
				}
			}
		}
		assertEquals(maze1, maze2);
	}
	

}
