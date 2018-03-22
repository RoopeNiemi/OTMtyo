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
import pacmangame.pacman.map.Tile;

/**
 *
 * @author nroope
 */
public class GameLogicTest {

    GameLogic logic;
    double redX, redY, orangeX, orangeY, blueX, blueY, yellowX, yellowY, playerX, playerY;

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
        logic = new GameLogic();
        redX = logic.getRed().getX();
        redY = logic.getRed().getY();

        orangeX = logic.getOrange().getX();
        orangeY = logic.getOrange().getY();

        blueX = logic.getBlue().getX();
        blueY = logic.getBlue().getY();

        yellowX = logic.getYellow().getX();
        yellowY = logic.getYellow().getY();

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
        assertTrue(logic.getYellow().getX() != yellowX || logic.getYellow().getY() != yellowY);
    }

    @Test
    public void collisionSetGameOverToTrue() {
        logic.getRed().setX(logic.getPlayer().getX());
        logic.getRed().setY(logic.getPlayer().getY());
        logic.updateMonsters();
        assertTrue(logic.getGameOver());
    }

    @Test
    public void initMapResetsCharacterPositions() {

        logic.getPlayer().setMovementDirection(Direction.DOWN);
        logic.movePlayer();
        logic.movePlayer();

        logic.updateMonsters();
        logic.updateMonsters();

        logic.init();

        assertTrue(logic.getRed().getX() == redX && logic.getRed().getY() == redY);
        assertTrue(logic.getOrange().getX() == orangeX && logic.getOrange().getY() == orangeY);
        assertTrue(logic.getBlue().getX() == blueX && logic.getBlue().getY() == blueY);
        assertTrue(logic.getYellow().getX() == yellowX && logic.getYellow().getY() == yellowY);
        assertTrue(logic.getPlayer().getX() == playerX && logic.getPlayer().getY() == playerY);

    }

    @Test
    public void initMapResetsGameOverState() {
        logic.getRed().setX(logic.getPlayer().getX());
        logic.getRed().setY(logic.getPlayer().getY());
        logic.updateMonsters();
        logic.init();
        assertTrue(!logic.getGameOver());
    }

    @Test
    public void monstersMoveWithAnotherBehaviourState() {

        logic.getRed().changeBehaviour();
        logic.getOrange().changeBehaviour();
        logic.getBlue().changeBehaviour();
        logic.getYellow().changeBehaviour();

        logic.updateMonsters();

        assertTrue(logic.getRed().getX() != redX || logic.getRed().getY() != redY);
        assertTrue(logic.getOrange().getX() != orangeX || logic.getOrange().getY() != orangeY);
        assertTrue(logic.getBlue().getX() != blueX || logic.getBlue().getY() != blueY);
        assertTrue(logic.getYellow().getX() != yellowX || logic.getYellow().getY() != yellowY);

    }

    @Test
    public void transferringPlayerToTheOtherSideOfMapWorks() {
        logic.getPlayer().setX(0);
        logic.getPlayer().setY(180);
        logic.getPlayer().setMovementDirection(Direction.LEFT);
        logic.movePlayer();
        assertTrue(logic.getPlayer().getX() == 359);
        logic.getPlayer().setMovementDirection(Direction.RIGHT);
        logic.movePlayer();
        assertTrue(logic.getPlayer().getX() == 0);
    }
}
