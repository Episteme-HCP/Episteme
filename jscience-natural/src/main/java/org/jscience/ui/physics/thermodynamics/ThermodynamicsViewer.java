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
package org.jscience.ui.physics.thermodynamics;

import javafx.application.Application;
import javafx.scene.Scene;
import org.jscience.natural.i18n.I18n;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import org.jscience.physics.classical.thermodynamics.IdealGas;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Dashboard for visualizing thermodynamic processes.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermodynamicsViewer extends Application {

    private IdealGas gas;
    private XYChart.Series<Number, Number> pvSeries;
    private Label infoLabel;

    @Override
    public void start(Stage stage) {
        gas = new IdealGas(Real.of(1.0), Real.of(293.15), Real.of(101325)); // 1 mol, 20C, 1 atm

        stage.setTitle(I18n.getInstance().get("thermo.dashboard"));

        // PV Diagram
        NumberAxis xAxis = new NumberAxis("Volume (m^3)", 0, 0.1, 0.01);
        NumberAxis yAxis = new NumberAxis("Pressure (Pa)", 0, 200000, 20000);
        LineChart<Number, Number> pvChart = new LineChart<>(xAxis, yAxis);
        pvChart.setTitle(I18n.getInstance().get("thermo.chart.title"));
        pvChart.setCreateSymbols(false);

        pvSeries = new XYChart.Series<>();
        pvSeries.setName(I18n.getInstance().get("thermo.series.name") + gas.getTemperature() + "K");
        pvChart.getData().add(pvSeries);

        // Controls
        Slider tempSlider = new Slider(100, 500, 293.15);
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);

        infoLabel = new Label(I18n.getInstance().get("thermo.temp.label"));

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateGraph(newVal.doubleValue());
        });

        VBox controls = new VBox(10, new Label(I18n.getInstance().get("thermo.temp.control")), tempSlider, infoLabel);
        controls.setStyle("-fx-padding: 10; -fx-background-color: #16213e;");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setCenter(pvChart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setScene(scene);
        stage.show();

        updateGraph(293.15);
    }

    private void updateGraph(double temperature) {
        pvSeries.getData().clear();
        infoLabel.setText(String.format("Temperature: %.2f K", temperature));

        // Plot P = nRT / V
        double n = 1.0; // moles
        double R = 8.314;
        double T = temperature;
        double const_nRT = n * R * T;

        for (double v = 0.005; v <= 0.1; v += 0.001) {
            double p = const_nRT / v;
            pvSeries.getData().add(new XYChart.Data<>(v, p));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void show(Stage stage) {
        new ThermodynamicsViewer().start(stage);
    }
}
