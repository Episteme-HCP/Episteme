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

package org.jscience.ui.computing.simulation;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jscience.computing.simulation.SpringMassSystem;

/**
 * 3D Cloth Simulation using Spring-Mass System.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ClothSimulationViewer extends Application {

    private final int gridWidth = 20;
    private final int gridHeight = 20;
    private final double spacing = 1.0;
    private SpringMassSystem system;
    private TriangleMesh mesh;
    private MeshView meshView;
    private AnimationTimer timer;
    private boolean running = true;

    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // 3D Scene
        Group world = new Group();
        setupSystem();
        setupMesh(world);

        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#1e1e1e"));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-40);
        camera.setTranslateY(-10);
        subScene.setCamera(camera);

        // Camera
        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(camera);
        // Initial camera position relative to pivot
        camera.setTranslateZ(-80);
        camera.setTranslateY(-20);

        world.getChildren().add(cameraGroup);
        // Rotations apply to the camera group (orbiting center)
        cameraGroup.getTransforms().addAll(rotateY, rotateX);

        // Controls
        VBox controls = new VBox(10);
        controls.setPadding(new javafx.geometry.Insets(10));
        controls.getStyleClass().add("dark-viewer-controls");

        Label title = new Label("3D Cloth Simulation");
        title.getStyleClass().add("dark-label");

        Button resetBtn = new Button("Reset Simulation");
        resetBtn.setOnAction(e -> {
            setupSystem();
            updateMesh();
        });

        ToggleButton pauseBtn = new ToggleButton("Pause / Resume");
        pauseBtn.setSelected(false);
        pauseBtn.selectedProperty().addListener((o, ov, nv) -> running = !nv);

        Slider gravitySlider = new Slider(0, 20, 9.81);
        Label gravLabel = new Label("Gravity: 9.81");
        gravitySlider.valueProperty().addListener((o, ov, nv) -> {
            system.setGravity(0, -nv.doubleValue(), 0);
            gravLabel.setText("Gravity: " + String.format("%.2f", nv.doubleValue()));
        });

        Slider dampingSlider = new Slider(0, 1, 0.1);
        Label dampLabel = new Label("Damping: 0.1");
        dampingSlider.valueProperty().addListener((o, ov, nv) -> {
            system.setDamping(nv.doubleValue());
            dampLabel.setText("Damping: " + String.format("%.2f", nv.doubleValue()));
        });

        CheckBox wireframeCheck = new CheckBox("Wireframe Mode");
        wireframeCheck.selectedProperty().addListener((o, ov, nv) -> {
            meshView.setDrawMode(nv ? DrawMode.LINE : DrawMode.FILL);
        });

        controls.getChildren().addAll(title, new Separator(), resetBtn, pauseBtn,
                gravLabel, gravitySlider, dampLabel, dampingSlider,
                wireframeCheck);

        root.setCenter(subScene);
        root.setRight(controls);

        // Interaction
        subScene.setOnMousePressed(event -> {
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });
        subScene.setOnMouseDragged(event -> {
            rotateY.setAngle(rotateY.getAngle() + (event.getSceneX() - mouseOldX));
            rotateX.setAngle(rotateX.getAngle() - (event.getSceneY() - mouseOldY));
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });
        // Zoom on scroll
        subScene.setOnScroll(event -> {
            double z = camera.getTranslateZ();
            double newZ = z + event.getDeltaY() * 0.1;
            camera.setTranslateZ(Math.max(-200, Math.min(-5, newZ)));
        });

        // Animation
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    // Small steps for stability
                    for (int i = 0; i < 5; i++) {
                        system.step(0.01);
                    }
                    updateMesh();
                }
            }
        };
        timer.start();

        Scene scene = new Scene(root, 1000, 700);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.cloth"));
        stage.setScene(scene);
        stage.show();
    }

    private void setupSystem() {
        int n = gridWidth * gridHeight;
        system = new SpringMassSystem(n);
        double k = 500.0;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                int i = y * gridWidth + x;
                double px = (x - gridWidth / 2.0) * spacing;
                double py = 0;
                double pz = (y - gridHeight / 2.0) * spacing;
                system.setParticle(i, px, py, pz, 1.0);

                // Fixed top row
                if (y == 0) {
                    system.fixParticle(i);
                }

                // Structural springs
                if (x > 0)
                    system.addSpring(i, i - 1, k, spacing);
                if (y > 0)
                    system.addSpring(i, i - gridWidth, k, spacing);

                // Shear springs
                if (x > 0 && y > 0) {
                    system.addSpring(i, i - gridWidth - 1, k, spacing * 1.414);
                    system.addSpring(i - 1, i - gridWidth, k, spacing * 1.414);
                }
            }
        }
    }

    private void setupMesh(Group world) {
        mesh = new TriangleMesh();

        // Add texture coordinates (single point as we use solid color)
        mesh.getTexCoords().addAll(0, 0);

        // Initial points
        updateMeshPoints();

        // Faces
        for (int y = 0; y < gridHeight - 1; y++) {
            for (int x = 0; x < gridWidth - 1; x++) {
                int p00 = y * gridWidth + x;
                int p01 = p00 + 1;
                int p10 = p00 + gridWidth;
                int p11 = p10 + 1;

                mesh.getFaces().addAll(p00, 0, p10, 0, p01, 0);
                mesh.getFaces().addAll(p01, 0, p10, 0, p11, 0);
            }
        }

        meshView = new MeshView(mesh);
        meshView.setDrawMode(DrawMode.FILL);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTBLUE);
        material.setSpecularColor(Color.WHITE);
        meshView.setMaterial(material);

        world.getChildren().add(meshView);

        // Ambient Light
        AmbientLight light = new AmbientLight(Color.rgb(100, 100, 100));
        world.getChildren().add(light);

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateY(-20);
        pointLight.setTranslateZ(-20);
        world.getChildren().add(pointLight);
    }

    private void updateMesh() {
        updateMeshPoints();
    }

    private void updateMeshPoints() {
        double[] pos = system.getPositions();
        float[] points = new float[pos.length];
        for (int i = 0; i < pos.length; i++) {
            // Invert Y for JavaFX coordinate system (Y is down)
            if (i % 3 == 1)
                points[i] = (float) -pos[i];
            else
                points[i] = (float) pos[i];
        }
        mesh.getPoints().setAll(points);
    }

    public static void show(Stage stage) {
        new ClothSimulationViewer().start(stage);
    }
}


