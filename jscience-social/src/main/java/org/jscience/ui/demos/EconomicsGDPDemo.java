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
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.economics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("EconomicsGDP.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("EconomicsGDP.desc");
    }

    @Override
    public void show(Stage stage) {
        NumberAxis xAxis = new NumberAxis(0, 50, 5);
        xAxis.setLabel(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.axis.years"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.axis.gdp"));

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.chart.title"));
        chart.setCreateSymbols(false);

        Slider rateSlider = new Slider(0.5, 5.0, 2.0);
        rateSlider.setShowTickLabels(true);
        Label rateLabel = new Label(
                String.format(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.label.rate_fmt"), 2.0));

        rateSlider.valueProperty().addListener((obs, old, val) -> {
            rateLabel.setText(String.format(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.label.rate_fmt"),
                    val.doubleValue()));
            updateChart(chart, val.doubleValue() / 100.0);
        });

        updateChart(chart, 0.02);

        VBox controls = new VBox(10,
                new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.label.rate_control")), rateSlider,
                rateLabel);
        controls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(chart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.window.title"));
        stage.setScene(scene);
        stage.show();
    }

    private void updateChart(LineChart<Number, Number> chart, double rate) {
        chart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(org.jscience.ui.i18n.SocialI18n.getInstance().get("econ.gdp.chart.series"));
        double gdp = 25.0; // Starting GDP in trillions
        for (int year = 0; year <= 50; year++) {
            series.getData().add(new XYChart.Data<>(year, gdp));
            gdp *= (1 + rate);
        }
        chart.getData().add(series);
    }
}
