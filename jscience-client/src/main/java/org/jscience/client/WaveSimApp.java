/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Wave Equation Solver with 2D wave propagation visualization.
 */
public class WaveSimApp extends Application {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int SCALE = 2;
    
    private Canvas canvas;
    private GraphicsContext gc;
    
    private double[][] u;      // Current state
    private double[][] uPrev;  // Previous state
    private double[][] uNext;  // Next state
    
    private double c = 0.5;    // Wave speed
    private double damping = 0.999;
    private boolean running = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸Å’Å  Wave Equation Simulation - JScience");

        canvas = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        gc = canvas.getGraphicsContext2D();

        // Controls
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(220);

        Label title = new Label("Wave Equation");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #7c3aed;");

        Slider speedSlider = new Slider(0.1, 0.9, c);
        speedSlider.setShowTickLabels(true);
        speedSlider.valueProperty().addListener((obs, old, val) -> c = val.doubleValue());

        Slider dampSlider = new Slider(0.9, 1.0, damping);
        dampSlider.setShowTickLabels(true);
        dampSlider.valueProperty().addListener((obs, old, val) -> damping = val.doubleValue());

        ComboBox<String> sourceType = new ComboBox<>();
        sourceType.getItems().addAll("Click", "Continuous", "Two Sources");
        sourceType.setValue("Click");

        Button startBtn = new Button("Ã¢â€“Â¶ Start");
        startBtn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white;");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? "Ã¢ÂÂ¸ Pause" : "Ã¢â€“Â¶ Resume");
        });

        Button resetBtn = new Button("Ã¢â€ Âº Reset");
        resetBtn.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> initialize());

        Button dropBtn = new Button("Ã°Å¸â€™Â§ Drop");
        dropBtn.setOnAction(e -> createDrop(WIDTH/2, HEIGHT/2, 20, 1.0));

        controls.getChildren().addAll(
                title,
                new Label("Wave Speed:"), speedSlider,
                new Label("Damping:"), dampSlider,
                new Label("Source Type:"), sourceType,
                new Separator(),
                startBtn, resetBtn, dropBtn
        );
        controls.getChildren().forEach(n -> {
            if (n instanceof Label) ((Label) n).setStyle("-fx-text-fill: #aaa;");
        });

        HBox root = new HBox(canvas, controls);
        root.setStyle("-fx-background-color: #0f0f1a;");

        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / SCALE);
            int y = (int) (e.getY() / SCALE);
            createDrop(x, y, 15, 1.0);
        });

        canvas.setOnMouseDragged(e -> {
            int x = (int) (e.getX() / SCALE);
            int y = (int) (e.getY() / SCALE);
            if (x > 1 && x < WIDTH-2 && y > 1 && y < HEIGHT-2) {
                u[x][y] = 0.5;
            }
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        initialize();
        startSimulation(sourceType);
    }

    private void initialize() {
        u = new double[WIDTH][HEIGHT];
        uPrev = new double[WIDTH][HEIGHT];
        uNext = new double[WIDTH][HEIGHT];
        render();
    }

    private void createDrop(int cx, int cy, int radius, double amplitude) {
        for (int x = Math.max(1, cx - radius); x < Math.min(WIDTH - 1, cx + radius); x++) {
            for (int y = Math.max(1, cy - radius); y < Math.min(HEIGHT - 1, cy + radius); y++) {
                double dist = Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (dist < radius) {
                    double factor = Math.cos(Math.PI * dist / radius / 2);
                    u[x][y] += amplitude * factor * factor;
                }
            }
        }
    }

    private long frameCount = 0;
    
    private void step(String sourceType) {
        frameCount++;
        
        // Continuous sources
        if ("Continuous".equals(sourceType)) {
            double freq = 0.1;
            u[WIDTH/2][HEIGHT/2] = Math.sin(frameCount * freq);
        } else if ("Two Sources".equals(sourceType)) {
            double freq = 0.1;
            u[WIDTH/3][HEIGHT/2] = Math.sin(frameCount * freq);
            u[2*WIDTH/3][HEIGHT/2] = Math.sin(frameCount * freq);
        }

        // Wave equation: u_tt = c^2 * (u_xx + u_yy)
        double c2 = c * c;
        
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                double laplacian = u[x+1][y] + u[x-1][y] + u[x][y+1] + u[x][y-1] - 4 * u[x][y];
                uNext[x][y] = 2 * u[x][y] - uPrev[x][y] + c2 * laplacian;
                uNext[x][y] *= damping;
            }
        }

        // Swap buffers
        double[][] temp = uPrev;
        uPrev = u;
        u = uNext;
        uNext = temp;
    }

    private void render() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double value = u[x][y];
                
                // Map to color
                double normalized = (value + 1) / 2;  // [-1, 1] -> [0, 1]
                normalized = Math.max(0, Math.min(1, normalized));
                
                // Blue-white-red colormap
                Color c;
                if (value < 0) {
                    c = Color.rgb(
                            (int) (50 + 100 * (1 + value)),
                            (int) (50 + 100 * (1 + value)),
                            (int) (150 + 105 * (1 + value))
                    );
                } else {
                    c = Color.rgb(
                            (int) (150 + 105 * value),
                            (int) (50 + 100 * (1 - value)),
                            (int) (50 + 100 * (1 - value))
                    );
                }
                
                gc.setFill(c);
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }

    private void startSimulation(ComboBox<String> sourceType) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) return;
                for (int i = 0; i < 3; i++) {
                    step(sourceType.getValue());
                }
                render();
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


