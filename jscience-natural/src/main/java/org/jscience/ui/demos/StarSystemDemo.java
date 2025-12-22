/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.astronomy.StarSystemViewer;

public class StarSystemDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "Star System Viewer";
    }

    @Override
    public String getDescription() {
        return "3D solar system visualization with ephemeris-based planet positions.";
    }

    @Override
    public void show(Stage stage) {
        new StarSystemViewer().start(stage);
    }
}
