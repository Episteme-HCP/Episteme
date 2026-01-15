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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jscience.device.sensors.Oscilloscope;
import org.jscience.ui.AbstractDeviceViewer;

/**
 * Viewer for Oscilloscope.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OscilloscopeViewer extends AbstractDeviceViewer<Oscilloscope> {

    private final Canvas canvas;
    private final AnimationTimer timer;

    public OscilloscopeViewer(Oscilloscope device) {
        super(device);

        this.canvas = new Canvas(300, 200);
        StackPane frame = new StackPane(canvas);
        frame.setStyle("-fx-background-color: black; -fx-border-color: #555; -fx-border-width: 2;");

        this.getChildren().add(frame);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    public void play() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK); // CRT phosphor persistence simulation could be here
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.LIME);
        gc.setLineWidth(1.5);

        try {
            double[] wave = device.captureWaveform(0);
            if (wave != null && wave.length > 0) {
                gc.beginPath();
                double w = canvas.getWidth();
                double h = canvas.getHeight();
                double step = w / wave.length;

                for (int i = 0; i < wave.length; i++) {
                    double x = i * step;
                    // Map -1..1 to h..0
                    double y = h / 2 - (wave[i] * h / 2.5);
                    if (i == 0)
                        gc.moveTo(x, y);
                    else
                        gc.lineTo(x, y);
                }
                gc.stroke();
            }
        } catch (Exception e) {
            // device might not be ready
        }
    }

    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.oscilloscope.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.oscilloscope.desc");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.oscilloscope.longdesc");
    }
}
