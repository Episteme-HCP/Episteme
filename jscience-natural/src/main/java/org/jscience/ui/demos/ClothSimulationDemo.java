/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.computing.simulation.ClothSimulationViewer;

public class ClothSimulationDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "3D Cloth Simulation";
    }

    @Override
    public String getDescription() {
        return "Mass-spring particle system simulating a 3D hanging cloth with gravity and damping controls.";
    }

    @Override
    public void show(Stage stage) {
        ClothSimulationViewer.show(stage);
    }
}
