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

package org.jscience.ui.viewers.medicine.anatomy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.biology.loaders.FbxMeshReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 3D Human Body Anatomy Viewer.
 * Displays anatomical layers (skeleton, muscles, organs, skin) with Z-Anatomy features:
 * Selection, Centering, Descriptions, and Advanced Camera Controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanBodyViewer extends Application implements org.jscience.ui.ViewerProvider {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Human Body Viewer (JavaFX)";
    }

    @Override
    public void show(Stage stage) {
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3D Scene Components
    private Group root3D;
    private SubScene subScene;
    private CameraController cameraController;
    private SelectionManager selectionManager;

    // Layers
    private Group skinLayer = new Group();
    private Group muscleLayer = new Group();
    private Group skeletonLayer = new Group();
    private Group organLayer = new Group();
    private Group nervousLayer = new Group();
    private Group circulatoryLayer = new Group();

    // UI Components
    private Label titleLabel;
    private TextArea descriptionArea;
    private Label loadingLabel;
    private ComboBox<String> searchField;
    private java.util.Set<String> knownParts = new java.util.HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JScience - Human Body Viewer");

        // --- 3D SETUP ---
        root3D = new Group();
        root3D.getChildren().addAll(skeletonLayer, muscleLayer, organLayer, nervousLayer, circulatoryLayer, skinLayer);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-500);

        // Camera Controller handles rotation/zoom/pan
        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(camera);
        root3D.getChildren().add(cameraGroup);
        
        cameraController = new CameraController(camera, cameraGroup);
        selectionManager = new SelectionManager();

        subScene = new SubScene(root3D, 1024, 768, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.rgb(30, 30, 30));
        subScene.setCamera(camera);

        // --- EVENT HANDLING ---
        cameraController.handleMouseEvents(subScene);
        
        // Picking Logic
        subScene.setOnMouseClicked(e -> {
            if (e.isStillSincePress()) {
                javafx.scene.Node picked = e.getPickResult().getIntersectedNode();
                // Find nearest MeshView parent if picked specific geometry
                while (picked != null && !(picked instanceof MeshView) && picked != root3D) {
                     picked = picked.getParent();
                }
                
                if (picked instanceof MeshView) {
                    selectionManager.select(picked);
                } else {
                    selectionManager.clearSelection();
                }
            }
        });

        // Update UI on selection
        selectionManager.setOnSelectionChanged(() -> {
            MeshView mesh = selectionManager.getSelectedMesh();
            if (mesh != null) {
                String id = mesh.getId();
                if (id != null) {
                    titleLabel.setText(id);
                    loadDefinition(id);
                } else {
                    titleLabel.setText("Unknown Part");
                    descriptionArea.setText("No ID found for this mesh.");
                }
            } else {
                titleLabel.setText("Human Body");
                descriptionArea.setText("Select a part to see its description.");
            }
        });

        // --- UI LAYOUT ---
        BorderPane root = new BorderPane();
        
        // CENTER: 3D View + Overlay
        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(subScene);
        subScene.widthProperty().bind(centerPane.widthProperty());
        subScene.heightProperty().bind(centerPane.heightProperty());
        
        loadingLabel = new Label("Loading Anatomy...");
        loadingLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 10; -fx-background-radius: 5;");
        loadingLabel.setVisible(false); // Managed by async loader
        StackPane.setAlignment(loadingLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(loadingLabel, new Insets(10));
        centerPane.getChildren().add(loadingLabel);
        
        // Attribution Label (Bottom Right)
        Label attributionLabel = new Label("Models: Z-Anatomy (CC BY-SA 4.0)");
        attributionLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-background-color: rgba(0,0,0,0.3); -fx-padding: 5; -fx-font-size: 10px;");
        StackPane.setAlignment(attributionLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(attributionLabel, new Insets(10));
        centerPane.getChildren().add(attributionLabel);
        
        root.setCenter(centerPane);

        // LEFT: Toolbar (Layers + Actions)
        VBox leftToolbar = new VBox(10);
        leftToolbar.setPadding(new Insets(10));
        leftToolbar.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #3f3f3f; -fx-border-width: 0 1 0 0;");
        leftToolbar.setPrefWidth(200);

        Label layersHeader = new Label("SYSTEMS");
        layersHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        leftToolbar.getChildren().addAll(
                layersHeader,
                createLayerWithLoading("Skin", skinLayer, "/org/jscience/medicine/anatomy/models/Regions of human body100.fbx", Color.rgb(255, 218, 185, 0.4)),
                createLayerWithLoading("Muscles", muscleLayer, "/org/jscience/medicine/anatomy/models/MuscularSystem100.fbx", Color.INDIANRED),
                createLayerWithLoading("Skeleton", skeletonLayer, "/org/jscience/medicine/anatomy/models/SkeletalSystem100.fbx", Color.IVORY),
                createLayerWithLoading("Organs", organLayer, "/org/jscience/medicine/anatomy/models/VisceralSystem100.fbx", Color.CORAL),
                createLayerWithLoading("Nervous", nervousLayer, "/org/jscience/medicine/anatomy/models/NervousSystem100.fbx", Color.GOLD),
                createLayerWithLoading("Circulatory", circulatoryLayer, "/org/jscience/medicine/anatomy/models/CardioVascular41.fbx", Color.DARKRED),
                new Separator(),
                new Label("ACTIONS"),
                createButton("Center View", () -> {
                   if (selectionManager.getSelectedMesh() != null) {
                       // Center on selection
                       MeshView mesh = selectionManager.getSelectedMesh();
                       javafx.geometry.Bounds b = mesh.localToScene(mesh.getBoundsInLocal());
                       javafx.geometry.Point3D center = new javafx.geometry.Point3D(
                           b.getMinX() + b.getWidth() / 2,
                           b.getMinY() + b.getHeight() / 2,
                           b.getMinZ() + b.getDepth() / 2
                       );
                       cameraController.centerOn(center);
                       System.out.println("Centering on " + mesh.getId() + " at " + center);
                   } else {
                       cameraController.reset();
                   }
                }),
                createButton("Reset Camera", () -> cameraController.reset())
        );
        root.setLeft(leftToolbar);

        // RIGHT: Description Panel
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #3f3f3f; -fx-border-width: 0 0 0 1;");
        rightPanel.setPrefWidth(300);

        searchField = new ComboBox<>();
        searchField.setEditable(true);
        searchField.setPromptText("Search...");
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white;");
        
        // Search Logic
        // Search Logic
        searchField.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                searchField.getItems().clear();
                searchField.hide();
                return;
            }
            
            // Filter knownParts
            java.util.List<String> suggestions = knownParts.stream()
                .filter(s -> s.toLowerCase().contains(newVal.toLowerCase()))
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
                
            Platform.runLater(() -> {
                if (!suggestions.isEmpty()) {
                    searchField.getItems().setAll(suggestions);
                    if (!searchField.isShowing()) {
                        searchField.show();
                    }
                } else {
                    searchField.getItems().clear();
                    searchField.hide();
                }
            });
        });
        
        searchField.setOnAction(e -> {
            String selected = searchField.getValue();
            if (selected == null && !searchField.getEditor().getText().isEmpty()) {
                selected = searchField.getEditor().getText();
            }
            
            if (selected != null) {
                // Find mesh with this ID
                // We don't have a direct map, we have to search the scene graph or maintain a map
                // For now, let's assume we can search recursivly or we find it in knownParts logic
                // Better: Map<String, MeshView> partMap?
                
                Node found = findNodeById(root3D, selected);
                if (found instanceof MeshView) {
                    selectionManager.select(found);
                    // Center?
                    MeshView mesh = (MeshView) found;
                    javafx.geometry.Bounds b = mesh.localToScene(mesh.getBoundsInLocal());
                    javafx.geometry.Point3D center = new javafx.geometry.Point3D(
                        b.getMinX() + b.getWidth() / 2,
                        b.getMinY() + b.getHeight() / 2,
                        b.getMinZ() + b.getDepth() / 2
                    );
                    cameraController.centerOn(center);
                }
            }
        });
        
        titleLabel = new Label("Human Body");
        titleLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 18px; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        descriptionArea = new TextArea("Select a part to view details.");
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: #b0b0b0; -fx-background-color: transparent;");
        VBox.setVgrow(descriptionArea, Priority.ALWAYS);

        rightPanel.getChildren().addAll(searchField, new Separator(), titleLabel, descriptionArea);
        root.setRight(rightPanel);

        // --- SCENE SETUP ---
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(HumanBodyViewer.class.getResource("/org/jscience/ui/theme.css").toExternalForm()); // Ensure we have a dark theme
        
        primaryStage.setScene(scene);
        primaryStage.show();

        // --- START LOADING ---
        build3DBody();
    }

    private Node createLayerWithLoading(String name, Group group, String fbxPath, Color fallbackColor) {
        HBox container = new HBox(5);
        container.setAlignment(Pos.CENTER_LEFT);
        
        CheckBox cb = new CheckBox(name);
        cb.setTextFill(Color.WHITE);
        cb.setSelected(false); // Start unchecked
        cb.setDisable(true); // Start disabled
        cb.selectedProperty().addListener((obs, old, val) -> group.setVisible(val));
        
        ProgressIndicator pi = new ProgressIndicator();
        pi.setMaxSize(16, 16);
        
        container.getChildren().addAll(cb, pi);
        
        // Trigger load
        loadFbxModelAsync(fbxPath, group, fallbackColor, 
            () -> { // On Success
                cb.setDisable(false);
                cb.setSelected(true); // Check when loaded
                container.getChildren().remove(pi);
            },
            () -> { // On Error
                container.getChildren().remove(pi);
                cb.setDisable(true);
                cb.setText(name + " (Error)");
                logError(fbxPath);
            }
        );
        
        return container;
    }
    
    // Kept generic simple one if needed elsewhere
    private CheckBox createLayerToggle(String name, Group group) {
        CheckBox cb = new CheckBox(name);
        cb.setSelected(true);
        cb.setTextFill(Color.WHITE);
        cb.selectedProperty().addListener((obs, old, val) -> group.setVisible(val));
        return cb;
    }
    
    private Button createButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void build3DBody() {
        // Loading is now triggered by the UI creation
        loadingLabel.setVisible(true); // Global loading status could still be used or removed
    }

    private void loadFbxModelAsync(String resourcePath, Group targetGroup, Color fallbackColor, Runnable onSuccess, Runnable onError) {
        Thread thread = new Thread(() -> {
            try {
                // FbxMeshReader handles scaling, pivots, ID assignment
                Group model = FbxMeshReader.loadResource(resourcePath);
                
                // Helper to apply materials if FBX has none
                applyMaterialRecursive(model, new PhongMaterial(fallbackColor));

                Platform.runLater(() -> {
                    targetGroup.getChildren().add(model);
                    loadingLabel.setText(loadingLabel.getText().replace("...", ".") + ".");
                    
                    // Index parts
                    indexPartsRecursive(model);
                    
                    if (onSuccess != null) onSuccess.run();
                });
            } catch (Exception e) {
                System.err.println("Async Load Failed: " + resourcePath);
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (onError != null) onError.run();
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    private void indexPartsRecursive(Node node) {
        if (node instanceof MeshView) {
            String id = node.getId();
            if (id != null && !id.isEmpty()) {
                knownParts.add(id);
            }
        } else if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                indexPartsRecursive(child);
            }
        }
    }

    private void applyMaterialRecursive(javafx.scene.Node node, PhongMaterial material) {
        if (node instanceof MeshView) {
            ((MeshView) node).setMaterial(material);
        } else if (node instanceof Group) {
            for (javafx.scene.Node child : ((Group) node).getChildren()) {
                applyMaterialRecursive(child, material);
            }
        }
    }
    
    private Node findNodeById(Node node, String id) {
        if (id.equals(node.getId())) return node;
        
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                Node found = findNodeById(child, id);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void loadDefinition(String id) {
        // Try to find the text file
        // ID might be "Femur", "Humerus", etc.
        // File: /org/jscience/medicine/anatomy/definitions/<ID>.txt
        String safeId = id.trim();
        String path = "/org/jscience/medicine/anatomy/definitions/" + safeId + ".txt";
        
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                String text = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));
                descriptionArea.setText(text);
            } else {
                descriptionArea.setText("No description available for " + safeId);
                // System.out.println("Missing definition: " + path);
            }
        } catch (Exception e) {
            descriptionArea.setText("Error loading description.");
            e.printStackTrace();
        }
    }
    
    private void logError(String model) {
        Platform.runLater(() -> System.err.println("Failed to load: " + model));
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void launch(Stage stage) {
        try {
            new HumanBodyViewer().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void launchMain(Stage stage) {
        launch(stage);
    }
}
