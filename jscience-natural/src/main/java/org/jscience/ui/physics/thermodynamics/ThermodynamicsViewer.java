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
import org.jscience.ui.i18n.I18n;
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
    private XYChart.Series<Number, Number> cycleSeries;
    private Label infoLabel;
    private Label cycleDesc;

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

        cycleSeries = new XYChart.Series<>();
        cycleSeries.setName("Thermodynamic Cycle");
        pvChart.getData().add(cycleSeries);

        // Controls
        Slider tempSlider = new Slider(100, 500, 293.15);
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);

        infoLabel = new Label(I18n.getInstance().get("thermo.temp.label"));
        cycleDesc = new Label("");
        cycleDesc.setWrapText(true);
        cycleDesc.setStyle("-fx-text-fill: #aaa; -fx-font-style: italic;");

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateGraph(newVal.doubleValue());
        });

        javafx.scene.control.Button carnotBtn = new javafx.scene.control.Button(
                I18n.getInstance().get("thermo.btn.carnot", "Show Carnot Cycle"));
        carnotBtn.setMaxWidth(Double.MAX_VALUE);
        carnotBtn.setOnAction(e -> drawCarnotCycle());

        javafx.scene.control.Button ottoBtn = new javafx.scene.control.Button(
                I18n.getInstance().get("thermo.btn.otto", "Show Otto Cycle"));
        ottoBtn.setMaxWidth(Double.MAX_VALUE);
        ottoBtn.setOnAction(e -> drawOttoCycle());

        javafx.scene.control.Button clearBtn = new javafx.scene.control.Button(
                I18n.getInstance().get("thermo.btn.clear", "Clear Cycle"));
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> cycleSeries.getData().clear());

        cycleDesc.setStyle(
                "-fx-text-fill: #ddd; -fx-font-style: italic; -fx-padding: 10; -fx-background-color: #444; -fx-background-radius: 5;");
        cycleDesc.setPrefWidth(250);

        VBox controls = new VBox(10, new Label(I18n.getInstance().get("thermo.temp.control")), tempSlider, infoLabel,
                new javafx.scene.control.Separator(), carnotBtn, ottoBtn, clearBtn,
                new javafx.scene.control.Separator(), cycleDesc);
        controls.setPadding(new javafx.geometry.Insets(15));
        controls.getStyleClass().add("dark-viewer-sidebar");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");
        root.setCenter(pvChart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        // org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
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

    private void drawCarnotCycle() {
        cycleDesc.setText(I18n.getInstance().get("thermo.cycle.carnot"));
        cycleSeries.getData().clear();
        // Carnot Cycle: Isotherm Exp -> Adiabat Exp -> Isotherm Comp -> Adiabat Comp
        double T1 = 400; // High Temp
        double T2 = 300; // Low Temp
        double V1 = 0.02;
        double R = 8.314;
        double n = 1.0;
        double gamma = 1.4;

        // Point 1 (V1, P1) is implicitly the start of the first isotherm
        // double P1 = n * R * T1 / V1;

        // 1->2 Isothermal Expansion at T1 to V2
        double V2 = 0.05;
        for (double v = V1; v <= V2; v += 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, n * R * T1 / v));
        }

        // 2->3 Adiabatic Expansion to T2
        // T1 * V2^(g-1) = T2 * V3^(g-1) => V3 = V2 * (T1/T2)^(1/(g-1))
        double V3 = V2 * Math.pow(T1 / T2, 1.0 / (gamma - 1.0));
        double constAdiabat1 = (n * R * T2 / V3) * Math.pow(V3, gamma); // PV^g = const

        for (double v = V2; v <= V3; v += 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, constAdiabat1 / Math.pow(v, gamma)));
        }

        // 3->4 Isothermal Compression at T2 to V4
        // V4 matches adiabatic compression back to V1?
        // T1 * V1^(g-1) = T2 * V4^(g-1) => V4 = V1 * (T1/T2)^(1/(g-1))
        double V4 = V1 * Math.pow(T1 / T2, 1.0 / (gamma - 1.0));

        for (double v = V3; v >= V4; v -= 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, n * R * T2 / v));
        }

        // 4->1 Adiabatic Compression
        double constAdiabat2 = (n * R * T1 / V1) * Math.pow(V1, gamma);
        for (double v = V4; v >= V1; v -= 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, constAdiabat2 / Math.pow(v, gamma)));
        }
    }

    private void drawOttoCycle() {
        cycleDesc.setText(I18n.getInstance().get("thermo.cycle.otto"));
        cycleSeries.getData().clear();
        // Otto Cycle: Adiabat Comp -> Isochoric Heat -> Adiabat Exp -> Isochoric Cool
        double V1 = 0.08; // Bottom dead center
        double V2 = 0.01; // Top dead center (Compression ratio 8)
        double P1 = 101325; // 1 atm start
        double gamma = 1.4;

        // 1->2 Adiabatic Compression
        double constAdiabat1 = P1 * Math.pow(V1, gamma);
        for (double v = V1; v >= V2; v -= 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, constAdiabat1 / Math.pow(v, gamma)));
        }

        // 2->3 Isochoric Heating (Explosion)
        double P2 = constAdiabat1 / Math.pow(V2, gamma);
        double P3 = P2 * 3; // Pressure jumps 3x
        cycleSeries.getData().add(new XYChart.Data<>(V2, P2));
        cycleSeries.getData().add(new XYChart.Data<>(V2, P3));

        // 3->4 Adiabatic Expansion
        double constAdiabat2 = P3 * Math.pow(V2, gamma);
        for (double v = V2; v <= V1; v += 0.001) {
            cycleSeries.getData().add(new XYChart.Data<>(v, constAdiabat2 / Math.pow(v, gamma)));
        }

        // 4->1 Isochoric Cooling (Exhaust)
        cycleSeries.getData().add(new XYChart.Data<>(V1, constAdiabat2 / Math.pow(V1, gamma)));
        cycleSeries.getData().add(new XYChart.Data<>(V1, P1));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void show(Stage stage) {
        new ThermodynamicsViewer().start(stage);
    }
}
