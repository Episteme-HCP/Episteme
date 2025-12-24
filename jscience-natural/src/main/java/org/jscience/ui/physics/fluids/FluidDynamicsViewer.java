package org.jscience.ui.physics.fluids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    private static final int N = 100;
    private static final int SCALE = 6;
    private static final int PARTICLE_COUNT = 500;

    private double viscosity = 0.0001;
    private boolean showField = true;
    private boolean showParticles = true;
    private String colorScheme = "Blue";

    private double zOff = 0;
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();

    // Mouse interaction
    private double mouseX = -1, mouseY = -1;
    private boolean mousePressed = false;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        Canvas canvas = new Canvas(N * SCALE, N * SCALE);

        // Initialize particles
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * N * SCALE, rand.nextDouble() * N * SCALE));
        }

        // Controls Panel
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #0f0f1a;");
        controls.setPrefWidth(220);

        Label titleLabel = new Label("Fluid Dynamics");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00d4ff;");

        // Info text
        Label infoLabel = new Label(
                "Visualization of fluid flow patterns.\n\n" +
                        "This demonstrates flow field\n" +
                        "visualization with particle\n" +
                        "advection.\n\n" +
                        "Click and drag to add flow\n" +
                        "disturbance.");
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #aaa;");

        // Viscosity slider
        Label viscLabel = new Label("Viscosity: 0.01");
        viscLabel.setStyle("-fx-text-fill: white;");
        Slider viscSlider = new Slider(0.001, 0.1, 0.01);
        viscSlider.valueProperty().addListener((o, old, val) -> {
            viscosity = val.doubleValue();
            viscLabel.setText(String.format("Viscosity: %.3f", viscosity));
        });

        // Speed slider
        Label speedLabel = new Label("Flow Speed: 1.0");
        speedLabel.setStyle("-fx-text-fill: white;");
        Slider speedSlider = new Slider(0.1, 3.0, 1.0);
        speedSlider.valueProperty().addListener((o, old, val) -> {
            speedLabel.setText(String.format("Flow Speed: %.1f", val.doubleValue()));
        });

        // Display toggles
        CheckBox fieldCheck = new CheckBox("Show Flow Field");
        fieldCheck.setSelected(true);
        fieldCheck.setStyle("-fx-text-fill: white;");
        fieldCheck.setOnAction(e -> showField = fieldCheck.isSelected());

        CheckBox particleCheck = new CheckBox("Show Particles");
        particleCheck.setSelected(true);
        particleCheck.setStyle("-fx-text-fill: white;");
        particleCheck.setOnAction(e -> showParticles = particleCheck.isSelected());

        // Color scheme
        Label colorLabel = new Label("Color Scheme:");
        colorLabel.setStyle("-fx-text-fill: white;");
        ComboBox<String> colorCombo = new ComboBox<>();
        colorCombo.getItems().addAll("Blue", "Fire", "Green", "Rainbow");
        colorCombo.setValue("Blue");
        colorCombo.setOnAction(e -> colorScheme = colorCombo.getValue());

        // Reset button
        Button resetBtn = new Button("Reset Particles");
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
                fieldCheck, particleCheck,
                colorLabel, colorCombo,
                new Separator(),
                resetBtn);

        root.setCenter(canvas);
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

        new AnimationTimer() {
            private double speed = 1.0;

            @Override
            public void handle(long now) {
                speed = speedSlider.getValue();
                drawFluid(canvas, speed);
                updateParticles(speed);
                zOff += 0.01 * speed;
            }
        }.start();

        Scene scene = new Scene(root, N * SCALE + 220, N * SCALE);
        stage.setTitle("JScience Fluid Dynamics Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void drawFluid(Canvas canvas, double speed) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw flow field
        if (showField) {
            for (int y = 0; y < N; y++) {
                for (int x = 0; x < N; x++) {
                    double[] flow = getFlowAt(x * SCALE, y * SCALE);
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
        gc.setFill(Color.WHITE);
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

    private double[] getFlowAt(double px, double py) {
        // Simplified flow field based on simplex-like noise
        double x = px / SCALE;
        double y = py / SCALE;

        double vx = Math.sin(x * 0.1 + zOff) * Math.cos(y * 0.08 + zOff * 0.7);
        double vy = Math.cos(x * 0.08 + zOff * 0.6) * Math.sin(y * 0.1 + zOff);

        // Add mouse influence
        if (mousePressed && mouseX > 0) {
            double dx = px - mouseX;
            double dy = py - mouseY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 100) {
                double influence = (100 - dist) / 100.0;
                vx += influence * 2 * Math.signum(-dx);
                vy += influence * 2 * Math.signum(-dy);
            }
        }

        // Dampen by viscosity
        vx *= (1.0 - viscosity);
        vy *= (1.0 - viscosity);

        return new double[] { vx, vy };
    }

    private void updateParticles(double speed) {
        for (Particle p : particles) {
            double[] flow = getFlowAt(p.x, p.y);
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
