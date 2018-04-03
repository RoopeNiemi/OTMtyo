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
        if (startingPoint.getValue() == 0 || startingPoint == dest) {
            return this.shortestPath;
        }
        while (!tileQueue.isEmpty()) {
            Tile tile = tileQueue.pollFirst();
            if (tile == dest) {
                this.shortestPath.push(tile);
                break;
            }
            int x = (int) Math.floor(tile.getX() / 20);
            int y = (int) Math.floor(tile.getY() / 20);

            //Fixed position, transition to  [9][18]
            if (x == 0 && y == 9) {
                if (!this.visited.contains(map[9][18])) {
                    checkPathAvailability(map[9][18], tile);
                    tileQueue.addLast(map[9][18]);
                }
            }
            //Fixed position, transition to [9][0]
            if (x == 18 && y == 9) {
                if (!this.visited.contains(map[9][0])) {
                    checkPathAvailability(map[9][0], tile);
                    tileQueue.addLast(map[9][0]);
                }
            }
            //Left side tile is unvisited and has value 1
            if (x > 0) {
                if (checkIfDestination(tile, map[y][x - 1], dest)) {
                    break;
                }
                if (!this.visited.contains(map[y][x - 1]) && map[y][x - 1].getValue() == 1) {
                    checkPathAvailability(map[y][x - 1], tile);
                    tileQueue.addLast(map[y][x - 1]);
                }
            }
            //Right side tile is unvisited and has value 1
            if (x < map[0].length - 1) {
                if (checkIfDestination(tile, map[y][x + 1], dest)) {
                    break;
                }
                if (!this.visited.contains(map[y][x + 1]) && map[y][x + 1].getValue() == 1) {
                    checkPathAvailability(map[y][x + 1], tile);
                    tileQueue.addLast(map[y][x + 1]);
                }
            }
            //Up side tile is unvisited and has value 1
            if (y > 0) {
                if (checkIfDestination(tile, map[y - 1][x], dest)) {
                    break;
                }
                if (!this.visited.contains(map[y - 1][x]) && map[y - 1][x].getValue() == 1) {
                    checkPathAvailability(map[y - 1][x], tile);
                    tileQueue.addLast(map[y - 1][x]);
                }
            }
            // Down side tile is unvisited and has value 1
            if (y < map.length - 1) {
                if (checkIfDestination(tile, map[y + 1][x], dest)) {
                    break;
                }
                if (!this.visited.contains(map[y + 1][x]) && map[y + 1][x].getValue() == 1) {
                    checkPathAvailability(map[y + 1][x], tile);
                    tileQueue.addLast(map[y + 1][x]);
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

    private boolean checkIfDestination(Tile tile, Tile adjacentTile, Tile dest) {
        if (adjacentTile.getValue() == 0 && adjacentTile == dest) {
            shortestPath.push(tile);
            return true;
        }
        return false;
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
        while (begin != null && begin.getPathFrom() != start) {
            begin = begin.getPathFrom();
            shortestPath.push(begin);
        }
    }
}
