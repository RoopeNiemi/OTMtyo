/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.characters.Monster;

/**
 *
 * @author User
 */
public class Graph {

    private int width, height;
    private Tile[][] graphMatrix;
    private ArrayList<Tile> movableTiles = new ArrayList<Tile>();
    private ArrayList<Point> points = new ArrayList<>();

    public Graph(List<String> map) {
        this.width = map.get(1).length();
        this.height = map.size();
        this.graphMatrix = new Tile[this.height][this.width];
        initGraph(map);
    }

    public void resetPaths() {
        for (int i = 0; i < this.graphMatrix.length; i++) {
            for (int j = 0; j < this.graphMatrix[0].length; j++) {
                this.graphMatrix[i][j].setPathFrom(null);
            }
        }
    }

    public void generatePointsAndFruits() {
        for (int i = 0; i < this.graphMatrix.length; i++) {
            for (int j = 0; j < this.graphMatrix[0].length; j++) {
                Tile t = this.graphMatrix[i][j];
                if (t.getValue() == 1) {
                    //Tile centre x and y
                    double tileCentreX = t.getX() + t.getWidth() / 2;
                    double tileCentreY = t.getY() + t.getWidth() / 2;
                    Point p = new Point(tileCentreX, tileCentreY, Type.POINT);
                    points.add(p);
                    t.addTilePoint(p);

                    if (i > 0) {
                        if (this.graphMatrix[i - 1][j].getValue() == 1) {
                            tileCentreX = t.getX() + t.getWidth() / 2;
                            double tileY = t.getY();
                            p = new Point(tileCentreX, tileY, Type.POINT);
                            points.add(p);
                            t.addTilePoint(p);
                        }
                    }
                    if (j > 0) {
                        if (this.graphMatrix[i][j - 1].getValue() == 1) {
                            double tileX = t.getX();
                            tileCentreY = t.getY() + t.getWidth() / 2;
                            p=new Point(tileX,tileCentreY,Type.POINT);
                            points.add(p);
                            t.addTilePoint(p);
                        }
                    }
                }
            }
        }

    }

    public ArrayList<Tile> getMovableTiles() {
        return this.movableTiles;
    }

    public boolean checkTurn(double x, double y, Direction dir) {
        int yCrd = (int) Math.floor(y / 20);
        int xCrd = (int) Math.floor(x / 20);

        if (null != dir) {
            switch (dir) {
                case DOWN:
                    if (y == this.graphMatrix[yCrd][xCrd].getY() && x == this.graphMatrix[yCrd][xCrd].getX()) {
                        if (yCrd < this.graphMatrix.length - 1 && this.graphMatrix[yCrd + 1][xCrd].getValue() == 1) {
                            return true;
                        }
                    } else if (yCrd < this.graphMatrix.length - 1 && y < this.graphMatrix[yCrd + 1][xCrd].getY()) {
                        if (x != this.graphMatrix[yCrd][xCrd].getX()) {
                            return false;
                        }
                        return true;
                    }

                    break;
                case UP:

                    if (y == this.graphMatrix[yCrd][xCrd].getY() && x == this.graphMatrix[yCrd][xCrd].getX()) {
                        if (yCrd > 0 && this.graphMatrix[yCrd - 1][xCrd].getValue() == 1) {
                            return true;
                        }
                    } else if (yCrd > 0 && y > this.graphMatrix[yCrd - 1][xCrd].getY()) {
                        if (x != this.graphMatrix[yCrd][xCrd].getX()) {
                            return false;
                        }
                        return true;
                    }
                    break;
                case LEFT:

                    if (yCrd == 9 && xCrd == 0) {
                        return true;
                    }
                    if (x == this.graphMatrix[yCrd][xCrd].getX() && y == this.graphMatrix[yCrd][xCrd].getY()) {
                        if (xCrd > 0 && this.graphMatrix[yCrd][xCrd - 1].getValue() == 1) {
                            return true;
                        }
                    } else if (xCrd > 0 && x > this.graphMatrix[yCrd][xCrd - 1].getX()) {
                        if (y != this.graphMatrix[yCrd][xCrd].getY()) {
                            return false;
                        }
                        return true;
                    }

                    break;
                case RIGHT:
                    if (yCrd == 9 && xCrd == 17) {
                        return true;
                    }
                    if (x == this.graphMatrix[yCrd][xCrd].getX() && y == this.graphMatrix[yCrd][xCrd].getY()) {
                        if (xCrd < this.graphMatrix[0].length - 1 && this.graphMatrix[yCrd][xCrd + 1].getValue() == 1) {
                            return true;
                        }
                    } else if (xCrd < this.graphMatrix[0].length - 1 && x < this.graphMatrix[yCrd][xCrd + 1].getX()) {
                        if (y != this.graphMatrix[yCrd][xCrd].getY()) {
                            return false;
                        }
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public ArrayList<Point> getPointsList() {
        return this.points;
    }

    private void initGraph(List<String> map) {
        movableTiles.clear();
        double tileWidth = 20;
        for (int i = 0; i < map.size(); i++) {
            char[] characters = map.get(i).toCharArray();
            for (int j = 0; j < characters.length; j++) {
                if (characters[j] == '1') {
                    Tile t = new Tile(j * tileWidth, i * tileWidth, tileWidth, 1);
                    graphMatrix[i][j] = t;
                    movableTiles.add(t);
                } else {
                    graphMatrix[i][j] = new Tile(j * tileWidth, i * tileWidth, tileWidth, 0);
                }
            }
        }
        this.points.clear();
        generatePointsAndFruits();
    }

    public Tile[][] getGraphMatrix() {
        return this.graphMatrix;
    }
}
