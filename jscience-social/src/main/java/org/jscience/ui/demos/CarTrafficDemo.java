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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.ui.i18n.I18n;

/**
 * Car Traffic Simulation Demo.
 * Merged implementation of Viewer and Demo.
 */
public class CarTrafficDemo extends AbstractSimulationDemo {

    private static final double TRACK_RADIUS_PIXELS = 250;
    private static final Quantity<Length> TRACK_LENGTH = Quantities.create(2 * Math.PI * TRACK_RADIUS_PIXELS, Units.METER);

    private org.jscience.architecture.traffic.TrafficSimulator simulator;
    private Canvas canvas;
    private boolean running = true;
    private AnimationTimer timer;
    private Real speed = Real.ONE;

    // private Label jamStatusLabel; // Removed unused field

    @Override
    public String getCategory() { return I18n.getInstance().get("category.architecture", "Architecture"); }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.cartrafficdemo.name", "Car Traffic Simulation");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.cartrafficdemo.desc", "Simulates traffic flow and phantom jams using the Intelligent Driver Model (IDM).");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        // Initialize UI components for direct rendering
        Pane canvasContainer = new StackPane();
        canvas = new Canvas(600, 600);
        canvasContainer.getChildren().add(canvas);
        canvasContainer.getStyleClass().add("viewer-root");

        // Handle resizing
        canvasContainer.widthProperty().addListener((obs, o, w) -> { canvas.setWidth(w.doubleValue()); draw(); });
        canvasContainer.heightProperty().addListener((obs, o, h) -> { canvas.setHeight(h.doubleValue()); draw(); });

        // Initialize simulation
        double trackRadius = TRACK_RADIUS_PIXELS;
        Quantity<Length> trackLength = Quantities.create(2 * Math.PI * trackRadius, Units.METER);
        simulator = new org.jscience.architecture.traffic.TrafficSimulator(trackLength);
        simulator.initCars(30);

        // Start animation loop
        timer = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long now) {
                if (!running) return;
                if (lastTime == 0) { lastTime = now; return; }
                double dt = (now - lastTime) / 1e9 * speed.doubleValue();
                lastTime = now;
                update(Quantities.create(dt, Units.SECOND));
                draw();
            }
        };
        timer.start();

        return canvasContainer;
    }

    @Override
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        
        // I18n i18n = I18n.getInstance(); // No local var needed
        
        panel.getChildren().add(new Separator());

        Button resetBtn = new Button(I18n.getInstance().get("demo.cartrafficdemo.button.reset", "Reset Sim"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.getStyleClass().add("text-light");
        resetBtn.setOnAction(e -> {
            reset(30); 
        });

        Button perturbBtn = new Button(I18n.getInstance().get("demo.cartrafficdemo.button.perturb", "Perturb Traffic"));
        perturbBtn.setMaxWidth(Double.MAX_VALUE);
        perturbBtn.getStyleClass().add("text-light");
        perturbBtn.setOnAction(e -> {
             perturb();
        });

        panel.getChildren().addAll(resetBtn, perturbBtn);
        return panel;
    }

    // --- Simulatable Delegation ---
    // --- Simulation Logic ---
    @Override public void play() { running = true; if (timer != null) timer.start(); }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; reset(30); }
    @Override public boolean isPlaying() { return running; }
    
    @Override
    public void setSpeed(double multiplier) {
        this.speed = org.jscience.mathematics.numbers.real.Real.of(multiplier);
    }
    
    @Override
    public void step() {
        update(Quantities.create(0.05, Units.SECOND));
        draw();
    }

    public void reset(int count) {
        if (simulator != null) simulator.initCars(count);
        draw();
    }

    public void perturb() {
        if (simulator != null) simulator.perturb();
    }

    private void update(Quantity<Time> dt) {
        if (simulator != null) simulator.update(dt);
    }

    private void draw() {
        if (canvas == null || simulator == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;

        gc.setFill(Color.web("#f8f8f8"));
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.web("#e0e0e0"));
        gc.setLineWidth(40);
        gc.strokeOval(cx - TRACK_RADIUS_PIXELS, cy - TRACK_RADIUS_PIXELS, TRACK_RADIUS_PIXELS * 2, TRACK_RADIUS_PIXELS * 2);

        for (org.jscience.architecture.traffic.TrafficSimulator.Car car : simulator.getCars()) {
            double posRatio = car.position.divide(TRACK_LENGTH).getValue().doubleValue();
            double theta = posRatio * 2 * Math.PI;

            double x = cx + TRACK_RADIUS_PIXELS * Math.cos(theta);
            double y = cy + TRACK_RADIUS_PIXELS * Math.sin(theta);

            double vVal = car.velocity.getValue().doubleValue();
            Color c = (vVal < 5.0) ? Color.RED : (vVal > 25.0 ? Color.GREEN : Color.ORANGE);

            gc.save();
            gc.translate(x, y);
            gc.rotate(Math.toDegrees(theta) + 180);
            gc.setFill(c);
            gc.fillRoundRect(-6, -12, 12, 24, 5, 5);
            gc.restore();
        }
    }

    @Override
    public String getLongDescription() {
         return I18n.getInstance().get("demo.cartrafficdemo.longdesc", "Analytic simulation of traffic flow demonstrating phantom jams and congestion dynamics using the Intelligent Driver Model.");
    }
}
