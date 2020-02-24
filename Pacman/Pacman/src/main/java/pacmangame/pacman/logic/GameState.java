package pacmangame.pacman.logic;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;
import pacmangame.pacman.dao.HighScoreDao;

@Data
public class GameState {

    private int points = 0;
    private boolean complete = false;
    private boolean gameOver = false;
    private int activeMonsters = 1;
    private int highScore = 0;
    private int timesScattered = 0;
    private HighScoreDao highScoreDatabase;

    /**
     *
     * @param startingPoints Player's points when starting the current map.
     * @param database Name of the database where the highscore is stored.
     */
    public GameState(int startingPoints, String database) {
        this.highScoreDatabase = new HighScoreDao(database);
        this.points = startingPoints;
        getHighScoreFromDatabase();
    }

    void addScatterTime() {
        this.timesScattered++;
    }

    void addActiveMonster() {
        this.activeMonsters++;
    }

    private void getHighScoreFromDatabase() {
        try {
            this.highScore = highScoreDatabase.getHighScore();
        } catch (SQLException ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves given highscore if it's higher than current highscore.
     *
     * @param score Highscore to be saved.
     */
    public void saveNewHighScoreIfNeeded(int score) {
        if (score > this.highScore) {
            try {
                highScoreDatabase.updateOrSetHighScore(score);
                System.out.println("update ended");
            } catch (SQLException ex) {
                Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void gainPoint(int points) {
        this.points += points;
    }

}
