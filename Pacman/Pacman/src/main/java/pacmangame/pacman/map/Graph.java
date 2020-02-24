package pacmangame.pacman.map;

import java.util.ArrayList;
import java.util.List;

import pacmangame.pacman.characters.Direction;

public class Graph {

    private Tile[][] map;
    private ArrayList<Tile> corridorTiles = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();

    public Graph(List<String> map) {
        this.map = new Tile[map.size()][map.get(0).length()];
        initGraph(map);
    }

    /**
     * Generates points on the map. Also generates Power pellets to fixed
     * locations on map.
     */
    private void generatePointsAndFruits() {
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[0].length; j++) {
                // Monster spawning zone
                if (i >= 8 && i <= 9 && j >= 7 && j <= 11) {
                    continue;
                }
                Tile t = this.map[i][j];
                if (t.getValue() == 1) {
                    //Generate points to the middle of a tile
                    double tileCentreX = t.getX() + t.getWidth() / 2;
                    double tileCentreY = t.getY() + t.getWidth() / 2;
                    Point p = new Point(tileCentreX, tileCentreY, Type.POINT);
                    points.add(p);
                    t.addTilePoint(p);

                    if (i > 0) {
                        if (this.map[i - 1][j].getValue() == 1) {
                            tileCentreX = t.getX() + t.getWidth() / 2;
                            double tileY = t.getY();
                            p = new Point(tileCentreX, tileY, Type.POINT);
                            points.add(p);
                            t.addTilePoint(p);
                        }
                    }
                    if (j > 0) {
                        if (this.map[i][j - 1].getValue() == 1) {
                            double tileX = t.getX();
                            tileCentreY = t.getY() + t.getWidth() / 2;
                            p = new Point(tileX, tileCentreY, Type.POINT);
                            points.add(p);
                            t.addTilePoint(p);
                        }
                    }
                }
            }
        }
        int temp = 0;
        int oneFourth = points.size() / 4;
        //This is just for tests so they work properly.
        if (points.size() < 100) {
            points.get(temp).setType(Type.POWER_PELLET);
            temp += oneFourth;

            points.get(temp).setType(Type.POWER_PELLET);
            temp += oneFourth;
            points.get(temp).setType(Type.POWER_PELLET);
            temp += oneFourth;
            points.get(temp).setType(Type.POWER_PELLET);
            return;
        }
        //This is for the game itself.
        points.get(temp).setType(Type.POWER_PELLET);
        temp += 78;
        points.get(temp).setType(Type.POWER_PELLET);
        temp += 2 * oneFourth;
        points.get(temp).setType(Type.POWER_PELLET);
        temp += 15;
        points.get(temp).setType(Type.POWER_PELLET);
    }

    /**
     * @return List of tiles that are not walls.
     */
    public ArrayList<Tile> getCorridorTiles() {
        return this.corridorTiles;
    }

    /**
     * Checks if moving to a given direction is possible. Has special cases for
     * player transferring from one side of the map to another.
     *
     * @param playerXCoordinate Player x-coordinate
     * @param playerYCoordinate Player y-coordinate
     * @param dir               Player direction
     * @return True if moving to given direction is possible.
     */
    public boolean checkTurn(double playerXCoordinate, double playerYCoordinate, Direction dir) {
        int yTile = (int) Math.floor(playerYCoordinate / Tile.TILE_WIDTH);
        int xTile = (int) Math.floor(playerXCoordinate / Tile.TILE_WIDTH);

        if (null != dir) {
            switch (dir) {
                case DOWN:
                    if (playerInCentreOfTile(playerXCoordinate, playerYCoordinate, xTile, yTile)) {
                        if (yTile < this.map.length - 1 && this.map[yTile + 1][xTile].getValue() == 1) {
                            return true;
                        }
                    } else if (yTile < this.map.length - 1 && playerYCoordinate < this.map[yTile + 1][xTile].getY()) {
                        return playerXCoordinate == this.map[yTile][xTile].getX();
                    }

                    break;
                case UP:

                    if (playerInCentreOfTile(playerXCoordinate, playerYCoordinate, xTile, yTile)) {
                        if (yTile > 0 && this.map[yTile - 1][xTile].getValue() == 1) {
                            return true;
                        }
                    } else if (yTile > 0 && playerYCoordinate > this.map[yTile - 1][xTile].getY()) {
                        return playerXCoordinate == this.map[yTile][xTile].getX();
                    }
                    break;
                case LEFT:

                    if (yTile == 9 && xTile == 0) {
                        return true;
                    }
                    if (playerInCentreOfTile(playerXCoordinate, playerYCoordinate, xTile, yTile)) {
                        if (xTile > 0 && this.map[yTile][xTile - 1].getValue() == 1) {
                            return true;
                        }
                    } else if (xTile > 0 && playerXCoordinate > this.map[yTile][xTile - 1].getX()) {
                        return playerYCoordinate == this.map[yTile][xTile].getY();
                    }

                    break;
                case RIGHT:
                    if (yTile == 9 && xTile == 18) {
                        return true;
                    }
                    if (playerInCentreOfTile(playerXCoordinate, playerYCoordinate, xTile, yTile)) {
                        if (xTile < this.map[0].length - 1 && this.map[yTile][xTile + 1].getValue() == 1) {
                            return true;
                        }
                    } else if (xTile < this.map[0].length - 1 && playerXCoordinate < this.map[yTile][xTile + 1].getX()) {
                        return playerYCoordinate == this.map[yTile][xTile].getY();
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    private boolean playerInCentreOfTile(double playerXCoordinate, double playerYCoordinate, int xTile, int yTile) {
        return playerYCoordinate == this.map[yTile][xTile].getY() && playerXCoordinate == this.map[yTile][xTile].getX();
    }

    /**
     * @return List of all points left on map
     */
    public ArrayList<Point> getPointsList() {
        return this.points;
    }

    private void initGraph(List<String> map) {
        corridorTiles.clear();
        double tileWidth = 20;
        for (int i = 0; i < map.size(); i++) {
            char[] characters = map.get(i).toCharArray();
            for (int j = 0; j < characters.length; j++) {
                if (characters[j] == '1') {
                    Tile t = new Tile(j * tileWidth, i * tileWidth, tileWidth, 1);
                    this.map[i][j] = t;
                    corridorTiles.add(t);
                } else {
                    this.map[i][j] = new Tile(j * tileWidth, i * tileWidth, tileWidth, 0);
                }
            }
        }
        this.points.clear();
        generatePointsAndFruits();
    }

    /**
     * @return Starting tile of red monster.
     */
    public Tile getRedStartingTile() {
        return this.map[7][9];
    }

    /**
     * @return Starting tile of pink monster.
     */
    public Tile getPinkStartingTile() {
        return this.map[9][8];
    }

    /**
     * @return Starting tile of blue monster.
     */
    public Tile getBlueStartingTile() {
        return this.map[9][9];
    }

    /**
     * @return Starting tile of orange monster.
     */
    public Tile getOrangeStartingTile() {
        return this.map[9][10];
    }

    /**
     * @return A two dimensional array of tiles representing the map.
     */
    public Tile[][] getMap() {
        return this.map;
    }

    /**
     * @return Tile on the bottom right of map that is not a wall.
     */
    public Tile getBottomRightTile() {
        return this.map[19][17];
    }

    /**
     * @return Tile on the top right of the map that is not a wall.
     */
    public Tile getTopRightTile() {
        return this.map[1][17];
    }

    /**
     * @return Tile on the bottom left of the map that is not a wall.
     */
    public Tile getBottomLeftTile() {
        return this.map[19][1];
    }

    /**
     * @return Tile on the top left of the map that is not a wall.
     */
    public Tile getTopLeftTile() {
        return this.map[1][1];
    }

    public Tile getRightSideTransitionTile() {
        return this.map[9][18];
    }

    public Tile getLeftSideTransitionTile() {
        return this.map[9][0];
    }
}
