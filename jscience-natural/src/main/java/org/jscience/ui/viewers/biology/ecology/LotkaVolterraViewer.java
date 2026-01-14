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

package org.jscience.ui.viewers.biology.ecology;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.analysis.ode.DormandPrinceIntegrator;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Dimensionless;

/**
 * Lotka-Volterra Predator-Prey Dynamics Simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LotkaVolterraViewer extends AbstractViewer implements Simulatable {

    private double alpha = 1.1;
    private double beta = 0.4;
    private double delta = 0.1;
    private double gamma = 0.4;

    private Quantity<Dimensionless> preyPop = Quantities.create(10.0, Units.ONE);
    private Quantity<Dimensionless> predPop = Quantities.create(5.0, Units.ONE);
    private double time = 0;

    private XYChart.Series<Number, Number> preySeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> predSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> phaseSeries = new XYChart.Series<>();

    private boolean running = false;
    private final DormandPrinceIntegrator integrator = new DormandPrinceIntegrator();
    private AnimationTimer timer;
    private double speed = 1.0;
    
    @Override
    public String getName() { return I18n.getInstance().get("lotka.title", "Lotka-Volterra Model"); }
    
    @Override
    public String getCategory() { return "Biology"; }

    public LotkaVolterraViewer() {
        initUI();
    }

    private void initUI() {
        this.setPadding(new Insets(10));
        this.getStyleClass().add("dark-viewer-root");

        VBox chartsBox = new VBox(10);

        NumberAxis xAxisTime = new NumberAxis();
        xAxisTime.setLabel(I18n.getInstance().get("lotka.axis.time", "Time"));
        NumberAxis yAxisPop = new NumberAxis();
        yAxisPop.setLabel(I18n.getInstance().get("lotka.axis.pop", "Population"));

        LineChart<Number, Number> timeChart = new LineChart<>(xAxisTime, yAxisPop);
        timeChart.setTitle(I18n.getInstance().get("lotka.chart.time", "Population Dynamics"));
        timeChart.setCreateSymbols(false);
        preySeries.setName(I18n.getInstance().get("lotka.series.prey", "Prey"));
        predSeries.setName(I18n.getInstance().get("lotka.series.pred", "Predator"));
        @SuppressWarnings({"unchecked", "unused"})
        var unused = timeChart.getData().addAll(preySeries, predSeries);

        NumberAxis xAxisPhase = new NumberAxis();
        xAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.prey", "Prey"));
        NumberAxis yAxisPhase = new NumberAxis();
        yAxisPhase.setLabel(I18n.getInstance().get("lotka.axis.pred", "Predator"));

        ScatterChart<Number, Number> phaseChart = new ScatterChart<>(xAxisPhase, yAxisPhase);
        phaseChart.setTitle(I18n.getInstance().get("lotka.chart.phase", "Phase Portrait"));
        phaseSeries.setName(I18n.getInstance().get("lotka.series.traj", "Trajectory"));
        phaseChart.getData().add(phaseSeries);
        phaseChart.setLegendVisible(false);

        chartsBox.getChildren().addAll(timeChart, phaseChart);
        this.setCenter(chartsBox);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        sidebar.getChildren().addAll(
                new Label(I18n.getInstance().get("lotka.header.params", "Parameters")),
                createSliderLabel(I18n.getInstance().get("lotka.label.alpha", "Alpha"), 0, 5, alpha, v -> alpha = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.beta", "Beta"), 0, 5, beta, v -> beta = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.delta", "Delta"), 0, 5, delta, v -> delta = v),
                createSliderLabel(I18n.getInstance().get("lotka.label.gamma", "Gamma"), 0, 5, gamma, v -> gamma = v));

        Button resetBtn = new Button(I18n.getInstance().get("lotka.btn.reset", "Reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> reset());

        sidebar.getChildren().addAll(new Separator(), resetBtn);
        this.setLeft(sidebar);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) step();
            }
        };
        timer.start();
    }
    
    @Override public void play() { running = true; timer.start(); }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; reset(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }

    @Override public void step() {
        double dt = 0.05 * speed;
        double[] current = { preyPop.getValue().doubleValue(), predPop.getValue().doubleValue() };

        double[] next = integrator.integrate((t, y) -> {
            double dy1 = alpha * y[0] - beta * y[0] * y[1];
            double dy2 = delta * y[0] * y[1] - gamma * y[1];
            return new double[] { dy1, dy2 };
        }, time, current, time + dt);

        preyPop = Quantities.create(Math.max(0, next[0]), Units.ONE);
        predPop = Quantities.create(Math.max(0, next[1]), Units.ONE);
        time += dt;

        double xVal = preyPop.getValue().doubleValue();
        double yVal = predPop.getValue().doubleValue();

        preySeries.getData().add(new XYChart.Data<>(time, xVal));
        predSeries.getData().add(new XYChart.Data<>(time, yVal));
        phaseSeries.getData().add(new XYChart.Data<>(xVal, yVal));

        if (preySeries.getData().size() > 500) {
            preySeries.getData().remove(0);
            predSeries.getData().remove(0);
        }
        if (phaseSeries.getData().size() > 500) {
            phaseSeries.getData().remove(0);
        }
    }

    public void reset() {
        time = 0;
        preyPop = Quantities.create(10.0, Units.ONE);
        predPop = Quantities.create(5.0, Units.ONE);
        preySeries.getData().clear();
        predSeries.getData().clear();
        phaseSeries.getData().clear();
    }

    private VBox createSliderLabel(String name, double min, double max, double val, java.util.function.DoubleConsumer consumer) {
        Label lbl = new Label(name + ": " + String.format("%.2f", val));
        Slider s = new Slider(min, max, val);
        s.valueProperty().addListener((o, ov, nv) -> {
            consumer.accept(nv.doubleValue());
            lbl.setText(name + ": " + String.format("%.2f", nv.doubleValue()));
        });
        return new VBox(5, lbl, s);
    }
}
