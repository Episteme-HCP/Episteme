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
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.classical.mechanics.PhysicsEngine;
import org.jscience.physics.classical.mechanics.RigidBody;
import org.jscience.ui.NumericParameter;
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

    private final PhysicsEngine engine;
    private final java.util.Map<PhysicsEngine.Body, VisualBodyData> visualData = new java.util.HashMap<>();
    
    private double gravityVal = 0.5;
    private double bouncinessVal = 0.8;
    private Canvas canvas;
    private Label countLabel;
    private boolean running = false;
    private AnimationTimer timer;
    private double speed = 1.0;

    @Override
    public String getName() { return I18n.getInstance().get("viewer.rigidbodyviewer.name", "Rigid Body Physics"); }
    
    @Override
    public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("viewer.rigidbodyviewer.desc", "2D rigid-body physics engine simulation."); }

    @Override
    public String getLongDescription() { return I18n.getInstance().get("viewer.rigidbodyviewer.longdesc", "Simulates multiple colliding rigid bodies in a 2D environment. features mass-based inertia, adjustable gravity, and bounciness parameters. Demonstrates momentum conservation and elastic collisions."); }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("rigid.gravity", I18n.getInstance().get("rigid.gravity", "Gravity"), 0, 2, 0.1, gravityVal, v -> {
            gravityVal = v;
            render();
        }));
        params.add(new NumericParameter("rigid.bounciness", I18n.getInstance().get("rigid.bounciness", "Bounciness"), 0.1, 1.0, 0.05, bouncinessVal, v -> {
            bouncinessVal = v;
            render();
        }));
        return params;
    }

    public RigidBodyViewer() {
        this(new PhysicsEngine());
    }

    public RigidBodyViewer(PhysicsEngine engine) {
        this.engine = engine;
        initUI();
    }

    private void initUI() {
        this.setId("root");

        canvas = new Canvas(800, 600);
        this.setCenter(canvas);
        
        // Resize canvas with parent
        this.widthProperty().addListener((o, old, val) -> { canvas.setWidth(val.doubleValue() - 180); render(); });
        this.heightProperty().addListener((o, old, val) -> { canvas.setHeight(val.doubleValue()); render(); });

        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(180);
        sidebar.getStyleClass().add("viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("rigid.title", "Rigid Bodies"));
        title.getStyleClass().add("header-label");

        countLabel = new Label(I18n.getInstance().get("rigid.bodies", "Bodies: 0"));
        countLabel.getStyleClass().add("description-label");

        Separator sep1 = new Separator();
        Separator sep2 = new Separator();

        Button addBtn = new Button(I18n.getInstance().get("rigid.add", "Add Body"));
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.getStyleClass().add("accent-button-green");
        addBtn.setOnAction(e -> addBody());

        Button add5Btn = new Button(I18n.getInstance().get("rigid.add5", "Add 5 Bodies"));
        add5Btn.setMaxWidth(Double.MAX_VALUE);
        add5Btn.setOnAction(e -> { for (int i = 0; i < 5; i++) addBody(); });

        Button clearBtn = new Button(I18n.getInstance().get("rigid.clear", "Clear"));
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.getStyleClass().add("accent-button-red");
        clearBtn.setOnAction(e -> {
            engine.clear();
            visualData.clear();
            countLabel.setText(I18n.getInstance().get("rigid.bodies", "Bodies: 0"));
        });

        sidebar.getChildren().addAll(title, countLabel, sep1, sep2, addBtn, add5Btn, clearBtn);
        this.setRight(sidebar);

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
    }

    @Override public void play() { running = true; }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; engine.clear(); visualData.clear(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }
    @Override public void step() { update(); render(); }

    private void addBody() {
        Random r = new Random();
        double radius = 10 + r.nextDouble() * 20;
        double m = radius * radius;
        double px = 100 + r.nextDouble() * (canvas.getWidth() - 200);
        double py = 50.0;
        Vector<Real> pos = toVector(px, py, 0.0);
        double vx = (r.nextDouble() - 0.5) * 10;
        double vy = 0.0;
        Vector<Real> vel = toVector(vx, vy, 0.0);

        Real one = Real.ONE, zero = Real.ZERO;
        List<List<Real>> rows = new ArrayList<>();
        rows.add(Arrays.asList(one, zero, zero));
        rows.add(Arrays.asList(zero, one, zero));
        rows.add(Arrays.asList(zero, zero, one));
        DenseMatrix<Real> inertia = new DenseMatrix<>(rows, Reals.getInstance());

        RigidBody rb = new RigidBody(pos, Real.of(m), inertia, null);
        rb.setVelocity(vel);

        PhysicsEngine.Body body = new PhysicsEngine.Body(rb, radius);
        body.bounciness = bouncinessVal;
        
        engine.addBody(body);
        visualData.put(body, new VisualBodyData(Color.hsb(r.nextDouble() * 360, 0.7, 0.9)));
        
        countLabel.setText(java.text.MessageFormat.format(I18n.getInstance().get("rigid.count.fmt", "Bodies: {0}"), engine.getBodies().size()));
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

        gc.setStroke(Color.web("#444"));
        gc.strokeLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1);

        for (PhysicsEngine.Body vb : engine.getBodies()) {
            RigidBody b = vb.physicsBody;
            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();

            VisualBodyData data = visualData.get(vb);
            gc.setFill(data != null ? data.color : Color.GRAY);
            gc.fillOval(x - vb.radius, y - vb.radius, vb.radius * 2, vb.radius * 2);
            gc.setStroke(Color.WHITE);
            gc.strokeOval(x - vb.radius, y - vb.radius, vb.radius * 2, vb.radius * 2);
        }
    }
}

