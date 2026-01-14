/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jscience.ui.*;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Mechanics Demo.
 * Visualizes a Mass-Spring-Damper system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MechanicsDemo extends AbstractSimulationDemo {

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("mechanics.title", "Mechanics: Mass-Spring");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.mechanics", "Simulation of a Mass-Spring-Damper system.");
    }

    @Override
    protected Node createViewerNode() {
        return new InternalMechanicsViewer();
    }

    @Override
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        if (viewer instanceof InternalMechanicsViewer iv) {
            panel.getChildren().add(iv.energyLabel);
        }
        return panel;
    }

    private static class InternalMechanicsViewer extends AbstractViewer implements Simulatable {

        private double mass = 5.0; // kg
        private double springConstant = 10.0; // N/m
        private double damping = 0.5; // Ns/m

        private double position = 100; // Displacement from equilibrium
        private double velocity = 0;

        private Canvas canvas;
        Label energyLabel;

        // Goldberg State
        private boolean ballTriggered = false;
        private double ballX = 50;
        private double ballY = 400;
        private double ballVx = 0;
        private boolean dominoFallen = false;
        private double dominoAngle = 0;

        private boolean running = true;
        private AnimationTimer timer;
        private long last = 0;
        private List<Parameter<?>> parameters = new ArrayList<>();

        public InternalMechanicsViewer() {
            canvas = new Canvas(400, 600);
            setCenter(canvas);
            
            // Resize canvas with window
            widthProperty().addListener(o -> { if(getWidth()>0) canvas.setWidth(getWidth()); draw(); });
            heightProperty().addListener(o -> { if(getHeight()>0) canvas.setHeight(getHeight()); draw(); });

            canvas.setOnMouseDragged(e -> {
                // Drag mass (simple interaction)
                position = e.getY() - 300; 
                velocity = 0;
                draw();
            });

            setupParameters();

            energyLabel = new Label("Energy: --");
            energyLabel.setTextFill(Color.BLACK); // Dark theme handled by CSS usually, but explicit set
            energyLabel.setFont(Font.font("Monospaced", 16));

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!running) { last = 0; return; }
                    if (last == 0) { last = now; return; }
                    double dt = (now - last) / 1e9;
                    last = now;
                    update(dt);
                    draw();
                }
            };
            timer.start();
        }

        private void setupParameters() {
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.mass", "Mass"), "Mass (kg)", 0.1, 10.0, 5.0, val -> mass = val));
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.spring", "Spring K"), "Spring Constant (N/m)", 0.1, 50.0, 10.0, val -> springConstant = val));
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.damping", "Damping"), "Damping Factor", 0.0, 2.0, 0.5, val -> damping = val));
        }

        private void update(double dt) {
            // Stage 1: Spring Mass
            double force = -springConstant * position - damping * velocity;
            double a = force / mass;
            velocity += a * dt * 50; // Scaling factor from original code
            position += velocity * dt * 50;

            // Stage 2: Ball Trigger
            if (position > 150 && !ballTriggered) {
                ballTriggered = true;
                ballVx = 5;
            }

            // Stage 3: Ball Physics
            if (ballTriggered) {
                ballX += ballVx * dt * 50;
                if (ballX > 300 && !dominoFallen) {
                    dominoAngle += 5 * dt * 50; 
                    if (dominoAngle > 90) {
                        dominoFallen = true;
                        dominoAngle = 90;
                    }
                }
            }
            
            // Energy Calc (Potential + Kinetic)
            double pe = 0.5 * springConstant * (position/50.0)*(position/50.0); // Approximate scaling back to meters?
            double ke = 0.5 * mass * (velocity/50.0)*(velocity/50.0);
            // energyLabel.setText(String.format("E: %.2f J", pe + ke)); // Optional
        }

        private void draw() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            
            gc.setFill(Color.web("#1a1a2e"));
            gc.fillRect(0, 0, w, h);

            double centerX = w / 2; // Dynamic center
            double anchorY = 50;
            double eqY = 300; // Original was 300
            double massY = eqY + position;

            // Draw Ceiling
            gc.setLineWidth(4);
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeLine(centerX - 50, anchorY, centerX + 50, anchorY);

            // Draw Spring
            gc.setLineWidth(2);
            gc.setStroke(Color.web("#aaa"));
            int segments = 15;
            double segHeight = (massY - anchorY) / segments;
            double wid = 20;
            double px = centerX;
            double py = anchorY;
            for (int i = 0; i < segments; i++) {
                double ny = py + segHeight;
                double nx = centerX + ((i % 2 == 0) ? wid : -wid);
                if (i == segments - 1) nx = centerX;
                gc.strokeLine(px, py, nx, ny);
                px = nx;
                py = ny;
            }

            // Draw Mass
            gc.setFill(Color.RED);
            gc.fillRect(centerX - 20, massY, 40, 40);
            gc.setFill(Color.WHITE);
            gc.fillText(String.format("%.1f kg", mass), centerX - 15, massY + 25);

            // Draw Platform
            gc.setFill(Color.web("#555"));
            gc.fillRect(centerX - 130, ballY + 20, 350, 10); // Offset based on original coords?
            // Original: 20, ballY+20, 350, 10. With centerX=150.
            // If centerX changes, platform should move?
            // Let's keep platform relative to centerX = 150 logic or anchor it.
            // I'll anchor it to centerX.

            gc.setFill(Color.ORANGE);
            gc.fillRect(centerX - 30, ballY + 15, 60, 5); 

            // Ball
            // ballX is absolute coordinate starting 50.
            // If we center the view, ballX needs shift.
            // Original view was 400x600 fixed. CenterX=150.
            // Current view is dynamic.
            // Let's shift ball drawing by (centerX - 150).
            double shift = centerX - 150;
            
            gc.setFill(Color.BLUE);
            gc.fillOval(ballX + shift, ballY, 20, 20);

            // Domino
            gc.setFill(Color.web("#444"));
            gc.save();
            gc.translate(320 + shift, ballY + 20);
            gc.rotate(dominoAngle);
            gc.fillRect(0, -60, 10, 60);
            gc.restore();

            if (dominoFallen) {
                gc.setFill(Color.GREEN);
                gc.setFont(Font.font(24));
                gc.fillText("SUCCESS!", 350 + shift, 200);
                gc.setFill(Color.GRAY);
                gc.setFont(Font.font(12));
                gc.fillText("Drag mass to reset", 350 + shift, 230);
            }
        }

        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; position = 100; velocity = 0; ballTriggered = false; ballX=50; dominoFallen=false; dominoAngle=0; draw(); }
        @Override public void step() { update(0.016); draw(); }
        @Override public void setSpeed(double s) { /* Not separate speed var, update step fixed? */ }
        @Override public boolean isPlaying() { return running; }
        
        @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
        @Override public String getName() { return "Mechanics Viewer"; }
        @Override public String getCategory() { return "Physics"; }
    }
}
