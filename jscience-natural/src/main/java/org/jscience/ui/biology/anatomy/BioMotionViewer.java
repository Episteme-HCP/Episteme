/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.anatomy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Biological Motion Demo.
 * Simulates a simple physics-based walker (Ragdoll/Spring-mass).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class BioMotionViewer extends Application {

    private Canvas canvas;
    private List<Node> nodes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private boolean running = true;

    // Physics parameters
    private double gravity = 9.81;
    private double muscleStrength = 1.0;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        // Canvas
        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        // Sidebar
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-padding: 10; -fx-background-color: #eee;");
        sidebar.setPrefWidth(200);

        Label title = new Label("BioMotion Controls");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Button resetBtn = new Button("Reset Walker");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> initWalker());

        Slider gravSlider = new Slider(0, 20, 9.81);
        Label gravLabel = new Label("Gravity: 9.81");
        gravSlider.valueProperty().addListener((o, ov, nv) -> {
            gravity = nv.doubleValue();
            gravLabel.setText(String.format("Gravity: %.2f", gravity));
        });

        Slider muscleSlider = new Slider(0, 5, 1.0);
        Label muscleLabel = new Label("Muscle Tone: 1.0");
        muscleSlider.valueProperty().addListener((o, ov, nv) -> {
            muscleStrength = nv.doubleValue();
            muscleLabel.setText(String.format("Muscle Tone: %.1f", muscleStrength));
        });

        sidebar.getChildren().addAll(title, resetBtn, new Separator(), gravLabel, gravSlider, muscleLabel,
                muscleSlider);
        root.setRight(sidebar);

        initWalker();

        // Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update(0.016);
                    draw();
                }
            }
        }.start();

        Scene scene = new Scene(root, 1000, 600);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle("JScience - BioMotion");
        stage.setScene(scene);
        stage.show();
    }

    private void initWalker() {
        nodes.clear();
        links.clear();

        // Simple Biped Structure
        // Hip
        Node hip = new Node(400, 200, false);
        nodes.add(hip);

        // Left Leg
        Node lKnee = new Node(380, 250, false);
        Node lFoot = new Node(380, 300, false);
        nodes.add(lKnee);
        nodes.add(lFoot);

        // Right Leg
        Node rKnee = new Node(420, 250, false);
        Node rFoot = new Node(420, 300, false);
        nodes.add(rKnee);
        nodes.add(rFoot);

        // Head
        Node head = new Node(400, 150, false);
        nodes.add(head);

        // Links (Bones)
        links.add(new Link(head, hip, 50));
        links.add(new Link(hip, lKnee, 50));
        links.add(new Link(lKnee, lFoot, 50));
        links.add(new Link(hip, rKnee, 50));
        links.add(new Link(rKnee, rFoot, 50));

        // Floor constraint is implicit in update
    }

    private void update(double dt) {
        // Physics Loop
        for (Node n : nodes) {
            if (n.fixed)
                continue;

            // Gravity
            n.vy += gravity * 10 * dt;

            // Update pos
            n.x += n.vx * dt;
            n.y += n.vy * dt;

            // Floor Collision
            if (n.y > 550) {
                n.y = 550;
                n.vy *= -0.5; // Bounce/Friction
                n.vx *= 0.9;
            }

            // Damping
            n.vx *= 0.99;
            n.vy *= 0.99;
        }

        // Constraints (Springs/Distance)
        for (int i = 0; i < 5; i++) { // Relax multiple times
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

        // Simple Muscle actuation (Walker logic would go here)
        // For now it's just a ragdoll drop to floor
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Floor
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 550, canvas.getWidth(), 50);

        // Links
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        for (Link l : links) {
            gc.strokeLine(l.n1.x, l.n1.y, l.n2.x, l.n2.y);
        }

        // Nodes
        gc.setFill(Color.RED);
        for (Node n : nodes) {
            gc.fillOval(n.x - 5, n.y - 5, 10, 10);
        }
    }

    private static class Node {
        double x, y, vx, vy;
        boolean fixed;

        Node(double x, double y, boolean fixed) {
            this.x = x;
            this.y = y;
            this.fixed = fixed;
        }
    }

    private static class Link {
        Node n1, n2;
        double length;

        Link(Node n1, Node n2, double length) {
            this.n1 = n1;
            this.n2 = n2;
            this.length = length;
        }
    }

    public static void show(Stage stage) {
        new BioMotionViewer().start(stage);
    }
}
