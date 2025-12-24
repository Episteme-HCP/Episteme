/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.anatomy;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.jscience.apps.framework.KillerAppBase;

import java.io.File;
import java.io.InputStream;

/**
 * Professional Human Anatomy Viewer.
 * <p>
 * Visualizes 3D anatomical models (STL) with layered exploration.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanAnatomyApp extends KillerAppBase {

    private Group root3D;
    private PerspectiveCamera camera;
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Translate translateZ = new Translate(0, 0, -500);

    private double mouseOldX, mouseOldY;

    private VBox sidePanel;
    private Label selectionLabel;

    @Override
    protected String getAppTitle() {
        return i18n.get("anatomy.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        // 3D Scene Setup
        root3D = new Group();

        // Load Default Models (Skull & Mandible) from resources
        loadDefaultModel("models/Skull.stl", "Skull", Color.BEIGE);
        loadDefaultModel("models/Mandible.stl", "Mandible", Color.BEIGE);

        // Camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(rotateX, rotateY, translateZ);

        // Adjust initial camera for Skull view
        rotateX.setAngle(-10);
        translateZ.setZ(-300);

        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.web("#222222"));

        // Mouse Handling
        handleMouse(subScene);

        // UI Overlay
        BorderPane layout = new BorderPane();
        layout.setCenter(subScene);

        // Bind subscene size
        subScene.widthProperty().bind(layout.widthProperty().subtract(250));
        subScene.heightProperty().bind(layout.heightProperty());

        // Side Panel
        sidePanel = createSidePanel();
        layout.setRight(sidePanel);

        return layout;
    }

    private void loadDefaultModel(String resourcePath, String name, Color color) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Could not find resource: " + resourcePath);
                return;
            }
            MeshView mesh = StlMeshLoader.load(is);
            mesh.setMaterial(new PhongMaterial(color));
            mesh.setDrawMode(DrawMode.FILL);
            mesh.setId(name);
            root3D.getChildren().add(mesh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createSidePanel() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(250);
        box.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Label title = new Label("Anatomy Layers");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font(18));

        Button loadBtn = new Button("Load STL Model...");
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setOnAction(e -> loadModelAction());

        selectionLabel = new Label("Selected: None");
        selectionLabel.setTextFill(Color.YELLOW);
        selectionLabel.setWrapText(true);

        CheckBox skeletonCheck = new CheckBox("Skeleton");
        skeletonCheck.setSelected(true);
        skeletonCheck.setTextFill(Color.WHITE);

        CheckBox organsCheck = new CheckBox("Organs");
        organsCheck.setSelected(true);
        organsCheck.setTextFill(Color.WHITE);

        box.getChildren().addAll(title, loadBtn, new Separator(), selectionLabel, new Separator(), skeletonCheck,
                organsCheck);
        return box;
    }

    private void handleMouse(SubScene scene) {
        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();

            // Picking
            if (me.isPrimaryButtonDown()) {
                Node picked = me.getPickResult().getIntersectedNode();
                if (picked instanceof MeshView) {
                    selectionLabel.setText("Selected: " + (picked.getId() != null ? picked.getId() : "Unknown Part"));
                    ((MeshView) picked).setMaterial(new PhongMaterial(Color.GOLD));
                } else if (picked instanceof Box) {
                    selectionLabel.setText("Selected: Reference Body");
                } else {
                    selectionLabel.setText("Selected: None");
                }
            }
        });

        scene.setOnMouseDragged(me -> {
            double dx = me.getSceneX() - mouseOldX;
            double dy = me.getSceneY() - mouseOldY;

            if (me.isPrimaryButtonDown()) {
                rotateY.setAngle(rotateY.getAngle() + dx * 0.5);
                rotateX.setAngle(rotateX.getAngle() - dy * 0.5);
            }

            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnScroll((ScrollEvent se) -> {
            double z = translateZ.getZ();
            double factor = 1.1;
            if (se.getDeltaY() < 0) {
                translateZ.setZ(z * factor);
            } else {
                translateZ.setZ(z / factor);
            }
        });
    }

    private void loadModelAction() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Open Anatomy STL");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("STL Files", "*.stl"));
        File f = fc.showOpenDialog(sidePanel.getScene().getWindow());
        if (f != null) {
            try {
                MeshView mesh = StlMeshLoader.load(f);
                mesh.setMaterial(new PhongMaterial(Color.BEIGE));
                mesh.setDrawMode(DrawMode.FILL);
                mesh.setId(f.getName());
                root3D.getChildren().add(mesh);
                selectionLabel.setText("Loaded: " + f.getName());
            } catch (Exception e) {
                showError("Load Error", e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
