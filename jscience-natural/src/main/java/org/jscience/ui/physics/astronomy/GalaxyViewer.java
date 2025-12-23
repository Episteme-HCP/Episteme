/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

    // View Parameters
    private double zoom = 1.0;
    private double panX = 0;
    private double panY = 0;
    private double dragStartX, dragStartY;

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
        root.setStyle("-fx-background-color: black;");

        Canvas canvas = new Canvas(1000, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // UI Controls Panel
        VBox controls = new VBox(10);
        controls.setPadding(new javafx.geometry.Insets(10));
        controls.setStyle("-fx-background-color: rgba(20,20,40,0.8); -fx-padding: 15;");
        controls.setMaxWidth(200);
        controls.setMaxHeight(300);
        controls.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        StackPane.setAlignment(controls, javafx.geometry.Pos.TOP_LEFT);

        javafx.scene.control.Label titleLbl = new javafx.scene.control.Label("Galaxy Simulation");
        titleLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #8af;");

        // Galaxy type selector
        javafx.scene.control.Label typeLbl = new javafx.scene.control.Label("Galaxy Type:");
        typeLbl.setStyle("-fx-text-fill: white;");
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
        timeLabel.setStyle("-fx-text-fill: cyan; -fx-font-family: monospace;");

        javafx.scene.control.Label infoLbl = new javafx.scene.control.Label("Controls:\n• Scroll: Zoom\n• Drag: Pan");
        infoLbl.setStyle("-fx-text-fill: #888;");

        controls.getChildren().addAll(titleLbl, new javafx.scene.control.Separator(), typeLbl, galaxyTypeCombo,
                btnCollision, btnReset, new javafx.scene.control.Separator(), timeLabel, infoLbl);
        root.getChildren().add(controls);

        initGalaxy(stars, 0, 0, 0); // Main galaxy

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
                update();
                render(gc, canvas.getWidth(), canvas.getHeight());
                simulationTime++;
                timeLabel.setText(String.format("Time: %d Myr", simulationTime / 10));
            }
        }.start();

        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("JScience Galaxy Viewer - Spiral Structure Simulation");
        stage.setScene(scene);
        stage.show();
    }

    private void resetGalaxy() {
        stars.clear();
        galaxy2.clear();
        collisionMode = false;
        simulationTime = 0;
        initGalaxy(stars, 0, 0, 0);
    }

    private void triggerCollision() {
        if (collisionMode)
            return;
        collisionMode = true;
        g2x = 800;
        g2y = -800;
        initGalaxy(galaxy2, g2x, g2y, 1);
        stars.addAll(galaxy2);
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
        if (collisionMode) {
            // Move Galaxy Cores
            double dx = g2x - 0;
            double dy = g2y - 0;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double force = 10000.0 / (dist * dist + 100); // Softened gravity

            // Integrate Cores (Simplified 2-body)
            g2vx -= (force * dx / dist);
            g2vy -= (force * dy / dist);
            g2x += g2vx;
            g2y += g2vy;

            // Perturb stars
            for (StarParticle s : stars) {
                // Gravity from Center A (0,0) - already implicitly in orbital v? No, we need
                // explicit integration now.
                // We switch to N-body with 2 attractors

                // Attractor 1 (0,0)
                double d1 = Math.sqrt(s.x * s.x + s.y * s.y);
                double f1 = 500.0 / (d1 * d1 + 100);
                s.vx -= (f1 * s.x / d1);
                s.vy -= (f1 * s.y / d1);

                // Attractor 2 (g2x, g2y)
                double d2x = s.x - g2x;
                double d2y = s.y - g2y;
                double d2 = Math.sqrt(d2x * d2x + d2y * d2y);
                double f2 = 300.0 / (d2 * d2 + 100);
                s.vx -= (f2 * d2x / d2);
                s.vy -= (f2 * d2y / d2);

                s.x += s.vx;
                s.y += s.vy;
            }
        } else {
            // Static Rotation (Visual only)
            for (StarParticle s : stars) {
                // Rotate around 0,0
                double x = s.x;
                double y = s.y;
                double r = Math.sqrt(x * x + y * y);
                double ang = Math.atan2(y, x);
                double v = (1.0 / (r / 200.0 + 0.1)) * 0.05; // Speed
                ang += v;
                s.x = Math.cos(ang) * r;
                s.y = Math.sin(ang) * r;
            }
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

    private static class StarParticle {
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
