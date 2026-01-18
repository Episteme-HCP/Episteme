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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jscience.device.sensors.PressureGauge;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.AbstractDeviceViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.i18n.I18n;

import java.util.List;
import java.util.ArrayList;

/**
 * An analog gauge viewer for pressure display.
 * Refactored to be parameter-based.
 */
public class PressureGaugeViewer extends AbstractDeviceViewer<PressureGauge> {

    private static final int SIZE = 200;
    private Canvas canvas;
    private double minPressure = 0;
    private double maxPressure = 10;
    private String unitLabel = "bar";
    private Real currentValue;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public PressureGaugeViewer(PressureGauge device) {
        super(device);
        setupParameters();
        canvas = new Canvas(SIZE, SIZE);
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: #333; -fx-background-radius: 100;");
        update();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("pressure.min", I18n.getInstance().get("pressure.min", "Min Pressure"), -1, 100, 1, minPressure, v -> { minPressure = v; draw(); }));
        parameters.add(new NumericParameter("pressure.max", I18n.getInstance().get("pressure.max", "Max Pressure"), 1, 1000, 1, maxPressure, v -> { maxPressure = v; draw(); }));
    }

    @Override
    public void update() {
        try { currentValue = device.readValue(); } catch (Exception e) { currentValue = null; }
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cx = SIZE / 2, cy = SIZE / 2, radius = SIZE / 2 - 10;
        gc.clearRect(0, 0, SIZE, SIZE);
        gc.setFill(new RadialGradient(0, 0, cx, cy, radius, false, CycleMethod.NO_CYCLE, new Stop(0.85, Color.web("#444")), new Stop(1.0, Color.web("#111"))));
        gc.fillOval(0, 0, SIZE, SIZE);
        gc.setFill(new RadialGradient(0, 0, cx, cy - 20, radius * 0.85, false, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f5f5f5")), new Stop(1, Color.web("#d0d0d0"))));
        gc.fillOval(15, 15, SIZE - 30, SIZE - 30);
        gc.setStroke(Color.BLACK); gc.setLineWidth(2); gc.setFont(Font.font("Arial", 11)); gc.setTextAlign(TextAlignment.CENTER);
        double startAngle = 225, sweepAngle = 270;
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            double angle = Math.toRadians(startAngle - (i * sweepAngle / divisions));
            gc.strokeLine(cx + Math.cos(angle) * radius * 0.65, cy - Math.sin(angle) * radius * 0.65, cx + Math.cos(angle) * radius * 0.8, cy - Math.sin(angle) * radius * 0.8);
            gc.fillText(String.format("%.0f", minPressure + (i * (maxPressure - minPressure) / divisions)), cx + Math.cos(angle) * radius * 0.5, cy - Math.sin(angle) * radius * 0.5 + 4);
        }
        double pressure = currentValue != null ? currentValue.doubleValue() : 0;
        double ratio = Math.max(0, Math.min(1, (pressure - minPressure) / (maxPressure - minPressure)));
        double needleAngle = Math.toRadians(startAngle - ratio * sweepAngle);
        gc.setStroke(Color.RED); gc.setLineWidth(3);
        gc.strokeLine(cx, cy, cx + Math.cos(needleAngle) * radius * 0.6, cy - Math.sin(needleAngle) * radius * 0.6);
        gc.setFill(Color.DARKGRAY); gc.fillOval(cx - 8, cy - 8, 16, 16);
        gc.fillText(unitLabel, cx, cy + 40);
        gc.fillText(device.getName(), cx, cy + 60);
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.measure", "Measurement"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.pressuregauge.name", "Pressure Gauge"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.pressuregauge.desc", "Pressure gauge viewer."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
