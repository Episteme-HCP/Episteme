/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.mechanics.PendulumViewer;

public class PendulumDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "Pendulum Simulation";
    }

    @Override
    public String getDescription() {
        return "Simple harmonic motion simulation with phase space trajectory plotting.";
    }

    @Override
    public void show(Stage stage) {
        PendulumViewer.show(stage);
    }
}
