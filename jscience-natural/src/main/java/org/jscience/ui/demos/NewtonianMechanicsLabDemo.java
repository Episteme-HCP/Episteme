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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.mathematics.analysis.ode.VerletIntegrator;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Newtonian Mechanics Lab Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NewtonianMechanicsLabDemo extends AbstractSimulationDemo {

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("mechanics.lab.title", "Newtonian Lab");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.mechanics.lab", "Experiments with Springs, Pendulums, and Gravity.");
    }

    @Override
    protected Node createViewerNode() {
        return new InternalNewtonianViewer();
    }

    private static class InternalNewtonianViewer extends AbstractViewer implements Simulatable {

        private boolean running = true;
        private List<AnimationTimer> timers = new ArrayList<>();

        public InternalNewtonianViewer() {
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            tabPane.getTabs().addAll(
                    createSpringTab(),
                    createPendulumTab(),
                    createGravityTab(),
                    createCollisionTab());
            
            setCenter(tabPane);
        }

        private Tab createSpringTab() {
            Tab tab = new Tab(I18n.getInstance().get("mechanics.spring.tab", "Spring"));
            Canvas canvas = new Canvas(600, 500);

            @SuppressWarnings("unchecked")
            final Quantity<Mass>[] mass = new Quantity[] { Quantities.create(2.0, Units.KILOGRAM) };
            final Real[] k = { Real.of(5.0) };
            final Real[] damping = { Real.of(0.1) };

            final Real[] pos = { Real.of(100.0) };
            final Real[] vel = { Real.ZERO };

            VBox controls = new VBox(15);
            controls.setPadding(new Insets(15));
            controls.setPrefWidth(280);

            Slider mSlider = labeledSlider("Mass (kg)", 0.5, 10, 2.0, v -> mass[0] = Quantities.create(v, Units.KILOGRAM));
            Slider kSlider = labeledSlider("Spring k (N/m)", 1, 50, 5.0, v -> k[0] = Real.of(v));

            Label energyLabel = new Label(I18n.getInstance().get("mechanics.energy.label", "Energy Plot"));
            Canvas phaseCanvas = new Canvas(200, 200);
            List<Point2D> phaseTrail = new ArrayList<>();

            controls.getChildren().addAll(new Label(I18n.getInstance().get("mechanics.spring.controls", "Controls")), new Separator(),
                    mSlider.getParent(),
                    kSlider.getParent(), energyLabel, new Separator(), phaseCanvas);

            AnimationTimer timer = new AnimationTimer() {
                long last = 0;
                @Override
                public void handle(long now) {
                    if (!running) { last = 0; return; }
                    if (last == 0) { last = now; return; }
                    double dtVal = (now - last) / 1e9;
                    last = now;
                    Real dt = Real.of(dtVal * 60);
                    Real massReal = Real.of(mass[0].getValue().doubleValue());

                    Real[][] result = VerletIntegrator.step(
                            pos, vel,
                            p -> new Real[] {
                                k[0].negate().multiply(p[0]).subtract(damping[0].multiply(vel[0])).divide(massReal)
                            }, dt);

                    pos[0] = result[0][0];
                    vel[0] = result[1][0];

                    double drawPos = pos[0].doubleValue();
                    double drawVel = vel[0].doubleValue();

                    drawSpring(canvas, drawPos, mass[0].getValue().doubleValue());
                    phaseTrail.add(new Point2D(drawPos, drawVel));
                    if (phaseTrail.size() > 200) phaseTrail.remove(0);
                    drawPhaseplot(phaseCanvas, phaseTrail, drawPos, drawVel, 150, 10);
                }
            };
            timer.start();
            timers.add(timer);

            tab.setContent(new HBox(20, canvas, controls));
            return tab;
        }

        private void drawSpring(Canvas canvas, double pos, double mass) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            double cx = canvas.getWidth() / 2, ey = 250, my = ey + pos;
            gc.setStroke(Color.DARKGRAY); gc.setLineWidth(4);
            gc.strokeLine(cx - 60, 50, cx + 60, 50);
            gc.setStroke(Color.GRAY); gc.setLineWidth(2);
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

        private Tab createPendulumTab() {
            Tab tab = new Tab(I18n.getInstance().get("mechanics.pendulum.tab", "Pendulum"));
            Canvas canvas = new Canvas(600, 500);

            final Real[] length = { Real.of(200.0) };
            final Real[] angle = { Real.of(Math.PI / 4) };
            final Real[] angVel = { Real.ZERO };
            final Real g = Real.of(9.81);

            VBox controls = new VBox(15);
            controls.setPadding(new Insets(15));
            Slider lSlider = labeledSlider("Length (m)", 1, 5, 2.0, v -> length[0] = Real.of(v * 100));

            Label pLabel = new Label(I18n.getInstance().get("mechanics.period.label", "Phase Space"));
            Canvas phaseCanvas = new Canvas(200, 200);
            List<Point2D> phaseTrail = new ArrayList<>();

            AnimationTimer timer = new AnimationTimer() {
                long last = 0;
                @Override
                public void handle(long now) {
                    if (!running) { last = 0; return; }
                    if (last == 0) { last = now; return; }
                    double dtVal = (now - last) / 1e9;
                    last = now;
                    Real dt = Real.of(dtVal);
                    Real L = length[0].divide(Real.of(100.0));

                    Real[][] res = VerletIntegrator.step(
                            angle, angVel,
                            p -> {
                                double val = p[0].doubleValue();
                                double s = Math.sin(val);
                                return new Real[] { g.negate().divide(L).multiply(Real.of(s)) };
                            }, dt);

                    angle[0] = res[0][0];
                    angVel[0] = res[1][0].multiply(Real.of(0.999));

                    double drawLen = length[0].doubleValue();
                    double drawAng = angle[0].doubleValue();
                    double drawVel = angVel[0].doubleValue();

                    drawPendulum(canvas, drawLen, drawAng);
                    phaseTrail.add(new Point2D(drawAng, drawVel));
                    if (phaseTrail.size() > 200) phaseTrail.remove(0);
                    drawPhaseplot(phaseCanvas, phaseTrail, drawAng, drawVel, 1.5, 3.0);
                }
            };
            timer.start();
            timers.add(timer);

            controls.getChildren().addAll(new Label(I18n.getInstance().get("mechanics.pendulum.controls", "Controls")), new Separator(),
                    lSlider.getParent(), pLabel, phaseCanvas);
            tab.setContent(new HBox(20, canvas, controls));
            return tab;
        }

        private void drawPendulum(Canvas canvas, double len, double ang) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            double px = canvas.getWidth() / 2, py = 80, bx = px + len * Math.sin(ang), by = py + len * Math.cos(ang);
            gc.setFill(Color.DARKGRAY); gc.fillOval(px - 8, py - 8, 16, 16);
            gc.setStroke(Color.GRAY); gc.setLineWidth(3); gc.strokeLine(px, py, bx, by);
            gc.setFill(Color.CRIMSON); gc.fillOval(bx - 20, by - 20, 40, 40);
        }

        private Tab createGravityTab() {
            Tab tab = new Tab(I18n.getInstance().get("mechanics.gravity.tab", "Gravity"));
            Canvas canvas = new Canvas(600, 500);

            final Real[] y = { Real.of(50.0) };
            final Real[] vy = { Real.ZERO };
            final boolean[] dropRunning = { false };
            final Real g = Real.of(9.81 * 30);

            VBox controls = new VBox(15);
            Button drop = new Button(I18n.getInstance().get("mechanics.drop", "Drop"));
            drop.setOnAction(e -> {
                y[0] = Real.of(50.0);
                vy[0] = Real.ZERO;
                dropRunning[0] = true;
            });
            controls.getChildren().addAll(drop);

            AnimationTimer timer = new AnimationTimer() {
                long last = 0;
                @Override
                public void handle(long now) {
                    if (!running) { last = 0; return; }
                    if (last == 0) { last = now; return; }
                    double dtVal = (now - last) / 1e9;
                    last = now;
                    Real dt = Real.of(dtVal);

                    if (dropRunning[0] && y[0].doubleValue() < 420) {
                        Real[][] res = VerletIntegrator.step(y, vy, p -> new Real[] { g }, dt);
                        y[0] = res[0][0];
                        vy[0] = res[1][0];
                    }
                    drawGravity(canvas, y[0].doubleValue());
                }
            };
            timer.start();
            timers.add(timer);

            tab.setContent(new HBox(20, canvas, controls));
            return tab;
        }

        private void drawGravity(Canvas canvas, double y) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, 600, 500);
            gc.setFill(Color.SADDLEBROWN); gc.fillRect(0, 450, 600, 50);
            gc.setFill(Color.ORANGE); gc.fillOval(300 - 25, y, 50, 50);
        }

        private Tab createCollisionTab() {
            Tab tab = new Tab(I18n.getInstance().get("mechanics.collision.tab", "Collision"));
            Canvas canvas = new Canvas(700, 300);

            final Real[] m1 = { Real.of(2.0) };
            final Real[] m2 = { Real.of(1.0) };
            final Real[] v1 = { Real.of(3.0) };
            final Real[] v2 = { Real.of(-1.0) };
            final Real[] x1 = { Real.of(100.0) };
            final Real[] x2 = { Real.of(400.0) };
            final boolean[] collided = { false };

            AnimationTimer timer = new AnimationTimer() {
                long last = 0;
                @Override
                public void handle(long now) {
                    if (!running) { last = 0; return; }
                    if (last == 0) { last = now; return; }
                    double dtVal = (now - last) / 1e9;
                    last = now;
                    Real dt = Real.of(dtVal * 50);

                    x1[0] = x1[0].add(v1[0].multiply(dt));
                    x2[0] = x2[0].add(v2[0].multiply(dt));

                    double r1Val = 20 + m1[0].doubleValue() * 5;
                    double r2Val = 20 + m2[0].doubleValue() * 5;
                    double x1Val = x1[0].doubleValue();
                    double x2Val = x2[0].doubleValue();

                    if (!collided[0] && Math.abs(x1Val - x2Val) < (r1Val + r2Val)) {
                        Real num1 = m1[0].subtract(m2[0]).multiply(v1[0]).add(m2[0].multiply(v2[0]).multiply(Real.of(2.0)));
                        Real den = m1[0].add(m2[0]);
                        Real nv1 = num1.divide(den);

                        Real num2 = m2[0].subtract(m1[0]).multiply(v2[0]).add(m1[0].multiply(v1[0]).multiply(Real.of(2.0)));
                        Real nv2 = num2.divide(den);

                        v1[0] = nv1; v2[0] = nv2;
                        collided[0] = true;
                    } else if (Math.abs(x1Val - x2Val) > (r1Val + r2Val)) collided[0] = false;

                    drawCollision(canvas, x1Val, x2Val, m1[0].doubleValue(), m2[0].doubleValue());
                }
            };
            timer.start();
            timers.add(timer);

            tab.setContent(canvas);
            return tab;
        }

        private void drawCollision(Canvas cv, double x1, double x2, double m1, double m2) {
            GraphicsContext gc = cv.getGraphicsContext2D();
            gc.clearRect(0, 0, 700, 300);
            double r1 = 20 + m1 * 5, r2 = 20 + m2 * 5;
            gc.setFill(Color.STEELBLUE); gc.fillOval(x1 - r1, 150 - r1, r1 * 2, r1 * 2);
            gc.setFill(Color.CORAL); gc.fillOval(x2 - r2, 150 - r2, r2 * 2, r2 * 2);
        }

        private Slider labeledSlider(String name, double min, double max, double val, java.util.function.DoubleConsumer action) {
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

        private void drawPhaseplot(Canvas cv, List<Point2D> trail, double x, double v, double sx, double sv) {
            GraphicsContext gc = cv.getGraphicsContext2D();
            gc.setFill(Color.BLACK); gc.fillRect(0, 0, 200, 200);
            gc.setStroke(Color.LIME); gc.beginPath();
            boolean first = true;
            for (Point2D p : trail) {
                double tx = 100 + (p.getX() / sx) * 80, ty = 100 - (p.getY() / sv) * 80;
                if (first) { gc.moveTo(tx, ty); first = false; } else gc.lineTo(tx, ty);
            }
            gc.stroke();
            gc.setFill(Color.RED); gc.fillOval(100 + (x / sx) * 80 - 3, 100 - (v / sv) * 80 - 3, 6, 6);
        }

        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; } // Should reset?
        @Override public void step() { /* Single step logic? Hard with timers. Skip */ }
        @Override public void setSpeed(double s) { }
        @Override public boolean isPlaying() { return running; }
        
        @Override public String getName() { return "Lab Viewer"; }
        @Override public String getCategory() { return "Physics"; }
    }
}
