/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.genetics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Population Genetics Viewer.
 * Demonstrates genetic drift through visual simulation with enhanced controls.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneticsViewer extends Application {

    private int popSize = 100;
    private int generations = 200;
    private double initialFreq = 0.5;
    private double[] history;
    private Canvas canvas;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(700, 400);
        history = new double[generations];

        // Controls
        Button runBtn = new Button("Run Simulation");
        runBtn.setOnAction(e -> runSimulation());

        Spinner<Integer> popSpinner = new Spinner<>(10, 1000, 100, 10);
        popSpinner.setEditable(true);
        popSpinner.valueProperty().addListener((o, old, val) -> popSize = val);

        Spinner<Integer> genSpinner = new Spinner<>(50, 500, 200, 50);
        genSpinner.setEditable(true);
        genSpinner.valueProperty().addListener((o, old, val) -> {
            generations = val;
            history = new double[generations];
        });

        Slider freqSlider = new Slider(0.01, 0.99, 0.5);
        freqSlider.setShowTickLabels(true);
        freqSlider.setMajorTickUnit(0.25);
        freqSlider.valueProperty().addListener((o, old, val) -> initialFreq = val.doubleValue());

        HBox controls = new HBox(15,
                new Label("Pop Size:"), popSpinner,
                new Label("Generations:"), genSpinner,
                new Label("Initial Freq:"), freqSlider,
                runBtn);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setStyle("-fx-background-color: #16213e;");

        // Info Panel
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.setStyle("-fx-background-color: #0f3460;");
        infoPanel.setPrefWidth(200);

        Label titleLabel = new Label("Genetic Drift Simulation");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #00d9ff;");

        Label explanationLabel = new Label(
                "This demonstrates genetic drift - random changes in allele " +
                        "frequency in a population over generations.\n\n" +
                        "The Wright-Fisher model simulates binomial sampling " +
                        "of alleles each generation.\n\n" +
                        "- Smaller populations show more drift\n" +
                        "- Frequency can fixate at 0 or 1\n" +
                        "- Initial frequency affects trajectory");
        explanationLabel.setWrapText(true);
        explanationLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaa;");

        statusLabel = new Label("Click 'Run Simulation' to start");
        statusLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #888;");

        infoPanel.getChildren().addAll(titleLabel, new Separator(), explanationLabel, new Separator(), statusLabel);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controls);
        root.setRight(infoPanel);
        root.setStyle("-fx-background-color: #1a1a2e;");

        drawAxes();

        Scene scene = new Scene(root, 950, 520);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.natural.i18n.I18n.getInstance().get("viewer.genetics"));
        stage.setScene(scene);
        stage.show();
    }

    private void runSimulation() {
        Random rand = new Random();
        double freq = initialFreq;

        for (int gen = 0; gen < generations; gen++) {
            history[gen] = freq;
            // Wright-Fisher model: binomial sampling
            int count = 0;
            for (int i = 0; i < popSize; i++) {
                if (rand.nextDouble() < freq)
                    count++;
            }
            freq = (double) count / popSize;
        }

        String outcome = freq >= 0.99 ? "FIXED (1.0)" : freq <= 0.01 ? "LOST (0.0)" : String.format("%.3f", freq);
        statusLabel.setText("Final Frequency: " + outcome);

        drawAxes();
        drawHistory();
    }

    private void drawAxes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Axes
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1.5);
        gc.strokeLine(50, 350, 680, 350); // X axis
        gc.strokeLine(50, 50, 50, 350); // Y axis

        // Labels
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Generation", 350, 385);
        gc.fillText("Allele", 5, 180);
        gc.fillText("Frequency", 5, 195);

        // Y-axis ticks
        gc.setStroke(Color.web("#444"));
        gc.setLineWidth(0.5);
        for (double f = 0; f <= 1.0; f += 0.25) {
            double y = 350 - (f * 300);
            gc.strokeLine(45, y, 680, y);
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText(String.format("%.2f", f), 20, y + 4);
        }

        // X-axis ticks
        for (int g = 0; g <= generations; g += generations / 4) {
            double x = 50 + (g * 630.0 / generations);
            gc.strokeLine(x, 350, x, 355);
            gc.fillText(String.valueOf(g), x - 10, 368);
        }
    }

    private void drawHistory() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#00d9ff"));
        gc.setLineWidth(2);
        gc.beginPath();
        for (int i = 0; i < generations; i++) {
            double x = 50 + (i * 630.0 / generations);
            double y = 350 - (history[i] * 300);
            if (i == 0)
                gc.moveTo(x, y);
            else
                gc.lineTo(x, y);
        }
        gc.stroke();

        // Draw reference line at 0.5
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.setLineDashes(5);
        gc.strokeLine(50, 350 - 150, 680, 350 - 150);
        gc.setLineDashes(null);
    }

    public static void show(Stage stage) {
        new GeneticsViewer().start(stage);
    }
}
