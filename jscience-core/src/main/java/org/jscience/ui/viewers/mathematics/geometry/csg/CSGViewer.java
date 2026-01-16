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
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import org.jscience.ui.i18n.I18n;

/**
 * CSG (Constructive Solid Geometry) Visualization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CSGViewer extends AbstractViewer {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("csg.title", "CSG Viewer");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    private Group world = new Group();
    private Box cube;
    private Sphere sphere;

    public CSGViewer() {
        // 3D Scene
        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#fdfbf7"));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-30);
        subScene.setCamera(camera);

        setupShapes();

        // Controls
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("csg.title"));
        title.getStyleClass().add("header-label");

        ComboBox<String> opBox = new ComboBox<>();
        opBox.getItems().addAll("Show Both", "A Union B", "A Subtract B", "A Intersect B");
        opBox.setValue("Show Both");
        opBox.setOnAction(e -> updateCSG(opBox.getValue()));

        Slider offsetSlider = new Slider(0, 10, 5);
        Label offsetLbl = new Label(I18n.getInstance().get("csg.offset") + " 5.0");
        offsetSlider.valueProperty().addListener((o, ov, nv) -> {
            sphere.setTranslateX(nv.doubleValue());
            offsetLbl.setText(I18n.getInstance().get("csg.offset") + " " + String.format("%.2f", nv.doubleValue()));
        });

        sidebar.getChildren().addAll(title, new Separator(), new Label(I18n.getInstance().get("csg.operation")), opBox,
                offsetLbl, offsetSlider);

        // SubScene container for BorderPane
        StackPane subSceneContainer = new StackPane(subScene);
        subScene.widthProperty().bind(subSceneContainer.widthProperty());
        subScene.heightProperty().bind(subSceneContainer.heightProperty());

        BorderPane layout = new BorderPane();
        layout.setRight(sidebar);
        layout.setCenter(subSceneContainer);
        getChildren().add(layout);

        // Rotation
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
        PhongMaterial m1 = new PhongMaterial(Color.CORNFLOWERBLUE);
        m1.setSpecularColor(Color.WHITE);
        cube.setMaterial(m1);

        sphere = new Sphere(7);
        sphere.setTranslateX(5);
        PhongMaterial m2 = new PhongMaterial(Color.ORANGERED);
        m2.setSpecularColor(Color.WHITE);
        sphere.setMaterial(m2);

        world.getChildren().addAll(cube, sphere, new AmbientLight(Color.GRAY), new PointLight(Color.WHITE) {
            {
                setTranslateZ(-20);
            }
        });
    }

    private void updateCSG(String op) {
        // Visualizing the concept using transparency and visibility
        switch (op) {
            case "Show Both" -> {
                cube.setOpacity(1.0);
                sphere.setOpacity(1.0);
                cube.setVisible(true);
                sphere.setVisible(true);
            }
            case "A Union B" -> {
                cube.setOpacity(0.8);
                sphere.setOpacity(0.8);
            }
            case "A Subtract B" -> {
                // Mockup: highlight the subtraction area
                cube.setOpacity(1.0);
                sphere.setOpacity(0.2);
            }
            case "A Intersect B" -> {
                cube.setOpacity(0.3);
                sphere.setOpacity(0.3);
                // We could add a third shape for the intersection
            }
        }
    }


    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("CSGViewer.desc", "CSGViewer description");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
