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
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.classical.mechanics.RigidBody;
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

    private static class VisualBody {
        RigidBody physicsBody;
        double radius;
        Color color;
        double bounciness = 0.8;

        VisualBody(RigidBody rb, double r, Color c) {
            this.physicsBody = rb;
            this.radius = r;
            this.color = c;
        }
    }

    private List<VisualBody> bodies = new ArrayList<>();
    private double gravityVal = 0.5;
    private double bouncinessVal = 0.8;
    private Canvas canvas;
    private Label countLabel;
    private boolean running = false;
    private AnimationTimer timer;
    private double speed = 1.0;

    @Override
    public String getName() { return I18n.getInstance().get("rigid.window", "Rigid Body Physics"); }
    
    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getDescription() { return I18n.getInstance().get("viewer.rigidbody.desc"); }

    @Override
    public String getLongDescription() { return I18n.getInstance().get("viewer.rigidbody.longdesc"); }

    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return new java.util.ArrayList<>();
    }

    public RigidBodyViewer() {
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

        Label gravLabel = new Label(I18n.getInstance().get("rigid.gravity", "Gravity"));
        gravLabel.getStyleClass().add("description-label");
        Slider gravSlider = new Slider(0, 2, 0.5);
        gravSlider.setShowTickLabels(true);
        gravSlider.valueProperty().addListener((o, ov, nv) -> {
            gravityVal = nv.doubleValue();
            gravLabel.setText(I18n.getInstance().get("rigid.gravity.fmt", "Gravity: %.1f", gravityVal));
        });

        Label bounceLabel = new Label(I18n.getInstance().get("rigid.bounciness", "Bounciness"));
        bounceLabel.getStyleClass().add("description-label");
        Slider bounceSlider = new Slider(0.1, 1.0, 0.8);
        bounceSlider.setShowTickLabels(true);
        bounceSlider.valueProperty().addListener((o, ov, nv) -> {
            bouncinessVal = nv.doubleValue();
            bounceLabel.setText(I18n.getInstance().get("rigid.bounciness.fmt", "Bounciness: %.1f", bouncinessVal));
        });

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
            bodies.clear();
            countLabel.setText(I18n.getInstance().get("rigid.bodies", "Bodies: 0"));
        });

        sidebar.getChildren().addAll(title, countLabel, sep1, gravLabel, gravSlider, bounceLabel, bounceSlider, sep2, addBtn, add5Btn, clearBtn);
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
    @Override public void stop() { running = false; bodies.clear(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }
    @Override public void step() { update(); render(); }

    private void addBody() {
        Random r = new Random();
        double radius = 10 + r.nextDouble() * 20;
        double m = radius * radius;
        double px = 100 + r.nextDouble() * 600;
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

        VisualBody vb = new VisualBody(rb, radius, Color.hsb(r.nextDouble() * 360, 0.7, 0.9));
        vb.bounciness = bouncinessVal;

        bodies.add(vb);
        countLabel.setText(java.text.MessageFormat.format(I18n.getInstance().get("rigid.count.fmt", "Bodies: {0}"), bodies.size()));
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    private void update() {
        Real dt = Real.of(0.2 * speed);
        for (VisualBody vb : bodies) {
            RigidBody b = vb.physicsBody;
            Vector<Real> grav = toVector(0, gravityVal, 0);
            b.setVelocity(b.getVelocity().add(grav));
            b.integrate(dt);

            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();
            double vx = b.getVelocity().get(0).doubleValue();
            double vy = b.getVelocity().get(1).doubleValue();

            if (y + vb.radius > canvas.getHeight()) {
                y = canvas.getHeight() - vb.radius;
                vy *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            if (x - vb.radius < 0) {
                x = vb.radius;
                vx *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            if (x + vb.radius > canvas.getWidth()) {
                x = canvas.getWidth() - vb.radius;
                vx *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
        }

        // Inter-body collisions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                VisualBody vb1 = bodies.get(i);
                VisualBody vb2 = bodies.get(j);
                RigidBody b1 = vb1.physicsBody;
                RigidBody b2 = vb2.physicsBody;

                double x1 = b1.getPosition().get(0).doubleValue();
                double y1 = b1.getPosition().get(1).doubleValue();
                double x2 = b2.getPosition().get(0).doubleValue();
                double y2 = b2.getPosition().get(1).doubleValue();

                double dx = x2 - x1;
                double dy = y2 - y1;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double minDist = vb1.radius + vb2.radius;

                if (dist < minDist && dist > 0) {
                    double nx = dx / dist;
                    double ny = dy / dist;

                    double vx1 = b1.getVelocity().get(0).doubleValue();
                    double vy1 = b1.getVelocity().get(1).doubleValue();
                    double vx2 = b2.getVelocity().get(0).doubleValue();
                    double vy2 = b2.getVelocity().get(1).doubleValue();

                    double relVelX = vx2 - vx1;
                    double relVelY = vy2 - vy1;
                    double velAlongNormal = relVelX * nx + relVelY * ny;

                    if (velAlongNormal > 0) continue;

                    double e = Math.min(vb1.bounciness, vb2.bounciness);
                    double m1 = vb1.radius * vb1.radius;
                    double m2 = vb2.radius * vb2.radius;

                    double jImpulse = -(1 + e) * velAlongNormal / (1 / m1 + 1 / m2);

                    double impulseX = jImpulse * nx;
                    double impulseY = jImpulse * ny;

                    vx1 -= 1 / m1 * impulseX;
                    vy1 -= 1 / m1 * impulseY;
                    vx2 += 1 / m2 * impulseX;
                    vy2 += 1 / m2 * impulseY;

                    b1.setVelocity(toVector(vx1, vy1, 0));
                    b2.setVelocity(toVector(vx2, vy2, 0));

                    double percent = 0.2, slop = 0.01;
                    double correction = Math.max(minDist - dist - slop, 0) / (1 / m1 + 1 / m2) * percent;

                    x1 -= 1 / m1 * nx * correction;
                    y1 -= 1 / m1 * ny * correction;
                    x2 += 1 / m2 * nx * correction;
                    y2 += 1 / m2 * ny * correction;

                    b1.setPosition(toVector(x1, y1, 0));
                    b2.setPosition(toVector(x2, y2, 0));
                }
            }
        }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.web("#444"));
        gc.strokeLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1);

        for (VisualBody vb : bodies) {
            RigidBody b = vb.physicsBody;
            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();

            gc.setFill(vb.color);
            gc.fillOval(x - vb.radius, y - vb.radius, vb.radius * 2, vb.radius * 2);
            gc.setStroke(Color.WHITE);
            gc.strokeOval(x - vb.radius, y - vb.radius, vb.radius * 2, vb.radius * 2);
        }
    }
}
