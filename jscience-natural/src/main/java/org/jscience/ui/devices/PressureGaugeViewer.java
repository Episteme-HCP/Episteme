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

package org.jscience.ui.devices;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Pressure;

/**
 * An analog gauge viewer for pressure display.
 * Renders a classic dial-style pressure gauge.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PressureGaugeViewer extends PhysicalMeasureInstrument<Pressure> {

    private static final int SIZE = 200;

    private Canvas canvas;
    private StackPane view;
    private double minPressure = 0; // Bar
    private double maxPressure = 10; // Bar
    private String unitLabel = "bar";

    public PressureGaugeViewer() {
        super("PressureGaugeViewer");
        canvas = new Canvas(SIZE, SIZE);
        view = new StackPane(canvas);
        view.setStyle("-fx-background-color: #333; -fx-background-radius: 100;");
        draw();
    }

    /**
     * Sets the pressure range to display.
     * 
     * @param min  minimum pressure
     * @param max  maximum pressure
     * @param unit unit label (e.g., "bar", "psi", "kPa")
     */
    public void setPressureRange(double min, double max, String unit) {
        this.minPressure = min;
        this.maxPressure = max;
        this.unitLabel = unit;
        draw();
    }

    @Override
    protected void onValueChanged(Quantity<Pressure> newValue) {
        draw();
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void reset() {
        currentValue = null;
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cx = SIZE / 2;
        double cy = SIZE / 2;
        double radius = SIZE / 2 - 10;

        // Clear
        gc.clearRect(0, 0, SIZE, SIZE);

        // Outer ring gradient
        RadialGradient ringGrad = new RadialGradient(0, 0, cx, cy, radius, false, CycleMethod.NO_CYCLE,
                new Stop(0.85, Color.web("#444")),
                new Stop(0.95, Color.web("#222")),
                new Stop(1.0, Color.web("#111")));
        gc.setFill(ringGrad);
        gc.fillOval(0, 0, SIZE, SIZE);

        // Inner face
        RadialGradient faceGrad = new RadialGradient(0, 0, cx, cy - 20, radius * 0.85, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#f5f5f5")),
                new Stop(1, Color.web("#d0d0d0")));
        gc.setFill(faceGrad);
        gc.fillOval(15, 15, SIZE - 30, SIZE - 30);

        // Scale markers
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 11));
        gc.setTextAlign(TextAlignment.CENTER);

        // Arc from -135 to 135 degrees (270 degree sweep)
        double startAngle = 225; // degrees
        double sweepAngle = 270;
        int divisions = 10;

        for (int i = 0; i <= divisions; i++) {
            double angle = Math.toRadians(startAngle - (i * sweepAngle / divisions));
            double innerR = radius * 0.65;
            double outerR = radius * 0.8;

            double x1 = cx + Math.cos(angle) * innerR;
            double y1 = cy - Math.sin(angle) * innerR;
            double x2 = cx + Math.cos(angle) * outerR;
            double y2 = cy - Math.sin(angle) * outerR;

            gc.strokeLine(x1, y1, x2, y2);

            // Label
            double labelR = radius * 0.5;
            double lx = cx + Math.cos(angle) * labelR;
            double ly = cy - Math.sin(angle) * labelR;
            double value = minPressure + (i * (maxPressure - minPressure) / divisions);
            gc.setFill(Color.BLACK);
            gc.fillText(String.format("%.0f", value), lx, ly + 4);
        }

        // Minor tick marks
        gc.setLineWidth(1);
        for (int i = 0; i <= divisions * 5; i++) {
            if (i % 5 == 0)
                continue;
            double angle = Math.toRadians(startAngle - (i * sweepAngle / (divisions * 5)));
            double innerR = radius * 0.72;
            double outerR = radius * 0.8;

            double x1 = cx + Math.cos(angle) * innerR;
            double y1 = cy - Math.sin(angle) * innerR;
            double x2 = cx + Math.cos(angle) * outerR;
            double y2 = cy - Math.sin(angle) * outerR;

            gc.strokeLine(x1, y1, x2, y2);
        }

        // Needle
        double pressure = 0;
        if (currentValue != null) {
            pressure = currentValue.getValue().doubleValue();
        }

        double ratio = Math.max(0, Math.min(1, (pressure - minPressure) / (maxPressure - minPressure)));
        double needleAngle = Math.toRadians(startAngle - ratio * sweepAngle);
        double needleLen = radius * 0.6;

        double nx = cx + Math.cos(needleAngle) * needleLen;
        double ny = cy - Math.sin(needleAngle) * needleLen;

        // Needle shadow
        gc.setStroke(Color.gray(0.3, 0.5));
        gc.setLineWidth(4);
        gc.strokeLine(cx + 2, cy + 2, nx + 2, ny + 2);

        // Needle
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(cx, cy, nx, ny);

        // Center cap
        gc.setFill(Color.DARKGRAY);
        gc.fillOval(cx - 8, cy - 8, 16, 16);

        // Unit label
        gc.setFont(Font.font("Arial", 10));
        gc.setFill(Color.DARKGRAY);
        gc.fillText(unitLabel, cx, cy + 40);

        // Title
        gc.setFont(Font.font("Arial", 12));
        gc.fillText(name, cx, cy + 60);
    }
}
