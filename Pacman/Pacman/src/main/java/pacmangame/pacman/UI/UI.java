/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.UI;

import java.awt.geom.Arc2D;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import pacmangame.pacman.characters.Direction;
import pacmangame.pacman.characters.Monster;
import pacmangame.pacman.logic.GameLogic;
import pacmangame.pacman.logic.PlayerResetTimer;
import pacmangame.pacman.map.Graph;
import pacmangame.pacman.map.MapLoader;
import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Tile;
import pacmangame.pacman.map.Type;

/**
 *
 * @author User
 */
public class UI extends Application {

    private final Image redImage = new Image(getClass().getResourceAsStream("/red.png"));
    private final Image orangeImage = new Image(getClass().getResourceAsStream("/orange.png"));
    private final Image blueImage = new Image(getClass().getResourceAsStream("/blue.png"));
    private final Image yellowImage = new Image(getClass().getResourceAsStream("/yellow.png"));
    private PlayerResetTimer timer = new PlayerResetTimer();
    private double width = 400;
    private double scoreBoardHeight = 40;
    private double height = 400;
    private boolean keyIsPressed = false;
    private MapLoader mapLoader = new MapLoader();
    private GameLogic game = new GameLogic(mapLoader,timer);
    private Label pointLabel = new Label("Points: 0");
    private Label lifeLabel = new Label("HP: 3");
    private Graph currentMap;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.pointLabel.setText("Points: 0");
        this.lifeLabel.setText("HP: 3");
        currentMap = game.getGraph();
        this.width = currentMap.getGraphMatrix()[0].length * 20;
        this.height = currentMap.getGraphMatrix().length * 20;
        Canvas c = new Canvas(width, height + scoreBoardHeight);
        BorderPane window = new BorderPane();
        window.setCenter(c);

        Scene scene = new Scene(window);
        scene.setOnMouseClicked(event -> {
            if (game.getGameOver()) {
                game = new GameLogic(mapLoader,timer);
                currentMap = game.getGraph();
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
                if(timer.addTime(25000000)){
                    game.getPlayer().setMortality(true);
                }
                if (!game.getGameOver() && !game.getPlayer().getLostHitPoint()) {
                    prev = now;
                    if (!game.isInProgress()) {
                        lifeLabel.setText("HP: " + game.getPlayer().getRemainingLife());
                        game.movePlayer();
                        game.updateMonsters();
                        paintGame(c.getGraphicsContext2D(), game.getGraph());

                    }

                } else {
                    if (game.getPlayer().getLostHitPoint()) {
                        game.getPlayer().loseHitPoints(timer);
                        paintGame(c.getGraphicsContext2D(), currentMap);
                        pointLabel.setText("Points: " + game.getPointAmount());
                    } else {
                        if (game.getGameOver()) {
                            lifeLabel.setText("HP: 0");
                            paintGame(c.getGraphicsContext2D(), currentMap);
                            drawGameOverText(c.getGraphicsContext2D());
                        }
                    }
                }
            }
        }.start();
        primaryStage.setScene(scene);

        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }

    public void paintGame(GraphicsContext gc, Graph map) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height + 30);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText(this.pointLabel.getText(), 20, 30);
        gc.fillText(this.lifeLabel.getText(), 150, 30);
        for (int i = 0; i < map.getGraphMatrix().length; i++) {
            for (int j = 0; j < map.getGraphMatrix()[0].length; j++) {
                Tile tile = map.getGraphMatrix()[i][j];
                if (tile.getValue() == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(tile.getX(), tile.getY() + scoreBoardHeight, tile.getWidth(), tile.getWidth());
                } else {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(tile.getX(), tile.getY() + scoreBoardHeight, tile.getWidth(), tile.getWidth());
                }
            }
        }
        drawPoints(gc);
        drawPlayer(gc);
        drawMonsters(gc);
    }

    public void drawPlayer(GraphicsContext gc) {
        gc.setFill(game.getPlayer().getColor());
        double currentAngle = game.getPlayer().getMouthAngle();
        Direction currentDirection = game.getPlayer().getMovementDirection();
        if (currentDirection == Direction.NOT_MOVING) {
            currentDirection = game.getPlayer().getPreviousDirection();
        }
        if (null != currentDirection) {
            switch (currentDirection) {
                case RIGHT:
                    break;
                case LEFT:
                    currentAngle += 180;
                    break;
                case UP:
                    currentAngle += 90;
                    break;
                case DOWN:
                    currentAngle -= 90;
                    break;
                default:
                    break;
            }
        }
        gc.fillArc(game.getPlayer().getX(), game.getPlayer().getY() + scoreBoardHeight, game.getPlayer().getWidth(), game.getPlayer().getWidth(), currentAngle, 360 - game.getPlayer().getMouthAngle() * 2, ArcType.ROUND);
    }

    public void drawMonsters(GraphicsContext gc) {
        Monster toDraw = game.getBlue();
        gc.drawImage(blueImage, toDraw.getX(), toDraw.getY() + scoreBoardHeight);

        toDraw = game.getRed();
        gc.drawImage(redImage, toDraw.getX(), toDraw.getY() + scoreBoardHeight);

        toDraw = game.getYellow();
        gc.drawImage(yellowImage, toDraw.getX(), toDraw.getY() + scoreBoardHeight);

        toDraw = game.getOrange();
        gc.drawImage(orangeImage, toDraw.getX(), toDraw.getY() + scoreBoardHeight);
    }

    public void drawGameOverText(GraphicsContext gc) {
        gc.setFont(new Font(50));
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", width / 4, height / 2 + scoreBoardHeight);
    }

    public void drawPoints(GraphicsContext gc) {
        ArrayList<Point> points = game.getGraph().getPointsList();
        gc.setFill(new Point(0, 0, Type.POINT).getColor());
        for (int i = 0; i < points.size(); i++) {
            gc.fillOval(points.get(i).getUpperLeftX(), points.get(i).getUpperLeftY() + scoreBoardHeight, points.get(i).getWidthAndHeight(), points.get(i).getWidthAndHeight());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
