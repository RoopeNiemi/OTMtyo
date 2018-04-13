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
import pacmangame.pacman.characters.Behaviour;
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
        logic = new GameLogic(new MapLoader(), 0, 2);
        logic.getRed().activate();
        logic.getPink().activate();
        logic.getBlue().activate();
        logic.getOrange().activate();
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
        while (logic.getPlayer().gotHit()) {
            logic.getPlayer().loseHitPoint();
        }
        assertTrue(logic.getPlayer().getRemainingLife() == 1);
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

    @Test
    public void scatteringWorksWhenScatteredLessThanFourTimes() {
        logic.scatterIfPossible(0);
        assertTrue(logic.getRed().getCurrentBehaviour() == Behaviour.SCATTER);
        assertTrue(logic.getPink().getCurrentBehaviour() == Behaviour.SCATTER);
        assertTrue(logic.getBlue().getCurrentBehaviour() == Behaviour.SCATTER);
        assertTrue(logic.getOrange().getCurrentBehaviour() == Behaviour.SCATTER);
    }

    @Test
    public void scatteringDoesNotWorkAfterFourTimes() {
        logic.scatterIfPossible(0);
        logic.scatterIfPossible(0);
        logic.scatterIfPossible(0);
        logic.scatterIfPossible(0);
        logic.setAllMonstersBehaviourState(Behaviour.NORMAL);
        logic.scatterIfPossible(0);
        assertTrue(logic.getRed().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getPink().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getBlue().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getOrange().getCurrentBehaviour() == Behaviour.NORMAL);
    }

    @Test
    public void activatingChaseModeWorks() {
        logic.setAllMonstersBehaviourState(Behaviour.SCATTER);
        logic.activateChaseMode(0);
        assertTrue(logic.getRed().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getPink().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getBlue().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getOrange().getCurrentBehaviour() == Behaviour.NORMAL);
    }

    @Test
    public void resettingMonsterPositionsWorks() {
        logic.updateMonsters();
        logic.updateMonsters();
        logic.resetMonsterStartingPositions();
        assertTrue(logic.getRed().getX() == redX && logic.getRed().getY() == redY);
        assertTrue(logic.getPink().getX() == pinkX && logic.getPink().getY() == pinkY);
        assertTrue(logic.getBlue().getX() == blueX && logic.getBlue().getY() == blueY);
        assertTrue(logic.getOrange().getX() == orangeX && logic.getOrange().getY() == orangeY);
    }

    @Test
    public void endingPanicPhaseWorks() {
        logic.setAllMonstersBehaviourState(Behaviour.PANIC);
        logic.endPanicPhase();
        assertTrue(logic.getRed().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getPink().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getBlue().getCurrentBehaviour() == Behaviour.NORMAL);
        assertTrue(logic.getOrange().getCurrentBehaviour() == Behaviour.NORMAL);
    }

    @Test
    public void activatingMonstersGraduallyWorks() {
        assertTrue(logic.getRed().isActive());
        logic.monsterActivation(4000000000L);
        assertTrue(logic.getPink().isActive());
        logic.monsterActivation(4000000000L);
        assertTrue(logic.getBlue().isActive());
        logic.monsterActivation(4000000000L);
        assertTrue(logic.getOrange().isActive());

    }

}
