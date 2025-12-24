/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.geology.EarthquakeMapViewer;

public class EarthquakeDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Earth Sciences";
    }

    @Override
    public String getName() {
        return "Earthquake Map";
    }

    @Override
    public String getDescription() {
        return "Seismic activity visualization showing 'Ring of Fire' data.";
    }

    @Override
    public void show(Stage stage) {
        EarthquakeMapViewer.show(stage);
    }
}
