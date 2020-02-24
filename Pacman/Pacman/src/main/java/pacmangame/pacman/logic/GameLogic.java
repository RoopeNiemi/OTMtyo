package pacmangame.pacman.logic;

import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Type;

import java.util.*;
import java.util.stream.Collectors;

import pacmangame.pacman.characters.*;
import pacmangame.pacman.map.*;
import pacmangame.pacman.pathfinding.Pathfinder;

public class GameLogic {

    private Player player;
    private Monster red;
    private Monster pink;
    private Monster blue;
    private Monster orange;
    private Pathfinder pathfinder = new Pathfinder();
    private Graph currentMap;
    private GameState gameState;
    private GameTimerHandler gameTimerHandler;

    public GameLogic(MapLoader mapLoader, int startingPoints, int playerLives) {
        this.player = new Player(180, 300, playerLives);
        this.currentMap = new Graph(mapLoader.loadMap());
        this.gameState = new GameState(startingPoints, "highscore.db");
        this.gameTimerHandler = new GameTimerHandler();
        initMonsters();
    }

    private void initMonsters() {
        this.red = new Monster(currentMap.getRedStartingTile(), 2, 5, "red");
        this.pink = new Monster(currentMap.getPinkStartingTile(), 2, 7, "pink");
        this.blue = new Monster(currentMap.getBlueStartingTile(), 2, 5, "blue");
        this.orange = new Monster(currentMap.getOrangeStartingTile(), 2, 10, "orange");
    }

    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * @return True if game is not over, player has not been hit, and level has
     * not been completed. Else returns false
     */
    public boolean situationNormal() {
        return !this.gameState.isGameOver() && !this.player.gotHit() && !this.gameState.isComplete();
    }

    /**
     *
     */
    public void resetMonsterStartingPositions() {
        resetMonsterPosition(this.red);
        resetMonsterPosition(this.pink);
        resetMonsterPosition(this.blue);
        resetMonsterPosition(this.orange);
    }

    /**
     * Reset monster's position on map. Monster's location on map is changed to
     * its starting location at the beginning of the game. Its calculated
     * movement paths are also reset.
     *
     * @param monster Monster that is to be reset
     */
    private void resetMonsterPosition(Monster monster) {
        monster.setX(monster.getStartingTile().getX());
        monster.setY(monster.getStartingTile().getY());
        monster.getNextPath().clear();
        monster.setNextTile(null);
    }

    /**
     * Adds time passed to the GameTimer that handles monsters' behaviour. If a
     * set threshold is reached, changes monster behaviour. Does not work if
     * monsters are in a panic state. If monsters are in a panic state, adds
     * time passed to the GameTimer that handles monsters' panic behaviour. If
     * panic behaviour threshold is reached, monsters' behaviour is set to
     * normal.
     *
     * @param updateFrequency time that is added to the GameTimers handling
     *                        behaviour changes
     */
    public void handleBehaviourUpdate(long updateFrequency) {
        if (monsterBehaviourThresholdReached(updateFrequency)) {
            if (gameTimerHandler.monsterBehaviourTimerThresholdNormal()) {
                scatterIfPossible();
            } else {
                activateNormalMode();
            }
        }
        if (timerThresholdReached(updateFrequency)) {
            endPanicPhase();
        }
    }

    /**
     * Adds time to GameTimer handling monster behaviour.
     *
     * @param addedTime time added to GameTimer handling monster behaviour.
     * @return True if the GameTimer handling monster behaviour reaches a
     * threshold. Else false.
     */
    private boolean monsterBehaviourThresholdReached(long addedTime) {
        return gameTimerHandler.getMonsterBehaviourTimer().addTime(addedTime);
    }

    /**
     * Adds time to GameTimer handling monster panic phase.
     *
     * @param addedTime Time added to GameTimer handling monster panic phase.
     * @return True if the GameTimer that handles monster panic phase has
     * reached a threshold.
     */
    private boolean timerThresholdReached(long addedTime) {
        return gameTimerHandler.getPanicTimer().addTime(addedTime);
    }

    private void findMonsterPath(Monster monster, Tile monsterTile, Tile destinationTile) {
        Stack<Tile> path = pathfinder.findPath(monsterTile, this.currentMap.getMap(), destinationTile);
        if (path.isEmpty() || path.peek() == null || path.size() == 1 && path.peek() == monsterTile) {
            path = pathfinder.findPath(monsterTile, this.currentMap.getMap(), getRandomDestinationTile(monster));
        }
        while (!path.isEmpty() && monster.getNextPath().size() < monster.getPathSize()) {
            monster.getNextPath().addLast(path.pop());
        }
    }

    /**
     * Sets monsters' behaviour to scatter if monsters have scattered less than
     * 4 times in current game.
     */
    public void scatterIfPossible() {
        if (this.gameState.getTimesScattered() < 4) {
            gameTimerHandler.setBehaviourThresholdToScatter();
            this.gameState.addScatterTime();
            setAllMonstersBehaviourState(Behaviour.SCATTER);
            gameTimerHandler.getMonsterBehaviourTimer().reset();
            gameTimerHandler.getMonsterBehaviourTimer().setActive(true);
        }
    }

    /**
     * Sets monsters' behaviour to normal
     */
    public void activateNormalMode() {
        gameTimerHandler.setBehaviourThresholdToNormal();
        gameTimerHandler.getMonsterBehaviourTimer().reset();
        setAllMonstersBehaviourState(Behaviour.NORMAL);
        gameTimerHandler.getMonsterBehaviourTimer().setActive(true);
    }

    /**
     * Sets all monsters behaviour to normal. Activates the GameTimer that
     * handles monsters' normal behaviour states.
     */
    public void endPanicPhase() {
        setAllMonstersBehaviourState(Behaviour.NORMAL);
        gameTimerHandler.getMonsterBehaviourTimer().setActive(true);
    }

    /**
     * Calls player's move method. Calls checkPointSituation method to check if
     * player collides with any points on map. Handles cases where player is
     * transferred from one side of the map to another. Calls for a method that
     * checks if a wall is hit or a movement direction in queue is legal.
     * Changes player's movement direction accordingly.
     */
    public void movePlayer() {
        this.player.move();
        checkPointSituation();
        if (playerAbleToTransitionToRightSide()) {
            player.setX(currentMap.getRightSideTransitionTile().getX() + 18);
            return;
        }
        if (playerAbleToTransitionToLeftSide()) {
            player.setX(currentMap.getLeftSideTransitionTile().getX());
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

    private boolean playerAbleToTransitionToRightSide() {
        final Tile leftTransitionTile = currentMap.getLeftSideTransitionTile();
        return player.getX() <= leftTransitionTile.getX() && player.getY() == leftTransitionTile.getY();
    }

    private boolean playerAbleToTransitionToLeftSide() {
        final Tile rightTransitionTile = currentMap.getRightSideTransitionTile();
        return player.getX() >= rightTransitionTile.getX() + 18 && player.getY() == rightTransitionTile.getY() && player.getMovementDirection() == Direction.RIGHT;
    }

    public void handleLosingHitPoints() {
        gameTimerHandler.getMonsterBehaviourTimer().setActive(false);
        getPlayer().loseHitPoints();
        if (!getPlayer().gotHit()) {
            gameTimerHandler.getMonsterBehaviourTimer().setActive(true);
            resetMonsterStartingPositions();
        }
    }

    public void handleGameOver() {
        gameTimerHandler.getPanicTimer().setActive(false);
        gameTimerHandler.getMonsterBehaviourTimer().setActive(false);
    }

    /**
     * Adds time to the GameTimer that handles activating monsters. Activates
     * next monster in line when a threshold is reached, or deactivates if all
     * are already active.
     *
     * @param addedTime Time added to the GameTimer that handles activating
     *                  monsters.
     */
    public void monsterActivation(long addedTime) {
        if (isMonsterActivationPossible(addedTime)) {
            switch (this.gameState.getActiveMonsters()) {
                case 1:
                    this.pink.setActive(true);
                    gameTimerHandler.getMonsterActivator().setActive(true);
                    this.gameState.addActiveMonster();
                    break;
                case 2:
                    this.blue.setActive(true);
                    gameTimerHandler.getMonsterActivator().setActive(true);
                    this.gameState.addActiveMonster();
                    break;
                case 3:
                    this.orange.setActive(true);
                    this.gameState.addActiveMonster();
                    break;
            }
        }
    }

    private boolean isMonsterActivationPossible(long addedTime) {
        GameTimer timer = gameTimerHandler.getMonsterActivator();
        return timer.isActive() && timer.addTime(addedTime);
    }

    private void checkPointSituation() {
        if (player.getCentreX() <= 379) {
            Tile playerTile = getTileFromCoordinates(player.getCentreX(), player.getCentreY());
            Stack<Point> deletedPoints = new Stack<>();
            for (Point currentPoint : playerTile.getTilesPoints()) {
                if (playerCollidesWithPoint(currentPoint)) {
                    deletedPoints.push(currentPoint);
                    if (currentPoint.getType() == Type.POWER_PELLET) {
                        //Monsters panic, slow down
                        setAllMonstersBehaviourState(Behaviour.PANIC);
                        gameTimerHandler.getMonsterBehaviourTimer().setActive(false);
                        gameTimerHandler.getPanicTimer().setActive(true);
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

    /**
     * Sets all monsters' behaviour to given behaviour.
     *
     * @param newBehaviour Monsters' new behaviour.
     */
    public void setAllMonstersBehaviourState(Behaviour newBehaviour) {
        this.red.setCurrentBehaviour(newBehaviour);
        this.orange.setCurrentBehaviour(newBehaviour);
        this.blue.setCurrentBehaviour(newBehaviour);
        this.pink.setCurrentBehaviour(newBehaviour);
    }

    private boolean playerCollidesWithPoint(Point point) {
        return (Math.abs(player.getCentreX() - point.getCentreX()) <= 5 && Math.abs(player.getCentreY() - point.getCentreY()) <= 5);
    }

    /**
     * Updates monsters. If a monsters path is empty, calculates a new one
     * according to its current behaviour. Moves monsters, and checks collision
     * with player. Does not work if monster is not active or game is over.
     */
    public void updateMonsters() {
        updateMonster(this.red, getPlayerTile(), this.currentMap.getTopRightTile());
        updateMonster(this.orange, orangeMovement(), this.currentMap.getBottomRightTile());
        updateMonster(this.blue, blueMovement(), this.currentMap.getTopLeftTile());
        updateMonster(this.pink, tileInFrontOfPlayer(), this.currentMap.getBottomLeftTile());
    }

    private Tile getPlayerTile() {
        return getTileFromCoordinates(player.getX(), player.getY());
    }

    private Tile orangeMovement() {
        if (distanceFromPlayer() > 4) {
            return getTileFromCoordinates(player.getX(), player.getY());
        } else {
            return this.currentMap.getBottomRightTile();
        }
    }

    private Tile blueMovement() {
        Tile inFrontOfPlayer = tileInFrontOfPlayer();
        Tile redTile = getTileFromCoordinates(this.red.getX(), this.red.getY());
        double xDifference = inFrontOfPlayer.getX() - redTile.getX();
        double yDifference = inFrontOfPlayer.getY() - redTile.getY();
        double destinationTileX = inFrontOfPlayer.getX() + xDifference;
        double destinationTileY = inFrontOfPlayer.getY() + yDifference;
        destinationTileX = checkInBounds(destinationTileX, 340);
        destinationTileY = checkInBounds(destinationTileY, 380);
        return getTileFromCoordinates(destinationTileX, destinationTileY);
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
        int distanceX = Math.abs((int) Math.floor(this.orange.getX() / Tile.TILE_WIDTH) - (int) Math.floor(this.player.getX() / Tile.TILE_WIDTH));
        int distanceY = Math.abs((int) Math.floor(this.orange.getY() / Tile.TILE_WIDTH) - (int) Math.floor(this.player.getY() / Tile.TILE_WIDTH));
        return distanceX + distanceY;
    }

    private Tile tileInFrontOfPlayer() {
        Direction dir = this.player.getMovementDirection();
        int playerTileX = (int) Math.floor(getTileFromCoordinates(player.getX(), player.getY()).getX() / Tile.TILE_WIDTH);
        int playerTileY = (int) Math.floor(getTileFromCoordinates(player.getX(), player.getY()).getY() / Tile.TILE_WIDTH);
        switch (dir) {
            case UP:
                return tileInFrontOfPlayerMovingUp(playerTileX, playerTileY);
            case DOWN:
                return tileInFrontOfPlayerMovingDown(playerTileX, playerTileY);
            case RIGHT:
                return tileInFrontOfPlayerMovingRight(playerTileX, playerTileY);
            case LEFT:
                return tileInFrontOfPlayerMovingLeft(playerTileX, playerTileY);
            default:
                return getTileFromCoordinates(player.getX(), player.getY());
        }
    }

    private Tile tileInFrontOfPlayerMovingUp(int playerTileX, int playerTileY) {
        if (playerIsFarEnoughFromTopWall(playerTileY)) {
            return this.currentMap.getMap()[playerTileY - 2][playerTileX];
        }
        return this.currentMap.getMap()[playerTileY + 4][playerTileX];

    }

    private Tile tileInFrontOfPlayerMovingDown(int playerTileX, int playerTileY) {
        if (playerIsFarEnoughFromBottomWall(playerTileY)) {
            return this.currentMap.getMap()[playerTileY + 2][playerTileX];
        }
        return this.currentMap.getMap()[playerTileY - 4][playerTileX];

    }

    private Tile tileInFrontOfPlayerMovingRight(int playerTileX, int playerTileY) {
        if (playerIsFarEnoughFromRightWall(playerTileX)) {
            return this.currentMap.getMap()[playerTileY][playerTileX + 2];
        }
        return this.currentMap.getMap()[playerTileY][playerTileX - 4];

    }

    private Tile tileInFrontOfPlayerMovingLeft(int playerTileX, int playerTileY) {
        if (playerIsFarEnoughFromLeftWall(playerTileX)) {
            return this.currentMap.getMap()[playerTileY][playerTileX - 2];
        }
        return this.currentMap.getMap()[playerTileY][playerTileX + 4];

    }

    private boolean playerIsFarEnoughFromTopWall(double playerTileY) {
        return playerTileY >= 2;
    }

    private boolean playerIsFarEnoughFromBottomWall(double playerTileY) {
        return playerTileY <= this.currentMap.getMap().length - 3;
    }

    private boolean playerIsFarEnoughFromRightWall(double playerTileX) {
        return playerTileX <= this.currentMap.getMap()[0].length - 3;
    }

    private boolean playerIsFarEnoughFromLeftWall(double playerTileX) {
        return playerTileX >= 2;
    }


    private Tile getRandomDestinationTile(Monster monster) {
        Tile monsterTile = getTileFromCoordinates(monster.getX(), monster.getY());
        List<Tile> tiles = getGraph().getCorridorTiles().stream().filter(a -> a.getX() != monsterTile.getX() && a.getY() != monsterTile.getY()).collect(Collectors.toCollection(ArrayList::new));
        return tiles.get(new Random().nextInt(tiles.size()));
    }

    private void updateMonster(Monster monster, Tile normalDestination, Tile scatterDestination) {
        if (this.gameState.isGameOver() || !monster.isActive()) {
            return;
        }
        if (monster.getNextTile() == null && monster.getNextPath().isEmpty()) {
            if (null == monster.getCurrentBehaviour()) {
                findMonsterPath(monster, getTileFromCoordinates(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
            } else {
                switch (monster.getCurrentBehaviour()) {
                    case NORMAL:
                        findMonsterPath(monster, getTileFromCoordinates(monster.getX(), monster.getY()), normalDestination);
                        break;
                    case SCATTER:
                        findMonsterPath(monster, getTileFromCoordinates(monster.getX(), monster.getY()), scatterDestination);
                        break;
                    case RESET:
                        findMonsterPath(monster, getTileFromCoordinates(monster.getX(), monster.getY()), monster.getStartingTile());
                        break;
                    default:
                        findMonsterPath(monster, getTileFromCoordinates(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
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

        if (playerCollidesWithMonster(monster)) {
            if (monster.getCurrentBehaviour() == Behaviour.PANIC) {
                playerEatsMonster(monster);
            } else {
                findMonsterPath(monster, getMonsterTile(monster), getRandomDestinationTile(monster));
                this.player.loseHitPoint();
                if (this.player.getRemainingLife() <= 0) {
                    this.gameState.setGameOver(true);
                }
            }
        }
    }

    private boolean playerCollidesWithMonster(Monster monster) {
        return Math.abs(this.player.getCentreX() - monster.getCentreX()) <= 10 && Math.abs(this.player.getCentreY() - monster.getCentreY()) <= 10;
    }

    private void playerEatsMonster(Monster monster) {
        monster.setCurrentBehaviour(Behaviour.RESET);
        monster.getNextPath().clear();
        monster.setMovementSpeed(4);
        this.gameState.gainPoint(100);
    }

    private Tile getMonsterTile(Monster monster) {
        return getTileFromCoordinates(monster.getX(), monster.getY());
    }

    private Tile getTileFromCoordinates(double x, double y) {
        int xK = (int) Math.floor(x / 20);
        if (xK < 0) {
            xK = 0;
        }
        int yK = (int) Math.floor(y / 20);
        return this.currentMap.getMap()[yK][xK];
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
