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
    private String opCheck = System.getProperty("os.name").toLowerCase();

    public Player(double x, double y, int lifeTotal) {
        this.x = x;
        this.y = y;
        this.startingX = x;
        this.startingY = y;
        this.hitPointsLeft = lifeTotal;
    }

    public double getStartingX() {
        return this.x;
    }

    public double getStartingY() {
        return this.y;
    }

    public int getRemainingLife() {
        return this.hitPointsLeft;
    }

    public boolean gotHit() {
        return this.gotHit;
    }

    public void loseHitPoint() {
        this.gotHit = true;
        loseHitPoints();
    }

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

    public void loseHitPoints() {
        if (opCheck.indexOf("win") >= 0) {
            this.mouthAngle += 2;
        } else {
            this.mouthAngle += 0.2;
        }
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

    public Direction getPreviousDirection() {
        return this.previousDirection;
    }

    public Direction getMovementDirection() {
        return movementDirection;
    }

    public double getMovementSpeed() {
        return this.movementSpeed;
    }

    public void setMovementDirection(Direction movementDirection) {
        if (this.movementDirection != Direction.NOT_MOVING) {
            this.previousDirection = this.movementDirection;
        }
        this.movementDirection = movementDirection;
    }

    public Direction getQueuedDirection() {
        return queuedDirection;
    }

    public void setQueuedDirection(Direction queuedDirection) {
        this.queuedDirection = queuedDirection;
    }

    public double getX() {
        return x;
    }

    public double getWidth() {
        return this.width;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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

    public double getCentreX() {
        return this.x + (this.width / 2);
    }

    public double getCentreY() {
        return this.y + (this.width / 2);
    }

}
