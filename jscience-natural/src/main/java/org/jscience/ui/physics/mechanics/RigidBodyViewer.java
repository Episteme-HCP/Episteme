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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 2D Rigid Body Physics Engine Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class RigidBodyViewer extends Application {

    private static class Body {
        double x, y, vx, vy, radius, mass, bounciness = 0.8;
        Color color;

        Body(double x, double y, double r, Color c) {
            this.x = x;
            this.y = y;
            this.radius = r;
            this.color = c;
            this.mass = r * r;
        }
    }

    private List<Body> bodies = new ArrayList<>();
    private double gravity = 0.5;
    private double bounciness = 0.8;
    private Canvas canvas;
    private Label countLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("⚾ Rigid Body Physics");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #00d9ff;");

        countLabel = new Label("Bodies: 0");
        countLabel.setStyle("-fx-text-fill: #aaa;");

        Separator sep1 = new Separator();

        Label gravLabel = new Label("Gravity: 0.5");
        gravLabel.setStyle("-fx-text-fill: #888;");
        Slider gravSlider = new Slider(0, 2, 0.5);
        gravSlider.setShowTickLabels(true);
        gravSlider.valueProperty().addListener((o, ov, nv) -> {
            gravity = nv.doubleValue();
            gravLabel.setText(String.format("Gravity: %.1f", gravity));
        });

        Label bounceLabel = new Label("Bounciness: 0.8");
        bounceLabel.setStyle("-fx-text-fill: #888;");
        Slider bounceSlider = new Slider(0.1, 1.0, 0.8);
        bounceSlider.setShowTickLabels(true);
        bounceSlider.valueProperty().addListener((o, ov, nv) -> {
            bounciness = nv.doubleValue();
            bounceLabel.setText(String.format("Bounciness: %.1f", bounciness));
        });

        Separator sep2 = new Separator();

        Button addBtn = new Button("Add Ball");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(e -> addBody());

        Button add5Btn = new Button("Add 5 Balls");
        add5Btn.setMaxWidth(Double.MAX_VALUE);
        add5Btn.setOnAction(e -> {
            for (int i = 0; i < 5; i++)
                addBody();
        });

        Button clearBtn = new Button("Clear All");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            bodies.clear();
            countLabel.setText("Bodies: 0");
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
        stage.setTitle("JScience - Rigid Body Engine");
        stage.setScene(scene);
        stage.show();
    }

    private void addBody() {
        Random r = new Random();
        double radius = 10 + r.nextDouble() * 20;
        Body b = new Body(100 + r.nextDouble() * 600, 50, radius,
                Color.hsb(r.nextDouble() * 360, 0.7, 0.9));
        b.vx = (r.nextDouble() - 0.5) * 10;
        b.bounciness = bounciness;
        bodies.add(b);
        countLabel.setText("Bodies: " + bodies.size());
    }

    private void update() {
        for (Body b : bodies) {
            b.vy += gravity;
            b.x += b.vx;
            b.y += b.vy;

            // Floor collision
            if (b.y + b.radius > 600) {
                b.y = 600 - b.radius;
                b.vy *= -b.bounciness;
            }
            // Wall collisions
            if (b.x - b.radius < 0) {
                b.x = b.radius;
                b.vx *= -b.bounciness;
            }
            if (b.x + b.radius > 800) {
                b.x = 800 - b.radius;
                b.vx *= -b.bounciness;
            }
        }

        // Inter-body collisions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Body b1 = bodies.get(i);
                Body b2 = bodies.get(j);
                double dx = b2.x - b1.x;
                double dy = b2.y - b1.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double minDist = b1.radius + b2.radius;

                if (dist < minDist) {
                    // Simple impulse response
                    double nx = dx / dist;
                    double ny = dy / dist;
                    double relVelX = b2.vx - b1.vx;
                    double relVelY = b2.vy - b1.vy;
                    double velAlongNormal = relVelX * nx + relVelY * ny;

                    if (velAlongNormal > 0)
                        continue;

                    double e = Math.min(b1.bounciness, b2.bounciness);
                    double jImpulse = -(1 + e) * velAlongNormal;
                    jImpulse /= (1 / b1.mass + 1 / b2.mass);

                    double impulseX = jImpulse * nx;
                    double impulseY = jImpulse * ny;

                    b1.vx -= 1 / b1.mass * impulseX;
                    b1.vy -= 1 / b1.mass * impulseY;
                    b2.vx += 1 / b2.mass * impulseX;
                    b2.vy += 1 / b2.mass * impulseY;

                    // Positional correction to prevent sticking
                    double percent = 0.2, slop = 0.01;
                    double correction = Math.max(minDist - dist - slop, 0) / (1 / b1.mass + 1 / b2.mass) * percent;
                    b1.x -= 1 / b1.mass * nx * correction;
                    b1.y -= 1 / b1.mass * ny * correction;
                    b2.x += 1 / b2.mass * nx * correction;
                    b2.y += 1 / b2.mass * ny * correction;
                }
            }
        }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, 800, 600);

        // Floor line
        gc.setStroke(Color.web("#444"));
        gc.strokeLine(0, 599, 800, 599);

        for (Body b : bodies) {
            gc.setFill(b.color);
            gc.fillOval(b.x - b.radius, b.y - b.radius, b.radius * 2, b.radius * 2);
            gc.setStroke(Color.WHITE);
            gc.strokeOval(b.x - b.radius, b.y - b.radius, b.radius * 2, b.radius * 2);
        }
    }

    public static void show(Stage stage) {
        new RigidBodyViewer().start(stage);
    }
}
