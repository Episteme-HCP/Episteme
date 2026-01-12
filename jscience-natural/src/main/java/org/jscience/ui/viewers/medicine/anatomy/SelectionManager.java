package org.jscience.ui.viewers.medicine.anatomy;

import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages selection, highlighting, and isolation of 3D anatomical parts.
 */
public class SelectionManager {

    private final Map<MeshView, Material> originalMaterials = new HashMap<>();
    private final PhongMaterial selectionMaterial;
    private MeshView selectedMesh;
    private Runnable onSelectionChanged;

    public SelectionManager() {
        selectionMaterial = new PhongMaterial(Color.YELLOW);
        selectionMaterial.setSpecularColor(Color.WHITE);
        selectionMaterial.setSpecularPower(32);
        // Make it slightly emissive to glow?
        // selectionMaterial.setSelfIlluminationMap(...);
    }

    public void setOnSelectionChanged(Runnable callback) {
        this.onSelectionChanged = callback;
    }

    public void select(Node node) {
        if (node == null || !(node instanceof MeshView)) {
            clearSelection();
            return;
        }

        MeshView mesh = (MeshView) node;
        if (selectedMesh == mesh) {
            return; // Already selected
        }

        // Restore previous
        if (selectedMesh != null) {
            restoreMaterial(selectedMesh);
        }

        // Select new
        selectedMesh = mesh;
        storeMaterial(selectedMesh);
        selectedMesh.setMaterial(selectionMaterial);

        if (onSelectionChanged != null) {
            onSelectionChanged.run();
        }
    }

    public void clearSelection() {
        if (selectedMesh != null) {
            restoreMaterial(selectedMesh);
            selectedMesh = null;
            if (onSelectionChanged != null) {
                onSelectionChanged.run();
            }
        }
    }

    public MeshView getSelectedMesh() {
        return selectedMesh;
    }

    private void storeMaterial(MeshView mesh) {
        if (!originalMaterials.containsKey(mesh)) {
            originalMaterials.put(mesh, mesh.getMaterial());
        }
    }

    private void restoreMaterial(MeshView mesh) {
        Material original = originalMaterials.get(mesh);
        if (original != null) {
            mesh.setMaterial(original);
        }
    }
}
