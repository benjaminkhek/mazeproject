package mazeproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MazeGeneratorTest {

	@Test
	void test() {
		MazeGenerator mg = new MazeGenerator(5);
		mg.createPath(4);
		MazeGenerator mg2 = new MazeGenerator(8);
	}

}
