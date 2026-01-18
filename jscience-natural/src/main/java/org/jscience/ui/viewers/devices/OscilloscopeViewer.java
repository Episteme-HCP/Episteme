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

package org.jscience.ui.viewers.devices;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jscience.device.sensors.Oscilloscope;
import org.jscience.ui.AbstractDeviceViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.i18n.I18n;

import java.util.List;
import java.util.ArrayList;

/**
 * Viewer for Oscilloscope.
 * Refactored to be parameter-based.
 */
public class OscilloscopeViewer extends AbstractDeviceViewer<Oscilloscope> {

    private final Canvas canvas;
    private final AnimationTimer timer;
    private double gain = 1.0;
    private double timeBase = 1.0;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public OscilloscopeViewer(Oscilloscope device) {
        super(device);
        setupParameters();
        this.canvas = new Canvas(600, 400);
        StackPane frame = new StackPane(canvas);
        frame.setStyle("-fx-background-color: black; -fx-border-color: #555; -fx-border-width: 2;");
        this.getChildren().add(frame);
        this.timer = new AnimationTimer() {
            @Override public void handle(long now) { update(); }
        };
        this.timer.start();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("osc.gain", I18n.getInstance().get("osc.gain", "Gain"), 0.1, 10, 0.1, gain, v -> gain = v));
        parameters.add(new NumericParameter("osc.timebase", I18n.getInstance().get("osc.timebase", "Time Base"), 0.1, 5, 0.1, timeBase, v -> timeBase = v));
    }

    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.web("#222"));
        for (int x=0; x<canvas.getWidth(); x+=40) gc.strokeLine(x, 0, x, canvas.getHeight());
        for (int y=0; y<canvas.getHeight(); y+=40) gc.strokeLine(0, y, canvas.getWidth(), y);
        gc.setStroke(Color.LIME); gc.setLineWidth(1.5);
        try {
            double[] wave = device.captureWaveform(0);
            if (wave != null && wave.length > 0) {
                gc.beginPath();
                double w = canvas.getWidth(), h = canvas.getHeight(), step = (w / wave.length) * timeBase;
                for (int i = 0; i < wave.length; i++) {
                    double x = i * step;
                    double y = h / 2 - (wave[i] * gain * h / 2.5);
                    if (i == 0) gc.moveTo(x, y); else gc.lineTo(x, y);
                    if (x > w) break;
                }
                gc.stroke();
            }
        } catch (Exception e) {}
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.measure", "Measurement"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.oscilloscope.name", "Oscilloscope"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.oscilloscope.desc", "Oscilloscope viewer."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
