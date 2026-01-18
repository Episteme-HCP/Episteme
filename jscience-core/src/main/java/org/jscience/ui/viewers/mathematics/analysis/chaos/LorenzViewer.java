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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Lorenz Attractor Chaos Theory Visualization.
 * Refactored to be 100% parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LorenzViewer extends AbstractViewer implements Simulatable {

    private static final String CFG_PREFIX = "viewer.lorenz.default.";

    @Override public String getCategory() { return I18n.getInstance().get("category.mathematics", "Mathematics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.lorenzviewer.name", "Lorenz Attractor"); }

    private Real x = Real.of(Configuration.getDouble(CFG_PREFIX + "x0", 0.1));
    private Real y = Real.of(Configuration.getDouble(CFG_PREFIX + "y0", 0.0));
    private Real z = Real.of(Configuration.getDouble(CFG_PREFIX + "z0", 0.0));
    
    private Real sigma = Real.of(Configuration.getDouble(CFG_PREFIX + "sigma", 10.0));
    private Real rho = Real.of(Configuration.getDouble(CFG_PREFIX + "rho", 28.0));
    private Real beta = Real.of(Configuration.getDouble(CFG_PREFIX + "beta", 8.0 / 3.0));
    private Real dt = Real.of(Configuration.getDouble(CFG_PREFIX + "dt", 0.01));
    
    private final List<RealPoint3D> points = new ArrayList<>();
    private Canvas canvas;
    private AnimationTimer timer;
    private boolean isRunning = false;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    private static class RealPoint3D {
        Real x, z;
        RealPoint3D(Real x, Real z) { this.x = x; this.z = z; }
    }

    public LorenzViewer() {
        setupParameters();
        initUI();
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    stepLogic();
                    render();
                }
            }
        };
        timer.start();
        isRunning = true;
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("viewer.lorenzviewer.param.sigma", I18n.getInstance().get("viewer.lorenzviewer.param.sigma", "Sigma"), 0, 50, 0.5, sigma.doubleValue(), v -> { sigma = Real.of(v); points.clear(); }));
        parameters.add(new NumericParameter("viewer.lorenzviewer.param.rho", I18n.getInstance().get("viewer.lorenzviewer.param.rho", "Rho"), 0, 100, 1, rho.doubleValue(), v -> { rho = Real.of(v); points.clear(); }));
        parameters.add(new NumericParameter("viewer.lorenzviewer.param.beta", I18n.getInstance().get("viewer.lorenzviewer.param.beta", "Beta"), 0, 10, 0.1, beta.doubleValue(), v -> { beta = Real.of(v); points.clear(); }));
        parameters.add(new NumericParameter("viewer.lorenzviewer.param.dt", I18n.getInstance().get("viewer.lorenzviewer.param.dt", "Time Step"), 0.001, 0.05, 0.001, dt.doubleValue(), v -> dt = Real.of(v)));
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        canvas = new Canvas(800, 600);
        setCenter(canvas);
        
        widthProperty().addListener((o, old, val) -> { canvas.setWidth(val.doubleValue()); render(); });
        heightProperty().addListener((o, old, val) -> { canvas.setHeight(val.doubleValue()); render(); });
    }

    private void stepLogic() {
        Real dx = sigma.multiply(y.subtract(x)).multiply(dt);
        Real dy = x.multiply(rho.subtract(z)).subtract(y).multiply(dt);
        Real dz = x.multiply(y).subtract(beta.multiply(z)).multiply(dt);
        x = x.add(dx); y = y.add(dy); z = z.add(dz);
        points.add(new RealPoint3D(x, z));
        if (points.size() > 2000) points.remove(0);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double cx = canvas.getWidth() / 2, cy = canvas.getHeight() / 2, scale = 10.0;
        for (int i = 1; i < points.size(); i++) {
            RealPoint3D p1 = points.get(i - 1), p2 = points.get(i);
            gc.setStroke(Color.hsb((i / 2000.0) * 360, 0.8, 1.0));
            gc.strokeLine(cx + p1.x.doubleValue() * scale, cy - p1.z.doubleValue() * scale + 250,
                          cx + p2.x.doubleValue() * scale, cy - p2.z.doubleValue() * scale + 250);
        }
    }

    @Override public void play() { isRunning = true; }
    @Override public void pause() { isRunning = false; }
    @Override public void stop() { isRunning = false; points.clear(); render(); }
    @Override public void step() { stepLogic(); render(); }
    @Override public void setSpeed(double multiplier) {}
    @Override public boolean isPlaying() { return isRunning; }

    @Override public String getDescription() { return I18n.getInstance().get("viewer.lorenzviewer.desc", "Lorenz attractor visualization."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.lorenzviewer.longdesc", "System of differential equations for atmospheric convection."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
