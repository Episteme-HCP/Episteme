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

package org.jscience.apps.engineering;

import java.util.Random;
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
import org.jscience.apps.framework.FeaturedAppBase;

import java.text.MessageFormat;

public class SmartGridApp extends FeaturedAppBase {
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
    private Label vizTitleLabel;
    private Label controlRoomTitleLabel;
    private Label coalLabel;
    private Label windLabel;
    private Label solarLabel;
    private Label demandLabel;
    private Label batteryTitleLabel;

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
    private Random rand;
    private Timeline loop;
    private Canvas gridCanvas;

    public SmartGridApp() {
        super();
        try {
            this.rand = new Random();
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize SmartGridApp: " + t.getMessage());
            t.printStackTrace();
        }
    }

    @Override
    protected String getAppTitle() {
        return i18n.get("grid.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("grid.desc");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
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

        vizTitleLabel = new Label(i18n.get("grid.viz.title"));
        vizTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        gridCanvas = new Canvas(300, 500);

        box.getChildren().addAll(vizTitleLabel, gridCanvas);
        VBox.setVgrow(gridCanvas, Priority.ALWAYS);

        return box;
    }

    private VBox createChartsPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        loadChart = ChartFactory.createAreaChart(i18n.get("grid.chart.title"), i18n.get("grid.label.time"),
                i18n.get("grid.label.power"));
        loadChart.setAnimated(false);
        loadChart.setCreateSymbols(false);

        supplySeries = new XYChart.Series<>();
        supplySeries.setName(i18n.get("grid.series.supply"));

        demandSeries = new XYChart.Series<>();
        demandSeries.setName(i18n.get("grid.series.demand"));

        @SuppressWarnings("unchecked")
        XYChart.Series<Number, Number>[] series = new XYChart.Series[] { supplySeries, demandSeries };
        loadChart.getData().addAll(series);

        // System Status
        HBox statusBox = new HBox(20);
        statusBox.setAlignment(javafx.geometry.Pos.CENTER);

        frequencyLabel = new Label(java.text.MessageFormat.format(i18n.get("grid.label.freq"), 50.00));
        frequencyLabel.setStyle("-fx-font-size: 24px; -fx-font-family: monospace; -fx-font-weight: bold;");

        statusLabel = new Label(i18n.get("grid.status.stable"));
        statusLabel.setStyle(
                "-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");

        loadBalanceBar = new ProgressBar(0.5);
        loadBalanceBar.setPrefWidth(200);

        statusBox.getChildren().addAll(new Label(i18n.get("grid.label.freq_short")), frequencyLabel, loadBalanceBar,
                statusLabel);

        box.getChildren().addAll(loadChart, statusBox);
        VBox.setVgrow(loadChart, Priority.ALWAYS);
        return box;
    }

    private VBox createControlRoom() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #f5f5f5;"); // Light control panel theme

        controlRoomTitleLabel = new Label(i18n.get("grid.panel.control"));
        controlRoomTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Coal
        coalLabel = new Label(i18n.get("grid.control.coal"));
        VBox coalBox = createControlGroup(coalLabel, 0, 1000, 500, Color.ORANGE);
        coalOutputSlider = (Slider) coalBox.getChildren().get(1);

        // Wind
        windLabel = new Label(i18n.get("grid.control.wind"));
        VBox windBox = createControlGroup(windLabel, 0, 500, 100, Color.CYAN);
        windCapacitySlider = (Slider) windBox.getChildren().get(1);

        // Solar
        solarLabel = new Label(i18n.get("grid.control.solar"));
        VBox solarBox = createControlGroup(solarLabel, 0, 500, 100, Color.YELLOW);
        solarCapacitySlider = (Slider) solarBox.getChildren().get(1);

        // Demand
        demandLabel = new Label(i18n.get("grid.control.demand"));
        VBox demandBox = createControlGroup(demandLabel, 50, 200, 100, Color.MAGENTA);
        demandScaleSlider = (Slider) demandBox.getChildren().get(1);

        // Battery Status
        VBox batteryBox = new VBox(5);
        batteryTitleLabel = new Label(i18n.get("grid.label.battery_title"));
        batteryTitleLabel.setTextFill(Color.DARKGREEN);
        batteryTitleLabel.setStyle("-fx-font-weight: bold;");

        batteryBar = new ProgressBar(0.5);
        batteryBar.setMaxWidth(Double.MAX_VALUE);
        batteryBar.setStyle("-fx-accent: #27ae60;");

        batteryLabel = new Label(
                MessageFormat.format(i18n.get("grid.label.battery_charge"), batteryCharge, batteryCapacity));
        batteryLabel.setTextFill(Color.DARKGRAY);

        batteryBox.getChildren().addAll(batteryTitleLabel, batteryBar, batteryLabel);

        box.getChildren().addAll(controlRoomTitleLabel, coalBox, windBox, solarBox, new Separator(), demandBox,
                batteryBox);
        return box;
    }

    private VBox createControlGroup(Label lbl, double min, double max, double val, Color color) {
        VBox box = new VBox(5);
        lbl.setTextFill(color);
        lbl.setStyle("-fx-font-weight: bold;");

        Slider sli = new Slider(min, max, val);
        sli.setShowTickLabels(true);
        sli.setShowTickMarks(true);

        Label valLbl = new Label(MessageFormat.format(i18n.get("grid.label.mw_value"), val));
        valLbl.setTextFill(Color.DARKGRAY);
        sli.valueProperty().addListener(
                (o, ov, nv) -> valLbl.setText(MessageFormat.format(i18n.get("grid.label.mw_value"), nv.doubleValue())));

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
        double solarFactor = Math.max(0, Math.sin(time * 0.1)); // Day/Night
                                                                // cycle

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
            batteryCharge += batteryPower * 0.1; // 0.1
                                                 // hour
                                                 // per
                                                 // tick
        } else if (imbalance < 0 && batteryCharge > 0) {
            // Power deficit: discharge battery
            batteryPower = Math.min(-imbalance, batteryMaxPower);
            batteryPower = Math.min(batteryPower, batteryCharge);
            batteryCharge -= batteryPower * 0.1;
            imbalance += batteryPower; // Battery
                                       // helps
                                       // cover
                                       // deficit
        }

        // Update battery UI
        batteryBar.setProgress(batteryCharge / batteryCapacity);
        batteryLabel
                .setText(MessageFormat.format(i18n.get("grid.label.battery_charge"), batteryCharge, batteryCapacity));

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
            triggerBlackout(
                    MessageFormat.format(i18n.get("grid.log.blackout_reason"), String.format("%.2f", gridFrequency)));
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
        frequencyLabel.setText(java.text.MessageFormat.format(i18n.get("grid.label.freq"), gridFrequency));
        frequencyLabel.setTextFill(Math.abs(gridFrequency - 50) < 0.2 ? Color.BLACK : Color.RED);

        if (Math.abs(gridFrequency - 50) < 0.5) {
            statusLabel.setText(i18n.get("grid.status.stable"));
            statusLabel.setStyle(
                    "-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        } else {
            statusLabel.setText(i18n.get("grid.status.unstable"));
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
        gc.fillText(i18n.get("grid.viz.city") + "\n" + (int) currentDemand + "MW", cx - 15, cy);

        // Draw Wind
        gc.setFill(Color.CYAN);
        gc.fillOval(cx - 100, cy - 120, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText(i18n.get("grid.viz.wind") + "\n" + (int) (windF * 100) + "%", cx - 95, cy - 100);

        // Draw Solar
        gc.setFill(solarF > 0.1 ? Color.YELLOW : Color.DARKGRAY);
        gc.fillOval(cx + 60, cy - 120, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText(i18n.get("grid.viz.sun") + "\n" + (int) (solarF * 100) + "%", cx + 65, cy - 100);

        // Draw Coal
        gc.setFill(Color.ORANGE);
        gc.fillRect(cx - 20, cy + 100, 40, 40);
        gc.setFill(Color.BLACK);
        gc.fillText(i18n.get("grid.viz.plant"), cx - 15, cy + 125);
    }

    private void triggerBlackout(String reason) {
        blackout = true;
        statusLabel.setText(i18n.get("grid.status.blackout"));
        statusLabel.setStyle(
                "-fx-font-size: 18px; -fx-background-color: #000000; -fx-text-fill: red; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        log(MessageFormat.format(i18n.get("grid.log.blackout"), reason));
        loop.stop();
    }

    private void resetSimulation() {
        gridFrequency = 50.0;
        time = 0;
        batteryCharge = 100.0;
        supplySeries.getData().clear();
        demandSeries.getData().clear();
        onRun();
    }

    private void log(String msg) {
        // System.out.println(msg);
        // Could hook to a listview if we added one
    }

    @Override
    public void onPause() {
        if (loop != null)
            loop.pause();
        setStatus(i18n.get("status.paused"));
    }

    @Override
    public void onStop() {
        if (loop != null)
            loop.stop();
        setStatus(i18n.get("status.complete"));
    }

    @Override
    public void onReset() {
        resetSimulation();
    }

    @Override
    protected void doNew() {
        onRun(); // Resets simulation logic
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Engineering", "Smart Grid Concepts",
                "Understand the power grid:\n\n" +
                        "- **Supply vs Demand**: Grid frequency deviates when supply != demand.\n" +
                        "- **Frequency**: Must stay near 50Hz. <48Hz risks blackout.\n" +
                        "- **Renewables**: Intermittent power sources (Wind/Solar) require backup or storage.\n" +
                        "- **Battery Storage**: Absorbs excess power and releases it during deficits.",
                null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorial", "Balancing the Grid",
                "1. **Monitor Frequency**: Keep the frequency gauge near 50Hz (Green Zone).\n" +
                        "2. **Adjust Supply**: Use sliders to increase Coal, Wind, or Solar output to match Demand.\n" +
                        "3. **Watch Batteries**: Batteries buffer small fluctuations but have limited capacity.\n" +
                        "4. **Avoid Blackouts**: If frequency drops below 48Hz or spikes above 52Hz, the grid trips.\n"
                        +
                        "5. **Reset**: Use the Reset button to restart after a blackout.",
                null);
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("time", String.valueOf(time));
        props.setProperty("coal", String.valueOf(coalOutputSlider.getValue()));
        props.setProperty("wind", String.valueOf(windCapacitySlider.getValue()));
        props.setProperty("solar", String.valueOf(solarCapacitySlider.getValue()));
        props.setProperty("demand", String.valueOf(demandScaleSlider.getValue()));
        props.setProperty("batteryCharge", String.valueOf(batteryCharge));
        props.setProperty("frequency", String.valueOf(gridFrequency));
        props.setProperty("blackout", String.valueOf(blackout));

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Smart Grid State");
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @Override
    protected void deserializeState(byte[] data) {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new java.io.ByteArrayInputStream(data));

            time = Double.parseDouble(props.getProperty("time", "0"));
            coalOutputSlider.setValue(Double.parseDouble(props.getProperty("coal", "500")));
            windCapacitySlider.setValue(Double.parseDouble(props.getProperty("wind", "100")));
            solarCapacitySlider.setValue(Double.parseDouble(props.getProperty("solar", "100")));
            demandScaleSlider.setValue(Double.parseDouble(props.getProperty("demand", "100")));
            batteryCharge = Double.parseDouble(props.getProperty("batteryCharge", "100"));
            gridFrequency = Double.parseDouble(props.getProperty("frequency", "50"));
            blackout = Boolean.parseBoolean(props.getProperty("blackout", "false"));

            // Refresh UI
            supplySeries.getData().clear();
            demandSeries.getData().clear();
            if (!blackout) {
                onRun();
            } else {
                triggerBlackout("Restored from file");
            }
        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    @Override
    protected void updateLocalizedUI() {
        if (vizTitleLabel != null)
            vizTitleLabel.setText(i18n.get("grid.viz.title"));
        if (controlRoomTitleLabel != null)
            controlRoomTitleLabel.setText(i18n.get("grid.panel.control"));
        if (coalLabel != null)
            coalLabel.setText(i18n.get("grid.control.coal"));
        if (windLabel != null)
            windLabel.setText(i18n.get("grid.control.wind"));
        if (solarLabel != null)
            solarLabel.setText(i18n.get("grid.control.solar"));
        if (demandLabel != null)
            demandLabel.setText(i18n.get("grid.control.demand"));
        if (batteryTitleLabel != null)
            batteryTitleLabel.setText(i18n.get("grid.label.battery_title"));

        if (loadChart != null) {
            loadChart.setTitle(i18n.get("grid.chart.title"));
            loadChart.getXAxis().setLabel(i18n.get("grid.label.time"));
            loadChart.getYAxis().setLabel(i18n.get("grid.label.power"));
            if (supplySeries != null)
                supplySeries.setName(i18n.get("grid.series.supply"));
            if (demandSeries != null)
                demandSeries.setName(i18n.get("grid.series.demand"));
        }

        updateStatus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
