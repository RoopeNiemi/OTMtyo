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

    public Stack<Tile> findPath(Tile startingPoint, Tile[][] map, Tile dest) {
        this.visited.clear();
        this.tileQueue.clear();
        this.shortestPath.clear();
        tileQueue.add(startingPoint);
        if (startingPoint.getValue() == 0 || dest.getValue() == 0) {
            return this.shortestPath;
        }
        if (startingPoint == dest) {
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

            if (tilePosX > 0 && map[tilePosY][tilePosX - 1].getValue() == 1.0) {
                if (map[tilePosY][tilePosX - 1].getPathFrom() == null) {
                    map[tilePosY][tilePosX - 1].setPathFrom(tile);
                }
                if (!this.visited.contains(map[tilePosY][tilePosX - 1])) {
                    tileQueue.addLast(map[tilePosY][tilePosX - 1]);

                }

            }
            if (tilePosX < map[0].length - 1 && map[tilePosY][tilePosX + 1].getValue() == 1.0) {

                if (!this.visited.contains(map[tilePosY][tilePosX + 1])) {
                    if (map[tilePosY][tilePosX + 1].getPathFrom() == null) {
                        map[tilePosY][tilePosX + 1].setPathFrom(tile);
                    }
                    tileQueue.addLast(map[tilePosY][tilePosX + 1]);

                }
            }
            if (tilePosY > 0 && map[tilePosY - 1][tilePosX].getValue() == 1.0) {

                if (!this.visited.contains(map[tilePosY - 1][tilePosX])) {
                    if (map[tilePosY - 1][tilePosX].getPathFrom() == null) {
                        map[tilePosY - 1][tilePosX].setPathFrom(tile);
                    }

                    tileQueue.addLast(map[tilePosY - 1][tilePosX]);
                }
            }
            if (tilePosY < map.length - 1 && map[tilePosY + 1][tilePosX].getValue() == 1.0) {

                if (!this.visited.contains(map[tilePosY + 1][tilePosX])) {
                    if (map[tilePosY + 1][tilePosX].getPathFrom() == null) {
                        map[tilePosY + 1][tilePosX].setPathFrom(tile);
                    }
                    tileQueue.addLast(map[tilePosY + 1][tilePosX]);

                }
            }
            this.visited.add(tile);

        }
        getPath(startingPoint);
        resetMap(map);
        return this.shortestPath;
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
