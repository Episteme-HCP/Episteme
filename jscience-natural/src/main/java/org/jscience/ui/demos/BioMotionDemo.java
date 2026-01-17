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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Biological Motion Demo.
 * Simulates a simple physics-based walker (Ragdoll/Spring-mass).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioMotionDemo extends AbstractSimulationDemo {

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("biomotion.title", "BioMotion Simulation");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("biomotion.desc",
                "Physics-based biological motion simulation (Skeleton/Walker).");
    }

    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("biomotion.long_desc", 
            "A simulation of biological motion using a skeleton model with muscle and gravity dynamics.");
    }

    @Override
    public javafx.scene.Node createViewerNode() {
        return new InternalViewer();
    }

    @Override
    public VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        if (viewer instanceof InternalViewer iv) {
            panel.getChildren().add(new Separator());
            Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.controls", "Controls"));
            title.getStyleClass().add("font-bold"); // Replaced inline style: -fx-font-weight: bold;
            
            Button resetBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.reset", "Reset Walker"));
            resetBtn.setMaxWidth(Double.MAX_VALUE);
            resetBtn.setOnAction(e -> iv.reset());

            Slider gravSlider = new Slider(0, 20, 9.81);
            Label gravLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.gravity", "Gravity: %.2f", 9.81));
            gravSlider.valueProperty().addListener((o, ov, nv) -> {
                iv.setGravity(nv.doubleValue());
                gravLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.gravity", "Gravity: %.2f", nv.doubleValue()));
            });

            Slider muscleSlider = new Slider(0, 5, 1.0);
            Label muscleLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.muscle", "Muscle Tone: %.1f", 1.0));
            muscleSlider.valueProperty().addListener((o, ov, nv) -> {
                iv.setMuscleStrength(nv.doubleValue());
                muscleLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("biomotion.muscle", "Muscle Tone: %.1f", nv.doubleValue()));
            });

            panel.getChildren().addAll(title, resetBtn, new Separator(), gravLabel, gravSlider, muscleLabel, muscleSlider);
        }
        return panel;
    }

    private static class InternalViewer extends AbstractViewer implements Simulatable {

        private Canvas canvas;
        private List<WalkerNode> nodes = new ArrayList<>();
        private List<Link> links = new ArrayList<>();
        private boolean running = false;
        private double gravity = 9.81;
        private double muscleStrength = 1.0;
        private AnimationTimer timer;

        public InternalViewer() {
            canvas = new Canvas(800, 600);
            this.setCenter(canvas);
            initWalker();
            draw();

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (running) {
                        update(0.016);
                        draw();
                    }
                }
            };
            timer.start();
        }

        public void reset() {
            initWalker();
            draw();
        }

        public void setGravity(double g) {
            this.gravity = g;
        }

        public void setMuscleStrength(double s) {
            this.muscleStrength = s;
        }

        @Override
    public String getCategory() { return "Biology"; }

        @Override
        public String getName() { return "BioMotion"; }

        @Override
        public void play() {
            running = true;
        }

        @Override
        public void pause() {
            running = false;
        }

        @Override
        public void stop() {
            running = false;
            initWalker();
            draw();
        }

        @Override
        public void step() {
            update(0.016);
            draw();
        }

        @Override
        public void setSpeed(double speedMultiplier) {
            // Not implemented in simple logic, update(dt) uses fixed dt
        }

        @Override
        public boolean isPlaying() {
            return running;
        }

        private void initWalker() {
            nodes.clear();
            links.clear();

            // Hip
            WalkerNode hip = new WalkerNode(400, 200, false);
            nodes.add(hip);

            // Left Leg
            WalkerNode lKnee = new WalkerNode(380, 250, false);
            WalkerNode lFoot = new WalkerNode(380, 300, false);
            nodes.add(lKnee);
            nodes.add(lFoot);

            // Right Leg
            WalkerNode rKnee = new WalkerNode(420, 250, false);
            WalkerNode rFoot = new WalkerNode(420, 300, false);
            nodes.add(rKnee);
            nodes.add(rFoot);

            // Head
            WalkerNode head = new WalkerNode(400, 150, false);
            nodes.add(head);

            // Links (Bones)
            links.add(new Link(head, hip, 50));
            links.add(new Link(hip, lKnee, 50));
            links.add(new Link(lKnee, lFoot, 50));
            links.add(new Link(hip, rKnee, 50));
            links.add(new Link(rKnee, rFoot, 50));
        }

        private void update(double dt) {
            for (WalkerNode n : nodes) {
                if (n.fixed) continue;
                n.vy += gravity * 10 * dt;
                n.x += n.vx * dt;
                n.y += n.vy * dt;

                // Floor Collision
                if (n.y > 550) {
                    n.y = 550;
                    n.vy *= -0.5;
                    n.vx *= 0.9;
                }
                n.vx *= 0.99;
                n.vy *= 0.99;
            }

            for (int i = 0; i < 5; i++) {
                for (Link l : links) {
                    double dx = l.n2.x - l.n1.x;
                    double dy = l.n2.y - l.n1.y;
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    double diff = (dist - l.length) / dist;

                    double moveX = dx * diff * 0.5 * muscleStrength;
                    double moveY = dy * diff * 0.5 * muscleStrength;

                    if (!l.n1.fixed) {
                        l.n1.x += moveX;
                        l.n1.y += moveY;
                    }
                    if (!l.n2.fixed) {
                        l.n2.x -= moveX;
                        l.n2.y -= moveY;
                    }
                }
            }
        }

        private void draw() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(0, 550, canvas.getWidth(), 50);

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(5);
            for (Link l : links) {
                gc.strokeLine(l.n1.x, l.n1.y, l.n2.x, l.n2.y);
            }

            gc.setFill(Color.RED);
            for (WalkerNode n : nodes) {
                gc.fillOval(n.x - 5, n.y - 5, 10, 10);
            }
        }
    
        @Override
        public String getDescription() { return "InternalViewer Internal Viewer"; }

        
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }

    @Override
    public String getLongDescription() { return getDescription(); }
}

    private static class WalkerNode {
        double x, y, vx, vy;
        boolean fixed;

        WalkerNode(double x, double y, boolean fixed) {
            this.x = x;
            this.y = y;
            this.fixed = fixed;
        }
    }

    private static class Link {
        WalkerNode n1, n2;
        double length;

        Link(WalkerNode n1, WalkerNode n2, double length) {
            this.n1 = n1;
            this.n2 = n2;
            this.length = length;
        }
    }


}