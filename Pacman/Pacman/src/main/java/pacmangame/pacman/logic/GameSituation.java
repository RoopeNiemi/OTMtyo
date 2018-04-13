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
public class GameSituation {

    private int points = 0;
    private boolean complete = false;
    private boolean gameOver = false;
    private int activeMonsters = 1;
    private int maxPoints = 0;
    private int highScore = 0;
    private int timesScattered = 0;
    private HighScoreDao highScoreDatabase = new HighScoreDao();

    public GameSituation(int maxPoints, int startingPoints) {
        this.maxPoints = maxPoints;
        this.points = startingPoints;
        setCurrentHighScore();
    }

    public void addScatterTime() {
        this.timesScattered++;
    }

    public int getActiveMonsters() {
        return this.activeMonsters;
    }

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

    public int getCurrentHighScore() {
        return this.highScore;
    }

    public void saveNewHighScoreIfNeeded(int score) {
        if (score > this.highScore) {
            try {
                highScoreDatabase.updateHighScore(score);
                System.out.println("update ended");
            } catch (SQLException ex) {
                Logger.getLogger(GameSituation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getTimesScattered() {
        return this.timesScattered;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    public void gainPoint(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
