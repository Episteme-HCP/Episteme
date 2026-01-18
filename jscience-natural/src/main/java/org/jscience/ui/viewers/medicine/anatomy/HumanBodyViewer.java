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

package org.jscience.ui.viewers.medicine.anatomy;

import javafx.application.Platform;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;

import org.jscience.ui.Parameter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import org.jscience.biology.loaders.FbxMeshReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.Stack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.text.MessageFormat;

/**
 * 3D Human Body Anatomy Viewer.
 * Displays anatomical layers (skeleton, muscles, organs, skin) with Z-Anatomy features:
 * Selection, Centering, Descriptions, and Advanced Camera Controls.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Aarseth, S. J. (2003). <i>Gravitational N-Body Simulations</i>. Cambridge University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanBodyViewer extends AbstractViewer {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.medicine", "Medicine");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.humanbodyviewer.name", "Human Body Viewer");
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
    private TreeView<String> hierarchyTree;
    private java.util.Set<String> knownParts = new java.util.HashSet<>();
    private Map<String, TreeItem<String>> idToTreeItem = new ConcurrentHashMap<>();
    private ComboBox<String> searchField;

    public HumanBodyViewer() {
        initUI();
    }

    private void initUI() {
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
        subScene.setFill(Color.web("#fdfbf7"));
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
                    
                    // Sync Tree (3D -> Tree)
                    TreeItem<String> item = idToTreeItem.get(id);
                    // Try fallback
                    if (item == null) item = idToTreeItem.get(id.replace("_", " "));
                    if (item == null) item = idToTreeItem.get(sanitizeIdFor3D(id)); // Try sanitized version of itself?

                    if (item != null) {
                        expandAndSelect(item);
                    }
                } else {
                    titleLabel.setText(I18n.getInstance().get("viewer.humanbodyviewer.unknown_part", "Unknown Part"));
                    descriptionArea.setText(I18n.getInstance().get("viewer.humanbodyviewer.no_id", "No ID found for this mesh."));
                }
            } else {
                titleLabel.setText(I18n.getInstance().get("viewer.humanbodyviewer.default_title", "Human Body"));
                descriptionArea.setText(I18n.getInstance().get("viewer.humanbodyviewer.default_desc", "Select a part to see its description."));
                hierarchyTree.getSelectionModel().clearSelection();
            }
        });

        // --- UI LAYOUT ---
        // Instead of creating new root BorderPane, we use 'this' since we are a BorderPane
        
        // CENTER: 3D View + Overlay
        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(subScene);
        subScene.widthProperty().bind(centerPane.widthProperty());
        subScene.heightProperty().bind(centerPane.heightProperty());
        
        loadingLabel = new Label(I18n.getInstance().get("viewer.humanbodyviewer.loading", "Loading Anatomy..."));
        loadingLabel.getStyleClass().add("info-panel");
        loadingLabel.getStyleClass().add("bg-panel"); // Replaced inline style: -fx-padding: 10; -fx-background-radius: 5;
        loadingLabel.setVisible(false); // Managed by async loader
        StackPane.setAlignment(loadingLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(loadingLabel, new Insets(10));
        centerPane.getChildren().add(loadingLabel);
        
        // Attribution Label (Bottom Right)
        Label attributionLabel = new Label(I18n.getInstance().get("viewer.humanbodyviewer.attribution", "Models: Z-Anatomy (CC BY-SA 4.0)"));
        attributionLabel.getStyleClass().add("description-label");
        attributionLabel.setStyle("-fx-font-size: 10px;");
        StackPane.setAlignment(attributionLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(attributionLabel, new Insets(10));
        centerPane.getChildren().add(attributionLabel);
        
        this.setCenter(centerPane);

        // LEFT: Toolbar (Layers + Actions)
        VBox leftToolbar = new VBox(10);
        leftToolbar.setPadding(new Insets(10));
        leftToolbar.getStyleClass().add("viewer-sidebar");
        leftToolbar.setPrefWidth(200);

        Label layersHeader = new Label(I18n.getInstance().get("viewer.humanbodyviewer.systems", "SYSTEMS"));
        layersHeader.getStyleClass().add("header-label");
        
        leftToolbar.getChildren().addAll(
                layersHeader,
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.skin", "Skin"), skinLayer, "/org/jscience/medicine/anatomy/models/Regions of human body100.fbx", Color.rgb(255, 218, 185, 0.4)),
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.muscles", "Muscles"), muscleLayer, "/org/jscience/medicine/anatomy/models/MuscularSystem100.fbx", Color.INDIANRED),
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.skeleton", "Skeleton"), skeletonLayer, "/org/jscience/medicine/anatomy/models/SkeletalSystem100.fbx", Color.IVORY),
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.organs", "Organs"), organLayer, "/org/jscience/medicine/anatomy/models/VisceralSystem100.fbx", Color.CORAL),
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.nervous", "Nervous"), nervousLayer, "/org/jscience/medicine/anatomy/models/NervousSystem100.fbx", Color.GOLD),
                createLayerWithLoading(I18n.getInstance().get("viewer.humanbodyviewer.layer.circulatory", "Circulatory"), circulatoryLayer, "/org/jscience/medicine/anatomy/models/CardioVascular41.fbx", Color.DARKRED),
                new Separator(),
                new Label(I18n.getInstance().get("viewer.humanbodyviewer.actions", "ACTIONS")),
                createButton(I18n.getInstance().get("viewer.humanbodyviewer.center_view", "Center View"), () -> {
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
                createButton(I18n.getInstance().get("viewer.humanbodyviewer.reset_camera", "Reset Camera"), () -> cameraController.reset())
        );
        this.setLeft(leftToolbar);

        // RIGHT: Description Panel
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.getStyleClass().add("viewer-sidebar");
        rightPanel.setPrefWidth(300);

        searchField = new ComboBox<>();
        searchField.setEditable(true);
        searchField.setPromptText(I18n.getInstance().get("viewer.humanbodyviewer.search.prompt", "Search..."));
        searchField.setMaxWidth(Double.MAX_VALUE);
        
        // Search Logic
        searchField.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                searchField.getItems().clear();
                searchField.hide();
                return;
            }
            
            // Filter knownParts AND hierarchy items
            List<String> suggestions = Stream.concat(knownParts.stream(), idToTreeItem.keySet().stream())
                .distinct()
                .filter(s -> s.toLowerCase().contains(newVal.toLowerCase()))
                .sorted()
                .limit(20)
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
                Node found = findNodeById(root3D, selected);
                if (found instanceof MeshView) {
                    selectionManager.select(found);
                    // Center
                    MeshView mesh = (MeshView) found;
                    javafx.geometry.Bounds b = mesh.localToScene(mesh.getBoundsInLocal());
                    javafx.geometry.Point3D center = new javafx.geometry.Point3D(
                        b.getMinX() + b.getWidth() / 2,
                        b.getMinY() + b.getHeight() / 2,
                        b.getMinZ() + b.getDepth() / 2
                    );
                    cameraController.centerOn(center);
                } else {
                    // Try Tree Item
                    TreeItem<String> item = idToTreeItem.get(selected);
                    if (item != null) {
                        expandAndSelect(item);
                        // This will trigger tree listener -> which will try to select 3D part via sanitizeId
                    }
                }
            }
        });
        
        titleLabel = new Label(I18n.getInstance().get("viewer.humanbodyviewer.default_title", "Human Body"));
        titleLabel.getStyleClass().add("header-label");
        titleLabel.getStyleClass().add("font-large"); // Replaced inline style: -fx-font-size: 18px;
        
        descriptionArea = new TextArea(I18n.getInstance().get("viewer.humanbodyviewer.default_desc", "Select a part to view details."));
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.getStyleClass().add("info-panel");
        VBox.setVgrow(descriptionArea, Priority.ALWAYS);

        
        hierarchyTree = new TreeView<>();
        hierarchyTree.setShowRoot(false);
        hierarchyTree.setShowRoot(false);
        VBox.setVgrow(hierarchyTree, Priority.ALWAYS);
        
        // Tree Selection Listener
        hierarchyTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String selected = newVal.getValue();
                titleLabel.setText(selected);
                loadDefinition(selected);
                
                // Try to select in 3D
                String meshId = sanitizeIdFor3D(selected);
                Node found = findNodeById(root3D, meshId);
                // Also try exact match
                if (found == null) found = findNodeById(root3D, selected);
                
                if (found instanceof MeshView) {
                    selectionManager.select(found);
                    // build3DBody(); // No, don't rebuild. 
                    
                    // Auto-center? Maybe optional.
                    // MeshView mesh = (MeshView) found;
                    // ... centering logic ...
                }
            }
        });

        rightPanel.getChildren().addAll(searchField, new Separator(), hierarchyTree, new Separator(), titleLabel, descriptionArea);
        this.setRight(rightPanel);

        // --- START LOADING ---
        build3DBody();
        loadHierarchy();
    }

    private Node createLayerWithLoading(String name, Group group, String fbxPath, Color fallbackColor) {
        HBox container = new HBox(5);
        container.setAlignment(Pos.CENTER_LEFT);
        
        CheckBox cb = new CheckBox(name);
        cb.getStyleClass().add("text-light");
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

    /**
     * Parses the hierarchy.txt file and populates the TreeView.
     * Format: Name.suffix;index
     * Logic: Index determines relationship (0=Child, >0=Sibling).
     */
    private void loadHierarchy() {
        Thread thread = new Thread(() -> {
            try {
                InputStream is = HumanBodyViewer.class.getResourceAsStream("/org/jscience/medicine/anatomy/z-anatomy/hierarchy.txt");
                if (is == null) {
                    System.err.println("Hierarchy file not found!");
                    return;
                }
                
                TreeItem<String> rootItem = new TreeItem<>("Root");
                Stack<TreeItem<String>> stack = new Stack<>();
                stack.push(rootItem);
                
                // Helper to track index of items in the stack
                Stack<Integer> indexStack = new Stack<>();
                indexStack.push(-1); // Root has no index really
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    
                    // Parse "Name;Index"
                    int semi = line.lastIndexOf(';');
                    if (semi == -1) continue;
                    
                    String name = line.substring(0, semi);
                    int index = Integer.parseInt(line.substring(semi + 1));
                    
                    TreeItem<String> newItem = new TreeItem<>(name);
                    // Populate Map
                    idToTreeItem.put(name, newItem);
                    idToTreeItem.put(sanitizeIdFor3D(name), newItem);
                    
                    if (index == 0) {
                        TreeItem<String> parent = stack.peek();
                        Platform.runLater(() -> parent.getChildren().add(newItem));
                        stack.push(newItem);
                        indexStack.push(index);
                    } else {
                        while (!stack.isEmpty() && !indexStack.isEmpty()) {
                             int topIndex = indexStack.peek();
                             if (topIndex == index - 1) {
                                  stack.pop();
                                  indexStack.pop();
                                  break; 
                             } else if (topIndex >= index) {
                                  stack.pop();
                                  indexStack.pop();
                             } else {
                                  break; 
                             }
                        }
                        
                        if (!stack.isEmpty()) {
                             TreeItem<String> parent = stack.peek();
                             Platform.runLater(() -> parent.getChildren().add(newItem));
                             stack.push(newItem);
                             indexStack.push(index);
                        }
                    }
                }
                
                Platform.runLater(() -> {
                     hierarchyTree.setRoot(rootItem);
                     rootItem.setExpanded(true);
                });
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    // Attempt to sanitize hierarchy name to match Fbx Node IDs
    private String sanitizeIdFor3D(String name) {
        return name.replace(" ", "_");
    }

    private void loadDefinition(String id) {
        String cleanName = id;
        // Remove suffixes .l, .r, .g, etc. if they exist and are preceded by dot
        if (cleanName.matches(".*\\.[a-z]+$")) {
             cleanName = cleanName.substring(0, cleanName.lastIndexOf('.'));
        }
        
        String descFolder = "/z-anatomy/descriptions/";
        
        // Try variants
        String[] variants = {
            id + "-FR.txt",
            cleanName + "-FR.txt",
            "(" + cleanName + ")-FR.txt",
            cleanName + ".txt"
        };
        
        String foundText = null;
        
        for (String v : variants) {
            String path = descFolder + v;
            try (InputStream is = HumanBodyViewer.class.getResourceAsStream(path)) {
                 if (is != null) {
                      foundText = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                                  .lines().collect(Collectors.joining("\n"));
                      break;
                 }
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (foundText != null) {
             descriptionArea.setText(foundText);
        } else {
             descriptionArea.setText(MessageFormat.format(I18n.getInstance().get("viewer.humanbodyviewer.no_description", "No description found for: {0}"), cleanName));
        }
    }
    
    private void logError(String model) {
        Platform.runLater(() -> System.err.println("Failed to load: " + model));
    }

    private void expandAndSelect(TreeItem<String> item) {
        if (item == null) return;
        
        // Expand parents
        TreeItem<String> parent = item.getParent();
        while (parent != null) {
            parent.setExpanded(true);
            parent = parent.getParent();
        }
        
        // Select and try to scroll
        hierarchyTree.getSelectionModel().select(item);
        int row = hierarchyTree.getRow(item);
        if (row >= 0) {
            hierarchyTree.scrollTo(row);
        }
    }


    @Override public String getDescription() { return I18n.getInstance().get("viewer.humanbodyviewer.desc", "3D Human Anatomy Viewer"); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.humanbodyviewer.longdesc", "Detailed 3D visualization of human anatomy including skeleton, muscles, organs, and nervous system."); }
    @Override public List<Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}

