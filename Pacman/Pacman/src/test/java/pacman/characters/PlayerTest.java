package pacman.characters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.characters.Player;

/**
 *
 * @author User
 */
public class PlayerTest {

    Player player;

    public PlayerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        player = new Player(50, 50,3);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void playerCreatedAtGivenCoordinates() {
        assertTrue(player.getX() == 50);
        assertTrue(player.getY() == 50);
    }

    @Test
    public void changingDirectionWorks() {
        player.setMovementDirection(Direction.RIGHT);
        assertTrue(player.getMovementDirection() == Direction.RIGHT);
        player.setMovementDirection(Direction.LEFT);
        assertTrue(player.getMovementDirection() == Direction.LEFT);
        player.setMovementDirection(Direction.DOWN);
        assertTrue(player.getMovementDirection() == Direction.DOWN);
        player.setMovementDirection(Direction.UP);
        assertTrue(player.getMovementDirection() == Direction.UP);
    }

    @Test
    public void changingQueuedDirectionWorks() {
        player.setQueuedDirection(Direction.DOWN);
        assertTrue(player.getQueuedDirection() == Direction.DOWN);
    }

    @Test
    public void playerMovingRightWorks() {
        player.setMovementDirection(Direction.RIGHT);
        double startX = player.getX();
        player.move();
        assertTrue(player.getX() == startX + player.getMovementSpeed());
    }

    @Test
    public void playerMovingLeftWorks() {
        player.setMovementDirection(Direction.LEFT);
        double startX = player.getX();
        player.move();
        assertTrue(player.getX() == startX - player.getMovementSpeed());
    }

    @Test
    public void playerMovingDownWorks() {
        player.setMovementDirection(Direction.DOWN);
        double startY = player.getY();
        player.move();
        assertTrue(player.getY() == startY + player.getMovementSpeed());
    }

    @Test
    public void playerMovingUpWorks() {
        player.setMovementDirection(Direction.UP);
        double startY = player.getY();
        player.move();
        assertTrue(player.getY() == startY - player.getMovementSpeed());
    }

    @Test
    public void playerDoesNotMoveWhenDirectionIsNotMoving() {
        player.setMovementDirection(Direction.NOT_MOVING);
        double startX = player.getX();
        player.move();
        assertTrue(player.getX() == startX);
    }
    
    @Test
    public void playerIsYellowAndGetColorWorks(){
        assertTrue(player.getColor()==Color.YELLOW);
    }
    
    @Test
    public void playerWidthIsCorrect(){
        assertTrue(player.getWidth()==20);
    }

}
