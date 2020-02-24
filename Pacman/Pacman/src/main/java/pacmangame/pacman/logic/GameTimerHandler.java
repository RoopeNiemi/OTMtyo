package pacmangame.pacman.logic;

import lombok.Data;

@Data
public class GameTimerHandler {

    private GameTimer panicTimer;
    private GameTimer monsterActivator;
    private GameTimer monsterBehaviourTimer;
    private final static long PANIC_PHASE_LENGTH = 5000000000L;
    private final static long NORMAL_MONSTER_BEHAVIOUR_LENGTH = 20000000000L;
    private final static long SCATTER_BEHAVIOUR_LENGTH = 7000000000L;
    private final static long MONSTER_ACTIVATION_TIME = 4000000000L;

    public GameTimerHandler() {
        initTimers();
    }

    private void initTimers() {
        this.panicTimer = new GameTimer(PANIC_PHASE_LENGTH);
        this.monsterActivator = createActivatedTimerWithThreshold(MONSTER_ACTIVATION_TIME);
        this.monsterBehaviourTimer = createActivatedTimerWithThreshold(NORMAL_MONSTER_BEHAVIOUR_LENGTH);
    }

    private GameTimer createActivatedTimerWithThreshold(Long threshold) {
        GameTimer timer = new GameTimer();
        timer.setThreshold(threshold);
        timer.setActive(true);
        return timer;
    }

    public void setBehaviourThresholdToScatter() {
        this.monsterBehaviourTimer.setThreshold(SCATTER_BEHAVIOUR_LENGTH);
    }

    public void setBehaviourThresholdToNormal() {
        this.monsterBehaviourTimer.setThreshold(NORMAL_MONSTER_BEHAVIOUR_LENGTH);
    }


    public boolean monsterBehaviourTimerThresholdNormal() {
        return this.monsterBehaviourTimer.getThreshold() == NORMAL_MONSTER_BEHAVIOUR_LENGTH;
    }

}
