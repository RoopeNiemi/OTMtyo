/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.characters;

import pacmangame.pacman.map.Tile;
import java.util.ArrayDeque;
import javafx.scene.image.Image;

/**
 *
 * @author User
 */
public class Monster {

    private double x, y, width, movementSpeed;
    private int pathSize;
    private Tile startingTile;
    private boolean active = false;
    private Behaviour currentBehaviour = Behaviour.NORMAL;
    private Tile nextTile = null;
    private ArrayDeque<Tile> nextPath = new ArrayDeque<>();
    private boolean isInPanic = false;
    private boolean panicInProgess = false;
    private String imagePath = "";
    private final Image up;
    private final Image down;
    private final Image left;
    private final Image right;
    private Image currentImage;

    public Monster(Tile startingTile, double movementSpeed, double pathSize, String imagepath) {
        this.x = startingTile.getX();
        this.y = startingTile.getY();
        this.width = 20;
        this.startingTile = startingTile;
        this.movementSpeed = movementSpeed;
        this.pathSize = (int) pathSize;
        this.imagePath = imagepath;

        up = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Up.png"));
        down = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Down.png"));
        left = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Left.png"));
        right = new Image(getClass().getResourceAsStream("/" + this.imagePath + "Right.png"));
        currentImage = up;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setCurrentBehaviour(Behaviour newBehaviour) {
        if (this.currentBehaviour != Behaviour.RESET) {
            this.currentBehaviour = newBehaviour;
        }
    }

    public Behaviour getCurrentBehaviour() {
        return this.currentBehaviour;
    }

    public void setMovementSpeed(double i) {
        this.movementSpeed = i;
    }

    public void setNextPath(ArrayDeque<Tile> path) {
        this.nextPath = path;
    }

    public Tile getStartingTile() {
        return this.startingTile;
    }

    public int getPathSize() {
        return this.pathSize;
    }

    public void checkPosition() {
        if (this.currentBehaviour == Behaviour.RESET && this.x == this.startingTile.getX() && this.y == this.startingTile.getY()) {
            this.currentBehaviour = Behaviour.NORMAL;
            this.movementSpeed = 2;
        }
        if (this.movementSpeed == 4 && Math.abs(this.x - nextTile.getX()) <= 2 && Math.abs(this.y - nextTile.getY()) <= 2) {
            this.x = nextTile.getX();
            this.y = nextTile.getY();
            this.nextTile = null;
            return;
        }
        if (this.x == nextTile.getX() && this.y == nextTile.getY()) {
            this.nextTile = null;
        }
        if (!this.panicInProgess && this.currentBehaviour == Behaviour.PANIC) {
            this.panicInProgess = true;
        }
    }

    public boolean move() {
        if (this.panicInProgess && this.currentBehaviour == Behaviour.PANIC) {
            this.panicInProgess = false;
            return false;
        }
        if (this.nextTile == null) {
            if (this.nextPath.isEmpty()) {
                return false;
            } else {
                this.nextTile = this.nextPath.pollFirst();
            }
        }
        double currentTileX = Math.floor(this.x / 20);
        if (currentTileX < 0) {
            currentTileX = 0;
        }

        if (nextTile.getX() == 0 && nextTile.getY() == 180 && currentTileX == 18) {
            if (this.x < 376) {
                x += this.movementSpeed;
                currentImage = right;
            } else {
                this.x = 0;
            }
            checkPosition();
            return true;
        }
        if (nextTile.getX() == 360 && nextTile.getY() == 180 && currentTileX == 0) {
            if (this.x > -16) {
                x -= this.movementSpeed;
                currentImage = left;
            } else {
                this.x = 376;
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
