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

package org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * 3D Viewer for Magnetic Fields in Spintronics.
 * Visualizes B-field vectors with color gradients and direction.
 */
public class MagneticFieldViewer extends org.jscience.ui.AbstractViewer {

    @Override
    public String getCategory() { return "Physics"; }
    
    @Override
    public String getName() { return "Magnetic Field Viewer"; }

    private final Group root = new Group();
    private final Group vectorFieldGroup = new Group();
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);

    public MagneticFieldViewer() {
        this.setStyle("-fx-background-color: #1a1a2e;"); // Dark background

        // 3D Scene setup
        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#1a1a2e"));
        
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);
        camera.setNearClip(0.1);
        camera.setFarClip(5000.0);
        subScene.setCamera(camera);

        // Lights
        AmbientLight ambient = new AmbientLight(Color.rgb(50, 50, 50));
        PointLight point = new PointLight(Color.WHITE);
        point.setTranslateZ(-500);
        point.setTranslateY(-500);
        root.getChildren().clear(); // Clear before adding to avoid duplicates if called multiple times
        root.getChildren().addAll(ambient, point, vectorFieldGroup);

        // Controls
        root.getTransforms().addAll(rotateY, rotateX);

        // Mouse Rotation
        subScene.setOnMousePressed((MouseEvent event) -> {
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        subScene.setOnMouseDragged((MouseEvent event) -> {
            rotateY.setAngle(rotateY.getAngle() + (event.getSceneX() - mouseOldX));
            rotateX.setAngle(rotateX.getAngle() - (event.getSceneY() - mouseOldY));
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        // Generate Field
        generateDipoleField();
        
        // Add a representation of the magnet (Ring)
        Cylinder magnet = new Cylinder(100, 10);
        magnet.setRotationAxis(Rotate.X_AXIS);
        magnet.setRotate(90); // Flat on XZ plane
        magnet.setMaterial(new PhongMaterial(Color.SILVER));
        root.getChildren().add(magnet);

        this.setCenter(subScene);
    }

    // Deprecated method for backward compatibility if needed, but we will update caller
    public javafx.scene.Parent createContent() {
        return this;
    }

    private void generateDipoleField() {
        vectorFieldGroup.getChildren().clear();
        double gridSpacing = 40;
        int gridRange = 4;

        for (int x = -gridRange; x <= gridRange; x++) {
            for (int y = -gridRange; y <= gridRange; y++) {
                for (int z = -gridRange; z <= gridRange; z++) {
                    if (x == 0 && z == 0 && Math.abs(y) < 1) continue; // Skip inside magnet

                    double px = x * gridSpacing;
                    double py = y * gridSpacing;
                    double pz = z * gridSpacing;

                    // Compute B-field for a dipole at origin aligned with Y axis
                    // B = (3(m.r)r - m*r^2) / r^5
                    // m = (0, 100000, 0)
                    double mx = 0, my = 500000, mz = 0;
                    double r2 = px*px + py*py + pz*pz;
                    double r = Math.sqrt(r2);
                    double mr = mx*px + my*py + mz*pz; // dot product

                    double bx = (3 * mr * px - mx * r2) / Math.pow(r, 5);
                    double by = (3 * mr * py - my * r2) / Math.pow(r, 5);
                    double bz = (3 * mr * pz - mz * r2) / Math.pow(r, 5);

                    double bMag = Math.sqrt(bx*bx + by*by + bz*bz);
                    
                    // Add Vector
                    addVector(px, py, pz, bx, by, bz, bMag);
                }
            }
        }
    }

    private void addVector(double x, double y, double z, double vx, double vy, double vz, double mag) {
        double length = 30; // Fixed visual length
        
        // Normalize vector
        double vMag = Math.sqrt(vx*vx + vy*vy + vz*vz);
        if (vMag < 1e-10) return;
        double nx = vx / vMag;
        double ny = vy / vMag;
        double nz = vz / vMag;

        // Color map based on magnitude
        // Logarithmic scale for better dynamic range visualization
        double logMag = Math.log10(mag + 1);
        double minLog = -5; // Tuned for this simulation
        double maxLog = -2;
        double hue = map(logMag, minLog, maxLog, 240, 0); // Blue (weak) to Red (strong)
        Color color = Color.hsb(hue, 1.0, 1.0);

        // Cylinder (Shaft)
        Cylinder shaft = new Cylinder(1, length * 0.8);
        shaft.setMaterial(new PhongMaterial(color));

        // Cone (Head) - JavaFX doesn't have Cone, use Mesh or scaled Cylinder/Pyramid
        // Using a Cylinder for simplicity in this demo or a MeshView for true cone
        // Lets construct a simple arrow group
        Group arrow = new Group();
        arrow.getChildren().add(shaft);
        
        // Transform
        arrow.setTranslateX(x);
        arrow.setTranslateY(y);
        arrow.setTranslateZ(z);

        // Orientation
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D target = new Point3D(nx, ny, nz);
        Point3D axis = yAxis.crossProduct(target);
        double angle = yAxis.angle(target);
        
        arrow.setRotationAxis(axis);
        arrow.setRotate(angle);

        vectorFieldGroup.getChildren().add(arrow);
    }
    
    private double map(double val, double min1, double max1, double min2, double max2) {
        return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
    }
    

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("MagneticFieldViewer.desc", "MagneticFieldViewer description");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}

