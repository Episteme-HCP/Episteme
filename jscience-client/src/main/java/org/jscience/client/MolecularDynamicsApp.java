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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
import org.jscience.chemistry.molecular.MolecularDynamicsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Visualization of Molecular Dynamics.
 */
public class MolecularDynamicsApp extends Application {

    private final Group atomGroup = new Group();
    private MolecularDynamicsTask task;
    private final List<Sphere> atomMeshes = new ArrayList<>();

    // Sim Settings
    private final double BOX_SIZE = 50.0;
    private final int NUM_ATOMS = 100;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸Â§Âª Molecular Dynamics - JScience");

        // 3D Scene
        Group root = new Group(atomGroup);

        // Draw bounding box
        Box box = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
        box.setMaterial(new PhongMaterial(Color.web("#ffffff", 0.1)));
        box.setTranslateX(BOX_SIZE / 2);
        box.setTranslateY(BOX_SIZE / 2);
        box.setTranslateZ(BOX_SIZE / 2);
        box.setDrawMode(javafx.scene.shape.DrawMode.LINE);
        root.getChildren().add(box);

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.rgb(20, 20, 30));

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-150);
        camera.setTranslateX(BOX_SIZE / 2);
        camera.setTranslateY(BOX_SIZE / 2);
        camera.setFarClip(1000.0);
        scene.setCamera(camera);

        // Interaction (Rotate scene)
        Group world = new Group(root);
        scene.setRoot(world);

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        world.getTransforms().addAll(xRotate, yRotate);

        // Center rotation pivot
        xRotate.setPivotX(BOX_SIZE / 2);
        xRotate.setPivotY(BOX_SIZE / 2);
        xRotate.setPivotZ(BOX_SIZE / 2);
        yRotate.setPivotX(BOX_SIZE / 2);
        yRotate.setPivotY(BOX_SIZE / 2);
        yRotate.setPivotZ(BOX_SIZE / 2);

        scene.setOnMouseDragged(event -> {
            xRotate.setAngle(xRotate.getAngle() - event.getSceneY() / 100); // Simple sensitivity
            yRotate.setAngle(yRotate.getAngle() + event.getSceneX() / 100);
        });

        // Initialize Simulation
        task = new MolecularDynamicsTask(NUM_ATOMS, 0.05, 1, BOX_SIZE);

        // Initialize Meshes
        PhongMaterial atomMat = new PhongMaterial(Color.CYAN);
        atomMat.setSpecularColor(Color.WHITE);

        for (int i = 0; i < task.getAtoms().size(); i++) {
            Sphere s = new Sphere(1.5);
            s.setMaterial(atomMat);
            atomMeshes.add(s);
            atomGroup.getChildren().add(s);
        }

        stage.setScene(scene);
        stage.show();

        // Label overlay
        Label stats = new Label("Atoms: " + NUM_ATOMS);
        stats.setTextFill(Color.WHITE);
        stats.setStyle("-fx-font-size: 16; -fx-padding: 10;");
        ((Group) scene.getRoot()).getChildren().add(stats);

        // Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Simulate 5 steps per frame
                for (int i = 0; i < 5; i++)
                    task.run();

                // Update visuals
                List<MolecularDynamicsTask.AtomState> states = task.getAtoms();
                for (int i = 0; i < NUM_ATOMS; i++) {
                    MolecularDynamicsTask.AtomState state = states.get(i);
                    Sphere mesh = atomMeshes.get(i);
                    mesh.setTranslateX(state.x);
                    mesh.setTranslateY(state.y);
                    mesh.setTranslateZ(state.z);

                    // Simple heat coloring
                    double speed = Math.sqrt(state.vx * state.vx + state.vy * state.vy + state.vz * state.vz);
                    if (speed > 1.5)
                        ((PhongMaterial) mesh.getMaterial()).setDiffuseColor(Color.RED);
                    else if (speed > 0.8)
                        ((PhongMaterial) mesh.getMaterial()).setDiffuseColor(Color.ORANGE);
                    else
                        ((PhongMaterial) mesh.getMaterial()).setDiffuseColor(Color.CYAN);
                }

                stats.setText(String.format("Kinetic Energy: %.2f", task.getTotalEnergy()));
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


