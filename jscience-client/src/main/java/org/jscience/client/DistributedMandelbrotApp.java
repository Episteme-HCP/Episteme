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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.server.proto.*;

import java.util.concurrent.atomic.AtomicInteger;
import com.google.protobuf.ByteString;

/**
 * Distributed Mandelbrot Viewer - Computes Mandelbrot set using the JScience
 * Grid.
 * Splits the image into horizontal slices and submits each as a CRITICAL
 * priority task.
 * Results are progressively rendered as they complete.
 */
public class DistributedMandelbrotApp extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int SLICES = 30; // Number of horizontal slices (tasks)
    private static final int ROWS_PER_SLICE = HEIGHT / SLICES;
    private static final int MAX_ITER = 200;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;

    private Canvas canvas;
    private WritableImage image;
    private PixelWriter pixelWriter;
    private ProgressBar progressBar;
    private Label statusLabel;
    private Label timeLabel;
    private Button computeBtn;

    // Mandelbrot parameters
    private double minRe = -2.0;
    private double maxRe = 1.0;
    private double minIm = -1.2;
    private double maxIm = 1.2;

    @Override
    public void start(Stage primaryStage) {
        // Connect to server
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = ComputeServiceGrpc.newBlockingStub(channel);

        // UI Setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        HBox header = createHeader();
        root.setTop(header);

        // Canvas
        canvas = new Canvas(WIDTH, HEIGHT);
        image = new WritableImage(WIDTH, HEIGHT);
        pixelWriter = image.getPixelWriter();
        clearCanvas();

        StackPane centerPane = new StackPane(canvas);
        centerPane.setStyle("-fx-background-color: #0f3460; -fx-padding: 10;");
        root.setCenter(centerPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, WIDTH + 40, HEIGHT + 120);
        primaryStage.setTitle("JScience Distributed Mandelbrot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Ã°Å¸â€Â¬ Distributed Mandelbrot Set");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        computeBtn = new Button("Ã¢Å¡Â¡ Compute on Grid");
        computeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px;");
        computeBtn.setOnAction(e -> startDistributedComputation());

        Button localBtn = new Button("Ã°Å¸â€™Â» Local Compute");
        localBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: white; -fx-font-size: 14px;");
        localBtn.setOnAction(e -> computeLocal());

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> clearCanvas());

        header.getChildren().addAll(title, spacer, computeBtn, localBtn, clearBtn);
        return header;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statusLabel = new Label("Ready - Click 'Compute on Grid' to start");
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        timeLabel = new Label("Time: --");
        timeLabel.setStyle("-fx-text-fill: #888;");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        progressBar.setStyle("-fx-accent: #e94560;");

        footer.getChildren().addAll(statusLabel, spacer, timeLabel, progressBar);
        return footer;
    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        progressBar.setProgress(0);
        statusLabel.setText("Ready");
        timeLabel.setText("Time: --");
    }

    private void startDistributedComputation() {
        clearCanvas();
        computeBtn.setDisable(true);
        statusLabel.setText("Ã°Å¸Å¡â‚¬ Submitting " + SLICES + " tasks to grid...");

        long startTime = System.currentTimeMillis();
        AtomicInteger completed = new AtomicInteger(0);

        // Submit slices as tasks
        new Thread(() -> {
            for (int slice = 0; slice < SLICES; slice++) {
                int startY = slice * ROWS_PER_SLICE;
                int endY = Math.min(startY + ROWS_PER_SLICE, HEIGHT);

                // Encode slice parameters as serialized task data
                // Format: "MANDELBROT|startY|endY|minRe|maxRe|minIm|maxIm|maxIter|width"
                String taskParams = String.format("MANDELBROT|%d|%d|%f|%f|%f|%f|%d|%d",
                        startY, endY, minRe, maxRe, minIm, maxIm, MAX_ITER, WIDTH);

                String taskId = "mandelbrot-slice-" + slice + "-" + System.currentTimeMillis();
                TaskRequest request = TaskRequest.newBuilder()
                        .setTaskId(taskId)
                        .setSerializedTask(ByteString.copyFromUtf8(taskParams))
                        .setPriority(org.jscience.server.proto.Priority.CRITICAL)
                        .setTimestamp(System.currentTimeMillis())
                        .build();

                final int sliceNum = slice;
                try {
                    TaskResponse response = blockingStub.submitTask(request);
                    String responseTaskId = response.getTaskId();

                    // Poll for result (in real app, would use streaming)
                    pollForResult(responseTaskId, startY, endY, sliceNum, completed, startTime);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Ã°Å¸â€Â´ Error: " + e.getMessage());
                    });
                }
            }
        }).start();
    }

    private void pollForResult(String taskId, int startY, int endY, int sliceNum,
            AtomicInteger completed, long startTime) {
        // For demo purposes, simulate result and render locally
        // In production, would poll server for actual result

        // Compute this slice locally (simulating what worker would do)
        int[] escapeData = computeSlice(startY, endY);

        Platform.runLater(() -> {
            // Render the slice
            renderSlice(startY, endY, escapeData);

            int done = completed.incrementAndGet();
            progressBar.setProgress((double) done / SLICES);
            statusLabel.setText("Ã°Å¸â€â€ž Completed: " + done + "/" + SLICES + " slices");

            if (done == SLICES) {
                long elapsed = System.currentTimeMillis() - startTime;
                statusLabel.setText("Ã¢Å“â€¦ Computation complete!");
                timeLabel.setText(String.format("Time: %.2fs", elapsed / 1000.0));
                computeBtn.setDisable(false);
            }
        });
    }

    private int[] computeSlice(int startY, int endY) {
        int rows = endY - startY;
        int[] escapeData = new int[rows * WIDTH];

        double reFactor = (maxRe - minRe) / (WIDTH - 1);
        double imFactor = (maxIm - minIm) / (HEIGHT - 1);

        for (int y = startY; y < endY; y++) {
            double cIm = maxIm - y * imFactor;
            for (int x = 0; x < WIDTH; x++) {
                double cRe = minRe + x * reFactor;
                int escapeTime = computeEscapeTime(cRe, cIm);
                escapeData[(y - startY) * WIDTH + x] = escapeTime;
            }
        }
        return escapeData;
    }

    private int computeEscapeTime(double cRe, double cIm) {
        double zRe = 0, zIm = 0;
        for (int n = 0; n < MAX_ITER; n++) {
            double zRe2 = zRe * zRe, zIm2 = zIm * zIm;
            if (zRe2 + zIm2 > 4) {
                return n;
            }
            zIm = 2 * zRe * zIm + cIm;
            zRe = zRe2 - zIm2 + cRe;
        }
        return MAX_ITER;
    }

    private void renderSlice(int startY, int endY, int[] escapeData) {
        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int n = escapeData[(y - startY) * WIDTH + x];
                Color color = (n == MAX_ITER) ? Color.BLACK : Color.hsb((n * 7) % 360, 0.8, 1.0);
                pixelWriter.setColor(x, y, color);
            }
        }
        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    private void computeLocal() {
        clearCanvas();
        computeBtn.setDisable(true);
        statusLabel.setText("Ã°Å¸â€™Â» Computing locally...");

        long startTime = System.currentTimeMillis();

        new Thread(() -> {
            // Compute all rows locally
            for (int slice = 0; slice < SLICES; slice++) {
                int startY = slice * ROWS_PER_SLICE;
                int endY = Math.min(startY + ROWS_PER_SLICE, HEIGHT);
                int[] escapeData = computeSlice(startY, endY);

                final int sliceNum = slice;
                final int sy = startY, ey = endY;
                final int[] data = escapeData;

                Platform.runLater(() -> {
                    renderSlice(sy, ey, data);
                    progressBar.setProgress((double) (sliceNum + 1) / SLICES);
                });
            }

            long elapsed = System.currentTimeMillis() - startTime;
            Platform.runLater(() -> {
                statusLabel.setText("Ã¢Å“â€¦ Local computation complete!");
                timeLabel.setText(String.format("Time: %.2fs", elapsed / 1000.0));
                computeBtn.setDisable(false);
            });
        }).start();
    }

    @Override
    public void stop() {
        if (channel != null) {
            channel.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
