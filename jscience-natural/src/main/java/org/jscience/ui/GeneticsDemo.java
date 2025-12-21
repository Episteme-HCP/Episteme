/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Population Genetics Demo.
 * Demonstrates genetic drift through visual simulation.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class GeneticsDemo implements DemoProvider {

    private static final int POP_SIZE = 100;
    private static final int GENERATIONS = 200;
    private double alleleFrequency = 0.5;
    private double[] history = new double[GENERATIONS];
    private Canvas canvas;

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Genetic Drift Simulation";
    }

    @Override
    public void show(Stage stage) {
        canvas = new Canvas(700, 400);

        Button runBtn = new Button("Run Simulation");
        runBtn.setOnAction(e -> runSimulation());

        HBox controls = new HBox(10, new Label("Genetic Drift (N=100)"), runBtn);
        controls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controls);

        drawAxes();

        Scene scene = new Scene(root, 750, 500);
        stage.setTitle("JScience - Genetic Drift");
        stage.setScene(scene);
        stage.show();
    }

    private void runSimulation() {
        Random rand = new Random();
        alleleFrequency = 0.5;

        for (int gen = 0; gen < GENERATIONS; gen++) {
            history[gen] = alleleFrequency;
            // Wright-Fisher model: binomial sampling
            int count = 0;
            for (int i = 0; i < POP_SIZE; i++) {
                if (rand.nextDouble() < alleleFrequency)
                    count++;
            }
            alleleFrequency = (double) count / POP_SIZE;
        }

        drawAxes();
        drawHistory();
    }

    private void drawAxes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeLine(50, 350, 680, 350); // X axis
        gc.strokeLine(50, 50, 50, 350); // Y axis
        gc.fillText("Generation", 350, 390);
        gc.fillText("Freq", 10, 200);
    }

    private void drawHistory() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1.5);
        gc.beginPath();
        for (int i = 0; i < GENERATIONS; i++) {
            double x = 50 + (i * 630.0 / GENERATIONS);
            double y = 350 - (history[i] * 300);
            if (i == 0)
                gc.moveTo(x, y);
            else
                gc.lineTo(x, y);
        }
        gc.stroke();
    }
}
