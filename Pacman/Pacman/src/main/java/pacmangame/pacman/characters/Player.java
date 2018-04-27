/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.characters;

import javafx.scene.paint.Color;
import pacmangame.pacman.logic.GameTimer;

/**
 *
 * @author User
 */
public class Player {

    private double width = 20;
    private double mouthAngle = 0;
    private boolean angleChangeDirection = true;
    private double x, y, startingX, startingY;
    private Color color = Color.YELLOW;
    private Direction movementDirection = Direction.NOT_MOVING;
    private Direction queuedDirection = Direction.NOT_MOVING;
    private Direction previousDirection = Direction.NOT_MOVING;
    private double movementSpeed = 2;
    private boolean gotHit = false;
    private int hitPointsLeft = 3;

    /**
     *
     * @param x
     * @param y
     * @param lifeTotal
     */
    public Player(double x, double y, int lifeTotal) {
        this.x = x;
        this.y = y;
        this.startingX = x;
        this.startingY = y;
        this.hitPointsLeft = lifeTotal;
    }

    /**
     *
     * @return Number of player's remaining hit points.
     */
    public int getRemainingLife() {
        return this.hitPointsLeft;
    }

    /**
     *
     * @return True if player has been hit, else False
     */
    public boolean gotHit() {
        return this.gotHit;
    }

    /**
     * Sets player's gotHit value to True. Loses a hitpoint.
     */
    public void loseHitPoint() {
        this.gotHit = true;
        loseHitPoints();
    }

    /**
     *
     * @return Angle of player character's mouth.
     */
    public double getMouthAngle() {
        return this.mouthAngle;
    }

    private void changeAngle() {
        if (this.angleChangeDirection) {
            mouthAngle += 15;
        } else {
            mouthAngle -= 15;
        }
        if (mouthAngle >= 60 || mouthAngle <= 0) {
            angleChangeDirection = !angleChangeDirection;
        }
    }

    /**
     * Used for animating player death. Adds to player's mouth angle until
     * player disappears. After disappearing, reset player position to its
     * starting position, reduce 1 hitpoint, reset movement direction and set
     * player's gotHit value to false.
     *
     */
    public void loseHitPoints() {
        this.mouthAngle += 3;
        if (this.mouthAngle >= 180) {
            if (this.hitPointsLeft > 0) {
                this.x = startingX;
                this.y = startingY;
                this.mouthAngle = 1;
                this.movementDirection = Direction.NOT_MOVING;
                this.queuedDirection = Direction.NOT_MOVING;
            }
            this.hitPointsLeft--;
            this.gotHit = false;
        }
    }

    /**
     *
     * @return Direction player moved to before changing direction.
     */
    public Direction getPreviousDirection() {
        return this.previousDirection;
    }

    /**
     *
     * @return Player's current movement direction.
     */
    public Direction getMovementDirection() {
        return movementDirection;
    }

    /**
     *
     * @return Player's movement speed.
     */
    public double getMovementSpeed() {
        return this.movementSpeed;
    }

    /**
     * Sets player's movement direction to given direction. If player's current
     * direction before changing is not NOT_MOVING, sets player's previous
     * direction to be player's current direction. Then player's current
     * direction is changed to given direction.
     *
     * @param movementDirection
     */
    public void setMovementDirection(Direction movementDirection) {
        if (this.movementDirection != Direction.NOT_MOVING) {
            this.previousDirection = this.movementDirection;
        }
        this.movementDirection = movementDirection;
    }

    /**
     *
     * @return Direction that is queued.
     */
    public Direction getQueuedDirection() {
        return queuedDirection;
    }

    /**
     *
     * @param queuedDirection Direction that is queued.
     */
    public void setQueuedDirection(Direction queuedDirection) {
        this.queuedDirection = queuedDirection;
    }

    /**
     *
     * @return Player's x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     *
     * @return Player character's width.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     *
     * @param x Player's new x coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     *
     * @return Player's current y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     *
     * @param y Player's new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     *
     * @return Player's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * If current direction is not NOT_MOVING, changes player's x or y
     * coordinates according to current direction.
     */
    public void move() {
        if (Direction.NOT_MOVING != this.movementDirection) {
            changeAngle();
            switch (this.movementDirection) {
                case DOWN:
                    this.y += movementSpeed;
                    break;
                case UP:
                    this.y -= movementSpeed;
                    break;
                case RIGHT:
                    this.x += movementSpeed;
                    break;
                case LEFT:
                    this.x -= movementSpeed;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *
     * @return x-coordinate in the middle of player character.
     */
    public double getCentreX() {
        return this.x + (this.width / 2);
    }

    /**
     *
     * @return y-coordinate in the middle of player character.
     */
    public double getCentreY() {
        return this.y + (this.width / 2);
    }

}
