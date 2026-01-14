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

package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.AbstractDemo;

public class EconomicsMarketDemo extends AbstractDemo {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.economics", "Economics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.desc");
    }

    @Override
    public void start(Stage stage) {
        initUI();
        super.start(stage);
    }
    
    private void initUI() {
        BorderPane root = new BorderPane();

        // Axes
        NumberAxis xAxis = new NumberAxis(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.axis.quantity"),
                0, 100, 10);
        NumberAxis yAxis = new NumberAxis(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.axis.price"),
                0,
                100, 10);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.chart.title"));
        lineChart.setAnimated(false); // crucial for smooth slider updates

        XYChart.Series<Number, Number> supplySeries = new XYChart.Series<>();
        supplySeries.setName(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.series.supply"));

        XYChart.Series<Number, Number> demandSeries = new XYChart.Series<>();
        demandSeries.setName(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.series.demand"));

        lineChart.getData().add(supplySeries);
        lineChart.getData().add(demandSeries);

        // Controls
        Slider supplyShift = new Slider(-20, 20, 0);
        Slider demandShift = new Slider(-20, 20, 0);

        Runnable updateGraph = () -> {
            org.jscience.mathematics.numbers.real.Real sShift = org.jscience.mathematics.numbers.real.Real
                    .of(supplyShift.getValue());
            org.jscience.mathematics.numbers.real.Real dShift = org.jscience.mathematics.numbers.real.Real
                    .of(demandShift.getValue());

            ObservableList<XYChart.Data<Number, Number>> sData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data<Number, Number>> dData = FXCollections.observableArrayList();

            for (int q = 0; q <= 100; q += 10) {
                org.jscience.mathematics.numbers.real.Real quantity = org.jscience.mathematics.numbers.real.Real.of(q);

                // Supply: P = 0.5Q + 10 + shift
                org.jscience.mathematics.numbers.real.Real pSupply = quantity
                        .multiply(org.jscience.mathematics.numbers.real.Real.of(0.5))
                        .add(org.jscience.mathematics.numbers.real.Real.of(10))
                        .add(sShift);

                sData.add(new XYChart.Data<>(q, Math.max(0, pSupply.doubleValue())));

                // Demand: P = -0.5Q + 90 + shift
                org.jscience.mathematics.numbers.real.Real pDemand = quantity
                        .multiply(org.jscience.mathematics.numbers.real.Real.of(-0.5))
                        .add(org.jscience.mathematics.numbers.real.Real.of(90))
                        .add(dShift);

                dData.add(new XYChart.Data<>(q, Math.max(0, pDemand.doubleValue())));
            }

            supplySeries.setData(sData);
            demandSeries.setData(dData);
        };

        supplyShift.valueProperty().addListener(e -> updateGraph.run());
        demandShift.valueProperty().addListener(e -> updateGraph.run());
        updateGraph.run(); // Init

        VBox controls = new VBox(10,
                new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.label.shift_supply")),
                supplyShift,
                new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.market.label.shift_demand")),
                demandShift);
        controls.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        root.setCenter(lineChart);
        root.setBottom(controls);
        
        this.viewer = root;
    }
    
    @Override
    protected String getLongDescription() {
        return getDescription();
    }
}
