/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.chemistry.MolecularViewer;

public class MolecularViewerDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Chemistry";
    }

    @Override
    public String getName() {
        return "Molecular Viewer";
    }

    @Override
    public String getDescription() {
        return "3D visualization of molecules including DNA, proteins, and small molecules with CPK coloring.";
    }

    @Override
    public void show(Stage stage) {
        MolecularViewer.show(stage);
    }
}
