package mazeproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MazeGeneratorTest {
	
	@Test
	void test() {
		MazeGenerator mg = new MazeGenerator(0);
		System.out.println("Maze 1:");
		String[][] maze = mg.maze(5);
		String[][] maze2 = new String[maze.length][maze.length];
		for(int i = 0; i < maze.length; i++) {
			maze2[i] = maze[i].clone();
		}
		
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
		for(int i = 0; i < DFSsolvedMaze.length; i++){//Print out a maze solved using DFS
			for(int j = 0; j < DFSsolvedMaze.length; j++) {
				System.out.print(DFSsolvedMaze[i][j]);
				if(j == maze.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] mazepathDFS = mg.path(DFSsolvedMaze);
		for(int i = 0; i < mazepathDFS.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < mazepathDFS.length; j++) {
				System.out.print(mazepathDFS[i][j]);
				if(j == mazepathDFS.length - 1) {
					System.out.println("");
				}
			}
		}
		
		String[][] BFSsolvedMaze = mg.BFSSolve(maze2);
		System.out.println("BFS: ");
		for(int i = 0; i < BFSsolvedMaze.length; i++){//Print out a maze solved using DFS
			for(int j = 0; j < BFSsolvedMaze.length; j++) {
				System.out.print(BFSsolvedMaze[i][j]);
				if(j == maze.length - 1) {
					System.out.println("");
				}
			}
		}
		String[][] mazepathBFS = mg.backTrack(maze2);
		for(int i = 0; i < mazepathBFS.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < mazepathBFS.length; j++) {
				System.out.print(mazepathBFS[i][j]);
				if(j == mazepathBFS.length - 1) {
					System.out.println("");
				}
			}
		}
	}
	

}
