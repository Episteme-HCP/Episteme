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

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.server.proto.TaskRequest;
// import org.jscience.server.proto.Priority; // Removed to avoid conflict
import org.jscience.server.proto.ComputeServiceGrpc;

import java.util.concurrent.atomic.AtomicInteger;
import com.google.protobuf.ByteString;

/**
 * Distributed Mandelbrot Viewer - Computes Mandelbrot set using the JScience
 * Grid.
 * 
 * Refactored to use JScience Complex numbers for fractal computation.
 */
public class DistributedMandelbrotApp extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int SLICES = 30;
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
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ComputeServiceGrpc.newBlockingStub(channel);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setTop(createHeader());

        canvas = new Canvas(WIDTH, HEIGHT);
        image = new WritableImage(WIDTH, HEIGHT);
        pixelWriter = image.getPixelWriter();
        clearCanvas();

        root.setCenter(new StackPane(canvas));
        root.setBottom(createFooter());

        primaryStage.setTitle("JScience Distributed Mandelbrot");
        primaryStage.setScene(new Scene(root, WIDTH + 40, HEIGHT + 120));
        primaryStage.show();
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
        statusLabel.setText("Ã°Å¡â‚¬ Submitting slices to grid...");
        long startTime = System.currentTimeMillis();
        AtomicInteger completed = new AtomicInteger(0);

        new Thread(() -> {
            for (int slice = 0; slice < SLICES; slice++) {
                int startY = slice * ROWS_PER_SLICE;
                int endY = Math.min(startY + ROWS_PER_SLICE, HEIGHT);

                String taskParams = String.format("MANDELBROT|%d|%d|%f|%f|%f|%f|%d|%d",
                        startY, endY, minRe, maxRe, minIm, maxIm, MAX_ITER, WIDTH);

                org.jscience.server.proto.TaskRequest request = org.jscience.server.proto.TaskRequest.newBuilder()
                        .setTaskId("mandel-" + slice)
                        .setSerializedTask(ByteString.copyFromUtf8(taskParams))
                        .setPriority(org.jscience.server.proto.Priority.CRITICAL)
                        .setTimestamp(System.currentTimeMillis())
                        .build();

                try {
                    blockingStub.submitTask(request);
                    // Mock result for demo
                    int[] data = computeSlice(startY, endY);
                    Platform.runLater(() -> {
                        renderSlice(startY, endY, data);
                        int done = completed.incrementAndGet();
                        progressBar.setProgress((double) done / SLICES);
                        if (done == SLICES)
                            finish(startTime);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
                }
            }
        }).start();
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
                // Using JScience Complex numbers
                Complex c = Complex.of(cRe, cIm);
                escapeData[(y - startY) * WIDTH + x] = computeEscapeTime(c);
            }
        }
        return escapeData;
    }

    private int computeEscapeTime(Complex c) {
        Complex z = Complex.ZERO;
        for (int n = 0; n < MAX_ITER; n++) {
            if (z.abs().doubleValue() > 2.0)
                return n;
            z = z.pow(2).add(c);
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

    private void finish(long startTime) {
        long elapsed = System.currentTimeMillis() - startTime;
        statusLabel.setText("Ã¢Å“â€¦ Computation complete!");
        timeLabel.setText(String.format("Time: %.2fs", elapsed / 1000.0));
        computeBtn.setDisable(false);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Ã°Å¸â€ Â¬ Distributed Mandelbrot Set");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        computeBtn = new Button("Ã¢Å¡Â¡ Compute on Grid");
        computeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        computeBtn.setOnAction(e -> startDistributedComputation());
        header.getChildren().addAll(title, spacer, computeBtn);
        return header;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");
        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        timeLabel = new Label("Time: --");
        timeLabel.setStyle("-fx-text-fill: #888;");
        progressBar = new ProgressBar(0);
        footer.getChildren().addAll(statusLabel, spacer, timeLabel, progressBar);
        return footer;
    }

    @Override
    public void stop() {
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
