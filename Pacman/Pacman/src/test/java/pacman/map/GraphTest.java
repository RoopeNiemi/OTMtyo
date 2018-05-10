package pacman.map;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.map.Graph;

public class GraphTest {

    Graph graph;

    public GraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<String> pointsOnMap = new ArrayList<>();
        pointsOnMap.add("0000");
        pointsOnMap.add("0110");
        pointsOnMap.add("0110");
        graph = new Graph(pointsOnMap);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void graphWidthAndHeightIsCorrect() {
        assertTrue(graph.getGraphMatrix().length == 3);
        assertTrue(graph.getGraphMatrix()[0].length == 4);
    }

    @Test
    public void turningReturnsTrueWhenPossible() {
        assertTrue(graph.checkTurn(20, 40, Direction.UP));
        assertTrue(graph.checkTurn(20, 20, Direction.DOWN));
        assertTrue(graph.checkTurn(40, 20, Direction.LEFT));
        assertTrue(graph.checkTurn(20, 40, Direction.RIGHT));
    }

    @Test
    public void continuingCurrentPathWorksIfNotAtExactCoordinatesOfTile() {
        assertTrue(graph.checkTurn(20, 45, Direction.UP));
        assertTrue(graph.checkTurn(20, 35, Direction.DOWN));
        assertTrue(graph.checkTurn(25, 20, Direction.RIGHT));
        assertTrue(graph.checkTurn(25, 40, Direction.LEFT));

    }

    @Test
    public void turningReturnsFalseWhenNotPossible() {
        assertTrue(graph.checkTurn(0, 40, Direction.UP) == false);
        assertTrue(graph.checkTurn(0, 40, Direction.LEFT) == false);
        assertTrue(graph.checkTurn(40, 40, Direction.RIGHT) == false);
        assertTrue(graph.checkTurn(0, 40, Direction.DOWN) == false);
    }

}
