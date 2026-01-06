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

package org.jscience.ui.viewers.physics.classical.mechanics;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;
import org.jscience.mathematics.analysis.ode.VerletIntegrator;

/**
 * Newtonian Mechanics Lab - Refactored to use JScience Quantity and
 * VerletIntegrator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NewtonianMechanicsLabViewer {

    public static void show(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                createSpringTab(),
                createPendulumTab(),
                createGravityTab(),
                createCollisionTab());

        BorderPane root = new BorderPane(tabPane);
        Scene scene = new Scene(root, 950, 750);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("mechanics.lab.title"));
        stage.setScene(scene);
        stage.show();
    }

    // ==== TAB 1: Spring Oscillator ====
    private static Tab createSpringTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.spring.tab"));
        Canvas canvas = new Canvas(600, 500);

        @SuppressWarnings("unchecked")
        final Quantity<Mass>[] mass = new Quantity[] { Quantities.create(2.0, Units.KILOGRAM) };
        final org.jscience.mathematics.numbers.real.Real[] k = { org.jscience.mathematics.numbers.real.Real.of(5.0) };
        final org.jscience.mathematics.numbers.real.Real[] damping = {
                org.jscience.mathematics.numbers.real.Real.of(0.1) };

        // Physics state in Real
        final org.jscience.mathematics.numbers.real.Real[] pos = {
                org.jscience.mathematics.numbers.real.Real.of(100.0) };
        final org.jscience.mathematics.numbers.real.Real[] vel = { org.jscience.mathematics.numbers.real.Real.ZERO };

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(280);

        Slider mSlider = labeledSlider("Mass (kg)", 0.5, 10, 2.0, v -> mass[0] = Quantities.create(v, Units.KILOGRAM));
        Slider kSlider = labeledSlider("Spring k (N/m)", 1, 50, 5.0,
                v -> k[0] = org.jscience.mathematics.numbers.real.Real.of(v));

        Label energyLabel = new Label(I18n.getInstance().get("mechanics.energy.label"));
        Canvas phaseCanvas = new Canvas(200, 200);
        List<Point2D> phaseTrail = new ArrayList<>();

        controls.getChildren().addAll(new Label(I18n.getInstance().get("mechanics.spring.controls")), new Separator(),
                mSlider.getParent(),
                kSlider.getParent(), energyLabel, new Separator(), phaseCanvas);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dtVal = (now - last) / 1e9;
                last = now;

                org.jscience.mathematics.numbers.real.Real dt = org.jscience.mathematics.numbers.real.Real
                        .of(dtVal * 60);
                org.jscience.mathematics.numbers.real.Real massReal = org.jscience.mathematics.numbers.real.Real
                        .of(mass[0].getValue().doubleValue());

                org.jscience.mathematics.numbers.real.Real[][] result = VerletIntegrator.step(
                        pos,
                        vel,
                        p -> new org.jscience.mathematics.numbers.real.Real[] {
                                k[0].negate().multiply(p[0]).subtract(damping[0].multiply(vel[0])).divide(massReal)
                        },
                        dt);

                pos[0] = result[0][0];
                vel[0] = result[1][0];

                // Convert to double only for rendering
                double drawPos = pos[0].doubleValue();
                double drawVel = vel[0].doubleValue();

                drawSpring(canvas, drawPos, mass[0].getValue().doubleValue());
                phaseTrail.add(new Point2D(drawPos, drawVel));
                if (phaseTrail.size() > 200)
                    phaseTrail.remove(0);
                drawPhaseplot(phaseCanvas, phaseTrail, drawPos, drawVel, 150, 10);
            }
        }.start();

        tab.setContent(new HBox(20, canvas, controls));
        return tab;
    }

    private static void drawSpring(Canvas canvas, double pos, double mass) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double cx = canvas.getWidth() / 2, ey = 250, my = ey + pos;
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(4);
        gc.strokeLine(cx - 60, 50, cx + 60, 50);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        double py = 50;
        for (int i = 0; i < 12; i++) {
            double ny = py + (my - 50) / 12;
            gc.strokeLine(cx + (i % 2 == 0 ? 20 : -20), py, cx + (i % 2 == 0 ? -20 : 20), ny);
            py = ny;
        }
        gc.setFill(Color.STEELBLUE);
        gc.fillRoundRect(cx - 30, my, 60, 50, 8, 8);
        gc.setFill(Color.WHITE);
        gc.fillText(String.format("%.1f kg", mass), cx - 20, my + 30);
    }

    // ==== TAB 2: Simple Pendulum ====
    private static Tab createPendulumTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.pendulum.tab"));
        Canvas canvas = new Canvas(600, 500);

        final org.jscience.mathematics.numbers.real.Real[] length = {
                org.jscience.mathematics.numbers.real.Real.of(200.0) };
        final org.jscience.mathematics.numbers.real.Real[] angle = {
                org.jscience.mathematics.numbers.real.Real.of(Math.PI / 4) };
        final org.jscience.mathematics.numbers.real.Real[] angVel = { org.jscience.mathematics.numbers.real.Real.ZERO };
        final org.jscience.mathematics.numbers.real.Real g = org.jscience.mathematics.numbers.real.Real.of(9.81);

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        Slider lSlider = labeledSlider("Length (m)", 1, 5, 2.0,
                v -> length[0] = org.jscience.mathematics.numbers.real.Real.of(v * 100));

        Label pLabel = new Label(I18n.getInstance().get("mechanics.period.label"));
        Canvas phaseCanvas = new Canvas(200, 200);
        List<Point2D> phaseTrail = new ArrayList<>();

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dtVal = (now - last) / 1e9;
                last = now;
                org.jscience.mathematics.numbers.real.Real dt = org.jscience.mathematics.numbers.real.Real.of(dtVal);

                org.jscience.mathematics.numbers.real.Real L = length[0]
                        .divide(org.jscience.mathematics.numbers.real.Real.of(100.0));

                org.jscience.mathematics.numbers.real.Real[][] res = VerletIntegrator.step(
                        angle,
                        angVel,
                        p -> {
                            // -(g / L) * sin(p[0])
                            // Note: Real doesn't have sin() convenience yet likely, fallback to double for
                            // sin?
                            // Or assuming MathContext used internally.
                            // Since Real.of(double) is used, we can do:
                            double val = p[0].doubleValue();
                            double s = Math.sin(val);
                            return new org.jscience.mathematics.numbers.real.Real[] {
                                    g.negate().divide(L).multiply(org.jscience.mathematics.numbers.real.Real.of(s))
                            };
                        },
                        dt);

                angle[0] = res[0][0];
                angVel[0] = res[1][0].multiply(org.jscience.mathematics.numbers.real.Real.of(0.999));

                double drawLen = length[0].doubleValue();
                double drawAng = angle[0].doubleValue();
                double drawVel = angVel[0].doubleValue();

                drawPendulum(canvas, drawLen, drawAng);
                phaseTrail.add(new Point2D(drawAng, drawVel));
                if (phaseTrail.size() > 200)
                    phaseTrail.remove(0);
                drawPhaseplot(phaseCanvas, phaseTrail, drawAng, drawVel, 1.5, 3.0);
            }
        }.start();

        controls.getChildren().addAll(new Label(I18n.getInstance().get("mechanics.pendulum.controls")), new Separator(),
                lSlider.getParent(), pLabel,
                phaseCanvas);
        tab.setContent(new HBox(20, canvas, controls));
        return tab;
    }

    private static void drawPendulum(Canvas canvas, double len, double ang) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double px = canvas.getWidth() / 2, py = 80, bx = px + len * Math.sin(ang), by = py + len * Math.cos(ang);
        gc.setFill(Color.DARKGRAY);
        gc.fillOval(px - 8, py - 8, 16, 16);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        gc.strokeLine(px, py, bx, by);
        gc.setFill(Color.CRIMSON);
        gc.fillOval(bx - 20, by - 20, 40, 40);
    }

    // ==== TAB 3: Free Fall ====
    private static Tab createGravityTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.gravity.tab"));
        Canvas canvas = new Canvas(600, 500);

        final org.jscience.mathematics.numbers.real.Real[] y = { org.jscience.mathematics.numbers.real.Real.of(50.0) };
        final org.jscience.mathematics.numbers.real.Real[] vy = { org.jscience.mathematics.numbers.real.Real.ZERO };

        final boolean[] running = { false };
        final org.jscience.mathematics.numbers.real.Real g = org.jscience.mathematics.numbers.real.Real.of(9.81 * 30);

        VBox controls = new VBox(15);
        Button drop = new Button(I18n.getInstance().get("mechanics.drop"));
        drop.setOnAction(e -> {
            y[0] = org.jscience.mathematics.numbers.real.Real.of(50.0);
            vy[0] = org.jscience.mathematics.numbers.real.Real.ZERO;
            running[0] = true;
        });
        controls.getChildren().addAll(drop);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dtVal = (now - last) / 1e9;
                last = now;
                org.jscience.mathematics.numbers.real.Real dt = org.jscience.mathematics.numbers.real.Real.of(dtVal);

                if (running[0] && y[0].doubleValue() < 420) {
                    org.jscience.mathematics.numbers.real.Real[][] res = VerletIntegrator.step(
                            y,
                            vy,
                            p -> new org.jscience.mathematics.numbers.real.Real[] { g },
                            dt);
                    y[0] = res[0][0];
                    vy[0] = res[1][0];
                }
                drawGravity(canvas, y[0].doubleValue());
            }
        }.start();

        tab.setContent(new HBox(20, canvas, controls));
        return tab;
    }

    private static void drawGravity(Canvas canvas, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 600, 500);
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, 450, 600, 50);
        gc.setFill(Color.ORANGE);
        gc.fillOval(300 - 25, y, 50, 50);
    }

    // ==== TAB 4: 1D Collision ====
    private static Tab createCollisionTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.collision.tab"));
        Canvas canvas = new Canvas(700, 300);

        final org.jscience.mathematics.numbers.real.Real[] m1 = { org.jscience.mathematics.numbers.real.Real.of(2.0) };
        final org.jscience.mathematics.numbers.real.Real[] m2 = { org.jscience.mathematics.numbers.real.Real.of(1.0) };
        final org.jscience.mathematics.numbers.real.Real[] v1 = { org.jscience.mathematics.numbers.real.Real.of(3.0) };
        final org.jscience.mathematics.numbers.real.Real[] v2 = { org.jscience.mathematics.numbers.real.Real.of(-1.0) };
        final org.jscience.mathematics.numbers.real.Real[] x1 = {
                org.jscience.mathematics.numbers.real.Real.of(100.0) };
        final org.jscience.mathematics.numbers.real.Real[] x2 = {
                org.jscience.mathematics.numbers.real.Real.of(400.0) };

        final boolean[] collided = { false };

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dtVal = (now - last) / 1e9;
                last = now;
                org.jscience.mathematics.numbers.real.Real dt = org.jscience.mathematics.numbers.real.Real
                        .of(dtVal * 50);

                x1[0] = x1[0].add(v1[0].multiply(dt));
                x2[0] = x2[0].add(v2[0].multiply(dt));

                double r1Val = 20 + m1[0].doubleValue() * 5;
                double r2Val = 20 + m2[0].doubleValue() * 5;

                double x1Val = x1[0].doubleValue();
                double x2Val = x2[0].doubleValue();

                if (!collided[0] && Math.abs(x1Val - x2Val) < (r1Val + r2Val)) {
                    // nv1 = ((m1 - m2)v1 + 2m2v2) / (m1 + m2)
                    org.jscience.mathematics.numbers.real.Real num1 = m1[0].subtract(m2[0]).multiply(v1[0])
                            .add(m2[0].multiply(v2[0]).multiply(org.jscience.mathematics.numbers.real.Real.of(2.0)));
                    org.jscience.mathematics.numbers.real.Real den = m1[0].add(m2[0]);
                    org.jscience.mathematics.numbers.real.Real nv1 = num1.divide(den);

                    // nv2 = ((m2 - m1)v2 + 2m1v1) / (m1 + m2)
                    org.jscience.mathematics.numbers.real.Real num2 = m2[0].subtract(m1[0]).multiply(v2[0])
                            .add(m1[0].multiply(v1[0]).multiply(org.jscience.mathematics.numbers.real.Real.of(2.0)));
                    org.jscience.mathematics.numbers.real.Real nv2 = num2.divide(den);

                    v1[0] = nv1;
                    v2[0] = nv2;
                    collided[0] = true;
                } else if (Math.abs(x1Val - x2Val) > (r1Val + r2Val))
                    collided[0] = false;

                drawCollision(canvas, x1Val, x2Val, m1[0].doubleValue(), m2[0].doubleValue());
            }
        }.start();

        tab.setContent(canvas);
        return tab;
    }

    private static void drawCollision(Canvas cv, double x1, double x2, double m1, double m2) {
        GraphicsContext gc = cv.getGraphicsContext2D();
        gc.clearRect(0, 0, 700, 300);
        double r1 = 20 + m1 * 5, r2 = 20 + m2 * 5;
        gc.setFill(Color.STEELBLUE);
        gc.fillOval(x1 - r1, 150 - r1, r1 * 2, r1 * 2);
        gc.setFill(Color.CORAL);
        gc.fillOval(x2 - r2, 150 - r2, r2 * 2, r2 * 2);
    }

    private static Slider labeledSlider(String name, double min, double max, double val,
            java.util.function.DoubleConsumer action) {
        VBox box = new VBox(2);
        Label l = new Label(String.format("%s: %.1f", name, val));
        Slider s = new Slider(min, max, val);
        s.valueProperty().addListener((o, ov, nv) -> {
            action.accept(nv.doubleValue());
            l.setText(String.format("%s: %.1f", name, nv.doubleValue()));
        });
        box.getChildren().addAll(l, s);
        s.setUserData(box);
        return s;
    }

    private static void drawPhaseplot(Canvas cv, List<Point2D> trail, double x, double v, double sx, double sv) {
        GraphicsContext gc = cv.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 200, 200);
        gc.setStroke(Color.LIME);
        gc.beginPath();
        boolean first = true;
        for (Point2D p : trail) {
            double tx = 100 + (p.getX() / sx) * 80, ty = 100 - (p.getY() / sv) * 80;
            if (first) {
                gc.moveTo(tx, ty);
                first = false;
            } else
                gc.lineTo(tx, ty);
        }
        gc.stroke();
        gc.setFill(Color.RED);
        gc.fillOval(100 + (x / sx) * 80 - 3, 100 - (v / sv) * 80 - 3, 6, 6);
    }
}
