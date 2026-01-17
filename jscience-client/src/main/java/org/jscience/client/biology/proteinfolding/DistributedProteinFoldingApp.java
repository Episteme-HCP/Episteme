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

package org.jscience.client.biology.proteinfolding;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.biology.structure.ProteinFoldingTask;
import org.jscience.biology.structure.ProteinFoldingTask.Monomer;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Distributed Protein Folding Simulation using the HP Model.
 * Visualizes 3D lattice protein folding with Monte Carlo methods.
 * Offloads computation to the JScience grid.
 */
public class DistributedProteinFoldingApp extends Application implements org.jscience.ui.App {

    private final Group moleculeGroup = new Group();
    private ProteinFoldingTask task;
    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private Label statusLabel;
    private Label energyLabel;
    private TextArea sequenceInput;
    private boolean serverAvailable = false;
    private long frameCount = 0;

    private static final double MONOMER_RADIUS = 8.0;
    private static final double SPACING = 20.0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.title", "🧬 JScience - Distributed Protein Folding (HP Model)"));

        StackPane root = new StackPane();
        root.getStyleClass().add("viewer-root");

        // 3D Scene container
        Group moleculeContainer = new Group(moleculeGroup);
        SubScene subScene = new SubScene(moleculeContainer, 900, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.rgb(10, 10, 25));
        
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-400);
        subScene.setCamera(camera);

        // Control panel
        VBox controls = createControlPanel();
        StackPane.setAlignment(controls, Pos.TOP_LEFT);
        StackPane.setMargin(controls, new Insets(20));

        root.getChildren().addAll(subScene, controls);

        Scene scene = new Scene(root, 1000, 750);
        ThemeManager.getInstance().applyTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.show();

        initGrpc();
        startAnimation();
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("section-box");
        panel.setMaxWidth(300);

        Label titleLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.header", "Protein Folding Simulation"));
        titleLabel.getStyleClass().add("header-label-white");

        Label seqLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.seq_label", "HP Sequence (H=Hydrophobic, P=Polar):"));
        seqLabel.getStyleClass().add("label-white");

        sequenceInput = new TextArea("HPPHHHPHHPPHHHPPHPHPHHPH");
        sequenceInput.setPrefRowCount(2);
        sequenceInput.setWrapText(true);
        sequenceInput.getStyleClass().add("info-text-area");

        Button startBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.btn.start", "▶ Start Folding"));
        startBtn.getStyleClass().add("accent-button-green");
        startBtn.setOnAction(e -> startFolding());

        Button resetBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.btn.reset", "↻ Reset"));
        resetBtn.getStyleClass().add("accent-button-red");
        resetBtn.setOnAction(e -> resetVisualization());

        HBox buttons = new HBox(10, startBtn, resetBtn);

        energyLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.energy", "Energy: {0}", "--"));
        energyLabel.getStyleClass().add("label-green");

        statusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.ready", "Ready"));
        statusLabel.getStyleClass().add("label-muted");

        panel.getChildren().addAll(titleLabel, seqLabel, sequenceInput, buttons, energyLabel, statusLabel);
        return panel;
    }

    private void initGrpc() {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                    .usePlaintext()
                    .build();
            blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
            serverAvailable = true;
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.grid_connected", "✅ Grid Connected"));
        } catch (Exception e) {
            serverAvailable = false;
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.local_mode", "⚠️ Local Mode"));
        }
    }

    private void startFolding() {
        String sequence = sequenceInput.getText().trim().toUpperCase().replaceAll("[^HP]", "");
        if (sequence.isEmpty()) {
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.invalid_seq", "Invalid sequence"));
            return;
        }

        task = new ProteinFoldingTask(sequence, 5000, 1.0);
        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.folding", "Folding..."));

        new Thread(() -> {
            if (serverAvailable) {
                runDistributed();
            } else {
                runLocal();
            }
        }).start();
    }

    private void runLocal() {
        task.run();
        Platform.runLater(this::updateVisualization);
    }

    private void runDistributed() {
        try {
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("protein-" + System.currentTimeMillis())
                    .setSerializedTask(ByteString.copyFrom(serialize(task)))
                    .build();

            TaskResponse response = blockingStub.submitTask(request);

            TaskResult result = blockingStub.withDeadlineAfter(30, TimeUnit.SECONDS)
                    .streamResults(TaskIdentifier.newBuilder()
                            .setTaskId(response.getTaskId())
                            .build())
                    .next();

            if (result.getStatus() == Status.COMPLETED) {
                task = (ProteinFoldingTask) deserialize(result.getSerializedData().toByteArray());
                Platform.runLater(this::updateVisualization);
            } else {
                Platform.runLater(() -> statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.task_failed", "Task failed")));
            }
        } catch (Exception e) {
            Platform.runLater(() -> {
                statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.grid_error", "Grid error: {0}", e.getMessage()));
                runLocal();
            });
        }
    }

    private void updateVisualization() {
        moleculeGroup.getChildren().clear();

        List<Monomer> monomers = task.getResult();
        if (monomers == null || monomers.isEmpty()) {
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.no_result", "No result"));
            return;
        }

        // Center the molecule
        double avgX = monomers.stream().mapToDouble(m -> (double) m.x()).average().orElse(0);
        double avgY = monomers.stream().mapToDouble(m -> (double) m.y()).average().orElse(0);
        double avgZ = monomers.stream().mapToDouble(m -> (double) m.z()).average().orElse(0);

        for (int i = 0; i < monomers.size(); i++) {
            Monomer m = monomers.get(i);

            // Monomer sphere
            Sphere sphere = new Sphere(MONOMER_RADIUS);
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(m.type() == ProteinFoldingTask.ResidueType.HYDROPHOBIC
                    ? Color.ORANGERED
                    : Color.DEEPSKYBLUE);
            material.setSpecularColor(Color.WHITE);
            sphere.setMaterial(material);
            sphere.setTranslateX((m.x() - avgX) * SPACING);
            sphere.setTranslateY((m.y() - avgY) * SPACING);
            sphere.setTranslateZ((m.z() - avgZ) * SPACING);
            moleculeGroup.getChildren().add(sphere);

            // Bond cylinder to next monomer
            if (i < monomers.size() - 1) {
                Monomer next = monomers.get(i + 1);
                Cylinder bond = createBond(
                        (m.x() - avgX) * SPACING, (m.y() - avgY) * SPACING, (m.z() - avgZ) * SPACING,
                        (next.x() - avgX) * SPACING, (next.y() - avgY) * SPACING, (next.z() - avgZ) * SPACING);
                moleculeGroup.getChildren().add(bond);
            }
        }

        energyLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.energy_format", "Energy: %.1f | Monomers: %d"),
                calculateEnergy(monomers), monomers.size()));
        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.complete", "✅ Folding Complete"));
    }

    private Cylinder createBond(double x1, double y1, double z1, double x2, double y2, double z2) {
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        Cylinder cylinder = new Cylinder(2, length);
        PhongMaterial mat = new PhongMaterial(Color.LIGHTGRAY);
        cylinder.setMaterial(mat);

        cylinder.setTranslateX((x1 + x2) / 2);
        cylinder.setTranslateY((y1 + y2) / 2);
        cylinder.setTranslateZ((z1 + z2) / 2);

        // Rotate to align with bond direction
        double dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
        Rotate rotX = new Rotate(Math.toDegrees(Math.atan2(dz, dy)), Rotate.X_AXIS);
        Rotate rotZ = new Rotate(Math.toDegrees(Math.atan2(dx, Math.sqrt(dy * dy + dz * dz))), Rotate.Z_AXIS);
        cylinder.getTransforms().addAll(rotX, rotZ);

        return cylinder;
    }

    private double calculateEnergy(List<Monomer> monomers) {
        double e = 0;
        for (int i = 0; i < monomers.size(); i++) {
            for (int j = i + 2; j < monomers.size(); j++) {
                Monomer m1 = monomers.get(i);
                Monomer m2 = monomers.get(j);
                if (m1.type() == ProteinFoldingTask.ResidueType.HYDROPHOBIC
                        && m2.type() == ProteinFoldingTask.ResidueType.HYDROPHOBIC) {
                    int dist = Math.abs(m1.x() - m2.x()) + Math.abs(m1.y() - m2.y()) + Math.abs(m1.z() - m2.z());
                    if (dist == 1)
                        e -= 1.0;
                }
            }
        }
        return e;
    }

    private void resetVisualization() {
        moleculeGroup.getChildren().clear();
        task = null;
        energyLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.energy", "Energy: {0}", "--"));
        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.status.ready", "Ready"));
    }

    private void startAnimation() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                frameCount++;
                moleculeGroup.setRotationAxis(Rotate.Y_AXIS);
                moleculeGroup.setRotate(frameCount * 0.3);
            }
        }.start();
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
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.name", "Distributed Protein Folding App"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.desc", "Distributed protein folding simulation using the HP lattice model."); }

    @Override
    public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("demo.apps.distributedproteinfoldingapp.longdesc", "Predict the 3D conformation of proteins based on hydrophobic-polar (HP) interactions. This application offloads Monte Carlo optimization tasks to the JScience cluster, enabling rapid folding analysis of long amino acid sequences in a 3D lattice."); }

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
