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
package org.jscience.ui.engineering.instruments;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * A high-performance oscilloscope-style plotter using JavaFX Canvas.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Oscilloscope extends Pane {

    private final Canvas canvas;
    private final List<Double> dataPoints = new LinkedList<>();
    private final int maxPoints = 500;

    public Oscilloscope() {
        this.canvas = new Canvas();
        getChildren().add(canvas);

        // Bind canvas size to pane size
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // Redraw on resize
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void addData(double value) {
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

        // Clear background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        // Draw Grid
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(0.5);
        for (double x = 0; x < w; x += 50)
            gc.strokeLine(x, 0, x, h);
        for (double y = 0; y < h; y += 50)
            gc.strokeLine(0, y, w, y);

        // Draw Data
        gc.setStroke(Color.LIME);
        gc.setLineWidth(1.5);
        gc.beginPath();

        synchronized (dataPoints) {
            if (dataPoints.isEmpty())
                return;

            double xStep = w / (double) maxPoints;
            double currentX = 0;

            // Start at first point
            // Normalize value (assuming -1 to 1 range for now, or auto-scale?)
            // For MVP, assuming -1.0 to 1.0 input, mapping to 0..h
            // y = h/2 - value * h/2

            boolean first = true;
            for (Double val : dataPoints) {
                double y = h / 2 - (val * h / 2) * 0.8; // 0.8 scale to keep inside
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
