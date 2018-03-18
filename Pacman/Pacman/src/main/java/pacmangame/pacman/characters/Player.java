/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.characters;

import javafx.scene.paint.Color;

/**
 *
 * @author User
 */
public class Player {

    private double width = 20;
    private double x, y;
    private Color color = Color.YELLOW;
    private Direction movementDirection = Direction.NOT_MOVING;
    private Direction queuedDirection = Direction.NOT_MOVING;
    private double movementSpeed = 2.5;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Direction getMovementDirection() {
        return movementDirection;
    }

    public double getMovementSpeed() {
        return this.movementSpeed;
    }

    public void setMovementDirection(Direction movementDirection) {
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

}
