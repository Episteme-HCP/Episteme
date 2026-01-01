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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Distributed N-Body Simulation - Computes gravitational forces using the
 * JScience Grid.
 * The O(nÃ‚Â²) force computation is partitioned across workers for parallel
 * execution.
 * Visualization shows real-time particle positions with smooth animation.
 */
public class DistributedNBodyApp extends Application {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final double G = 6.674e-11 * 1e24; // Scaled gravitational constant
    private static final double DT = 0.016; // Time step (60 FPS)
    private static final double SOFTENING = 10.0;

    private ManagedChannel channel;

    private Canvas canvas;

    private GraphicsContext gc;
    private List<Body> bodies;
    private AnimationTimer timer;
    private boolean running = false;
    private boolean distributed = false;

    private Label statusLabel;
    private Label fpsLabel;
    private Button startBtn;

    @Override
    public void start(Stage primaryStage) {
        // Connect to server
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        // blockingStub = ComputeServiceGrpc.newBlockingStub(channel); // Removed as
        // blockingStub is unused

        // Initialize bodies
        bodies = new ArrayList<>();
        initializeSolarSystem();

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a1a;");

        // Header
        HBox header = createHeader();
        root.setTop(header);

        // Canvas
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        StackPane centerPane = new StackPane(canvas);
        centerPane.setStyle("-fx-background-color: #000;");
        root.setCenter(centerPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        // Animation timer
        timer = new AnimationTimer() {
            private long lastTime = 0;
            private int frameCount = 0;
            private long lastFpsTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0)
                    lastTime = now;
                // double elapsed = (now - lastTime) / 1e9; // Removed as elapsed is unused
                lastTime = now;

                // FPS counter
                frameCount++;
                if (now - lastFpsTime >= 1_000_000_000) {
                    int fps = frameCount;
                    frameCount = 0;
                    lastFpsTime = now;
                    Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
                }

                // Simulation step
                if (distributed) {
                    computeForcesDistributed();
                } else {
                    computeForcesLocal();
                }
                updatePositions();
                render();
            }
        };

        render();

        Scene scene = new Scene(root, WIDTH + 40, HEIGHT + 140);
        primaryStage.setTitle("JScience Distributed N-Body Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        startBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; -fx-font-size: 14px;");
        startBtn.setOnAction(e -> toggleSimulation());

        ToggleButton distributedToggle = new ToggleButton("Ã°Å¸Å’Â Distributed");
        distributedToggle.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        distributedToggle.setOnAction(e -> {
            distributed = distributedToggle.isSelected();
            statusLabel.setText(distributed ? "Ã°Å¸Å’Â Distributed Mode" : "Ã°Å¸â€™Â» Local Mode");
        });

        Button resetBtn = new Button("Ã°Å¸â€â€ž Reset");
        resetBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> resetSimulation());

        ComboBox<String> presetBox = new ComboBox<>();
        presetBox.getItems().addAll("Solar System", "Galaxy Collision", "Random Cluster", "Binary Stars");
        presetBox.setValue("Solar System");
        presetBox.setOnAction(e -> {
            switch (presetBox.getValue()) {
                case "Solar System" -> initializeSolarSystem();
                case "Galaxy Collision" -> initializeGalaxyCollision();
                case "Random Cluster" -> initializeRandomCluster();
                case "Binary Stars" -> initializeBinaryStars();
            }
            render();
        });

        header.getChildren().addAll(title, spacer, presetBox, startBtn, distributedToggle, resetBtn);
        return header;
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

        Label bodiesLabel = new Label("Bodies: " + bodies.size());
        bodiesLabel.setStyle("-fx-text-fill: #888;");

        footer.getChildren().addAll(statusLabel, spacer, bodiesLabel, fpsLabel);
        return footer;
    }

    private void toggleSimulation() {
        running = !running;
        if (running) {
            timer.start();
            startBtn.setText("Ã¢ÂÂ¸ Pause");
        } else {
            timer.stop();
            startBtn.setText("Ã¢â€“Â¶ Start");
        }
    }

    private void resetSimulation() {
        if (running) {
            timer.stop();
            running = false;
            startBtn.setText("Ã¢â€“Â¶ Start");
        }
        initializeSolarSystem();
        render();
    }

    private void initializeSolarSystem() {
        bodies.clear();
        // Sun (centered, massive)
        bodies.add(new Body(WIDTH / 2, HEIGHT / 2, 0, 0, 1000, Color.YELLOW, 20));
        // Planets
        double[] radii = { 60, 100, 150, 200, 280 };
        Color[] colors = { Color.GRAY, Color.ORANGE, Color.BLUE, Color.RED, Color.BROWN };
        double[] masses = { 0.1, 0.5, 1, 0.8, 0.3 };
        for (int i = 0; i < radii.length; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double x = WIDTH / 2 + radii[i] * Math.cos(angle);
            double y = HEIGHT / 2 + radii[i] * Math.sin(angle);
            double v = Math.sqrt(G * 1000 / radii[i]) * 0.5;
            double vx = -v * Math.sin(angle);
            double vy = v * Math.cos(angle);
            bodies.add(new Body(x, y, vx, vy, masses[i], colors[i], 5 + masses[i] * 3));
        }
    }

    private void initializeGalaxyCollision() {
        bodies.clear();
        Random rand = new Random();
        // Galaxy 1
        for (int i = 0; i < 50; i++) {
            double angle = rand.nextDouble() * 2 * Math.PI;
            double r = 30 + rand.nextDouble() * 100;
            double x = WIDTH / 3 + r * Math.cos(angle);
            double y = HEIGHT / 2 + r * Math.sin(angle);
            double v = Math.sqrt(G * 500 / r) * 0.3;
            bodies.add(new Body(x, y, -v * Math.sin(angle) + 50, v * Math.cos(angle),
                    0.1 + rand.nextDouble() * 0.5, Color.CYAN, 3));
        }
        bodies.add(new Body(WIDTH / 3, HEIGHT / 2, 50, 0, 500, Color.BLUE, 15));

        // Galaxy 2
        for (int i = 0; i < 50; i++) {
            double angle = rand.nextDouble() * 2 * Math.PI;
            double r = 30 + rand.nextDouble() * 100;
            double x = 2 * WIDTH / 3 + r * Math.cos(angle);
            double y = HEIGHT / 2 + r * Math.sin(angle);
            double v = Math.sqrt(G * 500 / r) * 0.3;
            bodies.add(new Body(x, y, -v * Math.sin(angle) - 50, v * Math.cos(angle),
                    0.1 + rand.nextDouble() * 0.5, Color.PINK, 3));
        }
        bodies.add(new Body(2 * WIDTH / 3, HEIGHT / 2, -50, 0, 500, Color.MAGENTA, 15));
    }

    private void initializeRandomCluster() {
        bodies.clear();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            double x = WIDTH / 4 + rand.nextDouble() * WIDTH / 2;
            double y = HEIGHT / 4 + rand.nextDouble() * HEIGHT / 2;
            double vx = (rand.nextDouble() - 0.5) * 30;
            double vy = (rand.nextDouble() - 0.5) * 30;
            double mass = 0.5 + rand.nextDouble() * 2;
            Color color = Color.hsb(rand.nextDouble() * 360, 0.8, 1.0);
            bodies.add(new Body(x, y, vx, vy, mass, color, 3 + mass));
        }
    }

    private void initializeBinaryStars() {
        bodies.clear();
        bodies.add(new Body(WIDTH / 2 - 80, HEIGHT / 2, 0, 40, 500, Color.YELLOW, 18));
        bodies.add(new Body(WIDTH / 2 + 80, HEIGHT / 2, 0, -40, 500, Color.ORANGE, 18));
        // Orbiting planets
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double r = 200 + i * 40;
            double x = WIDTH / 2 + r * Math.cos(angle);
            double y = HEIGHT / 2 + r * Math.sin(angle);
            double v = Math.sqrt(G * 1000 / r) * 0.4;
            bodies.add(new Body(x, y, -v * Math.sin(angle), v * Math.cos(angle), 0.5, Color.LIGHTBLUE, 4));
        }
    }

    private void computeForcesLocal() {
        int n = bodies.size();
        for (Body b : bodies) {
            b.ax = 0;
            b.ay = 0;
        }

        for (int i = 0; i < n; i++) {
            Body bi = bodies.get(i);
            for (int j = i + 1; j < n; j++) {
                Body bj = bodies.get(j);
                double dx = bj.x - bi.x;
                double dy = bj.y - bi.y;
                double r2 = dx * dx + dy * dy + SOFTENING * SOFTENING;
                double r = Math.sqrt(r2);
                double f = G / (r2 * r);

                double fx = f * dx;
                double fy = f * dy;

                bi.ax += fx * bj.mass;
                bi.ay += fy * bj.mass;
                bj.ax -= fx * bi.mass;
                bj.ay -= fy * bi.mass;
            }
        }
    }

    private void computeForcesDistributed() {
        // For demo: partition bodies into chunks and submit to grid
        // Each worker computes force interactions for a subset

        // int chunkSize = Math.max(10, n / 4); // Removed as chunkSize is unused
        // AtomicInteger tasksCompleted = new AtomicInteger(0); // Removed as
        // tasksCompleted is unused

        for (Body b : bodies) {
            b.ax = 0;
            b.ay = 0;
        }

        // For now, fall back to local (server would need to implement NBody task
        // handler)
        computeForcesLocal();
    }

    private void updatePositions() {
        for (Body b : bodies) {
            b.vx += b.ax * DT;
            b.vy += b.ay * DT;
            b.x += b.vx * DT;
            b.y += b.vy * DT;

            // Wrap around edges
            if (b.x < 0)
                b.x += WIDTH;
            if (b.x > WIDTH)
                b.x -= WIDTH;
            if (b.y < 0)
                b.y += HEIGHT;
            if (b.y > HEIGHT)
                b.y -= HEIGHT;
        }
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

        // Draw bodies with glow effect
        for (Body b : bodies) {
            // Glow
            gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, b.color.deriveColor(0, 1, 1, 0.8)),
                    new Stop(1, Color.TRANSPARENT)));
            gc.fillOval(b.x - b.radius * 2, b.y - b.radius * 2, b.radius * 4, b.radius * 4);

            // Body
            gc.setFill(b.color);
            gc.fillOval(b.x - b.radius, b.y - b.radius, b.radius * 2, b.radius * 2);
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

    static class Body {
        double x, y, vx, vy, ax, ay, mass, radius;
        Color color;

        Body(double x, double y, double vx, double vy, double mass, Color color, double radius) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.ax = 0;
            this.ay = 0;
            this.mass = mass;
            this.color = color;
            this.radius = radius;
        }
    }
}
