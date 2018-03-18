/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.UI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.characters.Player;
import pacmangame.pacman.map.Graph;
import pacmangame.pacman.map.Tile;

/**
 *
 * @author User
 */
public class UI extends Application {

    private double width = 400;
    private double height = 400;
    private boolean keyIsPressed = false;

    private List<String> loadMap(String path) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        Scanner fileScanner = new Scanner(is);
        List<String> map = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            System.out.println(line);
            map.add(line);
        }
        return map;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph map = new Graph(loadMap("map1.txt"));
        Player player = new Player(20, 20);
        this.width = map.getGraphMatrix().length * 20;
        this.height = map.getGraphMatrix()[0].length * 20;
        Canvas c = new Canvas(width, height);
        BorderPane window = new BorderPane();
        window.setCenter(c);

        Scene scene = new Scene(window);
        scene.setOnKeyPressed(event -> {
            if (this.keyIsPressed) {
                return;
            }
            this.keyIsPressed = true;
            if (null != event.getCode()) {

                switch (event.getCode()) {
                    case RIGHT:

                        player.setQueuedDirection(Direction.RIGHT);

                        break;
                    case LEFT:

                        player.setQueuedDirection(Direction.LEFT);

                        break;
                    case UP:

                        player.setQueuedDirection(Direction.UP);

                        break;
                    case DOWN:

                        player.setQueuedDirection(Direction.DOWN);

                        break;

                }
            }
            System.out.println("Direction set");
        });
        scene.setOnKeyReleased(event -> {
            this.keyIsPressed = false;
        });

        new AnimationTimer() {
            long prev = 0;

            @Override
            public void handle(long now) {
                if (now - prev < 25000000) {
                    return;
                }
                prev = now;
                player.move();
                if (map.checkTurn(player.getX(), player.getY(), player.getQueuedDirection())) {
                    player.setMovementDirection(player.getQueuedDirection());
                    player.setQueuedDirection(Direction.NOT_MOVING);
                }
                if (!map.checkTurn(player.getX(), player.getY(), player.getMovementDirection())) {
                    player.setMovementDirection(Direction.NOT_MOVING);
                }

                paintGame(c.getGraphicsContext2D(), map);
                drawPlayer(c.getGraphicsContext2D(), player);
            }
        }.start();
        primaryStage.setScene(scene);

        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }

    public void paintGame(GraphicsContext gc, Graph map) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        for (int i = 0; i < map.getGraphMatrix().length; i++) {
            for (int j = 0; j < map.getGraphMatrix()[0].length; j++) {
                Tile tile = map.getGraphMatrix()[i][j];
                if (tile.getValue() == 1.0) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getWidth());
                } else {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getWidth());
                }
            }
        }
    }

    public void drawPlayer(GraphicsContext gc, Player player) {
        gc.setFill(player.getColor());
        gc.fillOval(player.getX(), player.getY(), player.getWidth(), player.getWidth());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
