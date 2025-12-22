/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.genetics;

import javafx.application.Application;
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
 * Population Genetics Viewer.
 * Demonstrates genetic drift through visual simulation.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class GeneticsViewer extends Application {

    private static final int POP_SIZE = 100;
    private static final int GENERATIONS = 200;
    private double alleleFrequency = 0.5;
    private double[] history = new double[GENERATIONS];
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(700, 400);

        Button runBtn = new Button("Run Simulation");
        runBtn.setOnAction(e -> runSimulation());

        HBox controls = new HBox(10, new Label("Genetic Drift (N=100)"), runBtn);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #f0f0f0;"); // Light theme for controls

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controls);
        root.setStyle("-fx-background-color: white;");

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
        gc.setFill(Color.BLACK);
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

    public static void show(Stage stage) {
        new GeneticsViewer().start(stage);
    }
}
