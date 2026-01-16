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
import javafx.geometry.Insets;
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

import java.util.ArrayList;
import java.util.List;

import org.jscience.ui.i18n.SocialI18n;
import javafx.geometry.Pos;

/**
 * Car Traffic Simulation Demo.
 * Merged implementation of Viewer and Demo.
 */
public class CarTrafficDemo extends AbstractSimulationDemo {

    // --- Simulation State ---
    private static final double TRACK_RADIUS_PIXELS = 250;
    private static final Quantity<Length> TRACK_LENGTH = Quantities.create(2 * Math.PI * TRACK_RADIUS_PIXELS, Units.METER);

    private Quantity<Velocity> desiredVelocity = Quantities.create(30.0, Units.METER_PER_SECOND);
    private Quantity<Time> timeGap = Quantities.create(1.5, Units.SECOND);
    private Quantity<Length> minGap = Quantities.create(2.0, Units.METER);
    private double delta = 4.0;
    private Quantity<Acceleration> maxAccel = Quantities.create(1.0, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> breakingDecel = Quantities.create(2.0, Units.METERS_PER_SECOND_SQUARED);


    private Canvas canvas;
    private boolean running = false;
    private AnimationTimer timer;
    private Label jamStatusLabel;

    @Override
    public String getCategory() { return "Architecture"; }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.cartrafficdemo.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.cartrafficdemo.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        return new org.jscience.ui.viewers.architecture.traffic.CarTrafficViewer();
    }

    @Override
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        
        SocialI18n i18n = SocialI18n.getInstance();
        
        panel.getChildren().add(new Separator());

        Button resetBtn = new Button(i18n.get("traffic.button.reset", "Reset Sim"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.getStyleClass().add("text-light");
        resetBtn.setOnAction(e -> {
            if (viewer instanceof org.jscience.ui.viewers.architecture.traffic.CarTrafficViewer v) {
                // Find density parameter value if possible, or just default
                v.reset(30); 
            }
        });

        Button perturbBtn = new Button(i18n.get("traffic.button.perturb", "Perturb Traffic"));
        perturbBtn.setMaxWidth(Double.MAX_VALUE);
        perturbBtn.getStyleClass().add("text-light");
        perturbBtn.setOnAction(e -> {
             if (viewer instanceof org.jscience.ui.viewers.architecture.traffic.CarTrafficViewer v) {
                 v.perturb();
             }
        });

        panel.getChildren().addAll(resetBtn, perturbBtn);
        return panel;
    }

    // --- Simulatable Delegation ---
    @Override
    public void play() { 
        if (viewer instanceof org.jscience.ui.Simulatable s) s.play(); 
    }
    
    @Override
    public void pause() { 
        if (viewer instanceof org.jscience.ui.Simulatable s) s.pause(); 
    }
    
    @Override
    public void stop() { 
        if (viewer instanceof org.jscience.ui.Simulatable s) s.stop(); 
    }

    @Override
    public boolean isPlaying() { 
        return (viewer instanceof org.jscience.ui.Simulatable s) && s.isPlaying(); 
    }
    
    @Override
    public void setSpeed(double multiplier) {
        if (viewer instanceof org.jscience.ui.Simulatable s) s.setSpeed(multiplier);
    }
    
    @Override
    public void step() {
        if (viewer instanceof org.jscience.ui.Simulatable s) s.step();
    }

    @Override
    public String getLongDescription() {
         return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.cartrafficdemo.longdesc");
    }
}
