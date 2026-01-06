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

package org.jscience.client.earth.climatesim;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.earth.climate.GeneralCirculationModelTask;

import org.jscience.mathematics.loaders.VTKWriter;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Advanced Climate Simulation Client (GCM) with VTK Export.
 */
public class DistributedClimateSimApp extends Application {

    private GeneralCirculationModelTask task;
    private Canvas mapCanvas;
    private GraphicsContext gc;
    private Label avgTempLabel;
    private Label statusLabel;

    private final int WIDTH = 1000;
    private final int HEIGHT = 500;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private boolean distributed = true;
    private boolean serverAvailable = false;
    private long stepCount = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🌍 General Circulation Model (GCM) - JScience Grid");
        mapCanvas = new Canvas(WIDTH, HEIGHT);
        gc = mapCanvas.getGraphicsContext2D();
        task = new GeneralCirculationModelTask(60, 120);

        BorderPane root = new BorderPane();
        root.setCenter(mapCanvas);

        VBox overlay = new VBox(10);
        overlay.setPadding(new Insets(15));
        overlay.setStyle("-fx-background-color: rgba(30,30,30,0.85); -fx-background-radius: 10;");

        Label title = new Label("Advanced GCM Dynamics");
        title.setStyle("-fx-text-fill: #00e5ff; -fx-font-size: 20; -fx-font-weight: bold;");
        avgTempLabel = new Label("Avg Surface Temp: -- K");
        avgTempLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        statusLabel = new Label("Status: Initializing...");
        statusLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12;");

        CheckBox distCheck = new CheckBox("Grid computing enabled");
        distCheck.setSelected(true);
        distCheck.setStyle("-fx-text-fill: white;");
        distCheck.setOnAction(e -> distributed = distCheck.isSelected());

        Button exportBtn = new Button("Export VTK");
        exportBtn.setOnAction(e -> exportToVtk(stage));

        Button exportJsonBtn = new Button("Export JSON");
        exportJsonBtn.setOnAction(e -> exportToJson(stage));

        Button loadJsonBtn = new Button("Load JSON");
        loadJsonBtn.setOnAction(e -> loadFromJson(stage));

        HBox controls = new HBox(10, distCheck, exportBtn, exportJsonBtn, loadJsonBtn);
        overlay.getChildren().addAll(title, avgTempLabel, new Separator(), controls, statusLabel);
        overlay.setMaxWidth(450);
        root.setTop(overlay);

        stage.setScene(new Scene(root));
        stage.show();
        initGrpc();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                stepCount++;
                if (distributed && serverAvailable)
                    runDistributedStep();
                else {
                    task.step(86400);
                    statusLabel.setText("Mode: Local Core (Heavy Compute)");
                    render();
                }
            }
        }.start();
    }

    private void initGrpc() {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
            blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
            serverAvailable = true;
            statusLabel.setText("Status: Connected to Grid ✅");
        } catch (Exception e) {
            serverAvailable = false;
            statusLabel.setText("Status: Grid Offline (Local Fallback)");
        }
    }

    private void runDistributedStep() {
        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("gcm-step-" + stepCount)
                    .setSerializedTask(ByteString.copyFrom(serialize(task)))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .build();

            TaskResponse response = blockingStub.withDeadlineAfter(3, TimeUnit.SECONDS).submitTask(request);
            if (response.getStatus() == Status.QUEUED) {
                TaskResult result = blockingStub.withDeadlineAfter(2, TimeUnit.SECONDS)
                        .streamResults(TaskIdentifier.newBuilder().setTaskId(response.getTaskId()).build())
                        .next();
                if (result.getStatus() == Status.COMPLETED) {
                    GeneralCirculationModelTask resultTask = (GeneralCirculationModelTask) deserialize(
                            result.getSerializedData().toByteArray());
                    Platform.runLater(() -> {
                        task.updateState(
                                resultTask.getSurfaceTemperature() == null ? null
                                        : new double[][][] { resultTask.getSurfaceTemperature(),
                                                resultTask.getAirTemperature(), new double[60][120] },
                                resultTask.getHumidity());
                        // Simple update for demonstration
                        render();
                    });
                    statusLabel.setText("Mode: Distributed GCM Grid");
                    return;
                }
            }
            task.step(86400);
            render();
        } catch (Exception e) {
            task.step(86400);
            render();
            statusLabel.setText("Mode: Local Fallback (Grid Delay)");
        }
    }

    private void exportToVtk(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save VTK Export", "VTK Files", "*.vtk");
        if (file != null) {
            try {
                new VTKWriter().save(task.getSurfaceTemperature(), file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, "Export successful: " + file.getName()).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();
            }
        }
    }

    private void exportToJson(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, "Save JSON State", "JSON Files",
                "*.json");
        if (file != null) {
            try {
                double[][][] temps = new double[][][] {
                        task.getSurfaceTemperature(),
                        task.getAirTemperature(),
                        new double[60][120]
                };
                new ClimateDataWriter().save(
                        new ClimateDataReader.ClimateState(temps, task.getHumidity()),
                        file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, "Export successful: " + file.getName()).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();
            }
        }
    }

    private void loadFromJson(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load JSON State", "JSON Files",
                "*.json");
        if (file != null) {
            try {
                ClimateDataReader.ClimateState state = new ClimateDataReader()
                        .load(file.getAbsolutePath());
                task.updateState(state.temperature, state.humidity);
                render();
                new Alert(Alert.AlertType.INFORMATION, "Loaded state from: " + file.getName()).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Load failed: " + e.getMessage()).show();
            }
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    private Object deserialize(byte[] data) throws Exception {
        return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
    }

    private void render() {
        double[][] temp = task.getSurfaceTemperature();
        int rows = temp.length;
        int cols = temp[0].length;
        double cellW = (double) WIDTH / cols;
        double cellH = (double) HEIGHT / rows;
        double totalTemp = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double t = temp[i][j];
                totalTemp += t;
                gc.setFill(getColor(t));
                gc.fillRect(j * cellW, i * cellH, cellW, cellH);
            }
        }
        avgTempLabel.setText(String.format("Global Avg Surface Temp: %.2f K", totalTemp / (rows * cols)));
    }

    private Color getColor(double tempK) {
        double norm = Math.max(0, Math.min(1, (tempK - 240.0) / 70.0));
        return Color.hsb(240 * (1 - norm), 0.8, 0.9);
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
