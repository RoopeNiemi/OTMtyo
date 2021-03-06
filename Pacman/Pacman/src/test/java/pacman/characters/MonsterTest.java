package pacman.characters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayDeque;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.characters.Behaviour;
import pacmangame.pacman.characters.Monster;
import pacmangame.pacman.map.Tile;

/**
 *
 * @author User
 */
public class MonsterTest {

    Monster monster;
    ArrayDeque<Tile> givenPath;

    public MonsterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        monster = new Monster(new Tile(40, 40, 20, 1), 2, 2, "red");
        monster.activate();
        givenPath = new ArrayDeque<>();
        givenPath.addLast(new Tile(60, 40, 20, 1));
        givenPath.addLast(new Tile(40, 60, 20, 1));
        givenPath.addLast(new Tile(20, 40, 20, 1));
        givenPath.addLast(new Tile(40, 20, 20, 1));
        monster.setNextPath(givenPath);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void doesNotMoveIfNextTileIsNullAndNextPathIsEmpty() {
        monster.setNextPath(new ArrayDeque<Tile>());
        monster.move();
        assertTrue(monster.getX() == 40 && monster.getY() == 40);
    }

    @Test
    public void movingRightWorks() {
        monster.move();
        assertTrue(monster.getX() == 42.0);
    }

    @Test
    public void movingDownWorks() {
        monster.getNextPath().pollFirst();
        monster.move();
        assertTrue(monster.getY() == 42.0);
    }

    @Test
    public void movingLeftWorks() {
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();

        monster.move();
        assertTrue(monster.getX() == 38.0);
    }

    @Test
    public void movingUpWorks() {
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();
        monster.move();
        assertTrue(monster.getY() == 38.0);
    }

    @Test
    public void getsNextTileFromQueueWhenAtCurrentTilesCoordinates() {
        monster.getNextPath().clear();
        monster.getNextPath().addLast(new Tile(60, 40, 20, 1));
        monster.getNextPath().addLast(new Tile(60, 20, 20, 1));
        monster.setX(58.0);
        monster.move();
        monster.move();
        assertTrue(monster.getX() == 60 && monster.getY() == 38.0);
    }

    @Test
    public void whenAtStartingTileBehaviourResetChangesToNormal() {
        monster.setCurrentBehaviour(Behaviour.RESET);
        monster.setNextTile(monster.getNextPath().pollFirst());
        monster.checkPosition();
        assertTrue(monster.getCurrentBehaviour() == Behaviour.NORMAL);
    }

    @Test
    public void monsterTransitionFromLeftSideToRightSideOfMapWorks() {
        monster.setX(-16);
        monster.setY(180);
        monster.setNextTile(new Tile(360, 180, 20, 1));
        monster.move();
        assertTrue(monster.getX() == 376);
    }

    @Test
    public void monsterTransitionFromRightSideToLeftSideOfMapWorks() {
        monster.setX(377);
        monster.setY(180);
        monster.setNextTile(new Tile(0, 180, 20, 1));
        monster.move();
        assertTrue(monster.getX() == 0);
    }

}
