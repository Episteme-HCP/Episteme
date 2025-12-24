/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.geometry.GeometryBoardViewer;

public class GeometryBoardDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Interactive Geometry Board";
    }

    @Override
    public String getDescription() {
        return "Construct geometric figures like lines, circles, and points to explore Euclidean theorems.";
    }

    @Override
    public void show(Stage stage) {
        GeometryBoardViewer.show(stage);
    }
}
