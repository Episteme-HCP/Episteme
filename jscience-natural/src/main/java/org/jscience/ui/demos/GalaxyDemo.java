/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.astronomy.GalaxyViewer;

public class GalaxyDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "Galaxy Viewer";
    }

    @Override
    public String getDescription() {
        return "Particle simulation of a spiral galaxy with 15,000 density-wave stars.";
    }

    @Override
    public void show(Stage stage) {
        GalaxyViewer.show(stage);
    }
}
