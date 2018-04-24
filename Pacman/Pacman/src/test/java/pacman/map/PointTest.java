package pacman.map;

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
import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Type;

/**
 *
 * @author User
 */
public class PointTest {

    Point pointOne;
    Point pointTwo;
    Point pointThree;

    public PointTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pointOne = new Point(20, 20, Type.POINT);
        pointTwo = new Point(20, 20, Type.POINT);
        pointThree = new Point(30, 20, Type.POWER_PELLET);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void isCreatedAtRightCoordinates() {
        assertTrue(pointOne.getCentreX() == 20);
        assertTrue(pointOne.getCentreY() == 20);
    }

    @Test
    public void hasRightWidthWhenTypePoint() {
        assertTrue(pointOne.getWidthAndHeight() == 2);
    }

    @Test
    public void hasRightWidthWhenTypeFruit() {
        assertTrue(pointThree.getWidthAndHeight() == 8);
    }

    @Test
    public void pointsAreEqualWhenSameCoordinates() {
        assertTrue(pointOne.equals(pointTwo));
    }

    @Test
    public void pointsAreDifferentWhenDifferentCoordinates() {
        pointThree.setType(Type.POINT);
        assertTrue(!pointOne.equals(pointThree));
    }
}
