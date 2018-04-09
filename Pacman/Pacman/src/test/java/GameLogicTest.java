/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.logic.GameLogic;
import pacmangame.pacman.logic.GameTimer;
import pacmangame.pacman.map.MapLoader;
import pacmangame.pacman.map.Tile;

/**
 *
 * @author nroope
 */
public class GameLogicTest {

    GameLogic logic;
    double redX, redY, orangeX, orangeY, blueX, blueY, pinkX, pinkY, playerX, playerY;
    GameTimer timer;

    public GameLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timer = new GameTimer();
        logic = new GameLogic(new MapLoader(), timer, 0,3);
        redX = logic.getRed().getX();
        redY = logic.getRed().getY();

        orangeX = logic.getOrange().getX();
        orangeY = logic.getOrange().getY();

        blueX = logic.getBlue().getX();
        blueY = logic.getBlue().getY();

        pinkX = logic.getPink().getX();
        pinkY = logic.getPink().getY();

        playerX = logic.getPlayer().getX();
        playerY = logic.getPlayer().getY();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void gameLoadsAMapWhenStarted() {
        Tile[][] map = logic.getGraph().getGraphMatrix();
        assertTrue(map.length > 0 && map[0].length > 0);
    }

    @Test
    public void logicMovesPlayer() {

        logic.getPlayer().setMovementDirection(Direction.DOWN);
        logic.movePlayer();
        assertTrue(logic.getPlayer().getY() != playerY);
    }

    @Test
    public void monstersAreUpdated() {

        logic.updateMonsters();

        assertTrue(logic.getRed().getX() != redX || logic.getRed().getY() != redY);
        assertTrue(logic.getOrange().getX() != orangeX || logic.getOrange().getY() != orangeY);
        assertTrue(logic.getBlue().getX() != blueX || logic.getBlue().getY() != blueY);
        assertTrue(logic.getPink().getX() != pinkX || logic.getPink().getY() != pinkY);
    }

    @Test
    public void collisionReducesPlayerHitPoints() {
        logic.getRed().setX(logic.getPlayer().getX());
        logic.getRed().setY(logic.getPlayer().getY());
        logic.updateMonsters();
        while (logic.getPlayer().getLostHitPoint()) {
            logic.getPlayer().loseHitPoint(timer);
        }
        assertTrue(logic.getPlayer().getRemainingLife() == 2);
    }

    @Test
    public void transferringPlayerToTheOtherSideOfMapWorks() {
        logic.getPlayer().setX(0);
        logic.getPlayer().setY(180);
        logic.getPlayer().setMovementDirection(Direction.LEFT);
        logic.movePlayer();
        assertTrue(logic.getPlayer().getX() == 378);
        logic.getPlayer().setMovementDirection(Direction.RIGHT);
        logic.movePlayer();
        assertTrue(logic.getPlayer().getX() == 0);
    }

    @Test
    public void pointsAreGainedAndRemovedFromMapWhenCollidingWithPlayer() {
        int points = logic.getSituation().getPoints();
        int currentPointsOnMap = logic.getGraph().getPointsList().size();
        logic.getPlayer().setMovementDirection(Direction.DOWN);
        logic.movePlayer();
        logic.movePlayer();
        logic.movePlayer();
        logic.movePlayer();
        assertTrue(currentPointsOnMap > logic.getGraph().getPointsList().size());
        assertTrue(points < logic.getSituation().getPoints());

    }
}
