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
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import org.jscience.biology.structure.DnaFoldingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Visualization of DNA Folding.
 */
public class DnaFoldingApp extends Application {

    private Group moleculeGroup;
    private DnaFoldingTask task;
    private Label energyLabel;
    private boolean playing = false;
    private final double rotateSpeed = 0.5;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ã°Å¸Â§Â¬ DNA Folding Simulation - JScience");

        // 3D Scene
        moleculeGroup = new Group();
        Group root3D = new Group(moleculeGroup);
        SubScene3D subScene = new SubScene3D(root3D, 800, 600, true, Color.rgb(20, 20, 30));

        // UI Controls
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.setStyle("-fx-background-color: #1a1a2e;");
        controls.setPrefWidth(250);

        Label title = new Label("DNA Folding");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4ecca3;");

        TextField seqField = new TextField("AGCTAGCTAGCTAGCTAGCTAGCT");
        seqField.setPromptText("DNA Sequence");

        Slider tempSlider = new Slider(0.1, 10.0, 1.0);
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);

        energyLabel = new Label("Energy: 0.0");
        energyLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #aaa;");

        Button foldBtn = new Button("Ã°Å¸Â§Â¬ Fold on Grid");
        foldBtn.setStyle("-fx-background-color: #4ecca3; -fx-text-fill: #1a1a2e; -fx-font-weight: bold;");
        foldBtn.setOnAction(e -> startFolding(seqField.getText(), tempSlider.getValue()));

        CheckBox rotateCheck = new CheckBox("Auto Rotate");
        rotateCheck.setSelected(true);
        rotateCheck.setStyle("-fx-text-fill: #aaa;");

        controls.getChildren().addAll(
                title,
                new Label("Sequence:"), seqField,
                new Label("Temperature:"), tempSlider,
                energyLabel,
                new Separator(),
                foldBtn,
                rotateCheck);
        controls.getChildren().forEach(n -> {
            if (n instanceof Label && n != title && n != energyLabel)
                ((Label) n).setStyle("-fx-text-fill: #aaa;");
        });

        BorderPane root = new BorderPane();
        root.setCenter(subScene.getSubScene());
        root.setRight(controls);

        Scene scene = new Scene(root, 1050, 600);
        stage.setScene(scene);
        stage.show();

        // Initial molecule
        updateVisualization(createLinearHelix(20));

        // Animation Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (rotateCheck.isSelected()) {
                    moleculeGroup.setRotate(moleculeGroup.getRotate() + rotateSpeed);
                }

                if (playing && task != null) {
                    // Simulate one step of "receiving updates from grid"
                    task.run(); // In reality this would be correctAsync
                    updateVisualization(task.getFoldedStructure());
                    energyLabel.setText(String.format("Energy: %.2f", task.getFinalEnergy()));
                }
            }
        }.start();
    }

    private void startFolding(String sequence, double temp) {
        task = new DnaFoldingTask(sequence, 1, temp); // 1 iteration per frame for anim
        task.run(); // Initial state
        playing = true;
    }

    private List<DnaFoldingTask.Point3D> createLinearHelix(int length) {
        List<DnaFoldingTask.Point3D> points = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            points.add(new DnaFoldingTask.Point3D(i * 3.4, Math.sin(i) * 2, Math.cos(i) * 2));
        }
        return points;
    }

    private void updateVisualization(List<DnaFoldingTask.Point3D> points) {
        moleculeGroup.getChildren().clear();

        if (points == null || points.isEmpty())
            return;

        // Draw Backbone
        PhongMaterial backboneMat = new PhongMaterial(Color.LIGHTGREY);
        PhongMaterial baseMat = new PhongMaterial();

        for (int i = 0; i < points.size(); i++) {
            DnaFoldingTask.Point3D p = points.get(i);

            // Atom
            Sphere atom = new Sphere(1.0);
            atom.setTranslateX(p.x());
            atom.setTranslateY(p.y());
            atom.setTranslateZ(p.z());

            // Color based on base (pseudo)
            switch (i % 4) {
                case 0:
                    baseMat.setDiffuseColor(Color.RED);
                    break; // A
                case 1:
                    baseMat.setDiffuseColor(Color.BLUE);
                    break; // T
                case 2:
                    baseMat.setDiffuseColor(Color.GREEN);
                    break; // C
                case 3:
                    baseMat.setDiffuseColor(Color.YELLOW);
                    break; // G
            }
            atom.setMaterial(baseMat);
            moleculeGroup.getChildren().add(atom);

            // Bond to next
            if (i < points.size() - 1) {
                DnaFoldingTask.Point3D next = points.get(i + 1);
                Cylinder bond = createBond(p, next);
                bond.setMaterial(backboneMat);
                moleculeGroup.getChildren().add(bond);
            }
        }
    }

    private Cylinder createBond(DnaFoldingTask.Point3D p1, DnaFoldingTask.Point3D p2) {
        DnaFoldingTask.Point3D diff = new DnaFoldingTask.Point3D(p2.x() - p1.x(), p2.y() - p1.y(), p2.z() - p1.z());
        double height = Math.sqrt(diff.x() * diff.x() + diff.y() * diff.y() + diff.z() * diff.z());

        Cylinder cylinder = new Cylinder(0.3, height);

        // Midpoint
        cylinder.setTranslateX(p1.x() + diff.x() / 2);
        cylinder.setTranslateY(p1.y() + diff.y() / 2);
        cylinder.setTranslateZ(p1.z() + diff.z() / 2);

        // Orientation (simplified for 3D alignment)
        // Correct 3D cylinder rotation is complex, this is approximate for demo

        // Note: Correct 3D cylinder rotation is complex, this is approximate for demo

        return cylinder;
    }

    /**
     * Helper for JavaFX 3D setup.
     */
    private static class SubScene3D {
        private final javafx.scene.SubScene subScene;
        private final PerspectiveCamera camera;

        public SubScene3D(Group root, double width, double height, boolean depthBuffer, Color fill) {
            this.subScene = new javafx.scene.SubScene(root, width, height, depthBuffer,
                    javafx.scene.SceneAntialiasing.BALANCED);
            this.subScene.setFill(fill);

            this.camera = new PerspectiveCamera(true);
            camera.setTranslateZ(-100);
            camera.setNearClip(0.1);
            camera.setFarClip(1000.0);

            subScene.setCamera(camera);
        }

        public javafx.scene.SubScene getSubScene() {
            return subScene;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


