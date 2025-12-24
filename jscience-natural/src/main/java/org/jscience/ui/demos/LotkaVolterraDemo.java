/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.ecology.LotkaVolterraViewer;

public class LotkaVolterraDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Lotka-Volterra Population Dynamics";
    }

    @Override
    public String getDescription() {
        return "Predator-prey simulation showing oscillating population cycles and phase-space trajectories.";
    }

    @Override
    public void show(Stage stage) {
        LotkaVolterraViewer.show(stage);
    }
}
