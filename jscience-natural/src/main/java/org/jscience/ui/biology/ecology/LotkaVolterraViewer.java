/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.ecology;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Lotka-Volterra Predator-Prey Dynamics Simulation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class LotkaVolterraViewer extends Application {

    // Parameters
    private double alpha = 1.1; // Prey birth rate
    private double beta = 0.4; // Predation rate
    private double delta = 0.1; // Predator birth rate per prey
    private double gamma = 0.4; // Predator death rate

    private double x = 10.0; // Current Prey population
    private double y = 5.0; // Current Predator population
    private double time = 0;

    private XYChart.Series<Number, Number> preySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> predSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> phaseSeries = new XYChart.Series<>();

    private boolean running = false;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Charts
        VBox chartsBox = new VBox(10);

        // Time series chart
        NumberAxis xAxisTime = new NumberAxis();
        xAxisTime.setLabel("Time");
        NumberAxis yAxisPop = new NumberAxis();
        yAxisPop.setLabel("Population");

        LineChart<Number, Number> timeChart = new LineChart<>(xAxisTime, yAxisPop);
        timeChart.setTitle("Population over Time");
        timeChart.setCreateSymbols(false);
        preySeries.setName("Prey (x)");
        predSeries.setName("Predators (y)");
        timeChart.getData().addAll(preySeries, predSeries);

        // Phase space chart
        NumberAxis xAxisPhase = new NumberAxis();
        xAxisPhase.setLabel("Prey (x)");
        NumberAxis yAxisPhase = new NumberAxis();
        yAxisPhase.setLabel("Predators (y)");

        ScatterChart<Number, Number> phaseChart = new ScatterChart<>(xAxisPhase, yAxisPhase);
        phaseChart.setTitle("Phase Space (y vs x)");
        phaseSeries.setName("Trajectory");
        phaseChart.getData().add(phaseSeries);
        phaseChart.setLegendVisible(false);

        chartsBox.getChildren().addAll(timeChart, phaseChart);
        root.setCenter(chartsBox);

        // Sidebar Controls
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #16213e;");

        Label controlTitle = new Label("Simulation Parameters");
        controlTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #00d9ff;");

        sidebar.getChildren().addAll(
                controlTitle,
                createSliderLabel("α (Prey growth)", 0, 5, alpha, v -> alpha = v),
                createSliderLabel("β (Predation)", 0, 5, beta, v -> beta = v),
                createSliderLabel("δ (Predator growth)", 0, 5, delta, v -> delta = v),
                createSliderLabel("γ (Predator death)", 0, 5, gamma, v -> gamma = v));

        Button startBtn = new Button("Start / Stop");
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setOnAction(e -> running = !running);

        Button resetBtn = new Button("Reset");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> reset());

        sidebar.getChildren().addAll(new Separator(), startBtn, resetBtn);
        root.setLeft(sidebar);

        // Animation
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    step();
                }
            }
        }.start();

        Scene scene = new Scene(root, 1000, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle("Lotka-Volterra Engine");
        stage.setScene(scene);
        stage.show();
    }

    private void step() {
        double dt = 0.05;

        // RK4 or simple Euler for demo
        double dx = (alpha * x - beta * x * y) * dt;
        double dy = (delta * x * y - gamma * y) * dt;

        x += dx;
        y += dy;
        time += dt;

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;

        preySeries.getData().add(new XYChart.Data<>(time, x));
        predSeries.getData().add(new XYChart.Data<>(time, y));
        phaseSeries.getData().add(new XYChart.Data<>(x, y));

        // Limit data points for performance
        if (preySeries.getData().size() > 500) {
            preySeries.getData().remove(0);
            predSeries.getData().remove(0);
        }
        if (phaseSeries.getData().size() > 500) {
            phaseSeries.getData().remove(0);
        }
    }

    private void reset() {
        time = 0;
        x = 10.0;
        y = 5.0;
        preySeries.getData().clear();
        predSeries.getData().clear();
        phaseSeries.getData().clear();
    }

    private VBox createSliderLabel(String name, double min, double max, double val,
            java.util.function.DoubleConsumer consumer) {
        Label lbl = new Label(name + ": " + String.format("%.2f", val));
        Slider s = new Slider(min, max, val);
        s.valueProperty().addListener((o, ov, nv) -> {
            consumer.accept(nv.doubleValue());
            lbl.setText(name + ": " + String.format("%.2f", nv.doubleValue()));
        });
        return new VBox(5, lbl, s);
    }

    public static void show(Stage stage) {
        new LotkaVolterraViewer().start(stage);
    }
}
