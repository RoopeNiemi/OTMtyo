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
public class GameTimer {

    private long timerThreshold = 1000000000;
    private long time = 0;
    private boolean active = false;

    public boolean addTime(long time) {
        if (!this.active) {
            return false;
        }
        this.time += time;
        if (this.time >= timerThreshold) {
            this.time = 0;
            deactivate();
            System.out.println("Threshold reached");
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return this.active;
    }

    public long getThreshold() {
        return this.timerThreshold;
    }

    public void setThreshold(long newThreshold) {
        this.timerThreshold = newThreshold;
    }

    public void reset() {
        this.time = 0;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

}
