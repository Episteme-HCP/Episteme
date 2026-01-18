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
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider;

/**
 * 3D Viewer for Magnetic Fields.
 * Refactored to be 100% parameter-based.
 */
public class MagneticFieldViewer extends org.jscience.ui.AbstractViewer {

    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.magneticfieldviewer.name", "Magnetic Field Viewer"); }

    private final Group root = new Group();
    private final Group fieldGroup = new Group();
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);

    private final MulticoreMaxwellProvider provider;
    private double time = 0;
    private double gridSpacing = 40.0;
    private boolean showVectors = false;
    private boolean showStreamlines = true;
    
    private final java.util.List<Parameter<?>> parameters = new java.util.ArrayList<>();

    public MagneticFieldViewer() {
        this(new MulticoreMaxwellProvider());
    }

    public MagneticFieldViewer(MulticoreMaxwellProvider provider) {
        this.provider = provider;
        setupParameters();
        initUI();
        
        new javafx.animation.AnimationTimer() {
            @Override public void handle(long now) { time += 0.02; updateField(); }
        }.start();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("mag.grid", I18n.getInstance().get("viewer.magneticfieldviewer.param.gridspacing", "Grid Spacing"), 10.0, 100.0, 5.0, gridSpacing, v -> gridSpacing = v));
        parameters.add(new BooleanParameter("mag.vectors", "Show Vectors", showVectors, v -> showVectors = v));
        parameters.add(new BooleanParameter("mag.lines", "Show Streamlines", showStreamlines, v -> showStreamlines = v));
    }

    private void initUI() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);
        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK); subScene.setCamera(camera);
        root.getChildren().add(fieldGroup);
        root.getTransforms().addAll(rotateX, rotateY);
        setCenter(subScene);
        
        subScene.setOnMouseDragged(e -> { rotateY.setAngle(rotateY.getAngle() + 1); rotateX.setAngle(rotateX.getAngle() + 1); });
    }

    private void updateField() {
        fieldGroup.getChildren().clear();
        if (showVectors) updateVectors();
        if (showStreamlines) updateStreamlines();
    }

    private void updateVectors() {
        int r = 4;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    double px = x * gridSpacing, py = y * gridSpacing, pz = z * gridSpacing;
                    double[][] f = provider.computeTensor(new org.jscience.mathematics.geometry.Vector4D(time, px, py, pz));
                    double bx = f[2][3], by = -f[1][3], bz = f[1][2];
                    double mag = Math.sqrt(bx*bx + by*by + bz*bz);
                    if (mag > 1e-10) addArrow(px, py, pz, bx, by, bz, mag);
                }
            }
        }
    }

    private void updateStreamlines() {
        for (org.jscience.technical.backend.algorithms.MaxwellSource s : provider.getSources()) {
            double[] pos = s.getPosition();
            for (int i=0; i<16; i++) {
                double a = i * Math.PI * 2 / 16;
                computeStreamline(pos[0]+40*Math.cos(a), pos[1]+40*Math.sin(a), pos[2], 120, true);
            }
        }
    }

    private void computeStreamline(double sx, double sy, double sz, int steps, boolean fwd) {
        double x = sx, y = sy, z = sz, step = 8.0 * (fwd ? 1 : -1);
        Point3D last = new Point3D(x, y, z);
        for (int i=0; i<steps; i++) {
            double[][] f = provider.computeTensor(new org.jscience.mathematics.geometry.Vector4D(time, x, y, z));
            double bx = f[2][3], by = -f[1][3], bz = f[1][2], mag = Math.sqrt(bx*bx + by*by + bz*bz);
            if (mag < 1e-12) break;
            x += (bx/mag)*step; y += (by/mag)*step; z += (bz/mag)*step;
            Point3D next = new Point3D(x, y, z);
            Color c = Color.hsb(Math.max(0, Math.min(360, 240 + Math.log10(mag+1e-10)*60)), 0.9, 1.0);
            addLine(last, next, 0.8, c);
            last = next;
            if (x*x+y*y+z*z > 4000000) break;
        }
    }

    private void addLine(Point3D p1, Point3D p2, double r, Color c) {
        Point3D d = p2.subtract(p1); double l = d.magnitude();
        if (l < 1e-6) return;
        Cylinder seg = new Cylinder(r, l); seg.setMaterial(new PhongMaterial(c));
        seg.setTranslateX((p1.getX()+p2.getX())/2); seg.setTranslateY((p1.getY()+p2.getY())/2); seg.setTranslateZ((p1.getZ()+p2.getZ())/2);
        Point3D y = new Point3D(0,1,0); Point3D a = y.crossProduct(d);
        if (a.magnitude() > 1e-6) { seg.setRotationAxis(a); seg.setRotate(y.angle(d)); }
        fieldGroup.getChildren().add(seg);
    }

    private void addArrow(double x, double y, double z, double vx, double vy, double vz, double m) {
        Cylinder c = new Cylinder(1.2, 30); c.setMaterial(new PhongMaterial(Color.CYAN));
        Group g = new Group(c); g.setTranslateX(x); g.setTranslateY(y); g.setTranslateZ(z);
        Point3D t = new Point3D(vx, vy, vz); Point3D ya = new Point3D(0,1,0); Point3D a = ya.crossProduct(t);
        if (a.magnitude() > 1e-6) { g.setRotationAxis(a); g.setRotate(ya.angle(t)); }
        fieldGroup.getChildren().add(g);
    }

    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.magneticfieldviewer.desc", "3D magnetic field visualization."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.magneticfieldviewer.longdesc", "Interactive 3D field lines."); }
}
