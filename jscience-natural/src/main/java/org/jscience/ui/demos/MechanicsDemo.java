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
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

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
    public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }

    @Override
    public String getName() {
        return I18n.getInstance().get("mechanics.title", "Mechanics: Mass-Spring");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.mechanics", "Simulation of a Mass-Spring-Damper system.");
    }

    @Override
    public String getLongDescription() { 
        return I18n.getInstance().get("viewer.mechanics.long", "Detailed simulation of a Mass-Spring-Damper system."); 
    }


    @Override
    public Node createViewerNode() {
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

        private final MassSpringSystem system;
        private Canvas canvas;
        Label energyLabel;

        // Rube Goldberg State (Demo specific)
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
            system = new MassSpringSystem(5.0, 10.0, 0.5);
            system.setPosition(2.0); // 2 meters displacement initially

            canvas = new Canvas(400, 600);
            setCenter(canvas);
            
            // Resize canvas with window
            widthProperty().addListener(o -> { if(getWidth()>0) canvas.setWidth(getWidth()); draw(); });
            heightProperty().addListener(o -> { if(getHeight()>0) canvas.setHeight(getHeight()); draw(); });

            canvas.setOnMouseDragged(e -> {
                // Drag mass (simple interaction)
                system.setPosition((e.getY() - 300) / 50.0);
                system.setVelocity(0);
                draw();
            });

            setupParameters();

            energyLabel = new Label(I18n.getInstance().get("generated.mechanics.energy", "Energy: --"));
            energyLabel.getStyleClass().add("description-label");
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
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.mass", "Mass"), "Mass (kg)", 0.1, 10.0, 0.1, 5.0, val -> system.setMass(val)));
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.spring", "Spring K"), "Spring Constant (N/m)", 0.1, 50.0, 0.1, 10.0, val -> system.setSpringConstant(val)));
            parameters.add(new NumericParameter(I18n.getInstance().get("mechanics.damping", "Damping"), "Damping Factor", 0.0, 2.0, 0.1, 0.5, val -> system.setDamping(val)));
        }

        private void update(double dt) {
            system.step(dt * 50);

            if (system.getPosition() > 3.0 && !ballTriggered) {
                ballTriggered = true;
                ballVx = 5;
            }

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
            
            Quantity<Energy> energy = system.getTotalEnergy();
            if (energyLabel != null) energyLabel.setText(String.format("E: %.2f J", energy.getValue().doubleValue()));
        }

        private void draw() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            
            gc.setFill(Color.web("#fdfbf7"));
            gc.fillRect(0, 0, w, h);

            double centerX = w / 2;
            double anchorY = 50;
            double eqY = 300; 
            double massY = eqY + system.getPosition() * 50.0;

            gc.setLineWidth(4);
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeLine(centerX - 50, anchorY, centerX + 50, anchorY);

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

            gc.setFill(Color.RED);
            gc.fillRect(centerX - 20, massY, 40, 40);
            gc.setFill(Color.WHITE);
            gc.fillText(String.format("%.1f kg", system.getMass()), centerX - 15, massY + 25);

            gc.setFill(Color.web("#555"));
            gc.fillRect(centerX - 130, ballY + 20, 350, 10); 

            gc.setFill(Color.ORANGE);
            gc.fillRect(centerX - 30, ballY + 15, 60, 5); 

            double shift = centerX - 150;
            gc.setFill(Color.BLUE);
            gc.fillOval(ballX + shift, ballY, 20, 20);

            gc.setFill(Color.web("#444"));
            gc.save();
            gc.translate(320 + shift, ballY + 20);
            gc.rotate(dominoAngle);
            gc.fillRect(0, -60, 10, 60);
            gc.restore();

            if (dominoFallen) {
                gc.setFill(Color.GREEN);
                gc.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 24));
                gc.fillText("SUCCESS!", 350 + shift, 200);
                gc.setFill(Color.GRAY);
                gc.setFont(Font.font(12));
                gc.fillText("Drag mass to reset", 350 + shift, 230);
            }
        }

        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; system.setPosition(2.0); system.setVelocity(0); ballTriggered = false; ballX=50; dominoFallen=false; dominoAngle=0; draw(); }
        @Override public void step() { update(0.016); draw(); }
        @Override public void setSpeed(double s) { }
        @Override public boolean isPlaying() { return running; }
        
        @Override public List<Parameter<?>> getViewerParameters() { return parameters; }

        @Override
        public String getName() {
            return I18n.getInstance().get("viewer.mechanics.name", "Mechanics Viewer");
        }

        @Override
        public String getCategory() {
            return I18n.getInstance().get("category.physics", "Physics");
        }

        @Override
        public String getDescription() {
            return I18n.getInstance().get("viewer.mechanics.desc", "Visual simulation of mass-spring physics.");
        }

        @Override
        public String getLongDescription() {
            return I18n.getInstance().get("viewer.mechanics.long", "Detailed simulation of a Mass-Spring-Damper system.");
        }
    }

    private static class MassSpringSystem {

        private double mass;           // kg
        private double springConstant; // N/m
        private double damping;        // Ns/m
    
        private double position;       // m (displacement from equilibrium)
        private double velocity;       // m/s
    
        public MassSpringSystem(double mass, double k, double c) {
            this.mass = mass;
            this.springConstant = k;
            this.damping = c;
            this.position = 0;
            this.velocity = 0;
        }
    
        public void step(double dt) {
            // F = -k*x - c*v
            double force = -springConstant * position - damping * velocity;
            double acceleration = force / mass;
            
            // Simple Euler integration
            velocity += acceleration * dt;
            position += velocity * dt;
        }
    
        // Getters and Setters
        public double getPosition() { return position; }
        public void setPosition(double position) { this.position = position; }
        public double getVelocity() { return velocity; }
        public void setVelocity(double velocity) { this.velocity = velocity; }
        public double getMass() { return mass; }
        public void setMass(double mass) { this.mass = mass; }
        public double getSpringConstant() { return springConstant; }
        public void setSpringConstant(double k) { this.springConstant = k; }
        public double getDamping() { return damping; }
        public void setDamping(double c) { this.damping = c; }
    
        /**
         * Calculates total energy: E = Kinetic + Potential
         */
        public Quantity<Energy> getTotalEnergy() {
            double ke = 0.5 * mass * velocity * velocity;
            double pe = 0.5 * springConstant * position * position;
            return Quantities.create(ke + pe, Units.JOULE);
        }
    }
}
