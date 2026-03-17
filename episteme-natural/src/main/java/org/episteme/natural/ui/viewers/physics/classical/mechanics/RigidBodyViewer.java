/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.ui.viewers.physics.classical.mechanics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBody;

import org.episteme.natural.physics.classical.mechanics.collision.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.collision.MechanicsFactory;
import org.episteme.core.ui.NumericParameter;
import org.episteme.core.ui.BooleanParameter;
import org.episteme.core.ui.Parameter;
import org.episteme.core.ui.AbstractViewer;
import org.episteme.core.ui.Simulatable;
import org.episteme.core.ui.i18n.I18N;

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

    private final PhysicsWorldBridge world = MechanicsFactory.createWorld();
    private final List<RigidBody> bodies = new ArrayList<>();
    private final java.util.Map<RigidBody, VisualBodyData> visualData = new java.util.HashMap<>();
    
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
        parameters.add(new NumericParameter("rigid.gravity", I18N.getInstance().get("rigid.gravity", "Gravity"), 0, 20.0, 0.5, gravityVal, v -> gravityVal = v));
        parameters.add(new NumericParameter("rigid.bounciness", I18N.getInstance().get("rigid.bounciness", "Bounciness"), 0.1, 1.0, 0.05, bouncinessVal, v -> bouncinessVal = v));
        
        parameters.add(new BooleanParameter("rigid.add", I18N.getInstance().get("rigid.add", "Add Body"), false, v -> {
            if (v) addBody();
        }));
        
        parameters.add(new BooleanParameter("rigid.clear", I18N.getInstance().get("rigid.clear", "Clear World"), false, v -> {
            if (v) {
                for (RigidBody b : bodies) world.removeRigidBody(b);
                bodies.clear();
                visualData.clear();
            }
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
        double px = 100 + r.nextDouble() * (canvas.getWidth() - 200);
        double py = 50.0;
        
        Real one = Real.ONE, zero = Real.ZERO;
        List<List<Real>> data = Arrays.asList(
            Arrays.asList(one, zero, zero), Arrays.asList(zero, one, zero), Arrays.asList(zero, zero, one)
        );
        org.episteme.core.mathematics.linearalgebra.Matrix<Real> inertia = org.episteme.core.mathematics.linearalgebra.Matrix.of(data, org.episteme.core.mathematics.sets.Reals.getInstance());

        // Use a sphere collision shape if available
        org.episteme.core.mathematics.geometry.collision.CollisionShape shape = null;
        try {
            shape = new org.episteme.core.mathematics.geometry.collision.SphereShape(radius);
        } catch (Exception e) {}

        RigidBody rb = new RigidBody(toVector(px, py, 0), Real.of(radius * radius), inertia, shape);
        rb.setVelocity(toVector((r.nextDouble() - 0.5) * 10, 0, 0));

        bodies.add(rb);
        world.addRigidBody(rb);
        visualData.put(rb, new VisualBodyData(Color.hsb(r.nextDouble() * 360, 0.7, 0.9)));
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return Vector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    private void update() {
        if (world != null) {
            world.setGravity(0, gravityVal, 0);
            world.stepSimulation(org.episteme.core.measure.Quantities.create(0.016 * speed, org.episteme.core.measure.units.SI.SECOND));
        }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1);

        for (RigidBody b : bodies) {
            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();
            VisualBodyData data = visualData.get(b);
            
            double radius = 15; // Fallback
            if (b.getCollisionShape() instanceof org.episteme.core.mathematics.geometry.collision.SphereShape s) {
                radius = s.getRadius().doubleValue();
            }
            
            gc.setFill(data != null ? data.color : Color.GRAY);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    @Override public void play() { running = true; }
    @Override public void pause() { running = false; }
    @Override public void stop() { 
        running = false; 
        for (RigidBody b : bodies) world.removeRigidBody(b);
        bodies.clear(); 
        visualData.clear(); 
    }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { speed = s; }
    @Override public void step() { update(); render(); }

    @Override public String getCategory() { return I18N.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.rigidbodyviewer.name", "Rigid Body Physics"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.rigidbodyviewer.desc", "Rigid body physics."); }
    @Override public String getLongDescription() { return I18N.getInstance().get("viewer.rigidbodyviewer.longdesc", "Interactive 2D rigid body physics simulation. Watch as geometric bodies interact with gravity, bounce off boundaries, and collide with each other. Adjust gravity and bounciness in real-time to see how the physical environment changes behavior."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}


