package org.jscience.apps.biology.pandemic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.jscience.ui.DemoProvider;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.biology.ecology.Epidemiology;

/**
 * Pandemic Forecaster (Killer App #4).
 * Uses SIR models to forecast disease spread.
 */
public class PandemicForecaster implements DemoProvider {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Pandemic Forecaster";
    }

    @Override
    public String getDescription() {
        return "Simulate infectious disease outbreaks using the SIR compartment model.";
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Controls
        Slider betaSlider = new Slider(0.0, 1.0, 0.3);
        Label betaLabel = new Label("Transmission Rate (Beta): 0.3");

        Slider gammaSlider = new Slider(0.0, 1.0, 0.1);
        Label gammaLabel = new Label("Recovery Rate (Gamma): 0.1");
        
        Label r0Label = new Label("R0: 3.0");
        Label hitLabel = new Label("Herd Immunity Threshold: 66%");

        Runnable updateLabels = () -> {
            betaLabel.setText(String.format("Transmission Rate (\u03B2): %.2f", betaSlider.getValue()));
            gammaLabel.setText(String.format("Recovery Rate (\u03B3): %.2f", gammaSlider.getValue()));
            
            Real beta = Real.of(betaSlider.getValue());
            Real gamma = Real.of(Math.max(0.001, gammaSlider.getValue()));
            Real r0 = Epidemiology.basicReproductionNumber(beta, gamma);
            Real hit = Epidemiology.herdImmunityThreshold(r0);
            
            r0Label.setText(String.format("R0: %.2f", r0.doubleValue()));
            hitLabel.setText(String.format("Herd Immunity Threshold: %.1f%%", hit.doubleValue() * 100));
        };

        // Chart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (Days)");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Population");
        
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("SIR Model Projection");
        lineChart.setCreateSymbols(false);
        
        XYChart.Series<Number, Number> sSeries = new XYChart.Series<>();
        sSeries.setName("Susceptible");
        
        XYChart.Series<Number, Number> iSeries = new XYChart.Series<>();
        iSeries.setName("Infectious");
        
        XYChart.Series<Number, Number> rSeries = new XYChart.Series<>();
        rSeries.setName("Recovered");
        
        lineChart.getData().addAll(sSeries, iSeries, rSeries);

        // Simulation Loop
        Runnable runSimulation = () -> {
            updateLabels.run();
            
            Real beta = Real.of(betaSlider.getValue());
            Real gamma = Real.of(Math.max(0.001, gammaSlider.getValue()));
            
            // Initial conditions
            Real N = Real.of(1000); // Total population
            Real I0 = Real.of(1);
            Real R0 = Real.ZERO;
            Real S0 = N.subtract(I0);
            
            int steps = 100;
            Real dt = Real.ONE; // 1 day steps
            
            Real[][] results = Epidemiology.sirModel(S0, I0, R0, beta, gamma, dt, steps);
            
            ObservableList<XYChart.Data<Number, Number>> sData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data<Number, Number>> iData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data<Number, Number>> rData = FXCollections.observableArrayList();
            
            for (int t = 0; t < steps; t++) {
                sData.add(new XYChart.Data<>(t, results[t][0].doubleValue()));
                iData.add(new XYChart.Data<>(t, results[t][1].doubleValue()));
                rData.add(new XYChart.Data<>(t, results[t][2].doubleValue()));
            }
            
            sSeries.setData(sData);
            iSeries.setData(iData);
            rSeries.setData(rData);
        };

        betaSlider.valueProperty().addListener(e -> runSimulation.run());
        gammaSlider.valueProperty().addListener(e -> runSimulation.run());
        
        // Initial run
        runSimulation.run();

        VBox controls = new VBox(10, betaLabel, betaSlider, gammaLabel, gammaSlider, new Separator(), r0Label, hitLabel);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #f0f0f0; -fx-min-width: 250;");
        
        root.setLeft(controls);
        root.setCenter(lineChart);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
