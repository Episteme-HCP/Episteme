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
import org.jscience.ui.ThemeManager;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Advanced Climate Simulation Client (GCM) with VTK Export.
 */
public class DistributedClimateSimApp extends Application implements org.jscience.ui.App {

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
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.title", "🌍 General Circulation Model (GCM) - JScience Grid"));
        mapCanvas = new Canvas(WIDTH, HEIGHT);
        gc = mapCanvas.getGraphicsContext2D();
        task = new GeneralCirculationModelTask(60, 120);

        BorderPane root = new BorderPane();
        root.setCenter(mapCanvas);

        VBox overlay = new VBox(10);
        overlay.setPadding(new Insets(15));
        overlay.getStyleClass().add("section-box");

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.header", "Advanced GCM Dynamics"));
        title.getStyleClass().add("header-label-white");
        avgTempLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.temp_label", "Avg Surface Temp: -- K"));
        avgTempLabel.getStyleClass().add("label-white");
        statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.init", "Status: Initializing..."));
        statusLabel.getStyleClass().add("label-muted");

        CheckBox distCheck = new CheckBox(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.chk.grid", "Grid computing enabled"));
        distCheck.setSelected(true);
        distCheck.getStyleClass().add("check-box-custom");
        distCheck.setOnAction(e -> distributed = distCheck.isSelected());

        Button exportBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.btn.export_vtk", "Export VTK"));
        exportBtn.setOnAction(e -> exportToVtk(stage));

        Button exportJsonBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.btn.export_json", "Export JSON"));
        exportJsonBtn.setOnAction(e -> exportToJson(stage));

        Button loadJsonBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.btn.load_json", "Load JSON"));
        loadJsonBtn.setOnAction(e -> loadFromJson(stage));

        HBox controls = new HBox(10, distCheck, exportBtn, exportJsonBtn, loadJsonBtn);
        overlay.getChildren().addAll(title, avgTempLabel, new Separator(), controls, statusLabel);
        overlay.setMaxWidth(450);
        root.setTop(overlay);

        Scene scene = new Scene(root);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setScene(scene);
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
                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.local", "Mode: Local Core (Heavy Compute)"));
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
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.connected", "Status: Connected to Grid ✅"));
        } catch (Exception e) {
            serverAvailable = false;
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.offline", "Status: Grid Offline (Local Fallback)"));
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
                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.dist_mode", "Mode: Distributed GCM Grid"));
                    return;
                }
            }
            task.step(86400);
            render();
        } catch (Exception e) {
            task.step(86400);
            render();
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.status.fallback", "Mode: Local Fallback (Grid Delay)"));
        }
    }

    private void exportToVtk(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.save.vtk", "Save VTK Export"), org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.vtk", "VTK Files"), "*.vtk");
        if (file != null) {
            try {
                new VTKWriter().save(task.getSurfaceTemperature(), file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.export.success", "Export successful: {0}", file.getName())).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.export.error", "Export failed: {0}", e.getMessage())).show();
            }
        }
    }

    private void exportToJson(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showSaveDialog(stage, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.save.json", "Save JSON State"), org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.json", "JSON Files"),
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
                new Alert(Alert.AlertType.INFORMATION, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.export.success", "Export successful: {0}", file.getName())).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.export.error", "Export failed: {0}", e.getMessage())).show();
            }
        }
    }

    private void loadFromJson(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.load.json", "Load JSON State"), org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.file.json", "JSON Files"),
                "*.json");
        if (file != null) {
            try {
                ClimateDataReader.ClimateState state = new ClimateDataReader()
                        .load(file.getAbsolutePath());
                task.updateState(state.temperature, state.humidity);
                render();
                new Alert(Alert.AlertType.INFORMATION, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.load.success", "Loaded state from: {0}", file.getName())).show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.alert.load.error", "Load failed: {0}", e.getMessage())).show();
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
        avgTempLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.temp_format", "Global Avg Surface Temp: %.2f K"), totalTemp / (rows * cols)));
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

    // App Interface Implementation
    @Override
    public boolean isDemo() {
        return false;
    }

    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.earthsciences", "Earth Sciences"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.name", "Distributed Climate Sim App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.desc", "Global circulation model (GCM) for planetary climate simulation using the JScience grid."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedclimatesimapp.longdesc", "Advanced climate modeling system that simulates atmospheric dynamics, heat transfer, and humidity across a planetary grid. It leverages distributed computing to process complex fluid dynamics equations at scale, with VTK export support for detailed scientific visualization."); }

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
