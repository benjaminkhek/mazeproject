package mazeproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MazeGeneratorTest {
	@Test
	void test() {
		/**MazeGenerator mg = new MazeGenerator(4);
		String[][] mazeConverted = mg.maze(4); 
		
		System.out.println(mg.convert(mazeConverted));
		**/
		MazeGenerator mg = new MazeGenerator(4);
		String[][] maze = mg.maze(4);
		for(int i = 0; i < maze.length; i++){//Print out an unfinished maze.
			for(int j = 0; j < maze.length; j++) {
				System.out.print(maze[i][j]);
				if(j == maze.length - 1) {
					System.out.println("");
				}
			}
		}
		
		
		MazeGenerator mg2 = new MazeGenerator(8);
		
	}
	

}
