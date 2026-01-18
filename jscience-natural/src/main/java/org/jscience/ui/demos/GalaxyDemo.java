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
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.MessageFormat;

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
        return I18n.getInstance().get("viewer.galaxydemo.name", "Galaxy Dynamics");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.galaxydemo.desc", "Simulation of spiral galaxies and interaction mechanics.");
    }

    @Override
    public String getLongDescription() { 
        return I18n.getInstance().get("viewer.galaxydemo.longdesc", "Detailed galaxy simulation including collisions and formation."); 
    }


    @Override
    public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }

    @Override
    public javafx.scene.Node createViewerNode() {
        return new InternalGalaxyViewer();
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
        private List<Parameter<?>> parameters = new ArrayList<>();

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

            setupParameters();

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (running) {
                        loop(now);
                    }
                }
            }.start();
        }

        private void setupParameters() {
            parameters.add(new NumericParameter(I18n.getInstance().get("viewer.galaxydemo.zoom", "Zoom"), "View Zoom", 0.1, 5.0, 0.1, zoom, v -> zoom = v));
            parameters.add(new BooleanParameter("viewer.galaxydemo.collision", I18n.getInstance().get("viewer.galaxydemo.collision", "Collision Mode"), collisionMode, v -> {
                if (v && !collisionMode) triggerCollision();
                collisionMode = v;
            }));
            
            parameters.add(new Parameter<>("viewer.galaxydemo.engine", I18n.getInstance().get("viewer.galaxydemo.mode", "Simulator Mode"), "Primitive", v -> {
                 if ("Scientific".equals(v)) {
                    simulator = new ObjectGalaxySimulator();
                } else {
                    simulator = new PrimitiveGalaxySimulator();
                }
                simulator.init(stars);
                simulator.setGalaxy2State(g2x, g2y, g2vx, g2vy);
            }));
        }

        private void loop(long now) {
            long cur = System.nanoTime();
            if (frameCount % 30 == 0 && lastFrameTime > 0) {
                double fps = 30.0 * 1e9 / (cur - lastFrameTime);
                if (fpsLabel != null) fpsLabel.setText(MessageFormat.format(I18n.getInstance().get("viewer.galaxydemo.fps", "FPS: {0}"), String.format("%.1f", fps)));
                lastFrameTime = cur;
            } else if (lastFrameTime == 0) {
                lastFrameTime = cur;
            }
            if (frameCount % 30 == 0) lastFrameTime = cur;
            frameCount++;

            update();
            render();
            simulationTime++;
            if (timeLabel != null) timeLabel.setText(MessageFormat.format(I18n.getInstance().get("viewer.galaxydemo.time", "Time: {0} Myr"), simulationTime / 10));
        }

        private void resetGalaxy(int typeIndex) {
            stars.clear();
            galaxy2.clear();
            collisionMode = false;
            simulationTime = 0;
            initGalaxy(stars, 0, 0, typeIndex);
            simulator.init(stars);
        }

        private void triggerCollision() {
            if (collisionMode) return;
            collisionMode = true;
            g2x = 800;
            g2y = -800;
            initGalaxy(galaxy2, g2x, g2y, 1); // Type 1 for red galaxy
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

            int armCount = 2;
            if (type == 2) armCount = 3;
            if (type == 1) armCount = 2; // Collision galaxy
            
            double armOffset = (Math.PI * 2 / armCount) * (random.nextInt(armCount));
            double spiralAngle = dist * 5.0 + armOffset;
            double noise = random.nextGaussian() * 0.2 * dist;
            double finalAngle = spiralAngle + noise + angle * 0.1;

            double r = dist * (type == 0 || type == 2 ? 400 : 250);
            double x = cx + Math.cos(finalAngle) * r;
            double y = cy + Math.sin(finalAngle) * r;

            double v = (1.0 / (dist + 0.1)) * ((type == 0 || type == 2) ? 1.0 : 0.8);
            double vx = -Math.sin(finalAngle) * v;
            double vy = Math.cos(finalAngle) * v;

            if (type == 1) { vx += g2vx; vy += g2vy; }

            Color color;
            if (type == 0 || type == 2) color = (random.nextDouble() > 0.9 || dist > 0.6) ? Color.rgb(200, 220, 255, 0.4) : Color.rgb(255, 220, 180, 0.3);
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
            gc.fillText(MessageFormat.format(I18n.getInstance().get("viewer.galaxydemo.zoom", "Zoom: {0}"), String.format("%.2f", zoom)), 10, 60);
        }

        @Override public String getName() { return I18n.getInstance().get("viewer.galaxydemo.name", "Galaxy Dynamics"); }
        @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
        
        // Simulatable
        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; resetGalaxy(0); render(); }
        @Override public void step() { update(); render(); }
        @Override public void setSpeed(double s) { }
        @Override public boolean isPlaying() { return running; }
    
        @Override public String getDescription() { return I18n.getInstance().get("viewer.galaxydemo.desc", "Simulation of spiral galaxies and interaction mechanics."); }
        @Override public String getLongDescription() { return I18n.getInstance().get("viewer.galaxydemo.longdesc", "Detailed galaxy simulation including collisions and formation."); }
        @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
    }
}

