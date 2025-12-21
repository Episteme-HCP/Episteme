/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Economic Growth Model Demo.
 * Demonstrates GDP growth projection using simple exponential model.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class EconomicsDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Economics";
    }

    @Override
    public String getName() {
        return "GDP Growth Model";
    }

    @Override
    public void show(Stage stage) {
        NumberAxis xAxis = new NumberAxis(0, 50, 5);
        xAxis.setLabel("Years");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("GDP (Trillion $)");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("GDP Growth Projection");
        chart.setCreateSymbols(false);

        Slider rateSlider = new Slider(0.5, 5.0, 2.0);
        rateSlider.setShowTickLabels(true);
        Label rateLabel = new Label("Growth Rate: 2.0%");

        rateSlider.valueProperty().addListener((obs, old, val) -> {
            rateLabel.setText(String.format("Growth Rate: %.1f%%", val.doubleValue()));
            updateChart(chart, val.doubleValue() / 100.0);
        });

        updateChart(chart, 0.02);

        VBox controls = new VBox(10, new Label("Annual Growth Rate"), rateSlider, rateLabel);
        controls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(chart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("JScience - GDP Growth Model");
        stage.setScene(scene);
        stage.show();
    }

    private void updateChart(LineChart<Number, Number> chart, double rate) {
        chart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Projected GDP");
        double gdp = 25.0; // Starting GDP in trillions
        for (int year = 0; year <= 50; year++) {
            series.getData().add(new XYChart.Data<>(year, gdp));
            gdp *= (1 + rate);
        }
        chart.getData().add(series);
    }
}
