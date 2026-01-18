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

import java.util.List;

import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.jscience.ui.i18n.I18n;

/**
 * 3D Viewer for Magnetic Fields with high-fidelity scientific visualization.
 * Features streamlines with magnitude gradients and a scientific HUD.
 * Physical source bodies (Globe, Cylinder) are handled by external SourceVisualizers.
 */
public class MagneticFieldViewer extends org.jscience.ui.AbstractViewer {

    @Override
    public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    
    @Override
    public String getName() { return I18n.getInstance().get("viewer.magneticfieldviewer.name", "Magnetic Field Viewer"); }

    private final Group root = new Group();
    private final Group vectorFieldGroup = new Group();
    private final Group streamlineGroup = new Group();
    private final Group sourceGroup = new Group();
    private final VBox hud = new VBox(5);

    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);

    private final org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider provider;
    private double time = 0;
    private javafx.animation.AnimationTimer timer;

    private final org.jscience.ui.RealParameter gridSpacingParam;
    private final org.jscience.ui.Parameter<Boolean> showVectorsParam;
    private final org.jscience.ui.Parameter<Boolean> showStreamlinesParam;
    
    private final java.util.List<SourceVisualizer> visualizers = new java.util.ArrayList<>();

    public MagneticFieldViewer() {
        this(new org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider());
    }

    public MagneticFieldViewer(org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider provider) {
        this.provider = provider;
        getStyleClass().add("viewer-root");

        this.gridSpacingParam = new org.jscience.ui.RealParameter(
                I18n.getInstance().get("viewer.magneticfieldviewer.param.gridspacing", "Grid Spacing"),
                I18n.getInstance().get("viewer.magneticfieldviewer.param.gridspacing.desc", "Spacing between field vectors"),
                10.0, 100.0, 5.0, 40.0,
                v -> updateField());

        this.showVectorsParam = new org.jscience.ui.Parameter<>(
                "Show Vectors", "Display field vectors", false, v -> updateField());
                
        this.showStreamlinesParam = new org.jscience.ui.Parameter<>(
                "Show Streamlines", "Display integrated field lines", true, v -> updateField());

        // 3D Scene Setup
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-800);

        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);

        root.getChildren().addAll(sourceGroup, vectorFieldGroup, streamlineGroup);
        root.getTransforms().addAll(rotateX, rotateY);

        // Lights
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateZ(-1500);
        root.getChildren().add(light);
        root.getChildren().add(new AmbientLight(Color.web("#333")));

        // HUD Setup
        setupHUD();

        // Mouse Controls
        subScene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        subScene.setOnMouseDragged(me -> {
            rotateY.setAngle(rotateY.getAngle() + (me.getSceneX() - mouseOldX));
            rotateX.setAngle(rotateX.getAngle() - (me.getSceneY() - mouseOldY));
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        setCenter(subScene);
        setLeft(hud);

        timer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.02;
                updateField();
            }
        };
        timer.start();
        
        updateSources();
        updateField(); 
    }

    private void setupHUD() {
        hud.setStyle("-fx-background-color: rgba(30,30,30,0.7); -fx-padding: 15; -fx-background-radius: 0 10 10 0;");
        hud.setAlignment(Pos.CENTER);
        
        Label title = new Label("Field Strength (B)");
        title.setStyle("-fx-text-fill: #CCC; -fx-font-weight: bold; -fx-font-size: 11px;");
        
        Rectangle gradientRect = new Rectangle(15, 200);
        LinearGradient lg = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.BLUE),
            new Stop(0.25, Color.CYAN),
            new Stop(0.5, Color.LIME),
            new Stop(0.75, Color.YELLOW),
            new Stop(1, Color.RED));
        gradientRect.setFill(lg);
        
        VBox scaleLabels = new VBox(170);
        scaleLabels.getChildren().addAll(new Label("High"), new Label("Low"));
        scaleLabels.setAlignment(Pos.CENTER);
        for (Node n : scaleLabels.getChildren()) if (n instanceof Label) ((Label)n).setTextFill(Color.web("#AAA"));

        hud.getChildren().addAll(title, gradientRect, scaleLabels);
    }

    public void addVisualizer(SourceVisualizer visualizer) {
        visualizers.add(visualizer);
        updateSources();
    }

    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return List.of(gridSpacingParam, showVectorsParam, showStreamlinesParam);
    }
    
    private void updateField() {
        vectorFieldGroup.getChildren().clear();
        streamlineGroup.getChildren().clear();
        
        if (showVectorsParam.getValue()) {
            updateVectors();
        }
        
        if (showStreamlinesParam.getValue()) {
            updateStreamlines();
        }
    }

    private void updateVectors() {
        int gridRange = 4;
        double gridSpacing = gridSpacingParam.getValue().doubleValue();

        for (int x = -gridRange; x <= gridRange; x++) {
            for (int y = -gridRange; y <= gridRange; y++) {
                for (int z = -gridRange; z <= gridRange; z++) {
                    double px = x * gridSpacing;
                    double py = y * gridSpacing;
                    double pz = z * gridSpacing;

                    double[][] f = provider.computeTensor(new org.jscience.mathematics.geometry.Vector4D(time, px, py, pz));
                    double bx = f[2][3], by = -f[1][3], bz = f[1][2];

                    double bMag = Math.sqrt(bx*bx + by*by + bz*bz);
                    addVector(px, py, pz, bx, by, bz, bMag);
                }
            }
        }
    }

    private void updateStreamlines() {
        for (org.jscience.technical.backend.algorithms.MaxwellSource source : provider.getSources()) {
            double[] pos = source.getPosition();
            
            for (int i = 0; i < 16; i++) {
                double angle = i * Math.PI * 2 / 16;
                double r = 40; 
                double sx = pos[0] + r * Math.cos(angle);
                double sy = pos[1] + r * Math.sin(angle);
                double sz = pos[2];
                computeStreamline(sx, sy, sz, 120, true);
                computeStreamline(sx, sy, sz, 120, false);
            }
        }
    }

    private void computeStreamline(double sx, double sy, double sz, int steps, boolean forward) {
        double x = sx, y = sy, z = sz;
        double stepSize = 8.0 * (forward ? 1 : -1);

        Point3D lastPoint = new Point3D(x, y, z);

        for (int i = 0; i < steps; i++) {
            double[][] f = provider.computeTensor(new org.jscience.mathematics.geometry.Vector4D(time, x, y, z));
            double bx = f[2][3], by = -f[1][3], bz = f[1][2];
            double bMag = Math.sqrt(bx*bx + by*by + bz*bz);
            if (bMag < 1e-12) break;

            x += (bx / bMag) * stepSize;
            y += (by / bMag) * stepSize;
            z += (bz / bMag) * stepSize;
            
            Point3D nextPoint = new Point3D(x, y, z);
            
            double logMag = Math.log10(bMag + 1e-10);
            double hue = map(Math.max(-5, Math.min(-1, logMag)), -5, -1, 240, 0); 
            Color color = Color.hsb(hue, 0.9, 1.0, 0.7);

            addLineSegment(streamlineGroup, lastPoint, nextPoint, 0.8, color);
            lastPoint = nextPoint;
            
            if (x*x + y*y + z*z > 2000*2000) break;
        }
    }

    private void addLineSegment(Group container, Point3D p1, Point3D p2, double radius, Color color) {
        Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();
        if (len < 1e-6) return;
        
        Cylinder segment = new Cylinder(radius, len);
        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(color.brighter());
        segment.setMaterial(mat);
        
        Point3D mid = p1.midpoint(p2);
        segment.setTranslateX(mid.getX());
        segment.setTranslateY(mid.getY());
        segment.setTranslateZ(mid.getZ());
        
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axis = yAxis.crossProduct(diff);
        double angle = yAxis.angle(diff);
        if (axis.magnitude() > 1e-6) {
            segment.setRotationAxis(axis);
            segment.setRotate(angle);
        }
        
        container.getChildren().add(segment);
    }

    private void updateSources() {
        sourceGroup.getChildren().clear();
        for (org.jscience.technical.backend.algorithms.MaxwellSource source : provider.getSources()) {
            for (SourceVisualizer v : visualizers) {
                if (v.supports(source)) {
                    Node node = v.getVisualRepresentation(source);
                    sourceGroup.getChildren().add(node);
                }
            }
        }
    }

    private void addVector(double x, double y, double z, double vx, double vy, double vz, double mag) {
        double length = 40;
        double vMag = Math.sqrt(vx*vx + vy*vy + vz*vz);
        if (vMag < 1e-10) return;
        
        double logMag = Math.log10(mag + 1e-10);
        double hue = map(Math.max(-5, Math.min(-1, logMag)), -5, -1, 240, 0); 
        Color color = Color.hsb(hue, 0.8, 1.0);

        PhongMaterial mat = new PhongMaterial(color);
        double shaftLen = length * 0.7;
        Cylinder shaft = new Cylinder(1.2, shaftLen);
        shaft.setMaterial(mat);
        shaft.setTranslateY(-shaftLen / 2);

        javafx.scene.shape.MeshView head = createCone(4, length * 0.3, color);
        head.setTranslateY(-shaftLen - (length * 0.3) / 2);
        
        Group arrow = new Group(shaft, head);
        arrow.setTranslateX(x); arrow.setTranslateY(y); arrow.setTranslateZ(z);

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D target = new Point3D(vx/vMag, vy/vMag, vz/vMag);
        Point3D axis = yAxis.crossProduct(target);
        if (axis.magnitude() > 1e-6) {
            arrow.setRotationAxis(axis);
            arrow.setRotate(yAxis.angle(target));
        }

        vectorFieldGroup.getChildren().add(arrow);
    }

    private javafx.scene.shape.MeshView createCone(double radius, double height, Color color) {
        javafx.scene.shape.TriangleMesh mesh = new javafx.scene.shape.TriangleMesh();
        mesh.getPoints().addAll(0, (float)(-height/2), 0, 0, (float)(height/2), (float)radius, (float)(radius*0.866), (float)(height/2), (float)(-0.5*radius), (float)(-0.866*radius), (float)(height/2), (float)(-0.5*radius));
        mesh.getTexCoords().addAll(0, 0);
        mesh.getFaces().addAll(0,0,1,0,2,0, 0,0,2,0,3,0, 0,0,3,0,1,0, 1,0,3,0,2,0);
        javafx.scene.shape.MeshView meshView = new javafx.scene.shape.MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(color));
        return meshView;
    }
    
    private double map(double val, double min1, double max1, double min2, double max2) {
        return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
    }

    @Override
    public String getDescription() { return I18n.getInstance().get("viewer.magneticfieldviewer.desc", "3D vector field visualization of magnetic fields."); }

    @Override
    public String getLongDescription() { 
        return I18n.getInstance().get("viewer.magneticfieldviewer.longdesc", "Interactive high-fidelity 3D visualization of magnetic field vectors and streamlines."); 
    }
}

