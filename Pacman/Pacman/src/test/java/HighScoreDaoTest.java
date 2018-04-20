/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacmangame.pacman.dao.HighScoreDao;

/**
 *
 * @author User
 */
public class HighScoreDaoTest {

    HighScoreDao highscores;

    public HighScoreDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        highscores = new HighScoreDao("test.db");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void databaseReturnsCurrentHighscore() throws SQLException {
        int highscore = highscores.getHighScore();
        assertTrue(highscore >= 0);
    }

    @Test
    public void updatingHighScoreWorksProperly() throws SQLException {
        int highscore = highscores.getHighScore();
        int newHighScore = highscore + 1;
        highscores.updateOrSetHighScore(newHighScore);
        highscore = highscores.getHighScore();
        assertTrue(newHighScore == highscore);

    }
}
