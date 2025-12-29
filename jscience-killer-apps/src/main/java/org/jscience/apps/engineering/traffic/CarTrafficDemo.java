/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.engineering.traffic;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

/**
 * Demo Provider for Car Traffic Simulation.
 *
 * @author Silvere Martin-Michiellot
 */
public class CarTrafficDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public String getName() {
        return "Car Traffic Simulation";
    }

    @Override
    public String getDescription() {
        return "Simulates traffic flow and phantom jams using the Intelligent Driver Model (IDM).";
    }

    @Override
    public void show(Stage stage) {
        CarTrafficViewer.show(stage);
    }
}
