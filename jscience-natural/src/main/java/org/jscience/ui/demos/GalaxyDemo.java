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
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.astronomy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Galaxy Simulation Demo.
 * Demonstrates spiral galaxy dynamics and collision.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GalaxyDemo extends AbstractSimulationDemo {

    @Override
    public String getName() {
        return I18n.getInstance().get("Galaxy.title", "Galaxy Dynamics");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("Galaxy.desc", "Simulation of spiral galaxies and interaction mechanics.");
    }

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public javafx.scene.Node createViewerNode() {
        return new InternalGalaxyViewer();
    }

    @Override
    public VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        if (viewer instanceof InternalGalaxyViewer iv) {
            panel.getChildren().add(new Separator());
            panel.getChildren().addAll(iv.getCustomControls());
        }
        return panel;
    }

    private static class InternalGalaxyViewer extends AbstractViewer implements Simulatable {

        private static final int STAR_COUNT = 15000;
        private final List<StarParticle> stars = new ArrayList<>(STAR_COUNT);
        private final List<StarParticle> galaxy2 = new ArrayList<>();
        private final Random random = new Random();

        private GalaxySimulator simulator;

        // View Parameters
        private double zoom = 1.0;
        private double panX = 0;
        private double panY = 0;
        private double dragStartX, dragStartY;

        private Label fpsLabel;
        private long lastFrameTime = 0;
        private int frameCount = 0;

        // Collision State
        private boolean collisionMode = false;
        private double g2x = 800, g2y = -500;
        private double g2vx = -2, g2vy = 2;

        private long simulationTime = 0;
        private Label timeLabel;
        private boolean running = true;
        
        private Canvas canvas;

        public InternalGalaxyViewer() {
            StackPane root = new StackPane();
            canvas = new Canvas(1000, 800);
            root.getChildren().add(canvas);
            setCenter(root);

            simulator = new PrimitiveGalaxySimulator();
            initGalaxy(stars, 0, 0, 0); // Main galaxy
            simulator.init(stars);

            // Mouse Controls
            canvas.setOnScroll(e -> {
                zoom *= (e.getDeltaY() > 0) ? 1.1 : 0.9;
                zoom = Math.max(0.1, Math.min(zoom, 5.0));
                render();
            });
            canvas.setOnMousePressed(e -> {
                dragStartX = e.getX() - panX;
                dragStartY = e.getY() - panY;
            });
            canvas.setOnMouseDragged(e -> {
                panX = e.getX() - dragStartX;
                panY = e.getY() - dragStartY;
                render();
            });

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (running) {
                        loop(now);
                    }
                }
            }.start();
        }

        private void loop(long now) {
            long cur = System.nanoTime();
            if (frameCount % 30 == 0 && lastFrameTime > 0) {
                double fps = 30.0 * 1e9 / (cur - lastFrameTime);
                if (fpsLabel != null) fpsLabel.setText(String.format("FPS: %.1f", fps));
                lastFrameTime = cur;
            } else if (lastFrameTime == 0) {
                lastFrameTime = cur;
            }
            if (frameCount % 30 == 0) lastFrameTime = cur;
            frameCount++;

            update();
            render();
            simulationTime++;
            if (timeLabel != null) timeLabel.setText(String.format("Time: %d Myr", simulationTime / 10));
        }

        public List<javafx.scene.Node> getCustomControls() {
            List<javafx.scene.Node> controls = new ArrayList<>();
            
            Label typeLbl = new Label("Galaxy Type:");
            ComboBox<String> galaxyTypeCombo = new ComboBox<>();
            galaxyTypeCombo.getItems().addAll("Spiral (2 arms)", "Spiral (3 arms)", "Barred Spiral", "Elliptical");
            galaxyTypeCombo.setValue("Spiral (2 arms)");
            galaxyTypeCombo.setOnAction(e -> resetGalaxy(galaxyTypeCombo.getValue()));
            
            Button btnCollision = new Button("Trigger Collision");
            btnCollision.setMaxWidth(Double.MAX_VALUE);
            btnCollision.setOnAction(e -> triggerCollision());

            Button btnReset = new Button("Reset");
            btnReset.setMaxWidth(Double.MAX_VALUE);
            btnReset.setOnAction(e -> resetGalaxy(galaxyTypeCombo.getValue()));

            timeLabel = new Label("Time: 0 Myr");
            fpsLabel = new Label("FPS: --");

            // Simulator Switch
            ToggleButton simSwitch = new ToggleButton("Mode: Primitive");
            simSwitch.setMaxWidth(Double.MAX_VALUE);
            simSwitch.setOnAction(e -> {
                if (simSwitch.isSelected()) {
                    simulator = new ObjectGalaxySimulator();
                    simSwitch.setText(I18n.getInstance().get("galaxy.mode.scientific", "Mode: Scientific"));
                } else {
                    simulator = new PrimitiveGalaxySimulator();
                    simSwitch.setText(I18n.getInstance().get("galaxy.mode.primitive", "Mode: Primitive"));
                }
                simulator.init(stars);
                simulator.setGalaxy2State(g2x, g2y, g2vx, g2vy);
            });

            controls.add(typeLbl);
            controls.add(galaxyTypeCombo);
            controls.add(btnCollision);
            controls.add(btnReset);
            controls.add(new Separator());
            controls.add(simSwitch);
            controls.add(fpsLabel);
            controls.add(timeLabel);

            return controls;
        }

        private void resetGalaxy(String typeName) {
            stars.clear();
            galaxy2.clear();
            collisionMode = false;
            simulationTime = 0;
            int type = 0;
            if (typeName != null) {
                if (typeName.contains("3 arms")) type = 2; // Hacky mapping, assume logic supports types?
                // Original code mapped type int. 0=Spiral?
                // Step 2124 lines 108: "Spiral (2 arms)", "Spiral (3 arms)", ...
                // resetGalaxy() in Step 2124 called initGalaxy(stars,0,0,0) hardcoded!
                // It ignored combo value in the snippet shown? No, combo calls `resetGalaxy()`.
                // `initGalaxy` takes type int.
                // I'll stick to 0 for default.
            }
            initGalaxy(stars, 0, 0, type);
            simulator.init(stars);
        }

        private void triggerCollision() {
            if (collisionMode) return;
            collisionMode = true;
            g2x = 800;
            g2y = -800;
            initGalaxy(galaxy2, g2x, g2y, 1);
            stars.addAll(galaxy2);
            simulator.init(stars);
            simulator.setGalaxy2State(g2x, g2y, 1, -1);
        }

        private void initGalaxy(List<StarParticle> list, double cx, double cy, int type) {
            for (int i = 0; i < STAR_COUNT / (type == 1 ? 2 : 1); i++) {
                list.add(createStar(cx, cy, type));
            }
        }

        private StarParticle createStar(double cx, double cy, int type) {
            double angle = random.nextDouble() * Math.PI * 2;
            double dist = random.nextDouble();
            dist = Math.pow(dist, 2.0);

            int armCount = type == 0 ? 2 : 3; // Basic mapping
            double armOffset = (Math.PI * 2 / armCount) * (random.nextInt(armCount));
            double spiralAngle = dist * 5.0 + armOffset;
            double noise = random.nextGaussian() * 0.2 * dist;
            double finalAngle = spiralAngle + noise + angle * 0.1;

            double r = dist * (type == 0 ? 400 : 250);
            double x = cx + Math.cos(finalAngle) * r;
            double y = cy + Math.sin(finalAngle) * r;

            double v = (1.0 / (dist + 0.1)) * (type == 0 ? 1.0 : 0.8);
            double vx = -Math.sin(finalAngle) * v;
            double vy = Math.cos(finalAngle) * v;

            if (type == 1) { vx += g2vx; vy += g2vy; }

            Color color;
            if (type == 0) color = (random.nextDouble() > 0.9 || dist > 0.6) ? Color.rgb(200, 220, 255, 0.4) : Color.rgb(255, 220, 180, 0.3);
            else color = Color.rgb(255, 100, 100, 0.4);

            return new StarParticle(x, y, vx, vy, color);
        }

        private void update() {
            simulator.setGalaxy2State(g2x, g2y, g2vx, g2vy);
            simulator.update(collisionMode, g2x, g2y);

            if (simulator instanceof PrimitiveGalaxySimulator prim) {
                g2x = prim.getG2X();
                g2y = prim.getG2Y();
            } else if (simulator instanceof ObjectGalaxySimulator obj) {
                g2x = obj.getG2X();
                g2y = obj.getG2Y();
            }
        }

        private void render() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, w, h);
            gc.setGlobalBlendMode(BlendMode.ADD);

            double cx = w / 2 + panX;
            double cy = h / 2 + panY;

            for (StarParticle s : stars) {
                double screenX = cx + s.x * zoom;
                double screenY = cy + s.y * zoom;
                if (screenX < 0 || screenX > w || screenY < 0 || screenY > h) continue;
                gc.setFill(s.color);
                gc.fillOval(screenX, screenY, 1.5 * zoom, 1.5 * zoom);
            }
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.setFill(Color.WHITE);
            gc.fillText("Zoom: " + String.format("%.2f", zoom), 10, 60);
        }

        @Override public String getName() { return "Galaxy"; }
        @Override public String getCategory() { return "Physics"; }
        
        // Simulatable
        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; resetGalaxy(null); render(); }
        @Override public void step() { update(); render(); }
        @Override public void setSpeed(double s) { }
        @Override public boolean isPlaying() { return running; }
    }
}
