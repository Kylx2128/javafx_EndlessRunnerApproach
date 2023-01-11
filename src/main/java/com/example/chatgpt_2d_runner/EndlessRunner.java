package com.example.chatgpt_2d_runner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class EndlessRunner extends Application {

    // game dimensions
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    // player dimensions
    private static final int PLAYER_WIDTH = 50;
    private static final int PLAYER_HEIGHT = 50;

    // player starting position
    private static final int PLAYER_START_X = WIDTH / 2;
    private static final int PLAYER_START_Y = HEIGHT - PLAYER_HEIGHT;

    // obstacle dimensions
    private static final int OBSTACLE_WIDTH = 50;
    private static final int OBSTACLE_HEIGHT = 50;

    // obstacle starting position
    private static final int OBSTACLE_START_X = WIDTH;
    private static final int OBSTACLE_START_Y = HEIGHT - OBSTACLE_HEIGHT;

    private static final double OBSTACLE_SPEED = 4.0;

    // player and obstacle rectangles
    private final Rectangle player = new Rectangle(PLAYER_START_X, PLAYER_START_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
    private final Rectangle obstacle = new Rectangle(OBSTACLE_START_X, OBSTACLE_START_Y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

    // canvas and graphics context
    private Canvas canvas;
    private GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        primaryStage.setTitle("Endless Runner");
        primaryStage.setScene(scene);
        primaryStage.show();

        new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double elapsedTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // move the obstacle to the left
                obstacle.setX(obstacle.getX() - OBSTACLE_SPEED * elapsedTime);

                // check for collision
                if (player.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    stop(); // stop the animation timer
                    gc.setFill(Color.RED);
                    gc.fillText("Game Over", WIDTH / 2, HEIGHT / 2);
                }

                // wrap the obstacle around to the right side of the screen if it goes off the left
                if (obstacle.getX() + OBSTACLE_WIDTH < 0) {
                    obstacle.setX(WIDTH);
                }

                // clear the screen
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                // draw the player and obstacle
                gc.setFill(Color.YELLOW);
                gc.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
                gc.setFill(Color.BLUE);
                gc.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            }
        }.start();

        // handle player movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.setX(player.getX() - 5);
                    break;
                case RIGHT:
                    player.setX(player.getX() + 5);
                    break;
            }
        });
    }
}

