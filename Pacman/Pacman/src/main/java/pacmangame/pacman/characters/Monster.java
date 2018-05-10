package pacmangame.pacman.characters;

import pacmangame.pacman.map.Tile;
import java.util.ArrayDeque;
import javafx.scene.image.Image;

public class Monster {

    private double x, y, width, movementSpeed;
    private int pathSize;
    private Tile startingTile;
    private boolean active = false;
    private Behaviour currentBehaviour = Behaviour.NORMAL;
    private Tile nextTile = null;
    private ArrayDeque<Tile> nextPath = new ArrayDeque<>();
    private boolean isInPanic = false;
    private boolean panicSpeedLimiter = false;
    private String imagePath = "";
    private final Image up;
    private final Image down;
    private final Image left;
    private final Image right;
    private Image currentImage;

    /**
     *
     * @param startingTile Tile from which the monster starts the game.
     * @param movementSpeed Movement speed of the monster.
     * @param pathSize Maximum size of the path a monster can have.
     * @param imagepath Path to the image of the monster.
     */
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

    /**
     *
     */
    public void activate() {
        this.active = true;
    }

    /**
     *
     * @return True if monster is active, else false.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets monster's behaviour to given behaviour, unless monster is in a reset
     * state.
     *
     * @param newBehaviour Monster's new behaviour.
     */
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

    /**
     *
     * @param path Monster's new path.
     */
    public void setNextPath(ArrayDeque<Tile> path) {
        this.nextPath = path;
    }

    /**
     *
     * @return Tile from which the monster started the game.
     */
    public Tile getStartingTile() {
        return this.startingTile;
    }

    /**
     *
     * @return Monster's path size.
     */
    public int getPathSize() {
        return this.pathSize;
    }

    /**
     * Checks monsters current position. If monster is in a reset state and has
     * reached its starting tile, sets monster behaviour and speed to normal. If
     * monster has reached its next tile on a path, sets next tile to null.
     */
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
        if (!this.panicSpeedLimiter && this.currentBehaviour == Behaviour.PANIC) {
            this.panicSpeedLimiter = true;
        }
    }

    /**
     * Moves the monster. If monster has no next tile to move to, takes the
     * first tile from the queue and sets it as the next tile. If the queue is
     * empty, returns false. Also changes monster's image to match the direction
     * it is going. Handles cases where monster is transferred from one side of
     * the map to the other. When monster is in panic, it can only move every other
     * time it is updated, making it slow down.
     *
     * @return False if moving the monster failed, else True.
     */
    public boolean move() {
        if (this.panicSpeedLimiter && this.currentBehaviour == Behaviour.PANIC) {
            this.panicSpeedLimiter = false;
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

    /**
     *
     * @return Monster's path.
     */
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

    /**
     *
     * @return x-coordinate at the centre of the monster.
     */
    public double getCentreX() {
        return this.x + (this.width / 2);
    }

    /**
     *
     * @return y-coordinate at the centre of the monster.
     */
    public double getCentreY() {
        return this.y + (this.width / 2);
    }

    /**
     *
     * @return Monster's current image.
     */
    public Image getCurrentImage() {
        return this.currentImage;
    }
}
