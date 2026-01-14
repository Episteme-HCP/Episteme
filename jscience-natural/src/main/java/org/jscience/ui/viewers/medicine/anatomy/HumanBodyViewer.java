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
import java.util.Stack;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 3D Human Body Anatomy Viewer.
 * Displays anatomical layers (skeleton, muscles, organs, skin) with Z-Anatomy features:
 * Selection, Centering, Descriptions, and Advanced Camera Controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanBodyViewer extends AbstractViewer {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Human Body Viewer (JavaFX)";
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
                    
                    // Sync Tree (3D -> Tree)
                    TreeItem<String> item = idToTreeItem.get(id);
                    // Try fallback
                    if (item == null) item = idToTreeItem.get(id.replace("_", " "));
                    if (item == null) item = idToTreeItem.get(sanitizeIdFor3D(id)); // Try sanitized version of itself?

                    if (item != null) {
                        expandAndSelect(item);
                    }
                } else {
                    titleLabel.setText("Unknown Part");
                    descriptionArea.setText("No ID found for this mesh.");
                }
            } else {
                titleLabel.setText("Human Body");
                descriptionArea.setText("Select a part to see its description.");
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
        
        this.setCenter(centerPane);

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
        this.setLeft(leftToolbar);

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
            
            // Filter knownParts AND hierarchy items
            java.util.List<String> suggestions = Stream.concat(knownParts.stream(), idToTreeItem.keySet().stream())
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
                // Find mesh with this ID
                // We don't have a direct map, we have to search the scene graph or maintain a map
                // For now, let's assume we can search recursivly or we find it in knownParts logic
                // Better: Map<String, MeshView> partMap?
                
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
        
        titleLabel = new Label("Human Body");
        titleLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 18px; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        descriptionArea = new TextArea("Select a part to view details.");
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: #b0b0b0; -fx-background-color: transparent;");
        VBox.setVgrow(descriptionArea, Priority.ALWAYS);

        
        hierarchyTree = new TreeView<>();
        hierarchyTree.setShowRoot(false);
        hierarchyTree.setStyle("-fx-background-color: #2b2b2b; -fx-control-inner-background: #2b2b2b; -fx-text-fill: white;");
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
                    
                    // Logic:
                    // If index == 0, it's a child of the current stack top.
                    if (index == 0) {
                        TreeItem<String> parent = stack.peek();
                        Platform.runLater(() -> parent.getChildren().add(newItem));
                        stack.push(newItem);
                        indexStack.push(index);
                    } else {
                        // Sibling or Uncle.
                        // We must pop until we find the predecessor (index - 1).
                        // Note: The stack contains the path. The top is the "previous sibling" if we are at the same level.
                        
                        // We need to find `index - 1` in the `indexStack`? 
                        // Actually, if we are valid, the stack top should satisfy some condition.
                        // Simplified Logic based on Z-Anatomy structure:
                        // The stack represents the active branch.
                        // We pop items that are "finished".
                        // A item is finished if the next item is its sibling (index == self.index + 1) NO, 
                        // If next item is sibling, the PREVIOUS item (on top of stack) is finished.
                        
                        while (!indexStack.isEmpty() && indexStack.peek() >= index) {
                            stack.pop();
                            indexStack.pop();
                        }
                        
                        // If logic holds, now stack.peek() is the PARENT for this new index?
                        // No, if I popped the previous sibling (index-1), stack.peek() is the PARENT.
                        // Wait.
                        // Seq: A(0), B(1).
                        // Stack: A(0). Next B(1).
                        // A(0) < 1. Loop doesn't run? 
                        // IF A(0) < 1, we DON'T pop? That means B(1) becomes child of A(0)? WRONG. Sibling.
                        
                        // Correct Logic:
                        // We need to pop until the stack top is the PARENT.
                        // The parent is the one whose last child had index = index - 1.
                        // But we don't store that.
                        
                        // Let's use the property: Sibling replaces Sibling on Stack.
                        // If index > 0:
                        //   Pop the previous sibling (which should have index = index - 1).
                        //   Add new item to the *new* stack top (which is the parent).
                        //   Push new item.
                        
                        // Robustness: What if we jump levels?
                        // We pop until we satisfy the structure.
                        
                        // Re-evaluating:
                        // A(0). Stack [Root, A(0)].
                        // B(1). B is sibling of A.
                        // We need to Pop A. Stack [Root]. Add B to Root. Push B. Stack [Root, B(1)].
                        // C(0). C is child of B. Add C to B. Push C. Stack [Root, B, C(0)].
                        // D(0). D is child of C? No, usually indices restart at 0 for new level.
                        
                        // My loop `while (peek >= index)`:
                        // A(0) vs 1. 0 < 1. False.
                        // So I need `while (peek >= index - 1)` ?
                        // A(0) vs 0. True. Pop A. Stack [Root]. OK.
                        
                        // Let's try:
                        // A(0) -> [Root, A(0)]
                        // B(1). Need to pop A(0).
                        // Condition `lastIndex == index - 1`.
                        // If `peek() == index - 1`, we pop it, then add to the *next* peek (parent).
                        
                        while (!stack.isEmpty() && !indexStack.isEmpty()) {
                             int topIndex = indexStack.peek();
                             if (topIndex == index - 1) {
                                  // Found the sibling. Pop it to get to parent.
                                  stack.pop();
                                  indexStack.pop();
                                  break; 
                             } else if (topIndex >= index) {
                                  // We are too deep (maybe returning from a deep branch).
                                  stack.pop();
                                  indexStack.pop();
                             } else {
                                  // topIndex < index - 1. This implies a gap? Or we looked too far?
                                  // Usually topIndex should match index-1 eventually.
                                  // Unless index is 0. But we handled index 0.
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
                     // loadingLabel.setVisible(false);
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
        // e.g. "Parietal bone.l" -> "Parietal_bone_l" or "ParietalBone_L"
        // This depends heavily on the FBX file.
        // For now, simple replacement
        return name.replace(" ", "_");
        // We will refine this as we validat against the actual IDs.
    }

    private void loadDefinition(String id) {
        // ID might be "Parietal bone.l". 
        // Definition might be "(Parietal bone)-FR.txt".
        
        // Strategy:
        // 1. Exact Name: "Parietal bone.l" -> "/.../Parietal bone.l.txt" (unlikely)
        // 2. Base name: "Parietal bone" -> "/.../Parietal bone-FR.txt"
        // 3. Parentheses: "(Parietal bone)-FR.txt"
        
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
             descriptionArea.setText("No description found for: " + cleanName);
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

}
