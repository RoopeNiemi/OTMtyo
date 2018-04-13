/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.ui;

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
import javafx.stage.Stage;
import pacmangame.pacman.characters.*;
import pacmangame.pacman.logic.*;
import pacmangame.pacman.map.*;

/**
 *
 * @author User
 */
public class UI extends Application {

    private final Image scaredImage = new Image(getClass().getResourceAsStream("/scared.png"));
    private final Image healthLeft = new Image(getClass().getResourceAsStream("/pacmanHealth.png"));
    private final Image reset = new Image(getClass().getResourceAsStream("/reset.png"));
    private int totalPoints = 0;
    private double width = 400;
    private double scoreBoardHeight = 40;
    private double height = 400;
    private boolean keyIsPressed = false;
    private MapLoader mapLoader = new MapLoader();
    private GameLogic game = new GameLogic(mapLoader, totalPoints, 2);
    private Label pointLabel = new Label("POINTS: 0");
    private Label highScoreLabel;
    private Graph currentMap;
    private long panicPhaseLength = 5000000000L;
    private long normalMonsterBehaviourLength = 20000000000L;
    private long scatterBehaviourLength = 7000000000L;
    private long updateFrequency = 25000000L;

    private void startOver() {
        this.totalPoints = game.getSituation().getPoints();
        game.getSituation().saveNewHighScoreIfNeeded(totalPoints);
        this.totalPoints = 0;
        this.game = new GameLogic(mapLoader, 0, 2);
        this.currentMap = game.getGraph();
        game.getRed().activate();
        highScoreLabel = new Label("HIGH SCORE: " + game.getSituation().getCurrentHighScore());
    }

    private void nextLevel() {
        int playerLivesLeft = game.getPlayer().getRemainingLife();
        this.totalPoints = game.getSituation().getPoints();
        game = game = new GameLogic(mapLoader, totalPoints, playerLivesLeft);
        currentMap = game.getGraph();
        game.getRed().activate();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        game.getMonsterBehaviourTimer().setThreshold(normalMonsterBehaviourLength);
        game.getMonsterBehaviourTimer().activate();
        this.pointLabel.setText("POINTS: 0");
        currentMap = game.getGraph();
        game.getRed().activate();
        this.highScoreLabel = new Label("HIGH SCORE: " + game.getSituation().getCurrentHighScore());
        this.width = currentMap.getGraphMatrix()[0].length * 20;
        this.height = currentMap.getGraphMatrix().length * 20;
        Canvas c = new Canvas(width, height + scoreBoardHeight * 2);
        BorderPane window = new BorderPane();
        window.setCenter(c);

        Scene scene = new Scene(window);
        scene.setOnMouseClicked(event -> {
            if (game.getSituation().isGameOver()) {
                startOver();
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
                if (now - prev < updateFrequency) {
                    return;
                }
                game.monsterActivation(updateFrequency);
                if (game.monsterBehaviourThresholdReached(updateFrequency)) {
                    if (game.getMonsterBehaviourTimer().getThreshold() == normalMonsterBehaviourLength) {
                        game.scatterIfPossible(scatterBehaviourLength);
                    } else {
                        game.activateChaseMode(normalMonsterBehaviourLength);
                    }
                }
                if (game.timerThresholdReached(updateFrequency)) {
                    game.endPanicPhase();
                }
                if (game.situationNormal()) {
                    prev = now;
                    game.movePlayer();
                    game.updateMonsters();
                    pointLabel.setText("POINTS: " + game.getSituation().getPoints());
                    paintGame(c.getGraphicsContext2D(), game.getGraph(), game.getPlayer());

                } else {
                    if (game.getPlayer().gotHit()) {
                        game.getPlayer().loseHitPoints();
                        if (!game.getPlayer().gotHit()) {
                            game.resetMonsterStartingPositions();
                        }
                        paintGame(c.getGraphicsContext2D(), currentMap, game.getPlayer());
                        pointLabel.setText("POINTS: " + game.getSituation().getPoints());
                    } else if (game.getSituation().isComplete()) {
                        nextLevel();
                    } else {
                        if (game.getSituation().isGameOver()) {
                            game.getTimer().deactivate();
                            game.getMonsterBehaviourTimer().deactivate();
                            paintGame(c.getGraphicsContext2D(), currentMap, game.getPlayer());
                            drawGameOverText(c.getGraphicsContext2D());
                        }
                    }
                }
            }
        }.start();

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pacman");
        primaryStage.show();
    }

    public void paintGame(GraphicsContext gc, Graph map, Player player) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height + scoreBoardHeight * 2);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText(this.pointLabel.getText(), 20, 30);
        gc.fillText(highScoreLabel.getText(), 200, 30);
        drawRemainingHealth(gc, player);
        for (int i = 0; i < map.getGraphMatrix().length; i++) {
            for (int j = 0; j < map.getGraphMatrix()[0].length; j++) {
                Tile tile = map.getGraphMatrix()[i][j];
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

    public void drawRemainingHealth(GraphicsContext gc, Player player) {
        double x = 10;
        for (int i = 1; i <= player.getRemainingLife(); i++) {
            gc.drawImage(healthLeft, x, this.height + scoreBoardHeight + 5);
            x += 40;
        }
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
        drawSingleMonster(gc, game.getRed());
        drawSingleMonster(gc, game.getBlue());
        drawSingleMonster(gc, game.getPink());
        drawSingleMonster(gc, game.getOrange());
    }

    public void drawSingleMonster(GraphicsContext gc, Monster monster) {
        if (monster.getCurrentBehaviour() == Behaviour.PANIC) {
            gc.drawImage(scaredImage, monster.getX(), monster.getY() + scoreBoardHeight);
            return;
        } else if (monster.getCurrentBehaviour() == Behaviour.RESET) {
            gc.drawImage(reset, monster.getX(), monster.getY() + scoreBoardHeight);
            return;
        }
        gc.drawImage(monster.getCurrentImage(), monster.getX(), monster.getY() + scoreBoardHeight);
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
