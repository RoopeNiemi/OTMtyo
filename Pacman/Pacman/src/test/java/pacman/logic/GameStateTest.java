/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.logic;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.logic.GameState;

/**
 *
 * @author User
 */
public class GameStateTest {

    GameState situation;

    public GameStateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        situation = new GameState(0, 0, "test.db");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void updatingHighScoreWorks() {
        int previousHighScore = situation.getCurrentHighScore();
        situation.saveNewHighScoreIfNeeded(previousHighScore + 1);
        situation = new GameState(0, 0, "test.db");
        assertTrue(previousHighScore + 1 == situation.getCurrentHighScore());
    }

    @Test
    public void highScoreNotUpdatedWithTooLowValues() {
        int previousHighScore = situation.getCurrentHighScore();
        situation.saveNewHighScoreIfNeeded(previousHighScore - 1);
        situation = new GameState(0, 0, "test.db");
        assertTrue(previousHighScore == situation.getCurrentHighScore());
    }

}
