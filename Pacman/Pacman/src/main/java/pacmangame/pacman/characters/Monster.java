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

/**
 *
 * @author User
 */
public class Monster {

    private double x, y, width, movementSpeed, behaviourChangeThreshold, behaviourFactor;
    private int pathSize;
    private Color color;
    private Tile nextTile = null;
    private ArrayDeque<Tile> nextPath = new ArrayDeque<>();
    private boolean behaviourState = false;

    public Monster(double x, double y, double movementSpeed, Color color, double behaviourThreshold) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.color = color;
        this.movementSpeed = movementSpeed;
        this.behaviourChangeThreshold = behaviourThreshold;
        this.behaviourFactor = 0;
        this.pathSize = (int) behaviourThreshold;

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
        resetBehaviourFactor();
        if (this.behaviourState) {
            this.pathSize /= 2;
        } else {
            this.pathSize *= 2;
        }
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

    public boolean move() {
        if (this.nextTile == null) {
            if (this.nextPath.isEmpty()) {
                return false;
            } else {
                this.nextTile = this.nextPath.pollFirst();
            }
        }
        double tileX = nextTile.getX();
        double tileY = nextTile.getY();
        if (tileX > this.x) {
            this.x += this.movementSpeed;
        } else if (tileX < this.x) {
            this.x -= this.movementSpeed;
        } else if (tileY > this.y) {
            this.y += this.movementSpeed;
        } else if (tileY < this.y) {
            this.y -= this.movementSpeed;
        }
        if (this.x == tileX && this.y == tileY) {
            this.behaviourFactor++;
            this.nextTile = null;
        }
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

}
