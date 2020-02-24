package pacmangame.pacman.ui;

import pacmangame.pacman.map.Point;
import pacmangame.pacman.map.Type;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pacmangame.pacman.characters.*;
import pacmangame.pacman.logic.*;
import pacmangame.pacman.map.*;

public class PacmanUi extends Application {

    private final Image scaredImage = new Image(getClass().getResourceAsStream("/scared.png"));
    private final Image healthLeft = new Image(getClass().getResourceAsStream("/pacmanHealth.png"));
    private final Image reset = new Image(getClass().getResourceAsStream("/reset.png"));
    private int totalPoints = 0;
    private double width = 400;
    private double scoreBoardHeight = 40;
    private double height = 400;
    private MapLoader mapLoader = new MapLoader();
    private GameLogic game = new GameLogic(mapLoader, totalPoints, 2);
    private Label pointLabel = new Label("0");
    private Label highScoreLabel;
    private Graph currentMap;
    private long updateFrequency = 25000000L;

    private void startOver() {
        this.totalPoints = 0;
        this.game = new GameLogic(mapLoader, 0, 2);
        this.currentMap = game.getGraph();
        game.getRed().setActive(true);
        highScoreLabel = new Label("HIGH SCORE: " + game.getGameState().getHighScore());
    }

    private void nextLevel() {
        int playerLivesLeft = game.getPlayer().getRemainingLife();
        this.totalPoints = game.getGameState().getPoints();
        game = game = new GameLogic(mapLoader, totalPoints, playerLivesLeft);
        currentMap = game.getGraph();
        game.getRed().setActive(true);
    }

    @Override
    public void init() {
        this.pointLabel.setText("0");
        currentMap = game.getGraph();
        game.getRed().setActive(true);
        this.highScoreLabel = new Label("HIGH SCORE: " + game.getGameState().getHighScore());
        this.width = currentMap.getMap()[0].length * 20;
        this.height = currentMap.getMap().length * 20;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas c = new Canvas(width, height + scoreBoardHeight * 2);
        BorderPane window = new BorderPane();
        window.setCenter(c);
        Scene scene = new Scene(window);

        setMouseClicked(scene);
        setKeyPressed(primaryStage, scene);
        initStage(primaryStage, scene);

        new AnimationTimer() {
            long prev = 0;

            @Override
            public void handle(long now) {
                if (now - prev < updateFrequency) {
                    return;
                }
                game.monsterActivation(updateFrequency);
                game.handleBehaviourUpdate(updateFrequency);
                prev = now;
                if (game.situationNormal()) {
                    game.movePlayer();
                    game.updateMonsters();
                    pointLabel.setText("" + game.getGameState().getPoints());
                    paintGame(c.getGraphicsContext2D(), game.getGraph(), game.getPlayer());
                } else {
                    if (game.getPlayer().gotHit()) {
                        game.handleLosingHitPoints();
                        paintGame(c.getGraphicsContext2D(), currentMap, game.getPlayer());
                        pointLabel.setText("" + game.getGameState().getPoints());
                    } else if (game.getGameState().isComplete()) {
                        nextLevel();
                    } else {
                        if (game.getGameState().isGameOver()) {
                            game.handleGameOver();
                            paintGame(c.getGraphicsContext2D(), currentMap, game.getPlayer());
                            drawGameOverText(c.getGraphicsContext2D());
                        }
                    }
                }
            }
        }.start();
    }

    private void initStage(Stage primaryStage, Scene scene) {
        primaryStage.setOnCloseRequest(event -> {
            if (game.getGameState().isGameOver()) {
                game.getGameState().saveNewHighScoreIfNeeded(game.getGameState().getPoints());
            }
        });
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }

    private void setKeyPressed(Stage stage, Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (game.getGameState().isGameOver()) {
                game.getGameState().saveNewHighScoreIfNeeded(game.getGameState().getPoints());
                if (event.getCode() == KeyCode.Y) {
                    startOver();
                } else if (event.getCode() == KeyCode.N) {
                    stage.close();
                }
            }
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
    }

    private void setMouseClicked(Scene scene) {
        scene.setOnMouseClicked(event -> {
            if (game.getGameState().isGameOver()) {
                startOver();
            }
        });
    }

    private void paintGame(GraphicsContext gc, Graph map, Player player) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height + scoreBoardHeight * 2);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", (20)));
        gc.fillText(this.pointLabel.getText(), 10, 30);
        gc.fillText(highScoreLabel.getText(), 120, 30);
        drawRemainingHealth(gc, player);
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                Tile tile = map.getMap()[i][j];
                if (tile.getValue() == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(tile.getX(), tile.getY() + scoreBoardHeight, tile.getWidth(), tile.getWidth());
                } else {
                    gc.setFill(Color.CORNFLOWERBLUE);
                    gc.fillRect(tile.getX(), tile.getY() + scoreBoardHeight, tile.getWidth(), tile.getWidth());
                }
            }
        }
        drawPoints(gc);
        drawPlayer(gc);
        drawMonsters(gc);
    }

    private void drawRemainingHealth(GraphicsContext gc, Player player) {
        double x = 10;
        for (int i = 1; i <= player.getRemainingLife(); i++) {
            gc.drawImage(healthLeft, x, this.height + scoreBoardHeight + 5);
            x += 40;
        }
    }

    private void drawPlayer(GraphicsContext gc) {
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

    private void drawMonsters(GraphicsContext gc) {
        drawSingleMonster(gc, game.getRed());
        drawSingleMonster(gc, game.getBlue());
        drawSingleMonster(gc, game.getPink());
        drawSingleMonster(gc, game.getOrange());
    }

    private void drawSingleMonster(GraphicsContext gc, Monster monster) {
        if (monster.getCurrentBehaviour() == Behaviour.PANIC) {
            gc.drawImage(scaredImage, monster.getX(), monster.getY() + scoreBoardHeight);
            return;
        } else if (monster.getCurrentBehaviour() == Behaviour.RESET) {
            gc.drawImage(reset, monster.getX(), monster.getY() + scoreBoardHeight);
            return;
        }
        gc.drawImage(monster.getCurrentImage(), monster.getX(), monster.getY() + scoreBoardHeight);
    }

    private void drawGameOverText(GraphicsContext gc) {
        gc.setFont(Font.font("Courier New", (50)));
        gc.setFill(Color.YELLOW);
        gc.fillText("GAME OVER", width / 6, height / 2 + scoreBoardHeight);
        gc.setFont(Font.font("Courier New", (20)));
        gc.fillText("Continue? (Y/N)", width / 3, height / 2 + scoreBoardHeight + 25);
    }

    private void drawPoints(GraphicsContext gc) {
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
