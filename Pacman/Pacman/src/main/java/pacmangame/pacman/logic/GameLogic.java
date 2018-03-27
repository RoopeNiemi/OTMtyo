/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;
import pacmangame.pacman.characters.Monster;
import pacmangame.pacman.characters.Player;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.map.Graph;
import pacmangame.pacman.map.MapLoader;
import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Tile;
import pacmangame.pacman.map.Type;
import pacmangame.pacman.pathfinding.Pathfinder;

/**
 *
 * @author User
 */
public class GameLogic {

    private Player player = new Player(180, 280);
    private Monster red = new Monster(160, 140, 2, 5, false, "red");
    private Monster pink = new Monster(180, 140, 2, 5, false, "pink");
    private Monster blue = new Monster(160, 160, 2, 5, false, "blue");
    private Monster orange = new Monster(180, 160, 2, 5, false, "orange");
    private Pathfinder pathfinder = new Pathfinder();
    private boolean gameOver = false;
    private MapLoader mapLoader;
    private Graph currentMap;
    private boolean inProgress = false;
    private PlayerResetTimer timer;
    private GameSituation situation;

    public GameLogic(MapLoader mapLoader, PlayerResetTimer timer) {
        this.currentMap = new Graph(mapLoader.loadMap());
        this.mapLoader = mapLoader;
        this.timer = timer;
        this.situation = new GameSituation(currentMap.getPointsList().size() * 10);
    }

    public void init() {
        this.currentMap = new Graph(mapLoader.loadMap());
        this.player = new Player(180, 280);
        this.red = new Monster(160, 140, 2, 5, false, "red");
        this.pink = new Monster(180, 140, 2, 5, false, "pink");
        this.blue = new Monster(160, 160, 2, 5, false, "blue");
        this.orange = new Monster(180, 160, 2, 5, false, "orange");
        this.pathfinder = new Pathfinder();
        this.gameOver = false;
        this.inProgress = false;
        this.situation = new GameSituation(currentMap.getPointsList().size() * 10);
    }

    public GameSituation getSituation() {
        return this.situation;
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    private void findMonsterPath(Monster monster, Tile monsterTile, Tile destinationTile) {
        this.inProgress = true;
        Stack<Tile> path = pathfinder.findPath(monsterTile, this.currentMap.getGraphMatrix(), destinationTile);
        if (path.size() < 1) {
            monster.setNextTile(destinationTile);
            return;
        }
        monster.getNextPath().clear();

        while (!path.isEmpty() && monster.getNextPath().size() < monster.getPathSize()) {
            monster.getNextPath().addLast(path.pop());
        }
        this.inProgress = false;
    }

    public void movePlayer() {
        this.player.move();
        checkPointSituation();
        if (player.getX() <= 0 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY()) {
            player.setX(this.currentMap.getGraphMatrix()[9][17].getX() + 18);
            return;
        }
        if (player.getX() >= this.currentMap.getGraphMatrix()[9][17].getX() + 18 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY() && player.getMovementDirection() == Direction.RIGHT) {
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
        if (player.getCentreX() <= 359) {
            Tile playerTile = getTile(player.getCentreX(), player.getCentreY());
            Stack<Point> deletedPoints = new Stack();
            for (Point currentPoint : playerTile.getTilesPoints()) {
                if (playerCollidesWithPoint(currentPoint)) {
                    deletedPoints.push(currentPoint);
                    if (currentPoint.getType() == Type.FRUIT) {
                        //Monsters panic, slow down
                        setAllMonsterPanicState(true);
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

    public void setAllMonsterPanicState(boolean bool) {
        this.red.setPanic(bool);
        this.orange.setPanic(bool);
        this.blue.setPanic(bool);
        this.pink.setPanic(bool);
    }

    private boolean playerCollidesWithPoint(Point point) {
        return (Math.abs(player.getCentreX() - point.getCentreX()) <= 5 && Math.abs(player.getCentreY() - point.getCentreY()) <= 5);
    }

    private void checkMonsterBehaviourState(Monster monster) {
        if (monster.getBehaviourFactor() == monster.getBehaviourChangeThreshold()) {
            monster.changeBehaviour();
        }
    }

    public void updateMonsters() {
        //RED MONSTER
        checkMonsterBehaviourState(this.red);
        if (!this.red.getBehaviourState()) {
            updateMonster(this.red, getTile(this.player.getX(), this.player.getY()));
        } else {
            updateMonster(this.red, getTile(this.player.getX(), this.player.getY()));
        }
        //ORANGE MONSTER
        checkMonsterBehaviourState(this.orange);
        if (!this.orange.getBehaviourState()) {
            orangeMovement();
        } else {
            orangeMovement();
        }
        //BLUE MONSTER
        checkMonsterBehaviourState(this.blue);
        if (!this.blue.getBehaviourState()) {
            blueMovement();
        } else {
            updateMonster(this.blue, getTile(this.player.getX(), this.player.getY()));
        }
        //PINK MONSTER
        checkMonsterBehaviourState(this.pink);
        if (!this.pink.getBehaviourState()) {
            updateMonster(this.pink, tileInFrontOfPlayer());
        } else {
            updateMonster(this.pink, tileInFrontOfPlayer());
        }
    }

    private void chooseFromThreeTiles(Monster monster, Tile firstChoice, Tile secondChoice, Tile thirdChoice) {
        int random = new Random().nextInt(4);
        if (random == 0 && getTile(monster.getX(), monster.getY()) != secondChoice) {
            updateMonster(monster, secondChoice);
        } else {
            if (getTile(monster.getX(), monster.getY()) == firstChoice) {
                updateMonster(monster, thirdChoice);
            } else {
                updateMonster(monster, firstChoice);
            }
        }
    }

    private void orangeMovement() {
        if (distanceFromPlayer() > 5) {
            updateMonster(this.orange, getTile(this.player.getX(), this.player.getY()));
        } else {
            if (getTile(this.orange.getX(), this.orange.getY()) == getBottomRightTile()) {
                    updateMonster(this.orange, getTopRightTile());
            } else {
                updateMonster(this.orange, getBottomRightTile());
            }
        }
    }

    private void blueMovement() {
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:
                updateMonster(this.blue, tileInFrontOfPlayer());
                break;
            case 1:
                updateMonster(this.blue, getTile(player.getX(), player.getY()));
                break;
            case 2:
                if (getTile(this.blue.getX(), this.blue.getY()) == getBottomRightTile()) {
                    updateMonster(this.blue, tileInFrontOfPlayer());
                } else {
                    updateMonster(this.blue, getBottomRightTile());
                }
                break;
            default:
                break;
        }
    }

    private int distanceFromPlayer() {
        int distanceX = Math.abs((int) Math.floor(this.blue.getX() / 20) - (int) Math.floor(this.player.getX() / 20));
        int distanceY = Math.abs((int) Math.floor(this.blue.getY() / 20) - (int) Math.floor(this.player.getY() / 20));
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
            case NOT_MOVING:
                return getTile(player.getX(), player.getY());
        }
        return null;
    }

    public Tile getRandomDestinationTile(Monster monster) {
        Tile monsterTile = getTile(monster.getX(), monster.getY());
        List<Tile> tiles = getGraph().getMovableTiles().stream().filter(a -> a.getX() != monsterTile.getX() && a.getY() != monsterTile.getY()).collect(Collectors.toCollection(ArrayList::new));
        return tiles.get(new Random().nextInt(tiles.size()));
    }

    public void updateMonster(Monster monster, Tile destinationTile) {
        if (this.gameOver) {
            return;
        }

        if (monster.getNextTile() == null && monster.getNextPath().isEmpty()) {
            if (monster.getPanic()) {
                findMonsterPath(monster, getTile(monster.getX(), monster.getY()), getRandomDestinationTile(monster));
            } else {
                findMonsterPath(monster, getTile(monster.getX(), monster.getY()), destinationTile);
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
        return this.currentMap.getGraphMatrix()[16][16];
    }

    public Tile getTopRightTile() {
        return this.currentMap.getGraphMatrix()[1][16];
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
