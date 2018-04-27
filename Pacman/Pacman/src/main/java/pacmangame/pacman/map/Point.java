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

    /**
     *
     * @param x x-coordinate of the point.
     * @param y y-coordinate of the point.
     * @param type Type of the point.
     */
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

    /**
     *
     * @return generated hashcode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.centreX) ^ (Double.doubleToLongBits(this.centreX) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.centreY) ^ (Double.doubleToLongBits(this.centreY) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.widthAndHeight) ^ (Double.doubleToLongBits(this.widthAndHeight) >>> 32));
        hash = 71 * hash + Objects.hashCode(this.type);
        return hash;
    }

    /**
     *
     * @param obj Object that the point is compared to
     * @return True if given object and this point are the same, else false.
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

    /**
     *
     * @return x-coordinate of the upper left corner of the point.
     */
    public double getUpperLeftX() {
        return this.centreX - this.widthAndHeight / 2;
    }

    /**
     *
     * @return y-coordinate of the upper left corner of the point.
     */
    public double getUpperLeftY() {
        return this.centreY - this.widthAndHeight / 2;
    }

    /**
     *
     * @return x-coordinate of the centre of the point.
     */
    public double getCentreX() {
        return centreX;
    }

    /**
     *
     * @param centreX new x-coordinate of the centre of the point.
     */
    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }

    /**
     *
     * @return y-coordinate of the centre of the point.
     */
    public double getCentreY() {
        return centreY;
    }

    /**
     *
     * @param centreY new y-coordinate of the centre of the point.
     */
    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }

    /**
     *
     * @return width and height of the point.
     */
    public double getWidthAndHeight() {
        return widthAndHeight;
    }

    /**
     *
     * @param widthAndHeight new width and height of the point.
     */
    public void setWidthAndHeight(double widthAndHeight) {
        this.widthAndHeight = widthAndHeight;
    }

    /**
     *
     * @return Color of the point.
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return Type of this point. A point can be either of type POINT or type
     * POWER_PELLET.
     */
    public Type getType() {
        return type;
    }

    /**
     *
     * @param type New type of the point. Changes point's width and height
     * according to the type given.
     */
    public void setType(Type type) {
        this.type = type;
        if (type == Type.POINT) {
            this.widthAndHeight = 2;
        } else {
            this.widthAndHeight = 8;
        }
    }

}
