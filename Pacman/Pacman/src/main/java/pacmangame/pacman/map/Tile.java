/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.map;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Tile {

    private double x, y, width, value;
    private Tile pathFrom = null;
    private ArrayList<Point> tilesPoints;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param value
     */
    public Tile(double x, double y, double width, double value) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.value = value;
        this.tilesPoints = new ArrayList<>();
    }

    /**
     *
     * @param p Point added to the tile.
     */
    public void addTilePoint(Point p) {
        this.tilesPoints.add(p);
    }

    /**
     *
     * @return List of points on the tile.
     */
    public ArrayList<Point> getTilesPoints() {
        return this.tilesPoints;
    }


    /**
     *
     * @return x-coordinate of the upper left corner of the tile.
     */
    public double getX() {
        return x;
    }

    /**
     * If a path has been calculated through this tile, return the tile from which the path comes to this tile.
     * @return Tile from which the path comes to this tile. Null if no path has been calculated through this tile.
     */
    public Tile getPathFrom() {
        return this.pathFrom;
    }

    /**
     *
     * @param tile Tile from which a path comes to this tile.
     */
    public void setPathFrom(Tile tile) {
        this.pathFrom = tile;
    }

    /**
     *
     * @return y-coordinate of the upper left corner of the tile.
     */
    public double getY() {
        return y;
    }

    /**
     *
     * @return Width of the tile.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     *
     * @return Value of the tile. 0 if the tile is a wall, else 1.
     */
    public double getValue() {
        return this.value;
    }

    /**
     *
     * @return hashcode of the tile.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    /**
     *
     * @param obj Object the tile is compared to.
     * @return True if given object and this tile are the same, else false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

}
