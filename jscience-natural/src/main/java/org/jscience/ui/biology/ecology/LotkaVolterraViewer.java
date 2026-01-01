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
import org.jscience.ui.i18n.I18n;

/**
 * Lotka-Volterra Predator-Prey Dynamics Simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
        root.getStyleClass().add("dark-viewer-root");

        // Charts
        VBox chartsBox = new VBox(10);

        // Time series chart
        NumberAxis xAxisTime = new NumberAxis();
        xAxisTime.setLabel(I18n.getInstance().get("lotka.axis.time"));
        NumberAxis yAxisPop = new NumberAxis();
        yAxisPop.setLabel(I18n.getInstance().get("lotka.axis.pop"));

        LineChart<Number, Number> timeChart = new LineChart<>(xAxisTime, yAxisPop);
        timeChart.setTitle(I18n.getInstance().get("lotka.chart.time"));
        timeChart.setCreateSymbols(false);
        preySeries.setName(I18n.getInstance().get("lotka.series.prey"));
        predSeries.setName(I18n.getInstance().get("lotka.series.pred"));

        timeChart.getData().addAll(preySeries, predSeries);

        // Custom Colors
        // Wait for node to be created
        preySeries.nodeProperty().addListener((o, old, node) -> {
            if (node != null)
                node.setStyle("-fx-stroke: #2ecc71; -fx-stroke-width: 2px;");
        });
        predSeries.nodeProperty().addListener((o, old, node) -> {
            if (node != null)
                node.setStyle("-fx-stroke: #e74c3c; -fx-stroke-width: 2px;");
        });

        // Phase space chart
        NumberAxis xAxisPhase = new NumberAxis();
        xAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.prey"));
        NumberAxis yAxisPhase = new NumberAxis();
        yAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.pred"));

        ScatterChart<Number, Number> phaseChart = new ScatterChart<>(xAxisPhase, yAxisPhase);
        phaseChart.setTitle(I18n.getInstance().get("lotka.chart.phase"));
        phaseSeries.setName(I18n.getInstance().get("lotka.series.traj"));
        phaseChart.getData().add(phaseSeries);
        phaseChart.setLegendVisible(false);

        chartsBox.getChildren().addAll(timeChart, phaseChart);
        root.setCenter(chartsBox);

        // Sidebar Controls
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        Label controlTitle = new Label(I18n.getInstance().get("lotka.header.params"));
        controlTitle.getStyleClass().add("dark-header");

        sidebar.getChildren().addAll(
                controlTitle,
                createSliderLabel(I18n.getInstance().get("lotka.label.alpha"), 0, 5, alpha, v -> alpha = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.beta"), 0, 5, beta, v -> beta = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.delta"), 0, 5, delta, v -> delta = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.gamma"), 0, 5, gamma, v -> gamma = v));

        Button startBtn = new Button(I18n.getInstance().get("lotka.btn.start"));
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setOnAction(e -> running = !running);

        Button resetBtn = new Button(I18n.getInstance().get("lotka.btn.reset"));
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
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("lotka.title"));
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


