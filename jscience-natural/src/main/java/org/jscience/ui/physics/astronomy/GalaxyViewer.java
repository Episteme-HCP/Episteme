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

package org.jscience.ui.physics.astronomy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.i18n.I18n;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Spiral Galaxy Visualization.
 * Simulates a density wave spiral structure using a particle system.
 * Demonstrates high-performance rendering with semantic initialization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GalaxyViewer extends Application {

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

    private javafx.scene.control.Label fpsLabel;
    private long lastFrameTime = 0;
    private int frameCount = 0;

    // Collision State
    private boolean collisionMode = false;
    private double g2x = 800, g2y = -500;
    private double g2vx = -2, g2vy = 2;

    // Simulation Time
    private long simulationTime = 0;
    private javafx.scene.control.Label timeLabel;
    private javafx.scene.control.ComboBox<String> galaxyTypeCombo;

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getStyleClass().add("dark-viewer-root");

        Canvas canvas = new Canvas(1000, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // Init Simulator (Default Primitive)
        simulator = new PrimitiveGalaxySimulator();

        // UI Controls Panel
        VBox controls = new VBox(10);
        controls.setPadding(new javafx.geometry.Insets(10));
        controls.getStyleClass().add("dark-viewer-sidebar");
        controls.setMaxWidth(200);
        controls.setMaxHeight(300);
        controls.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        StackPane.setAlignment(controls, javafx.geometry.Pos.TOP_LEFT);

        javafx.scene.control.Label titleLbl = new javafx.scene.control.Label("Galaxy Simulation");
        titleLbl.getStyleClass().add("dark-label-accent");

        // Galaxy type selector
        javafx.scene.control.Label typeLbl = new javafx.scene.control.Label("Galaxy Type:");
        typeLbl.getStyleClass().add("dark-label");
        galaxyTypeCombo = new javafx.scene.control.ComboBox<>();
        galaxyTypeCombo.getItems().addAll("Spiral (2 arms)", "Spiral (3 arms)", "Barred Spiral", "Elliptical");
        galaxyTypeCombo.setValue("Spiral (2 arms)");
        galaxyTypeCombo.setOnAction(e -> resetGalaxy());

        javafx.scene.control.Button btnCollision = new javafx.scene.control.Button("Trigger Collision");
        btnCollision.setMaxWidth(Double.MAX_VALUE);
        btnCollision.setOnAction(e -> triggerCollision());

        javafx.scene.control.Button btnReset = new javafx.scene.control.Button("Reset");
        btnReset.setMaxWidth(Double.MAX_VALUE);
        btnReset.setOnAction(e -> resetGalaxy());

        timeLabel = new javafx.scene.control.Label("Time: 0 Myr");
        timeLabel.getStyleClass().add("dark-label-accent");

        // FPS Label
        fpsLabel = new javafx.scene.control.Label("FPS: --");
        fpsLabel.getStyleClass().add("dark-label");

        // Simulator Switch
        javafx.scene.control.ToggleButton simSwitch = new javafx.scene.control.ToggleButton("Mode: Primitive");
        simSwitch.setMaxWidth(Double.MAX_VALUE);
        simSwitch.setOnAction(e -> {
            if (simSwitch.isSelected()) {
                simulator = new ObjectGalaxySimulator();
                simSwitch.setText(I18n.getInstance().get("galaxy.mode.scientific"));
            } else {
                simulator = new PrimitiveGalaxySimulator();
                simSwitch.setText(I18n.getInstance().get("galaxy.mode.primitive"));
            }
            // Re-init with current stars
            simulator.init(stars);
            // Sync G2 state if needed?
            // Currently simple reset might be cleaner but let's try to preserve state?
            // ObjectSim expects Init to create internal vectors.
            // If we swap mid-sim, we should probably reset or sync.
            // Let's reset for simplicity as user can just hit reset.
            // Or just init, it will grab current positions.
            simulator.setGalaxy2State(g2x, g2y, g2vx, g2vy);
        });

        javafx.scene.control.Label infoLbl = new javafx.scene.control.Label("Controls:\n• Scroll: Zoom\n• Drag: Pan");
        infoLbl.getStyleClass().add("dark-label-muted");

        controls.getChildren().addAll(titleLbl, new javafx.scene.control.Separator(), typeLbl, galaxyTypeCombo,
                btnCollision, btnReset, new javafx.scene.control.Separator(),
                simSwitch, fpsLabel, timeLabel, infoLbl);
        root.getChildren().add(controls);

        initGalaxy(stars, 0, 0, 0); // Main galaxy
        simulator.init(stars);

        // Mouse Controls
        canvas.setOnScroll(e -> {
            zoom *= (e.getDeltaY() > 0) ? 1.1 : 0.9;
            zoom = Math.max(0.1, Math.min(zoom, 5.0));
        });
        canvas.setOnMousePressed(e -> {
            dragStartX = e.getX() - panX;
            dragStartY = e.getY() - panY;
        });
        canvas.setOnMouseDragged(e -> {
            panX = e.getX() - dragStartX;
            panY = e.getY() - dragStartY;
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // FPS Calc
                long cur = System.nanoTime();
                if (frameCount % 30 == 0 && lastFrameTime > 0) {
                    double fps = 30.0 * 1e9 / (cur - lastFrameTime);
                    fpsLabel.setText(String.format("FPS: %.1f", fps));
                    lastFrameTime = cur;
                } else if (lastFrameTime == 0) {
                    lastFrameTime = cur;
                }
                if (frameCount % 30 == 0)
                    lastFrameTime = cur;
                frameCount++;

                update();
                render(gc, canvas.getWidth(), canvas.getHeight());
                simulationTime++;
                timeLabel.setText(String.format("Time: %d Myr", simulationTime / 10));
            }
        }.start();

        Scene scene = new Scene(root, 1000, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.galaxy"));
        stage.setScene(scene);
        stage.show();
    }

    private void resetGalaxy() {
        stars.clear();
        galaxy2.clear();
        collisionMode = false;
        simulationTime = 0;
        initGalaxy(stars, 0, 0, 0);
        simulator.init(stars);
    }

    private void triggerCollision() {
        if (collisionMode)
            return;
        collisionMode = true;
        g2x = 800;
        g2y = -800;
        initGalaxy(galaxy2, g2x, g2y, 1);
        stars.addAll(galaxy2);
        simulator.init(stars); // Re-init with new stars
        simulator.setGalaxy2State(g2x, g2y, 1, -1); // approximate core velocity

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

        int armCount = type == 0 ? 2 : 3;
        double armOffset = (Math.PI * 2 / armCount) * (random.nextInt(armCount));
        double spiralAngle = dist * 5.0 + armOffset;

        double noise = random.nextGaussian() * 0.2 * dist;
        double finalAngle = spiralAngle + noise + angle * 0.1;

        double r = dist * (type == 0 ? 400 : 250);

        double x = cx + Math.cos(finalAngle) * r;
        double y = cy + Math.sin(finalAngle) * r;

        // Orbital velocity (simplified) around center
        double v = (1.0 / (dist + 0.1)) * (type == 0 ? 1.0 : 0.8);
        double vx = -Math.sin(finalAngle) * v; // Tangential
        double vy = Math.cos(finalAngle) * v;

        // Add Grid Velocity if Galaxy 2
        if (type == 1) {
            vx += g2vx;
            vy += g2vy;
        }

        Color color;
        if (type == 0)
            color = (random.nextDouble() > 0.9 || dist > 0.6) ? Color.rgb(200, 220, 255, 0.4)
                    : Color.rgb(255, 220, 180, 0.3);
        else
            color = Color.rgb(255, 100, 100, 0.4); // Red galaxy

        return new StarParticle(x, y, vx, vy, color);
    }

    private void update() {
        // Delegate to Simulator
        simulator.setGalaxy2State(g2x, g2y, g2vx, g2vy); // Pass current view state if primitive?
        // Actually, if we are in Object mode, the Simulator manages state.
        // If we are in Primitive mode, the Simulator manages state.
        // We really shouldn't manage state in View for G2 anymore if we outsource it.
        // But `g2x` etc are fields here.
        // Let's assume Simulator updates its own state, and we just call Update.
        // BUT we need to render the Core (G2).
        // We should add getters to Simulator (or Primitive implementation).
        // Since interface doesn't enforce getters for G2, we might fail to sync.
        // For Benchmark, preserving Logic in View for Primitive and logic in Sim for
        // Object is tricky if they diverge.

        simulator.update(collisionMode, g2x, g2y);

        // Update View state from Simulator (for G2 rendering if it moved)
        if (simulator instanceof PrimitiveGalaxySimulator) {
            PrimitiveGalaxySimulator prim = (PrimitiveGalaxySimulator) simulator;
            g2x = prim.getG2X();
            g2y = prim.getG2Y();
        } else if (simulator instanceof ObjectGalaxySimulator) {
            ObjectGalaxySimulator obj = (ObjectGalaxySimulator) simulator;
            g2x = obj.getG2X();
            g2y = obj.getG2Y();
        }
    }

    private void render(GraphicsContext gc, double w, double h) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);
        gc.setGlobalBlendMode(BlendMode.ADD);

        double cx = w / 2 + panX;
        double cy = h / 2 + panY;

        for (StarParticle s : stars) {
            // Apply Zoom
            double screenX = cx + s.x * zoom;
            double screenY = cy + s.y * zoom;

            if (screenX < 0 || screenX > w || screenY < 0 || screenY > h)
                continue;

            gc.setFill(s.color);
            gc.fillOval(screenX, screenY, 1.5 * zoom, 1.5 * zoom);
        }
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);

        gc.setFill(Color.WHITE);
        gc.fillText("Zoom: " + String.format("%.2f", zoom), 10, 60);
    }

    public static class StarParticle {
        double x, y;
        double vx, vy;
        Color color;

        StarParticle(double x, double y, double vx, double vy, Color c) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = c;
        }
    }

    public static void show(Stage stage) {
        new GalaxyViewer().start(stage);
    }
}
