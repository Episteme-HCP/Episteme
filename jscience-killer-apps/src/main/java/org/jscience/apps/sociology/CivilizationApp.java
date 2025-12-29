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

package org.jscience.apps.sociology;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.jscience.apps.framework.KillerAppBase;

/**
 * Civilization Collapse Model.
 * <p>
 * A simplified System Dynamics model (Limits to Growth style) simulating
 * the interaction between Population, Resources, and Pollution.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CivilizationApp extends KillerAppBase {

    // Model State (Stocks)
    private double population = 1000.0;
    private double resources = 100000.0;
    private double pollution = 0.0;

    // Parameters
    private double birthRateBase = 0.05;
    private double consumptionPerCapita = 1.0;
    private double pollutionFactor = 0.1;
    private double innovationRate = 0.0; // Reduces consumption/pollution
    private double regenerationRate = 0.0;
    private double aggression = 0.0;

    private double time = 0;
    private boolean running = false;

    // UI
    private XYChart.Series<Number, Number> popSeries;
    private XYChart.Series<Number, Number> resSeries;
    private XYChart.Series<Number, Number> polSeries;
    private Label statusLabel;

    @Override
    protected String getAppTitle() {
        return i18n.get("civilization.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("civilization.desc");
    }

    @Override
    protected Region createMainContent() {
        BorderPane root = new BorderPane();

        // Charts
        LineChart<Number, Number> chart = createChart();
        root.setCenter(chart);

        // Controls
        VBox controls = createControls();
        root.setBottom(controls);

        // Status Overlay
        statusLabel = new Label(i18n.get("civilization.status.stable"));
        statusLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: green;");
        StackPane overlay = new StackPane(statusLabel);
        overlay.setPickOnBounds(false);
        overlay.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        overlay.setPadding(new Insets(20));

        StackPane mainStack = new StackPane(root, overlay);

        startLoop();

        return mainStack;
    }

    @SuppressWarnings("unchecked")
    private LineChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(i18n.get("civilization.label.years"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(i18n.get("civilization.label.value"));

        LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setTitle(i18n.get("civilization.chart.title"));
        lc.setCreateSymbols(false);
        lc.setAnimated(false);

        popSeries = new XYChart.Series<>();
        popSeries.setName(i18n.get("civilization.label.population"));
        resSeries = new XYChart.Series<>();
        resSeries.setName(i18n.get("civilization.label.resources"));
        polSeries = new XYChart.Series<>();
        polSeries.setName(i18n.get("civilization.series.pollution"));

        lc.getData().addAll(popSeries, resSeries, polSeries);
        return lc;
    }

    private VBox createControls() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #eee;");

        HBox sliders = new HBox(20);

        sliders.getChildren().add(
                createSlider(i18n.get("civilization.label.consumption"), 0.5, 5.0, 1.0, v -> consumptionPerCapita = v));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.birth"), 0.0, 0.2, 0.05, v -> birthRateBase = v));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.innovation"), 0.0, 0.05, 0.0, v -> innovationRate = v));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.regen"), 0.0, 1.0, 0.0, v -> regenerationRate = v));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.aggression"), 0.0, 1.0, 0.0, v -> aggression = v));

        HBox buttons = new HBox(10);
        Button btnReset = new Button(i18n.get("civilization.button.reset"));
        btnReset.setOnAction(e -> reset());

        ToggleButton btnPause = new ToggleButton(i18n.get("civilization.button.run"));
        btnPause.setSelected(false);
        btnPause.selectedProperty().addListener((o, ov, nv) -> running = nv);

        buttons.getChildren().addAll(btnReset, btnPause);

        box.getChildren().addAll(new Label(i18n.get("civilization.label.params")), sliders, buttons);
        return box;
    }

    private VBox createSlider(String name, double min, double max, double val,
            java.util.function.DoubleConsumer consumer) {
        VBox vb = new VBox(5);
        Label l = new Label(name);
        Slider s = new Slider(min, max, val);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.valueProperty().addListener((o, ov, nv) -> consumer.accept(nv.doubleValue()));
        vb.getChildren().addAll(l, s);
        return vb;
    }

    private void startLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > 50_000_000) { // 20 FPS
                    if (running && population > 0) {
                        step();
                    }
                    last = now;
                }
            }
        };
        timer.start();
    }

    private void step() {
        time += 1.0;

        // 1. Calculations
        double efficiency = 1.0 + (innovationRate * time); // Tech improves efficiency
        double effectiveConsumption = consumptionPerCapita / efficiency;

        // Resource Scarcity Factor
        double scarcity = Math.max(0, resources) / 10000.0;
        if (scarcity > 1.0)
            scarcity = 1.0;

        // Pollution Impact
        double pollutionDeath = pollution * 0.001;

        // Dynamics
        // Birth Rate decays with wealth (demographic transition) but drops with
        // scarcity
        double birthRate = birthRateBase * scarcity;

        // Death Rate rises with pollution and starvation
        // Aggression increases base death rate (wars)
        double deathRate = 0.02 + pollutionDeath + (1.0 - scarcity) * 0.1 + (aggression * 0.05);

        double dPop = population * (birthRate - deathRate);
        double dRes = -(population * effectiveConsumption);
        double dPol = (population * effectiveConsumption * pollutionFactor) - (pollution * 0.05); // Generation - Decay

        // Update
        population += dPop;
        resources += dRes + (100 * regenerationRate); // Regeneration
        pollution += dPol;

        if (population < 0)
            population = 0;
        if (resources < 0)
            resources = 0;
        if (pollution < 0)
            pollution = 0;

        // Update Chart (downsample)
        if (time % 5 == 0) {
            popSeries.getData().add(new XYChart.Data<>(time, population));
            resSeries.getData().add(new XYChart.Data<>(time, resources));
            polSeries.getData().add(new XYChart.Data<>(time, pollution));

            // Limit history
            if (popSeries.getData().size() > 200) {
                popSeries.getData().remove(0);
                resSeries.getData().remove(0);
                polSeries.getData().remove(0);
            }
        }

        updateStatus(dPop);
    }

    private void updateStatus(double dPop) {
        if (population <= 10) {
            statusLabel.setText(i18n.get("civilization.status.extinct"));
            statusLabel.setTextFill(Color.BLACK);
        } else if (dPop < -10) {
            statusLabel.setText(i18n.get("civilization.status.collapse"));
            statusLabel.setTextFill(Color.RED);
        } else if (dPop < 0) {
            statusLabel.setText(i18n.get("civilization.status.declining"));
            statusLabel.setTextFill(Color.ORANGE);
        } else {
            statusLabel.setText(i18n.get("civilization.status.thriving"));
            statusLabel.setTextFill(Color.GREEN);
        }
    }

    private void reset() {
        population = 1000;
        resources = 100000;
        pollution = 0;
        time = 0;
        popSeries.getData().clear();
        resSeries.getData().clear();
        polSeries.getData().clear();
        statusLabel.setText(i18n.get("civilization.status.stable"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
