/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.waves;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

/**
 * Real-time Spectrograph visualization showing frequency analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpectrographViewer extends Application {

    private final int BANDS = 64;
    private double[] spectrum = new double[BANDS];
    private double time = 0;
    private double sensitivity = 1.0;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #111;");

        Label title = new Label("Frequency Spectrograph");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Canvas canvas = new Canvas(800, 400);

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        Slider sensSlider = new Slider(0.1, 5, 1);
        sensSlider.valueProperty().addListener((o, ov, nv) -> sensitivity = nv.doubleValue());
        Label sensLbl = new Label("Sensitivity");
        sensLbl.setTextFill(Color.WHITE);
        controls.getChildren().addAll(sensLbl, sensSlider);

        root.getChildren().addAll(title, canvas, controls);

        LinearGradient gradient = new LinearGradient(0, 400, 0, 0, false, null,
                new Stop(0, Color.BLUE),
                new Stop(0.5, Color.YELLOW),
                new Stop(1, Color.RED));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSpectrum();
                render(canvas.getGraphicsContext2D(), gradient);
            }
        }.start();

        Scene scene = new Scene(root, 850, 500);
        stage.setTitle("JScience Waveform Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    private void updateSpectrum() {
        time += 0.1;
        for (int i = 0; i < BANDS; i++) {
            // Mock harmonic noise
            double base = Math.sin(time + i * 0.2) * 0.5 + 0.5;
            double high = Math.sin(time * 2.5 + i * 0.5) * 0.3;
            double random = Math.random() * 0.2;

            // Falloff for high frequencies
            double falloff = Math.exp(-i / (double) BANDS);

            spectrum[i] = (base + high + random) * falloff * sensitivity;
        }
    }

    private void render(GraphicsContext gc, LinearGradient gradient) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        double bandWidth = gc.getCanvas().getWidth() / BANDS;
        gc.setFill(gradient);

        for (int i = 0; i < BANDS; i++) {
            double h = spectrum[i] * 350;
            gc.fillRect(i * bandWidth + 1, 400 - h, bandWidth - 2, h);

            // Small glow on top
            gc.setFill(Color.WHITE);
            gc.setGlobalAlpha(0.3);
            gc.fillRect(i * bandWidth + 1, 395 - h, bandWidth - 2, 5);
            gc.setGlobalAlpha(1.0);
            gc.setFill(gradient);
        }
    }

    public static void show(Stage stage) {
        new SpectrographViewer().start(stage);
    }
}
