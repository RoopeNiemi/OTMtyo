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
import pacmangame.pacman.map.Tile;

/**
 *
 * @author User
 */
public class TileTest {

    Tile tile;

    public TileTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        tile = new Tile(50, 50, 20, 1);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void tileIsCreatedAtGivenPoint() {
        assertTrue(tile.getX() == 50);
        assertTrue(tile.getY() == 50);
    }

    @Test
    public void tileWidthIsGivenWidth(){
        assertTrue(tile.getWidth()==20);
    }
    
    @Test
    public void tileIsCreatedWithGivenValue(){
        assertTrue(tile.getValue()==1);
    }
    
    @Test
    public void tilesPathFromIsNullInBeginning() {
        assertTrue(tile.getPathFrom() == null);
    }

    @Test
    public void settingTilePathFromWorks() {
        Tile secondTile = new Tile(100, 100, 20, 1);
        tile.setPathFrom(secondTile);
        assertTrue(tile.getPathFrom()==secondTile);
    }

    @Test
    public void tilesWithSameCoordinatesAreEqual() {
        Tile secondTile = new Tile(50, 50, 20, 1);
        assertTrue(secondTile.equals(tile));
    }
    
    @Test
    public void tilesWithDifferentCoordinatesAreNotEqual(){
         Tile secondTile = new Tile(100, 100, 20, 1);
         assertTrue(!secondTile.equals(tile));
    }
}
