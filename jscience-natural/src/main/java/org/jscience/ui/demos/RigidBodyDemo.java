/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.mechanics.RigidBodyViewer;

public class RigidBodyDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return "Rigid Body Mechanics";
    }

    @Override
    public String getDescription() {
        return "2D physics simulation of colliding circles with impulse response, mass properties, and elastic bouncing.";
    }

    @Override
    public void show(Stage stage) {
        RigidBodyViewer.show(stage);
    }
}
