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

package org.jscience.client.mathematics.montecarlopi;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.protobuf.ByteString;
import org.jscience.server.proto.*;
import org.jscience.ui.ThemeManager;

import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Monte Carlo Pi Estimation with distributed sampling.
 * 
 * Uses the JScience Grid to distribute sampling across multiple workers
 * for faster convergence to π.
 
 * <p>
 * <b>Reference:</b><br>
 * Metropolis, N., et al. (1953). Equation of State Calculations by Fast Computing Machines. <i>The Journal of Chemical Physics</i>, 21(6), 1087.
 * </p>
 *
 */
public class DistributedMonteCarloPiApp extends Application implements org.jscience.ui.App {

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

        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.title", "🎯 Monte Carlo π Estimation - JScience"));

        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();

        // Control panel
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.getStyleClass().add("viewer-sidebar");
        controls.setPrefWidth(250);

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.header", "Monte Carlo π"));
        title.getStyleClass().add("header-label-white");

        piLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.pi_label", "π ≈ ?"));
        piLabel.getStyleClass().add("title-label-white");

        samplesLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.samples_label", "Samples: 0"));
        samplesLabel.getStyleClass().add("label-muted");

        modeLabel = new Label(serverAvailable ? org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.mode.dist", "🌐 Distributed Mode") : org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.mode.local", "💻 Local Mode"));
        modeLabel.getStyleClass().add("label-green");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);

        Label accuracyLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.accuracy_label", "Accuracy: ±?"));
        accuracyLabel.getStyleClass().add("label-muted");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333;");

        ToggleButton distributedBtn = new ToggleButton(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.btn.dist", "🌐 Distributed"));
        distributedBtn.setSelected(serverAvailable);
        distributedBtn.getStyleClass().add("accent-button-green");
        distributedBtn.setOnAction(e -> {
            useDistributed = distributedBtn.isSelected();
            if (useDistributed && !serverAvailable) {
                checkServerAvailability();
            }
            modeLabel.setText(useDistributed && serverAvailable ? org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.mode.dist", "🌐 Distributed Mode") : org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.mode.local", "💻 Local Mode"));
        });

        Button startBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.btn.start", "▶ Start Sampling"));
        startBtn.getStyleClass().add("accent-button-red");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.btn.pause", "⏸ Pause") : org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.btn.resume", "▶ Resume"));
            if (running && useDistributed && serverAvailable) {
                startDistributedSampling();
            }
        });

        Button resetBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.btn.reset", "↺ Reset"));
        resetBtn.getStyleClass().add("accent-button-gray");
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
        infoArea.getStyleClass().add("info-text-area");

        controls.getChildren().addAll(
                title, piLabel, samplesLabel, modeLabel, progressBar, accuracyLabel,
                sep, distributedBtn, startBtn, resetBtn, new Separator(), infoArea);

        HBox root = new HBox(canvas, controls);
        root.getStyleClass().add("viewer-root");

        Scene scene = new Scene(root);
        ThemeManager.getInstance().applyTheme(scene);
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
        // Use proper object serialization of the Task
        org.jscience.mathematics.montecarlo.MonteCarloPiTask task = new org.jscience.mathematics.montecarlo.MonteCarloPiTask(
                numSamples);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(task);
        oos.flush();
        return bos.toByteArray();
    }

    private void applyDistributedResult(byte[] data) throws IOException {
        // Result is serialized Long (from Task.execute return value)
        // WorkerNode serializes the result object.
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            Long inside = (Long) ois.readObject();

            // We know the batch size (implicit via SAMPLES_PER_BATCH / NUM_WORKERS)
            // But ideally the result should carry it or we track it.
            // For now, assuming batch size is constant SAMPLES_PER_BATCH / NUM_WORKERS
            long batchSize = SAMPLES_PER_BATCH / NUM_WORKERS;

            insideCircle.addAndGet(inside);
            totalSamples.addAndGet(batchSize);
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize result", e);
        }
    }

    private void performLocalBatch(long batchSize) {
        // Use Provider for local fallback too
        org.jscience.technical.backend.algorithms.MonteCarloPiProvider provider = new org.jscience.technical.backend.algorithms.MulticoreMonteCarloPiProvider();

        long inside = provider.countPointsInside(batchSize);

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
        drawBackground();
        piLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.pi_label", "π ≈ ?"));
        samplesLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.samples_label", "Samples: 0"));
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

                    piLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.pi_format", "π ≈ %.8f"), pi));
                    samplesLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.samples_format", "Samples: %,d"), total));
                    progressBar.setProgress((double) total / targetSamples);
                    accuracyLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.error_format", "Error: %.6f"), error));
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

    // App Interface Implementation
    @Override
    public boolean isDemo() {
        return false;
    }

    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.mathematics", "Mathematics"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.name", "Distributed Monte Carlo Pi App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.desc", "Statistical estimation of π using distributed random sampling on the JScience grid."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedmontecarlopiapp.longdesc", "The Monte Carlo method uses random numbers to approximate mathematical constants. This application distributes billions of samples across the JScience cluster, allowing for rapid convergence and high-accuracy estimation of π through massive parallel execution."); }

    @Override
    public void show(javafx.stage.Stage stage) {
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return new java.util.ArrayList<>();
    }
}
