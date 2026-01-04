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

package org.jscience.client.biology.dnafolding;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jscience.biology.Protein;
import org.jscience.biology.loaders.PDBLoader;
import org.jscience.biology.ProteinSequence;
import org.jscience.biology.RNAFolding;
import org.jscience.biology.structure.DnaFoldingTask;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.server.proto.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;

/**
 * DNA Folding Simulation 3D Visualization with JScience Grid support.
 */
public class DistributedDnaFoldingApp extends Application {

    private final Group moleculeGroup = new Group();
    private DnaFoldingTask task;
    private Label energyLabel;
    private Label statusLabel;
    private CheckBox distCheck;

    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    private boolean serverAvailable = false;
    private long stepCount = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🧬 DNA Folding - Distributed JScience");

        EnergyView energyView = new EnergyView();
        energyLabel = energyView.label;
        statusLabel = energyView.status;
        distCheck = energyView.checkbox;

        energyView.exportBtn.setOnAction(e -> exportToPdb());

        Scene scene = new Scene(new Group(moleculeGroup), 1024, 768, true);
        scene.setFill(Color.rgb(10, 10, 15));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-150);
        camera.setFarClip(2000.0);
        scene.setCamera(camera);

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        moleculeGroup.getTransforms().addAll(xRotate, yRotate);

        scene.setOnMouseDragged(e -> {
            xRotate.setAngle(xRotate.getAngle() - e.getSceneY() / 100);
            yRotate.setAngle(yRotate.getAngle() + e.getSceneX() / 100);
        });

        task = new DnaFoldingTask("ATGCATGCATGCATGC", 200, 300.0);
        ((Group) scene.getRoot()).getChildren().add(energyView.pane);

        stage.setScene(scene);
        stage.show();

        initGrpc();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                stepCount++;
                if (distCheck.isSelected() && serverAvailable)
                    runDistributedFolding();
                else {
                    task.run();
                    renderMolecule();
                    statusLabel.setText("Status: Local Performance");
                }
            }
        }.start();
    }

    private void exportToPdb() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDB Export");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDB Files", "*.pdb"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Protein p = new Protein("DNA-FOLD");
                Protein.Chain chain = new Protein.Chain("A");
                int resIdx = 1;
                for (DnaFoldingTask.Point3D pt : task.getFoldedStructure()) {
                    Protein.Residue res = new Protein.Residue("NUC", resIdx++);
                    DenseVector<Real> pos = DenseVector.of(
                            java.util.Arrays.asList(Real.of(pt.x()), Real.of(pt.y()), Real.of(pt.z())),
                            Reals.getInstance());
                    Atom atom = new Atom(PeriodicTable.getElement("P"), pos);
                    res.addAtom(atom);
                    chain.addResidue(res);
                }
                p.addChain(chain);
                new PDBLoader().save(p, file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, "PDB Export successful").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Export failed: " + ex.getMessage()).show();
            }
        }
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

    private void runDistributedFolding() {
        try {
            byte[] foldingData = serializeFoldingTask();
            TaskRequest request = TaskRequest.newBuilder()
                    .setTaskId("dna-fold-" + stepCount)
                    .setSerializedTask(ByteString.copyFrom(foldingData))
                    .setPriority(org.jscience.server.proto.Priority.HIGH)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            TaskResponse response = blockingStub.withDeadlineAfter(2, TimeUnit.SECONDS).submitTask(request);
            if (response.getStatus() == Status.QUEUED) {
                try {
                    TaskResult result = blockingStub.withDeadlineAfter(100, TimeUnit.MILLISECONDS)
                            .streamResults(TaskIdentifier.newBuilder().setTaskId(response.getTaskId()).build())
                            .next();
                    if (result.getStatus() == Status.COMPLETED) {
                        applyFoldingResults(result.getSerializedData().toByteArray());
                        renderMolecule();
                        statusLabel.setText("Status: Grid Computed ✅");
                        return;
                    }
                } catch (Exception e) {
                }
            }
            task.run();
            renderMolecule();
            statusLabel.setText("Status: Grid Pending ⏳");
        } catch (Exception e) {
            task.run();
            renderMolecule();
            statusLabel.setText("Status: Grid Error ❌");
        }
    }

    private byte[] serializeFoldingTask() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("DNA_FOLDING");
        dos.writeUTF(task.getSequence());
        dos.writeDouble(300.0);
        List<DnaFoldingTask.Point3D> points = task.getFoldedStructure();
        dos.writeInt(points.size());
        for (DnaFoldingTask.Point3D p : points) {
            dos.writeDouble(p.x());
            dos.writeDouble(p.y());
            dos.writeDouble(p.z());
        }
        dos.flush();
        return bos.toByteArray();
    }

    private void applyFoldingResults(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        double energy = dis.readDouble();
        int count = dis.readInt();
        List<DnaFoldingTask.Point3D> points = new ArrayList<>();
        for (int i = 0; i < count; i++)
            points.add(new DnaFoldingTask.Point3D(dis.readDouble(), dis.readDouble(), dis.readDouble()));
        Platform.runLater(() -> task.updateState(points, energy));
    }

    private void renderMolecule() {
        moleculeGroup.getChildren().clear();
        List<DnaFoldingTask.Point3D> points = task.getFoldedStructure();
        for (int i = 0; i < points.size(); i++) {
            DnaFoldingTask.Point3D p = points.get(i);
            Sphere s = new Sphere(2.0);
            s.setTranslateX(p.x());
            s.setTranslateY(p.y());
            s.setTranslateZ(p.z());
            PhongMaterial mat = new PhongMaterial(task.getSequence().charAt(i) == 'A' ? Color.RED : Color.BLUE);
            s.setMaterial(mat);
            moleculeGroup.getChildren().add(s);
            if (i > 0) {
                moleculeGroup.getChildren().add(createBond(points.get(i - 1), p));
            }
        }
        energyLabel.setText(String.format("Energy: %.2f kcal/mol", task.getFinalEnergy()));
    }

    private Node createBond(DnaFoldingTask.Point3D p1, DnaFoldingTask.Point3D p2) {
        Cylinder c = new Cylinder(0.5, p1.distance(p2));
        c.setMaterial(new PhongMaterial(Color.SILVER));
        c.setTranslateX((p1.x() + p2.x()) / 2);
        c.setTranslateY((p1.y() + p2.y()) / 2);
        c.setTranslateZ((p1.z() + p2.z()) / 2);
        return c;
    }

    static class EnergyView {
        VBox pane = new VBox(10);
        Label label = new Label();
        Label status = new Label();
        CheckBox checkbox = new CheckBox("Distributed Mode");
        Button exportBtn = new Button("💾 Export PDB");

        EnergyView() {
            pane.setStyle("-fx-background-color: rgba(30,30,50,0.8); -fx-padding: 20;");
            label.setStyle("-fx-text-fill: #4ecca3; -fx-font-size: 16;");
            status.setStyle("-fx-text-fill: white; -fx-font-size: 12;");
            checkbox.setStyle("-fx-text-fill: white;");
            checkbox.setSelected(true);
            pane.getChildren().addAll(label, checkbox, exportBtn, status);
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
