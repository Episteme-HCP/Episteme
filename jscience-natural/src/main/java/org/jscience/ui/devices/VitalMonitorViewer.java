/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.devices;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.jscience.medicine.VitalSigns;
import org.jscience.medicine.VitalSignsMonitor;
import org.jscience.devices.sim.SimulatedVitalSignsMonitor;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

/**
 * Professional Vital Signs Monitor display.
 * <p>
 * Displays real-time vital signs including ECG waveform, blood pressure,
 * oxygen saturation (SpO2), respiration rate, and temperature.
 * Styled to match professional medical monitoring equipment.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VitalMonitorViewer extends Application {

    // Colors matching professional medical monitors
    private static final Color HR_COLOR = Color.web("#00FF00"); // Green ECG
    private static final Color HR_VALUE_COLOR = Color.web("#FF69B4"); // Pink HR value
    private static final Color BP_COLOR = Color.web("#FFFF00"); // Yellow BP
    private static final Color SPO2_COLOR = Color.web("#00FFFF"); // Cyan SpO2
    private static final Color RR_COLOR = Color.web("#FFFF00"); // Yellow RR
    private static final Color TEMP_COLOR = Color.WHITE; // White Temp
    private static final Color BACKGROUND = Color.BLACK;

    private VitalSignsMonitor monitor;
    private Canvas ecgCanvas;
    private Canvas plethCanvas;

    // Vital display labels
    private Label hrValueLabel;
    private Label bpValueLabel;
    private Label spo2ValueLabel;
    private Label rrValueLabel;
    private Label tempValueLabel;

    /**
     * Creates a viewer with a default simulated monitor.
     */
    public VitalMonitorViewer() {
        this(new SimulatedVitalSignsMonitor("SimMonitor"));
    }

    /**
     * Creates a viewer for the specified monitor.
     *
     * @param monitor the vital signs monitor to display
     */
    public VitalMonitorViewer(VitalSignsMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // Main content - waveforms on left, values on right
        HBox mainContent = new HBox(10);
        mainContent.setPadding(new Insets(10));
        mainContent.setStyle("-fx-background-color: black;");

        // Left side - Waveforms
        VBox waveformPanel = createWaveformPanel();
        HBox.setHgrow(waveformPanel, Priority.ALWAYS);

        // Right side - Vital values
        VBox valuesPanel = createValuesPanel();
        valuesPanel.setMinWidth(180);
        valuesPanel.setMaxWidth(200);

        mainContent.getChildren().addAll(waveformPanel, valuesPanel);
        root.setCenter(mainContent);

        // Animation timer for updating display
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Update waveforms every frame
                drawWaveforms();

                // Update values less frequently (every 100ms)
                if (now - lastUpdate > 100_000_000) {
                    updateVitalValues();
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        Scene scene = new Scene(root, 900, 500);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("vital.title"));
        stage.setScene(scene);
        stage.show();
    }

    private VBox createWaveformPanel() {
        VBox panel = new VBox(5);
        panel.setStyle("-fx-background-color: black;");

        // ECG waveform with label
        HBox ecgRow = new HBox(5);
        ecgRow.setAlignment(Pos.CENTER_LEFT);

        VBox ecgLabelBox = new VBox(2);
        Label hrLabel = createSmallLabel("HR", HR_VALUE_COLOR);
        Label ecgLeadLabel = createSmallLabel("II", HR_COLOR);
        ecgLabelBox.getChildren().addAll(hrLabel, ecgLeadLabel);
        ecgLabelBox.setMinWidth(40);

        ecgCanvas = new Canvas(600, 120);
        ecgCanvas.setStyle("-fx-background-color: black;");
        HBox.setHgrow(ecgCanvas, Priority.ALWAYS);

        ecgRow.getChildren().addAll(ecgLabelBox, ecgCanvas);

        // NIBP waveform placeholder (shows flatline for now)
        HBox bpRow = new HBox(5);
        bpRow.setAlignment(Pos.CENTER_LEFT);

        Label nibpLabel = createSmallLabel("NIBP", BP_COLOR);
        nibpLabel.setMinWidth(40);

        Canvas bpCanvas = new Canvas(600, 40);
        drawFlatline(bpCanvas.getGraphicsContext2D(), BP_COLOR);
        HBox.setHgrow(bpCanvas, Priority.ALWAYS);

        bpRow.getChildren().addAll(nibpLabel, bpCanvas);

        // SpO2 plethysmograph
        HBox spo2Row = new HBox(5);
        spo2Row.setAlignment(Pos.CENTER_LEFT);

        Label spo2Label = createSmallLabel("SpO2", SPO2_COLOR);
        spo2Label.setMinWidth(40);

        plethCanvas = new Canvas(600, 80);
        HBox.setHgrow(plethCanvas, Priority.ALWAYS);

        spo2Row.getChildren().addAll(spo2Label, plethCanvas);

        // RR label row
        HBox rrRow = new HBox(5);
        rrRow.setAlignment(Pos.CENTER_LEFT);

        Label rrLabel = createSmallLabel("RR", RR_COLOR);
        rrLabel.setMinWidth(40);

        Canvas rrCanvas = new Canvas(600, 30);
        drawFlatline(rrCanvas.getGraphicsContext2D(), RR_COLOR);

        rrRow.getChildren().addAll(rrLabel, rrCanvas);

        panel.getChildren().addAll(ecgRow, bpRow, spo2Row, rrRow);
        VBox.setVgrow(ecgRow, Priority.ALWAYS);
        VBox.setVgrow(spo2Row, Priority.ALWAYS);

        return panel;
    }

    private VBox createValuesPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(5));
        panel.setStyle("-fx-background-color: black;");
        panel.setAlignment(Pos.TOP_RIGHT);

        // HR Value
        hrValueLabel = new Label("89");
        VBox hrBox = createValueBox("Heart Rate", hrValueLabel, "bpm", HR_VALUE_COLOR);

        // BP Value
        bpValueLabel = new Label("108/68");
        VBox bpBox = createValueBox("Blood Pressure", bpValueLabel, "mmHg", BP_COLOR);

        // SpO2 Value
        spo2ValueLabel = new Label("99");
        VBox spo2Box = createValueBox("Oxygen Saturation", spo2ValueLabel, "%", SPO2_COLOR);

        // RR Value
        rrValueLabel = new Label("16");
        VBox rrBox = createValueBox("Respiration", rrValueLabel, "bpm", RR_COLOR);

        // Temperature
        tempValueLabel = new Label("98.6");
        VBox tempBox = createValueBox("Temperature", tempValueLabel, "°F", TEMP_COLOR);

        panel.getChildren().addAll(hrBox, bpBox, spo2Box, rrBox, tempBox);
        return panel;
    }

    private VBox createValueBox(String title, Label valueLabel, String unit, Color color) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER_RIGHT);

        // Value with unit
        HBox valueRow = new HBox(5);
        valueRow.setAlignment(Pos.CENTER_RIGHT);

        valueLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 36));
        valueLabel.setTextFill(color);

        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Arial", 12));
        unitLabel.setTextFill(color.deriveColor(0, 1, 0.7, 1));

        valueRow.getChildren().addAll(valueLabel, unitLabel);

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 11));
        titleLabel.setTextFill(color);

        box.getChildren().addAll(valueRow, titleLabel);
        return box;
    }

    private Label createSmallLabel(String text, Color color) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        label.setTextFill(color);
        return label;
    }

    private void drawWaveforms() {
        // Draw ECG
        double[] ecg = monitor.getECGWaveform();
        drawWaveform(ecgCanvas.getGraphicsContext2D(), ecg, HR_COLOR, 80);

        // Draw Pleth (SpO2)
        double[] pleth = monitor.getPlethWaveform();
        drawWaveform(plethCanvas.getGraphicsContext2D(), pleth, SPO2_COLOR, 60);
    }

    private void drawWaveform(GraphicsContext gc, double[] data, Color color, double amplitude) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        // Clear with black background
        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        // Draw grid lines (subtle)
        gc.setStroke(Color.web("#1a1a1a"));
        gc.setLineWidth(1);
        for (double x = 0; x < w; x += 50) {
            gc.strokeLine(x, 0, x, h);
        }
        for (double y = 0; y < h; y += 20) {
            gc.strokeLine(0, y, w, y);
        }

        // Draw waveform
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.beginPath();

        double midY = h / 2;
        double step = w / data.length;

        for (int i = 0; i < data.length; i++) {
            double x = i * step;
            double y = midY - data[i] * amplitude;

            if (i == 0) {
                gc.moveTo(x, y);
            } else {
                gc.lineTo(x, y);
            }
        }
        gc.stroke();
    }

    private void drawFlatline(GraphicsContext gc, Color color) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(color.deriveColor(0, 1, 0.3, 1));
        gc.setLineWidth(1);
        gc.strokeLine(0, h / 2, w, h / 2);
    }

    private void updateVitalValues() {
        VitalSigns vitals = monitor.getVitalSigns();

        hrValueLabel.setText(String.valueOf(vitals.heartRate()));
        bpValueLabel.setText(vitals.bloodPressureString());
        spo2ValueLabel.setText(String.valueOf(vitals.spO2()));
        rrValueLabel.setText(String.valueOf(vitals.respirationRate()));
        tempValueLabel.setText(vitals.temperatureString());
    }

    public static void show(Stage stage) {
        new VitalMonitorViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
