/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Type;
import java.util.*;
import java.util.stream.Collectors;
import pacmangame.pacman.characters.*;
import pacmangame.pacman.map.*;
import pacmangame.pacman.pathfinding.Pathfinder;

/**
 *
 * @author User
 */
public class GameLogic {

    private Player player;
    private Monster red;
    private Monster pink;
    private Monster blue;
    private Monster orange;
    private Pathfinder pathfinder = new Pathfinder();
    private boolean gameOver = false;
    private Graph currentMap;
    private GameTimer timer;
    private GameState gameState;
    private GameTimer monsterBehaviourTimer;
    private GameTimer monsterActivator;
    private int highscore = 0;
    private long panicPhaseLength = 5000000000L;
    private long normalMonsterBehaviourLength = 20000000000L;
    private long scatterBehaviourLength = 7000000000L;

    public GameLogic(MapLoader mapLoader, int startingPoints, int playerLives) {
        this.player = new Player(180, 300, playerLives);
        this.currentMap = new Graph(mapLoader.loadMap());
        this.gameState = new GameState(currentMap.getPointsList().size() * 10, startingPoints, "highscore.db");
        initMonsters();
        initTimers();
    }

    private void initTimers() {
        this.timer = new GameTimer();
        this.monsterActivator = new GameTimer();
        this.monsterActivator.setThreshold(4000000000L);
        this.monsterActivator.activate();
        this.monsterBehaviourTimer = new GameTimer();
        this.monsterBehaviourTimer.setThreshold(normalMonsterBehaviourLength);
        this.monsterBehaviourTimer.activate();
    }

    private void initMonsters() {
        this.red = new Monster(currentMap.getRedStartingTile(), 2, 5, "red");
        this.pink = new Monster(currentMap.getPinkStartingTile(), 2, 7, "pink");
        this.blue = new Monster(currentMap.getBlueStartingTile(), 2, 5, "blue");
        this.orange = new Monster(currentMap.getOrangeStartingTile(), 2, 15, "orange");
    }

    public boolean situationNormal() {
        return !this.gameState.isGameOver() && !this.player.gotHit() && !this.gameState.isComplete();
    }

    public GameTimer getMonsterActivator() {
        return this.monsterActivator;
    }

    public void resetMonsterStartingPositions() {
        resetMonsterPosition(this.red);
        resetMonsterPosition(this.pink);
        resetMonsterPosition(this.blue);
        resetMonsterPosition(this.orange);
    }

    public void resetMonsterPosition(Monster monster) {
        monster.setX(monster.getStartingTile().getX());
        monster.setY(monster.getStartingTile().getY());
        monster.getNextPath().clear();
        monster.setNextTile(null);
    }

    public void handleBehaviourUpdate(long updateFrequency) {
        if (monsterBehaviourThresholdReached(updateFrequency)) {
            if (getMonsterBehaviourTimer().getThreshold() == normalMonsterBehaviourLength) {
                scatterIfPossible(scatterBehaviourLength);
            } else {
                activateChaseMode(normalMonsterBehaviourLength);
            }
        }
        if (timerThresholdReached(updateFrequency)) {
            endPanicPhase();
        }
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public boolean monsterBehaviourThresholdReached(long addedTime) {
        return this.monsterBehaviourTimer.addTime(addedTime);
    }

    public boolean timerThresholdReached(long addedTime) {
        return this.timer.addTime(addedTime);
    }

    private void findMonsterPath(Monster monster, Tile monsterTile, Tile destinationTile) {
        Stack<Tile> path = pathfinder.findPath(monsterTile, this.currentMap.getGraphMatrix(), destinationTile);
        if (path.isEmpty() || path.peek() == null || path.size() == 1 && path.peek() == monsterTile) {
            path = pathfinder.findPath(monsterTile, this.currentMap.getGraphMatrix(), getRandomDestinationTile(monster));
        }
        while (!path.isEmpty() && monster.getNextPath().size() < monster.getPathSize()) {
            monster.getNextPath().addLast(path.pop());
        }
    }

    public void scatterIfPossible(long scatterBehaviourLength) {
        if (getGameState().getTimesScattered() < 4) {
            getMonsterBehaviourTimer().setThreshold(scatterBehaviourLength);
            getGameState().addScatterTime();
            System.out.println("SCATTER ACTIVATED");
            setAllMonstersBehaviourState(Behaviour.SCATTER);
            getMonsterBehaviourTimer().reset();
            getMonsterBehaviourTimer().activate();
        }
    }

    public void activateChaseMode(long chaseModeLength) {
        getMonsterBehaviourTimer().setThreshold(chaseModeLength);
        System.out.println("SCATTER DEACTIVATED");
        getMonsterBehaviourTimer().reset();
        setAllMonstersBehaviourState(Behaviour.NORMAL);
        getMonsterBehaviourTimer().activate();
    }

    public void endPanicPhase() {
        setAllMonstersBehaviourState(Behaviour.NORMAL);
        getMonsterBehaviourTimer().activate();
    }

    public void movePlayer() {
        this.player.move();
        checkPointSituation();
        if (player.getX() <= 0 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY()) {
            player.setX(this.currentMap.getGraphMatrix()[9][18].getX() + 18);
            return;
        }
        if (player.getX() >= this.currentMap.getGraphMatrix()[9][18].getX() + 18 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY() && player.getMovementDirection() == Direction.RIGHT) {
            player.setX(this.currentMap.getGraphMatrix()[9][0].getX());
            return;
        }
        if (this.currentMap.checkTurn(player.getX(), player.getY(), player.getQueuedDirection())) {
            player.setMovementDirection(player.getQueuedDirection());
            player.setQueuedDirection(Direction.NOT_MOVING);
        }
        if (!this.currentMap.checkTurn(player.getX(), player.getY(), player.getMovementDirection())) {
            player.setMovementDirection(Direction.NOT_MOVING);
        }
    }

    public GameTimer getTimer() {
        return this.timer;
    }

    public GameTimer getMonsterBehaviourTimer() {
        return this.monsterBehaviourTimer;
    }

    public void monsterActivation(long addedTime) {
        if (this.monsterActivator.isActive()) {
            if (this.monsterActivator.addTime(addedTime)) {
                switch (this.gameState.getActiveMonsters()) {
                    case 1:
                        this.pink.activate();
                        this.monsterActivator.activate();
                        this.gameState.addActiveMonster();
                        break;
                    case 2:
                        this.blue.activate();
                        this.monsterActivator.activate();
                        this.gameState.addActiveMonster();
                        break;
                    case 3:
                        this.orange.activate();
                        this.gameState.addActiveMonster();
                        break;
                }
            }
        }
    }

    private void checkPointSituation() {
        if (player.getCentreX() <= 379) {
            Tile playerTile = getTile(player.getCentreX(), player.getCentreY());
            Stack<Point> deletedPoints = new Stack();
            for (Point currentPoint : playerTile.getTilesPoints()) {
                if (playerCollidesWithPoint(currentPoint)) {
                    deletedPoints.push(currentPoint);
                    if (currentPoint.getType() == Type.POWER_PELLET) {
                        //Monsters panic, slow down
                        setAllMonstersBehaviourState(Behaviour.PANIC);
                        monsterBehaviourTimer.deactivate();
                        this.timer.setThreshold(panicPhaseLength);
                        this.timer.activate();
                    }
                }
            }
            while (!deletedPoints.isEmpty()) {
                Point p = deletedPoints.pop();
                playerTile.getTilesPoints().remove(p);
                this.gameState.gainPoint(10);
                this.currentMap.getPointsList().remove(p);
            }
        }
        if (currentMap.getPointsList().isEmpty()) {
            this.gameState.setComplete(true);
        }
    }

    public void setAllMonstersBehaviourState(Behaviour newBehaviour) {
        this.red.setCurrentBehaviour(newBehaviour);
        this.orange.setCurrentBehaviour(newBehaviour);
        this.blue.setCurrentBehaviour(newBehaviour);
        this.pink.setCurrentBehaviour(newBehaviour);
    }

    private boolean playerCollidesWithPoint(Point point) {
        return (Math.abs(player.getCentreX() - point.getCentreX()) <= 5 && Math.abs(player.getCentreY() - point.getCentreY()) <= 5);
    }

    public void updateMonsters() {
        updateMonster(this.red, getTile(player.getX(), player.getY()), this.currentMap.getTopRightTile());
        updateMonster(this.orange, orangeMovement(), this.currentMap.getBottomRightTile());
        updateMonster(this.blue, blueMovement(), this.currentMap.getTopLeftTile());
        updateMonster(this.pink, tileInFrontOfPlayer(), this.currentMap.getBottomLeftTile());
    }

    private Tile orangeMovement() {
        if (distanceFromPlayer() > 4) {
            return getTile(player.getX(), player.getY());
        } else {
            return this.currentMap.getBottomRightTile();
        }
    }

    private Tile blueMovement() {
        Tile inFrontOfPlayer = tileInFrontOfPlayer();
        Tile redTile = getTile(this.red.getX(), this.red.getY());
        double xDifference = inFrontOfPlayer.getX() - redTile.getX();
        double yDifference = inFrontOfPlayer.getY() - redTile.getY();
        double destinationTileX = inFrontOfPlayer.getX() + xDifference;
        double destinationTileY = inFrontOfPlayer.getY() + yDifference;
        destinationTileX = checkInBounds(destinationTileX, 340);
        destinationTileY = checkInBounds(destinationTileY, 380);
        Tile blueDestinationTile = getTile(destinationTileX, destinationTileY);
        return blueDestinationTile;
    }

    private double checkInBounds(double value, double max) {
        if (value > max) {
            value = max;
        }
        if (value < 20) {
            value = 20;
        }
        return value;
    }

    private int distanceFromPlayer() {
        int distanceX = Math.abs((int) Math.floor(this.orange.getX() / 20) - (int) Math.floor(this.player.getX() / 20));
        int distanceY = Math.abs((int) Math.floor(this.orange.getY() / 20) - (int) Math.floor(this.player.getY() / 20));
        return distanceX + distanceY;
    }

    private Tile tileInFrontOfPlayer() {
        Direction dir = this.player.getMovementDirection();
        int playerTileX = (int) Math.floor(getTile(player.getX(), player.getY()).getX() / 20);
        int playerTileY = (int) Math.floor(getTile(player.getX(), player.getY()).getY() / 20);

        switch (dir) {
            case UP:
                if (playerTileY >= 2) {
                    return this.currentMap.getGraphMatrix()[playerTileY - 2][playerTileX];
                } else {
                    return this.currentMap.getGraphMatrix()[playerTileY + 4][playerTileX];
                }
            case DOWN:
                if (playerTileY <= this.currentMap.getGraphMatrix().length - 3) {
                    return this.currentMap.getGraphMatrix()[playerTileY + 2][playerTileX];
                } else {
                    return this.currentMap.getGraphMatrix()[playerTileY - 4][playerTileX];
                }
            case RIGHT:
                if (playerTileX <= this.currentMap.getGraphMatrix()[0].length - 3) {
                    return this.currentMap.getGraphMatrix()[playerTileY][playerTileX + 2];
                } else {
                    return this.currentMap.getGraphMatrix()[playerTileY][playerTileX - 4];
                }
            case LEFT:
                if (playerTileX >= 2) {
                    return this.currentMap.getGraphMatrix()[playerTileY][playerTileX - 2];
                } else {
                    return this.currentMap.getGraphMatrix()[playerTileY][playerTileX + 4];
                }
            default:
                return getTile(player.getX(), player.getY());
        }
    }

    private Tile getRandomDestinationTile(Monster monster) {
        Tile monsterTile = getTile(monster.getX(), monster.getY());
        List<Tile> tiles = getGraph().getMovableTiles().stream().filter(a -> a.getX() != monsterTile.getX() && a.getY() != monsterTile.getY()).collect(Collectors.toCollection(ArrayList::new));
        return tiles.get(new Random().nextInt(tiles.size()));
    }

    private void updateMonster(Monster monster, Tile normalDestination, Tile scatterDestination) {
        if (this.gameState.isGameOver() || !monster.isActive()) {
            return;
        }
        if (monster.getNextTile() == null && monster.getNextPath().isEmpty()) {
            if (null == monster.getCurrentBehaviour()) {
                findMonsterPath(monster, getTile(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
            } else {
                switch (monster.getCurrentBehaviour()) {
                    case NORMAL:
                        findMonsterPath(monster, getTile(monster.getX(), monster.getY()), normalDestination);
                        break;
                    case SCATTER:
                        findMonsterPath(monster, getTile(monster.getX(), monster.getY()), scatterDestination);
                        break;
                    case RESET:
                        findMonsterPath(monster, getTile(monster.getX(), monster.getY()), monster.getStartingTile());
                        break;
                    default:
                        findMonsterPath(monster, getTile(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
                        break;
                }
            }
        }
        monster.move();
        checkCollision(monster);
    }

    private void checkCollision(Monster monster) {
        if (monster.getCurrentBehaviour() == Behaviour.RESET) {
            return;
        }

        if (Math.abs(this.player.getCentreX() - monster.getCentreX()) <= 10 && Math.abs(this.player.getCentreY() - monster.getCentreY()) <= 10) {
            if (monster.getCurrentBehaviour() == Behaviour.PANIC) {
                monster.setCurrentBehaviour(Behaviour.RESET);
                monster.getNextPath().clear();
                monster.setMovementSpeed(4);
                this.gameState.gainPoint(100);
                return;
            }
            findMonsterPath(monster, getTile(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
            this.player.loseHitPoint();
            if (this.player.getRemainingLife() <= 0) {
                this.gameState.setGameOver(true);
            }

        }
    }

    private Tile getTile(double x, double y) {
        int xK = (int) Math.floor(x / 20);
        if (xK < 0) {
            xK = 0;
        }
        int yK = (int) Math.floor(y / 20);
        return this.currentMap.getGraphMatrix()[yK][xK];
    }

    public Player getPlayer() {
        return this.player;
    }

    public Monster getRed() {
        return this.red;
    }

    public Monster getPink() {
        return this.pink;
    }

    public Monster getBlue() {
        return this.blue;
    }

    public Monster getOrange() {
        return this.orange;
    }

    public Graph getGraph() {
        return this.currentMap;
    }
}
