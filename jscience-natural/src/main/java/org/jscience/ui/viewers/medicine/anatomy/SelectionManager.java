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
