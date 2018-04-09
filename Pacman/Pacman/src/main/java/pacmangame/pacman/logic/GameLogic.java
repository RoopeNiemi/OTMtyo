/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

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
    private Monster red = new Monster(180, 140, 2, 5, false, "red");
    private Monster pink = new Monster(160, 180, 2, 5, false, "pink");
    private Monster blue = new Monster(180, 180, 2, 5, false, "blue");
    private Monster orange = new Monster(200, 180, 2, 5, false, "orange");
    private Pathfinder pathfinder = new Pathfinder();
    private boolean gameOver = false;
    private MapLoader mapLoader;
    private Graph currentMap;
    private GameTimer timer;
    private GameSituation situation;

    public GameLogic(MapLoader mapLoader, GameTimer timer, int startingPoints, int playerLives) {
        this.player = new Player(180, 300, playerLives);
        this.currentMap = new Graph(mapLoader.loadMap());
        this.mapLoader = mapLoader;
        this.timer = timer;
        this.situation = new GameSituation(currentMap.getPointsList().size() * 10, startingPoints);
    }

    public GameSituation getSituation() {
        return this.situation;
    }

    private void findMonsterPath(Monster monster, Tile monsterTile, Tile destinationTile) {
        if (monsterTile == destinationTile) {
            destinationTile = getRandomDestinationTile(monster);
        }
        Stack<Tile> path = pathfinder.findPath(monsterTile, this.currentMap.getGraphMatrix(), destinationTile);
        if (path.size() < 2) {
            monster.setNextTile(destinationTile);
            return;
        }
        monster.getNextPath().clear();
        while (!path.isEmpty() && monster.getNextPath().size() < monster.getPathSize()) {
            monster.getNextPath().addLast(path.pop());
        }
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
                        this.timer.setThreshold(5000000000L);
                        this.timer.activate();
                        this.player.setMortality(false);
                    }
                }
            }
            while (!deletedPoints.isEmpty()) {
                Point p = deletedPoints.pop();
                playerTile.getTilesPoints().remove(p);
                this.situation.gainPoint();
                this.currentMap.getPointsList().remove(p);
            }
        }
        if (currentMap.getPointsList().isEmpty()) {
            this.situation.setComplete(true);
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
        updateMonster(this.red, getTile(player.getX(), player.getY()), getTopRightTile());
        updateMonster(this.orange, orangeMovement(), getBottomRightTile());
        updateMonster(this.blue, blueMovement(), getTopLeftTile());
        updateMonster(this.pink, tileInFrontOfPlayer(), getBottomLeftTile());
    }

    private Tile orangeMovement() {
        if (distanceFromPlayer() > 8) {
            return getTile(player.getX(), player.getY());
        } else {
            return getBottomRightTile();
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
        if (this.gameOver) {
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
        if (!this.player.getMortality()) {
            return;
        }
        if (Math.abs(this.player.getCentreX() - monster.getCentreX()) <= 10 && Math.abs(this.player.getCentreY() - monster.getCentreY()) <= 10) {
            this.player.loseHitPoint(timer);
            findMonsterPath(monster, getTile(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
            if (this.player.getRemainingLife() <= 0) {
                this.situation.setGameOver(true);
            }
        }
    }

    public Tile getTile(double x, double y) {
        int xK = (int) Math.floor(x / 20);
        if (xK < 0) {
            xK = 0;
        }
        int yK = (int) Math.floor(y / 20);
        return this.currentMap.getGraphMatrix()[yK][xK];
    }

    public Tile getBottomRightTile() {
        return this.currentMap.getGraphMatrix()[19][17];
    }

    public Tile getTopRightTile() {
        return this.currentMap.getGraphMatrix()[1][17];
    }

    public Tile getBottomLeftTile() {
        return this.currentMap.getGraphMatrix()[19][1];
    }

    public Tile getTopLeftTile() {
        return this.currentMap.getGraphMatrix()[1][1];
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
