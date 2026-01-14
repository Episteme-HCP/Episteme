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

package org.jscience.apps.physics.spintronics.viewer;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import org.jscience.mathematics.numbers.real.Real;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX 3D implementation of Spintronic3DViewer.
 */
public class JavaFXSpintronic3DViewer implements Spintronic3DViewer {

    private final Group root = new Group();
    private final SubScene subScene;
    private final Map<String, Group> arrows = new HashMap<>();

    public JavaFXSpintronic3DViewer(double width, double height) {
        subScene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#1a1a2e"));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-600);
        camera.setNearClip(0.1);
        camera.setFarClip(3000.0);
        subScene.setCamera(camera);

        // Lights
        AmbientLight ambient = new AmbientLight(Color.color(0.4, 0.4, 0.4));
        PointLight point = new PointLight(Color.color(0.9, 0.9, 0.9));
        point.setTranslateZ(-400);
        point.setTranslateY(-300);
        root.getChildren().addAll(ambient, point);

        // Perspective tilt
        root.setRotationAxis(Rotate.Y_AXIS);
        root.setRotate(25);
    }

    @Override
    public void addLayer(double y, double thickness, String color, String label) {
        Box layer = new Box(250, thickness * 1e9 * 4, 200); // Scale nm to display units
        layer.setTranslateY(y);
        layer.setMaterial(new PhongMaterial(Color.web(color)));
        root.getChildren().add(layer);
    }

    @Override
    public void addMagnetizationArrow(double x, double y, double z, Real mx, Real my, Real mz, String id) {
        Group arrow = createArrow(Color.YELLOW);
        arrow.setTranslateX(x);
        arrow.setTranslateY(y);
        arrow.setTranslateZ(z);
        updateArrowOrientation(arrow, mx, my, mz);
        arrows.put(id, arrow);
        root.getChildren().add(arrow);
    }

    @Override
    public void updateMagnetizationArrow(String id, Real mx, Real my, Real mz) {
        Group arrow = arrows.get(id);
        if (arrow != null) {
            updateArrowOrientation(arrow, mx, my, mz);
        }
    }

    private void updateArrowOrientation(Group arrow, Real mx, Real my, Real mz) {
        double theta = Math.toDegrees(Math.atan2(my.doubleValue(), mx.doubleValue()));
        double phi = Math.toDegrees(Math.acos(mz.doubleValue()));
        arrow.getTransforms().clear();
        arrow.getTransforms().addAll(
            new Rotate(theta, Rotate.Z_AXIS),
            new Rotate(phi, Rotate.Y_AXIS)
        );
    }

    private Group createArrow(Color color) {
        Group arrow = new Group();
        Cylinder shaft = new Cylinder(3, 50);
        shaft.setMaterial(new PhongMaterial(color));
        shaft.setRotationAxis(Rotate.Z_AXIS);
        shaft.setRotate(90);

        Sphere head = new Sphere(8);
        head.setTranslateX(25);
        head.setMaterial(new PhongMaterial(color));

        arrow.getChildren().addAll(shaft, head);
        return arrow;
    }

    @Override
    public void addSpinCurrentFlow(double fromY, double toY, int density) {
        double step = (toY - fromY) / density;
        for (int i = 0; i < density; i++) {
            double y = fromY + i * step;
            Sphere spin = new Sphere(3);
            spin.setTranslateY(y);
            spin.setTranslateX(-120 + (i % 5) * 10);
            spin.setMaterial(new PhongMaterial(Color.CYAN));
            root.getChildren().add(spin);
        }
    }

    @Override
    public void clear() {
        root.getChildren().removeIf(n -> !(n instanceof AmbientLight || n instanceof PointLight));
        arrows.clear();
    }

    @Override
    public Object getComponent() {
        return subScene;
    }

    @Override
    public void setViewAngle(double azimuth, double elevation) {
        root.setRotate(azimuth);
    }
}
