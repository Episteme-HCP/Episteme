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

package org.jscience.ui.viewers.physics.classical.matter.fluids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import org.jscience.ui.i18n.I18n;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enhanced Fluid Dynamics Viewer.
 * Real-time fluid simulation with particle visualization and controls.
 * Uses a simplified Navier-Stokes inspired approach with particles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidDynamicsViewer extends Application {

    private int N = 100;
    private static final int SCALE = 6;
    private static final int PARTICLE_COUNT = 5000;

    private void resetSimulation() {
        if (solver != null) {
            solver.initialize(N, SCALE);
        }
        particles.clear();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * N * SCALE, rand.nextDouble() * N * SCALE));
        }
    }

    private double viscosity = 0.0001;
    private boolean showField = true;
    private boolean showParticles = true;
    private String colorScheme = "Blue";

    private double zOff = 0;
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();

    // Solver
    private FluidSolver solver;
    private long lastFrameTime = 0;
    private int frameCount = 0;
    private Label fpsLabel;

    // Mouse interaction
    private double mouseX = -1, mouseY = -1;
    private boolean mousePressed = false;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        // Removed dark-viewer-root style

        Canvas canvas = new Canvas(N * SCALE, N * SCALE);

        // Initialize particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * N * SCALE, rand.nextDouble() * N * SCALE));
        }

        // Initialize Default Solver
        solver = new PrimitiveFluidSolver();
        stage.setTitle(I18n.getInstance().get("fluid.title"));

        // Controls Panel
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setPrefWidth(220);

        Label titleLabel = new Label(I18n.getInstance().get("fluid.controls"));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Info text
        Label infoLabel = new Label(
                "Visualization of fluid flow patterns.\n\n" +
                        "This demonstrates flow field\n" +
                        "visualization with particle\n" +
                        "advection.\n\n" +
                        "Click and drag to add flow\n" +
                        "disturbance.");
        infoLabel.setWrapText(true);
        // infoLabel.getStyleClass().add("dark-label-muted");

        // Grid Resolution Control
        new Label(I18n.getInstance().get("fluid.resolution")); // Label created but variable not needed
        ComboBox<Integer> resCombo = new ComboBox<>();
        resCombo.getItems().addAll(32, 64, 128);
        resCombo.setValue(N);
        resCombo.setOnAction(e -> {
            int newN = resCombo.getValue();
            if (newN != N) {
                N = newN;
                resetSimulation();
                // Recreate canvas with new size
                canvas.setWidth(N * SCALE);
                canvas.setHeight(N * SCALE);
            }
        });

        // Viscosity slider
        Label viscLabel = new Label(I18n.getInstance().get("fluid.viscosity"));
        // viscLabel.setTextFill(Color.LIGHTGRAY);
        Slider viscSlider = new Slider(0, 0.05, 0.0001); // Increased range for visibility
        viscSlider.setShowTickLabels(true);
        viscSlider.setShowTickMarks(true);
        viscSlider.valueProperty().addListener((o, ov, nv) -> viscosity = nv.doubleValue());

        // FPS Label
        fpsLabel = new Label(I18n.getInstance().get("fluid.fps"));
        // fpsLabel.setTextFill(Color.LIGHTGRAY);
        // fpsLabel.getStyleClass().add("dark-label");

        // Solver Switch
        Label engineLabel = new Label(I18n.getInstance().get("fluid.label.engine"));
        // engineLabel.getStyleClass().add("dark-label");
        ToggleButton engineSwitch = new ToggleButton("Mode: Primitive");
        engineSwitch.setMaxWidth(Double.MAX_VALUE);
        engineSwitch.setOnAction(e -> {
            if (engineSwitch.isSelected()) {
                solver = new ObjectFluidSolver();
                engineSwitch.setText(I18n.getInstance().get("fluid.mode.scientific"));
            } else {
                solver = new PrimitiveFluidSolver();
                engineSwitch.setText(I18n.getInstance().get("fluid.mode.primitive"));
            }
            solver.initialize(N, SCALE);
        });

        // Speed slider
        Label speedLabel = new Label(String.format(I18n.getInstance().get("fluid.label.speed.fmt"), 1.0));
        Slider speedSlider = new Slider(0.1, 3.0, 1.0);
        speedSlider.valueProperty().addListener((o, old, val) -> {
            speedLabel.setText(String.format(I18n.getInstance().get("fluid.label.speed.fmt"), val.doubleValue()));
        });

        // Display toggles
        CheckBox fieldCheck = new CheckBox("Show Flow Field");
        fieldCheck.setSelected(true);
        fieldCheck.setOnAction(e -> showField = fieldCheck.isSelected());

        CheckBox particleCheck = new CheckBox("Show Particles");
        particleCheck.setSelected(true);
        particleCheck.setOnAction(e -> showParticles = particleCheck.isSelected());

        // Color scheme
        Label colorLabel = new Label(I18n.getInstance().get("fluid.label.color"));
        ComboBox<String> colorCombo = new ComboBox<>();
        colorCombo.getItems().addAll("Blue", "Fire", "Green", "Rainbow");
        colorCombo.setValue("Blue");
        colorCombo.setOnAction(e -> colorScheme = colorCombo.getValue());

        // Reset button
        Button resetBtn = new Button(I18n.getInstance().get("fluid.btn.reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> {
            particles.clear();
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                particles.add(new Particle(rand.nextDouble() * N * SCALE, rand.nextDouble() * N * SCALE));
            }
        });

        controls.getChildren().addAll(
                titleLabel, new Separator(), infoLabel, new Separator(),
                viscLabel, viscSlider,
                speedLabel, speedSlider,
                new Separator(),
                engineLabel, engineSwitch, fpsLabel,
                new Separator(),
                fieldCheck, particleCheck,
                colorLabel, colorCombo,
                new Separator(),
                resetBtn);

        StackPane displayStack = new StackPane();
        displayStack.getChildren().add(canvas);

        // Start Button Overlay
        Button startBtn = new Button("CLICK TO START SIMULATION");
        startBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-base: #4CAF50;");
        startBtn.setPadding(new Insets(20));

        displayStack.getChildren().add(startBtn);

        root.setCenter(displayStack);
        root.setRight(controls);

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

        AnimationTimer timer = new AnimationTimer() {
            private double speed = 1.0;

            @Override
            public void handle(long now) {
                speed = speedSlider.getValue();
                speed = speedSlider.getValue();

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

                // Solver Step
                solver.step(speed, viscosity, zOff);

                // Add Force
                if (mousePressed) {
                    solver.addForce(mouseX, mouseY, 0, 0); // Direction not used yet
                } else {
                    // Primitive solver might need manual clear?
                    // Interface addForce is stateful in current impl.
                    // Ideally we pass force in step or explicit method.
                    // For now, let's just update solver state.
                    if (solver instanceof PrimitiveFluidSolver) {
                        ((PrimitiveFluidSolver) solver).clearForce();
                    }
                    // Ideally add clearForce to interface
                }

                drawFluid(canvas, speed);
                updateParticles(speed);
                zOff += 0.01 * speed;
            }
        };

        startBtn.setOnAction(e -> {
            startBtn.setVisible(false);
            timer.start();
        });

        Scene scene = new Scene(root, N * SCALE + 220, N * SCALE);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.fluid"));
        stage.setScene(scene);
        stage.show();
    }

    private void drawFluid(Canvas canvas, double speed) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw flow field
        if (showField) {
            for (int y = 0; y < N; y++) {
                for (int x = 0; x < N; x++) {
                    double[] flow = solver.getFlowAt(x * SCALE, y * SCALE);
                    double mag = Math.sqrt(flow[0] * flow[0] + flow[1] * flow[1]);
                    Color c = getFlowColor(mag);
                    gc.setFill(c);
                    gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                }
            }
        } else {
            gc.setFill(Color.web("#0a0a15"));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        // Draw particles
        if (showParticles) {
            gc.setFill(Color.WHITE);
            for (Particle p : particles) {
                double size = 2 + p.life * 2;
                gc.setFill(Color.hsb(200, 0.5, 0.9, p.life));
                gc.fillOval(p.x - size / 2, p.y - size / 2, size, size);
            }
        }

        // Draw mouse interaction effect
        if (mousePressed && mouseX > 0) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeOval(mouseX - 20, mouseY - 20, 40, 40);
        }

        // Labels
        gc.setFill(Color.BLACK); // Changed to BLACK to be visible on non-dark background if flow is light
        // gc.fillText("Particles: " + particles.size(), 10, 20); // Removed as
        // redundant with sidebar? Or Keep? Keep.
        gc.fillText("Particles: " + particles.size(), 10, 20);
    }

    private Color getFlowColor(double magnitude) {
        double val = Math.min(1.0, magnitude * 2);
        return switch (colorScheme) {
            case "Fire" -> Color.hsb(30 - val * 30, 0.9, 0.2 + val * 0.8);
            case "Green" -> Color.hsb(120, 0.7, 0.1 + val * 0.6);
            case "Rainbow" -> Color.hsb(val * 300, 0.8, 0.3 + val * 0.5);
            default -> Color.color(0, val * 0.4, val * 0.8 + 0.1); // Blue
        };
    }

    private void updateParticles(double speed) {
        for (Particle p : particles) {
            double[] flow = solver.getFlowAt(p.x, p.y);
            p.vx = p.vx * 0.9 + flow[0] * speed;
            p.vy = p.vy * 0.9 + flow[1] * speed;
            p.x += p.vx;
            p.y += p.vy;
            p.life -= 0.002;

            // Wrap around
            if (p.x < 0)
                p.x = N * SCALE;
            if (p.x > N * SCALE)
                p.x = 0;
            if (p.y < 0)
                p.y = N * SCALE;
            if (p.y > N * SCALE)
                p.y = 0;

            // Respawn dead particles
            if (p.life <= 0) {
                p.x = rand.nextDouble() * N * SCALE;
                p.y = rand.nextDouble() * N * SCALE;
                p.life = 1.0;
                p.vx = 0;
                p.vy = 0;
            }
        }
    }

    private static class Particle {
        double x, y, vx, vy;
        double life = 1.0;

        Particle(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void show(Stage stage) {
        new FluidDynamicsViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


