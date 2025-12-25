/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.medical;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Medical Monitor Mockup showing vital constants (ECG, SpO2, Heart Rate).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class VitalMonitorViewer extends Application {

    private static final int HISTORY_SIZE = 400;
    private List<Double> ecgHistory = new ArrayList<>();
    private List<Double> spo2History = new ArrayList<>();
    private double time = 0;

    // Vitals
    private int heartRate = 72;

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: black;");
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        // ECG Monitor
        Canvas ecgCanvas = new Canvas(600, 200);
        VBox ecgBox = createMonitorBox("ECG (II)", ecgCanvas, Color.LIME);
        root.add(ecgBox, 0, 0);

        // SpO2 Monitor
        Canvas spo2Canvas = new Canvas(600, 200);
        VBox spo2Box = createMonitorBox("SpO2", spo2Canvas, Color.CYAN);
        root.add(spo2Box, 0, 1);

        // Numerical Panel
        VBox vitalsPanel = new VBox(20);
        vitalsPanel.setPadding(new Insets(10));
        vitalsPanel.setStyle("-fx-border-color: #444; -fx-border-width: 2;");

        Label hrLabel = createVitalLabel("HR", Color.LIME, "72");
        Label spLabel = createVitalLabel("SpO2", Color.CYAN, "98%");
        Label bpLabel = createVitalLabel("NIBP", Color.WHITE, "120/80");

        vitalsPanel.getChildren().addAll(hrLabel, spLabel, bpLabel);
        root.add(vitalsPanel, 1, 0, 1, 2);

        // Animation
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateVitals(now);
                drawWaveform(ecgCanvas.getGraphicsContext2D(), ecgHistory, Color.LIME, 2.0);
                drawWaveform(spo2Canvas.getGraphicsContext2D(), spo2History, Color.CYAN, 1.0);

                // Update numerical values slightly
                if (now % 100 == 0) {
                    int hr = 70 + (int) (Math.sin(time) * 5);
                    hrLabel.setText("HR\n" + hr);
                }
            }
        }.start();

        Scene scene = new Scene(root, 900, 500);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle("JScience Vital Constants Monitor");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createMonitorBox(String title, Canvas canvas, Color color) {
        VBox box = new VBox(5);
        Label lbl = new Label(title);
        lbl.setTextFill(color);
        lbl.setStyle("-fx-font-weight: bold;");
        box.getChildren().addAll(lbl, canvas);
        return box;
    }

    private Label createVitalLabel(String title, Color color, String value) {
        Label lbl = new Label(title + "\n" + value);
        lbl.setTextFill(color);
        lbl.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-alignment: center;");
        return lbl;
    }

    private void updateVitals(long now) {
        time += 0.1;

        // Mock ECG Pulse
        double hrScale = heartRate / 60.0;
        double pulseCycle = time % (2.0 * Math.PI / hrScale);
        double ecgVal = 0;
        if (pulseCycle < 0.2)
            ecgVal = Math.sin(pulseCycle * 50) * 0.8; // QRS
        else if (pulseCycle > 0.5 && pulseCycle < 0.8)
            ecgVal = Math.sin((pulseCycle - 0.5) * 10) * 0.2; // T wave

        ecgHistory.add(ecgVal + (Math.random() - 0.5) * 0.05); // Noise
        if (ecgHistory.size() > HISTORY_SIZE)
            ecgHistory.remove(0);

        // Mock SpO2
        double spo2Val = Math.sin(time * 0.5) * 0.1 + 0.5;
        spo2History.add(spo2Val);
        if (spo2History.size() > HISTORY_SIZE)
            spo2History.remove(0);
    }

    private void drawWaveform(GraphicsContext gc, List<Double> history, Color color, double scale) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setStroke(color);
        gc.setLineWidth(2);

        double midY = gc.getCanvas().getHeight() / 2;
        double step = gc.getCanvas().getWidth() / HISTORY_SIZE;

        gc.beginPath();
        for (int i = 0; i < history.size(); i++) {
            double x = i * step;
            double y = midY - history.get(i) * 50 * scale;
            if (i == 0)
                gc.moveTo(x, y);
            else
                gc.lineTo(x, y);
        }
        gc.stroke();
    }

    public static void show(Stage stage) {
        new VitalMonitorViewer().start(stage);
    }
}
