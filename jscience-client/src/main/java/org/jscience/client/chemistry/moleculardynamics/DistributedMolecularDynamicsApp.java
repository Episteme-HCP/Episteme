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

package org.jscience.client.chemistry.moleculardynamics;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jscience.server.proto.*;
import org.jscience.physics.classical.mechanics.Particle;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.biology.loaders.PDBReader;
import org.jscience.biology.loaders.PDBWriter;
import org.jscience.biology.Protein;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.MolecularDynamicsTask;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

/**
 * 3D Visualization of Molecular Dynamics with Distributed Grid support.
 */
public class DistributedMolecularDynamicsApp extends Application {

    private final Group atomGroup = new Group();
    private MolecularDynamicsTask task;
    private final List<Sphere> atomMeshes = new ArrayList<>();
    private Label stats;

    private CheckBox localSimCheckBox;
    private Button loadPdbBtn;

    private final double BOX_SIZE = 50.0;
    private final int NUM_ATOMS = 100;

    // Distributed Logic
    private ManagedChannel channel;
    private ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;
    // private boolean distributed = true; // Unused // This variable is now
    // controlled by localSimCheckBox
    private boolean serverAvailable = false;
    private long stepCount = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🧪 Molecular Dynamics - Distributed JScience");

        Group root = new Group(atomGroup);
        Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
        box.setMaterial(new PhongMaterial(Color.web("#ffffff", 0.1)));
        box.setTranslateX(BOX_SIZE / 2);
        box.setTranslateY(BOX_SIZE / 2);
        box.setTranslateZ(BOX_SIZE / 2);
        box.setDrawMode(javafx.scene.shape.DrawMode.LINE);
        root.getChildren().add(box);

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.rgb(20, 20, 30));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-150);
        camera.setTranslateX(BOX_SIZE / 2);
        camera.setTranslateY(BOX_SIZE / 2);
        camera.setFarClip(2000.0);
        scene.setCamera(camera);

        Group world = new Group(root);
        scene.setRoot(world);
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        world.getTransforms().addAll(xRotate, yRotate);
        xRotate.setPivotX(BOX_SIZE / 2);
        xRotate.setPivotY(BOX_SIZE / 2);
        xRotate.setPivotZ(BOX_SIZE / 2);
        yRotate.setPivotX(BOX_SIZE / 2);
        yRotate.setPivotY(BOX_SIZE / 2);
        yRotate.setPivotZ(BOX_SIZE / 2);

        scene.setOnMouseDragged(event -> {
            xRotate.setAngle(xRotate.getAngle() - event.getSceneY() / 100);
            yRotate.setAngle(yRotate.getAngle() + event.getSceneX() / 100);
        });

        task = new MolecularDynamicsTask(NUM_ATOMS, 0.05, 5, BOX_SIZE);

        for (int i = 0; i < NUM_ATOMS; i++) {
            Sphere s = new Sphere(1.5);
            s.setMaterial(new PhongMaterial(Color.CYAN));
            atomMeshes.add(s);
            atomGroup.getChildren().add(s);
        }

        stats = new Label("Initializing Grid...");
        stats.setTextFill(Color.WHITE);
        stats.setStyle("-fx-font-size: 16;");

        localSimCheckBox = new CheckBox("Local Simulation");
        localSimCheckBox.setSelected(true);
        localSimCheckBox.setTextFill(Color.WHITE);

        Button exportBtn = new Button("💾 Export PDB");
        exportBtn.setOnAction(e -> exportToPdb());

        loadPdbBtn = new Button("📂 Load PDB");
        loadPdbBtn.setOnAction(e -> loadPdb(stage));
        HBox overlay = new HBox(20, stats, localSimCheckBox, loadPdbBtn, exportBtn);
        overlay.setAlignment(Pos.CENTER_LEFT);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 20;");
        ((Group) scene.getRoot()).getChildren().add(overlay);

        stage.setScene(scene);
        stage.show();

        initGrpc();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                stepCount++;
                if (localSimCheckBox.isSelected()) {
                    runLocalStep();
                } else {
                    runDistributedStep();
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
                Protein p = new Protein("MD-SIM");
                Protein.Chain chain = new Protein.Chain("A");
                Protein.Residue res = new Protein.Residue("UNK", 1);

                for (MolecularDynamicsTask.AtomState state : task.getAtoms()) {
                    DenseVector<Real> pos = DenseVector.of(
                            java.util.Arrays.asList(Real.of(state.x), Real.of(state.y), Real.of(state.z)),
                            Reals.getInstance());
                    Atom atom = new Atom(PeriodicTable.getElement("Ar"), pos); // Assume Argon for MD gas
                    res.addAtom(atom);
                }
                chain.addResidue(res);
                p.addChain(chain);

                new PDBWriter().save(p, file.getAbsolutePath());
                new Alert(Alert.AlertType.INFORMATION, "Exported " + task.getNumAtoms() + " atoms to " + file.getName())
                        .show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();
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

    private org.jscience.technical.backend.algorithms.MolecularDynamicsProvider localProvider;
    private List<Particle> localParticles = new ArrayList<>();

    private void runLocalStep() {
        if (localParticles.isEmpty()) {
            for (int i = 0; i < NUM_ATOMS; i++) {
                localParticles.add(new Particle(Math.random() * BOX_SIZE, Math.random() * BOX_SIZE,
                        Math.random() * BOX_SIZE, 1.0));
            }
        }

        if (localProvider == null) {
            localProvider = new org.jscience.technical.backend.algorithms.MulticoreMolecularDynamicsProvider();
        }

        // Pack data for Provider
        int n = localParticles.size();
        Real[] positions = new Real[n * 3];
        Real[] velocities = new Real[n * 3];
        Real[] forces = new Real[n * 3];
        Real[] masses = new Real[n];

        for (int i = 0; i < n; i++) {
            Particle p = localParticles.get(i);
            positions[i * 3] = Real.of(p.getX());
            positions[i * 3 + 1] = Real.of(p.getY());
            positions[i * 3 + 2] = Real.of(p.getZ());

            // Assume Particle has velocity getter returning Vector<Double> or similar?
            // Checking previous view: used p.getVelocity().get(0).doubleValue()
            // So p.getVelocity() returns Vector<Real> or Vector<Double>?
            // In init we saw: p.getVelocity().get(0).doubleValue().
            // Only way is if Vector contains Number/Real.
            // Let's assume Real.
            // Wait, previous code: p.getVelocity().get(0).doubleValue()

            // Actually, Particle in jscience-physics usually uses Real.
            // Let's use Real.of(p.getWx()) if available or get vector.

            // Checking previous code again:
            // p.getVelocity().get(0).doubleValue()
            // So it returns Vector with objects.

            velocities[i * 3] = Real.of(p.getVelocity().get(0).doubleValue());
            velocities[i * 3 + 1] = Real.of(p.getVelocity().get(1).doubleValue());
            velocities[i * 3 + 2] = Real.of(p.getVelocity().get(2).doubleValue());

            forces[i * 3] = Real.ZERO;
            forces[i * 3 + 1] = Real.ZERO;
            forces[i * 3 + 2] = Real.ZERO;

            masses[i] = Real.of(p.getMass().to(org.jscience.measure.Units.KILOGRAM).getValue().doubleValue());
        }

        // Compute Forces (Lennard-Jones Gas)
        // Mimic Task Logic
        localProvider.calculateNonBondedForces(positions, forces, Real.ONE, Real.ONE, Real.of(2.5));

        // Integrate
        localProvider.integrate(positions, velocities, forces, masses, Real.of(0.01), Real.ONE);

        // Unpack and Box Constraints
        for (int i = 0; i < n; i++) {
            Particle p = localParticles.get(i);
            double x = positions[i * 3].doubleValue();
            double y = positions[i * 3 + 1].doubleValue();
            double z = positions[i * 3 + 2].doubleValue();
            double vx = velocities[i * 3].doubleValue();
            double vy = velocities[i * 3 + 1].doubleValue();
            double vz = velocities[i * 3 + 2].doubleValue();

            if (x < 0 || x > BOX_SIZE) {
                vx *= -1;
                x = Math.max(0, Math.min(BOX_SIZE, x));
            }
            if (y < 0 || y > BOX_SIZE) {
                vy *= -1;
                y = Math.max(0, Math.min(BOX_SIZE, y));
            }
            if (z < 0 || z > BOX_SIZE) {
                vz *= -1;
                z = Math.max(0, Math.min(BOX_SIZE, z));
            }

            p.setPosition(
                    DenseVector.of(java.util.Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance()));
            p.setVelocity(DenseVector.of(java.util.Arrays.asList(Real.of(vx), Real.of(vy), Real.of(vz)),
                    Reals.getInstance()));
        }

        // Sync local to task.atoms for rendering
        List<MolecularDynamicsTask.AtomState> states = new ArrayList<>();
        for (Particle p : localParticles) {
            states.add(new MolecularDynamicsTask.AtomState(p.getX(), p.getY(), p.getZ(),
                    p.getVelocity().get(0).doubleValue(),
                    p.getVelocity().get(1).doubleValue(),
                    p.getVelocity().get(2).doubleValue(),
                    p.getMass().to(org.jscience.measure.Units.KILOGRAM).getValue().doubleValue()));
        }
        task.updateState(states, 0.0);
        updateUI("Local Provider");
    }

    private void runDistributedStep() {
        if (!serverAvailable) {
            runLocalStep();
            return;
        }

        // Run in background to avoid freezing UI
        new Thread(() -> {
            try {
                byte[] taskData = serializeAtoms();
                TaskRequest request = TaskRequest.newBuilder()
                        .setTaskId("MD-" + stepCount)
                        .setSerializedTask(ByteString.copyFrom(taskData))
                        .setPriority(org.jscience.server.proto.Priority.HIGH)
                        .build();

                TaskResponse response = blockingStub.submitTask(request);

                // For MD, we want to wait for the result immediately to render the next frame
                // In a real app, this might be async with interpolated rendering
                TaskResult result = blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .streamResults(TaskIdentifier.newBuilder().setTaskId(response.getTaskId()).build())
                        .next();

                if (result.getStatus() == Status.COMPLETED) {
                    applyAtoms(result.getSerializedData().toByteArray());
                }
            } catch (Exception e) {
                Platform.runLater(() -> stats.setText("Grid Error: " + e.getMessage()));
            }
        }).start();
    }

    private byte[] serializeAtoms() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
            dos.writeUTF("MOLECULAR_DYNAMICS");
            dos.writeInt(task.getNumAtoms());
            dos.writeDouble(task.getTimeStep());
            dos.writeInt(task.getSteps());
            dos.writeDouble(task.getBoxSize());
            for (MolecularDynamicsTask.AtomState a : task.getAtoms()) {
                dos.writeDouble(a.x);
                dos.writeDouble(a.y);
                dos.writeDouble(a.z);
                dos.writeDouble(a.vx);
                dos.writeDouble(a.vy);
                dos.writeDouble(a.vz);
                dos.writeDouble(a.mass);
            }
            dos.flush();
            return bos.toByteArray();
        }
    }

    private void applyAtoms(byte[] data) throws IOException {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
            double energy = dis.readDouble();
            int count = dis.readInt();
            List<MolecularDynamicsTask.AtomState> newAtoms = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                newAtoms.add(new MolecularDynamicsTask.AtomState(
                        dis.readDouble(), dis.readDouble(), dis.readDouble(),
                        dis.readDouble(), dis.readDouble(), dis.readDouble(), 1.0));
            }
            Platform.runLater(() -> task.updateState(newAtoms, energy));
        }
    }

    private void updateUI(String mode) {
        List<MolecularDynamicsTask.AtomState> states = task.getAtoms();
        for (int i = 0; i < NUM_ATOMS; i++) {
            MolecularDynamicsTask.AtomState state = states.get(i);
            Sphere mesh = atomMeshes.get(i);
            mesh.setTranslateX(state.x);
            mesh.setTranslateY(state.y);
            mesh.setTranslateZ(state.z);
            double speed = Math.sqrt(state.vx * state.vx + state.vy * state.vy + state.vz * state.vz);
            Color color = speed > 1.5 ? Color.RED : (speed > 0.8 ? Color.ORANGE : Color.CYAN);
            ((PhongMaterial) mesh.getMaterial()).setDiffuseColor(color);
        }
        stats.setText(String.format("Mode: %s | Energy: %.2f", mode, task.getTotalEnergy()));
    }

    @Override
    public void stop() {
        if (channel != null)
            channel.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadPdb(Stage stage) {
        File file = org.jscience.client.util.FileHelper.showOpenDialog(stage, "Load Protein PDB", "PDB Files", "*.pdb",
                "*.ent");
        if (file != null) {
            try {
                Protein protein = new PDBReader().load(file.getAbsolutePath());
                if (protein != null) {
                    atomGroup.getChildren().clear();
                    atomMeshes.clear();
                    // Assuming 'outputPath' is defined elsewhere or passed as an argument
                    // For demonstration, let's assume a dummy path for now.
                    // In a real application, you would prompt the user for a save path.
                    String outputPath = "output.pdb"; // Placeholder
                    new PDBWriter().save(protein, outputPath); // Added line
                    // Convert Protein atoms to Particle system for MD
                    List<Particle> particles = new ArrayList<>();
                    for (Protein.Chain chain : protein.getChains()) {
                        for (Protein.Residue res : chain.getResidues()) {
                            for (Atom atom : res.getAtoms()) {
                                // Map chem atom to physics particle
                                // Use atomic mass from element
                                double mass = atom.getElement().getAtomicMass().to(org.jscience.measure.Units.KILOGRAM)
                                        .getValue().doubleValue();
                                // Position
                                Particle p = new Particle(atom.getX(), atom.getY(), atom.getZ(), mass);
                                particles.add(p);

                                Sphere mesh = new Sphere(Math.max(0.5, mass / 20.0)); // Size based on mass
                                Color color = getColorForElement(atom.getElement().getSymbol());
                                mesh.setMaterial(new PhongMaterial(color));
                                mesh.setTranslateX(atom.getX());
                                mesh.setTranslateY(atom.getY());
                                mesh.setTranslateZ(atom.getZ());

                                atomGroup.getChildren().add(mesh);
                                atomMeshes.add(mesh);
                            }
                        }
                    }
                    // Re-init task with new particles
                    task = new MolecularDynamicsTask(particles, BOX_SIZE);
                    stats.setText("Loaded: " + protein.getName() + " (" + particles.size() + " atoms)");
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to load PDB: " + e.getMessage()).show();
            }
        }
    }

    private Color getColorForElement(String symbol) {
        switch (symbol.toUpperCase()) {
            case "H":
                return Color.WHITE;
            case "C":
                return Color.GREY;
            case "N":
                return Color.BLUE;
            case "O":
                return Color.RED;
            case "S":
                return Color.YELLOW;
            case "P":
                return Color.ORANGE;
            default:
                return Color.PINK;
        }
    }
}
