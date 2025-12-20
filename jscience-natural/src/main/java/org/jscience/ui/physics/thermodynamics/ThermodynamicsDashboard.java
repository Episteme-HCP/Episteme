package org.jscience.ui.physics.thermodynamics;

import javafx.application.Application;
import javafx.scene.Scene;
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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ThermodynamicsDashboard extends Application {

    private IdealGas gas;
    private XYChart.Series<Number, Number> pvSeries;
    private Label infoLabel;

    @Override
    public void start(Stage stage) {
        gas = new IdealGas(Real.of(1.0), Real.of(293.15), Real.of(101325)); // 1 mol, 20C, 1 atm

        stage.setTitle("Thermodynamics Dashboard");

        // PV Diagram
        NumberAxis xAxis = new NumberAxis("Volume (m^3)", 0, 0.1, 0.01);
        NumberAxis yAxis = new NumberAxis("Pressure (Pa)", 0, 200000, 20000);
        LineChart<Number, Number> pvChart = new LineChart<>(xAxis, yAxis);
        pvChart.setTitle("PV Diagram (Isothermal Expansion)");
        pvChart.setCreateSymbols(false);

        pvSeries = new XYChart.Series<>();
        pvSeries.setName("Isotherm T=" + gas.getTemperature() + "K");
        pvChart.getData().add(pvSeries);

        // Controls
        Slider tempSlider = new Slider(100, 500, 293.15);
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);

        infoLabel = new Label("Temperature: 293.15 K");

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateGraph(newVal.doubleValue());
        });

        VBox controls = new VBox(10, new Label("Temperature (K)"), tempSlider, infoLabel);
        controls.setStyle("-fx-padding: 10;");

        BorderPane root = new BorderPane();
        root.setCenter(pvChart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
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
}
