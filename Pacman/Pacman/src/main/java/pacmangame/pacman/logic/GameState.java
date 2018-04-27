/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pacmangame.pacman.dao.HighScoreDao;

/**
 *
 * @author User
 */
public class GameState {

    private int points = 0;
    private boolean complete = false;
    private boolean gameOver = false;
    private int activeMonsters = 1;
    private int maxPoints = 0;
    private int highScore = 0;
    private int timesScattered = 0;
    private HighScoreDao highScoreDatabase;

    /**
     *
     * @param maxPoints Maximum amount of points that can be gained from this
     * map.
     * @param startingPoints Player's points when starting the current map.
     * @param database Name of the database where the highscore is stored.
     */
    public GameState(int maxPoints, int startingPoints, String database) {
        this.highScoreDatabase = new HighScoreDao(database);
        this.maxPoints = maxPoints;
        this.points = startingPoints;
        setCurrentHighScore();
    }

    /**
     * Adds 1 to the timesScattered value.
     */
    public void addScatterTime() {
        this.timesScattered++;
    }

    /**
     *
     * @return number of monsters active in the game.
     */
    public int getActiveMonsters() {
        return this.activeMonsters;
    }

    /**
     * Adds 1 to the number of monsters active.
     */
    public void addActiveMonster() {
        this.activeMonsters++;
    }

    private void setCurrentHighScore() {
        try {
            this.highScore = highScoreDatabase.getHighScore();
        } catch (SQLException ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return current highscore of the game.
     */
    public int getCurrentHighScore() {
        return this.highScore;
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

    /**
     *
     * @return Number of times monsters have been scattered.
     */
    public int getTimesScattered() {
        return this.timesScattered;
    }

    /**
     *
     * @return Number of maximum points available on this map.
     */
    public int getMaxPoints() {
        return this.maxPoints;
    }

    /**
     * Player gains a given amount of points.
     *
     * @param points Points gained.
     */
    public void gainPoint(int points) {
        this.points += points;
    }

    /**
     *
     * @return Player's current points.
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @param points New amount of points.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     *
     * @return True if all points on map have been gained.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     *
     * @param complete new complete value of the game state.
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     *
     * @return True if game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets gameOver value to given value.
     *
     * @param gameOver new gameOver value.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
