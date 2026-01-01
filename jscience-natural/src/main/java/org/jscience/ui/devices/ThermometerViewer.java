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
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Temperature;

/**
 * A thermometer viewer for temperature display.
 * Renders a classic mercury-style thermometer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermometerViewer extends PhysicalMeasureInstrument<Temperature> {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 300;

    private Canvas canvas;
    private StackPane view;
    private double minTemp = -20; // Celsius
    private double maxTemp = 120; // Celsius

    public ThermometerViewer() {
        super("ThermometerViewer");
        canvas = new Canvas(WIDTH, HEIGHT);
        view = new StackPane(canvas);
        view.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10;");
        draw();
    }

    /**
     * Sets the temperature range to display.
     * 
     * @param min minimum temperature in Celsius
     * @param max maximum temperature in Celsius
     */
    public void setTemperatureRange(double min, double max) {
        this.minTemp = min;
        this.maxTemp = max;
        draw();
    }

    @Override
    protected void onValueChanged(Quantity<Temperature> newValue) {
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
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Background
        gc.setFill(Color.web("#eee"));
        gc.fillRoundRect(0, 0, WIDTH, HEIGHT, 15, 15);

        // Thermometer tube
        double tubeX = WIDTH / 2 - 8;
        double tubeWidth = 16;
        double tubeTop = 30;
        double tubeBottom = HEIGHT - 50;
        double bulbRadius = 20;

        // Tube outline
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        gc.strokeRoundRect(tubeX, tubeTop, tubeWidth, tubeBottom - tubeTop + bulbRadius, 8, 8);

        // Bulb
        gc.setFill(Color.DARKRED);
        gc.fillOval(WIDTH / 2 - bulbRadius, tubeBottom, bulbRadius * 2, bulbRadius * 2);

        // Mercury level
        double temp = 25; // Default
        if (currentValue != null) {
            temp = currentValue.getValue().doubleValue(); // Assume Kelvin, convert later or use raw
            // If in Kelvin, convert to Celsius: temp = temp - 273.15
            temp = temp - 273.15;
        }

        double range = maxTemp - minTemp;
        double ratio = Math.max(0, Math.min(1, (temp - minTemp) / range));
        double mercuryHeight = (tubeBottom - tubeTop) * ratio;
        double mercuryTop = tubeBottom - mercuryHeight;

        // Mercury gradient
        LinearGradient mercuryGrad = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#ff3030")),
                new Stop(0.5, Color.web("#ff5050")),
                new Stop(1, Color.web("#ff3030")));
        gc.setFill(mercuryGrad);
        gc.fillRoundRect(tubeX + 2, mercuryTop, tubeWidth - 4, tubeBottom - mercuryTop + 5, 4, 4);

        // Scale markers
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFont(Font.font("Arial", 10));
        gc.setTextAlign(TextAlignment.RIGHT);

        for (int t = (int) minTemp; t <= maxTemp; t += 20) {
            double y = tubeBottom - ((t - minTemp) / range) * (tubeBottom - tubeTop);
            gc.strokeLine(tubeX - 5, y, tubeX, y);
            gc.setFill(Color.BLACK);
            gc.fillText(t + "Ã‚Â°", tubeX - 8, y + 4);
        }

        // Title
        gc.setFont(Font.font("Arial", 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.DARKGRAY);
        gc.fillText(name, WIDTH / 2, 20);

        // Current value display
        if (currentValue != null) {
            gc.setFont(Font.font("Arial", 11));
            gc.setFill(Color.BLACK);
            gc.fillText(String.format("%.1fÃ‚Â°C", temp), WIDTH / 2, HEIGHT - 10);
        }
    }
}


