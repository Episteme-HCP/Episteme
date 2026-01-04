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
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        Tab tab = new Tab("Spring Oscillator");
        Canvas canvas = new Canvas(600, 500);

        final Quantity<Mass>[] mass = new Quantity[] { Quantities.create(2.0, Units.KILOGRAM) };
        final double[] k = { 5.0 };
        final double[] damping = { 0.1 };
        final double[] pos = { 100.0 }; // m
        final double[] vel = { 0.0 }; // m/s

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(280);

        Slider mSlider = labeledSlider("Mass (kg)", 0.5, 10, 2.0, v -> mass[0] = Quantities.create(v, Units.KILOGRAM));
        Slider kSlider = labeledSlider("Spring k (N/m)", 1, 50, 5.0, v -> k[0] = v);

        Label energyLabel = new Label("Energy: 0 J");
        Canvas phaseCanvas = new Canvas(200, 200);
        List<Point2D> phaseTrail = new ArrayList<>();

        controls.getChildren().addAll(new Label("Spring Controls"), new Separator(), mSlider.getParent(),
                kSlider.getParent(), energyLabel, new Separator(), phaseCanvas);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                // Verlet Step
                double[][] result = VerletIntegrator.step(new double[] { pos[0] }, new double[] { vel[0] },
                        p -> new double[] { (-k[0] * p[0] - damping[0] * vel[0]) / mass[0].getValue().doubleValue() },
                        dt * 60);
                pos[0] = result[0][0];
                vel[0] = result[1][0];

                drawSpring(canvas, pos[0], mass[0].getValue().doubleValue());
                phaseTrail.add(new Point2D(pos[0], vel[0]));
                if (phaseTrail.size() > 200)
                    phaseTrail.remove(0);
                drawPhaseplot(phaseCanvas, phaseTrail, pos[0], vel[0], 150, 10);
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
        Tab tab = new Tab("Simple Pendulum");
        Canvas canvas = new Canvas(600, 500);

        final double[] length = { 200.0 };
        final double[] angle = { Math.PI / 4 };
        final double[] angVel = { 0.0 };
        final double g = 9.81;

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        Slider lSlider = labeledSlider("Length (m)", 1, 5, 2.0, v -> length[0] = v * 100);

        Label pLabel = new Label("Period: ");
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
                double dt = (now - last) / 1e9;
                last = now;

                double L = length[0] / 100.0;
                double[][] res = VerletIntegrator.step(new double[] { angle[0] }, new double[] { angVel[0] },
                        p -> new double[] { -(g / L) * Math.sin(p[0]) }, dt);
                angle[0] = res[0][0];
                angVel[0] = res[1][0] * 0.999;

                drawPendulum(canvas, length[0], angle[0]);
                phaseTrail.add(new Point2D(angle[0], angVel[0]));
                if (phaseTrail.size() > 200)
                    phaseTrail.remove(0);
                drawPhaseplot(phaseCanvas, phaseTrail, angle[0], angVel[0], 1.5, 3.0);
            }
        }.start();

        controls.getChildren().addAll(new Label("Pendulum Controls"), new Separator(), lSlider.getParent(), pLabel,
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
        Tab tab = new Tab("Free Fall");
        Canvas canvas = new Canvas(600, 500);
        final double[] y = { 50.0 }, vy = { 0.0 };
        final boolean[] running = { false };
        final double g = 9.81 * 30;

        VBox controls = new VBox(15);
        Button drop = new Button("Drop");
        drop.setOnAction(e -> {
            y[0] = 50;
            vy[0] = 0;
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
                double dt = (now - last) / 1e9;
                last = now;
                if (running[0] && y[0] < 420) {
                    double[][] res = VerletIntegrator.step(new double[] { y[0] }, new double[] { vy[0] },
                            p -> new double[] { g }, dt);
                    y[0] = res[0][0];
                    vy[0] = res[1][0];
                }
                drawGravity(canvas, y[0]);
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
        Tab tab = new Tab("1D Collision");
        Canvas canvas = new Canvas(700, 300);
        final double[] m1 = { 2.0 }, m2 = { 1.0 }, v1 = { 3.0 }, v2 = { -1.0 }, x1 = { 100 }, x2 = { 400 };
        final boolean[] collided = { false };

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;
                x1[0] += v1[0] * dt * 50;
                x2[0] += v2[0] * dt * 50;
                double r1 = 20 + m1[0] * 5, r2 = 20 + m2[0] * 5;
                if (!collided[0] && Math.abs(x1[0] - x2[0]) < (r1 + r2)) {
                    double nv1 = ((m1[0] - m2[0]) * v1[0] + 2 * m2[0] * v2[0]) / (m1[0] + m2[0]);
                    double nv2 = ((m2[0] - m1[0]) * v2[0] + 2 * m1[0] * v1[0]) / (m1[0] + m2[0]);
                    v1[0] = nv1;
                    v2[0] = nv2;
                    collided[0] = true;
                } else if (Math.abs(x1[0] - x2[0]) > (r1 + r2))
                    collided[0] = false;
                drawCollision(canvas, x1[0], x2[0], m1[0], m2[0]);
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
