/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.map;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 *
 * @author nroope
 */
public class Point {

    private double centreX, centreY, widthAndHeight;
    private Color color = Color.GOLD;
    private Type type;

    public Point(double x, double y, Type type) {
        this.centreX = x;
        this.centreY = y;
        this.type = type;
        if (type == Type.POINT) {
            this.widthAndHeight = 2;
        } else {
            this.widthAndHeight = 8;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.centreX) ^ (Double.doubleToLongBits(this.centreX) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.centreY) ^ (Double.doubleToLongBits(this.centreY) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.widthAndHeight) ^ (Double.doubleToLongBits(this.widthAndHeight) >>> 32));
        hash = 71 * hash + Objects.hashCode(this.type);
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
        final Point other = (Point) obj;
        if (Double.doubleToLongBits(this.centreX) != Double.doubleToLongBits(other.centreX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.centreY) != Double.doubleToLongBits(other.centreY)) {
            return false;
        }
        if (Double.doubleToLongBits(this.widthAndHeight) != Double.doubleToLongBits(other.widthAndHeight)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    public double getUpperLeftX() {
        return this.centreX - this.widthAndHeight / 2;
    }

    public double getUpperLeftY() {
        return this.centreY - this.widthAndHeight / 2;
    }

    public double getCentreX() {
        return centreX;
    }

    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }

    public double getCentreY() {
        return centreY;
    }

    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }

    public double getWidthAndHeight() {
        return widthAndHeight;
    }

    public void setWidthAndHeight(double widthAndHeight) {
        this.widthAndHeight = widthAndHeight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        if(type==Type.POINT){
            this.widthAndHeight=2;
        }
        else{
            this.widthAndHeight=8;
        }
    }

}
