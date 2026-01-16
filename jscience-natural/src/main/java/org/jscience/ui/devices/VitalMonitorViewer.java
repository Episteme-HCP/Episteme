/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.devices;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.jscience.medicine.VitalSigns;
import org.jscience.medicine.VitalSignsMonitor;
import org.jscience.device.sim.SimulatedVitalSignsMonitor;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Professional Vital Signs Monitor display.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VitalMonitorViewer extends AbstractViewer implements Simulatable {

    private static final Color HR_COLOR = Color.web("#00FF00");
    private static final Color HR_VALUE_COLOR = Color.web("#FF69B4");
    private static final Color BP_COLOR = Color.web("#FFFF00");
    private static final Color SPO2_COLOR = Color.web("#00FFFF");
    private static final Color RR_COLOR = Color.web("#FFFF00");
    private static final Color TEMP_COLOR = Color.WHITE;
    private static final Color BACKGROUND = Color.BLACK;

    private VitalSignsMonitor monitor;
    private Canvas ecgCanvas;
    private Canvas plethCanvas;

    private Label hrValueLabel;
    private Label bpValueLabel;
    private Label spo2ValueLabel;
    private Label rrValueLabel;
    private Label tempValueLabel;

    private final List<Parameter<?>> viewerParameters = new ArrayList<>();
    private AnimationTimer timer;
    private boolean playing = false;

    @Override
    public String getName() { return I18n.getInstance().get("vital.title", "Vital Signs Monitor"); }

    @Override
    public String getCategory() { return "Medicine"; }

    @Override
    public List<Parameter<?>> getViewerParameters() { return viewerParameters; }

    public VitalMonitorViewer() {
        this(new SimulatedVitalSignsMonitor("SimMonitor"));
    }

    public VitalMonitorViewer(VitalSignsMonitor monitor) {
        this.monitor = monitor;
        setStyle("-fx-background-color: black;");
        setupParameters();
        buildUI();
        setupAnimation();
    }

    private void setupParameters() {
        if (monitor instanceof SimulatedVitalSignsMonitor sim) {
            viewerParameters.add(new NumericParameter(
                    I18n.getInstance().get("vital.param.hr", "Target HR"),
                    "Base heart rate in beats per minute",
                    40, 200, 1, sim.getBaseHeartRate(),
                    val -> sim.setBaseHeartRate(val.intValue())));

            viewerParameters.add(new NumericParameter(
                    I18n.getInstance().get("vital.param.spo2", "Target SpO2"),
                    "Base oxygen saturation level",
                    70, 100, 1, sim.getBaseSpO2(),
                    val -> sim.setBaseSpO2(val.intValue())));
        }
    }

    private void buildUI() {
        HBox mainContent = new HBox(10);
        mainContent.setPadding(new Insets(10));
        mainContent.setStyle("-fx-background-color: black;");

        VBox waveformPanel = createWaveformPanel();
        HBox.setHgrow(waveformPanel, Priority.ALWAYS);

        VBox valuesPanel = createValuesPanel();
        valuesPanel.setMinWidth(200);
        valuesPanel.setMaxWidth(300);

        mainContent.getChildren().addAll(waveformPanel, valuesPanel);
        setCenter(mainContent);
    }

    private void setupAnimation() {
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (!playing) return;
                drawWaveforms();
                if (now - lastUpdate > 100_000_000) {
                    updateVitalValues();
                    lastUpdate = now;
                }
            }
        };
    }

    @Override
    public void play() {
        this.playing = true;
        if (timer != null) timer.start();
    }

    @Override
    public void pause() { this.playing = false; }

    @Override
    public void stop() { this.playing = false; }

    @Override
    public void step() { drawWaveforms(); updateVitalValues(); }

    @Override
    public void setSpeed(double multiplier) { }

    @Override
    public boolean isPlaying() { return playing; }

    private VBox createWaveformPanel() {
        VBox panel = new VBox(5);
        panel.setStyle("-fx-background-color: black;");

        HBox ecgRow = new HBox(5);
        ecgRow.setAlignment(Pos.CENTER_LEFT);

        VBox ecgLabelBox = new VBox(2);
        Label hrLabel = createSmallLabel("HR", HR_VALUE_COLOR);
        Label ecgLeadLabel = createSmallLabel("II", HR_COLOR);
        ecgLabelBox.getChildren().addAll(hrLabel, ecgLeadLabel);
        ecgLabelBox.setMinWidth(40);

        ecgCanvas = new Canvas(600, 120);
        ecgCanvas.widthProperty().bind(panel.widthProperty().subtract(50));
        HBox.setHgrow(ecgCanvas, Priority.ALWAYS);
        ecgRow.getChildren().addAll(ecgLabelBox, ecgCanvas);

        HBox spo2Row = new HBox(5);
        spo2Row.setAlignment(Pos.CENTER_LEFT);
        Label spo2Label = createSmallLabel("SpO2", SPO2_COLOR);
        spo2Label.setMinWidth(40);
        plethCanvas = new Canvas(600, 80);
        plethCanvas.widthProperty().bind(panel.widthProperty().subtract(50));
        HBox.setHgrow(plethCanvas, Priority.ALWAYS);
        spo2Row.getChildren().addAll(spo2Label, plethCanvas);

        panel.getChildren().addAll(ecgRow, spo2Row);
        VBox.setVgrow(ecgRow, Priority.ALWAYS);
        VBox.setVgrow(spo2Row, Priority.ALWAYS);

        return panel;
    }

    private VBox createValuesPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(5));
        panel.setStyle("-fx-background-color: black;");
        panel.setAlignment(Pos.TOP_RIGHT);

        hrValueLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.vitalmonitor.", "--"));
        VBox hrBox = createValueBox("Heart Rate", hrValueLabel, "bpm", HR_VALUE_COLOR);

        bpValueLabel = new Label("--/--");
        VBox bpBox = createValueBox("Blood Pressure", bpValueLabel, "mmHg", BP_COLOR);

        spo2ValueLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.vitalmonitor.", "--"));
        VBox spo2Box = createValueBox("Oxygen Saturation", spo2ValueLabel, "%", SPO2_COLOR);

        rrValueLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.vitalmonitor.", "--"));
        VBox rrBox = createValueBox("Respiration", rrValueLabel, "bpm", RR_COLOR);

        tempValueLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.vitalmonitor..3", "--.-"));
        VBox tempBox = createValueBox("Temperature", tempValueLabel, "°F", TEMP_COLOR);

        panel.getChildren().addAll(hrBox, bpBox, spo2Box, rrBox, tempBox);
        return panel;
    }

    private VBox createValueBox(String title, Label valueLabel, String unit, Color color) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER_RIGHT);

        HBox valueRow = new HBox(5);
        valueRow.setAlignment(Pos.CENTER_RIGHT);

        valueLabel.getStyleClass().add("font-title");
        valueLabel.setTextFill(color);

        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Arial", 12));
        unitLabel.setTextFill(color.deriveColor(0, 1, 0.7, 1));

        valueRow.getChildren().addAll(valueLabel, unitLabel);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 11));
        titleLabel.setTextFill(color);

        box.getChildren().addAll(valueRow, titleLabel);
        return box;
    }

    private Label createSmallLabel(String text, Color color) {
        Label label = new Label(text);
        label.getStyleClass().add("font-bold");
        label.setTextFill(color);
        return label;
    }

    private void drawWaveforms() {
        if (ecgCanvas == null || plethCanvas == null) return;
        double[] ecg = monitor.getECGWaveform();
        drawWaveform(ecgCanvas.getGraphicsContext2D(), ecg, HR_COLOR, 50);
        double[] pleth = monitor.getPlethWaveform();
        drawWaveform(plethCanvas.getGraphicsContext2D(), pleth, SPO2_COLOR, 35);
    }

    private void drawWaveform(GraphicsContext gc, double[] data, Color color, double amplitude) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.web("#1a1a1a"));
        gc.setLineWidth(1);
        for (double x = 0; x < w; x += 50) gc.strokeLine(x, 0, x, h);
        for (double y = 0; y < h; y += 20) gc.strokeLine(0, y, w, y);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.beginPath();

        double midY = h / 2;
        double step = w / data.length;

        for (int i = 0; i < data.length; i++) {
            double x = i * step;
            double y = midY - data[i] * amplitude;
            if (i == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();
    }

    private void updateVitalValues() {
        VitalSigns vitals = monitor.getVitalSigns();
        if (vitals == null) return;

        hrValueLabel.setText(String.valueOf(vitals.heartRate()));
        bpValueLabel.setText(vitals.bloodPressureString());
        spo2ValueLabel.setText(String.valueOf(vitals.spO2()));
        rrValueLabel.setText(String.valueOf(vitals.respirationRate()));
        tempValueLabel.setText(vitals.temperatureString());
    }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.vitalmonitor.desc"); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.vitalmonitor.longdesc"); }
}