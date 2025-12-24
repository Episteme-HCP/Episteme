/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.geometry.csg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * CSG (Constructive Solid Geometry) Visualization.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class CSGViewer extends Application {

    private Group world = new Group();
    private Box cube;
    private Sphere sphere;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // 3D Scene
        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#1e1e1e"));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-30);
        subScene.setCamera(camera);

        setupShapes();

        // Controls
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Label title = new Label("CSG Operations");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        ComboBox<String> opBox = new ComboBox<>();
        opBox.getItems().addAll("Show Both", "A Union B", "A Subtract B", "A Intersect B");
        opBox.setValue("Show Both");
        opBox.setOnAction(e -> updateCSG(opBox.getValue()));

        Slider offsetSlider = new Slider(0, 10, 5);
        Label offsetLbl = new Label("Shape Offset: 5.0");
        offsetSlider.valueProperty().addListener((o, ov, nv) -> {
            sphere.setTranslateX(nv.doubleValue());
            offsetLbl.setText("Shape Offset: " + String.format("%.2f", nv.doubleValue()));
        });

        sidebar.getChildren().addAll(title, new Separator(), new Label("Operation:"), opBox, offsetLbl, offsetSlider);
        root.setRight(sidebar);
        root.setCenter(subScene);

        // Rotation
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(45, Rotate.Y_AXIS);
        world.getTransforms().addAll(rotateY, rotateX);

        subScene.setOnMouseDragged(e -> {
            rotateY.setAngle(rotateY.getAngle() + 1);
            rotateX.setAngle(rotateX.getAngle() + 0.5);
        });

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("JScience - Constructive Solid Geometry");
        stage.setScene(scene);
        stage.show();
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

    public static void show(Stage stage) {
        new CSGViewer().start(stage);
    }
}
