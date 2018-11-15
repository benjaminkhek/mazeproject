package mazeproject;

public class MazeTester {
	public static void main(String[] arg) {
		MazeGenerator mg = new MazeGenerator(4);
		String[][] mazeConverted = mg.maze(4); 
		
		System.out.println(mg.convert(mazeConverted));
	}
}
