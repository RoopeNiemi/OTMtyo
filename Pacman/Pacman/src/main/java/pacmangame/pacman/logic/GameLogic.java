/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.logic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;
import pacmangame.pacman.characters.Monster;
import pacmangame.pacman.characters.Player;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.map.Graph;
import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Tile;
import pacmangame.pacman.pathfinding.Pathfinder;

/**
 *
 * @author User
 */
public class GameLogic {

    private Player player = new Player(20, 20);
    private Monster red = new Monster(120, 80, 1, Color.RED, 15);
    private Monster yellow = new Monster(120, 60, 1, Color.YELLOW, 12);
    private Monster blue = new Monster(140, 80, 1, Color.CYAN, 10);
    private Monster orange = new Monster(140, 60, 1, Color.ORANGE, 11);
    private Pathfinder pathfinder = new Pathfinder();
    private boolean gameOver = false;
    private Graph currentMap = new Graph(loadMap("map1.txt"));
    private boolean inProgress = false;
    private int points = 0;

    public GameLogic() {

    }

    public void init() {
        this.points = 0;
        this.currentMap = new Graph(loadMap("map1.txt"));
        this.player = new Player(20, 20);
        this.red = new Monster(120, 80, 1, Color.RED, 15);
        this.yellow = new Monster(120, 60, 1, Color.YELLOW, 12);
        this.blue = new Monster(140, 80, 1, Color.CYAN, 10);
        this.orange = new Monster(140, 60, 1, Color.ORANGE, 11);
        this.pathfinder = new Pathfinder();
        this.gameOver = false;
    }

    private List<String> loadMap(String path) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        Scanner fileScanner = new Scanner(is);
        List<String> map = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            map.add(fileScanner.nextLine());
        }
        return map;
    }

    public boolean isInProgress() {
        return this.inProgress;
    }
    
    public int getPointAmount(){
        return this.points;
    }

    private void gameOver() {
        this.gameOver = true;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    private void findMonsterPath(Monster monster, Tile monsterTile, Tile playerTile) {
        this.inProgress = true;
        Stack<Tile> path = pathfinder.findPath(monsterTile, this.currentMap.getGraphMatrix(), playerTile);
        if (path.size() < 1) {
            monster.setNextTile(playerTile);
            return;
        }
        monster.getNextPath().clear();
        while (!path.isEmpty() && monster.getNextPath().size() < monster.getBehaviourChangeThreshold()) {
            monster.getNextPath().addLast(path.pop());
        }
        this.inProgress = false;
    }

    public void movePlayer() {
        this.player.move();
        checkPointSituation();
        if (player.getX() <= 0 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY()) {
            player.setX(this.currentMap.getGraphMatrix()[9][17].getX() + 19);
            return;
        }
        if (player.getX() >= this.currentMap.getGraphMatrix()[9][17].getX() + 17 && player.getY() == this.currentMap.getGraphMatrix()[9][0].getY() && player.getMovementDirection() == Direction.RIGHT) {
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
        double playerCentreX = player.getX() + player.getWidth() / 2;
        double playerCentreY = player.getY() + player.getWidth() / 2;

        if (playerCentreX <= 359) {
            Tile playerTile = getTile(playerCentreX, playerCentreY);
            Stack<Point> deletedPoints = new Stack();
            for (int i = 0; i < playerTile.getTilesPoints().size(); i++) {
                if (Math.abs(playerCentreX - playerTile.getTilesPoints().get(i).getCentreX()) <= 5 && 
                        Math.abs(playerCentreY - playerTile.getTilesPoints().get(i).getCentreY()) <= 5) {
                        deletedPoints.push(playerTile.getTilesPoints().get(i));
                }
            }
            
            
            while(!deletedPoints.isEmpty()){
                Point p=deletedPoints.pop();
                playerTile.getTilesPoints().remove(p);
                points++;
                this.currentMap.getPointsList().remove(p);
            }
        }
    }

    public Tile getTile(double x, double y) {
        int xK = (int) Math.floor(x / 20);
        int yK = (int) Math.floor(y / 20);
        return this.currentMap.getGraphMatrix()[yK][xK];
    }

    public Tile getBottomRightTile() {
        return this.currentMap.getGraphMatrix()[16][16];
    }

    public Tile getTopRightTile() {
        return this.currentMap.getGraphMatrix()[1][16];
    }

    public void updateMonsters() {
        //RED MONSTER
        if (this.red.getBehaviourFactor() == this.red.getBehaviourChangeThreshold()) {
            this.red.changeBehaviour();
            this.red.resetBehaviourFactor();
        }
        if (!this.red.getBehaviourState()) {
            updateMonster(this.red, getRandomDestinationTile(this.red));

        } else {
            updateMonster(this.red, getPlayerTile());
        }

        //ORANGE MONSTER
        if (this.orange.getBehaviourFactor() == this.orange.getBehaviourChangeThreshold()) {
            this.orange.changeBehaviour();
            this.orange.resetBehaviourFactor();
        }
        if (!this.orange.getBehaviourState()) {
            updateMonster(this.orange, getRandomDestinationTile(this.orange));
        } else {
            updateMonster(this.orange, getPlayerTile());
        }
        //BLUE MONSTER
        if (this.blue.getBehaviourFactor() == this.blue.getBehaviourChangeThreshold()) {
            this.blue.changeBehaviour();
            this.blue.resetBehaviourFactor();
        }
        if (!this.blue.getBehaviourState()) {
            int random = new Random().nextInt(4);
            if (random == 0 && getTile(this.blue.getX(), this.blue.getY()) != getTopRightTile()) {
                updateMonster(this.blue, getTopRightTile());
            } else {
                if (getTile(this.blue.getX(), this.blue.getY()) == getBottomRightTile()) {
                    updateMonster(this.blue, getRandomDestinationTile(red));
                } else {
                    updateMonster(this.blue, getBottomRightTile());
                }
            }
        } else {
            updateMonster(this.blue, getPlayerTile());
        }
        //YELLOW MONSTER
        if (this.yellow.getBehaviourFactor() == this.yellow.getBehaviourChangeThreshold()) {
            this.yellow.changeBehaviour();
            this.yellow.resetBehaviourFactor();
        }
        if (!this.yellow.getBehaviourState()) {
            int random = new Random().nextInt(4);
            if (random == 0 && getTile(this.yellow.getX(), this.yellow.getY()) != getBottomRightTile()) {
                updateMonster(this.yellow, getBottomRightTile());
            } else {
                if (getTile(this.yellow.getX(), this.yellow.getY()) == getTopRightTile()) {
                    updateMonster(this.yellow, getRandomDestinationTile(red));
                } else {
                    updateMonster(this.yellow, getTopRightTile());
                }
            }
        } else {
            updateMonster(this.yellow, getPlayerTile());
        }
    }

    public Tile getRandomDestinationTile(Monster monster) {
        Tile monsterTile = getTile(monster.getX(), monster.getY());
        List<Tile> tiles = getGraph().getMovableTiles().stream().filter(a -> a.getX() != monsterTile.getX() && a.getY() != monsterTile.getY()).collect(Collectors.toCollection(ArrayList::new));
        int random = new Random().nextInt(tiles.size());
        return tiles.get(random);
    }

    public void updateMonster(Monster monster, Tile destinationTile) {
        if (this.gameOver) {
            return;
        }
        System.out.println("monster pos check");
        Tile monstersCurrentTile = this.currentMap.getGraphMatrix()[(int) Math.floor(monster.getY() / 20)][(int) Math.floor(monster.getX() / 20)];
        System.out.println("check if new path");
        if (monster.getNextTile() == null && monster.getNextPath().isEmpty()) {
            findMonsterPath(monster, monstersCurrentTile, destinationTile);
        }
        System.out.println("move");
        monster.move();
        monstersCurrentTile = this.currentMap.getGraphMatrix()[(int) Math.floor(monster.getY() / 20)][(int) Math.floor(monster.getX() / 20)];
        checkCollision(monster, monstersCurrentTile, getPlayerTile());
    }

    private void checkCollision(Monster monster, Tile monsterTile, Tile playerTile) {

        if (Math.abs(this.player.getX() - monster.getX()) <= 15) {
            if (Math.abs(this.player.getY() - monster.getY()) <= 15) {
                gameOver();
            }
        }
    }

    public Tile getPlayerTile() {
        return this.currentMap.getGraphMatrix()[(int) Math.floor(this.player.getY() / 20)][(int) Math.floor(player.getX() / 20)];
    }

    public int manhattanDistance(Tile monsterTile, Tile playerTile) {
        return 0;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Monster getRed() {
        return this.red;
    }

    public Monster getYellow() {
        return this.yellow;
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
