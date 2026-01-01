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
 * Monte Carlo Pi Estimation with distributed sampling.
 * 
 * Visualizes random point sampling to estimate Ãâ‚¬ using the grid.
 */
public class MonteCarloPiApp extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private Label piLabel;
    private Label samplesLabel;
    private ProgressBar progressBar;
    
    private long insideCircle = 0;
    private long totalSamples = 0;
    private final long targetSamples = 10_000_000;
    private boolean running = false;
    
    private final java.util.Random random = new java.util.Random();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸Å½Â¯ Monte Carlo Ãâ‚¬ Estimation - JScience");

        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();

        // Control panel
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(250);

        Label title = new Label("Monte Carlo Ãâ‚¬");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        piLabel = new Label("Ãâ‚¬ Ã¢â€°Ë† ?");
        piLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #eee;");

        samplesLabel = new Label("Samples: 0");
        samplesLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);

        Label accuracyLabel = new Label("Accuracy: Ã‚Â±?");
        accuracyLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333;");

        Button startBtn = new Button("Ã¢â€“Â¶ Start Sampling");
        startBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-padding: 10 20;");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? "Ã¢ÂÂ¸ Pause" : "Ã¢â€“Â¶ Resume");
        });

        Button resetBtn = new Button("Ã¢â€ Âº Reset");
        resetBtn.setStyle("-fx-background-color: #333; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-padding: 10 20;");
        resetBtn.setOnAction(e -> reset());

        // Info section
        TextArea infoArea = new TextArea("""
            Monte Carlo Method:
            
            1. Random points in unit square
            2. Count points inside circle
            3. Ãâ‚¬ Ã¢â€°Ë† 4 Ãƒâ€” (inside / total)
            
            The more samples, the more
            accurate the estimation!
            """);
        infoArea.setEditable(false);
        infoArea.setWrapText(true);
        infoArea.setPrefRowCount(8);
        infoArea.setStyle("-fx-control-inner-background: #16213e; -fx-text-fill: #aaa;");

        controls.getChildren().addAll(
                title, piLabel, samplesLabel, progressBar, accuracyLabel,
                sep, startBtn, resetBtn, new Separator(), infoArea
        );

        HBox root = new HBox(canvas, controls);
        root.setStyle("-fx-background-color: #0f0f1a;");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        drawBackground();
        startAnimation(accuracyLabel);
    }

    private void drawBackground() {
        gc.setFill(Color.web("#16213e"));
        gc.fillRect(0, 0, 600, 600);

        // Draw circle
        gc.setStroke(Color.web("#e94560"));
        gc.setLineWidth(2);
        gc.strokeOval(0, 0, 600, 600);

        // Draw axes
        gc.setStroke(Color.web("#333"));
        gc.setLineWidth(1);
        gc.strokeLine(300, 0, 300, 600);
        gc.strokeLine(0, 300, 600, 300);
    }

    private void reset() {
        insideCircle = 0;
        totalSamples = 0;
        running = false;
        drawBackground();
        piLabel.setText("Ãâ‚¬ Ã¢â€°Ë† ?");
        samplesLabel.setText("Samples: 0");
        progressBar.setProgress(0);
    }

    private void startAnimation(Label accuracyLabel) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) return;

                // Batch sample for performance
                int batchSize = 1000;
                for (int i = 0; i < batchSize && totalSamples < targetSamples; i++) {
                    double x = random.nextDouble() * 2 - 1;  // [-1, 1]
                    double y = random.nextDouble() * 2 - 1;  // [-1, 1]

                    boolean inside = x * x + y * y <= 1;
                    if (inside) insideCircle++;
                    totalSamples++;

                    // Draw point (scaled to canvas)
                    double px = (x + 1) * 300;
                    double py = (y + 1) * 300;
                    gc.setFill(inside ? Color.web("#4ecca3", 0.7) : Color.web("#e94560", 0.5));
                    gc.fillOval(px - 1, py - 1, 2, 2);
                }

                // Update UI
                double pi = 4.0 * insideCircle / totalSamples;
                double error = Math.abs(pi - Math.PI);
                
                piLabel.setText(String.format("Ãâ‚¬ Ã¢â€°Ë† %.8f", pi));
                samplesLabel.setText(String.format("Samples: %,d", totalSamples));
                progressBar.setProgress((double) totalSamples / targetSamples);
                accuracyLabel.setText(String.format("Error: %.6f", error));

                if (totalSamples >= targetSamples) {
                    running = false;
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


