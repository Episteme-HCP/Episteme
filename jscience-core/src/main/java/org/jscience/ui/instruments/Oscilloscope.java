/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.instruments;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Dimensionless;

import java.util.LinkedList;

/**
 * An oscilloscope-style waveform display instrument.
 * Displays time-series data as a scrolling waveform.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Oscilloscope extends PhysicalMeasureInstrument<Dimensionless> {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;
    private static final int MAX_SAMPLES = 400;

    private Canvas canvas;
    private VBox view;
    private LinkedList<Double> samples = new LinkedList<>();
    private double minValue = -1.0;
    private double maxValue = 1.0;
    private Color traceColor = Color.LIME;
    private Color gridColor = Color.web("#1a3a1a");
    private boolean running = false;

    public Oscilloscope() {
        super("Oscilloscope");
        canvas = new Canvas(WIDTH, HEIGHT);
        view = new VBox(canvas);
        view.setStyle("-fx-background-color: #001000; -fx-padding: 5;");

        // Initialize with zeros
        for (int i = 0; i < MAX_SAMPLES; i++) {
            samples.add(0.0);
        }

        draw();
    }

    /**
     * Sets the vertical scale range.
     */
    public void setVerticalRange(double min, double max) {
        this.minValue = min;
        this.maxValue = max;
    }

    /**
     * Sets the trace color.
     */
    public void setTraceColor(Color color) {
        this.traceColor = color;
    }

    /**
     * Adds a new sample value to the display.
     */
    public void addSample(double value) {
        samples.addLast(value);
        if (samples.size() > MAX_SAMPLES) {
            samples.removeFirst();
        }
        draw();
    }

    /**
     * Starts auto-updating from a demo signal.
     */
    public void startDemo() {
        if (running)
            return;
        running = true;

        new AnimationTimer() {
            private double t = 0;

            @Override
            public void handle(long now) {
                if (!running) {
                    stop();
                    return;
                }
                // Demo: composite waveform
                double v = Math.sin(t * 0.1) * 0.5 +
                        Math.sin(t * 0.3) * 0.3 +
                        Math.sin(t * 0.7) * 0.1;
                addSample(v);
                t++;
            }
        }.start();
    }

    /**
     * Stops the demo signal.
     */
    public void stopDemo() {
        running = false;
    }

    @Override
    protected void onValueChanged(Quantity<Dimensionless> newValue) {
        if (newValue != null) {
            addSample(newValue.getValue().doubleValue());
        }
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void reset() {
        samples.clear();
        for (int i = 0; i < MAX_SAMPLES; i++) {
            samples.add(0.0);
        }
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Background
        gc.setFill(Color.web("#001000"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Grid
        gc.setStroke(gridColor);
        gc.setLineWidth(0.5);

        // Vertical grid lines
        for (int i = 0; i <= 10; i++) {
            double x = i * WIDTH / 10;
            gc.strokeLine(x, 0, x, HEIGHT);
        }

        // Horizontal grid lines
        for (int i = 0; i <= 8; i++) {
            double y = i * HEIGHT / 8;
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Center line (zero line)
        gc.setStroke(Color.web("#2a4a2a"));
        gc.setLineWidth(1);
        double centerY = HEIGHT * (maxValue / (maxValue - minValue));
        gc.strokeLine(0, centerY, WIDTH, centerY);

        // Waveform trace
        gc.setStroke(traceColor);
        gc.setLineWidth(1.5);
        gc.beginPath();

        double range = maxValue - minValue;
        boolean first = true;
        int i = 0;
        for (Double sample : samples) {
            double x = i * WIDTH / (double) MAX_SAMPLES;
            double normalized = (sample - minValue) / range;
            double y = HEIGHT - normalized * HEIGHT;

            if (first) {
                gc.moveTo(x, y);
                first = false;
            } else {
                gc.lineTo(x, y);
            }
            i++;
        }
        gc.stroke();

        // Glow effect overlay
        gc.setStroke(Color.color(traceColor.getRed(), traceColor.getGreen(), traceColor.getBlue(), 0.3));
        gc.setLineWidth(4);
        gc.beginPath();
        first = true;
        i = 0;
        for (Double sample : samples) {
            double x = i * WIDTH / (double) MAX_SAMPLES;
            double normalized = (sample - minValue) / range;
            double y = HEIGHT - normalized * HEIGHT;

            if (first) {
                gc.moveTo(x, y);
                first = false;
            } else {
                gc.lineTo(x, y);
            }
            i++;
        }
        gc.stroke();

        // Title and info
        gc.setFont(Font.font("Consolas", 10));
        gc.setFill(Color.web("#4a8a4a"));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(name, 5, 15);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(String.format("Range: %.1f to %.1f", minValue, maxValue), WIDTH - 5, 15);
    }
}
