package pacmangame.pacman.logic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameTimer {

    public GameTimer(long threshold) {
        this.threshold = threshold;
    }

    private long threshold = 1000000000;
    private long time = 0;
    private boolean active = false;

    /**
     * Adds time to the timer if the timer is active. If a threshold is reached,
     * sets time to 0 and deactivates the timer.
     *
     * @param time Added to the timer.
     * @return True if a threshold is reached, else false.
     */
    boolean addTime(long time) {
        if (!this.active) {
            return false;
        }
        this.time += time;
        if (this.time >= threshold) {
            this.time = 0;
            setActive(false);
            return true;
        }
        return false;
    }

    /**
     * Sets GameTimer's time to 0.
     */
    void reset() {
        this.time = 0;
    }

}
