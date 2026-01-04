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

package org.jscience.client.physics.wavesim;

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
import org.jscience.client.physics.wavesim.WaveSimTask;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Wave Equation Solver with Distributed support.
 */
public class DistributedWaveSimApp extends Application {

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
        stage.setTitle("🌊 Wave Equation - Distributed JScience");

        canvas = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        gc = canvas.getGraphicsContext2D();
        task = new WaveSimTask(WIDTH, HEIGHT);

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(220);

        Label title = new Label("Wave Equation");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #7c3aed;");

        Slider speedSlider = new Slider(0.1, 0.9, 0.5);
        speedSlider.valueProperty().addListener((obs, old, val) -> task.setC(val.doubleValue()));

        Slider dampSlider = new Slider(0.9, 1.0, 0.999);
        dampSlider.valueProperty().addListener((obs, old, val) -> task.setDamping(val.doubleValue()));

        statusLabel = new Label("Status: Connected to Grid");
        statusLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11;");

        CheckBox distCheck = new CheckBox("Distributed Mode");
        distCheck.setSelected(true);
        distCheck.setStyle("-fx-text-fill: white;");
        distCheck.setOnAction(e -> distributed = distCheck.isSelected());

        Button startBtn = new Button("▶ Start");
        startBtn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-pref-width: 200;");
        startBtn.setOnAction(e -> {
            running = !running;
            startBtn.setText(running ? "⏸ Pause" : "▶ Resume");
        });

        controls.getChildren().addAll(title, new Label("Speed:"), speedSlider, new Label("Damping:"), dampSlider,
                new Separator(), distCheck, statusLabel, startBtn);

        canvas.setOnMouseClicked(e -> createDrop((int) (e.getX() / SCALE), (int) (e.getY() / SCALE), 15));
        HBox rootPanel = new HBox(canvas, controls);
        rootPanel.setStyle("-fx-background-color: #0f0f1a;");

        stage.setScene(scene(rootPanel));
        stage.show();

        initGrpc();
        startLoop();
    }

    private Scene scene(HBox root) {
        return new Scene(root);
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
                    statusLabel.setText("Status: Local Performance");
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
                        statusLabel.setText("Status: Grid Computed ✅");
                        render();
                        return;
                    }
                } catch (Exception e) {
                }
            }
            task.step();
            render();
            statusLabel.setText("Status: Grid Queued ⏳");
        } catch (Exception e) {
            task.step();
            render();
            statusLabel.setText("Status: Grid Offline ❌");
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
}
