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

package org.jscience.client.physics.fluidsim;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.physics.fluid.LatticeBoltzmann;
import org.jscience.server.proto.*;
import org.jscience.ui.i18n.I18n;

import java.io.*;
import java.util.concurrent.TimeUnit;
// import javafx.scene.image.Image;
// import javafx.scene.image.PixelReader;

/**
 * Lattice Boltzmann Fluid Simulation with distributed support.
 */
public class DistributedFluidSimApp extends Application implements org.jscience.ui.App {

    private final int W = 200, H = 100, SCALE = 4;
    private double[][][] f = new double[W][H][9];
    private boolean[][] obstacle = new boolean[W][H];
    private double[][] grid = new double[W][H];
    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;
    private CheckBox localSimCheckBox;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private long stepCount = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle(I18n.getInstance().get("app.distributedfluidsimapp.title", "🌊 Fluid Dynamics - Distributed LBM"));
        canvas = new Canvas(W * SCALE, H * SCALE);
        gc = canvas.getGraphicsContext2D();
        initFluid();

        BorderPane root = new BorderPane(canvas);
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        statusLabel = new Label(I18n.getInstance().get("app.distributedfluidsimapp.status.checking", "Grid Status: Checking..."));
        statusLabel.setTextFill(Color.WHITE);
        localSimCheckBox = new CheckBox(I18n.getInstance().get("app.distributedfluidsimapp.chk.local", "Local LBM Simulation")); // Renamed and initialized
        localSimCheckBox.setSelected(true);
        localSimCheckBox.setTextFill(Color.WHITE);
        Button loadObstacleBtn = new Button(I18n.getInstance().get("app.distributedfluidsimapp.btn.load_map", "Load Obstacle Map"));
        loadObstacleBtn.setTextFill(Color.BLACK);
        loadObstacleBtn.setOnAction(e -> loadObstacleMap(stage));

        controls.getChildren().addAll(new Label(I18n.getInstance().get("app.distributedfluidsimapp.header", "Fluid Control")), localSimCheckBox, loadObstacleBtn, statusLabel);
        root.setRight(controls);

        stage.setScene(new Scene(root));
        stage.show();
        initGrpc();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                stepCount++;
                if (localSimCheckBox.isSelected()) {
                    runLocalLbmStep();
                } else {
                    submitDistributedTask();
                }
            }
        }.start();
    }

    private void initFluid() {
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++) {
                if (Math.hypot(x - W / 4, y - H / 2) < 10)
                    obstacle[x][y] = true;
                for (int i = 0; i < 9; i++)
                    f[x][y][i] = 1.0 / 9.0;
            }
    }

    private void initGrpc() {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
            blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
        } catch (Exception e) {
        }
    }

    private void submitDistributedTask() { // Renamed from runDistributedStep
        try {
            byte[] fluidData = serializeFluidState();
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("lbm-" + stepCount)
                    .setSerializedTask(ByteString.copyFrom(fluidData))
                    .setPriority(org.jscience.server.proto.Priority.CRITICAL)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            TaskResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).submitTask(request);
            if (response.getStatus() == Status.QUEUED) {
                try {
                    TaskResult result = blockingStub.withDeadlineAfter(30, TimeUnit.MILLISECONDS)
                            .streamResults(TaskIdentifier.newBuilder().setTaskId(response.getTaskId()).build())
                            .next();
                    if (result.getStatus() == Status.COMPLETED) {
                        applyFluidResults(result.getSerializedData().toByteArray());
                        render();
                        statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.dist", "Mode: Distributed ✅"));
                        return;
                    }
                } catch (Exception e) {
                }
            }
            localStep(); // Fallback to local if distributed fails or is slow
            render();
            statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.sluggish", "Mode: Local (Grid Sluggish)"));
        } catch (Exception e) {
            localStep(); // Fallback to local if distributed fails
            render();
            statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.offline", "Mode: Grid Offline"));
        }
    }

    private byte[] serializeFluidState() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("FLUID_LBM");
        dos.writeInt(W);
        dos.writeInt(H);
        dos.writeDouble(0.02);
        dos.writeDouble(0.1);
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++) {
                dos.writeBoolean(obstacle[x][y]);
                for (int i = 0; i < 9; i++)
                    dos.writeDouble(f[x][y][i]);
            }
        dos.flush();
        return bos.toByteArray();
    }

    private void applyFluidResults(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++) {
                dis.readDouble(); // Assuming these are density, velocityX, velocityY
                dis.readDouble();
                dis.readDouble();
                for (int i = 0; i < 9; i++)
                    f[x][y][i] = dis.readDouble();
            }
    }

    private LatticeBoltzmann localLbm;

    private void runLocalLbmStep() {
        if (localLbm == null) {
            localLbm = new LatticeBoltzmann(W, H, 0.01);
            for (int x = 0; x < W; x++)
                for (int y = 0; y < H; y++)
                    localLbm.setObstacle(x, y, obstacle[x][y]);
        }
        localLbm.step();
        double[][] density = localLbm.getDensity();
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++)
                grid[x][y] = density[x][y];
        Platform.runLater(this::drawSimulation);
        statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.local", "Mode: Local LBM"));
    }

    private void drawSimulation() {
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++) {
                if (obstacle[x][y]) {
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                    continue;
                }
                gc.setFill(Color.hsb(200, 0.8, Math.min(1, grid[x][y] / 2)));
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
    }

    private void localStep() {
        /* Simplified LBM placeholder */
        // This method is now only a fallback for distributed mode
        // and should ideally be replaced by a proper local LBM step if needed.
        // For now, it just renders the existing 'f' array.
        render();
    }

    private void render() {
        for (int x = 0; x < W; x++)
            for (int y = 0; y < H; y++) {
                if (obstacle[x][y]) {
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                    continue;
                }
                double rho = 0;
                for (int i = 0; i < 9; i++)
                    rho += f[x][y][i];
                gc.setFill(Color.hsb(200, 0.8, Math.min(1, rho / 2)));
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
    }

    private void loadObstacleMap(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, I18n.getInstance().get("app.distributedfluidsimapp.file.load_map", "Load Obstacle Map"), I18n.getInstance().get("app.distributedfluidsimapp.file.images", "Images"), "*.png",
                "*.jpg", "*.bmp");
        if (file != null) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(new FileInputStream(file));
                javafx.scene.image.PixelReader pr = img.getPixelReader();
                if (pr == null)
                    return;

                int imgW = (int) img.getWidth();
                int imgH = (int) img.getHeight();

                // Reset fluid and obstacles
                for (int x = 0; x < W; x++) {
                    for (int y = 0; y < H; y++) {
                        obstacle[x][y] = false; // clear old obstacles
                        for (int i = 0; i < 9; i++)
                            f[x][y][i] = 1.0 / 9.0; // reset fluid
                    }
                }

                // Map image to grid
                for (int x = 0; x < Math.min(W, imgW); x++) {
                    for (int y = 0; y < Math.min(H, imgH); y++) {
                        // Dark pixels are obstacles
                        if (pr.getColor(x, y).getBrightness() < 0.5) {
                            obstacle[x][y] = true;
                        }
                    }
                }
                statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.loaded", "Map Loaded: {0}", file.getName()));
            } catch (Exception e) {
                statusLabel.setText(I18n.getInstance().get("app.distributedfluidsimapp.status.load_failed", "Load Failed"));
            }
        }
    }

    @Override
    public void stop() {
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
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.physics", "Physics"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedfluidsimapp.name", "Distributed Fluid Sim App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedfluidsimapp.desc", "Distributed application for Distributed Fluid Sim App."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("app.distributedfluidsimapp.longdesc", "Distributed application for Distributed Fluid Sim App."); }

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
