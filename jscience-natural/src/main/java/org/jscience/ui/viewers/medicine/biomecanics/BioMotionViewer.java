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

package org.jscience.ui.viewers.medicine.biomecanics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;


import java.util.ArrayList;
import java.util.List;

/**
 * Biological Motion Viewer.
 * Simulates a simple physics-based walker (Ragdoll/Spring-mass).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioMotionViewer extends AbstractViewer implements Simulatable {

    private Canvas canvas;
    private List<Node> nodes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private boolean running = true;
    private AnimationTimer timer;
    private double speed = 1.0;

    private double gravity = 9.81;
    private double muscleStrength = 1.0;

    @Override
    public String getName() { return I18n.getInstance().get("viewer.biomotionviewer.name", "BioMotion Viewer"); }
    
    @Override
    public String getCategory() { return I18n.getInstance().get("category.medicine", "Medicine"); }

    public BioMotionViewer() {
        initUI();
    }

    private void initUI() {
        this.getStyleClass().add("viewer-root");

        canvas = new Canvas(800, 600);
        this.setCenter(canvas);
        
        this.widthProperty().addListener((o, old, val) -> { canvas.setWidth(val.doubleValue() - 200); draw(); });
        this.heightProperty().addListener((o, old, val) -> { canvas.setHeight(val.doubleValue()); draw(); });

        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.setPrefWidth(200);

        Label title = new Label(I18n.getInstance().get("biomotion.controls", "BioMotion Controls"));
        title.getStyleClass().add("header-label");

        Button resetBtn = new Button(I18n.getInstance().get("biomotion.reset", "Reset Walker"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> initWalker());

        Slider gravSlider = new Slider(0, 20, 9.81);
        Label gravLabel = new Label(String.format("Gravity: %.2f", 9.81));
        gravLabel.getStyleClass().add("description-label");
        gravSlider.valueProperty().addListener((o, ov, nv) -> {
            gravity = nv.doubleValue();
            gravLabel.setText(String.format("Gravity: %.2f", gravity));
        });

        Slider muscleSlider = new Slider(0, 5, 1.0);
        Label muscleLabel = new Label(String.format("Muscle Tone: %.1f", 1.0));
        muscleLabel.getStyleClass().add("description-label");
        muscleSlider.valueProperty().addListener((o, ov, nv) -> {
            muscleStrength = nv.doubleValue();
            muscleLabel.setText(String.format("Muscle Tone: %.1f", muscleStrength));
        });

        sidebar.getChildren().addAll(title, resetBtn, new Separator(), gravLabel, gravSlider, muscleLabel, muscleSlider);
        this.setRight(sidebar);

        initWalker();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update(0.016 * speed);
                    draw();
                }
            }
        };
        timer.start();
    }

    @Override public void play() { running = true; }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; initWalker(); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }
    @Override public void step() { update(0.016); draw(); }

    private void initWalker() {
        nodes.clear();
        links.clear();

        Node hip = new Node(400, 200, false);
        nodes.add(hip);

        Node lKnee = new Node(380, 250, false);
        Node lFoot = new Node(380, 300, false);
        nodes.add(lKnee);
        nodes.add(lFoot);

        Node rKnee = new Node(420, 250, false);
        Node rFoot = new Node(420, 300, false);
        nodes.add(rKnee);
        nodes.add(rFoot);

        Node head = new Node(400, 150, false);
        nodes.add(head);

        links.add(new Link(head, hip, 50));
        links.add(new Link(hip, lKnee, 50));
        links.add(new Link(lKnee, lFoot, 50));
        links.add(new Link(hip, rKnee, 50));
        links.add(new Link(rKnee, rFoot, 50));
    }

    private void update(double dt) {
        for (Node n : nodes) {
            if (n.fixed) continue;
            n.vy += gravity * 10 * dt;
            n.x += n.vx * dt;
            n.y += n.vy * dt;

            double floor = canvas.getHeight() - 50;
            if (n.y > floor) {
                n.y = floor;
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
                if (dist == 0) continue;
                double diff = (dist - l.length) / dist;

                double moveX = dx * diff * 0.5 * muscleStrength;
                double moveY = dy * diff * 0.5 * muscleStrength;

                if (!l.n1.fixed) { l.n1.x += moveX; l.n1.y += moveY; }
                if (!l.n2.fixed) { l.n2.x -= moveX; l.n2.y -= moveY; }
            }
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, canvas.getHeight() - 50, canvas.getWidth(), 50);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        for (Link l : links) {
            gc.strokeLine(l.n1.x, l.n1.y, l.n2.x, l.n2.y);
        }

        gc.setFill(Color.CORAL);
        for (Node n : nodes) {
            gc.fillOval(n.x - 8, n.y - 8, 16, 16);
        }
    }

    private static class Node {
        double x, y, vx, vy;
        boolean fixed;
        Node(double x, double y, boolean fixed) { this.x = x; this.y = y; this.fixed = fixed; }
    }

    private static class Link {
        Node n1, n2;
        double length;
        Link(Node n1, Node n2, double length) { this.n1 = n1; this.n2 = n2; this.length = length; }
    }

    @Override public String getDescription() { return I18n.getInstance().get("viewer.biomotionviewer.desc", "Physics-based biological motion simulator."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.biomotionviewer.longdesc", "Simulates biological movement using a spring-mass ragdoll model. Adjust gravity and muscle tone to observe how they affect the walker's dynamics and stability."); }
    @Override public List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
