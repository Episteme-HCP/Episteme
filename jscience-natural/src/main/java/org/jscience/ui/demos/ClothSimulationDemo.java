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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import org.jscience.computing.simulation.SpringMassSystem;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

/**
 * Cloth Simulation Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ClothSimulationDemo extends AbstractSimulationDemo {

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getName() { return I18n.getInstance().get("ClothSimulation.title", "Cloth Simulation"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("ClothSimulation.desc", "3D Cloth Simulation using Spring-Mass System"); }

    @Override
    public String getLongDescription() { 
        return "Simulates a piece of cloth using a grid of particles and springs (Spring-Mass System). Features gravity, damping, and interaction."; 
    }

    @Override
    public Node createViewerNode() {
        return new InternalClothViewer();
    }

    private static class InternalClothViewer extends AbstractViewer implements Simulatable {

        private final int gridWidth = 20, gridHeight = 20;
        private final double spacing = 1.0;
        private SpringMassSystem system;
        private TriangleMesh mesh;
        private MeshView meshView;
        private boolean running = false; // Start paused
        private int iterations = 5;

        private double mouseOldX, mouseOldY;
        private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        private SubScene subScene;

        public InternalClothViewer() {
            // 3D World
            Group world = new Group();
            setupSystem();
            setupMesh(world);
            
            // Camera
            PerspectiveCamera camera = new PerspectiveCamera(true);
            camera.setNearClip(0.1); camera.setFarClip(1000.0);
            camera.setTranslateZ(-80); camera.setTranslateY(-20); // Initial pos
            
            Group cameraGroup = new Group(camera);
            cameraGroup.getTransforms().addAll(rotateY, rotateX);
            world.getChildren().add(cameraGroup);
            
            // SubScene
            subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
            subScene.setFill(Color.web("#1e1e1e"));
            subScene.setCamera(camera);
            
            setCenter(subScene);
            setRight(createSidebar());

            // Interaction
            subScene.setOnMousePressed(e -> { mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY(); });
            subScene.setOnMouseDragged(e -> {
                rotateY.setAngle(rotateY.getAngle() + (e.getSceneX() - mouseOldX));
                rotateX.setAngle(rotateX.getAngle() - (e.getSceneY() - mouseOldY));
                mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY();
            });
            subScene.setOnScroll(e -> {
                double z = camera.getTranslateZ();
                camera.setTranslateZ(Math.max(-200, Math.min(-5, z + e.getDeltaY() * 0.1)));
            });

            // Resize
            widthProperty().addListener(o -> { if(getWidth()>0) subScene.setWidth(getWidth() - 200); });
            heightProperty().addListener(o -> { if(getHeight()>0) subScene.setHeight(getHeight()); });

            // Timer
            new AnimationTimer() {
                @Override public void handle(long now) {
                    if (running) {
                        for (int i = 0; i < iterations; i++) system.step(0.01);
                        updateMesh();
                    }
                }
            }.start();
        }

        private VBox createSidebar() {
            VBox box = new VBox(10);
            box.setPadding(new Insets(10));
            box.setPrefWidth(200);
            box.getStyleClass().add("dark-viewer-sidebar");

            Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.clothsimulation.settings", "Settings"));
            title.getStyleClass().add("dark-header");
            
            Slider gravSlider = new Slider(0, 20, 9.81);
            Label gravLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.clothsimulation.gravity.981", "Gravity: 9.81"));
            gravSlider.valueProperty().addListener((o,ov,nv) -> {
                system.setGravity(0, -nv.doubleValue(), 0);
                gravLabel.setText(String.format("Gravity: %.2f", nv.doubleValue()));
            });
            
            Slider dampSlider = new Slider(0, 1, 0.1);
            Label dampLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.clothsimulation.damping.010", "Damping: 0.10"));
            dampSlider.valueProperty().addListener((o,ov,nv) -> {
                system.setDamping(nv.doubleValue());
                dampLabel.setText(String.format("Damping: %.2f", nv.doubleValue()));
            });

            CheckBox wireCheck = new CheckBox(org.jscience.ui.i18n.I18n.getInstance().get("generated.clothsimulation.wireframe", "Wireframe"));
            wireCheck.selectedProperty().addListener((o,ov,nv) -> meshView.setDrawMode(nv ? DrawMode.LINE : DrawMode.FILL));
            
            box.getChildren().addAll(title, new Separator(), gravLabel, gravSlider, dampLabel, dampSlider, new Separator(), wireCheck);
            return box;
        }

        private void setupSystem() {
            system = new SpringMassSystem(gridWidth * gridHeight);
            double k = 500.0;
            for (int y = 0; y < gridHeight; y++) {
                for (int x = 0; x < gridWidth; x++) {
                    int i = y * gridWidth + x;
                    system.setParticle(i, (x - gridWidth/2.0)*spacing, 0, (y - gridHeight/2.0)*spacing, 1.0);
                    if (y == 0) system.fixParticle(i); // Fix top row
                    if (x > 0) system.addSpring(i, i - 1, k, spacing);
                    if (y > 0) system.addSpring(i, i - gridWidth, k, spacing);
                    if (x > 0 && y > 0) {
                        system.addSpring(i, i - gridWidth - 1, k, spacing * 1.414);
                        system.addSpring(i - 1, i - gridWidth, k, spacing * 1.414);
                    }
                }
            }
        }
        
        private void setupMesh(Group world) {
            mesh = new TriangleMesh();
            mesh.getTexCoords().addAll(0, 0);
            updateMeshPoints();
            for (int y = 0; y < gridHeight - 1; y++) {
                for (int x = 0; x < gridWidth - 1; x++) {
                    int p00 = y * gridWidth + x, p01 = p00 + 1, p10 = p00 + gridWidth, p11 = p10 + 1;
                    mesh.getFaces().addAll(p00,0, p10,0, p01,0, p01,0, p10,0, p11,0);
                }
            }
            meshView = new MeshView(mesh);
            meshView.setDrawMode(DrawMode.FILL);
            PhongMaterial mat = new PhongMaterial(Color.LIGHTBLUE); mat.setSpecularColor(Color.WHITE);
            meshView.setMaterial(mat);
            
            world.getChildren().add(meshView);
            world.getChildren().add(new AmbientLight(Color.rgb(100, 100, 100)));
            PointLight pl = new PointLight(Color.WHITE); pl.setTranslateY(-20); pl.setTranslateZ(-20);
            world.getChildren().add(pl);
        }
        
        private void updateMesh() {
            updateMeshPoints();
        }
        
        private void updateMeshPoints() {
            double[] pos = system.getPositions();
            float[] points = new float[pos.length];
            for (int i = 0; i < pos.length; i++) points[i] = (float) (i % 3 == 1 ? -pos[i] : pos[i]); // Invert Y
            mesh.getPoints().setAll(points);
        }

        @Override public void play() { running = true; }
        @Override public void pause() { running = false; }
        @Override public void stop() { running = false; setupSystem(); updateMesh(); }
        @Override public void step() { for(int i=0; i<iterations; i++) system.step(0.01); updateMesh(); }
        @Override public void setSpeed(double s) { iterations = Math.max(1, (int)(s * 5)); }
        @Override public boolean isPlaying() { return running; }
        
        @Override public String getName() { return "Cloth Viewer"; }
    @Override
    public String getCategory() { return "Physics"; }
    
        @Override
        public String getDescription() { return "InternalClothViewer Internal Viewer"; }

        @Override
}

    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}