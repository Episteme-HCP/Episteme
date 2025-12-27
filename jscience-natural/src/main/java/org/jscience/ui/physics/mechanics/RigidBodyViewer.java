/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.mechanics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.physics.classical.mechanics.RigidBody;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 2D Rigid Body Physics Engine Demo.
 * Demonstrates integration with
 * org.jscience.physics.classical.mechanics.RigidBody.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class RigidBodyViewer extends Application {

    // Extension to hold visual data not in the core physics body
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

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setId("root"); // CSS hook
        root.getStyleClass().add("dark-viewer-root");

        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(180);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("rigid.title"));
        title.getStyleClass().add("dark-label-accent");

        countLabel = new Label(I18n.getInstance().get("rigid.bodies"));
        countLabel.getStyleClass().add("dark-label-muted");

        Separator sep1 = new Separator();

        Label gravLabel = new Label(I18n.getInstance().get("rigid.gravity"));
        gravLabel.getStyleClass().add("dark-label-muted");
        Slider gravSlider = new Slider(0, 2, 0.5);
        gravSlider.setShowTickLabels(true);
        gravSlider.valueProperty().addListener((o, ov, nv) -> {
            gravityVal = nv.doubleValue();
            gravLabel.setText(String.format(I18n.getInstance().get("rigid.gravity") + ": %.1f", gravityVal));
        });

        Label bounceLabel = new Label(I18n.getInstance().get("rigid.bounciness"));
        bounceLabel.getStyleClass().add("dark-label-muted");
        Slider bounceSlider = new Slider(0.1, 1.0, 0.8);
        bounceSlider.setShowTickLabels(true);
        bounceSlider.valueProperty().addListener((o, ov, nv) -> {
            bouncinessVal = nv.doubleValue();
            bounceLabel.setText(String.format(I18n.getInstance().get("rigid.bounciness") + ": %.1f", bouncinessVal));
        });

        Separator sep2 = new Separator();

        Button addBtn = new Button(I18n.getInstance().get("rigid.add"));
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.getStyleClass().add("accent-button-green");
        addBtn.setOnAction(e -> addBody());

        Button add5Btn = new Button(I18n.getInstance().get("rigid.add5"));
        add5Btn.setMaxWidth(Double.MAX_VALUE);
        add5Btn.setOnAction(e -> {
            for (int i = 0; i < 5; i++)
                addBody();
        });

        Button clearBtn = new Button(I18n.getInstance().get("rigid.clear"));
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.getStyleClass().add("accent-button-red");
        clearBtn.setOnAction(e -> {
            bodies.clear();
            countLabel.setText(I18n.getInstance().get("rigid.bodies"));
        });

        sidebar.getChildren().addAll(title, countLabel, sep1,
                gravLabel, gravSlider, bounceLabel, bounceSlider,
                sep2, addBtn, add5Btn, clearBtn);
        root.setRight(sidebar);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();

        Scene scene = new Scene(root, 950, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);

        // Load CSS if available
        java.net.URL css = getClass().getResource("/org/jscience/ui/theme.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle(I18n.getInstance().get("rigid.window"));
        stage.setScene(scene);
        stage.show();
    }

    private void addBody() {
        Random r = new Random();
        double radius = 10 + r.nextDouble() * 20;
        double m = radius * radius; // Mass proportional to area

        // Initial Position
        double px = 100 + r.nextDouble() * 600;
        double py = 50.0;
        Vector<Real> pos = toVector(px, py, 0.0);

        // Initial Velocity
        double vx = (r.nextDouble() - 0.5) * 10;
        double vy = 0.0;
        Vector<Real> vel = toVector(vx, vy, 0.0);

        // Dummy Inertia (Identity matrix, 3x3 diagonal)
        Real one = Real.ONE;
        Real zero = Real.ZERO;
        List<List<Real>> rows = new ArrayList<>();
        rows.add(Arrays.asList(one, zero, zero));
        rows.add(Arrays.asList(zero, one, zero));
        rows.add(Arrays.asList(zero, zero, one));
        DenseMatrix<Real> inertia = new DenseMatrix<>(rows, Reals.getInstance());

        // Create Physics Body
        RigidBody rb = new RigidBody(pos, Real.of(m), inertia, null);
        rb.setVelocity(vel);

        // Visual Wrapper
        VisualBody vb = new VisualBody(rb, radius, Color.hsb(r.nextDouble() * 360, 0.7, 0.9));
        vb.bounciness = bouncinessVal;

        bodies.add(vb);
        countLabel.setText("Bodies: " + bodies.size());
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    private void update() {
        Real dt = Real.of(0.2); // Slower time step for stability with Real types overhead
        // Pre-computed gravity force (currently unused, applying directly to velocity
        // for stability)
        @SuppressWarnings("unused")
        Vector<Real> gravityForce = toVector(0, gravityVal, 0);

        for (VisualBody vb : bodies) {
            RigidBody b = vb.physicsBody;

            // Apply Gravity (F = m*g) - Here simplified as adding to velocity or applying
            // force
            // Since we integrate, let's update velocity directly for gravity?
            // Or apply constant force.
            // b.applyForce(gravityForce.multiply(Real.of(vb.radius*vb.radius)),
            // b.getPosition());
            // Simplified: Add gravity to velocity manually or use force.
            // Let's use velocity addition for stability in this simple demo
            Vector<Real> grav = toVector(0, gravityVal, 0);
            b.setVelocity(b.getVelocity().add(grav));

            // Integrate
            b.integrate(dt);

            // Fetch state back
            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();
            double vx = b.getVelocity().get(0).doubleValue();
            double vy = b.getVelocity().get(1).doubleValue();

            // Floor collision
            if (y + vb.radius > 600) {
                y = 600 - vb.radius;
                vy *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            // Wall collisions
            if (x - vb.radius < 0) {
                x = vb.radius;
                vx *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            if (x + vb.radius > 800) {
                x = 800 - vb.radius;
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

                if (dist < minDist) {
                    // Simple impulse response
                    double nx = dx / dist;
                    double ny = dy / dist;

                    double vx1 = b1.getVelocity().get(0).doubleValue();
                    double vy1 = b1.getVelocity().get(1).doubleValue();
                    double vx2 = b2.getVelocity().get(0).doubleValue();
                    double vy2 = b2.getVelocity().get(1).doubleValue();

                    double relVelX = vx2 - vx1;
                    double relVelY = vy2 - vy1;
                    double velAlongNormal = relVelX * nx + relVelY * ny;

                    if (velAlongNormal > 0)
                        continue;

                    double e = Math.min(vb1.bounciness, vb2.bounciness);
                    double m1 = vb1.radius * vb1.radius;
                    double m2 = vb2.radius * vb2.radius; // Mass approx

                    double jImpulse = -(1 + e) * velAlongNormal;
                    jImpulse /= (1 / m1 + 1 / m2);

                    double impulseX = jImpulse * nx;
                    double impulseY = jImpulse * ny;

                    vx1 -= 1 / m1 * impulseX;
                    vy1 -= 1 / m1 * impulseY;
                    vx2 += 1 / m2 * impulseX;
                    vy2 += 1 / m2 * impulseY;

                    b1.setVelocity(toVector(vx1, vy1, 0));
                    b2.setVelocity(toVector(vx2, vy2, 0));

                    // Positional correction
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
        gc.clearRect(0, 0, 800, 600); // Transparent cleanup if BG is set by Pane

        // Floor line
        gc.setStroke(Color.web("#444"));
        gc.strokeLine(0, 599, 800, 599);

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

    public static void show(Stage stage) {
        new RigidBodyViewer().start(stage);
    }
}
