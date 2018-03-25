/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayDeque;
import java.util.Stack;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
        monster = new Monster(40, 40, 2.5, Color.CORAL, 2,false);
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
    public void monsterCreatedWithGivenParameters() {
        assertTrue(monster.getX() == 40);
        assertTrue(monster.getY() == 40);
        assertTrue(monster.getColor() == Color.CORAL);
        assertTrue(monster.getBehaviourChangeThreshold() == 2);

    }

    @Test
    public void changeBehaviourWorks() {
        assertTrue(monster.getBehaviourState() == false);
        monster.changeBehaviour();
        assertTrue(monster.getBehaviourState() == true);
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
        assertTrue(monster.getX() == 42.5);
    }

    @Test
    public void movingDownWorks() {
        monster.getNextPath().pollFirst();
        monster.move();
        assertTrue(monster.getY() == 42.5);
    }

    @Test
    public void movingLeftWorks() {
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();

        monster.move();
        assertTrue(monster.getX() == 37.5);
    }

    @Test
    public void movingUpWorks() {
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();
        monster.getNextPath().pollFirst();
        monster.move();
        assertTrue(monster.getY() == 37.5);
    }

    @Test
    public void getsNextTileFromQueueWhenAtCurrentTilesCoordinates() {
        monster.getNextPath().clear();
        monster.getNextPath().addLast(new Tile(60, 40, 20, 1));
        monster.getNextPath().addLast(new Tile(60, 20, 20, 1));
        monster.setX(57.5);
        monster.move();
        monster.move();
        assertTrue(monster.getX()==60 && monster.getY()==37.5);

    }

}
