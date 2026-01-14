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
import org.jscience.ui.viewers.physics.classical.matter.fluids.FluidSolver;
import org.jscience.ui.viewers.physics.classical.matter.fluids.ObjectFluidSolver;
import org.jscience.ui.viewers.physics.classical.matter.fluids.PrimitiveFluidSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enhanced Fluid Dynamics Demo.
 * Real-time fluid simulation with particle visualization and controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidDynamicsDemo extends AbstractSimulationDemo {

    @Override
    public String getName() {
        return I18n.getInstance().get("fluid.title", "Fluid Dynamics");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("fluid.info", "Fluid simulation using simplified Navier-Stokes equations.");
    }

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public javafx.scene.Node createViewerNode() {
        return new InternalFluidViewer();
    }

    @Override
    public VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        if (viewer instanceof InternalFluidViewer iv) {
            panel.getChildren().addAll(iv.getCustomControls());
        }
        return panel;
    }

    private static class InternalFluidViewer extends AbstractViewer implements Simulatable {

        private int N = 100;
        private static final int SCALE = 6;
        private static final int PARTICLE_COUNT = 5000;

        private double viscosity = 0.0001;
        private boolean showField = true;
        private boolean showParticles = true;
        private String colorScheme = "Blue";
        private double zOff = 0;
        private double simSpeed = 1.0;

        private List<Particle> particles = new ArrayList<>();
        private Random rand = new Random();
        private FluidSolver solver;
        private long lastFrameTime = 0;
        private int frameCount = 0;
        private Label fpsLabel;

        private double mouseX = -1, mouseY = -1;
        private boolean mousePressed = false;
        private boolean running = false;
        private AnimationTimer timer;
        private Canvas canvas;

        public InternalFluidViewer() {
            canvas = new Canvas(N * SCALE, N * SCALE);
            
            // Mouse interaction
            canvas.setOnMousePressed(e -> {
                mousePressed = true;
                mouseX = e.getX();
                mouseY = e.getY();
            });
            canvas.setOnMouseDragged(e -> {
                mouseX = e.getX();
                mouseY = e.getY();
            });
            canvas.setOnMouseReleased(e -> mousePressed = false);

            this.setCenter(canvas); // Assuming AbstractViewer extends BorderPane

            // Initialize
            solver = new PrimitiveFluidSolver();
            solver.initialize(N, SCALE);
            resetParticles();

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (running) {
                        loop(now);
                    }
                }
            };
            timer.start();
            running = true; // Auto-start
        }

        private void loop(long now) {
            // FPS Calc
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

            solver.step(simSpeed, viscosity, zOff);

            if (mousePressed) {
                solver.addForce(mouseX, mouseY, 0, 0); 
            } else {
                if (solver instanceof PrimitiveFluidSolver pfs) {
                    pfs.clearForce();
                }
            }

            drawFluid(canvas.getGraphicsContext2D());
            updateParticles();
            zOff += 0.01 * simSpeed;
        }

        public List<javafx.scene.Node> getCustomControls() {
            List<javafx.scene.Node> controls = new ArrayList<>();
            controls.add(new Separator());
            
            // Grid Resolution
            controls.add(new Label(I18n.getInstance().get("fluid.resolution", "Grid Resolution"))); 
            ComboBox<Integer> resCombo = new ComboBox<>();
            resCombo.getItems().addAll(32, 64, 128);
            resCombo.setValue(N);
            resCombo.setOnAction(e -> {
                int newN = resCombo.getValue();
                if (newN != N) {
                    N = newN;
                    solver.initialize(N, SCALE);
                    resetParticles();
                    canvas.setWidth(N * SCALE);
                    canvas.setHeight(N * SCALE);
                }
            });
            controls.add(resCombo);

            // Viscosity
            Label viscLabel = new Label(I18n.getInstance().get("fluid.viscosity", "Viscosity"));
            Slider viscSlider = new Slider(0, 0.05, 0.0001);
            viscSlider.valueProperty().addListener((o, ov, nv) -> viscosity = nv.doubleValue());
            controls.addAll(List.of(viscLabel, viscSlider));

            // Engine Switch
            ToggleButton engineSwitch = new ToggleButton(I18n.getInstance().get("fluid.mode.primitive", "Simple Solver"));
            engineSwitch.setMaxWidth(Double.MAX_VALUE);
            engineSwitch.setOnAction(e -> {
                if (engineSwitch.isSelected()) {
                    solver = new ObjectFluidSolver();
                    engineSwitch.setText(I18n.getInstance().get("fluid.mode.scientific", "Object Solver"));
                } else {
                    solver = new PrimitiveFluidSolver();
                    engineSwitch.setText(I18n.getInstance().get("fluid.mode.primitive", "Simple Solver"));
                }
                solver.initialize(N, SCALE);
            });
            controls.add(engineSwitch);
            
            fpsLabel = new Label("FPS: --");
            controls.add(fpsLabel);

            // Toggles
            CheckBox fieldCheck = new CheckBox(I18n.getInstance().get("fluid.check.field", "Show Flow"));
            fieldCheck.setSelected(showField);
            fieldCheck.setOnAction(e -> { showField = fieldCheck.isSelected(); drawFluid(canvas.getGraphicsContext2D()); });
            
            CheckBox particleCheck = new CheckBox(I18n.getInstance().get("fluid.check.particles", "Show Particles"));
            particleCheck.setSelected(showParticles);
            particleCheck.setOnAction(e -> { showParticles = particleCheck.isSelected(); drawFluid(canvas.getGraphicsContext2D()); });
            
            controls.addAll(List.of(new Separator(), fieldCheck, particleCheck));

            // Color Scheme
            ComboBox<String> colorCombo = new ComboBox<>();
            colorCombo.getItems().addAll("Blue", "Fire", "Green", "Rainbow");
            colorCombo.setValue(colorScheme);
            colorCombo.setOnAction(e -> { colorScheme = colorCombo.getValue(); drawFluid(canvas.getGraphicsContext2D()); });
            controls.add(colorCombo);

            Button resetBtn = new Button(I18n.getInstance().get("fluid.btn.reset", "Reset Particles"));
            resetBtn.setMaxWidth(Double.MAX_VALUE);
            resetBtn.setOnAction(e -> resetParticles());
            controls.add(resetBtn);

            return controls;
        }

        private void resetParticles() {
            particles.clear();
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                particles.add(new Particle(rand.nextDouble() * N * SCALE, rand.nextDouble() * N * SCALE));
            }
        }

        private void drawFluid(GraphicsContext gc) {
            if (showField) {
                for (int y = 0; y < N; y++) {
                    for (int x = 0; x < N; x++) {
                        double[] flow = solver.getFlowAt(x * SCALE, y * SCALE);
                        double mag = Math.sqrt(flow[0] * flow[0] + flow[1] * flow[1]);
                        gc.setFill(getFlowColor(mag));
                        gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                    }
                }
            } else {
                gc.setFill(Color.web("#0a0a15"));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }

            if (showParticles) {
                gc.setFill(Color.WHITE);
                for (Particle p : particles) {
                    double size = 2 + p.life * 2;
                    gc.setFill(Color.hsb(200, 0.5, 0.9, p.life));
                    gc.fillOval(p.x - size / 2, p.y - size / 2, size, size);
                }
            }
            
            gc.setFill(Color.BLACK);
            gc.fillText("Particles: " + particles.size(), 10, 20);
        }

        private Color getFlowColor(double magnitude) {
            double val = Math.min(1.0, magnitude * 2);
            return switch (colorScheme) {
                case "Fire" -> Color.hsb(30 - val * 30, 0.9, 0.2 + val * 0.8);
                case "Green" -> Color.hsb(120, 0.7, 0.1 + val * 0.6);
                case "Rainbow" -> Color.hsb(val * 300, 0.8, 0.3 + val * 0.5);
                default -> Color.color(0, val * 0.4, val * 0.8 + 0.1); 
            };
        }

        private void updateParticles() {
            for (Particle p : particles) {
                double[] flow = solver.getFlowAt(p.x, p.y);
                p.vx = p.vx * 0.9 + flow[0] * simSpeed;
                p.vy = p.vy * 0.9 + flow[1] * simSpeed;
                p.x += p.vx;
                p.y += p.vy;
                p.life -= 0.002;

                if (p.x < 0) p.x = N * SCALE;
                if (p.x > N * SCALE) p.x = 0;
                if (p.y < 0) p.y = N * SCALE;
                if (p.y > N * SCALE) p.y = 0;

                if (p.life <= 0) {
                    p.x = rand.nextDouble() * N * SCALE;
                    p.y = rand.nextDouble() * N * SCALE;
                    p.life = 1.0;
                    p.vx = 0;
                    p.vy = 0;
                }
            }
        }

        // Simulatable Methods
        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; resetParticles(); drawFluid(canvas.getGraphicsContext2D()); }
        @Override public void step() { loop(System.nanoTime()); }
        @Override public void setSpeed(double s) { this.simSpeed = s; }
        @Override public boolean isPlaying() { return running; }
        
        @Override public String getName() { return "Fluid Viewer"; }
        @Override public String getCategory() { return "Physics"; }
    }

    private static class Particle {
        double x, y, vx, vy;
        double life = 1.0;
        Particle(double x, double y) { this.x = x; this.y = y; }
    }
}
