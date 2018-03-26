/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.characters;

import javafx.scene.paint.Color;
import pacmangame.pacman.map.Tile;
import java.util.ArrayDeque;
import java.util.Stack;
import javafx.scene.image.Image;

/**
 *
 * @author User
 */
public class Monster {

    private double x, y, width, movementSpeed, behaviourChangeThreshold, behaviourFactor;
    private int pathSize;

    private Tile nextTile = null;
    private ArrayDeque<Tile> nextPath = new ArrayDeque<>();
    private boolean behaviourState;
    private boolean isInPanic = false;
    private double startingMovementSpeed;
    private boolean panicInProgess = false;
    private String imagePath = "";
    private final Image up;
    private final Image down;
    private final Image left;
    private final Image right;
    private Image currentImage;

    public Monster(double x, double y, double movementSpeed, double behaviourThreshold, boolean startBehaviour, String imagepath) {
        this.x = x;
        this.y = y;
        this.width = 20;

        this.movementSpeed = movementSpeed;
        startingMovementSpeed = movementSpeed;
        this.behaviourChangeThreshold = behaviourThreshold;
        this.behaviourFactor = 0;
        this.pathSize = (int) behaviourThreshold;
        this.behaviourState = startBehaviour;
        this.imagePath = imagepath;

        up = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Up.png"));
        down = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Down.png"));
        left = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Left.png"));
        right = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Right.png"));
        currentImage = up;
    }

    public boolean getPanic() {
        return this.isInPanic;
    }

    public void setPanic(boolean panic) {
        this.isInPanic = panic;
    }

    public boolean getBehaviourState() {
        return this.behaviourState;
    }

    public void setNextPath(ArrayDeque<Tile> path) {
        this.nextPath = path;
    }

    public void changeBehaviour() {
        this.behaviourState = !this.behaviourState;
        this.nextPath.clear();
        this.nextTile=null;
        resetBehaviourFactor();
    }

    public int getPathSize() {
        return this.pathSize;
    }

    public double getBehaviourFactor() {
        return this.behaviourFactor;
    }

    public void resetBehaviourFactor() {
        this.behaviourFactor = 0;
    }

    public double getBehaviourChangeThreshold() {
        return this.behaviourChangeThreshold;
    }

    public void checkPosition() {
        if (this.x == nextTile.getX() && this.y == nextTile.getY()) {
            //   this.behaviourFactor++;
            this.nextTile = null;
        }
        if (!this.panicInProgess && this.isInPanic) {
            this.panicInProgess = true;
        }
    }

    public boolean move() {
        if (this.panicInProgess && this.isInPanic) {
            this.panicInProgess = false;
            return false;
        }
        if (this.nextTile == null) {
            if (this.nextPath.isEmpty()) {
               if(this.behaviourState){
                   this.changeBehaviour();
               }
                return false;
            } else {
                this.nextTile = this.nextPath.pollFirst();
            }
        }
        double currentTileX = Math.floor(this.x / 20);
        if (currentTileX < 0) {
            currentTileX = 0;
        }

        if (nextTile.getX() == 0 && nextTile.getY() == 180 && currentTileX == 17) {
            if (this.x < 356) {
                x += this.movementSpeed;
                currentImage = right;
            } else {
                this.x = 0;
            }
            checkPosition();
            return true;
        }
        if (nextTile.getX() == 340 && nextTile.getY() == 180 && currentTileX == 0) {
            if (this.x > -16) {
                x -= this.movementSpeed;
                currentImage = left;
            } else {
                this.x = 356;
            }
            checkPosition();
            return true;
        }
        double tileX = nextTile.getX();
        double tileY = nextTile.getY();
        if (tileX > this.x) {
            this.x += this.movementSpeed;
            currentImage = right;
        } else if (tileX < this.x) {
            this.x -= this.movementSpeed;
            currentImage = left;
        } else if (tileY > this.y) {
            this.y += this.movementSpeed;
            currentImage = down;
        } else if (tileY < this.y) {
            this.y -= this.movementSpeed;
            currentImage = up;
        }
        checkPosition();
        return true;
    }

    public double getX() {
        return x;
    }

    public ArrayDeque<Tile> getNextPath() {
        return this.nextPath;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public double getCentreX() {
        return this.x + (this.width / 2);
    }

    public double getCentreY() {
        return this.y + (this.width / 2);
    }

    public Image getCurrentImage() {
        return this.currentImage;
    }
}
