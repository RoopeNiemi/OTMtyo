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

    public Tile(double x, double y, double width, double value) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.value = value;
        this.tilesPoints=new ArrayList<>();
    }

    public void addTilePoint(Point p){
        this.tilesPoints.add(p);
    }
    public ArrayList<Point> getTilesPoints(){
        return this.tilesPoints;
    }
    
    public void setTilesPoints(ArrayList<Point> newPoints){
        this.tilesPoints=newPoints;
    }
    
    public double getX() {
        return x;
    }

    public Tile getPathFrom() {
        return this.pathFrom;
    }

    public void setPathFrom(Tile tile) {
        this.pathFrom = tile;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return this.width;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

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
