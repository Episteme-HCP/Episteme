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

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jscience.server.proto.*;

import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Monte Carlo Pi Estimation with distributed sampling.
 * 
 * Uses the JScience Grid to distribute sampling across multiple workers
 * for faster convergence to π.
 */
public class MonteCarloPiApp extends Application {

    private static final int NUM_WORKERS = 4; // Distribute across 4 tasks
    private static final long SAMPLES_PER_BATCH = 1_000_000;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub computeStub;
    private boolean serverAvailable = false;

    private Canvas canvas;
    private GraphicsContext gc;
    private Label piLabel;
    private Label samplesLabel;
    private Label modeLabel;
    private ProgressBar progressBar;

    private AtomicLong insideCircle = new AtomicLong(0);
    private AtomicLong totalSamples = new AtomicLong(0);
    private final long targetSamples = 10_000_000;
    private boolean running = false;
    private boolean useDistributed = true;

    private final java.util.Random random = new java.util.Random();
    private ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);

    @Override
    public void start(Stage stage) {
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        computeStub = ComputeServiceGrpc.newBlockingStub(channel);

        checkServerAvailability();

        stage.setTitle("🎯 Monte Carlo π Estimation - JScience");

        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();

        // Control panel
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(250);

        Label title = new Label("Monte Carlo π");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        piLabel = new Label("π ≈ ?");
        piLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #eee;");

        samplesLabel = new Label("Samples: 0");
        samplesLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");

        modeLabel = new Label(serverAvailable ? "🌐 Distributed Mode" : "💻 Local Mode");
        modeLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #4ecca3;");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);

        Label accuracyLabel = new Label("Accuracy: ±?");
        accuracyLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333;");

        ToggleButton distributedBtn = new ToggleButton("🌐 Distributed");
        distributedBtn.setSelected(serverAvailable);
        distributedBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; " +
                "-fx-font-size: 12; -fx-padding: 8 15;");
        distributedBtn.setOnAction(e -> {
            useDistributed = distributedBtn.isSelected();
            if (useDistributed && !serverAvailable) {
                checkServerAvailability();
            }
            modeLabel.setText(useDistributed && serverAvailable ? "🌐 Distributed Mode" : "💻 Local Mode");
        });

        Button startBtn = new Button("▶ Start Sampling");
        startBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-padding: 10 20;");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? "⏸ Pause" : "▶ Resume");
            if (running && useDistributed && serverAvailable) {
                startDistributedSampling();
            }
        });

        Button resetBtn = new Button("↺ Reset");
        resetBtn.setStyle("-fx-background-color: #333; -fx-text-fill: white; " +
                "-fx-font-size: 14; -fx-padding: 10 20;");
        resetBtn.setOnAction(e -> reset());

        // Info section
        TextArea infoArea = new TextArea("""
                Monte Carlo Method:

                1. Random points in unit square
                2. Count points inside circle
                3. π ≈ 4 × (inside / total)

                Distributed mode sends batches
                to multiple workers for parallel
                sampling!
                """);
        infoArea.setEditable(false);
        infoArea.setWrapText(true);
        infoArea.setPrefRowCount(8);
        infoArea.setStyle("-fx-control-inner-background: #16213e; -fx-text-fill: #aaa;");

        controls.getChildren().addAll(
                title, piLabel, samplesLabel, modeLabel, progressBar, accuracyLabel,
                sep, distributedBtn, startBtn, resetBtn, new Separator(), infoArea);

        HBox root = new HBox(canvas, controls);
        root.setStyle("-fx-background-color: #0f0f1a;");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        drawBackground();
        startAnimation(accuracyLabel);
    }

    private void checkServerAvailability() {
        try {
            ServerStatus status = computeStub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getStatus(Empty.newBuilder().build());
            serverAvailable = status.getActiveWorkers() > 0;
        } catch (Exception e) {
            serverAvailable = false;
        }
    }

    private void startDistributedSampling() {
        // Submit sampling tasks to workers
        for (int i = 0; i < NUM_WORKERS; i++) {
            final int workerId = i;
            executor.submit(() -> submitDistributedBatch(workerId));
        }
    }

    private void submitDistributedBatch(int workerId) {
        while (running && totalSamples.get() < targetSamples) {
            try {
                // Create batch task
                byte[] taskData = serializeSamplingTask(SAMPLES_PER_BATCH / NUM_WORKERS);
                String taskId = "montecarlo-" + workerId + "-" + System.currentTimeMillis();

                TaskRequest request = TaskRequest.newBuilder()
                        .setTaskId(taskId)
                        .setSerializedTask(ByteString.copyFrom(taskData))
                        .setPriority(org.jscience.server.proto.Priority.HIGH)
                        .setTimestamp(System.currentTimeMillis())
                        .build();

                TaskResponse response = computeStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .submitTask(request);

                if (response.getStatus() == Status.QUEUED) {
                    // Try to get result
                    TaskResult result = computeStub.withDeadlineAfter(10, TimeUnit.SECONDS)
                            .streamResults(TaskIdentifier.newBuilder().setTaskId(taskId).build())
                            .next();

                    if (result.getStatus() == Status.COMPLETED) {
                        applyDistributedResult(result.getSerializedData().toByteArray());
                        continue;
                    }
                }

                // Fallback to local
                performLocalBatch(SAMPLES_PER_BATCH / NUM_WORKERS);

            } catch (Exception e) {
                // Fallback to local on error
                performLocalBatch(SAMPLES_PER_BATCH / NUM_WORKERS);
            }
        }
    }

    private byte[] serializeSamplingTask(long numSamples) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("MONTECARLO_PI");
        dos.writeLong(numSamples);
        dos.flush();
        return bos.toByteArray();
    }

    private void applyDistributedResult(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        long inside = dis.readLong();
        long total = dis.readLong();

        insideCircle.addAndGet(inside);
        totalSamples.addAndGet(total);
    }

    private void performLocalBatch(long batchSize) {
        long inside = 0;
        for (long i = 0; i < batchSize && totalSamples.get() < targetSamples; i++) {
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            if (x * x + y * y <= 1)
                inside++;
        }
        insideCircle.addAndGet(inside);
        totalSamples.addAndGet(batchSize);
    }

    private void drawBackground() {
        gc.setFill(Color.web("#16213e"));
        gc.fillRect(0, 0, 600, 600);

        // Draw circle
        gc.setStroke(Color.web("#e94560"));
        gc.setLineWidth(2);
        gc.strokeOval(0, 0, 600, 600);

        // Draw axes
        gc.setStroke(Color.web("#333"));
        gc.setLineWidth(1);
        gc.strokeLine(300, 0, 300, 600);
        gc.strokeLine(0, 300, 600, 300);
    }

    private void reset() {
        insideCircle.set(0);
        totalSamples.set(0);
        running = false;
        drawBackground();
        piLabel.setText("π ≈ ?");
        samplesLabel.setText("Samples: 0");
        progressBar.setProgress(0);
    }

    private void startAnimation(Label accuracyLabel) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running)
                    return;

                // Local sampling for visualization
                if (!useDistributed || !serverAvailable) {
                    int batchSize = 1000;
                    for (int i = 0; i < batchSize && totalSamples.get() < targetSamples; i++) {
                        double x = random.nextDouble() * 2 - 1;
                        double y = random.nextDouble() * 2 - 1;

                        boolean inside = x * x + y * y <= 1;
                        if (inside)
                            insideCircle.incrementAndGet();
                        totalSamples.incrementAndGet();

                        // Draw point (scaled to canvas)
                        double px = (x + 1) * 300;
                        double py = (y + 1) * 300;
                        gc.setFill(inside ? Color.web("#4ecca3", 0.7) : Color.web("#e94560", 0.5));
                        gc.fillOval(px - 1, py - 1, 2, 2);
                    }
                }

                // Update UI
                long total = totalSamples.get();
                long inside = insideCircle.get();
                if (total > 0) {
                    double pi = 4.0 * inside / total;
                    double error = Math.abs(pi - Math.PI);

                    piLabel.setText(String.format("π ≈ %.8f", pi));
                    samplesLabel.setText(String.format("Samples: %,d", total));
                    progressBar.setProgress((double) total / targetSamples);
                    accuracyLabel.setText(String.format("Error: %.6f", error));
                }

                if (total >= targetSamples) {
                    running = false;
                }
            }
        };
        timer.start();
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdown();
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
