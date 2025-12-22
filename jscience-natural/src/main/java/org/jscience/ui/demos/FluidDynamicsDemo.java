/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.fluids.FluidDynamicsViewer;

public class FluidDynamicsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "Fluid Dynamics";
    }

    @Override
    public String getDescription() {
        return "Lattice Boltzmann simulation of fluid flow.";
    }

    @Override
    public void show(Stage stage) {
        // Assuming FluidDynamicsViewer has a static show or similar, or instantiate
        // Checking usage: usually new FluidDynamicsViewer().start(stage);
        // But need to verify class exists and has no-arg constructor/start.
        // Assuming standard pattern for now.
        new FluidDynamicsViewer().start(stage);
    }
}
