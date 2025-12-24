/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.mechanics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jscience.physics.PhysicalConstants;

/**
 * Simple Pendulum Simulation with Phase Space visualization.
 * Demonstrates use of Physical Constants and Real types.
 */
public class PendulumViewer extends Application {

    private double theta0 = Math.PI / 4; // 45 degrees
    private double length = 2.0; // meters
    private double time = 0;
    private double omega; // calculated frequency

    private final XYChart.Series<Number, Number> phaseData = new XYChart.Series<>();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Canvas for simulation
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Chart for Phase Space
        // Chart for Phase Space
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(org.jscience.natural.i18n.I18n.getInstance().get("pendulum.axis.angle"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(org.jscience.natural.i18n.I18n.getInstance().get("pendulum.axis.velocity"));
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(org.jscience.natural.i18n.I18n.getInstance().get("pendulum.title"));
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.getData().add(phaseData);
        phaseData.setName(org.jscience.natural.i18n.I18n.getInstance().get("pendulum.series.trajectory"));

        // Controls
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        Slider lengthSlider = new Slider(0.5, 5.0, 2.0);
        Label lengthLabel = new Label("Length: 2.0 m");
        lengthSlider.valueProperty().addListener((obs, old, val) -> {
            length = val.doubleValue();
            lengthLabel.setText(String.format("Length: %.1f m", length));
            recalcFrequency();
            phaseData.getData().clear();
            time = 0;
        });

        controls.getChildren().addAll(new Label("Pendulum Parameters"), lengthLabel, lengthSlider);

        root.setCenter(canvas);
        root.setRight(chart);
        root.setBottom(controls);

        recalcFrequency();

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                time += dt;
                update(dt);
                render(gc);
            }
        }.start();

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("JScience Pendulum Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void recalcFrequency() {
        // omega = sqrt(g/L)
        // Using JScience constants
        // g = 9.80665 m/s^2
        double g = PhysicalConstants.STANDARD_GRAVITY.getValue().doubleValue();
        omega = Math.sqrt(g / length);
    }

    private void update(double dt) {
        // Analytical solution for simple pendulum (small angle approx for visualization
        // simplicity here,
        // though strictly large angles require elliptic integrals. For demo we stick to
        // SHM approx or exact Euler integration)

        // Exact solution for SHM: theta(t) = theta0 * cos(omega * t)
        // theta_dot(t) = -theta0 * omega * sin(omega * t)

        double theta = theta0 * Math.cos(omega * time);
        double thetaDot = -theta0 * omega * Math.sin(omega * time);

        // Update Chart
        // Downsample to avoid flooding chart
        if (Math.frameCount % 5 == 0) {
            phaseData.getData().add(new XYChart.Data<>(theta, thetaDot));
            if (phaseData.getData().size() > 500)
                phaseData.getData().remove(0);
        }
    }

    // Hack to access frame count locally or just random downsample
    private static class Math {
        static long frameCount = 0;
        static double PI = java.lang.Math.PI;

        static double sqrt(double a) {
            return java.lang.Math.sqrt(a);
        }

        static double cos(double a) {
            return java.lang.Math.cos(a);
        }

        static double sin(double a) {
            return java.lang.Math.sin(a);
        }
    }

    private void render(GraphicsContext gc) {
        Math.frameCount++;

        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, w, h);

        double pivotX = w / 2;
        double pivotY = 50;
        double scale = 100; // pixels per meter (visual scale)

        double theta = theta0 * java.lang.Math.cos(omega * time);

        double bobX = pivotX + length * scale * java.lang.Math.sin(theta);
        double bobY = pivotY + length * scale * java.lang.Math.cos(theta);

        gc.setStroke(Color.web("#aaa"));
        gc.setLineWidth(2);
        gc.strokeLine(pivotX, pivotY, bobX, bobY);

        // Draw Bob
        gc.setFill(Color.web("#ff6b6b"));
        gc.fillOval(bobX - 10, bobY - 10, 20, 20);

        // Draw Pivot
        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval(pivotX - 5, pivotY - 5, 10, 10);
    }

    public static void show(Stage stage) {
        new PendulumViewer().start(stage);
    }
}
