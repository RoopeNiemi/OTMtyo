/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.UI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.characters.Monster;
import pacmangame.pacman.characters.Player;
import pacmangame.pacman.logic.GameLogic;
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
    private GameLogic game = new GameLogic();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph map = game.getGraph();
        this.width = map.getGraphMatrix()[0].length * 20;
        this.height = map.getGraphMatrix().length * 20;
        Canvas c = new Canvas(width, height);
        BorderPane window = new BorderPane();
        window.setCenter(c);

        Scene scene = new Scene(window);
        scene.setOnMouseClicked(event -> {
            if (game.getGameOver()) {
                game.init();
            }
        });
        scene.setOnKeyPressed(event -> {
            if (this.keyIsPressed) {
                return;
            }
            this.keyIsPressed = true;
            if (null != event.getCode()) {

                switch (event.getCode()) {
                    case RIGHT:

                        game.getPlayer().setQueuedDirection(Direction.RIGHT);

                        break;
                    case LEFT:

                        game.getPlayer().setQueuedDirection(Direction.LEFT);

                        break;
                    case UP:

                        game.getPlayer().setQueuedDirection(Direction.UP);

                        break;
                    case DOWN:

                        game.getPlayer().setQueuedDirection(Direction.DOWN);

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
                if (!game.getGameOver()) {
                    prev = now;
                    if (!game.isInProgress()) {
                        game.movePlayer();
                        game.updateMonsters();
                        paintGame(c.getGraphicsContext2D(), game.getGraph());
                    }
                } else {
                    drawGameOverText(c.getGraphicsContext2D());
                }
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
                if (tile.getValue() == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getWidth());
                } else {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getWidth());
                }
            }
        }
        drawPlayer(gc);
        drawMonsters(gc);
    }

    public void drawPlayer(GraphicsContext gc) {
        gc.setFill(game.getPlayer().getColor());
        gc.fillOval(game.getPlayer().getX(), game.getPlayer().getY(), game.getPlayer().getWidth(), game.getPlayer().getWidth());
    }

    public void drawMonsters(GraphicsContext gc) {
        Monster toDraw = game.getBlue();
        gc.setFill(toDraw.getColor());
        if (toDraw.getBehaviourState()) {
            gc.setFill(Color.DARKBLUE);
        }
        gc.fillOval(toDraw.getX(), toDraw.getY(), toDraw.getWidth(), toDraw.getWidth());

        toDraw = game.getRed();
        gc.setFill(toDraw.getColor());
        if (toDraw.getBehaviourState()) {
            gc.setFill(Color.DARKBLUE);
        }
        gc.fillOval(toDraw.getX(), toDraw.getY(), toDraw.getWidth(), toDraw.getWidth());

        toDraw = game.getYellow();
        gc.setFill(toDraw.getColor());
        if (toDraw.getBehaviourState()) {
            gc.setFill(Color.DARKBLUE);
        }
        gc.fillOval(toDraw.getX(), toDraw.getY(), toDraw.getWidth(), toDraw.getWidth());

        toDraw = game.getOrange();
        gc.setFill(toDraw.getColor());
        if (toDraw.getBehaviourState()) {
            gc.setFill(Color.DARKBLUE);
        }

        gc.fillOval(toDraw.getX(), toDraw.getY(), toDraw.getWidth(), toDraw.getWidth());
    }

    public void drawGameOverText(GraphicsContext gc) {
        gc.setFont(new Font(50));
        gc.setFill(Color.RED);

        gc.fillText("GAME OVER", width / 4, height / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
