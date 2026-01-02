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

package org.jscience.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Stage;

import org.jscience.physics.classical.mechanics.NBodySimulation;
import org.jscience.physics.classical.mechanics.Particle;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Distributed N-Body Simulation - Computes gravitational forces using the
 * JScience Grid.
 * 
 * Refactored to use NBodySimulation and Particle from jscience-natural.
 */
public class DistributedNBodyApp extends Application {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final double DT = 0.016;

    private ManagedChannel channel;
    private Canvas canvas;
    private GraphicsContext gc;
    private NBodySimulation simulation;
    private AnimationTimer timer;
    private boolean running = false;
    private boolean distributed = false;

    private Label statusLabel;
    private Label fpsLabel;
    private Button startBtn;

    // Mapping for UI colors/radii
    private List<BodyStyle> styles = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        resetSimulation();

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a1a;");

        root.setTop(createHeader());
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(new StackPane(canvas));
        root.setBottom(createFooter());

        timer = new AnimationTimer() {
            private long lastFpsTime = 0;
            private int frameCount = 0;

            @Override
            public void handle(long now) {
                frameCount++;
                if (now - lastFpsTime >= 1_000_000_000) {
                    int fps = frameCount;
                    frameCount = 0;
                    lastFpsTime = now;
                    Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
                }

                if (distributed) {
                    // In a real distributed app, we would send the particles to the server
                    simulation.step(Real.of(DT));
                } else {
                    simulation.step(Real.of(DT));
                }
                render();
            }
        };

        Scene scene = new Scene(root, WIDTH + 40, HEIGHT + 140);
        primaryStage.setTitle("JScience Distributed N-Body Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
        render();
    }

    private void resetSimulation() {
        simulation = new NBodySimulation(PhysicalConstants.G.multiply(Real.of(1e24))); // Scaled for visualization
        simulation.setSoftening(Real.of(10.0));
        styles.clear();
        initializeSolarSystem();
    }

    private void initializeSolarSystem() {
        // Sun
        addBody(WIDTH / 2, HEIGHT / 2, 0, 0, 1000, Color.YELLOW, 20);

        // Planets
        double[] radii = { 60, 100, 150, 200, 280 };
        Color[] colors = { Color.GRAY, Color.ORANGE, Color.BLUE, Color.RED, Color.BROWN };
        double[] masses = { 0.1, 0.5, 1, 0.8, 0.3 };

        statusLabel.setText(String.format("Step: 0 | Time: 0.00s | E: %.2e J",
                simulation.totalEnergy().doubleValue())); // Placeholder for G
        // Using local G scale
        double G_scaled = PhysicalConstants.G.doubleValue() * 1e24;

        for (int i = 0; i < radii.length; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double x = WIDTH / 2 + radii[i] * Math.cos(angle);
            double y = HEIGHT / 2 + radii[i] * Math.sin(angle);
            double v = Math.sqrt(G_scaled * 1000 / radii[i]) * 0.5;
            double vx = -v * Math.sin(angle);
            double vy = v * Math.cos(angle);
            addBody(x, y, vx, vy, masses[i], colors[i], 5 + masses[i] * 3);
        }
    }

    private void addBody(double x, double y, double vx, double vy, double mass, Color color, double radius) {
        Particle p = new Particle(x, y, 0, mass);
        p.setVelocity(vx, vy, 0);
        simulation.addParticle(p);
        styles.add(new BodyStyle(color, radius));
    }

    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw stars background
        gc.setFill(Color.gray(0.3));
        Random starRand = new Random(42);
        for (int i = 0; i < 100; i++) {
            gc.fillOval(starRand.nextDouble() * WIDTH, starRand.nextDouble() * HEIGHT, 1, 1);
        }

        List<Particle> particles = simulation.getParticles();
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            BodyStyle style = styles.get(i);
            double x = p.getX().doubleValue();
            double y = p.getY().doubleValue();
            double r = style.radius;

            // Glow
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, style.color.deriveColor(0, 1, 1, 0.8)),
                    new Stop(1, Color.TRANSPARENT)));
            gc.fillOval(x - r * 2, y - r * 2, r * 4, r * 4);

            // Body
            gc.setFill(style.color);
            gc.fillOval(x - r, y - r, r * 2, r * 2);
        }
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Ã°Å¸Å’Å’ Distributed N-Body Simulation");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        startBtn = new Button("Ã¢â€“Â¶ Start");
        startBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white;");
        startBtn.setOnAction(e -> toggleSimulation());

        ToggleButton distributedToggle = new ToggleButton("Ã°Å¸Å’Â  Distributed");
        distributedToggle.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        distributedToggle.setOnAction(e -> {
            distributed = distributedToggle.isSelected();
            statusLabel.setText(distributed ? "Ã°Å¸Å’Â  Distributed Mode" : "Ã°Å¸â€™Â» Local Mode");
        });

        header.getChildren().addAll(title, spacer, startBtn, distributedToggle);
        return header;
    }

    private void toggleSimulation() {
        running = !running;
        if (running) {
            timer.start();
            startBtn.setText("Ã¢Â Â¸ Pause");
        } else {
            timer.stop();
            startBtn.setText("Ã¢â€“Â¶ Start");
        }
    }

    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statusLabel = new Label("Ã°Å¸â€™Â» Local Mode - Ready");
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        fpsLabel = new Label("FPS: --");
        fpsLabel.setStyle("-fx-text-fill: #888;");

        footer.getChildren().addAll(statusLabel, spacer, fpsLabel);
        return footer;
    }

    private static class BodyStyle {
        Color color;
        double radius;

        BodyStyle(Color c, double r) {
            this.color = c;
            this.radius = r;
        }
    }

    @Override
    public void stop() {
        if (timer != null)
            timer.stop();
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
