/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.engineering;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.AreaChart;

import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.KillerAppBase;

import java.util.Random;

/**
 * Smart Grid Balancer Application.
 * <p>
 * Simulates city-scale power distribution, renewable energy integration,
 * and load balancing to prevent blackouts.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SmartGridApp extends KillerAppBase {

    // UI Components
    private AreaChart<Number, Number> loadChart;
    private XYChart.Series<Number, Number> supplySeries;
    private XYChart.Series<Number, Number> demandSeries;
    private Label frequencyLabel;
    private Label statusLabel;
    private ProgressBar loadBalanceBar;

    // Sliders
    private Slider coalOutputSlider;
    private Slider windCapacitySlider;
    private Slider solarCapacitySlider;
    private Slider demandScaleSlider;

    // Simulation State
    private double currentSupply = 0;
    private double currentDemand = 0;
    private double gridFrequency = 50.0; // Hz
    private double time = 0;
    private boolean blackout = false;

    // Battery Storage
    private double batteryCapacity = 200.0; // MWh
    private double batteryCharge = 100.0; // Current charge MWh
    private double batteryMaxPower = 50.0; // Max charge/discharge rate MW
    private Label batteryLabel;
    private ProgressBar batteryBar;

    // Grid Simulation
    private final Random rand = new Random();
    private Timeline loop;
    private Canvas gridCanvas;

    @Override
    protected String getAppTitle() {
        return i18n.get("grid.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        // Left: Network Visualization
        VBox visPane = createVisualizationPane();

        // Center: Charts
        VBox chartsPane = createChartsPane();

        // Right: Control Room
        VBox controlsPane = createControlRoom();

        mainSplit.getItems().addAll(visPane, chartsPane, controlsPane);
        mainSplit.setDividerPositions(0.3, 0.7);

        return mainSplit;
    }

    private VBox createVisualizationPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label title = new Label("🏙️ Grid Network Map");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        gridCanvas = new Canvas(300, 500);

        box.getChildren().addAll(title, gridCanvas);
        VBox.setVgrow(gridCanvas, Priority.ALWAYS);

        return box;
    }

    private VBox createChartsPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        loadChart = ChartFactory.createAreaChart("Grid Stability (MW)", "Time", "Power (MW)");
        loadChart.setAnimated(false);
        loadChart.setCreateSymbols(false);

        supplySeries = new XYChart.Series<>();
        supplySeries.setName("Total Supply");

        demandSeries = new XYChart.Series<>();
        demandSeries.setName("Total Demand");

        @SuppressWarnings("unchecked")
        XYChart.Series<Number, Number>[] series = new XYChart.Series[] { supplySeries, demandSeries };
        loadChart.getData().addAll(series);

        // System Status
        HBox statusBox = new HBox(20);
        statusBox.setAlignment(javafx.geometry.Pos.CENTER);

        frequencyLabel = new Label("50.00 Hz");
        frequencyLabel.setStyle("-fx-font-size: 24px; -fx-font-family: monospace; -fx-font-weight: bold;");

        statusLabel = new Label("STABLE");
        statusLabel.setStyle(
                "-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");

        loadBalanceBar = new ProgressBar(0.5);
        loadBalanceBar.setPrefWidth(200);

        statusBox.getChildren().addAll(new Label("Freq:"), frequencyLabel, loadBalanceBar, statusLabel);

        box.getChildren().addAll(loadChart, statusBox);
        VBox.setVgrow(loadChart, Priority.ALWAYS);
        return box;
    }

    private VBox createControlRoom() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #2b2b2b;"); // Dark control room theme

        Label title = new Label("🎛️ Control Room");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Coal
        VBox coalBox = createControlGroup("🏭 Coal/Gas Baseload", 0, 1000, 500, Color.ORANGE);
        coalOutputSlider = (Slider) coalBox.getChildren().get(1);

        // Wind
        VBox windBox = createControlGroup("💨 Wind Farm Capacity", 0, 500, 100, Color.CYAN);
        windCapacitySlider = (Slider) windBox.getChildren().get(1);

        // Solar
        VBox solarBox = createControlGroup("☀️ Solar Array Capacity", 0, 500, 100, Color.YELLOW);
        solarCapacitySlider = (Slider) solarBox.getChildren().get(1);

        // Demand
        VBox demandBox = createControlGroup("🏙️ City Demand Scaling", 50, 200, 100, Color.MAGENTA);
        demandScaleSlider = (Slider) demandBox.getChildren().get(1);

        // Battery Status
        VBox batteryBox = new VBox(5);
        Label batteryTitle = new Label("🔋 Battery Storage");
        batteryTitle.setTextFill(Color.LIGHTGREEN);
        batteryTitle.setStyle("-fx-font-weight: bold;");

        batteryBar = new ProgressBar(0.5);
        batteryBar.setMaxWidth(Double.MAX_VALUE);
        batteryBar.setStyle("-fx-accent: #27ae60;");

        batteryLabel = new Label(String.format("%.0f / %.0f MWh", batteryCharge, batteryCapacity));
        batteryLabel.setTextFill(Color.LIGHTGRAY);

        batteryBox.getChildren().addAll(batteryTitle, batteryBar, batteryLabel);

        Button tripReset = new Button("🔄 Manual Trip Reset");
        tripReset.setMaxWidth(Double.MAX_VALUE);
        tripReset.setOnAction(e -> resetSimulation());

        box.getChildren().addAll(title, coalBox, windBox, solarBox, new Separator(), demandBox, batteryBox, tripReset);
        return box;
    }

    private VBox createControlGroup(String title, double min, double max, double val, Color color) {
        VBox box = new VBox(5);
        Label lbl = new Label(title);
        lbl.setTextFill(color);
        lbl.setStyle("-fx-font-weight: bold;");

        Slider sli = new Slider(min, max, val);
        sli.setShowTickLabels(true);
        sli.setShowTickMarks(true);

        Label valLbl = new Label(String.format("%.0f MW", val));
        valLbl.setTextFill(Color.LIGHTGRAY);
        sli.valueProperty().addListener((o, ov, nv) -> valLbl.setText(String.format("%.0f MW", nv.doubleValue())));

        box.getChildren().addAll(lbl, sli, valLbl);
        return box;
    }

    @Override
    public void onRun() {
        if (loop != null)
            loop.stop();
        blackout = false;

        loop = new Timeline(new KeyFrame(Duration.millis(100), e -> updateTick()));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
        setStatus(i18n.get("status.running"));
    }

    private void updateTick() {
        if (blackout)
            return;

        time += 0.1;

        // Simulate fluctuating weather
        double windFactor = Math.abs(Math.sin(time * 0.2) + Math.sin(time * 0.7) * 0.5) * 0.8;
        double solarFactor = Math.max(0, Math.sin(time * 0.1)); // Day/Night cycle

        // Base Demand with diurnal curve
        double baseDemand = 400 + 200 * Math.sin(time * 0.1 - 1);
        baseDemand += (rand.nextDouble() - 0.5) * 50;

        currentDemand = baseDemand * (demandScaleSlider.getValue() / 100.0);

        // Supply from generation
        double coal = coalOutputSlider.getValue();
        double wind = windCapacitySlider.getValue() * windFactor;
        double solar = solarCapacitySlider.getValue() * solarFactor;

        double generation = coal + wind + solar;

        // Battery logic: charge when excess, discharge when deficit
        double imbalance = generation - currentDemand;
        double batteryPower = 0;

        if (imbalance > 0 && batteryCharge < batteryCapacity) {
            // Excess power: charge battery
            batteryPower = Math.min(imbalance, batteryMaxPower);
            batteryPower = Math.min(batteryPower, batteryCapacity - batteryCharge);
            batteryCharge += batteryPower * 0.1; // 0.1 hour per tick
        } else if (imbalance < 0 && batteryCharge > 0) {
            // Power deficit: discharge battery
            batteryPower = Math.min(-imbalance, batteryMaxPower);
            batteryPower = Math.min(batteryPower, batteryCharge);
            batteryCharge -= batteryPower * 0.1;
            imbalance += batteryPower; // Battery helps cover deficit
        }

        // Update battery UI
        batteryBar.setProgress(batteryCharge / batteryCapacity);
        batteryLabel.setText(String.format("%.0f / %.0f MWh", batteryCharge, batteryCapacity));

        // Effective supply includes battery contribution
        currentSupply = generation + (imbalance < 0 ? batteryPower : 0);

        // Grid Physics (Simplified Inertia)
        double balance = currentSupply - currentDemand;
        double deltaF = (balance / 1000.0) * 0.5;
        gridFrequency += deltaF * 0.1;
        gridFrequency += (50.0 - gridFrequency) * 0.02;

        updateCharts();
        updateStatus();
        drawGridVisualization(windFactor, solarFactor);

        // Blackout conditions
        if (gridFrequency < 48.0 || gridFrequency > 52.0) {
            triggerBlackout("Frequency Instability: " + String.format("%.2f", gridFrequency) + " Hz");
        }
    }

    private void updateCharts() {
        // Optimize chart performance by limiting data points
        if (supplySeries.getData().size() > 100) {
            supplySeries.getData().remove(0);
            demandSeries.getData().remove(0);
        }

        supplySeries.getData().add(new XYChart.Data<>(time, currentSupply));
        demandSeries.getData().add(new XYChart.Data<>(time, currentDemand));
    }

    private void updateStatus() {
        frequencyLabel.setText(String.format("%.2f Hz", gridFrequency));
        frequencyLabel.setTextFill(Math.abs(gridFrequency - 50) < 0.2 ? Color.BLACK : Color.RED);

        if (Math.abs(gridFrequency - 50) < 0.5) {
            statusLabel.setText("STABLE");
            statusLabel.setStyle(
                    "-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        } else {
            statusLabel.setText("UNSTABLE");
            statusLabel.setStyle(
                    "-fx-font-size: 18px; -fx-background-color: #FF5722; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        }

        // Bar balance: 0.5 is perfectly balanced. >0.5 supply heavy.
        double ratio = currentSupply / (currentDemand + 1); // Avoid div 0
        // Map ratio to 0..1 where 1 = 0.5
        double barVal = 0.5 + (ratio - 1.0) * 0.5;
        barVal = Math.max(0, Math.min(1, barVal));
        loadBalanceBar.setProgress(barVal);
    }

    private void drawGridVisualization(double windF, double solarF) {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        double w = gridCanvas.getWidth();
        double h = gridCanvas.getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#eee"));
        gc.fillRect(0, 0, w, h);

        // Center: City
        double cx = w / 2, cy = h / 2;

        // Draw connections
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(cx, cy, cx - 80, cy - 100); // To Wind
        gc.strokeLine(cx, cy, cx + 80, cy - 100); // To Solar
        gc.strokeLine(cx, cy, cx, cy + 120); // To Coal

        // Draw City
        gc.setFill(blackout ? Color.BLACK : Color.VIOLET);
        gc.fillOval(cx - 30, cy - 30, 60, 60);
        gc.setFill(Color.WHITE);
        gc.fillText("CITY\n" + (int) currentDemand + "MW", cx - 15, cy);

        // Draw Wind
        gc.setFill(Color.CYAN);
        gc.fillOval(cx - 100, cy - 120, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText("WIND\n" + (int) (windF * 100) + "%", cx - 95, cy - 100);

        // Draw Solar
        gc.setFill(solarF > 0.1 ? Color.YELLOW : Color.DARKGRAY);
        gc.fillOval(cx + 60, cy - 120, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText("SUN\n" + (int) (solarF * 100) + "%", cx + 65, cy - 100);

        // Draw Coal
        gc.setFill(Color.ORANGE);
        gc.fillRect(cx - 20, cy + 100, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText("PLANT", cx - 15, cy + 125);
    }

    private void triggerBlackout(String reason) {
        blackout = true;
        statusLabel.setText("BLACKOUT");
        statusLabel.setStyle(
                "-fx-font-size: 18px; -fx-background-color: #000000; -fx-text-fill: red; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        log("💥 GRID FAILURE: " + reason);
        loop.stop();
    }

    private void resetSimulation() {
        gridFrequency = 50.0;
        time = 0;
        supplySeries.getData().clear();
        demandSeries.getData().clear();
        onRun();
    }

    private void log(String msg) {
        // System.out.println(msg);
        // Could hook to a listview if we added one
    }

    @Override
    public void onStop() {
        if (loop != null)
            loop.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
