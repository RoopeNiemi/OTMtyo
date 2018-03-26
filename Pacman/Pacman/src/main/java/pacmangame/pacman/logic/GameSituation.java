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
    private boolean firstThresholdReached, secondThresholdReached, thirdThresholdReached, fourthThresholdReached;

    public GameSituation(int maxPoints) {
        this.maxPoints = maxPoints;
        this.firstThresholdReached = false;
        this.secondThresholdReached = false;
        this.thirdThresholdReached = false;
        this.fourthThresholdReached = false;
    }

    public boolean getNextThreshold() {
        if (!this.firstThresholdReached && this.points >= firstScatterThreshold()) {
            this.firstThresholdReached = true;
            return true;
        }
        if (!this.secondThresholdReached && this.points >= secondScatterThreshold()) {
            this.secondThresholdReached = true;
            return true;
        }
        if (!this.thirdThresholdReached && this.points >= thirdScatterThreshold()) {
            this.thirdThresholdReached = true;
            return true;
        }
        if (!this.fourthThresholdReached && this.points >= fourthScatterThreshold()) {
            this.fourthThresholdReached = true;
            return true;
        }
        return false;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    private int firstScatterThreshold() {
        return this.maxPoints / 5;
    }

    private int secondScatterThreshold() {
        return this.maxPoints / 5 * 2;
    }

    private int thirdScatterThreshold() {
        return this.maxPoints / 5 * 3;
    }

    private int fourthScatterThreshold() {
        return this.maxPoints / 5 * 4;
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
