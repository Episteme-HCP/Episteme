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

package org.jscience.client.physics.wavesim;

import org.jscience.physics.wave.WaveSimTask;

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
import org.jscience.ui.ThemeManager;


import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Wave Equation Solver with Distributed support.
 */
public class DistributedWaveSimApp extends Application implements org.jscience.ui.App {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int SCALE = 2;

    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;
    private WaveSimTask task;
    private boolean running = false;
    private boolean distributed = true;

    // Distributed Logic
    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private boolean serverAvailable = false;
    private long stepCount = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.title", "🌊 Wave Equation - Distributed JScience"));

        canvas = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        gc = canvas.getGraphicsContext2D();
        task = new WaveSimTask(WIDTH, HEIGHT);

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.getStyleClass().add("viewer-sidebar");
        controls.setPrefWidth(250);

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.header", "Wave Equation"));
        title.getStyleClass().add("header-label-white");

        Slider speedSlider = new Slider(0.1, 0.9, 0.5);
        speedSlider.getStyleClass().add("slider-custom");
        speedSlider.valueProperty().addListener((obs, old, val) -> task.setC(val.doubleValue()));

        Slider dampSlider = new Slider(0.9, 1.0, 0.999);
        dampSlider.getStyleClass().add("slider-custom");
        dampSlider.valueProperty().addListener((obs, old, val) -> task.setDamping(val.doubleValue()));

        statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.status.connected", "Status: Connected to Grid"));
        statusLabel.getStyleClass().add("label-muted");

        CheckBox distCheck = new CheckBox(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.chk.dist", "Distributed Mode"));
        distCheck.setSelected(true);
        distCheck.getStyleClass().add("check-box-custom");
        distCheck.setOnAction(e -> distributed = distCheck.isSelected());

        Button startBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.btn.start", "▶ Start"));
        startBtn.getStyleClass().add("accent-button-green");
        startBtn.setPrefWidth(220);
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.btn.pause", "⏸ Pause") : org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.btn.resume", "▶ Resume"));
            startBtn.getStyleClass().setAll("button", running ? "accent-button-red" : "accent-button-green");
        });

        controls.getChildren().addAll(title, new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.lbl.speed", "Speed:")), speedSlider, new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.lbl.damping", "Damping:")), dampSlider,
                new Separator(), distCheck, statusLabel, startBtn);

        canvas.setOnMouseClicked(e -> createDrop((int) (e.getX() / SCALE), (int) (e.getY() / SCALE), 15));
        
        BorderPane rootPanel = new BorderPane();
        rootPanel.getStyleClass().add("viewer-root");
        rootPanel.setCenter(canvas);
        rootPanel.setRight(controls);

        Scene scene = new Scene(rootPanel);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setScene(scene);
        stage.show();

        initGrpc();
        startLoop();
    }


    private void initGrpc() {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
            blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
            serverAvailable = true;
        } catch (Exception e) {
            serverAvailable = false;
        }
    }

    private void startLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running)
                    return;
                stepCount++;
                if (distributed && serverAvailable)
                    runDistributedStep();
                else {
                    task.step();
                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.status.local", "Status: Local Performance"));
                    render();
                }
            }
        }.start();
    }

    private void runDistributedStep() {
        try {
            byte[] waveData = serializeWave();
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("wave-" + stepCount)
                    .setSerializedTask(ByteString.copyFrom(waveData))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .build();

            TaskResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).submitTask(request);
            if (response.getStatus() == Status.QUEUED) {
                try {
                    TaskResult result = blockingStub.withDeadlineAfter(50, TimeUnit.MILLISECONDS)
                            .streamResults(TaskIdentifier.newBuilder().setTaskId(response.getTaskId()).build())
                            .next();
                    if (result.getStatus() == Status.COMPLETED) {
                        applyWave(result.getSerializedData().toByteArray());
                        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.status.dist", "Status: Grid Computed ✅"));
                        render();
                        return;
                    }
                } catch (Exception e) {
                }
            }
            task.step();
            render();
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.status.queued", "Status: Grid Queued ⏳"));
        } catch (Exception e) {
            task.step();
            render();
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.status.offline", "Status: Grid Offline ❌"));
        }
    }

    private byte[] serializeWave() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("WAVE_2D");
        dos.writeInt(WIDTH);
        dos.writeInt(HEIGHT);
        dos.writeDouble(task.getC());
        dos.writeDouble(task.getDamping());
        double[][] u = task.getU();
        double[][] up = task.getUPrev();
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++) {
                dos.writeDouble(u[x][y]);
                dos.writeDouble(up[x][y]);
            }
        dos.flush();
        return bos.toByteArray();
    }

    private void applyWave(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        double[][] u = new double[WIDTH][HEIGHT];
        double[][] up = new double[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++) {
                u[x][y] = dis.readDouble();
                up[x][y] = dis.readDouble();
            }
        Platform.runLater(() -> task.updateState(u, up));
    }

    private void createDrop(int cx, int cy, int radius) {
        double[][] u = task.getU();
        for (int x = Math.max(1, cx - radius); x < Math.min(WIDTH - 1, cx + radius); x++) {
            for (int y = Math.max(1, cy - radius); y < Math.min(HEIGHT - 1, cy + radius); y++) {
                double dist = Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (dist < radius)
                    u[x][y] += Math.cos(Math.PI * dist / radius / 2);
            }
        }
    }

    private void render() {
        double[][] u = task.getU();
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++) {
                double v = (u[x][y] + 1) / 2;
                v = Math.max(0, Math.min(1, v));
                gc.setFill(Color.rgb((int) (255 * v), (int) (100 * (1 - v)), (int) (255 * (1 - v))));
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
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
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.name", "Distributed Wave Sim App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.desc", "Distributed 2D wave equation solver for physics simulations."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedwavesimapp.longdesc", "Simulate wave propagation in 2D media. This application uses the JScience computing cluster to parallelize the numerical integration of the wave equation, allowing for real-time high-resolution simulations and complex boundary condition analysis."); }

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

