/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.devices;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.devices.sensors.Oscilloscope;
import org.jscience.devices.sim.SimulatedOscilloscope;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

import java.util.LinkedList;
import java.util.List;

/**
 * Interactive viewer for Oscilloscope devices.
 * <p>
 * Displays waveform data from any {@link Oscilloscope} implementation.
 * Includes controls for frequency and amplitude adjustment.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OscilloscopeViewer extends Application {

    private final Oscilloscope oscilloscope;
    private WaveformDisplay display;
    private double frequency = 1.0;
    private double amplitude = 0.8;

    /**
     * Creates a viewer with a default simulated oscilloscope.
     */
    public OscilloscopeViewer() {
        this(new SimulatedOscilloscope("Default"));
    }

    /**
     * Creates a viewer for the specified oscilloscope.
     *
     * @param oscilloscope the oscilloscope to display
     */
    public OscilloscopeViewer(Oscilloscope oscilloscope) {
        this.oscilloscope = oscilloscope;
        try {
            if (!this.oscilloscope.isConnected()) {
                this.oscilloscope.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getView(), 800, 600);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("oscilloscope.title"));
        stage.setScene(scene);
        stage.show();
    }

    public javafx.scene.Parent getView() {
        display = new WaveformDisplay();
        display.setPrefSize(800, 400);

        // Controls
        Slider freqSlider = new Slider(0.1, 10.0, 1.0);
        freqSlider.setShowTickLabels(true);
        freqSlider.setShowTickMarks(true);
        Label freqLabel = new Label(String.format(I18n.getInstance().get("oscilloscope.freq.fmt"), 1.0));
        freqSlider.valueProperty().addListener((obs, old, newV) -> {
            frequency = newV.doubleValue();
            freqLabel.setText(String.format(I18n.getInstance().get("oscilloscope.freq.fmt"), frequency));
        });

        Slider ampSlider = new Slider(0.1, 1.0, 0.8);
        ampSlider.setShowTickLabels(true);
        ampSlider.setShowTickMarks(true);
        Label ampLabel = new Label(String.format(I18n.getInstance().get("oscilloscope.amp.fmt"), 0.8));
        ampSlider.valueProperty().addListener((obs, old, newV) -> {
            amplitude = newV.doubleValue();
            ampLabel.setText(String.format(I18n.getInstance().get("oscilloscope.amp.fmt"), amplitude));
        });

        VBox controls = new VBox(10,
                new Label(I18n.getInstance().get("oscilloscope.label.freq")), freqSlider, freqLabel,
                new Label(I18n.getInstance().get("oscilloscope.label.amp")), ampSlider, ampLabel);
        controls.setPadding(new Insets(10));
        controls.getStyleClass().add("dark-viewer-sidebar");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");
        root.setCenter(display);
        root.setBottom(controls);

        // Signal Capture Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double[] wave = oscilloscope.captureWaveform(0);
                if (wave != null) {
                    for (double v : wave) {
                        display.addData(v);
                    }
                }
            }
        }.start();

        return root;
    }

    public static void show(Stage stage) {
        new OscilloscopeViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inner class for waveform display - high-performance canvas-based plotter.
     */
    private static class WaveformDisplay extends Pane {
        private final Canvas canvas;
        private final List<Double> dataPoints = new LinkedList<>();
        private final int maxPoints = 500;

        WaveformDisplay() {
            this.canvas = new Canvas();
            getChildren().add(canvas);
            canvas.widthProperty().bind(widthProperty());
            canvas.heightProperty().bind(heightProperty());
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }

        void addData(double value) {
            synchronized (dataPoints) {
                dataPoints.add(value);
                if (dataPoints.size() > maxPoints) {
                    dataPoints.remove(0);
                }
            }
            draw();
        }

        private void draw() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double w = getWidth();
            double h = getHeight();

            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, w, h);

            // Grid
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(0.5);
            for (double x = 0; x < w; x += 50)
                gc.strokeLine(x, 0, x, h);
            for (double y = 0; y < h; y += 50)
                gc.strokeLine(0, y, w, y);

            // Waveform
            gc.setStroke(Color.LIME);
            gc.setLineWidth(1.5);
            gc.beginPath();

            synchronized (dataPoints) {
                if (dataPoints.isEmpty())
                    return;
                double xStep = w / (double) maxPoints;
                double currentX = 0;
                boolean first = true;

                for (Double val : dataPoints) {
                    double y = h / 2 - (val * h / 2) * 0.8;
                    if (first) {
                        gc.moveTo(currentX, y);
                        first = false;
                    } else {
                        gc.lineTo(currentX, y);
                    }
                    currentX += xStep;
                }
            }
            gc.stroke();
        }
    }
}
