/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.geometry.csg.CSGViewer;

public class CSGDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Constructive Solid Geometry";
    }

    @Override
    public String getDescription() {
        return "Visualization of 3D boolean operations (Union, Difference, Intersection) on solid primitives.";
    }

    @Override
    public void show(Stage stage) {
        CSGViewer.show(stage);
    }
}
