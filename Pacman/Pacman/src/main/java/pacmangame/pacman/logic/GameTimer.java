package pacmangame.pacman.logic;

public class GameTimer {

    private long timerThreshold = 1000000000;
    private long time = 0;
    private boolean active = false;

    /**
     * Adds time to the timer if the timer is active. If a threshold is reached,
     * sets time to 0 and deactivates the timer.
     *
     * @param time Added to the timer.
     * @return True if a threshold is reached, else false.
     */
    public boolean addTime(long time) {
        if (!this.active) {
            return false;
        }
        this.time += time;
        if (this.time >= timerThreshold) {
            this.time = 0;
            deactivate();
            return true;
        }
        return false;
    }

    /**
     *
     * @return True if timer is active, else false.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     *
     * @return GameTimer's current threshold.
     */
    public long getThreshold() {
        return this.timerThreshold;
    }

    /**
     *
     * @param newThreshold New threshold of the GameTimer
     */
    public void setThreshold(long newThreshold) {
        this.timerThreshold = newThreshold;
    }

    /**
     * Sets GameTimer's time to 0.
     */
    public void reset() {
        this.time = 0;
    }

    /**
     * Activates the GameTimer.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the GameTimer.
     */
    public void deactivate() {
        this.active = false;
    }

}
