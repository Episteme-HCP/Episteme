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
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.analysis.ode.DormandPrinceIntegrator;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Dimensionless;

import java.util.ArrayList;
import java.util.List;

/**
 * Lotka-Volterra Predator-Prey Dynamics Simulation.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Lotka, A. J. (1925). <i>Elements of Physical Biology</i>. Williams & Wilkins.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LotkaVolterraViewer extends AbstractViewer implements Simulatable {

    private RealParameter alphaParam;
    private RealParameter betaParam;
    private RealParameter deltaParam;
    private RealParameter gammaParam;

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
    public String getName() { return I18n.getInstance().get("viewer.lotkavolterra.name", "Lotka-Volterra Model"); }
    
    @Override
    public String getCategory() { return I18n.getInstance().get("category.biology", "Biology"); }

    public LotkaVolterraViewer() {
        initParameters();
        initUI();
    }

    private void initParameters() {
        alphaParam = new RealParameter("Alpha", "Prey Growth Rate", 0.0, 5.0, 0.1, 1.1, null);
        betaParam = new RealParameter("Beta", "Predation Rate", 0.0, 5.0, 0.1, 0.4, null);
        deltaParam = new RealParameter("Delta", "Predator Growth Rate", 0.0, 5.0, 0.1, 0.1, null);
        gammaParam = new RealParameter("Gamma", "Predator Death Rate", 0.0, 5.0, 0.1, 0.4, null);
    }

    private void initUI() {
        this.setPadding(new Insets(10));
        this.getStyleClass().add("viewer-root");

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
        sidebar.getStyleClass().add("viewer-sidebar");

        sidebar.getChildren().addAll(
                new Label(I18n.getInstance().get("lotka.header.params", "Parameters")),
                createParameterControl(alphaParam),
                createParameterControl(betaParam),
                createParameterControl(deltaParam),
                createParameterControl(gammaParam)
        );

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

    // Helper to generate UI control from Parameter manually (duplicating AbstractDemo logic mostly, or could expose it)
    // Since AbstractViewer doesn't provide UI generation, this Viewer builds its own.
    // Ideally we should use the same builder as AbstractDemo. 
    // Here we implement a simple one for the Sidebar.
    private Node createParameterControl(RealParameter param) {
        VBox box = new VBox(3);
        Label label = new Label(param.getName());
        Slider slider = new Slider(param.getMin().doubleValue(), param.getMax().doubleValue(), param.getValue().doubleValue());
        slider.setBlockIncrement(param.getStep().doubleValue());
        slider.setShowTickLabels(true);

        Label valLabel = new Label(String.format("%.2f", param.getValue().doubleValue()));
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            param.setValue(Real.of(newVal.doubleValue()));
            valLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });

        box.getChildren().addAll(label, slider, valLabel);
        return box;
    }

    @Override public void play() { running = true; timer.start(); }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; reset(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }

    @Override public void step() {
        double dt = 0.05 * speed;
        double[] current = { preyPop.getValue().doubleValue(), predPop.getValue().doubleValue() };
        
        // Cache parameters for the step (Optimization)
        double alpha = alphaParam.getValue().doubleValue();
        double beta = betaParam.getValue().doubleValue();
        double delta = deltaParam.getValue().doubleValue();
        double gamma = gammaParam.getValue().doubleValue();

        double[] next = integrator.integrate((Double t, double[] y) -> {
            double rY0 = y[0];
            double rY1 = y[1];
            double dY1 = alpha * rY0 - beta * rY0 * rY1;
            double dY2 = delta * rY0 * rY1 - gamma * rY1;
            return new double[] { dY1, dY2 };
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

    @Override public String getDescription() { return I18n.getInstance().get("viewer.lotkavolterra.desc", "Dynamic predator-prey ecosystem with adjustable parameters."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.lotkavolterra.longdesc", "Predicts the population dynamics of two species that interact, one as a predator and the other as prey. Adjust growth, predation, and death rates to observe cyclic population shifts."); }
    
    @Override public List<Parameter<?>> getViewerParameters() { 
        List<Parameter<?>> params = new ArrayList<>();
        params.add(alphaParam);
        params.add(betaParam);
        params.add(deltaParam);
        params.add(gammaParam);
        return params;
    }
}

