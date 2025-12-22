/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

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
import org.jscience.ui.DemoProvider;

/**
 * Economic Growth Model Demo.
 * Demonstrates GDP growth projection using simple exponential model.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class EconomicsGDPDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Economics";
    }

    @Override
    public String getName() {
        return "GDP Growth Model";
    }

    @Override
    public String getDescription() {
        return "Project GDP growth over 50 years with adjustable rates.";
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
