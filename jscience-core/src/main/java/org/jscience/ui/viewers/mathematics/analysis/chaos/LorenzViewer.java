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

package org.jscience.ui.viewers.mathematics.analysis.chaos;

import javafx.animation.AnimationTimer;
import org.jscience.io.Configuration;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.i18n.I18n;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Lorenz Attractor Chaos Theory Visualization.
 * Uses Real for all internal calculations, double only for display output.
 * All default values loaded from jscience.properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LorenzViewer extends AbstractViewer implements Simulatable {

    // Configuration keys prefix
    private static final String CFG_PREFIX = "viewer.lorenz.default.";

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics", "Mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.lorenzviewer.name", "Lorenz Attractor");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.lorenzviewer.desc", "Lorenz system chaos visualization.");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("viewer.lorenzviewer.longdesc", "Interactive 2D visualization of the Lorenz attractor. Adjust the sigma, rho, and beta parameters to explore deterministic chaos and sensitivity to initial conditions.");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("viewer.lorenzviewer.param.sigma",
                I18n.getInstance().get("viewer.lorenzviewer.param.sigma.desc", "Prandtl number controlling fluid viscosity ratio"),
                0, 50, 0.5, sigma.doubleValue(), v -> { sigma = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.rho",
                I18n.getInstance().get("viewer.lorenzviewer.param.rho.desc", "Rayleigh number controlling convection intensity"),
                0, 100, 1, rho.doubleValue(), v -> { rho = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.beta",
                I18n.getInstance().get("viewer.lorenzviewer.param.beta.desc", "Geometric factor related to cell dimensions"),
                0, 10, 0.1, beta.doubleValue(), v -> { beta = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.x0",
                I18n.getInstance().get("viewer.lorenzviewer.param.x0.desc", "Initial X coordinate of the attractor"),
                -20, 20, 0.1, x.doubleValue(), v -> { x = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.y0",
                I18n.getInstance().get("viewer.lorenzviewer.param.y0.desc", "Initial Y coordinate of the attractor"),
                -30, 30, 0.1, y.doubleValue(), v -> { y = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.z0",
                I18n.getInstance().get("viewer.lorenzviewer.param.z0.desc", "Initial Z coordinate of the attractor"),
                0, 50, 0.1, z.doubleValue(), v -> { z = Real.of(v); points.clear(); }));
        params.add(new NumericParameter("viewer.lorenzviewer.param.dt",
                I18n.getInstance().get("viewer.lorenzviewer.param.dt.desc", "Integration time step size"),
                0.001, 0.05, 0.001, dt.doubleValue(), v -> { dt = Real.of(v); }));
        return params;
    }

    private AnimationTimer timer;
    private boolean isRunning = false;
    
    @Override public void play() { 
        if (timer != null) timer.start(); 
        isRunning = true; 
    }
    @Override public void pause() { 
        if (timer != null) timer.stop(); 
        isRunning = false; 
    }
    @Override public void stop() { 
        pause(); 
        points.clear(); 
        render(); 
    }
    @Override public void step() { 
        stepLogic(); 
        render(); 
    }
    @Override public void setSpeed(double multiplier) { }
    @Override public boolean isPlaying() { return isRunning; }

    // State variables using Real - defaults loaded from Configuration
    private Real x = Real.of(Configuration.getDouble(CFG_PREFIX + "x0", 0.1));
    private Real y = Real.of(Configuration.getDouble(CFG_PREFIX + "y0", 0.0));
    private Real z = Real.of(Configuration.getDouble(CFG_PREFIX + "z0", 0.0));
    
    // System parameters using Real - defaults loaded from Configuration
    private Real sigma = Real.of(Configuration.getDouble(CFG_PREFIX + "sigma", 10.0));
    private Real rho = Real.of(Configuration.getDouble(CFG_PREFIX + "rho", 28.0));
    private Real beta = Real.of(Configuration.getDouble(CFG_PREFIX + "beta", 8.0 / 3.0));
    private Real dt = Real.of(Configuration.getDouble(CFG_PREFIX + "dt", 0.01));
    
    private List<RealPoint3D> points = new ArrayList<>();
    private Canvas canvas;

    private static class RealPoint3D {
        Real x, z; // Removed unused y

        RealPoint3D(Real x, Real y, Real z) {
            this.x = x;
            // this.y = y; // Unused
            this.z = z;
        }
    }

    public LorenzViewer() {
        getStyleClass().add("viewer-background");

        canvas = new Canvas(800, 600);
        setCenter(canvas);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("viewer-controls");

        Label title = new Label(I18n.getInstance().get("viewer.lorenzviewer.title", "Lorenz Attractor"));
        title.getStyleClass().add("viewer-header-label");

        sidebar.getChildren().addAll(title, new Separator(),
                createSlider(I18n.getInstance().get("viewer.lorenzviewer.param.sigma", "Sigma"), 0, 50, sigma.doubleValue(), v -> sigma = Real.of(v)),
                createSlider(I18n.getInstance().get("viewer.lorenzviewer.param.rho", "Rho"), 0, 100, rho.doubleValue(), v -> rho = Real.of(v)),
                createSlider(I18n.getInstance().get("viewer.lorenzviewer.param.beta", "Beta"), 0, 10, beta.doubleValue(), v -> beta = Real.of(v)));
        setRight(sidebar);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                stepLogic();
                render();
            }
        };
        timer.start();
        isRunning = true;
    }

    private void stepLogic() {
        // Lorenz system computation using Real arithmetic
        Real dx = sigma.multiply(y.subtract(x)).multiply(dt);
        Real dy = x.multiply(rho.subtract(z)).subtract(y).multiply(dt);
        Real dz = x.multiply(y).subtract(beta.multiply(z)).multiply(dt);

        x = x.add(dx);
        y = y.add(dy);
        z = z.add(dz);

        points.add(new RealPoint3D(x, y, z));
        if (points.size() > 2000)
            points.remove(0);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 800, 600);

        gc.setLineWidth(1.0);
        double centerX = 400, centerY = 300, scale = 10.0;

        for (int i = 1; i < points.size(); i++) {
            RealPoint3D p1 = points.get(i - 1);
            RealPoint3D p2 = points.get(i);

            // Convert Real to double only for display
            double x1 = centerX + p1.x.doubleValue() * scale;
            double y1 = centerY - p1.z.doubleValue() * scale + 250;
            double x2 = centerX + p2.x.doubleValue() * scale;
            double y2 = centerY - p2.z.doubleValue() * scale + 250;

            gc.setStroke(Color.hsb((i / (double) 2000) * 360, 0.8, 1.0));
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private VBox createSlider(String name, double min, double max, double val, java.util.function.DoubleConsumer c) {
        Label l = new Label(name);
        Slider s = new Slider(min, max, val);
        s.valueProperty().addListener((o, ov, nv) -> {
            c.accept(nv.doubleValue());
            points.clear();
        });
        return new VBox(5, l, s);
    }
}

