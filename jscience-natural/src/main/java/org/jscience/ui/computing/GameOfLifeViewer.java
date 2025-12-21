package org.jscience.ui.computing;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.computing.automata.ConwayLife;

/**
 * Game of Life Viewer.
 * Visualizes Cellular Automata.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GameOfLifeViewer extends Application {

    private static final int CELL_SIZE = 5;
    private static final int WIDTH = 160;
    private static final int HEIGHT = 120;

    private ConwayLife life;
    private Canvas canvas;
    private boolean paused = false;

    @Override
    public void start(Stage stage) {
        life = new ConwayLife(WIDTH, HEIGHT);
        // Random seed
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (Math.random() < 0.2)
                    life.setState(x, y, true);
            }
        }

        canvas = new Canvas(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                int x = (int) (e.getX() / CELL_SIZE);
                int y = (int) (e.getY() / CELL_SIZE);
                life.setState(x, y, !life.getState(x, y));
                draw();
            } else {
                paused = !paused;
            }
        });

        canvas.setOnMouseDragged(e -> {
            int x = (int) (e.getX() / CELL_SIZE);
            int y = (int) (e.getY() / CELL_SIZE);
            life.setState(x, y, true);
            draw();
        });

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (paused)
                    return;
                if (now - last > 50_000_000) { // 20 FPS
                    life.nextGeneration();
                    draw();
                    last = now;
                }
            }
        }.start();

        stage.setTitle("JScience Game of Life");
        stage.setScene(scene);
        stage.show();
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.LIME);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (life.getState(x, y)) {
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }

    public static void show(Stage stage) {
        new GameOfLifeViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
