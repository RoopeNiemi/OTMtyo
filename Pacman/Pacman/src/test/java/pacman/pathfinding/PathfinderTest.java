package pacman.pathfinding;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.map.Graph;
import pacmangame.pacman.map.Tile;
import pacmangame.pacman.pathfinding.Pathfinder;

/**
 *
 * @author User
 */
public class PathfinderTest {

    Graph map;
    Pathfinder pathfinder;

    public PathfinderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<String> mapDetails = new ArrayList<>();
        mapDetails.add("0110");
        mapDetails.add("1110");
        mapDetails.add("1010");
        mapDetails.add("1000");
        map = new Graph(mapDetails);
        pathfinder = new Pathfinder();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void returnsEmptyStackWhenStartingTileValueIsZero() {
        Tile start = map.getMap()[0][0];
        Tile dest = map.getMap()[0][1];
        Stack<Tile> path = pathfinder.findPath(start, map.getMap(), dest);
        assertTrue(path.isEmpty());
    }

    @Test
    public void returnEmptyStackWhenStartingTileAndDestinationTileAreSame() {
        Tile start = map.getMap()[0][1];
        Tile dest = map.getMap()[0][1];
        Stack<Tile> path = pathfinder.findPath(start, map.getMap(), dest);
        assertTrue(path.isEmpty());
    }

    @Test
    public void pathSizeIsCorrect() {
        Tile start = map.getMap()[3][0];
        Tile dest = map.getMap()[2][2];
        Stack<Tile> path = pathfinder.findPath(start, map.getMap(), dest);
        assertTrue(path.size() == 5);
    }

    @Test
    public void pathDoesNotContainTilesWithZeroValue() {
        Tile start = map.getMap()[0][3];
        Tile dest = map.getMap()[2][2];
        Stack<Tile> path = pathfinder.findPath(start, map.getMap(), dest);
        boolean zeroFound = false;
        while (!path.isEmpty()) {
            if (path.pop().getValue() == 0) {
                zeroFound = true;
            }
        }
        assertTrue(!zeroFound);
    }

}
