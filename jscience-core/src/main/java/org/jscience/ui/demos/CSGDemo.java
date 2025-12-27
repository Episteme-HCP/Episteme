/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.geometry.csg.CSGViewer;

import org.jscience.ui.i18n.I18n;

public class CSGDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.csg");
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
