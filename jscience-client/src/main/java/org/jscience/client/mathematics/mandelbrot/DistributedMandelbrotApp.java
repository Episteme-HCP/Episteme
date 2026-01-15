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

package org.jscience.client.mathematics.mandelbrot;

import org.jscience.mathematics.mandelbrot.MandelbrotTask;
import org.jscience.mathematics.mandelbrot.RealMandelbrotTask;
import org.jscience.mathematics.numbers.real.Real;

import com.google.protobuf.ByteString;
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
import org.jscience.server.proto.*;
import org.jscience.ui.i18n.I18n;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

/**
 * Distributed Mandelbrot Viewer - Computes Mandelbrot set using the JScience
 * Grid.
 * 
 * Uses actual distributed computation when connected to a server with workers.
 * Falls back to local computation when server is unavailable.
 */
public class DistributedMandelbrotApp extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int SLICES = 30;
    private static final int ROWS_PER_SLICE = HEIGHT / SLICES;
    private static final int MAX_ITER = 200;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private boolean serverAvailable = false;

    private Canvas canvas;
    private WritableImage image;
    private PixelWriter pixelWriter;
    private ProgressBar progressBar;
    private Label statusLabel;
    private Label timeLabel;
    private Button computeBtn;

    private ToggleButton localModeToggle;
    private CheckBox highPrecisionToggle;

    // Mandelbrot parameters
    private double minRe = -2.0;
    private double maxRe = 1.0;
    private double minIm = -1.2;
    private double maxIm = 1.2;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void start(Stage primaryStage) {
        String host = getParameters().getNamed().getOrDefault("host", "localhost");
        int port = Integer.parseInt(getParameters().getNamed().getOrDefault("port", "50051"));

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ComputeServiceGrpc.newBlockingStub(channel);

        // Check server availability
        checkServerAvailability();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setTop(createHeader());

        canvas = new Canvas(WIDTH, HEIGHT);
        image = new WritableImage(WIDTH, HEIGHT);
        pixelWriter = image.getPixelWriter();
        clearCanvas();

        root.setCenter(new StackPane(canvas));
        root.setBottom(createFooter());

        primaryStage.setTitle(I18n.getInstance().get("app.distributedmandelbrotapp.title", "JScience Distributed Mandelbrot"));
        primaryStage.setScene(new Scene(root, WIDTH + 40, HEIGHT + 120));
        primaryStage.show();
    }

    private void checkServerAvailability() {
        try {
            ServerStatus status = blockingStub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getStatus(Empty.newBuilder().build());
            serverAvailable = status.getActiveWorkers() > 0;
            System.out.println("Server status: " + status.getActiveWorkers() + " workers available");
        } catch (Exception e) {
            serverAvailable = false;
            System.out.println("Server not available, will use local computation");
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        progressBar.setProgress(0);
        statusLabel.setText(I18n.getInstance().get("app.distributedmandelbrotapp.status.ready", "Ready"));
        timeLabel.setText(I18n.getInstance().get("app.distributedmandelbrotapp.status.time", "Time: --"));
    }

    private void startDistributedComputation() {
        clearCanvas();
        computeBtn.setDisable(true);
        long startTime = System.currentTimeMillis();
        AtomicInteger completed = new AtomicInteger(0);

        boolean useLocal = localModeToggle.isSelected() || !serverAvailable;

        if (useLocal) {
            statusLabel.setText(I18n.getInstance().get("app.distributedmandelbrotapp.status.local", "💻 Computing locally..."));
            startLocalComputation(startTime, completed);
        } else {
            statusLabel.setText(I18n.getInstance().get("app.distributedmandelbrotapp.status.submitting", "🌐 Submitting slices to grid..."));
            startGridComputation(startTime, completed);
        }
    }

    private void startLocalComputation(long startTime, AtomicInteger completed) {
        for (int slice = 0; slice < SLICES; slice++) {
            // final int sliceNum = slice;
            final int startY = slice * ROWS_PER_SLICE;
            final int endY = Math.min(startY + ROWS_PER_SLICE, HEIGHT);

            executorService.submit(() -> {
                int[] data = computeSlice(startY, endY);
                Platform.runLater(() -> {
                    renderSlice(startY, endY, data);
                    int done = completed.incrementAndGet();
                    progressBar.setProgress((double) done / SLICES);
                    if (done == SLICES)
                        finish(startTime, "local");
                });
            });
        }
    }

    private void startGridComputation(long startTime, AtomicInteger completed) {
        // ConcurrentHashMap<String, int[]> results = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(SLICES);

        for (int slice = 0; slice < SLICES; slice++) {
            final int sliceNum = slice;
            final int startY = slice * ROWS_PER_SLICE;
            final int endY = Math.min(startY + ROWS_PER_SLICE, HEIGHT);

            // Serialize task parameters
            String taskId = "mandel-" + slice + "-" + System.currentTimeMillis();
            byte[] taskData = serializeSliceTask(startY, endY);

            String type = (highPrecisionToggle != null && highPrecisionToggle.isSelected()) ? "MANDELBROT_REAL"
                    : "MANDELBROT";

            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId(taskId)
                    .setTaskType(type)
                    .setSerializedTask(ByteString.copyFrom(taskData))
                    .setPriority(org.jscience.server.proto.Priority.CRITICAL)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            executorService.submit(() -> {
                try {
                    TaskResponse response = blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                            .submitTask(request);

                    if (response.getStatus() == Status.QUEUED) {
                        // Wait for result from worker
                        TaskIdentifier taskIdent = TaskIdentifier.newBuilder()
                                .setTaskId(taskId)
                                .build();

                        try {
                            Iterator<TaskResult> resultIterator = blockingStub
                                    .withDeadlineAfter(30, TimeUnit.SECONDS)
                                    .streamResults(taskIdent);

                            if (resultIterator.hasNext()) {
                                TaskResult result = resultIterator.next();
                                if (result.getStatus() == Status.COMPLETED) {
                                    int[] sliceData = deserializeSliceResult(result.getSerializedData().toByteArray());
                                    applySliceResult(sliceNum, startY, endY, sliceData, completed, startTime);
                                    latch.countDown();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            // Result not available, compute locally
                        }
                    }

                    // Fallback to local computation
                    int[] localData = computeSlice(startY, endY);
                    applySliceResult(sliceNum, startY, endY, localData, completed, startTime);
                    latch.countDown();

                } catch (Exception e) {
                    // Server error, compute locally
                    int[] localData = computeSlice(startY, endY);
                    applySliceResult(sliceNum, startY, endY, localData, completed, startTime);
                    latch.countDown();
                }
            });
        }
    }

    private void applySliceResult(int sliceNum, int startY, int endY, int[] data,
            AtomicInteger completed, long startTime) {
        Platform.runLater(() -> {
            renderSlice(startY, endY, data);
            int done = completed.incrementAndGet();
            progressBar.setProgress((double) done / SLICES);
            if (done == SLICES)
                finish(startTime, serverAvailable ? "distributed" : "local");
        });
    }

    private byte[] serializeSliceTask(int startY, int endY) {
        try {
            int sliceHeight = endY - startY;
            double imFactor = (maxIm - minIm) / (HEIGHT - 1);
            // Calculate Y bounds for this slice (top is y=0 -> maxIm)
            double sliceMaxIm = maxIm - startY * imFactor;
            double sliceMinIm = maxIm - (endY - 1) * imFactor;

            MandelbrotTask task;
            if (highPrecisionToggle != null && highPrecisionToggle.isSelected()) {
                task = new RealMandelbrotTask(WIDTH, sliceHeight,
                        Real.of(minRe), Real.of(maxRe),
                        Real.of(sliceMinIm), Real.of(sliceMaxIm),
                        100);
            } else {
                task = new MandelbrotTask(WIDTH, sliceHeight, minRe, maxRe, sliceMinIm, sliceMaxIm);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(task);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private int[] deserializeSliceResult(byte[] data) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            MandelbrotTask resultTask = (MandelbrotTask) ois.readObject();
            // result is 2D array [width][height]
            int[][] result2D = resultTask.getResult();
            if (result2D == null)
                return new int[0];

            // Flatten to 1D for renderer (or update renderer to support 2D)
            // Existing logic expected 1D flat array [rows * WIDTH]
            int w = resultTask.getWidth();
            int h = resultTask.getHeight();
            int[] flat = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    flat[y * w + x] = result2D[x][y];
                }
            }
            return flat;
        } catch (Exception e) {
            e.printStackTrace();
            return new int[0];
        }
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

    private void finish(long startTime, String mode) {
        long elapsed = System.currentTimeMillis() - startTime;
        String modeIcon = mode.equals("distributed") ? "🌐" : "💻";
        statusLabel.setText(String.format(I18n.getInstance().get("app.distributedmandelbrotapp.status.complete", "%s Computation complete (%s)"), modeIcon, mode));
        timeLabel.setText(String.format(I18n.getInstance().get("app.distributedmandelbrotapp.time_format", "Time: %.2fs"), elapsed / 1000.0));
        computeBtn.setDisable(false);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #16213e;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(I18n.getInstance().get("app.distributedmandelbrotapp.header", "🔬 Distributed Mandelbrot Set"));
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        localModeToggle = new ToggleButton(I18n.getInstance().get("app.distributedmandelbrotapp.btn.local_toggle", "💻 Local Only"));
        localModeToggle.setStyle("-fx-background-color: #666; -fx-text-fill: white;");
        localModeToggle.setOnAction(e -> {
            if (localModeToggle.isSelected()) {
                statusLabel.setText(I18n.getInstance().get("app.distributedmandelbrotapp.status.local_mode", "💻 Local mode - will not use grid"));
            } else {
                checkServerAvailability();
                statusLabel.setText(serverAvailable ? I18n.getInstance().get("app.distributedmandelbrotapp.status.grid_avail", "🌐 Grid available") : I18n.getInstance().get("app.distributedmandelbrotapp.status.grid_unavail", "⚠️ Grid unavailable"));
            }
        });

        highPrecisionToggle = new CheckBox(I18n.getInstance().get("app.distributedmandelbrotapp.chk.high_prec", "High Precision"));
        highPrecisionToggle.setStyle("-fx-text-fill: white;");

        computeBtn = new Button(I18n.getInstance().get("app.distributedmandelbrotapp.btn.compute", "⚡ Compute on Grid"));
        computeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        computeBtn.setOnAction(e -> startDistributedComputation());

        Button saveImgBtn = new Button(I18n.getInstance().get("app.distributedmandelbrotapp.btn.save_img", "💾 Save Image"));
        saveImgBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e;");
        saveImgBtn.setOnAction(e -> saveImage((Stage) computeBtn.getScene().getWindow()));

        Button saveCfgBtn = new Button(I18n.getInstance().get("app.distributedmandelbrotapp.btn.save_cfg", "⚙ Save Config"));
        saveCfgBtn.setOnAction(e -> saveConfig((Stage) computeBtn.getScene().getWindow()));

        Button loadCfgBtn = new Button(I18n.getInstance().get("app.distributedmandelbrotapp.btn.load_cfg", "📂 Load Config"));
        loadCfgBtn.setOnAction(e -> loadConfig((Stage) computeBtn.getScene().getWindow()));

        header.getChildren().addAll(title, spacer, localModeToggle, highPrecisionToggle, computeBtn, saveImgBtn,
                saveCfgBtn, loadCfgBtn);
        return header;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #16213e;");

        statusLabel = new Label(serverAvailable ? I18n.getInstance().get("app.distributedmandelbrotapp.status.grid_avail", "🌐 Grid available") : I18n.getInstance().get("app.distributedmandelbrotapp.status.local_footer", "💻 Local mode"));
        statusLabel.setStyle("-fx-text-fill: #4ecca3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        timeLabel = new Label(I18n.getInstance().get("app.distributedmandelbrotapp.status.time", "Time: --"));
        timeLabel.setStyle("-fx-text-fill: #888;");

        progressBar = new ProgressBar(0);

        footer.getChildren().addAll(statusLabel, spacer, timeLabel, progressBar);
        return footer;
    }

    @Override
    public void stop() {
        executorService.shutdown();
        if (channel != null)
            channel.shutdown();
    }

    private void saveImage(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, I18n.getInstance().get("app.distributedmandelbrotapp.file.save_img", "Save Image"), I18n.getInstance().get("app.distributedmandelbrotapp.file.png", "PNG Images"), "*.png");
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, I18n.getInstance().get("app.distributedmandelbrotapp.alert.save_img.error", "Failed to save image: {0}", e.getMessage())).show();
            }
        }
    }

    private void saveConfig(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, I18n.getInstance().get("app.distributedmandelbrotapp.file.save_cfg", "Save Config"), I18n.getInstance().get("app.distributedmandelbrotapp.file.json", "JSON Config"), "*.json");
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.printf("{\"minRe\": %f, \"maxRe\": %f, \"minIm\": %f, \"maxIm\": %f}", minRe, maxRe, minIm, maxIm);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, I18n.getInstance().get("app.distributedmandelbrotapp.alert.save_cfg.error", "Failed to save config")).show();
            }
        }
    }

    private void loadConfig(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, I18n.getInstance().get("app.distributedmandelbrotapp.file.load_cfg", "Load Config"), I18n.getInstance().get("app.distributedmandelbrotapp.file.json", "JSON Config"), "*.json");
        if (file != null) {
            try (java.util.Scanner s = new java.util.Scanner(file).useDelimiter("\\A")) {
                String content = s.hasNext() ? s.next() : "";
                content = content.replace("{", "").replace("}", "").replace("\"", "");
                String[] parts = content.split(",");
                for (String p : parts) {
                    String[] kv = p.split(":");
                    if (kv.length < 2)
                        continue;
                    String key = kv[0].trim();
                    double val = Double.parseDouble(kv[1].trim());
                    if (key.equals("minRe"))
                        minRe = val;
                    if (key.equals("maxRe"))
                        maxRe = val;
                    if (key.equals("minIm"))
                        minIm = val;
                    if (key.equals("maxIm"))
                        maxIm = val;
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, I18n.getInstance().get("app.distributedmandelbrotapp.alert.load_cfg.error", "Failed to load config")).show();
            }
            clearCanvas();
            startDistributedComputation();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
