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

package org.jscience.ui.viewers.physics.classical.mechanics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.classical.mechanics.PhysicsEngine;
import org.jscience.physics.classical.mechanics.RigidBody;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 2D Rigid Body Physics Engine Viewer.
 * Refactored to be 100% parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RigidBodyViewer extends AbstractViewer implements Simulatable {

    private static class VisualBodyData {
        Color color;
        VisualBodyData(Color c) { this.color = c; }
    }

    private final PhysicsEngine engine = new PhysicsEngine();
    private final java.util.Map<PhysicsEngine.Body, VisualBodyData> visualData = new java.util.HashMap<>();
    
    private double gravityVal = 0.5;
    private double bouncinessVal = 0.8;
    private Canvas canvas;
    private boolean running = true;
    private AnimationTimer timer;
    private double speed = 1.0;
    
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public RigidBodyViewer() {
        setupParameters();
        initUI();
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                    render();
                }
            }
        };
        timer.start();
        
        // Add initial bodies
        for(int i=0; i<5; i++) addBody();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("rigid.gravity", I18n.getInstance().get("rigid.gravity", "Gravity"), 0, 2, 0.1, gravityVal, v -> gravityVal = v));
        parameters.add(new NumericParameter("rigid.bounciness", I18n.getInstance().get("rigid.bounciness", "Bounciness"), 0.1, 1.0, 0.05, bouncinessVal, v -> bouncinessVal = v));
        
        parameters.add(new BooleanParameter("rigid.add", I18n.getInstance().get("rigid.add", "Add Body"), false, v -> {
            if (v) addBody();
        }));
        
        parameters.add(new BooleanParameter("rigid.clear", I18n.getInstance().get("rigid.clear", "Clear World"), false, v -> {
            if (v) { engine.clear(); visualData.clear(); }
        }));
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        canvas = new Canvas(800, 600);
        setCenter(canvas);
        
        widthProperty().addListener((o, old, val) -> { canvas.setWidth(val.doubleValue()); render(); });
        heightProperty().addListener((o, old, val) -> { canvas.setHeight(val.doubleValue()); render(); });
    }

    private void addBody() {
        Random r = new Random();
        double radius = 10 + r.nextDouble() * 20;
        double px = 100 + r.nextDouble() * (600);
        double py = 50.0;
        
        Real one = Real.ONE, zero = Real.ZERO;
        DenseMatrix<Real> inertia = new DenseMatrix<>(Arrays.asList(
            Arrays.asList(one, zero, zero), Arrays.asList(zero, one, zero), Arrays.asList(zero, zero, one)
        ), Reals.getInstance());

        RigidBody rb = new RigidBody(toVector(px, py, 0), Real.of(radius * radius), inertia, null);
        rb.setVelocity(toVector((r.nextDouble() - 0.5) * 10, 0, 0));

        PhysicsEngine.Body body = new PhysicsEngine.Body(rb, radius);
        body.bounciness = bouncinessVal;
        engine.addBody(body);
        visualData.put(body, new VisualBodyData(Color.hsb(r.nextDouble() * 360, 0.7, 0.9)));
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    private void update() {
        engine.setGravity(gravityVal);
        engine.setBounds(canvas.getWidth(), canvas.getHeight());
        engine.update(speed);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1);

        for (PhysicsEngine.Body vb : engine.getBodies()) {
            RigidBody b = vb.physicsBody;
            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();
            VisualBodyData data = visualData.get(vb);
            gc.setFill(data != null ? data.color : Color.GRAY);
            gc.fillOval(x - vb.radius, y - vb.radius, vb.radius * 2, vb.radius * 2);
        }
    }

    @Override public void play() { running = true; }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; engine.clear(); visualData.clear(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { speed = s; }
    @Override public void step() { update(); render(); }

    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.rigidbodyviewer.name", "Rigid Body Physics"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.rigidbodyviewer.desc", "Rigid body physics."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.rigidbodyviewer.longdesc", "Interactive 2D rigid body physics simulation. Watch as geometric bodies interact with gravity, bounce off boundaries, and collide with each other. Adjust gravity and bounciness in real-time to see how the physical environment changes behavior."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
