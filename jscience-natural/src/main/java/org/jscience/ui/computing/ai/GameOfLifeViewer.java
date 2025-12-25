/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.computing.ai;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.computing.ai.automata.ConwayLife;

/**
 * Game of Life Viewer.
 * Visualizes Cellular Automata with interactive controls.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class GameOfLifeViewer extends Application {

    private static final int CELL_SIZE = 5;
    private static final int WIDTH = 160;
    private static final int HEIGHT = 100;

    private ConwayLife life;
    private Canvas canvas;
    private boolean paused = true;
    private long lastUpdate = 0;
    private long updateInterval = 460_000_000; // 460ms default (matches slider value 50)

    @Override
    public void start(Stage stage) {
        life = new ConwayLife(WIDTH, HEIGHT);
        // Canvas Area
        BorderPane root = new BorderPane();
        canvas = new Canvas(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);

        randomize();
        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / CELL_SIZE);
            int y = (int) (e.getY() / CELL_SIZE);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                life.setState(x, y, !life.getState(x, y));
                draw();
            }
        });
        canvas.setOnMouseDragged(e -> {
            int x = (int) (e.getX() / CELL_SIZE);
            int y = (int) (e.getY() / CELL_SIZE);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                life.setState(x, y, true);
                draw();
            }
        });

        // Wrap canvas in a pane to center it
        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setPadding(new Insets(10));
        canvasContainer.setStyle("-fx-background-color: #222; -fx-alignment: center;");
        root.setCenter(canvasContainer);

        // Controls Area
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #333;");

        Button btnPlay = new Button("Play");
        Button btnStep = new Button("Step");
        Button btnClear = new Button("Clear");
        Button btnRandom = new Button("Randomize");

        Slider speedSlider = new Slider(10, 500, 50); // ms delay
        speedSlider.setShowTickLabels(false);
        speedSlider.setShowTickMarks(false);
        Label speedLabel = new Label("Speed");
        speedLabel.setTextFill(Color.WHITE);

        btnPlay.setOnAction(e -> {
            paused = !paused;
            btnPlay.setText(paused ? "Play" : "Pause");
        });

        btnStep.setOnAction(e -> {
            paused = true;
            btnPlay.setText("Play");
            life.nextGeneration();
            draw();
        });

        btnClear.setOnAction(e -> {
            paused = true;
            btnPlay.setText("Play");
            life = new ConwayLife(WIDTH, HEIGHT); // Reset
            draw();
        });

        btnRandom.setOnAction(e -> randomize());

        speedSlider.valueProperty().addListener((obs, old, val) -> {
            // Invert: Left (10) = slow (500ms), Right (500) = fast (10ms)
            double invertedDelay = 510 - val.doubleValue();
            updateInterval = (long) (invertedDelay * 1_000_000);
        });
        // Initialize slider to match default updateInterval (460ms -> slider value 50)
        speedSlider.setValue(50); // Middle-slow, actual delay = 460ms

        controls.getChildren().addAll(btnPlay, btnStep, btnClear, btnRandom, speedLabel, speedSlider);
        root.setBottom(controls);

        Scene scene = new Scene(root);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);

        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!paused) {
                    if (now - lastUpdate > updateInterval) {
                        life.nextGeneration();
                        draw();
                        lastUpdate = now;
                    }
                }
            }
        }.start();

        stage.setTitle("JScience Game of Life - Enhanced");
        stage.setScene(scene);
        stage.show();
        draw();
    }

    private void randomize() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (Math.random() < 0.2)
                    life.setState(x, y, true);
                else
                    life.setState(x, y, false);
            }
        }
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
