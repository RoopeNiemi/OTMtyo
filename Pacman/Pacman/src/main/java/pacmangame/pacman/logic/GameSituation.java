/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

/**
 *
 * @author User
 */
public class GameSituation {

    private int points = 0;
    private boolean complete = false;
    private boolean gameOver = false;
    private int maxPoints = 0;

    public GameSituation(int maxPoints, int startingPoints) {
        this.maxPoints = maxPoints;
        this.points = startingPoints;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    public void gainPoint() {
        this.points += 10;
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
