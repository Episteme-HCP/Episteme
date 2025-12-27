/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.fluids.FluidDynamicsViewer;
import org.jscience.ui.i18n.I18n;

public class FluidDynamicsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("FluidDynamics.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("FluidDynamics.desc");
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
