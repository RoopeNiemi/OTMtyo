/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.pathfinding;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Stack;
import pacmangame.pacman.map.Tile;

/**
 *
 * @author User
 */
public class Pathfinder {

    private ArrayDeque<Tile> tileQueue = new ArrayDeque<>();
    private Stack<Tile> shortestPath = new Stack<>();
    private HashSet<Tile> visited = new HashSet<>();

    private void resetSearches() {
        this.visited.clear();
        this.tileQueue.clear();
        this.shortestPath.clear();
    }

    public Stack<Tile> findPath(Tile startingPoint, Tile[][] map, Tile dest) {
        resetSearches();
        tileQueue.add(startingPoint);
        if (startingPoint.getValue() == 0 || dest.getValue() == 0 || startingPoint == dest) {
            return this.shortestPath;
        }
        while (!tileQueue.isEmpty()) {
            Tile tile = tileQueue.pollFirst();
            if (tile == dest) {
                this.shortestPath.push(tile);
                break;
            }
            int tilePosX = (int) Math.floor(tile.getX() / 20);
            int tilePosY = (int) Math.floor(tile.getY() / 20);
            
            //Fixed position, transition to  [9][17]
            if (tilePosX == 0 && tilePosY == 9) {
                if (!this.visited.contains(map[9][17])) {
                    checkPathAvailability(map[9][17], tile);
                    tileQueue.addLast(map[9][17]);
                }
            }
            //Fixed position, transition to [9][0]
            if (tilePosX == 17 && tilePosY == 9) {
                if (!this.visited.contains(map[9][0])) {
                    checkPathAvailability(map[9][0], tile);
                    tileQueue.addLast(map[9][0]);
                }
            }
            //Left side tile is unvisited and has value 1
            if (tilePosX > 0 && map[tilePosY][tilePosX - 1].getValue() == 1.0) {
                if (!this.visited.contains(map[tilePosY][tilePosX - 1])) {
                    checkPathAvailability(map[tilePosY][tilePosX - 1], tile);
                    tileQueue.addLast(map[tilePosY][tilePosX - 1]);
                }
            }
            //Right side tile is unvisited and has value 1
            if (tilePosX < map[0].length - 1 && map[tilePosY][tilePosX + 1].getValue() == 1.0) {
                if (!this.visited.contains(map[tilePosY][tilePosX + 1])) {
                    checkPathAvailability(map[tilePosY][tilePosX + 1], tile);
                    tileQueue.addLast(map[tilePosY][tilePosX + 1]);
                }
            }
            //Up side tile is unvisited and has value 1
            if (tilePosY > 0 && map[tilePosY - 1][tilePosX].getValue() == 1.0) {
                if (!this.visited.contains(map[tilePosY - 1][tilePosX])) {
                    checkPathAvailability(map[tilePosY - 1][tilePosX], tile);
                    tileQueue.addLast(map[tilePosY - 1][tilePosX]);
                }
            }
            // Down side tile is unvisited and has value 1
            if (tilePosY < map.length - 1 && map[tilePosY + 1][tilePosX].getValue() == 1.0) {
                if (!this.visited.contains(map[tilePosY + 1][tilePosX])) {
                    checkPathAvailability(map[tilePosY + 1][tilePosX], tile);
                    tileQueue.addLast(map[tilePosY + 1][tilePosX]);
                }
            }
            this.visited.add(tile);
        }
        getPath(startingPoint);
        resetMap(map);
        return this.shortestPath;
    }

    private void checkPathAvailability(Tile to, Tile from) {
        if (to.getPathFrom() == null) {
            to.setPathFrom(from);
        }
    }

    public void resetMap(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].setPathFrom(null);
            }
        }
    }

    private void getPath(Tile start) {
        if (this.shortestPath.isEmpty()) {
            return;
        }
        Tile begin = this.shortestPath.peek();
        while (begin.getPathFrom() != start) {
            begin = begin.getPathFrom();
            shortestPath.push(begin);
        }
    }
}
