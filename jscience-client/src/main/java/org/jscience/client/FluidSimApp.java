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
 * Fluid Dynamics Simulation using Lattice Boltzmann Method.
 * 
 * Visualizes 2D fluid flow around obstacles.
 */
public class FluidSimApp extends Application {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    private static final int SCALE = 5;
    
    private Canvas canvas;
    private GraphicsContext gc;
    
    // Lattice Boltzmann velocities
    private double[][] rho;      // Density
    private double[][] ux, uy;   // Velocity components
    private double[][][] f;      // Distribution functions
    private double[][][] fEq;    // Equilibrium distributions
    private boolean[][] obstacle;
    
    // D2Q9 lattice
    private final int[] cx = {0, 1, 0, -1, 0, 1, -1, -1, 1};
    private final int[] cy = {0, 0, 1, 0, -1, 1, 1, -1, -1};
    private final double[] w = {4.0/9, 1.0/9, 1.0/9, 1.0/9, 1.0/9, 1.0/36, 1.0/36, 1.0/36, 1.0/36};
    
    private double viscosity = 0.02;
    private double inletVelocity = 0.1;
    private boolean running = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸â€™Â§ Fluid Dynamics Simulation - JScience");

        canvas = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        gc = canvas.getGraphicsContext2D();

        // Controls
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(220);

        Label title = new Label("Fluid Dynamics");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");

        Slider viscSlider = new Slider(0.001, 0.1, viscosity);
        viscSlider.setShowTickLabels(true);
        viscSlider.valueProperty().addListener((obs, old, val) -> viscosity = val.doubleValue());

        Slider velSlider = new Slider(0.01, 0.2, inletVelocity);
        velSlider.setShowTickLabels(true);
        velSlider.valueProperty().addListener((obs, old, val) -> inletVelocity = val.doubleValue());

        ComboBox<String> vizMode = new ComboBox<>();
        vizMode.getItems().addAll("Velocity Magnitude", "Vorticity", "Pressure");
        vizMode.setValue("Velocity Magnitude");

        Button startBtn = new Button("Ã¢â€“Â¶ Start");
        startBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: white;");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? "Ã¢ÂÂ¸ Pause" : "Ã¢â€“Â¶ Resume");
        });

        Button resetBtn = new Button("Ã¢â€ Âº Reset");
        resetBtn.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> initialize());

        Button addObstacle = new Button("+ Circle");
        addObstacle.setOnAction(e -> addCircleObstacle(WIDTH/2, HEIGHT/2, 10));

        controls.getChildren().addAll(
                title,
                new Label("Viscosity:"), viscSlider,
                new Label("Inlet Velocity:"), velSlider,
                new Label("Visualization:"), vizMode,
                new Separator(),
                startBtn, resetBtn, addObstacle
        );
        controls.getChildren().forEach(n -> {
            if (n instanceof Label) ((Label) n).setStyle("-fx-text-fill: #aaa;");
        });

        HBox root = new HBox(canvas, controls);
        root.setStyle("-fx-background-color: #0f0f1a;");

        canvas.setOnMouseDragged(e -> {
            int x = (int) (e.getX() / SCALE);
            int y = (int) (e.getY() / SCALE);
            if (x > 0 && x < WIDTH-1 && y > 0 && y < HEIGHT-1) {
                obstacle[x][y] = true;
            }
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        initialize();
        startSimulation(vizMode);
    }

    private void initialize() {
        rho = new double[WIDTH][HEIGHT];
        ux = new double[WIDTH][HEIGHT];
        uy = new double[WIDTH][HEIGHT];
        f = new double[WIDTH][HEIGHT][9];
        fEq = new double[WIDTH][HEIGHT][9];
        obstacle = new boolean[WIDTH][HEIGHT];

        // Initialize uniform density
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                rho[x][y] = 1.0;
                ux[x][y] = inletVelocity;
                uy[x][y] = 0;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = equilibrium(i, rho[x][y], ux[x][y], uy[x][y]);
                }
            }
        }

        // Default obstacle
        addCircleObstacle(WIDTH/4, HEIGHT/2, 12);
    }

    private void addCircleObstacle(int cx, int cy, int r) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if ((x-cx)*(x-cx) + (y-cy)*(y-cy) < r*r) {
                    obstacle[x][y] = true;
                }
            }
        }
    }

    private double equilibrium(int i, double density, double velX, double velY) {
        double cu = cx[i] * velX + cy[i] * velY;
        double u2 = velX * velX + velY * velY;
        return w[i] * density * (1 + 3*cu + 4.5*cu*cu - 1.5*u2);
    }

    private void step() {
        double omega = 1.0 / (3.0 * viscosity + 0.5);

        // Collision
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (obstacle[x][y]) continue;
                for (int i = 0; i < 9; i++) {
                    fEq[x][y][i] = equilibrium(i, rho[x][y], ux[x][y], uy[x][y]);
                    f[x][y][i] = f[x][y][i] + omega * (fEq[x][y][i] - f[x][y][i]);
                }
            }
        }

        // Streaming (simplified)
        double[][][] fNew = new double[WIDTH][HEIGHT][9];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int i = 0; i < 9; i++) {
                    int xSrc = (x - cx[i] + WIDTH) % WIDTH;
                    int ySrc = (y - cy[i] + HEIGHT) % HEIGHT;
                    fNew[x][y][i] = f[xSrc][ySrc][i];
                }
            }
        }
        f = fNew;

        // Bounce-back at obstacles
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (obstacle[x][y]) {
                    double[] temp = new double[9];
                    temp[1] = f[x][y][3]; temp[3] = f[x][y][1];
                    temp[2] = f[x][y][4]; temp[4] = f[x][y][2];
                    temp[5] = f[x][y][7]; temp[7] = f[x][y][5];
                    temp[6] = f[x][y][8]; temp[8] = f[x][y][6];
                    temp[0] = f[x][y][0];
                    f[x][y] = temp;
                }
            }
        }

        // Inlet boundary
        for (int y = 0; y < HEIGHT; y++) {
            rho[0][y] = 1.0;
            ux[0][y] = inletVelocity;
            uy[0][y] = 0;
            for (int i = 0; i < 9; i++) {
                f[0][y][i] = equilibrium(i, rho[0][y], ux[0][y], uy[0][y]);
            }
        }

        // Compute macroscopic
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (obstacle[x][y]) continue;
                double density = 0, vx = 0, vy = 0;
                for (int i = 0; i < 9; i++) {
                    density += f[x][y][i];
                    vx += cx[i] * f[x][y][i];
                    vy += cy[i] * f[x][y][i];
                }
                rho[x][y] = density;
                ux[x][y] = vx / density;
                uy[x][y] = vy / density;
            }
        }
    }

    private void render(String mode) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Color c;
                if (obstacle[x][y]) {
                    c = Color.DARKGRAY;
                } else {
                    double value = Math.sqrt(ux[x][y]*ux[x][y] + uy[x][y]*uy[x][y]);
                    value = Math.min(1, value / (inletVelocity * 2));
                    c = Color.hsb(240 - value * 240, 0.8, 0.9);
                }
                gc.setFill(c);
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }

    private void startSimulation(ComboBox<String> vizMode) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) return;
                for (int i = 0; i < 5; i++) step();
                render(vizMode.getValue());
            }
        };
        timer.start();
        render(vizMode.getValue());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


