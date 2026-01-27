/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.chemistry;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Simulatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Chemical Reaction Kinetics Viewer.
 * Simulates and visualizes concentration changes over time (A -> B).
 */
public final class ReactionKineticsViewer extends AbstractViewer implements Simulatable {

    private final LineChart<Number, Number> chart;
    private final XYChart.Series<Number, Number> seriesA = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> seriesB = new XYChart.Series<>();
    
    private double rateConstant = 0.05;
    private double initialA = 100.0;
    private double time = 0;
    private boolean playing = false;

    @SuppressWarnings("unchecked")
    public ReactionKineticsViewer() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Label title = new Label("CHEMICAL KINETICS SIMULATOR");
        title.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 20px; -fx-font-weight: bold;");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Concentration (M)");
        
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Reaction Progress: A → B");
        chart.setCreateSymbols(false);
        chart.getStyleClass().add("premium-chart");

        seriesA.setName("Reactant [A]");
        seriesB.setName("Product [B]");
        chart.getData().addAll(seriesA, seriesB);

        root.getChildren().addAll(title, chart);
        setCenter(root);
        
        resetSimulation();
    }

    private void resetSimulation() {
        seriesA.getData().clear();
        seriesB.getData().clear();
        time = 0;
        updateSeries();
    }

    private void updateSeries() {
        // [A] = [A]0 * e^(-kt)
        double a = initialA * Math.exp(-rateConstant * time);
        double b = initialA - a;
        seriesA.getData().add(new XYChart.Data<>(time, a));
        seriesB.getData().add(new XYChart.Data<>(time, b));
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("Initial [A]", "Starting concentration", 10.0, 500.0, 10.0, 100.0, val -> {
            this.initialA = val;
            resetSimulation();
        }));
        params.add(new NumericParameter("Rate Constant (k)", "Reaction speed", 0.01, 1.0, 0.01, 0.05, val -> {
            this.rateConstant = val;
            resetSimulation();
        }));
        return params;
    }

    @Override public void step() {
        time += 1.0;
        updateSeries();
    }

    @Override public void play() { playing = true; }
    @Override public void pause() { playing = false; }
    @Override public void stop() { playing = false; resetSimulation(); }
    @Override public boolean isPlaying() { return playing; }
    @Override public void setSpeed(double speed) {}

    @Override public String getName() { return "Kinetics Viewer"; }
    @Override public String getCategory() { return "Natural Sciences / Chemistry"; }
    @Override public String getDescription() { return "Simulates first-order reaction kinetics."; }
    @Override public String getLongDescription() { return "A simulation environment for studying chemical reaction rates, concentration decay, and product formation over time."; }
}
