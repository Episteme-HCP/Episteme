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
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.earth.climate.ClimateModelTask;

/**
 * Climate Simulation Visualization.
 * 
 * Displays a heatmap of global temperatures.
 */
public class ClimateSimApp extends Application {

    private ClimateModelTask task;
    private Canvas mapCanvas;
    private GraphicsContext gc;
    private Label avgTempLabel;
    
    private final int WIDTH = 800;
    private final int HEIGHT = 400;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸Å’Â Climate Model - JScience");

        mapCanvas = new Canvas(WIDTH, HEIGHT);
        gc = mapCanvas.getGraphicsContext2D();

        task = new ClimateModelTask(40, 80); // 40 lat bins, 80 long bins

        BorderPane root = new BorderPane();
        root.setCenter(mapCanvas);
        
        VBox overlay = new VBox();
        overlay.setPadding(new Insets(10));
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        
        Label title = new Label("Global Energy Balance Model");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");
        
        avgTempLabel = new Label("Avg Temp: -- K");
        avgTempLabel.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 14;");
        
        overlay.getChildren().addAll(title, avgTempLabel);
        overlay.setMaxSize(250, 80);
        root.setTop(overlay);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Run a "day" of simulation
                task.runStep(86400); 
                render();
            }
        }.start();
    }

    private void render() {
        double[][] temp = task.getTemperatureMap();
        int rows = task.getLatitudeBins();
        int cols = task.getLongitudeBins();
        
        double cellW = (double) WIDTH / cols;
        double cellH = (double) HEIGHT / rows;
        
        double totalTemp = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double t = temp[i][j];
                totalTemp += t;
                
                // Color scale: Blue (250K) -> White (288K) -> Red (320K)
                Color c = getColor(t);
                
                gc.setFill(c);
                gc.fillRect(j * cellW, i * cellH, cellW, cellH); // i is lat (y), j is long (x)
            }
        }
        
        avgTempLabel.setText(String.format("Global Avg Temp: %.2f K (%.1f Ã‚Â°C)", 
            totalTemp / (rows*cols), (totalTemp / (rows*cols)) - 273.15));
    }

    private Color getColor(double tempK) {
        double minK = 230.0;
        double maxK = 320.0;
        double norm = (tempK - minK) / (maxK - minK);
        norm = Math.max(0, Math.min(1, norm));
        return Color.hsb(240 * (1 - norm), 0.8, 0.9);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


