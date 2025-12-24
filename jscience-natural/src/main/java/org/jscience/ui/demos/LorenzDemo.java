/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.chaos.LorenzViewer;

public class LorenzDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Lorenz Chaos Attractor";
    }

    @Override
    public String getDescription() {
        return "Visualizes deterministic chaos through the Lorenz system of differential equations.";
    }

    @Override
    public void show(Stage stage) {
        LorenzViewer.show(stage);
    }
}
