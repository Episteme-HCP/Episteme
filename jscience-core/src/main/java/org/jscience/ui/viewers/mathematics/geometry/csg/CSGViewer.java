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

package org.jscience.ui.viewers.mathematics.geometry.csg;

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.ChoiceParameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.theme.ThemeColors;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * CSG (Constructive Solid Geometry) Visualization.
 * Refactored to be 100% parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CSGViewer extends AbstractViewer {

    private Group world = new Group();
    private Box cube;
    private Sphere sphere;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public CSGViewer() {
        setupParameters();
        initUI();
    }

    private void setupParameters() {
        List<String> ops = List.of("Show Both", "A Union B", "A Subtract B", "A Intersect B");
        parameters.add(new ChoiceParameter("csg.operation", I18n.getInstance().get("csg.operation", "Boolean Operation"), ops, "Show Both", this::updateCSG));
        
        parameters.add(new NumericParameter("csg.offset", I18n.getInstance().get("csg.offset", "Offset"), 0, 10, 0.1, 5.0, v -> {
            if (sphere != null) sphere.setTranslateX(v);
        }));
    }

    private void initUI() {
        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(ThemeColors.BACKGROUND);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1); camera.setFarClip(1000.0); camera.setTranslateZ(-30);
        subScene.setCamera(camera);

        setupShapes();

        StackPane container = new StackPane(subScene);
        subScene.widthProperty().bind(container.widthProperty());
        subScene.heightProperty().bind(container.heightProperty());
        setCenter(container);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(45, Rotate.Y_AXIS);
        world.getTransforms().addAll(rotateY, rotateX);

        subScene.setOnMouseDragged(e -> {
            rotateY.setAngle(rotateY.getAngle() + 1);
            rotateX.setAngle(rotateX.getAngle() + 0.5);
        });
    }

    private void setupShapes() {
        cube = new Box(10, 10, 10);
        cube.setMaterial(new PhongMaterial(ThemeColors.SHAPE_A));
        sphere = new Sphere(7);
        sphere.setTranslateX(5);
        sphere.setMaterial(new PhongMaterial(ThemeColors.SHAPE_B));
        world.getChildren().addAll(cube, sphere, new AmbientLight(Color.GRAY), new PointLight(Color.WHITE) {{ setTranslateZ(-20); }});
    }

    private void updateCSG(String op) {
        if (cube == null || sphere == null) return;
        switch(op) {
            case "Show Both" -> { cube.setOpacity(1.0); sphere.setOpacity(1.0); }
            case "A Union B" -> { cube.setOpacity(0.8); sphere.setOpacity(0.8); }
            case "A Subtract B" -> { cube.setOpacity(1.0); sphere.setOpacity(0.2); }
            case "A Intersect B" -> { cube.setOpacity(0.3); sphere.setOpacity(0.3); }
        }
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.mathematics", "Mathematics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.csgviewer.name", "CSG Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.csgviewer.desc", "CSG operations in 3D."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.csgviewer.longdesc", "Explore boolean operations on solids."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
